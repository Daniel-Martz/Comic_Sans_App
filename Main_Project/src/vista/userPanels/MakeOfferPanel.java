package vista.userPanels;

import modelo.producto.ProductoSegundaMano;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;
import java.util.Set;

public class MakeOfferPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Color COLOR_FONDO = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private SelectionColumn panelRequested;
    private SelectionColumn panelOffered;
    private JButton btnSubmitOffer;

    public MakeOfferPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        // --- CABECERA ---
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel lblTitulo = new JLabel("MAKE AN OFFER", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 15, 0));
        contentWrapper.add(lblTitulo, BorderLayout.NORTH);

        // --- CUERPO (DOS COLUMNAS) ---
        JPanel columnsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        columnsContainer.setBackground(COLOR_FONDO);

        panelRequested = new SelectionColumn("REQUESTED PRODUCTS (Other users)", "REQ");
        panelOffered = new SelectionColumn("OFFERED PRODUCTS (My portfolio)", "OFF");

        columnsContainer.add(panelRequested);
        columnsContainer.add(panelOffered);
        contentWrapper.add(columnsContainer, BorderLayout.CENTER);

        // --- BOTÓN INFERIOR ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(COLOR_FONDO);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnSubmitOffer = new JButton("SUBMIT OFFER 🤝");
        btnSubmitOffer.setBackground(new Color(50, 205, 50));
        btnSubmitOffer.setForeground(Color.WHITE);
        btnSubmitOffer.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnSubmitOffer.setPreferredSize(new Dimension(250, 50));
        btnSubmitOffer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmitOffer.setActionCommand("SUBMIT_OFFER");
        
        bottomPanel.add(btnSubmitOffer);
        contentWrapper.add(bottomPanel, BorderLayout.SOUTH);

        add(contentWrapper, BorderLayout.CENTER);
    }

    // --- MÉTODOS PARA EL CONTROLADOR ---
    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public SelectionColumn getPanelRequested() { return panelRequested; }
    public SelectionColumn getPanelOffered() { return panelOffered; }
    public void addSubmitListener(ActionListener l) { btnSubmitOffer.addActionListener(l); }

    // =========================================================================
    // CLASE INTERNA: COLUMNA DE SELECCIÓN (Buscador + Grid + Status)
    // =========================================================================
    public class SelectionColumn extends JPanel {
        private JTextField txtSearch;
        private JButton btnSearch;
        private JPanel gridProductos;
        private JLabel lblStatus;
        private String commandPrefix;

        public SelectionColumn(String title, String commandPrefix) {
            this.commandPrefix = commandPrefix;
            setLayout(new BorderLayout(0, 10));
            setBackground(COLOR_FONDO);
            setBorder(new LineBorder(Color.DARK_GRAY, 2));

            // Header de la columna
            JPanel top = new JPanel(new BorderLayout());
            top.setBackground(new Color(74, 118, 201));
            top.setBorder(new EmptyBorder(8, 10, 8, 10));
            JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            top.add(lblTitle, BorderLayout.NORTH);

            // Buscador interno
            JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
            searchPanel.setOpaque(false);
            searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            txtSearch = new JTextField();
            btnSearch = new JButton("Search");
            btnSearch.setActionCommand("SEARCH_" + commandPrefix);
            searchPanel.add(txtSearch, BorderLayout.CENTER);
            searchPanel.add(btnSearch, BorderLayout.EAST);
            top.add(searchPanel, BorderLayout.SOUTH);
            add(top, BorderLayout.NORTH);

            // Grid
            gridProductos = new JPanel(new GridLayout(0, 2, 10, 10));
            gridProductos.setBackground(Color.WHITE);
            gridProductos.setBorder(new EmptyBorder(10, 10, 10, 10));
            JPanel gridWrapper = new JPanel(new BorderLayout());
            gridWrapper.setBackground(Color.WHITE);
            gridWrapper.add(gridProductos, BorderLayout.NORTH);
            JScrollPane scroll = new JScrollPane(gridWrapper);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(scroll, BorderLayout.CENTER);

            // Status Bar
            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottom.setBackground(Color.DARK_GRAY);
            lblStatus = new JLabel("Selected: 0 | Total: 0.00 €");
            lblStatus.setForeground(Color.WHITE);
            lblStatus.setFont(new Font("SansSerif", Font.BOLD, 13));
            bottom.add(lblStatus);
            add(bottom, BorderLayout.SOUTH);
        }

        public void actualizarProductos(List<ProductoSegundaMano> productos, Set<ProductoSegundaMano> seleccionados, ActionListener actionCtrl, ItemListener itemCtrl) {
            gridProductos.removeAll();
            if (productos.isEmpty()) {
                gridProductos.add(new JLabel("No products found."));
            } else {
                for (ProductoSegundaMano p : productos) {
                    gridProductos.add(crearTarjetaMini(p, seleccionados.contains(p), actionCtrl, itemCtrl));
                }
            }
            gridProductos.revalidate();
            gridProductos.repaint();
        }

        public void updateStatus(int count, double total) {
            lblStatus.setText(String.format("Selected: %d | Total: %.2f €", count, total));
        }

        public void addSearchListener(ActionListener l) {
            btnSearch.addActionListener(l);
            txtSearch.addActionListener(e -> btnSearch.doClick());
        }
        
        public String getSearchText() { return txtSearch.getText().trim(); }

        private JPanel crearTarjetaMini(ProductoSegundaMano prod, boolean isSelected, ActionListener actionCtrl, ItemListener itemCtrl) {
            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5,5,5,5)));
            tarjeta.setBackground(Color.WHITE);

            // Imagen del producto
            JLabel img = new JLabel("", SwingConstants.CENTER);
            img.setOpaque(true);
            img.setBackground(new Color(220, 220, 220));
            img.setPreferredSize(new Dimension(100, 100));
            img.setMaximumSize(new Dimension(100, 100));
            img.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (prod.getFoto() != null && prod.getFoto().exists()) {
                ImageIcon icon = new ImageIcon(prod.getFoto().getAbsolutePath());
                Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(image));
            } else {
                img.setText("NO IMAGE");
            }
            tarjeta.add(img);
            tarjeta.add(Box.createVerticalStrut(5));

            JLabel lblNombre = new JLabel(prod.getNombre());
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(lblNombre);
            
            if ("REQ".equals(commandPrefix) && prod.getClienteProducto() != null) {
                JLabel lblOwner = new JLabel("Owner: " + prod.getClienteProducto().getNombreUsuario());
                lblOwner.setFont(new Font("SansSerif", Font.ITALIC, 11));
                lblOwner.setForeground(Color.GRAY);
                lblOwner.setAlignmentX(Component.CENTER_ALIGNMENT);
                tarjeta.add(lblOwner);
            }

            double estimado = prod.getDatosValidacion() != null ? prod.getDatosValidacion().getPrecioEstimadoProducto() : 0.0;
            JLabel lblPrecio = new JLabel(String.format("%.2f €", estimado));
            lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(lblPrecio);

            JCheckBox chkSelect = new JCheckBox("Select");
            chkSelect.setSelected(isSelected);
            chkSelect.setBackground(Color.WHITE);
            chkSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
            chkSelect.setActionCommand(commandPrefix + "_" + prod.getID());
            chkSelect.addItemListener(itemCtrl);
            tarjeta.add(chkSelect);

            tarjeta.add(Box.createVerticalStrut(5));

            JButton btnInfo = new JButton("+ More Info");
            btnInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
            btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnInfo.setActionCommand("INFO_" + prod.getID());
            btnInfo.addActionListener(actionCtrl);
            tarjeta.add(btnInfo);

            return tarjeta;
        }
    }
}