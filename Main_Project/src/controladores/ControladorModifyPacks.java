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

public class ControladorModifyPacks implements ActionListener {

    private final ModifyPacksPanel panel;
    private final MainFrame mainFrame;
    private final MainController mainController;

    public ControladorModifyPacks(ModifyPacksPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.panel.getBtnBack().addActionListener(e -> mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS));
        this.panel.addSearchListener(e -> cargarPacks());
        
        this.panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarPacks();
            }
        });
    }

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