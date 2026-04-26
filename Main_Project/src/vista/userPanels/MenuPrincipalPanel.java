package vista.userPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel principal de usuario (Menu Principal).
*/
public class MenuPrincipalPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // --- Colores de la paleta del Mockup ---
    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 
    private final Color BANNER_SUB_COLOR = new Color(131, 117, 181); 
    private final Color BUY_NOW_COLOR = new Color(255, 204, 0);      

    // --- Componentes de la Cabecera ---
    private JLabel logoLabel;
    private JButton btnHome;
    private JButton btnDescuentos;
    private JButton btnOutstanding;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnFilters;

    // Botones de iconos
    private JButton btnCarrito;    private JButton btnIntercambios;
    private JButton btnConfiguracion;
    private JButton btnPerfil;
    private JButton btnNotificaciones;

    // --- Contenedores Principales ---
    private JPanel recommendedPanel;
    private JPanel categoriesPanel;

    // --- Listas para gestión eficiente de Listeners ---
    private List<JButton> buyNowButtons;
    private List<JButton> categoryButtons;

    public MenuPrincipalPanel() {
        buyNowButtons = new ArrayList<>();
        categoryButtons = new ArrayList<>();
        
        initComponents();
        initLayout();
    }

    private void initComponents() {
        // Logo de la app
        logoLabel = new JLabel();
        try {
            File logoFile = new File("src/assets/appicon.png");
            if(logoFile.exists()) {
                Image img = javax.imageio.ImageIO.read(logoFile);
                Image scaledLogo = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledLogo));
            } else {
                logoLabel.setText("COMIC SANS");
            }
        } catch (Exception e) {
            logoLabel.setText("COMIC SANS");
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        }

        // Navegación izquierda 
        btnHome = createTopNavButton("HOME 🏠");
        btnDescuentos = createTopNavButton("DISCOUNTS %");
        btnOutstanding = createTopNavButton("OUTSTANDING ⭐");

        // Búsqueda y filtros 
        txtSearch = new JTextField(20) { 
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g);
            }
        };
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14)); 
        txtSearch.setBorder(new EmptyBorder(6, 12, 6, 12)); 
        
        btnFilters = new JButton("⚙ Filtros");
        btnFilters.setFont(new Font("SansSerif", Font.BOLD, 14)); 
        btnFilters.setContentAreaFilled(false);
        btnFilters.setBorderPainted(false);
        btnFilters.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSearch = createImageIconButton("src/assets/lupa.png", 25, 25, "SEARCH");

        // Carga de iconos estricta (Te avisará por consola si algo falla)
        btnCarrito = createImageIconButton("src/assets/carrito.png", 35, 35, "CART");
        btnIntercambios = createImageIconButton("src/assets/intercambio.png", 35, 35, "INT");
        btnConfiguracion = createImageIconButton("src/assets/configuracion.png", 35, 35, "CFG"); 
        btnPerfil = createImageIconButton("src/assets/fotoperfil.png", 35, 35, "PERF");
        btnNotificaciones = createImageIconButton("src/assets/notificaciones.png", 35, 35, "NOTI"); 
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // ==========================================
        // 1. HEADER (NORTE)
        // ==========================================
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        
        JLabel speechBubble = new JLabel("<html><center>YOUR FAVORITE STORE<br>IS NOW ONLINE!!</center></html>");
        speechBubble.setBackground(Color.WHITE);
        speechBubble.setOpaque(true);
        speechBubble.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1, false), new EmptyBorder(10, 20, 10, 20)));
        leftPanel.add(speechBubble);

        JPanel centerNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        centerNavPanel.setOpaque(false);
        centerNavPanel.add(btnHome);
        centerNavPanel.add(btnDescuentos);
        centerNavPanel.add(btnOutstanding);

        JPanel rightPanel = new JPanel(new BorderLayout(15, 0)); 
        rightPanel.setOpaque(false);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 20));
        searchPanel.setOpaque(false);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnFilters);

        // Fila 1 de iconos
        JPanel iconsTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconsTop.setOpaque(false);
        iconsTop.add(btnCarrito);
        iconsTop.add(btnIntercambios);
        iconsTop.add(btnConfiguracion);
        iconsTop.add(btnPerfil);

        // Fila 2 de iconos (Notificaciones debajo)
        JPanel iconsBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconsBottom.setOpaque(false);
        iconsBottom.add(btnNotificaciones);

        JPanel iconsContainer = new JPanel(new BorderLayout());
        iconsContainer.setOpaque(false);
        iconsContainer.add(iconsTop, BorderLayout.NORTH);
        iconsContainer.add(iconsBottom, BorderLayout.SOUTH);

        rightPanel.add(searchPanel, BorderLayout.CENTER);
        rightPanel.add(iconsContainer, BorderLayout.EAST);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(centerNavPanel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ==========================================
        // 2. BODY (CENTRO)
        // ==========================================
        JPanel bodyContent = new JPanel();
        bodyContent.setLayout(new BoxLayout(bodyContent, BoxLayout.Y_AXIS));
        bodyContent.setBackground(BG_COLOR);
        bodyContent.setBorder(new EmptyBorder(0, 20, 20, 20));

        bodyContent.add(createBanner("MAIN MENU", BANNER_MAIN_COLOR, 28));
        bodyContent.add(Box.createVerticalStrut(10));
        bodyContent.add(createBanner("YOU MIGHT LIKE THIS", BANNER_SUB_COLOR, 18));
        bodyContent.add(Box.createVerticalStrut(15));

        recommendedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        recommendedPanel.setBackground(BANNER_MAIN_COLOR); 
        recommendedPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        
        //Esto hay que cambiarlo 100% por una consulta real a la base de datos, pero por ahora lo dejo hardcodeado para que veas el diseño con varios productos
        
        for (int i = 1; i <= 5; i++) {
            recommendedPanel.add(createProductCard("19.99 $", "PROD_" + i));
        }

        JPanel recWrapper = new JPanel(new BorderLayout());
        recWrapper.setBackground(BANNER_MAIN_COLOR);
        recWrapper.add(recommendedPanel, BorderLayout.CENTER);

        JScrollPane scrollProducts = new JScrollPane(recWrapper);
        scrollProducts.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollProducts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollProducts.setBorder(null);
        scrollProducts.setPreferredSize(new Dimension(1000, 240));
        
        bodyContent.add(scrollProducts);
        bodyContent.add(Box.createVerticalStrut(20));

        bodyContent.add(createBanner("TYPE OF PRODUCTS", BANNER_SUB_COLOR, 18));
        bodyContent.add(Box.createVerticalStrut(15));

        categoriesPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        categoriesPanel.setOpaque(false);
        
        categoriesPanel.add(createCategoryCard("BOARD GAMES", "BOARD_GAMES", "src/assets/juegodemesa.png"));
        categoriesPanel.add(createCategoryCard("COMICS", "COMICS", "src/assets/comic.png"));
        categoriesPanel.add(createCategoryCard("FIGURES", "FIGURES", "src/assets/figuraGoku.png"));

        bodyContent.add(categoriesPanel);

        JScrollPane mainScroll = new JScrollPane(bodyContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE CREACIÓN DE UI
    // ==========================================

    private JButton createTopNavButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Crea un botón cargando una imagen desde los assets.
     * Utiliza ImageIO para detectar "falsos PNG" y anula los márgenes para evitar los "..."
     */
    private JButton createImageIconButton(String imagePath, int width, int height, String fallbackText) {
        JButton btn = new JButton();
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Quitamos los márgenes por defecto de Swing para que quepa el texto de emergencia
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setPreferredSize(new Dimension(width + 5, height + 5)); 
        
        try {
            File imgFile = new File(imagePath);
            if (!imgFile.exists()) {
                System.err.println(" ERROR: No se encuentra el archivo " + imagePath);
                setFallback(btn, fallbackText);
                return btn;
            }
            
            // ImageIO leerá los bytes reales. Si es un falso PNG, devolverá null o lanzará excepción
            Image img = javax.imageio.ImageIO.read(imgFile);
            if (img == null) {
                System.err.println(" ERROR: Java no reconoce el formato de " + imagePath + ". ¡Seguramente sea un AVIF renombrado a mano!");
                setFallback(btn, fallbackText);
                return btn;
            }
            
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaledImg));
            
        } catch (Exception e) {
            System.err.println(" ERROR al leer la imagen " + imagePath + ": " + e.getMessage());
            setFallback(btn, fallbackText);
        }
        return btn;
    }

    private void setFallback(JButton btn, String text) {
        btn.setText(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 10)); // Letra pequeña para que no se recorte
        btn.setForeground(Color.RED); // En rojo para que veas claramente que ha fallado
    }

    private JPanel createBanner(String text, Color bgColor, int fontSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProductCard(String price, String productId) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_COLOR);
        card.setPreferredSize(new Dimension(180, 200));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel imagePlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                g.drawLine(0, 0, getWidth(), getHeight());
                g.drawLine(getWidth(), 0, 0, getHeight());
                g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            }
        };
        imagePlaceholder.setBackground(Color.WHITE);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblPrice = new JLabel(price, SwingConstants.CENTER);
        lblPrice.setOpaque(true);
        lblPrice.setBackground(new Color(0, 204, 204));
        lblPrice.setBorder(new LineBorder(Color.BLACK, 1));
        
        JButton btnBuy = new JButton("BUY NOW") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
            }
        };
        btnBuy.setContentAreaFilled(false);
        btnBuy.setBackground(BUY_NOW_COLOR);
        btnBuy.setForeground(Color.RED);
        btnBuy.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnBuy.setBorder(new EmptyBorder(5, 10, 5, 10));
        btnBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuy.setActionCommand(productId);
        
        buyNowButtons.add(btnBuy);

        bottom.add(lblPrice, BorderLayout.CENTER);
        bottom.add(btnBuy, BorderLayout.EAST);

        card.add(imagePlaceholder, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCategoryCard(String title, String actionCommand, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(136, 212, 204)); 
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 2),
                new EmptyBorder(10, 10, 10, 10)));
        card.setPreferredSize(new Dimension(300, 200));

        JButton btnCat = new JButton(title);
        btnCat.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCat.setBackground(Color.WHITE);
        btnCat.setFocusPainted(false);
        btnCat.setActionCommand(actionCommand);
        btnCat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        categoryButtons.add(btnCat);

        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(Color.WHITE);
        try {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Image img = javax.imageio.ImageIO.read(imgFile);
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(180, 140, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(scaledImg));
                } else {
                    imgLabel.setText("NO IMAGE");
                }
            } else {
                imgLabel.setText("NO IMAGE");
            }
        } catch (Exception e) {
            imgLabel.setText("NO IMAGE");
        }

        card.add(btnCat, BorderLayout.NORTH);
        card.add(imgLabel, BorderLayout.CENTER);

        return card;
    }

    // ==========================================
    // MÉTODOS PARA REGISTRAR LISTENERS
    // ==========================================
    public void addHomeListener(ActionListener l) { btnHome.addActionListener(l); }
    public void addDescuentosListener(ActionListener l) { btnDescuentos.addActionListener(l); }
    public void addOutstandingListener(ActionListener l) { btnOutstanding.addActionListener(l); }
    
    
    public void addSearchListener(ActionListener l) { 
        txtSearch.addActionListener(l); 
        btnSearch.addActionListener(e -> {
            l.actionPerformed(new java.awt.event.ActionEvent(txtSearch, java.awt.event.ActionEvent.ACTION_PERFORMED, txtSearch.getText()));
        });
    }
    public void addFiltrosListener(ActionListener l) { btnFilters.addActionListener(l); }
    
    public void addCarritoListener(ActionListener l) { btnCarrito.addActionListener(l); }
    public void addIntercambiosListener(ActionListener l) { btnIntercambios.addActionListener(l); }
    public void addConfiguracionListener(ActionListener l) { btnConfiguracion.addActionListener(l); }
    public void addPerfilListener(ActionListener l) { btnPerfil.addActionListener(l); }
    public void addNotificacionesListener(ActionListener l) { btnNotificaciones.addActionListener(l); }

    public void addBuyNowListener(ActionListener l) {
        for (JButton btn : buyNowButtons) {
            btn.addActionListener(l);
        }
    }

    public void addCategoryListener(ActionListener l) {
        for (JButton btn : categoryButtons) {
            btn.addActionListener(l);
        }
    }
    
}