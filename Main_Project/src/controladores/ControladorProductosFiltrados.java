package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.LineaProductoVenta;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;
import vista.userPanels.ProductosFiltradosPanel;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la vista de productos filtrados.
 */
public class ControladorProductosFiltrados implements ActionListener {

    private final ProductosFiltradosPanel vista;
    private final vista.userWindows.FiltrosDialog filtrosDialog;

    public ControladorProductosFiltrados(ProductosFiltradosPanel vista, vista.userWindows.FiltrosDialog filtrosDialog) {
        this.vista = vista;
        this.filtrosDialog = filtrosDialog;
    }

    public ControladorProductosFiltrados(ProductosFiltradosPanel vista) {
        this.vista = vista;
        this.filtrosDialog = null;
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
                vista.userWindows.VentanaDetallesProducto dialog = new vista.userWindows.VentanaDetallesProducto(parentWindow, p);
                dialog.setVisible(true);
            } catch (NumberFormatException ex) {
                vista.mostrarMensaje("Invalid product id.", "Error");
            }
        }
    }

    /** Helper que consulta el catálogo y actualiza la vista con el prompt dado. */
    public void buscarYActualizar(String prompt) {
        List<LineaProductoVenta> resultados = Catalogo.getInstancia().obtenerProductosNuevosFiltrados(prompt);
        if (filtrosDialog != null) {
            resultados = resultados.stream().filter(filtrosDialog::cumpleFiltrosAvanzados).toList();
        }
        vista.actualizarProductos(resultados, this);
    }
}
