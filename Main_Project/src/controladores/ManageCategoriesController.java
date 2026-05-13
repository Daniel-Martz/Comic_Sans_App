package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import vista.clienteWindows.AddCategoryWindow;
import vista.clienteWindows.ManageCategoriesWindow;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Controlador para gestionar categorías de producto (ventanas de gestión/crear).
 *
 * Abre la ventana de gestión, permite crear y eliminar categorías y mantiene
 * la vista sincronizada con el catálogo.
 */
public class ManageCategoriesController {

    private ManageCategoriesWindow manageCategoriesWindow;
    private AddCategoryWindow addCategoryWindow;
    private Catalogo catalogo;
    private JFrame parentFrame;

    /**
     * Crea el controlador con la ventana padre donde se mostrarán los dialogs.
     *
     * @param parentFrame ventana padre
     */
    public ManageCategoriesController(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.catalogo = Aplicacion.getInstancia().getCatalogo();
    }

    /**
     * Muestra la ventana de gestión de categorías (la crea si hace falta).
     */
    public void mostrarVentana() {
        if (manageCategoriesWindow == null) {
            manageCategoriesWindow = new ManageCategoriesWindow(parentFrame);
            inicializarManageListeners();
        }
        actualizarVistaCategorias();
        manageCategoriesWindow.setVisible(true);
    }

    /**
     * Registra listeners para crear y borrar categorías desde la ventana.
     */
    private void inicializarManageListeners() {
        manageCategoriesWindow.addCreateListener(e -> {
            abrirVentanaCrearCategoria();
        });

        manageCategoriesWindow.addDeleteListener(e -> {
            String command = e.getActionCommand();
            if (command.startsWith("DELETE_")) {
                String catName = command.substring(7);
                Categoria cat = catalogo.buscarCategoriaPorNombre(catName);
                if (cat != null) {
                    catalogo.eliminarCategoria(cat);
                    actualizarVistaCategorias(); // Recargar vista
                }
            }
        });
    }

    /**
     * Abre la ventana modal para crear una nueva categoría y procesa la
     * confirmación (validación y actualización del catálogo).
     */
    private void abrirVentanaCrearCategoria() {
        addCategoryWindow = new AddCategoryWindow(manageCategoriesWindow);
        addCategoryWindow.addConfirmListener(e -> {
            String name = addCategoryWindow.getCategoryName();
            if (name != null && !name.trim().isEmpty()) {
                if (catalogo.buscarCategoriaPorNombre(name) == null) {
                    catalogo.añadirCategoria(new Categoria(name));
                    JOptionPane.showMessageDialog(addCategoryWindow, "Category created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    actualizarVistaCategorias();
                    addCategoryWindow.dispose();
                } else {
                    JOptionPane.showMessageDialog(addCategoryWindow, "Category already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(addCategoryWindow, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addCategoryWindow.setVisible(true);
    }

    /**
     * Actualiza la lista de categorías mostrada en la ventana.
     */
    private void actualizarVistaCategorias() {
        if (manageCategoriesWindow != null) {
            manageCategoriesWindow.setCategorias(new ArrayList<>(catalogo.getCategoriasTienda()));
        }
    }
}