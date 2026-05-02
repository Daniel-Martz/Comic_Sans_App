package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.*;
import vista.main.MainFrame;
import vista.empleadoPanel.AddProductsPanel;
import vista.empleadoPanel.LoadFromFileWindow;
import vista.empleadoPanel.AddProductManuallyWindow;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorAddProducts implements ActionListener {

    private final MainFrame mainFrame;
    private final MainController mainController;

    private AddProductsPanel addProductsPanel;
    private AddProductManuallyWindow windowAddSingle;

    public ControladorAddProducts(MainFrame mainFrame, MainController mainController) {
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.addProductsPanel = mainFrame.getAddProductsPanel();

        registrarListeners();
    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.equals("CONFIRM_ADD")) {
            añadirProducto();
        }
    }

    private void añadirProducto() {
        if (windowAddSingle == null) return;
        
        try {
            int newId = Integer.parseInt(windowAddSingle.getNewId());
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

                // Constructor: Comic(String nombre, String descripcion, File foto, int stock, double precio,
                //                      int unidadesVendidas, int numeroPaginas, String autor, String editorial, int añoPublicacion)
                nuevoProducto = new Comic(newName, newDescription, windowAddSingle.getSelectedPhotoFile(), newStock, newPrice,
                        0, numPaginas, autor, editorial, año);

            } else if ("Figure".equals(productType)) {
                String marca = windowAddSingle.getNewMarca();
                String material = windowAddSingle.getNewMaterial();
                double dimX = Double.parseDouble(windowAddSingle.getNewDimX().replace(",", "."));
                double dimY = Double.parseDouble(windowAddSingle.getNewDimY().replace(",", "."));
                double dimZ = Double.parseDouble(windowAddSingle.getNewDimZ().replace(",", "."));

                // Constructor: Figura(String nombre, String descripcion, File foto, int stock, double precio,
                //                      int unidadesVendidas, String marca, String material, double dimensionX, double dimensionY, double dimensionZ)
                nuevoProducto = new Figura(newName, newDescription, windowAddSingle.getSelectedPhotoFile(), newStock, newPrice,
                        0, marca, material, dimX, dimY, dimZ);

            } else if ("Board Game".equals(productType)) {
                int numJugadores = Integer.parseInt(windowAddSingle.getNewNumJugadores());
                int edadMin = Integer.parseInt(windowAddSingle.getNewEdadMin());
                int edadMax = Integer.parseInt(windowAddSingle.getNewEdadMax());
                modelo.producto.TipoJuegoMesa tipoJuego = windowAddSingle.getNewTipoJuego();

                // Constructor: JuegoDeMesa(String nombre, String descripcion, File foto, int stock, double precio,
                //                          int numeroJugadores, int edadMinima, int edadMaxima, TipoJuegoMesa tipo)
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