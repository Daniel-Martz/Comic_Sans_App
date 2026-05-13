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
 *
 * Atiende acciones de la lista filtrada: añadir al carrito, ver detalles o
 * ver información del descuento. También proporciona métodos para buscar y
 * actualizar la lista según filtros simples o avanzados.
 */
public class ControladorProductosFiltrados implements ActionListener {

    private final ProductosFiltradosPanel vista;
    private final vista.clienteWindows.FiltrosWindow filtrosDialog;

    /**
     * Constructor con diálogo de filtros avanzados.
     *
     * @param vista panel que muestra los productos
     * @param filtrosDialog ventana opcional con filtros avanzados
     */
    public ControladorProductosFiltrados(ProductosFiltradosPanel vista, vista.clienteWindows.FiltrosWindow filtrosDialog) {
        this.vista = vista;
        this.filtrosDialog = filtrosDialog;
    }

    /**
     * Constructor sin filtros avanzados.
     *
     * @param vista panel que muestra los productos
     */
    public ControladorProductosFiltrados(ProductosFiltradosPanel vista) {
        this.vista = vista;
        this.filtrosDialog = null;
    }

    /**
     * Maneja acciones de la vista: añadir al carrito, ver detalles o ver info
     * del descuento.
     *
     * @param e evento de acción
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
                    vista.clienteWindows.DiscountInfoWindow dialog = new vista.clienteWindows.DiscountInfoWindow(parentWindow, p);
                    dialog.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                vista.mostrarMensaje("Invalid product id.", "Error");
            }
        }
    }

    /**
     * Busca productos filtrados por texto y aplica filtros avanzados si existen.
     *
     * @param prompt texto de búsqueda
     */
    public void buscarYActualizar(String prompt) {
        List<LineaProductoVenta> resultados = Catalogo.getInstancia().obtenerProductosNuevosFiltrados(prompt);
        if (filtrosDialog != null) {
            resultados = resultados.stream().filter(filtrosDialog::cumpleFiltrosAvanzados).toList();
        }
        vista.actualizarProductos(resultados, this);
    }

    /**
     * Busca productos destacados (puntuación entre 4 y 5) y actualiza la vista.
     */
    public void buscarYActualizarOutstanding() {
        List<LineaProductoVenta> resultados = new java.util.ArrayList<>(Catalogo.getInstancia().getProductosNuevos());
        resultados = resultados.stream()
                .filter(p -> p.obtenerPuntuacionMedia() >= 4.0 && p.obtenerPuntuacionMedia() <= 5.0)
                .toList();
        vista.actualizarProductos(resultados, this);
    }
}
