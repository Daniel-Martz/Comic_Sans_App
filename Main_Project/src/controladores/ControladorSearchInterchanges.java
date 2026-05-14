package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.producto.ProductoSegundaMano;
import modelo.usuario.ClienteRegistrado;
import vista.userPanels.SearchInterchangesPanel;

import javax.swing.*;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * Controlador para buscar productos disponibles para intercambio.
 *
 * Permite filtrar productos, seleccionarlos y proceder a crear una oferta con
 * los elementos seleccionados.
 */
public class ControladorSearchInterchanges implements ActionListener, ItemListener {

    /** The vista. */
    private final SearchInterchangesPanel vista;
    
    /** The main controller. */
    private final MainController mainController;
    
    /** The seleccionados. */
    private final Set<ProductoSegundaMano> seleccionados;

    /**
     * Crea el controlador y enlaza los listeners básicos de búsqueda y filtros.
     *
     * @param vista panel de búsqueda de intercambios
     * @param mainController controlador principal para abrir ventanas
     */
    public ControladorSearchInterchanges(SearchInterchangesPanel vista, MainController mainController) {
        this.vista = vista;
        this.mainController = mainController;
        this.seleccionados = new HashSet<>();
        // Enlazamos los botones fijos de abajo
        this.vista.setControladorInferior(this);

        // Enlazamos el buscador y los filtros
        this.vista.getHeaderPanel().addSearchListener(e -> cargarProductosDisponibles(this.vista.getHeaderPanel().getSearchText()));
        this.vista.getHeaderPanel().addFiltrosListener(e -> {
            this.mainController.abrirVentanaFiltros();
            // Al cerrarse la ventana modal de filtros, actualizamos la vista
            cargarProductosDisponibles(this.vista.getHeaderPanel().getSearchText());
        });

        cargarProductosDisponibles("");
        actualizarBarraEstado();
    }
    /**
     * Se llama cuando el usuario vuelve a entrar a la vista. 
     * Limpia la selección y refresca la lista.
     */
    public void recargar() {
        seleccionados.clear();
        vista.desmarcarTodos();
        cargarProductosDisponibles(this.vista.getHeaderPanel().getSearchText());
    }

    /**
     * Cargar productos disponibles.
     *
     * @param prompt the prompt
     */
    private void cargarProductosDisponibles(String prompt) {
        ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();
        cliente.actualizarOfertas(); // Limpiamos ofertas caducadas
        
        // Obtiene todos los validados que cumplan los filtros actuales y el texto buscado
        List<ProductoSegundaMano> todos = Catalogo.getInstancia().obtenerProductosIntercambioFiltrados(prompt);
        
        List<ProductoSegundaMano> ajenos = new ArrayList<>();
        for (ProductoSegundaMano p : todos) {
            // Descartamos nuestros propios productos y los que ya estén bloqueados en otros intercambios
            if (!p.getClienteProducto().equals(cliente) && !p.estaBloqueado()) {
                ajenos.add(p);
            }
        }
        vista.actualizarProductos(ajenos, this, this);
    }

    /**
     * Action performed.
     *
     * @param e the e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.equals("RESET")) {
            seleccionados.clear();
            vista.desmarcarTodos();
            actualizarBarraEstado();
        } else if (cmd.equals("PROCEED")) {
            mainController.mostrarMakeOffer(seleccionados);
        } else if (cmd.startsWith("INFO_")) {
            int id = Integer.parseInt(cmd.substring(5));
            ProductoSegundaMano p = Catalogo.getInstancia().buscarProductoIntercambio(id);
            if (p != null) {
                Window parentWindow = SwingUtilities.getWindowAncestor(vista);
                vista.clienteWindows.VentanaDetallesProductoSegundaManoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoSegundaManoWindow(parentWindow, p);
                dialog.setVisible(true);
            }
        }
    }

    /**
     * Item state changed.
     *
     * @param e the e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox chk) {
            String cmd = chk.getActionCommand();
            if (cmd != null && cmd.startsWith("SELECT_")) {
                int id = Integer.parseInt(cmd.substring(7));
                ProductoSegundaMano p = Catalogo.getInstancia().buscarProductoIntercambio(id);
                if (p != null) {
                    if (e.getStateChange() == ItemEvent.SELECTED) seleccionados.add(p);
                    else seleccionados.remove(p);
                    actualizarBarraEstado();
                }
            }
        }
    }

    /**
     * Actualizar barra estado.
     */
    private void actualizarBarraEstado() {
        double total = 0.0;
        for (ProductoSegundaMano p : seleccionados) {
            if (p.getDatosValidacion() != null) {
                total += p.getDatosValidacion().getPrecioEstimadoProducto();
            }
        }
        vista.updateSelectionInfo(seleccionados.size(), total);
    }
}