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
    private MainFrame mainFrame;
    private ClienteRegistrado clienteActual;

    public ControladorCarrito(CarritoPanel vista, MainFrame mainFrame) {
        this.vista = vista;
        this.mainFrame = mainFrame;
        this.vista.setControladorPrincipal(this);
        refrescarVista();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("PAY_NOW".equals(comando)) {
            gestionarPago();
            return;
        }

        // ── Botón Volver ─────────────────────────────────────────────────
        if ("VOLVER".equals(comando)) {
            mainFrame.mostrarPanel(MainController.PANEL_MENU_PRINCIPAL);
            return;
        }

        // ── Acciones dinámicas de producto (INC_, DEC_, DEL_) ────────────
        if (comando != null && comando.length() > 4) {
            String accion = comando.substring(0, 4);
            String nombreProducto = comando.substring(4);

            LineaProductoVenta productoImplicado = buscarProductoPorNombre(nombreProducto);
            if (productoImplicado != null) {
                switch (accion) {
                    case "INC_":
                        try {
                            clienteActual.añadirProductoACarrito(productoImplicado, 1);
                        } catch (Exception ex) {
                            vista.mostrarMensaje(ex.getMessage(), "Error");
                        }
                        break;
                    case "DEC_":
                        clienteActual.eliminarProductoDelCarrito(productoImplicado, 1);
                        break;
                    case "DEL_":
                        Integer cantActual = clienteActual.getCarrito().getProductos()
                                .getOrDefault(productoImplicado, 0);
                        if (cantActual > 0) {
                            clienteActual.eliminarProductoDelCarrito(productoImplicado, cantActual);
                        }
                        break;
                }
                refrescarVista();
            }
        }
    }

    private void gestionarPago() {
        if (clienteActual == null || clienteActual.getCarrito().getProductos().isEmpty()) {
            vista.mostrarMensaje("Your cart is empty. Add some products first!", "Empty Cart");
            return;
        }

        try {
            // Creamos un pedido simulado solo para mostrar en la ventana de pago
            SolicitudPedido pedidoSimulado = new SolicitudPedido(clienteActual, clienteActual.getCarrito().getProductos());

            VentanaPagoPedido ventanaPago = new VentanaPagoPedido(
                    SwingUtilities.getWindowAncestor(vista), pedidoSimulado);
            ControladorPagoPedido controladorPago = new ControladorPagoPedido(ventanaPago);
            ventanaPago.setControlador(controladorPago);
            ventanaPago.setVisible(true);

            refrescarVista();
        } catch (Exception ex) {
            vista.mostrarMensaje("Error generating order: " + ex.getMessage(), "Error");
        }
    }

    public void refrescarVista() {
        modelo.usuario.Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();

        if (usuarioActual == null || !(usuarioActual instanceof ClienteRegistrado)) {
            this.clienteActual = null;
            vista.actualizarCarrito(new java.util.HashMap<>(), 0.0, this);
            return;
        }

        this.clienteActual = (ClienteRegistrado) usuarioActual;
        Map<LineaProductoVenta, Integer> productos = this.clienteActual.getCarrito().getProductos();
        double total = this.clienteActual.getCarrito().getPrecioProductos();
        vista.actualizarCarrito(productos, total, this);
    }

    private LineaProductoVenta buscarProductoPorNombre(String nombre) {
        if (clienteActual == null) return null;
        for (LineaProductoVenta p : clienteActual.getCarrito().getProductos().keySet()) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        return null;
    }
}