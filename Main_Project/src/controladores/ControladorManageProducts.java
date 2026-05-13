package controladores;

import modelo.producto.Comic;
import modelo.producto.Figura;
import modelo.producto.JuegoDeMesa;
import modelo.producto.LineaProductoVenta;
import vista.main.MainFrame;
import vista.empleadoPanel.ManageProductsPanel;
import vista.empleadoPanel.ModifyProductsPanel;
import vista.empleadoWindow.ModifyAProductWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de productos (empleado).
 *
 * Coordina las vistas de "Manage Products" y "Modify Products", abre ventanas
 * auxiliares (modificar, seleccionar packs, descuentos) y gestiona la lógica
 * de guardar cambios en productos.
 */
public class ControladorManageProducts implements ActionListener {

    private final MainFrame mainFrame;
    private final MainController mainController;

    private ManageProductsPanel panelManage;
    private ModifyProductsPanel panelModifyList;
    private ModifyAProductWindow windowModifySingle;

    /**
     * Inicializa el controlador y registra los listeners de navegación.
     *
     * @param mainFrame ventana principal
     * @param mainController controlador principal (para navegar entre paneles)
     */
    public ControladorManageProducts(MainFrame mainFrame, MainController mainController) {
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.panelManage = mainFrame.getManageProductsPanel();
        this.panelModifyList = mainFrame.getModifyProductsPanel();

        registrarListeners();
    }

    /**
     * Registra los listeners de los botones y acciones de los paneles relacionados
     * con la gestión de productos.
     */
    private void registrarListeners() {
        // Desde el Menú Principal del Gestor -> Manage Products
        mainFrame.getMenuGestorPanel().addManageProductsListener(e -> 
            mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS)
        );

        // Desde Manage Products -> Add Products
        panelManage.addAddProductsListener(e -> 
            mainController.navegarA(MainFrame.PANEL_ADD_PRODUCTS)
        );

        // Desde Manage Products -> Modify Products
        panelManage.addModifyProductsListener(e -> {
            cargarProductos(""); // Carga todos la primera vez
            mainController.navegarA(MainFrame.PANEL_MODIFY_PRODUCTS);
        });

        // Desde Manage Products -> Manage Categories
        panelManage.addManageCategoriesListener(e -> {
            mainController.mostrarManageCategories();
        });
        
        // Desde Manage Products -> Discounts
        panelManage.addDiscountsListener(e -> {
            vista.empleadoPanel.SelectDiscountTypeWindow win = new vista.empleadoPanel.SelectDiscountTypeWindow(mainFrame);
            win.getBtnProducts().addActionListener(ev -> {
                win.dispose();
                mainController.navegarA(MainFrame.PANEL_DESCUENTOS);
            });
            win.getBtnCategories().addActionListener(ev -> {
                win.dispose();
                mainController.navegarA(MainFrame.PANEL_DESCUENTOS_CATEGORIA);
            });
            win.setVisible(true);
        });

        // Desde Manage Products -> Manage Packs
        panelManage.addManagePacksListener(e -> {
            vista.empleadoWindow.SelectManagePackOptionWindow win = new vista.empleadoWindow.SelectManagePackOptionWindow(mainFrame);
            win.getBtnAddPack().addActionListener(ev -> {
                win.dispose();
                mainController.navegarA(MainFrame.PANEL_CREATE_PACK);
            });
            win.getBtnModifyPack().addActionListener(ev -> {
                win.dispose();
                mainController.navegarA(MainFrame.PANEL_MODIFY_PACKS);
            });
            win.setVisible(true);
        });

        // Desde Modify Products -> Volver a HOME
        panelModifyList.getBtnBack().addActionListener(e -> 
            mainController.mostrarMenuPrincipal()
        );

