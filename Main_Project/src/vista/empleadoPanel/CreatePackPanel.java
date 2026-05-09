package vista.empleadoPanel;

import modelo.producto.LineaProductoVenta;
import vista.userPanels.HeaderPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.event.ChangeListener;

public class CreatePackPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Color COLOR_FONDO = new Color(162, 187, 210);

    private JPanel panelScrollProductos;
    private JLabel lblStatus;
    private JButton btnProceed;
    private JButton btnBack;
    private JTextField txtSearch;
    private JButton btnSearch;
    
    private HeaderPanel headerPanel;
    private List<JCheckBox> checkboxesSeleccion = new ArrayList<>();
    private Map<Integer, JSpinner> spinnersQty = new HashMap<>();

    public CreatePackPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();
        btnBack = headerPanel.addSecondaryTopButton("BACK");
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(COLOR_FONDO);
        
        JLabel lblTitulo = new JLabel("SELECT THE PRODUCTS OF THE PACK", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(54, 119, 189));
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        topPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnSearch = new JButton("Search");
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        contentWrapper.add(topPanel, BorderLayout.NORTH);

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
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scroll, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Color.DARK_GRAY);
        bottomBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        lblStatus = new JLabel("Selected items: 0");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottomButtons.setOpaque(false);

        btnProceed = new JButton("Continue ➔");
        btnProceed.setBackground(new Color(50, 205, 50));
        btnProceed.setForeground(Color.WHITE);
        btnProceed.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnProceed.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomButtons.add(btnProceed);

        bottomBar.add(lblStatus, BorderLayout.WEST);
        bottomBar.add(bottomButtons, BorderLayout.EAST);
        contentWrapper.add(bottomBar, BorderLayout.SOUTH);
        
        add(contentWrapper, BorderLayout.CENTER);
    }

    public void actualizarProductos(List<LineaProductoVenta> productos, Map<LineaProductoVenta, Integer> seleccionados, ItemListener itemCtrl, ChangeListener changeCtrl) {
        panelScrollProductos.removeAll();
        checkboxesSeleccion.clear();
        spinnersQty.clear();

        if (productos == null || productos.isEmpty()) {
            JLabel vacio = new JLabel("No products found.");
            vacio.setFont(new Font("SansSerif", Font.BOLD, 16));
            panelScrollProductos.add(vacio);
        } else {
            for (LineaProductoVenta p : productos) {
                boolean isSelected = seleccionados != null && seleccionados.containsKey(p);
                int qty = isSelected ? seleccionados.get(p) : 1;
                JPanel tarjeta = crearTarjeta(p, isSelected, qty, itemCtrl, changeCtrl);
                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                wrapper.setBackground(COLOR_FONDO);
                wrapper.add(tarjeta);
                panelScrollProductos.add(wrapper);
            }
        }
        panelScrollProductos.revalidate();
        panelScrollProductos.repaint();
    }

    private JPanel crearTarjeta(LineaProductoVenta prod, boolean isSelected, int qty, ItemListener itemCtrl, ChangeListener changeCtrl) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(10,10,10,10)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(220, 290));

        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblNombre);

        tarjeta.add(Box.createVerticalStrut(10));

        JLabel img = new JLabel("IMAGE", SwingConstants.CENTER);
        img.setOpaque(true);
        img.setBackground(new Color(220,220,220));
        img.setPreferredSize(new Dimension(160, 130));
        img.setMaximumSize(new Dimension(160, 130));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            if (prod.getFoto() != null && prod.getFoto().exists()) {
                ImageIcon icon = new ImageIcon(prod.getFoto().getAbsolutePath());
                Image image = icon.getImage().getScaledInstance(160, 130, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(image));
                img.setText("");
            }
        } catch(Exception e){}
        tarjeta.add(img);

        tarjeta.add(Box.createVerticalStrut(15));

        JPanel bottomAction = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        bottomAction.setBackground(Color.WHITE);

        JCheckBox chkSelect = new JCheckBox("Select");
        chkSelect.setFont(new Font("SansSerif", Font.BOLD, 12));
        chkSelect.setBackground(Color.WHITE);
        chkSelect.setSelected(isSelected);
        chkSelect.setActionCommand("SELECT_" + prod.getID());
        chkSelect.addItemListener(itemCtrl);
        checkboxesSeleccion.add(chkSelect);

        JSpinner spnQty = new JSpinner(new SpinnerNumberModel(qty, 1, 100, 1));
        spnQty.setName("SPN_" + prod.getID());
        spnQty.setEnabled(isSelected);
        spnQty.addChangeListener(changeCtrl);
        spinnersQty.put(prod.getID(), spnQty);

        chkSelect.addItemListener(e -> spnQty.setEnabled(chkSelect.isSelected()));

        bottomAction.add(chkSelect);
        bottomAction.add(spnQty);

        tarjeta.add(bottomAction);

        return tarjeta;
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public JButton getBtnBack() { return btnBack; }
    public void setControladorInferior(ActionListener l) { btnProceed.addActionListener(l); btnProceed.setActionCommand("CONTINUE"); }
    public void updateSelectionInfo(int count) { lblStatus.setText(String.format("Selected items: %d", count)); }
    public void desmarcarTodos() { for (JCheckBox chk : checkboxesSeleccion) { chk.setSelected(false); } }
    public int getQuantity(int id) {
        JSpinner spn = spinnersQty.get(id);
        if (spn != null) return (Integer) spn.getValue();
        return 1;
    }
    public String getSearchText() { return txtSearch.getText().trim(); }
    public void addSearchListener(ActionListener l) {
        btnSearch.addActionListener(l);
        txtSearch.addActionListener(e -> btnSearch.doClick());
    }
}