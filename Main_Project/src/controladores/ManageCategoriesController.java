package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import vista.userWindows.AddCategoryWindow;
import vista.userWindows.ManageCategoriesWindow;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Controlador encargado de enlazar la lógica del modelo de Categorías
 * con las vistas ManageCategoriesWindow y AddCategoryWindow.
 */
public class ManageCategoriesController {

    private ManageCategoriesWindow manageCategoriesWindow;
    private AddCategoryWindow addCategoryWindow;
    private Catalogo catalogo;
    private JFrame parentFrame;

    public ManageCategoriesController(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.catalogo = Aplicacion.getInstancia().getCatalogo();
    }

    public void mostrarVentana() {
        if (manageCategoriesWindow == null) {
            manageCategoriesWindow = new ManageCategoriesWindow(parentFrame);
            inicializarManageListeners();
        }
        actualizarVistaCategorias();
        manageCategoriesWindow.setVisible(true);
    }

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

    private void actualizarVistaCategorias() {
        if (manageCategoriesWindow != null) {
            manageCategoriesWindow.setCategorias(new ArrayList<>(catalogo.getCategoriasTienda()));
        }
    }
}