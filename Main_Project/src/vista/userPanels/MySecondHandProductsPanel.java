package vista.userPanels;

import modelo.aplicacion.Aplicacion;
import modelo.producto.EstadoConservacion;
import modelo.producto.ProductoSegundaMano;
import modelo.solicitud.SolicitudValidacion;
import modelo.usuario.ClienteRegistrado;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.File;

/**
 * Panel que muestra los productos de segunda mano del cliente.
 *
 * Estructura:
 *  - Banner "MY SECOND-HAND PRODUCTS"
 *  - Cuerpo dividido en dos columnas scrollables:
 *      · INTERCHANGE-READY   (productos validados y disponibles)
 *      · AWAITING VALIDATION (productos pendientes / ya pagados)
 *  - Botón "ADD NEW PRODUCT" en la parte inferior
 *
 * Sigue el patrón MVC: NO contiene lógica de negocio.
 * El controlador llama a cargarProductos() para rellenar las listas.
 */
public class MySecondHandProductsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // ── Paleta ──────────────────────────────────────────────────────────────
    private static final Color BG_COLOR      = new Color(153, 180, 209);
    private static final Color BANNER_COLOR  = new Color(74, 118, 201);
    private static final Color CARD_BG       = new Color(188, 210, 232);
    private static final Color CARD_BORDER   = new Color(120, 150, 185);
    private static final Color HEADER_LEFT   = new Color(60,  100, 180);
    private static final Color HEADER_RIGHT  = new Color(60,  100, 180);
    private static final Color BTN_ADD_BG    = new Color(255, 220,  50);
    private static final Color BTN_ADD_FG    = new Color( 30,  30,  30);
    private static final Color BTN_PAY_BG    = new Color( 50, 180,  70);
    private static final Color LABEL_FG      = new Color( 30,  50,  90);
    private static final Color VALUE_FG      = new Color( 20,  20,  60);
    private static final Color PAID_FG       = new Color( 50, 150,  50);

    private static final Font BANNER_FONT  = new Font("Comic Sans MS", Font.BOLD, 22);
    private static final Font HEADER_FONT  = new Font("Comic Sans MS", Font.BOLD, 14);
    private static final Font LABEL_FONT   = new Font("Comic Sans MS", Font.BOLD, 11);
    private static final Font VALUE_FONT   = new Font("Comic Sans MS", Font.PLAIN, 11);
    private static final Font BTN_ADD_FONT = new Font("Comic Sans MS", Font.BOLD, 16);
    private static final Font BTN_PAY_FONT = new Font("Comic Sans MS", Font.BOLD, 11);

    // ── Contenedores de tarjetas ────────────────────────────────────────────
    private HeaderPanel headerPanel;
    private JPanel readyContainer;        // columna izquierda
    private JPanel validationContainer;   // columna derecha

    // ── Botón de añadir ─────────────────────────────────────────────────────
    private JButton btnAddProduct;

    // ── Listeners registrados por el controlador ────────────────────────────
    private ActionListener addProductListener;
    // Listener para botones PAY_VALIDATION; el controlador lo registrará aquí
    private ActionListener payValidationListener;

    // =====================================================================
    //  Constructor
    // =====================================================================
    public MySecondHandProductsPanel() {
        initComponents();
        initLayout();
    }

    // =====================================================================
    //  Inicialización
    // =====================================================================
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
        
        // ── Cabecera Global ────────────────────────────────────────────────
        headerPanel = new HeaderPanel();
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(BG_COLOR);
        topWrapper.add(headerPanel, BorderLayout.NORTH);

        // ── Banner ─────────────────────────────────────────────────────────
        JLabel banner = new JLabel("MY SECOND-HAND PRODUCTS", SwingConstants.CENTER);
        banner.setFont(BANNER_FONT);
        banner.setForeground(Color.WHITE);
        banner.setOpaque(true);
        banner.setBackground(BANNER_COLOR);
        banner.setBorder(new EmptyBorder(14, 0, 14, 0));
        topWrapper.add(banner, BorderLayout.SOUTH);
        
        add(topWrapper, BorderLayout.NORTH);

        // ── Cuerpo: dos columnas ───────────────────────────────────────────
        JPanel body = new JPanel(new GridLayout(1, 2, 12, 0));
        body.setBackground(BG_COLOR);
        body.setBorder(new EmptyBorder(14, 14, 10, 14));

        body.add(buildColumn("INTERCHANGE-READY", HEADER_LEFT, readyContainer));
        body.add(buildColumn("AWAITING VALIDATION", HEADER_RIGHT, validationContainer));

        add(body, BorderLayout.CENTER);

        // ── Botón inferior ─────────────────────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottom.setBackground(BG_COLOR);
        bottom.setBorder(new EmptyBorder(6, 0, 12, 0));
        bottom.add(btnAddProduct);
        add(bottom, BorderLayout.SOUTH);
    }

    // =====================================================================
    //  Helpers de construcción de UI
    // =====================================================================

    /** Crea una columna con cabecera y scroll. */
    private JPanel buildColumn(String title, Color headerColor, JPanel container) {
        JPanel col = new JPanel(new BorderLayout(0, 6));
        col.setBackground(BG_COLOR);
        col.setBorder(new LineBorder(new Color(100, 130, 175), 2));

        // Cabecera
        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(headerColor);
        header.setBorder(new EmptyBorder(8, 6, 8, 6));
        col.add(header, BorderLayout.NORTH);

        // Scroll con tarjetas
        JScrollPane scroll = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(BG_COLOR);
        col.add(scroll, BorderLayout.CENTER);

        return col;
    }

    /** Crea el contenedor vertical que irá dentro del scroll. */
    private JPanel createScrollableContainer() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_COLOR);
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        return p;
    }

    // =====================================================================
    //  API pública – llamada por el controlador
    // =====================================================================

    /**
     * Vacía ambas columnas y recarga los productos del cliente actual.
     * También puede llamarse cuando cambia el estado de un producto.
     */
    public void cargarProductos() {
        // NOTE: Este método quedará obsoleto en favor de la responsabilidad del controlador.
        // Mantenerlo por compatibilidad llamando a los nuevos helpers.
        readyContainer.removeAll();
        validationContainer.removeAll();
        readyContainer.revalidate();
        validationContainer.revalidate();
    }

    // ------------------------------------------------------------------
    // API que el controlador usará para poblar la vista (sin lógica de negocio)
    // ------------------------------------------------------------------
    public void clearProducts() {
        readyContainer.removeAll();
        validationContainer.removeAll();
    }

    public void addReadyProduct(ProductoSegundaMano producto) {
        readyContainer.add(buildReadyCard(producto));
        readyContainer.add(Box.createVerticalStrut(10));
    }

    public void addValidationProduct(ProductoSegundaMano producto, boolean pagado) {
        validationContainer.add(buildValidationCard(producto, pagado));
        validationContainer.add(Box.createVerticalStrut(10));
    }

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

    // ── Tarjeta izquierda (INTERCHANGE-READY) ───────────────────────────────
    private JPanel buildReadyCard(ProductoSegundaMano producto) {
        JPanel card = createCardPanel();

        // Imagen del producto (o placeholder si no tiene)
        card.add(createImagePanel(producto.getFoto()), BorderLayout.WEST);

        // Info + Estado
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
            JPanel actionArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            actionArea.setOpaque(false);
            JLabel pendingLabel = new JLabel("⏳ PENDING EXCHANGE APPROVAL");
            pendingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            pendingLabel.setForeground(new Color(180, 100, 0));
            actionArea.add(pendingLabel);
            rightSide.add(actionArea, BorderLayout.SOUTH);
        } else if (producto.estaEnOferta()) {
            JPanel actionArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            actionArea.setOpaque(false);
            JLabel offerLabel = new JLabel("📌 INCLUDED IN AN OFFER");
            offerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            offerLabel.setForeground(new Color(200, 80, 30));
            actionArea.add(offerLabel);
            rightSide.add(actionArea, BorderLayout.SOUTH);
        }

        card.add(rightSide, BorderLayout.CENTER);
        return card;
    }

    // ── Tarjeta derecha (AWAITING VALIDATION) ───────────────────────────────
    private JPanel buildValidationCard(ProductoSegundaMano producto, boolean pagado) {
        JPanel card = createCardPanel();

        // Imagen del producto (o placeholder si no tiene)
        card.add(createImagePanel(producto.getFoto()), BorderLayout.WEST);

        // Info + botón
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

        // Acción inferior según estado
        JPanel actionArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionArea.setOpaque(false);

        boolean validated = producto.isValidado();
        if (!validated) {
            JLabel pending = new JLabel("⏳ Awaiting validation...");
            pending.setFont(LABEL_FONT);
            pending.setForeground(new Color(180, 100, 0));
            actionArea.add(pending);
        } else if (!pagado) {
            JButton btnPay = createPayButton(producto);
            actionArea.add(btnPay);
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

    /** Crea un botón PAY VALIDATION con su action command. */
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
        // Si el controlador ya registró un listener global, lo añadimos aquí
        if (this.payValidationListener != null) {
            btn.addActionListener(this.payValidationListener);
        }
        return btn;
    }

    // ── Utilidades de construcción ───────────────────────────────────────────

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(CARD_BORDER, 1),
                new EmptyBorder(4, 4, 4, 4)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    private JPanel createImagePanel(File imageFile) {
        if (imageFile != null && imageFile.exists()) {
            JPanel imgPanel = new JPanel(new BorderLayout());
            imgPanel.setBackground(new Color(200, 215, 230));
            imgPanel.setPreferredSize(new Dimension(150, 140));
            imgPanel.setMinimumSize(new Dimension(150, 140));
            imgPanel.setMaximumSize(new Dimension(150, 140));

            JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(150, 140, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
            imgPanel.add(lblFoto, BorderLayout.CENTER);
            return imgPanel;
        }

        // Si no hay archivo, mostramos el placeholder original
        JPanel img = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.GRAY);
                g2.drawLine(0, 0, getWidth(), getHeight());
                g2.drawLine(getWidth(), 0, 0, getHeight());
                g2.setColor(new Color(160, 160, 160));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        img.setBackground(new Color(200, 215, 230));
        img.setPreferredSize(new Dimension(150, 140));
        img.setMinimumSize(new Dimension(150, 140));
        img.setMaximumSize(new Dimension(150, 140));
        return img;
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
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
        lbl.setForeground(new Color(90, 110, 150));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    // =====================================================================
    //  Métodos para registrar listeners (MVC)
    // =====================================================================

    /** Registra un listener para el botón "ADD NEW PRODUCT". */
    public void addAddProductListener(ActionListener l) {
        btnAddProduct.addActionListener(l);
        btnAddProduct.setActionCommand("ADD_NEW_PRODUCT");
    }

    /**
     * Registra un listener global para todos los botones "PAY VALIDATION".
     * El action command tiene el formato "PAY_VALIDATION:<idProducto>".
     * Llama a este método ANTES de cargarProductos() para que las tarjetas
     * futuras también reciban el listener, o vuelve a llamarlo tras recargar.
     */
    public void addPayValidationListener(ActionListener l) {
        // Guardamos el listener para que los botones creados posteriormente lo reciban.
        // El controlador debe llamar a este método ANTES de poblar la vista.
        this.payValidationListener = l;
    }

    private void attachListenerToButtons(Container parent, ActionListener l, String commandPrefix) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JButton btn) {
                String cmd = btn.getActionCommand();
                if (cmd != null && cmd.startsWith(commandPrefix)) {
                    btn.addActionListener(l);
                }
            }
            if (c instanceof Container sub) {
                attachListenerToButtons(sub, l, commandPrefix);
            }
        }
    }

    // =====================================================================
    //  Acceso a subpaneles (por si el controlador necesita referencias)
    // =====================================================================
    public HeaderPanel getHeaderPanel()    { return headerPanel; }
    public JPanel getReadyContainer()      { return readyContainer; }
    public JPanel getValidationContainer() { return validationContainer; }
    public JButton getBtnAddProduct()      { return btnAddProduct; }

    // =====================================================================
    //  Main de prueba rápida
    // =====================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My Second-Hand Products – preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 760);

            MySecondHandProductsPanel panel = new MySecondHandProductsPanel();

            // Datos de demo (sin modelo real)
            panel.readyContainer.add(panel.buildDemoReadyCard("Peluche de perro", "VERY_GOOD", "Un peluche suave", "10.00 €"));
            panel.readyContainer.add(Box.createVerticalStrut(10));
            panel.readyContainer.add(panel.buildDemoReadyCard("Comic Spiderman", "PERFECT", "Primera edición", "25.00 €"));
            panel.readyContainer.add(Box.createVerticalStrut(10));

            panel.validationContainer.add(panel.buildDemoValidationCard("Camión de bomberos", "Juguete infantil", "PENDING", false));
            panel.validationContainer.add(Box.createVerticalStrut(10));
            panel.validationContainer.add(panel.buildDemoValidationCard("Figura Iron Man", "Edición limitada", "PAY", false));
            panel.validationContainer.add(Box.createVerticalStrut(10));
            panel.validationContainer.add(panel.buildDemoValidationCard("Juego de mesa", "Casi nuevo", "PAID", true));
            panel.validationContainer.add(Box.createVerticalStrut(10));

            frame.setContentPane(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Helpers solo para el main de demo
    private JPanel buildDemoReadyCard(String name, String condition, String desc, String value) {
        JPanel card = createCardPanel();
        card.add(createImagePanel(null), BorderLayout.WEST);
        JPanel info = new JPanel(new GridLayout(0, 1, 2, 2));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(6, 10, 6, 6));
        addInfoRow(info, "PRODUCT NAME:", name);
        addInfoRow(info, "CONDITION:", condition);
        addInfoRow(info, "DESCRIPTION:", desc);
        addInfoRow(info, "VALUE:", value);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildDemoValidationCard(String name, String desc, String state, boolean paid) {
        JPanel card = createCardPanel();
        card.add(createImagePanel(null), BorderLayout.WEST);
        JPanel right = new JPanel(new BorderLayout(0, 6));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(6, 10, 6, 6));
        JPanel info = new JPanel(new GridLayout(0, 1, 2, 2));
        info.setOpaque(false);
        addInfoRow(info, "PRODUCT NAME:", name);
        addInfoRow(info, "CONDITION:", state.equals("PENDING") ? "Pending" : state);
        addInfoRow(info, "DESCRIPTION:", desc);
        right.add(info, BorderLayout.CENTER);
        JPanel action = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        action.setOpaque(false);
        if (state.equals("PENDING")) {
            JLabel lbl = new JLabel("⏳ Awaiting validation...");
            lbl.setFont(LABEL_FONT);
            lbl.setForeground(new Color(180, 100, 0));
            action.add(lbl);
        } else if (state.equals("PAY")) {
            JButton btn = new JButton("PAY VALIDATION");
            btn.setFont(BTN_PAY_FONT);
            btn.setBackground(BTN_PAY_BG);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            btn.setPreferredSize(new Dimension(140, 28));
            action.add(btn);
        } else {
            JLabel lbl = new JLabel("✔ VALIDATION PAID");
            lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            lbl.setForeground(PAID_FG);
            action.add(lbl);
        }
        right.add(action, BorderLayout.SOUTH);
        card.add(right, BorderLayout.CENTER);
        return card;
    }
}