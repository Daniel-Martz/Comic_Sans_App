package vista.userPanels;

import modelo.producto.ProductoSegundaMano;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * Panel to create a trade offer between users.
 * Allows selecting requested products and offered products.
 */
public class MakeOfferPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The background color. */
    private final Color BACKGROUND_COLOR = new Color(153, 180, 209);

    /** The header panel. */
    private HeaderPanel headerPanel;
    
    /** The panel requested. */
    private SelectionColumn panelRequested;
    
    /** The panel offered. */
    private SelectionColumn panelOffered;
    
    /** The btn submit offer. */
    private JButton btnSubmitOffer;

    /**
     * Constructs the panel and initializes UI components.
     */
    public MakeOfferPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(BACKGROUND_COLOR);
        contentWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel lblTitle = new JLabel("MAKE AN OFFER", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitle.setForeground(Color.DARK_GRAY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        contentWrapper.add(lblTitle, BorderLayout.NORTH);

        JPanel columnsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        columnsContainer.setBackground(BACKGROUND_COLOR);

        panelRequested = new SelectionColumn("REQUESTED PRODUCTS (Other users)", "REQ");
        panelOffered = new SelectionColumn("OFFERED PRODUCTS (My portfolio)", "OFF");

        columnsContainer.add(panelRequested);
        columnsContainer.add(panelOffered);
        contentWrapper.add(columnsContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnSubmitOffer = new JButton("SUBMIT OFFER 🤝");
        btnSubmitOffer.setBackground(new Color(50, 205, 50));
        btnSubmitOffer.setForeground(Color.WHITE);
        btnSubmitOffer.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        btnSubmitOffer.setPreferredSize(new Dimension(250, 50));
        btnSubmitOffer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmitOffer.setActionCommand("SUBMIT_OFFER");
        
        bottomPanel.add(btnSubmitOffer);
        contentWrapper.add(bottomPanel, BorderLayout.SOUTH);

        add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() { return headerPanel; }
    
    /**
     * Gets the panel requested.
     *
     * @return the panel requested
     */
    public SelectionColumn getPanelRequested() { return panelRequested; }
    
    /**
     * Gets the panel offered.
     *
     * @return the panel offered
     */
    public SelectionColumn getPanelOffered() { return panelOffered; }
    
    /**
     * Adds the submit listener.
     *
     * @param l the l
     */
    public void addSubmitListener(ActionListener l) { btnSubmitOffer.addActionListener(l); }

    /**
     * Inner class representing a selection column with search and status bar.
     */
    public class SelectionColumn extends JPanel {
        
        private static final long serialVersionUID = 1L;

		/** The txt search. */
        private JTextField txtSearch;
        
        /** The btn search. */
        private JButton btnSearch;
        
        /** The grid products. */
        private JPanel gridProducts;
        
        /** The lbl status. */
        private JLabel lblStatus;
        
        /** The command prefix. */
        private String commandPrefix;

        /**
         * Instantiates a new selection column.
         *
         * @param title the title
         * @param commandPrefix the command prefix
         */
        public SelectionColumn(String title, String commandPrefix) {
            this.commandPrefix = commandPrefix;
            setLayout(new BorderLayout(0, 10));
            setBackground(BACKGROUND_COLOR);
            setBorder(new LineBorder(Color.DARK_GRAY, 2));

            JPanel top = new JPanel(new BorderLayout());
            top.setBackground(new Color(74, 118, 201));
            top.setBorder(new EmptyBorder(8, 10, 8, 10));
            JLabel lblColTitle = new JLabel(title, SwingConstants.CENTER);
            lblColTitle.setForeground(Color.WHITE);
            lblColTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
            top.add(lblColTitle, BorderLayout.NORTH);

            JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
            searchPanel.setOpaque(false);
            searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            txtSearch = new JTextField("Search...");
            txtSearch.setForeground(Color.GRAY);
            txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtSearch.getText().equals("Search...")) {
                        txtSearch.setText("");
                        txtSearch.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtSearch.getText().isEmpty()) {
                        txtSearch.setText("Search...");
                        txtSearch.setForeground(Color.GRAY);
                    }
                }
            });
            btnSearch = new JButton("Search");
            btnSearch.setActionCommand("SEARCH_" + commandPrefix);
            searchPanel.add(txtSearch, BorderLayout.CENTER);
            searchPanel.add(btnSearch, BorderLayout.EAST);
            top.add(searchPanel, BorderLayout.SOUTH);
            add(top, BorderLayout.NORTH);

            gridProducts = new JPanel(new GridLayout(0, 2, 10, 10));
            gridProducts.setBackground(Color.WHITE);
            gridProducts.setBorder(new EmptyBorder(10, 10, 10, 10));
            JPanel gridWrapper = new JPanel(new BorderLayout());
            gridWrapper.setBackground(Color.WHITE);
            gridWrapper.add(gridProducts, BorderLayout.NORTH);
            JScrollPane scroll = new JScrollPane(gridWrapper);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            add(scroll, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottom.setBackground(Color.DARK_GRAY);
            lblStatus = new JLabel("Selected: 0 | Total: 0.00 €");
            lblStatus.setForeground(Color.WHITE);
            lblStatus.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
            bottom.add(lblStatus);
            add(bottom, BorderLayout.SOUTH);
        }

        /**
         * Actualizar productos.
         *
         * @param productos the productos
         * @param seleccionados the seleccionados
         * @param actionCtrl the action ctrl
         * @param itemCtrl the item ctrl
         */
        public void actualizarProductos(List<ProductoSegundaMano> productos, Set<ProductoSegundaMano> seleccionados, ActionListener actionCtrl, ItemListener itemCtrl) {
            gridProducts.removeAll();
            if (productos.isEmpty()) {
                gridProducts.add(new JLabel("No products found."));
            } else {
                for (ProductoSegundaMano p : productos) {
                    gridProducts.add(crearTarjetaMini(p, seleccionados.contains(p), actionCtrl, itemCtrl));
                }
            }
            gridProducts.revalidate();
            gridProducts.repaint();
        }

        /**
         * Update status.
         *
         * @param count the count
         * @param total the total
         */
        public void updateStatus(int count, double total) {
            lblStatus.setText(String.format("Selected: %d | Total: %.2f €", count, total));
        }

        /**
         * Adds the search listener.
         *
         * @param l the l
         */
        public void addSearchListener(ActionListener l) {
            btnSearch.addActionListener(l);
            txtSearch.addActionListener(e -> btnSearch.doClick());
        }
        
        /**
         * Gets the search text.
         *
         * @return the search text
         */
        public String getSearchText() { 
            String text = txtSearch.getText().trim(); 
            return text.equals("Search...") ? "" : text;
        }

        /**
         * Crear tarjeta mini.
         *
         * @param prod the prod
         * @param isSelected the is selected
         * @param actionCtrl the action ctrl
         * @param itemCtrl the item ctrl
         * @return the j panel
         */
        private JPanel crearTarjetaMini(ProductoSegundaMano prod, boolean isSelected, ActionListener actionCtrl, ItemListener itemCtrl) {
            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5,5,5,5)));
            tarjeta.setBackground(Color.WHITE);

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
            lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
            lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(lblNombre);
            
            if ("REQ".equals(commandPrefix) && prod.getClienteProducto() != null) {
                JLabel lblOwner = new JLabel("Owner: " + prod.getClienteProducto().getNombreUsuario());
                lblOwner.setFont(new Font("Comic Sans MS", Font.ITALIC, 11));
                lblOwner.setForeground(Color.GRAY);
                lblOwner.setAlignmentX(Component.CENTER_ALIGNMENT);
                tarjeta.add(lblOwner);
            }

            double estimado = prod.getDatosValidacion() != null ? prod.getDatosValidacion().getPrecioEstimadoProducto() : 0.0;
            JLabel lblPrecio = new JLabel(String.format("%.2f €", estimado));
            lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(lblPrecio);

            if (prod.isPendienteAprobacionIntercambio()) {
                JLabel lblStatusIcon = new JLabel("⏳ PENDING");
                lblStatusIcon.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
                lblStatusIcon.setForeground(new Color(180, 100, 0));
                lblStatusIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
                tarjeta.add(lblStatusIcon);
            } else if (prod.estaEnOferta()) {
                JLabel lblStatusIcon = new JLabel("📌 INCLUDED IN OFFER");
                lblStatusIcon.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
                lblStatusIcon.setForeground(new Color(200, 80, 30));
                lblStatusIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
                tarjeta.add(lblStatusIcon);
            }

            JCheckBox chkSelect = new JCheckBox("Select");
            chkSelect.setSelected(isSelected);
            chkSelect.setBackground(Color.WHITE);
            chkSelect.setAlignmentX(Component.CENTER_ALIGNMENT);
            chkSelect.setActionCommand(commandPrefix + "_" + prod.getID());
            chkSelect.addItemListener(itemCtrl);
            if (prod.estaBloqueado()) {
                chkSelect.setEnabled(false);
            }
            tarjeta.add(chkSelect);

            tarjeta.add(Box.createVerticalStrut(5));

            JButton btnInfo = new JButton("+ More Info");
            btnInfo.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
            btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnInfo.setActionCommand("INFO_" + prod.getID());
            btnInfo.addActionListener(actionCtrl);
            tarjeta.add(btnInfo);

            return tarjeta;
        }
    }
}