package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.producto.LineaProductoVenta;
import modelo.usuario.ClienteRegistrado;
import modelo.solicitud.SolicitudPedido;
import vista.userPanels.CarritoPanel;
import vista.userWindows.VentanaPagoPedido;
import vista.main.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.SwingUtilities;

public class ControladorCarrito implements ActionListener {

    private CarritoPanel vista;
    private MainFrame mainFrame; // Referencia por si hay que cambiar de pantalla entera
    private ClienteRegistrado clienteActual;

    public ControladorCarrito(CarritoPanel vista, MainFrame mainFrame) {
        this.vista = vista;
        this.mainFrame = mainFrame;
        
        // Enganchamos el botón de PAY NOW
        this.vista.setControladorPrincipal(this);
        
        // Primera carga visual del carrito
        refrescarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.equals("PAY_NOW")) {
            gestionarPago();
            return;
        }

        // Si es una acción dinámica de un producto (Empieza por INC_, DEC_ o DEL_)
        if (comando.length() > 4) {
            String accion = comando.substring(0, 4); // "INC_", "DEC_", "DEL_"
            String nombreProducto = comando.substring(4); // "Catan", etc.
            
            LineaProductoVenta productoImplicado = buscarProductoPorNombre(nombreProducto);
            if (productoImplicado != null) {
                switch (accion) {
                    case "INC_":
                        // Añadimos una unidad del producto al carrito
                        clienteActual.añadirProductoACarrito(productoImplicado, 1);
                        break;
                    case "DEC_":
                        // Eliminamos una unidad del producto del carrito
                        clienteActual.eliminarProductoDelCarrito(productoImplicado, 1);
                        break;
                    case "DEL_":
                        // Eliminamos completamente el producto: usamos la cantidad actual
                        Integer cantActual = clienteActual.getCarrito().getProductos().getOrDefault(productoImplicado, 0);
                        if (cantActual > 0) {
                            clienteActual.eliminarProductoDelCarrito(productoImplicado, cantActual);
                        }
                        break;
                }
                // Tras modificar el modelo, repintamos la vista
                refrescarVista();
            }
        }
    }

    private void gestionarPago() {
        if (clienteActual.getCarrito().getProductos().isEmpty()) {
            vista.mostrarMensaje("Your cart is empty. Add some products first!", "Empty Cart");
            return;
        }

        try {
            // El modelo genera la SolicitudPedido a partir del carrito
            SolicitudPedido pedido = clienteActual.realizarPedido();
            
            // Abrimos la ventana de pago que creaste en el paso anterior!
            VentanaPagoPedido ventanaPago = new VentanaPagoPedido(SwingUtilities.getWindowAncestor(vista), pedido);
            ControladorPagoPedido controladorPago = new ControladorPagoPedido(ventanaPago, pedido);
            ventanaPago.setControlador(controladorPago);
            ventanaPago.setVisible(true);

            // Tras cerrar la ventana de pago (si se vacía el carrito por compra exitosa), refrescamos
            refrescarVista();
            
        } catch (Exception ex) {
            vista.mostrarMensaje("Error generating order: " + ex.getMessage(), "Error");
        }
    }

    public void refrescarVista() {
        // 1. Pedimos a la app quién está conectado AHORA MISMO
        modelo.usuario.Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
        
        // 2. Si no hay nadie conectado, o no es un ClienteRegistrado (ej. es un Gestor)
        if (usuarioActual == null || !(usuarioActual instanceof ClienteRegistrado)) {
            // Le pasamos un mapa vacío a la vista
            // Aseguramos que la referencia de cliente en el controlador se resetee
            this.clienteActual = null;
            vista.actualizarCarrito(new java.util.HashMap<>(), 0.0, this);
            return; // Cortamos la ejecución aquí
        }

        // 3. Si llegamos aquí, ¡es un cliente de verdad!
        // Guardamos también en el atributo del controlador para que otros
        // métodos (por ejemplo gestionarPago o buscarProductoPorNombre)
        // puedan acceder al cliente actual sin NPE.
        this.clienteActual = (ClienteRegistrado) usuarioActual;

        Map<LineaProductoVenta, Integer> productos = this.clienteActual.getCarrito().getProductos();
        double total = this.clienteActual.getCarrito().getPrecioProductos();

        vista.actualizarCarrito(productos, total, this);
    }

    /** Método auxiliar para encontrar el objeto completo a partir de su nombre */
    private LineaProductoVenta buscarProductoPorNombre(String nombre) {
        for (LineaProductoVenta p : clienteActual.getCarrito().getProductos().keySet()) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        return null;
    }
}