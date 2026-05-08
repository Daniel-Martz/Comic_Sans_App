package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.LineaProductoVenta;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;
import vista.userPanels.OutstandingPanel;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la vista de productos destacados.
 */
public class ControladorOutstanding implements ActionListener {

    private final OutstandingPanel vista;

    public ControladorOutstanding(OutstandingPanel vista) {
        this.vista = vista;
        buscarYActualizarOutstanding();
    }

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
        }
    }

    /** Helper que consulta el catálogo y actualiza la vista con los productos destacados (valoración entre 4 y 5). */
    public void buscarYActualizarOutstanding() {
        List<LineaProductoVenta> resultados = new java.util.ArrayList<>(Catalogo.getInstancia().getProductosNuevos());
        resultados = resultados.stream()
                .filter(p -> p.obtenerPuntuacionMedia() >= 4.0 && p.obtenerPuntuacionMedia() <= 5.0)
                .toList();
        vista.actualizarProductos(resultados, this);
    }
}
