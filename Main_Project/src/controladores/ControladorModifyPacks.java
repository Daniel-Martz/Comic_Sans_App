package controladores;

import modelo.aplicacion.Catalogo;
import modelo.producto.LineaProductoVenta;
import modelo.producto.Pack;
import vista.main.MainFrame;
import vista.empleadoPanel.ModifyPacksPanel;
import vista.empleadoWindow.ModifyAPackWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para modificar packs existentes.
 *
 * Lista los packs, abre la ventana de modificación y aplica los cambios
 * guardados por el usuario.
 */
public class ControladorModifyPacks implements ActionListener {

    private final ModifyPacksPanel panel;
    private final MainFrame mainFrame;
    /**
     * Inicializa el controlador y registra listeners para búsqueda y navegación.
     *
     * @param panel panel de modificación de packs
     * @param mainFrame ventana principal
     * @param mainController controlador principal
     */
    public ControladorModifyPacks(ModifyPacksPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.panel.getBtnBack().addActionListener(e -> mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS));
        this.panel.addSearchListener(e -> cargarPacks());
        
        this.panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarPacks();
            }
        });
    }

    /**
     * Recupera los packs del catálogo y actualiza la vista según el filtro de
     * búsqueda.
     */
    private void cargarPacks() {
        String prompt = panel.getSearchText().toLowerCase();
        List<LineaProductoVenta> todos = Catalogo.getInstancia().getProductosNuevos().stream().toList();
        List<LineaProductoVenta> packs = new ArrayList<>();

        for (LineaProductoVenta p : todos) {
            if (p instanceof Pack) {
                if (prompt.isEmpty() || p.getNombre().toLowerCase().contains(prompt)) {
                    packs.add(p);
                }
            }
        }
        panel.actualizarPacks(packs, this);
    }

    /**
     * Maneja eventos de la vista: búsqueda y petición de modificar un pack.
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.startsWith("SEARCH")) {
            cargarPacks();
        } else if (cmd.startsWith("MODIFY_")) {
            try {
                int id = Integer.parseInt(cmd.substring(7));
                LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                if (p instanceof Pack pack) {
                    ModifyAPackWindow win = new ModifyAPackWindow(mainFrame, pack, this);
                    win.setVisible(true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Aplica las modificaciones al pack y actualiza la vista.
     *
     * @param pack pack a modificar
     * @param name nombre nuevo
     * @param desc descripción nueva
     * @param price precio nuevo
     * @param stock stock nuevo
     * @param photo posible foto nueva (null si no cambia)
     * @param nuevosProductos mapa de productos y cantidades del pack
     */
    public void confirmarModificacion(Pack pack, String name, String desc, double price, int stock, java.io.File photo, java.util.Map<LineaProductoVenta, Integer> nuevosProductos) {
        try {
            pack.setNombre(name);
            pack.setDescripcion(desc);
            pack.setPrecio(price);
            pack.setStock(stock);
            if (photo != null) {
                pack.setFoto(photo);
            }
            
            java.util.Map<LineaProductoVenta, Integer> antiguos = new java.util.HashMap<>(pack.getProductosPack());
            for (java.util.Map.Entry<LineaProductoVenta, Integer> entry : antiguos.entrySet()) {
                pack.eliminarProductoDePack(entry.getKey(), entry.getValue());
            }
            for (java.util.Map.Entry<LineaProductoVenta, Integer> entry : nuevosProductos.entrySet()) {
                pack.añadirProductoAPack(entry.getKey(), entry.getValue());
            }

            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Pack modified successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarPacks();
            
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Error modifying pack: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}