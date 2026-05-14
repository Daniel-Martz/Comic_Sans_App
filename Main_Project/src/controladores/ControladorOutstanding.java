package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.LineaProductoVenta;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;
import vista.clienteWindows.DiscountInfoWindow;
import vista.userPanels.OutstandingPanel;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Controlador de la vista de productos destacados.
 *
 * Muestra productos con buena valoración y permite ver detalles o añadir al
 * carrito si el usuario está logueado.
 */
public class ControladorOutstanding implements ActionListener {

    /** The vista. */
    private final OutstandingPanel vista;

    /**
     * Crea el controlador y carga inicialmente los productos destacados.
     *
     * @param vista panel donde se muestran los productos destacados
     */
    public ControladorOutstanding(OutstandingPanel vista) {
        this.vista = vista;
        buscarYActualizarOutstanding();
    }

    /**
     * Maneja acciones de la vista: añadir al carrito, ver detalles o ver info de descuento.
     *
     * @param e evento de acción recibido
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.startsWith("ADD_")) {
            String idStr = cmd.substring(4);
            try {
                int id = Integer.parseInt(idStr);
                LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p == null) {
                    vista.mostrarMensaje("Product not found.", "Error");
                    return;
                }

                Usuario u = modelo.aplicacion.Aplicacion.getInstancia().getUsuarioActual();
                if (!(u instanceof ClienteRegistrado)) {
                    vista.mostrarMensaje("You must be logged in as a customer to add products to the cart.", "Not logged in");
                    return;
                }

                ClienteRegistrado cliente = (ClienteRegistrado) u;
                cliente.añadirProductoACarrito(p, 1);
                vista.mostrarMensaje("Product added to cart.", "Added");

            } catch (NumberFormatException ex) {
                vista.mostrarMensaje("Invalid product id.", "Error");
            }
        } else if (cmd.startsWith("INFO_")) {
            String idStr = cmd.substring(5);
            try {
                int id = Integer.parseInt(idStr);
                LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p == null) {
                    vista.mostrarMensaje("Product not found.", "Error");
                    return;
                }
                
                Window parentWindow = SwingUtilities.getWindowAncestor(vista);
                vista.clienteWindows.VentanaDetallesProductoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoWindow(parentWindow, p);
                dialog.setVisible(true);
            } catch (NumberFormatException ex) {
                vista.mostrarMensaje("Invalid product id.", "Error");
            }
        } else if (cmd.startsWith("DESCINFO_")) {
            String idStr = cmd.substring(9);
            try {
                int id = Integer.parseInt(idStr);
                LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p != null) {
                    Window parentWindow = SwingUtilities.getWindowAncestor(vista);
                    DiscountInfoWindow dialog = new DiscountInfoWindow(parentWindow, p);
                    dialog.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                vista.mostrarMensaje("Invalid product id.", "Error");
            }
        }
    }

    /**
     * Busca en el catálogo los productos con valoración entre 4 y 5 y actualiza la vista.
     */
    public void buscarYActualizarOutstanding() {
        List<LineaProductoVenta> resultados = new java.util.ArrayList<>(Catalogo.getInstancia().getProductosNuevos());
        resultados = resultados.stream()
                .filter(p -> p.obtenerPuntuacionMedia() >= 4.0 && p.obtenerPuntuacionMedia() <= 5.0)
                .toList();
        vista.actualizarProductos(resultados, this);
    }
}
