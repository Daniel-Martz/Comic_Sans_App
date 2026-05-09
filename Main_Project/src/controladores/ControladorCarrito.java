package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.producto.LineaProductoVenta;
import modelo.usuario.ClienteRegistrado;
import modelo.solicitud.SolicitudPedido;
import vista.userPanels.CarritoPanel;
import vista.clienteWindows.VentanaPagoPedidoWindow;
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

        if ("PROCESS_ORDER".equals(comando)) {
            procesarPedido();
            return;
        }
        
        if (comando != null && comando.startsWith("PAY_PEDIDO_")) {
            if (e.getSource() instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) e.getSource();
                SolicitudPedido pedido = (SolicitudPedido) btn.getClientProperty("pedido");
                if (pedido != null) {
                    abrirVentanaPagoParaPedido(pedido);
                }
            }
            return;
        }

        if (comando != null && comando.startsWith("INFO_PEDIDO_")) {
            if (e.getSource() instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) e.getSource();
                SolicitudPedido pedido = (SolicitudPedido) btn.getClientProperty("pedido");
                if (pedido != null) {
                    // Reutilizamos la ventana de visualización de pedido implementada para empleados
                    vista.empleadoWindow.ViewOrderWindow dialog = new vista.empleadoWindow.ViewOrderWindow(mainFrame, pedido);
                    dialog.setVisible(true);
                }
            }
            return;
        }

        if (comando != null && comando.startsWith("CANCEL_PEDIDO_")) {
            if (e.getSource() instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) e.getSource();
                SolicitudPedido pedido = (SolicitudPedido) btn.getClientProperty("pedido");
                if (pedido != null) {
                    try {
                        Aplicacion.getInstancia().cancelarPedido(pedido);
                        vista.mostrarMensaje("Pedido cancelado correctamente.", "Pedido cancelado");
                        refrescarVista();
                    } catch (Exception ex) {
                        vista.mostrarMensaje("No se pudo cancelar el pedido: " + ex.getMessage(), "Error");
                    }
                }
            }
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

    private void procesarPedido() {
        if (clienteActual == null || clienteActual.getCarrito().getProductos().isEmpty()) {
            vista.mostrarMensaje("Your cart is empty. Add some products first!", "Empty Cart");
            return;
        }

        try {
            clienteActual.realizarPedido();
            vista.mostrarMensaje("Order processed successfully. Please pay it from the pending orders list.", "Order Processed");
            refrescarVista();
        } catch (Exception ex) {
            vista.mostrarMensaje("Error generating order: " + ex.getMessage(), "Error");
        }
    }

    private void abrirVentanaPagoParaPedido(SolicitudPedido pedido) {
        try {
            VentanaPagoPedidoWindow ventanaPago = new VentanaPagoPedidoWindow(
                    SwingUtilities.getWindowAncestor(vista), pedido);
            ControladorPagoPedido controladorPago = new ControladorPagoPedido(ventanaPago);
            ventanaPago.setControlador(controladorPago);
            ventanaPago.setVisible(true);

            refrescarVista();
        } catch (Exception ex) {
            vista.mostrarMensaje("Error opening payment: " + ex.getMessage(), "Error");
        }
    }

    public void refrescarVista() {
        modelo.usuario.Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();

        if (usuarioActual == null || !(usuarioActual instanceof ClienteRegistrado)) {
            this.clienteActual = null;
            vista.actualizarCarrito(new java.util.HashMap<>(), 0.0, this);
            vista.actualizarPedidos(new java.util.ArrayList<>(), this);
            return;
        }

        this.clienteActual = (ClienteRegistrado) usuarioActual;
        Map<LineaProductoVenta, Integer> productos = this.clienteActual.getCarrito().getProductos();
        double total = this.clienteActual.getCarrito().getPrecioProductos();
        vista.actualizarCarrito(productos, total, this);

        java.util.List<SolicitudPedido> pendientes = new java.util.ArrayList<>();
        for (SolicitudPedido p : this.clienteActual.getPedidos()) {
            if (p.getEstado() == modelo.solicitud.EstadoPedido.PENDIENTE_DE_PAGO) {
                pendientes.add(p);
            }
        }
        vista.actualizarPedidos(pendientes, this);
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