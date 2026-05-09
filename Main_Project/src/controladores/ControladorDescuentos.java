 package controladores;

import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import modelo.descuento.Descuento;
import modelo.producto.LineaProductoVenta;
import modelo.producto.Pack;
import vista.empleadoWindow.AddDiscountWindow;
import vista.main.MainFrame;
import vista.userPanels.DescuentosPanel;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorDescuentos implements ActionListener {

    private final DescuentosPanel panel;
    private final MainFrame mainFrame;
    private final MainController mainController;

    public ControladorDescuentos(DescuentosPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.mainController = mainController;

        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
        this.panel.getColIndividuales().addSearchListener(this);
        this.panel.getColPacks().addSearchListener(this);

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

    private void cargarDatos() {
        String searchInd = panel.getColIndividuales().getSearchText().toLowerCase();
        String searchPacks = panel.getColPacks().getSearchText().toLowerCase();

        List<LineaProductoVenta> individuales = new ArrayList<>();
        List<LineaProductoVenta> packs = new ArrayList<>();

        for (LineaProductoVenta p : Catalogo.getInstancia().getProductosNuevos()) {
            if (p instanceof Pack) {
                if (searchPacks.isEmpty() || p.getNombre().toLowerCase().contains(searchPacks)) {
                    packs.add(p);
                }
            } else {
                if (searchInd.isEmpty() || p.getNombre().toLowerCase().contains(searchInd)) {
                    individuales.add(p);
                }
            }
        }

        panel.getColIndividuales().actualizarProductos(individuales, this);
        panel.getColPacks().actualizarProductos(packs, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;
        
        if (cmd.startsWith("SEARCH_")) {
            cargarDatos();
        } else if (cmd.startsWith("INFO_")) {
            int id = Integer.parseInt(cmd.substring(5));
            LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
            if (p != null) {
                Window parentWindow = SwingUtilities.getWindowAncestor(panel);
                vista.clienteWindows.VentanaDetallesProductoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoWindow(parentWindow, p);
                dialog.setVisible(true);
            }
        } else if (cmd.startsWith("ADD_")) {
            int id = Integer.parseInt(cmd.substring(4));
            LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
            if (p != null) {
                AddDiscountWindow win = new AddDiscountWindow(mainFrame, p, this);
                win.setVisible(true);
            }
        }
    }

    public void confirmarAñadirDescuento(LineaProductoVenta p, Descuento d) {
        try {
            if (p.getDescuento() != null) {
                Catalogo.getInstancia().eliminarDescuento(p.getDescuento(), p);
            }
            Catalogo.getInstancia().añadirDescuento(d);
            Catalogo.getInstancia().aplicarDescuento(p, d);
            cargarDatos();
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Discount applied successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Error applying discount: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}