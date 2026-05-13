 package controladores;

import modelo.aplicacion.Catalogo;
import modelo.descuento.Descuento;
import modelo.producto.LineaProductoVenta;
import modelo.producto.Pack;
import vista.empleadoPanel.DescuentosPanel;
import vista.empleadoWindow.AddDiscountWindow;
import vista.main.MainFrame;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la gestión de descuentos.
 *
 * Permite listar productos y packs, abrir la ventana para añadir un descuento
 * y aplicar/eliminar descuentos sobre productos.
 */
public class ControladorDescuentos implements ActionListener {

    private final DescuentosPanel panel;
    private final MainFrame mainFrame;
    /**
     * Inicializa el controlador y configura listeners básicos.
     *
     * @param panel panel de descuentos en la UI
     * @param mainFrame ventana principal
     * @param mainController controlador principal para navegación
     */
    public ControladorDescuentos(DescuentosPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
        this.panel.getColIndividuales().addSearchListener(this);
        this.panel.getColPacks().addSearchListener(this);

        if (this.panel.getBtnBack() != null) {
            this.panel.getBtnBack().addActionListener(e -> mainController.navegarA(MainFrame.PANEL_MANAGE_PRODUCTS));
        }
        
        this.panel.getColIndividuales().getBtnApply().addActionListener(this);
        this.panel.getColIndividuales().getBtnApply().setActionCommand("APPLY_INDIVIDUAL");
        this.panel.getColPacks().getBtnApply().addActionListener(this);
        this.panel.getColPacks().getBtnApply().setActionCommand("APPLY_PACKS");

        // Auto-recargar al abrir la pestaña
        this.panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatos();
            }
        });
    }

    /**
     * Carga los datos de productos y packs aplicando filtros de búsqueda.
     *
     * Actualiza las columnas de la vista con los resultados.
     */
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

    /**
     * Maneja acciones de la UI: búsquedas, ver información y abrir ventana de
     * añadir descuento.
     *
     * @param e evento de acción
     */
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
        } else if (cmd.startsWith("EDIT_")) {
            int id = Integer.parseInt(cmd.substring(5));
            LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
            if (p != null && p.getDescuento() != null) {
                Descuento d = p.getDescuento();
                List<LineaProductoVenta> affected = new ArrayList<>();
                for (LineaProductoVenta prod : Catalogo.getInstancia().getProductosNuevos()) {
                    if (prod.getDescuento() == d) affected.add(prod);
                }
                AddDiscountWindow win = new AddDiscountWindow(mainFrame, affected, d, this);
                win.setVisible(true);
            }
        } else if (cmd.startsWith("APPLY_")) {
            List<LineaProductoVenta> seleccionados;
            if (cmd.equals("APPLY_INDIVIDUAL")) {
                seleccionados = panel.getColIndividuales().getSelectedProducts();
            } else {
                seleccionados = panel.getColPacks().getSelectedProducts();
            }

            if (seleccionados.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(mainFrame, "Please select at least one product using the checkboxes.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            AddDiscountWindow win = new AddDiscountWindow(mainFrame, seleccionados, null, this);
            win.setVisible(true);
        }
    }

    public void confirmarDescuentosMulti(List<LineaProductoVenta> productos, Descuento oldDiscount, String type, modelo.tiempo.DateTimeSimulado inicio, modelo.tiempo.DateTimeSimulado fin, int perc, double thresh, int threshPerc, int buyQty, int recvQty, double giftThresh, int giftId, int giftQty) {
        try {
            if (oldDiscount != null) {
                for (LineaProductoVenta p : Catalogo.getInstancia().getProductosNuevos()) {
                    if (p.getDescuento() == oldDiscount) {
                        Catalogo.getInstancia().eliminarDescuento(oldDiscount, p);
                    }
                }
            }

            if (type.contains("Percentage") || type.contains("Quantity")) {
                for (LineaProductoVenta p : productos) {
                    Descuento d = null;
                    if (type.contains("Percentage")) {
                        d = new modelo.descuento.Precio(inicio, fin, perc);
                    } else {
                        d = new modelo.descuento.Cantidad(inicio, fin, buyQty, recvQty);
                    }
                    if (p.getDescuento() != null) Catalogo.getInstancia().eliminarDescuento(p.getDescuento(), p);
                    Catalogo.getInstancia().añadirDescuento(d);
                    Catalogo.getInstancia().aplicarDescuento(p, d);
                }
            } else {
                Descuento d = null;
                if (type.contains("Threshold")) {
                    d = new modelo.descuento.RebajaUmbral(inicio, fin, thresh, threshPerc);
                } else if (type.contains("Gift")) {
                    LineaProductoVenta giftProd = Catalogo.getInstancia().buscarProductoNuevo(giftId);
                    if (giftProd == null) throw new IllegalArgumentException("Gift Product ID not found");
                    java.util.Map<LineaProductoVenta, Integer> gifts = new java.util.HashMap<>();
                    gifts.put(giftProd, giftQty);
                    d = new modelo.descuento.Regalo(inicio, fin, giftThresh, gifts);
                }
                Catalogo.getInstancia().añadirDescuento(d);
                for (LineaProductoVenta p : productos) {
                    if (p.getDescuento() != null) Catalogo.getInstancia().eliminarDescuento(p.getDescuento(), p);
                    Catalogo.getInstancia().aplicarDescuento(p, d);
                }
            }
            cargarDatos();
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Discounts applied successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Error applying discounts: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
}