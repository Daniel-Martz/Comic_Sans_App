package vista.userPanels;

import modelo.producto.ProductoSegundaMano;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para buscar productos de segunda mano y seleccionarlos para intercambiar.
 */
public class SearchInterchangesPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Color COLOR_FONDO = new Color(153, 180, 209);

    private JPanel panelScrollProductos;
    private JLabel lblStatus;
    private JButton btnReset;
    private JButton btnProceed;
    
    private HeaderPanel headerPanel;
    private List<JCheckBox> checkboxesSeleccion = new ArrayList<>();

    public SearchInterchangesPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        // --- HEADER ---
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH); // Añadimos la cabecera en el límite superior absoluto
        
        // Envoltorio para el resto de la vista, manteniendo el margen lateral original
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JLabel lblTitulo = new JLabel("SEARCH FOR INTERCHANGES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentWrapper.add(lblTitulo, BorderLayout.NORTH);

        // --- CENTER (Product Grid) ---
        panelScrollProductos = new JPanel(new GridLayout(0, 4, 15, 15));
        panelScrollProductos.setBackground(COLOR_FONDO);
        panelScrollProductos.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel contenedorGrid = new JPanel(new BorderLayout());
        contenedorGrid.setBackground(COLOR_FONDO);
        contenedorGrid.add(panelScrollProductos, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(contenedorGrid);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.setBorder(null);
        contentWrapper.add(scroll, BorderLayout.CENTER);

        // --- BOTTOM (Status Bar) ---
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Color.DARK_GRAY);
        bottomBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        lblStatus = new JLabel("Selected items: 0 | Total Estimated Value: 0.00 €");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottomButtons.setOpaque(false);

        btnReset = new JButton("Reset Selections");
        btnReset.setBackground(new Color(178, 34, 34));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        btnProceed = new JButton("Select Own Products to Offer ➔");
        btnProceed.setBackground(new Color(50, 205, 50));
        btnProceed.setForeground(Color.WHITE);
        btnProceed.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        bottomButtons.add(btnReset);
        bottomButtons.add(btnProceed);

        bottomBar.add(lblStatus, BorderLayout.WEST);
        bottomBar.add(bottomButtons, BorderLayout.EAST);
        contentWrapper.add(bottomBar, BorderLayout.SOUTH);
        
        add(contentWrapper, BorderLayout.CENTER);
    }

    public void actualizarProductos(List<ProductoSegundaMano> productos, ActionListener actionCtrl, ItemListener itemCtrl) {
        panelScrollProductos.removeAll();
        checkboxesSeleccion.clear();

        if (productos == null || productos.isEmpty()) {
            JLabel vacio = new JLabel("No interchange products found.");
            vacio.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            panelScrollProductos.add(vacio);
        } else {
            for (ProductoSegundaMano p : productos) {
                JPanel tarjeta = crearTarjeta(p, actionCtrl, itemCtrl);
                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                wrapper.setBackground(COLOR_FONDO);
                wrapper.add(tarjeta);
                panelScrollProductos.add(wrapper);
            }
        }
        panelScrollProductos.revalidate();
        panelScrollProductos.repaint();
    }

    private JPanel crearTarjeta(ProductoSegundaMano prod, ActionListener actionCtrl, ItemListener itemCtrl) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(10,10,10,10)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(230, 340));

        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblNombre);

        tarjeta.add(Box.createVerticalStrut(10));

        JLabel img = new JLabel("", SwingConstants.CENTER);
        img.setOpaque(true);
        img.setBackground(new Color(220,220,220));
        img.setPreferredSize(new Dimension(160, 130));
        img.setMaximumSize(new Dimension(160, 130));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (prod.getFoto() != null && prod.getFoto().exists()) {
            ImageIcon icon = new ImageIcon(prod.getFoto().getAbsolutePath());
            Image image = icon.getImage().getScaledInstance(160, 130, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(image));
        } else {
            img.setText("NO IMAGE");
        }
        tarjeta.add(img);

        tarjeta.add(Box.createVerticalStrut(10));
        
        double estimado = prod.getDatosValidacion() != null ? prod.getDatosValidacion().getPrecioEstimadoProducto() : 0.0;
        JLabel lblPrecio = new JLabel(String.format("Estimated: %.2f €", estimado));
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblPrecio);

        tarjeta.add(Box.createVerticalStrut(10));

        JCheckBox chkSelect = new JCheckBox("Select for Interchange");
        chkSelect.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        chkSelect.setBackground(Color.WHITE);
        chkSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkSelect.setActionCommand("SELECT_" + prod.getID());
        chkSelect.addItemListener(itemCtrl);
        checkboxesSeleccion.add(chkSelect);
        tarjeta.add(chkSelect);

        tarjeta.add(Box.createVerticalStrut(10));

        JButton btnInfo = new JButton("+ More Info");
        btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInfo.setActionCommand("INFO_" + prod.getID());
        btnInfo.addActionListener(actionCtrl);
        tarjeta.add(btnInfo);

        return tarjeta;
    }

    // --- Métodos de interacción (MVC) ---
    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public void setControladorInferior(ActionListener l) { btnReset.addActionListener(l); btnProceed.addActionListener(l); btnReset.setActionCommand("RESET"); btnProceed.setActionCommand("PROCEED"); }
    public void updateSelectionInfo(int count, double total) { lblStatus.setText(String.format("Selected items: %d | Total Estimated Value: %.2f €", count, total)); }
    public void desmarcarTodos() { for (JCheckBox chk : checkboxesSeleccion) { chk.setSelected(false); } }
}
