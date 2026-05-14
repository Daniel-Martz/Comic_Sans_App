package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.*;
import vista.main.MainFrame;
import vista.empleadoPanel.AddProductsPanel;
import vista.empleadoWindow.AddProductManuallyWindow;
import vista.empleadoWindow.LoadFromFileWindow;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * Controlador para la pantalla de añadir productos.
 * Este controlador solo conecta botones y
 * realiza conversiones simples antes de delegar en el modelo.
 */
public class ControladorAddProducts implements ActionListener {

    /** The main frame. */
    private final MainFrame mainFrame;
    
    /** The main controller. */
    private final MainController mainController;

    /** The add products panel. */
    private AddProductsPanel addProductsPanel;
    
    /** The window add single. */
    private AddProductManuallyWindow windowAddSingle;

    /**
     * Crea el controlador con las referencias a la ventana principal y al
     * controlador principal.
     *
     * @param mainFrame ventana principal de la aplicación
     * @param mainController controlador principal usado para navegar entre paneles
     */
    public ControladorAddProducts(MainFrame mainFrame, MainController mainController) {
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.addProductsPanel = mainFrame.getAddProductsPanel();

        registrarListeners();
    }

    /**
     * Registra los listeners en los componentes de la vista relacionados con
     * añadir productos (botones para añadir manualmente y para cargar desde
     * fichero). No recibe parámetros y no devuelve nada.
     */
    private void registrarListeners() {
        // Desde AddProductsPanel
        addProductsPanel.getBtnAddManually().addActionListener(e -> {
            windowAddSingle = new AddProductManuallyWindow(mainFrame);
            windowAddSingle.getBtnBack().addActionListener(ev -> windowAddSingle.dispose());
            windowAddSingle.getBtnConfirmChanges().addActionListener(this);
            windowAddSingle.getBtnConfirmChanges().setActionCommand("CONFIRM_ADD");
            windowAddSingle.setVisible(true);
        });

        // BACK button in AddProducts header -> back to ManageProducts
        try {
            if (addProductsPanel.getBtnBackToManage() != null) {
                addProductsPanel.getBtnBackToManage().addActionListener(e ->
                    mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS)
                );
            }
        } catch (Exception ignored) {}

        addProductsPanel.getBtnLoadFromFile().addActionListener(e -> {
            LoadFromFileWindow win = new LoadFromFileWindow(mainFrame);
            win.getBtnSelectFile().addActionListener(ev -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select products file to load");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setAcceptAllFileFilterUsed(true);
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

                int res = chooser.showOpenDialog(mainFrame);
                if (res != JFileChooser.APPROVE_OPTION) {
                    return; // usuario canceló
                }

                File selected = chooser.getSelectedFile();
                if (selected == null) return;

                try {
                    List<LineaProductoVenta> añadidos = Catalogo.getInstancia().añadirProductosDesdeFichero(selected);
                    JOptionPane.showMessageDialog(mainFrame,
                            "Se han añadido " + añadidos.size() + " producto(s) desde: " + selected.getName(),
                            "Import successful", JOptionPane.INFORMATION_MESSAGE);
                    win.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Formato inválido o error en fichero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainFrame, "No se pudo leer el fichero: " + ex.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            win.setVisible(true);
        });
    }

    /**
     * Maneja acciones de los botones que delegaron este ActionListener.
     * Se usa la acción "CONFIRM_ADD" para confirmar la creación manual de
     * un producto.
     *
     * @param e evento de acción (se consulta actionCommand)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.equals("CONFIRM_ADD")) {
            añadirProducto();
        }
    }

    /**
     * Lee los campos de la ventana de añadir producto (si está abierta),
     * crea la instancia del producto apropiada y la añade al catálogo.
     *
     * Realiza parsing de números y muestra diálogos en caso de error.
     */
    private void añadirProducto() {
        if (windowAddSingle == null) return;
        
        try {
            // Validar que la foto sea obligatoria
            if (windowAddSingle.getSelectedPhotoFile() == null) {
                JOptionPane.showMessageDialog(mainFrame, 
                        "You must select a product photo. This field is mandatory.", 
                        "Missing Photo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String newName = windowAddSingle.getNewName();
            double newPrice = Double.parseDouble(windowAddSingle.getNewPrice().replace(",", "."));
            String newDescription = windowAddSingle.getNewDescription();
            int newStock = Integer.parseInt(windowAddSingle.getNewStock());
            
            String productType = windowAddSingle.getProductType();
            LineaProductoVenta nuevoProducto = null;
            
            if ("Comic".equals(productType)) {
                int numPaginas = Integer.parseInt(windowAddSingle.getNewNumPaginas());
                String autor = windowAddSingle.getNewAutor();
                String editorial = windowAddSingle.getNewEditorial();
                int año = Integer.parseInt(windowAddSingle.getNewAño());

                nuevoProducto = new Comic(newName, newDescription, windowAddSingle.getSelectedPhotoFile(), newStock, newPrice,
                        0, numPaginas, autor, editorial, año);

            } else if ("Figure".equals(productType)) {
                String marca = windowAddSingle.getNewMarca();
                String material = windowAddSingle.getNewMaterial();
                double dimX = Double.parseDouble(windowAddSingle.getNewDimX().replace(",", "."));
                double dimY = Double.parseDouble(windowAddSingle.getNewDimY().replace(",", "."));
                double dimZ = Double.parseDouble(windowAddSingle.getNewDimZ().replace(",", "."));

                nuevoProducto = new Figura(newName, newDescription, windowAddSingle.getSelectedPhotoFile(), newStock, newPrice,
                        0, marca, material, dimX, dimY, dimZ);

            } else if ("Board Game".equals(productType)) {
                int numJugadores = Integer.parseInt(windowAddSingle.getNewNumJugadores());
                int edadMin = Integer.parseInt(windowAddSingle.getNewEdadMin());
                int edadMax = Integer.parseInt(windowAddSingle.getNewEdadMax());
                modelo.producto.TipoJuegoMesa tipoJuego = windowAddSingle.getNewTipoJuego();

                nuevoProducto = new JuegoDeMesa(newName, newDescription, windowAddSingle.getSelectedPhotoFile(), newStock, newPrice,
                        numJugadores, edadMin, edadMax, tipoJuego);
            }
            
            if (nuevoProducto != null) {
                // Añadir categorías
                for (modelo.categoria.Categoria c : windowAddSingle.getCategoriasSeleccionadas()) {
                    nuevoProducto.añadirCategoria(c);
                }
                
                // Añadir al catálogo
                Catalogo.getInstancia().añadirProducto(nuevoProducto);
                
                JOptionPane.showMessageDialog(mainFrame, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                windowAddSingle.dispose();
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Please check numeric fields (ID, Price, Stock, Dimensions, etc.) and ensure they are valid.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}