        // Desde Modify Products -> Volver a ManageProducts (nuevo botón BACK junto a HOME)
        try {
            if (panelModifyList.getBtnBackManage() != null) {
                panelModifyList.getBtnBackManage().addActionListener(e ->
                    mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS)
                );
            }
        } catch (Exception ignored) {}

        // Escuchar el buscador del panel Modify Products
        panelModifyList.addSearchListener(e -> {
            cargarProductos(panelModifyList.getSearchText());
        });
    }

    /**
     * Recupera los productos desde el catálogo (modo gestión) y los clasifica
     * por tipo para actualizar la vista de modificación.
     *
     * @param prompt texto de búsqueda para filtrar por nombre
     */
    private void cargarProductos(String prompt) {
        // Obtener productos (sin importar si están ocultos o no, ya que el empleado gestiona el catálogo)
        List<LineaProductoVenta> todos = modelo.aplicacion.Catalogo.getInstancia().obtenerProductosNuevosGestion(prompt);

        List<LineaProductoVenta> comics = new ArrayList<>();
        List<LineaProductoVenta> boardGames = new ArrayList<>();
        List<LineaProductoVenta> figures = new ArrayList<>();

        if (todos != null) {
            for (LineaProductoVenta p : todos) {
                if (p instanceof Comic) {
                    comics.add(p);
                } else if (p instanceof JuegoDeMesa) {
                    boardGames.add(p);
                } else if (p instanceof Figura) {
                    figures.add(p);
                }
            }
        }

        panelModifyList.actualizarProductos(comics, boardGames, figures, this);
    }

    /**
     * Maneja eventos emitidos por la UI relacionados con modificar productos
     * (abrir ventana de modificación, confirmar cambios, etc.).
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.startsWith("MODIFY_")) {
            try {
                int id = Integer.parseInt(cmd.substring(7));
                LineaProductoVenta p = modelo.aplicacion.Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p != null) {
                    windowModifySingle = new ModifyAProductWindow(mainFrame);
                    windowModifySingle.cargarProducto(p);
                    
                    windowModifySingle.getBtnBack().addActionListener(ev -> windowModifySingle.dispose());
                    windowModifySingle.getBtnConfirmChanges().addActionListener(this);
                    windowModifySingle.getBtnConfirmChanges().setActionCommand("CONFIRM_CHANGES");
                    
                    windowModifySingle.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } else if (cmd.equals("CONFIRM_CHANGES")) {
            guardarCambios();
        }
    }
    
    /**
     * Lee los campos de la ventana de modificación y aplica los cambios al
     * producto seleccionado. Muestra diálogos en caso de error.
     */
    private void guardarCambios() {
        if (windowModifySingle == null) return;
        LineaProductoVenta p = windowModifySingle.getCurrentProduct();
        if (p == null) return;
        
        try {
            int newId = Integer.parseInt(windowModifySingle.getNewId());
            p.setID(newId);
            
            p.setNombre(windowModifySingle.getNewName());
            
            double newPrice = Double.parseDouble(windowModifySingle.getNewPrice().replace(",", "."));
            p.setPrecio(newPrice);
            
            int newStock = Integer.parseInt(windowModifySingle.getNewStock());
            p.setStock(newStock);
            
            p.setDescripcion(windowModifySingle.getNewDescription());
            
            // Actualizar Categorías
            java.util.Set<modelo.categoria.Categoria> actuales = new java.util.HashSet<>(p.getCategorias());
            java.util.Set<modelo.categoria.Categoria> seleccionadas = windowModifySingle.getCategoriasSeleccionadas();
            for (modelo.categoria.Categoria c : actuales) {
                if (!seleccionadas.contains(c)) p.eliminarCategoria(c);
            }
            for (modelo.categoria.Categoria c : seleccionadas) {
                if (!actuales.contains(c)) p.añadirCategoria(c);
            }
            
            // Actualizar Foto
            if (windowModifySingle.getSelectedPhotoFile() != null) {
                p.setFoto(windowModifySingle.getSelectedPhotoFile());
            }
            
            // Actualizar específicos
            if (p instanceof Comic) {
                Comic c = (Comic) p;
                c.setNumeroPaginas(Integer.parseInt(windowModifySingle.getNewNumPaginas()));
                c.setAutor(windowModifySingle.getNewAutor());
                c.setEditorial(windowModifySingle.getNewEditorial());
                c.setAñoPublicacion(Integer.parseInt(windowModifySingle.getNewAño()));
            } else if (p instanceof Figura) {
                Figura f = (Figura) p;
                f.setMarca(windowModifySingle.getNewMarca());
                f.setMaterial(windowModifySingle.getNewMaterial());
                f.setDimensionX(Double.parseDouble(windowModifySingle.getNewDimX().replace(",", ".")));
                f.setDimensionY(Double.parseDouble(windowModifySingle.getNewDimY().replace(",", ".")));
                f.setDimensionZ(Double.parseDouble(windowModifySingle.getNewDimZ().replace(",", ".")));
            } else if (p instanceof JuegoDeMesa) {
                JuegoDeMesa j = (JuegoDeMesa) p;
                j.setNumeroJugadores(Integer.parseInt(windowModifySingle.getNewNumJugadores()));
                j.setEdadMinima(Integer.parseInt(windowModifySingle.getNewEdadMin()));
                j.setEdadMaxima(Integer.parseInt(windowModifySingle.getNewEdadMax()));
                j.setTipoJuegoDeMesa(windowModifySingle.getNewTipoJuego());
            }
            
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Please check numeric fields (ID, Price, Stock, Dimensions, etc.) and ensure they are valid.", "Invalid Input", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "An error occurred: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        javax.swing.JOptionPane.showMessageDialog(mainFrame, "Product updated successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        
        windowModifySingle.dispose();
        cargarProductos(panelModifyList.getSearchText());
    }
}