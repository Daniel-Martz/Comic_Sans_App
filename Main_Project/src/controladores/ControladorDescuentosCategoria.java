package controladores;

import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import modelo.descuento.Descuento;
import vista.main.MainFrame;
import vista.userPanels.DescuentosCategoriaPanel;
import vista.empleadoPanel.AddCategoryDiscountWindow;
 
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controlador para aplicar descuentos por categoría.
 *
 * Lista las categorías de la tienda y abre la ventana para crear un descuento
 * aplicado a una categoría concreta.
 */
public class ControladorDescuentosCategoria implements ActionListener {

    private final DescuentosCategoriaPanel panel;
    private final MainFrame mainFrame;
    /**
     * Inicializa el controlador.
     *
     * @param panel panel de categorías/ descuentos
     * @param mainFrame ventana principal
     * @param mainController controlador principal
     */
    public ControladorDescuentosCategoria(DescuentosCategoriaPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
        this.panel.getColCategorias().addSearchListener(this);

        if (this.panel.getBtnBack() != null) {
            this.panel.getBtnBack().addActionListener(e -> mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS));
        }

        // Auto-recargar al abrir la pestaña
        this.panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatos();
            }
        });
    }

    /**
     * Carga y filtra las categorías mostradas según el campo de búsqueda.
     */
    private void cargarDatos() {
        String search = panel.getColCategorias().getSearchText().toLowerCase();
        List<Categoria> resultado = new ArrayList<>();

        Set<Categoria> todas = Catalogo.getInstancia().getCategoriasTienda();
        for (Categoria c : todas) {
            if (search.isEmpty() || c.getNombre().toLowerCase().contains(search)) {
                resultado.add(c);
            }
        }

        panel.getColCategorias().actualizarCategorias(resultado, this);
    }

    /**
     * Maneja eventos de la UI (buscar, añadir descuento sobre una categoría).
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;
        
        if (cmd.startsWith("SEARCH_")) {
            cargarDatos();
        } else if (cmd.startsWith("ADD_")) {
            String catName = cmd.substring(4);
            Categoria c = Catalogo.getInstancia().buscarCategoriaPorNombre(catName);
            if (c != null) {
                AddCategoryDiscountWindow win = new AddCategoryDiscountWindow(mainFrame, c, this);
                win.setVisible(true);
            }
        }
    }

    /**
     * Confirma y aplica un descuento a una categoría.
     *
     * @param c categoría a la que se aplica el descuento
     * @param d descuento que se aplicará
     */
    public void confirmarAñadirDescuento(Categoria c, Descuento d) {
        try {
            if (c.getDescuento() != null && !c.getDescuento().haCaducado()) {
                Catalogo.getInstancia().eliminarDescuento(c.getDescuento(), c);
            }
            Catalogo.getInstancia().añadirDescuento(d);
            Catalogo.getInstancia().aplicarDescuento(d, c);
            cargarDatos();
            JOptionPane.showMessageDialog(mainFrame, "Category discount applied successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error applying discount: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}