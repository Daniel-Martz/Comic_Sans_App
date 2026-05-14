package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.LineaProductoVenta;
import modelo.producto.Pack;
import vista.empleadoPanel.CreatePackPanel;
import vista.empleadoWindow.AddPackDetailsWindow;
import vista.main.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// TODO: Auto-generated Javadoc
/**
 * Controlador para la creación de packs (paquetes) de productos.
 *
 * Permite seleccionar productos, ajustar cantidades y crear un Pack que se
 * añadirá al catálogo. Coordina la vista del panel y las ventanas de detalles.
 */
public class ControladorCreatePack implements ActionListener, ItemListener, ChangeListener {

    /** The vista. */
    private final CreatePackPanel vista;
    
    /** The main frame. */
    private final MainFrame mainFrame;
    
    /** The main controller. */
    private final MainController mainController;
    
    /** The seleccionados. */
    private final Map<LineaProductoVenta, Integer> seleccionados;

    /**
     * Inicializa el controlador y sus listeners básicos.
     *
     * @param vista panel donde se muestran los productos
     * @param mainFrame ventana principal
     * @param mainController controlador principal para navegación
     */
    public ControladorCreatePack(CreatePackPanel vista, MainFrame mainFrame, MainController mainController) {
        this.vista = vista;
        this.mainFrame = mainFrame;
        this.mainController = mainController;
        this.seleccionados = new HashMap<>();

        this.vista.setControladorInferior(this);
        this.vista.getBtnBack().addActionListener(e -> mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS));
        this.vista.addSearchListener(e -> recargar());
        
        // Auto-recargar al abrir la pestaña
        this.vista.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                recargar();
            }
        });
    }

    /**
     * Recarga la lista de productos que pueden añadirse al pack según el texto
     * de búsqueda. Excluye ya los packs para evitar packs dentro de packs.
     */
    public void recargar() {
        String prompt = vista.getSearchText().toLowerCase();
        
        List<LineaProductoVenta> todos = Catalogo.getInstancia().getProductosNuevos().stream().toList();
        List<LineaProductoVenta> sinPacks = new ArrayList<>();
        
        // Excluimos a los Packs para no meter packs dentro de packs
        for (LineaProductoVenta p : todos) {
            if (!(p instanceof Pack) && (prompt.isEmpty() || p.getNombre().toLowerCase().contains(prompt))) {
                sinPacks.add(p);
            }
        }
        vista.actualizarProductos(sinPacks, seleccionados, this, this);
        vista.updateSelectionInfo(seleccionados.size());
    }

    /**
     * Maneja eventos de acción desde la vista (por ejemplo "CONTINUE").
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.equals("CONTINUE")) {
            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "You must select at least one product for the pack.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            AddPackDetailsWindow dialog = new AddPackDetailsWindow(mainFrame);
            dialog.getBtnConfirm().addActionListener(ev -> {
                try {
                    String name = dialog.getNewName();
                    String desc = dialog.getNewDescription();
                    double price = Double.parseDouble(dialog.getNewPrice().replace(",", "."));
                    int stock = Integer.parseInt(dialog.getNewStock());
                    File photo = dialog.getSelectedPhotoFile();
                    
                    if (name.isEmpty() || desc.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Name and description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Pack newPack = new Pack(name, desc, photo, stock, price);
                    Map<LineaProductoVenta, Integer> prods = new HashMap<>(seleccionados);
                    
                    Catalogo.getInstancia().añadirPack(newPack, prods);
                    JOptionPane.showMessageDialog(dialog, "Pack created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid price or stock.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            dialog.setVisible(true);
        }
    }

    /**
     * Maneja cambios en los checkboxes de selección de productos.
     *
     * @param e evento de cambio de estado del item
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox chk) {
            String cmd = chk.getActionCommand();
            if (cmd != null && cmd.startsWith("SELECT_")) {
                int id = Integer.parseInt(cmd.substring(7));
                LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p != null) {
                    if (e.getStateChange() == ItemEvent.SELECTED) seleccionados.put(p, vista.getQuantity(id));
                    else seleccionados.remove(p);
                    vista.updateSelectionInfo(seleccionados.size());
                }
            }
        }
    }

    /**
     * Maneja cambios en los spinners de cantidad y actualiza la selección.
     *
     * @param e evento de cambio (spinner)
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSpinner spn) {
            String name = spn.getName();
            if (name != null && name.startsWith("SPN_")) {
                int id = Integer.parseInt(name.substring(4));
                LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p != null && seleccionados.containsKey(p)) {
                    seleccionados.put(p, (Integer) spn.getValue());
                }
            }
        }
    }
}