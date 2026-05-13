package vista.userPanels;

import modelo.producto.ProductoSegundaMano;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Panel that displays the user's second-hand products.
 * Divided into products ready for interchange and those awaiting validation.
 */
public class MySecondHandProductsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color BG_COLOR      = new Color(153, 180, 209);
    private static final Color BANNER_COLOR  = new Color(74, 118, 201);
    private static final Color CARD_BG       = new Color(188, 210, 232);
    private static final Color CARD_BORDER   = new Color(120, 150, 185);
    private static final Color HEADER_LEFT   = new Color(60, 100, 180);
    private static final Color HEADER_RIGHT  = new Color(60, 100, 180);
    private static final Color BTN_ADD_BG    = new Color(255, 220, 50);
    private static final Color BTN_ADD_FG    = new Color(30, 30, 30);
    private static final Color BTN_PAY_BG    = new Color(50, 180, 70);
    private static final Color LABEL_FG      = new Color(30, 50, 90);
    private static final Color VALUE_FG      = new Color(20, 20, 60);
    private static final Color PAID_FG       = new Color(50, 150, 50);

    private static final Font BANNER_FONT  = new Font("Comic Sans MS", Font.BOLD, 22);
    private static final Font HEADER_FONT  = new Font("Comic Sans MS", Font.BOLD, 14);
    private static final Font LABEL_FONT   = new Font("Comic Sans MS", Font.BOLD, 11);
    private static final Font VALUE_FONT   = new Font("Comic Sans MS", Font.PLAIN, 11);
    private static final Font BTN_ADD_FONT = new Font("Comic Sans MS", Font.BOLD, 16);
    private static final Font BTN_PAY_FONT = new Font("Comic Sans MS", Font.BOLD, 11);

    private HeaderPanel headerPanel;
    private JPanel readyContainer;
    private JPanel validationContainer;
    private JButton btnAddProduct;

    private ActionListener payValidationListener;

    /**
     * Constructs the panel and initializes the UI components.
     */
    public MySecondHandProductsPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        readyContainer      = createScrollableContainer();
        validationContainer = createScrollableContainer();

        btnAddProduct = new JButton("ADD NEW PRODUCT");
        btnAddProduct.setFont(BTN_ADD_FONT);
        btnAddProduct.setBackground(BTN_ADD_BG);
        btnAddProduct.setForeground(BTN_ADD_FG);
        btnAddProduct.setFocusPainted(false);
        btnAddProduct.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        btnAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddProduct.setPreferredSize(new Dimension(320, 44));
        btnAddProduct.setMaximumSize(new Dimension(480, 44));

        btnAddProduct.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btnAddProduct.setBackground(BTN_ADD_BG.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btnAddProduct.setBackground(BTN_ADD_BG);
            }
        });
    }

    private void initLayout() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(0, 0));
        
        headerPanel = new HeaderPanel();
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(BG_COLOR);
        topWrapper.add(headerPanel, BorderLayout.NORTH);

        JLabel banner = new JLabel("MY SECOND-HAND PRODUCTS", SwingConstants.CENTER);
        banner.setFont(BANNER_FONT);
        banner.setForeground(Color.WHITE);
        banner.setOpaque(true);
        banner.setBackground(BANNER_COLOR);
        banner.setBorder(new EmptyBorder(14, 0, 14, 0));
        topWrapper.add(banner, BorderLayout.SOUTH);
        
        add(topWrapper, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 12, 0));
        body.setBackground(BG_COLOR);
        body.setBorder(new EmptyBorder(14, 14, 10, 14));

        body.add(buildColumn("INTERCHANGE-READY", HEADER_LEFT, readyContainer));
        body.add(buildColumn("AWAITING VALIDATION", HEADER_RIGHT, validationContainer));

        add(body, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottom.setBackground(BG_COLOR);
        bottom.setBorder(new EmptyBorder(6, 0, 12, 0));
        bottom.add(btnAddProduct);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel buildColumn(String title, Color headerColor, JPanel container) {
        JPanel col = new JPanel(new BorderLayout(0, 6));
        col.setBackground(BG_COLOR);
        col.setBorder(new LineBorder(new Color(100, 130, 175), 2));

        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(headerColor);
        header.setBorder(new EmptyBorder(8, 6, 8, 6));
        col.add(header, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(BG_COLOR);
        col.add(scroll, BorderLayout.CENTER);

        return col;
    }

    private JPanel createScrollableContainer() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_COLOR);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        return p;
    }

    /**
     * Clears all product lists.
     */
    public void clearProducts() {
        readyContainer.removeAll();
        validationContainer.removeAll();
    }

    /**
     * Adds a validated product to the ready list.
     */
    public void addReadyProduct(ProductoSegundaMano producto) {
        readyContainer.add(buildReadyCard(producto));
        readyContainer.add(Box.createVerticalStrut(10));
    }

    /**
     * Adds a product to the validation list.
     */
    public void addValidationProduct(ProductoSegundaMano producto, boolean pagado) {
        validationContainer.add(buildValidationCard(producto, pagado));
        validationContainer.add(Box.createVerticalStrut(10));
    }

    /**
     * Displays empty placeholders if containers have no products.
     */
    public void ensurePlaceholdersIfEmpty() {
        if (readyContainer.getComponentCount() == 0) {
            readyContainer.add(buildEmptyPlaceholder("No products ready for interchange"));
        }
        if (validationContainer.getComponentCount() == 0) {
            validationContainer.add(buildEmptyPlaceholder("No products awaiting validation"));
        }
        readyContainer.revalidate();
        readyContainer.repaint();
        validationContainer.revalidate();
        validationContainer.repaint();
    }

    private JPanel buildReadyCard(ProductoSegundaMano producto) {
        JPanel card = createCardPanel();
        card.add(createImagePanel(producto.getFoto()), BorderLayout.WEST);

        JPanel rightSide = new JPanel(new BorderLayout(0, 6));
        rightSide.setOpaque(false);
        rightSide.setBorder(new EmptyBorder(6, 10, 6, 6));

        JPanel info = new JPanel(new GridLayout(0, 1, 2, 2));
        info.setOpaque(false);

        addInfoRow(info, "PRODUCT NAME:", producto.getNombre());
        addInfoRow(info, "CONDITION:",
                producto.getDatosValidacion() != null
                        ? producto.getDatosValidacion().getEstadoConservacion().toString()
                        : "-");
        addInfoRow(info, "DESCRIPTION:", producto.getDescripcion() != null ? producto.getDescripcion() : "-");
        addInfoRow(info, "VALUE:",
                producto.getDatosValidacion() != null
                        ? String.format("%.2f €", producto.getDatosValidacion().getPrecioEstimadoProducto())
                        : "-");

        rightSide.add(info, BorderLayout.CENTER);

        if (producto.isPendienteAprobacionIntercambio()) {
            JLabel pendingLabel = new JLabel("⏳ PENDING EXCHANGE APPROVAL");
            pendingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            pendingLabel.setForeground(new Color(180, 100, 0));
            rightSide.add(pendingLabel, BorderLayout.SOUTH);
        } else if (producto.estaEnOferta()) {
            JLabel offerLabel = new JLabel("📌 INCLUDED IN AN OFFER");
            offerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            offerLabel.setForeground(new Color(200, 80, 30));
            rightSide.add(offerLabel, BorderLayout.SOUTH);
        }

        card.add(rightSide, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildValidationCard(ProductoSegundaMano producto, boolean pagado) {
        JPanel card = createCardPanel();
        card.add(createImagePanel(producto.getFoto()), BorderLayout.WEST);

        JPanel rightSide = new JPanel(new BorderLayout(0, 6));
        rightSide.setOpaque(false);
        rightSide.setBorder(new EmptyBorder(6, 10, 6, 6));

        JPanel info = new JPanel(new GridLayout(0, 1, 2, 2));
        info.setOpaque(false);
        addInfoRow(info, "PRODUCT NAME:", producto.getNombre());
        addInfoRow(info, "CONDITION:",
                producto.getDatosValidacion() != null
                        ? producto.getDatosValidacion().getEstadoConservacion().toString()
                        : "Pending");
        addInfoRow(info, "DESCRIPTION:", producto.getDescripcion() != null ? producto.getDescripcion() : "-");

        rightSide.add(info, BorderLayout.CENTER);

        JPanel actionArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionArea.setOpaque(false);

        if (!producto.isValidado()) {
            JLabel pending = new JLabel("⏳ Awaiting validation...");
            pending.setFont(LABEL_FONT);
            pending.setForeground(new Color(180, 100, 0));
            actionArea.add(pending);
        } else if (!pagado) {
            actionArea.add(createPayButton(producto));
        } else {
            JLabel paidLabel = new JLabel("✔ VALIDATION PAID");
            paidLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            paidLabel.setForeground(PAID_FG);
            actionArea.add(paidLabel);
        }

        rightSide.add(actionArea, BorderLayout.SOUTH);
        card.add(rightSide, BorderLayout.CENTER);
        return card;
    }

    private JButton createPayButton(ProductoSegundaMano producto) {
        JButton btn = new JButton("PAY VALIDATION");
        btn.setFont(BTN_PAY_FONT);
        btn.setBackground(BTN_PAY_BG);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 28));
        btn.setActionCommand("PAY_VALIDATION:" + producto.getID());

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(BTN_PAY_BG.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(BTN_PAY_BG); }
        });
        if (this.payValidationListener != null) {
            btn.addActionListener(this.payValidationListener);
        }
        return btn;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(new LineBorder(CARD_BORDER, 1), new EmptyBorder(4, 4, 4, 4)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    private JPanel createImagePanel(File imageFile) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(200, 215, 230));
        panel.setPreferredSize(new Dimension(150, 140));
        panel.setMaximumSize(new Dimension(150, 140));

        if (imageFile != null && imageFile.exists()) {
            JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(150, 140, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
            panel.add(lblFoto, BorderLayout.CENTER);
        } else {
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        return panel;
    }

    private void addInfoRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(LABEL_FG);
        JLabel val = new JLabel(value);
        val.setFont(VALUE_FONT);
        val.setForeground(VALUE_FG);
        row.add(lbl);
        row.add(val);
        parent.add(row);
    }

    private JPanel buildEmptyPlaceholder(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
        lbl.setForeground(new Color(90, 110, 150));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    /**
     * Registers a listener for adding a new product.
     */
    public void addAddProductListener(ActionListener l) {
        btnAddProduct.addActionListener(l);
        btnAddProduct.setActionCommand("ADD_NEW_PRODUCT");
    }

    /**
     * Registers a global listener for validation payment.
     */
    public void addPayValidationListener(ActionListener l) {
        this.payValidationListener = l;
    }

    public HeaderPanel getHeaderPanel()    { return headerPanel; }
    public JPanel getReadyContainer()      { return readyContainer; }
    public JPanel getValidationContainer() { return validationContainer; }
    public JButton getBtnAddProduct()      { return btnAddProduct; }
}