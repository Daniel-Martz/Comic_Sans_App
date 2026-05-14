package vista.userPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import modelo.tiempo.DateTimeSimulado;

// TODO: Auto-generated Javadoc
/**
 * The Class HeaderPanel.
 */
public class HeaderPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The logo label. */
    private JLabel logoLabel;
    
    /** The btn home. */
    private JButton btnHome;
    
    /** The btn descuentos. */
    private JButton btnDescuentos;
    
    /** The btn outstanding. */
    private JButton btnOutstanding;
    
    /** The txt search. */
    private JTextField txtSearch;
    
    /** The btn filters. */
    private JButton btnFilters;
    
    /** The btn carrito. */
    private JButton btnCarrito;
    
    /** The btn intercambios. */
    private JButton btnIntercambios;
    
    /** The btn perfil. */
    private JButton btnPerfil;
    
    /** The btn notificaciones. */
    private JButton btnNotificaciones;
    
    /** The btn search icon. */
    private JButton btnSearchIcon;
    
    /** The speech bubble. */
    private JLabel speechBubble;
    
    /** The lbl date. */
    private JLabel lblDate;
    
    /** The search box. */
    private JPanel searchBox;
    
    /** The left panel. */
    private JPanel leftPanel;
    
    /** The right panel. */
    private JPanel rightPanel;
    
    /** The center nav panel. */
    private JPanel centerNavPanel;

    /** The icono sin log in. */
    ImageIcon iconoSinLogIn = getSclaedIcon("src/assets/fotoperfil.png");
    
    /** The icono con log in. */
    ImageIcon iconoConLogIn = getSclaedIcon("src/assets/fotoperfilLoggedIn.png");

    /**
     * Instantiates a new header panel.
     */
    public HeaderPanel() {
        initComponents();
        initLayout();
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateDate();
            }
        });
    }
    
    /**
     * Gets the sclaed icon.
     *
     * @param path the path
     * @return the sclaed icon
     */
    public ImageIcon getSclaedIcon(String path) {
        try {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Image img = javax.imageio.ImageIO.read(imgFile);
                Image scaledImg = img.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }
    
    /**
     * Refresh icon image.
     *
     * @param isLoggedIn the is logged in
     */
    public void refreshIconImage(boolean isLoggedIn) {
    	if(isLoggedIn) {
			btnPerfil.setIcon(this.iconoConLogIn);
    	}else {
    		btnPerfil.setIcon(this.iconoSinLogIn);
    	}
    }

    /**
     * Update date.
     */
    public void updateDate() {
        if (lblDate != null) {
            DateTimeSimulado ahora = new DateTimeSimulado();
            String timeStr = String.format("%02d:%02d", ahora.getHora(), ahora.getMinuto());
            lblDate.setText("Simulated Time: " + ahora.toStringFecha() + "   " + timeStr);
        }
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
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
            logoLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        }

        btnHome = createTopNavButton("HOME 🏠");
        btnDescuentos = createTopNavButton("DISCOUNTS %");
        btnOutstanding = createTopNavButton("OUTSTANDING ⭐");

        txtSearch = new JTextField(20);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Comic Sans MS", Font.PLAIN, 14)); 
        txtSearch.setBorder(null); // Sin borde, el borde lo dibujará el contenedor
        txtSearch.setText("Search...");
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
        
        btnSearchIcon = new JButton();
        btnSearchIcon.setContentAreaFilled(false);
        btnSearchIcon.setBorderPainted(false);
        btnSearchIcon.setFocusPainted(false);
        btnSearchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearchIcon.setMargin(new Insets(0, 0, 0, 0));
        try {
            File lupaFile = new File("src/assets/lupa.png");
            if(lupaFile.exists()) {
                Image img = javax.imageio.ImageIO.read(lupaFile);
                Image scaledLupa = img.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                btnSearchIcon.setIcon(new ImageIcon(scaledLupa));
            } else {
                btnSearchIcon.setText("🔍");
            }
        } catch (Exception e) {
            btnSearchIcon.setText("🔍");
        }

        btnFilters = new JButton("⚙ Filters");
        btnFilters.setFont(new Font("Comic Sans MS", Font.BOLD, 14)); 
        btnFilters.setContentAreaFilled(false);
        btnFilters.setBorderPainted(false);
        btnFilters.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCarrito = createImageIconButton("src/assets/carrito.png", 35, 35, "CART");
        btnIntercambios = createImageIconButton("src/assets/intercambio.png", 35, 35, "INT");
        btnPerfil = createImageIconButton("src/assets/fotoperfil.png", 35, 35, "PERF");
        btnNotificaciones = createImageIconButton("src/assets/notificaciones.png", 35, 35, "NOTI"); 
        
        lblDate = new JLabel();
        lblDate.setFont(new Font("Comic Sans MS", Font.ITALIC, 11));
        lblDate.setForeground(Color.DARK_GRAY);
        lblDate.setHorizontalAlignment(SwingConstants.CENTER);
        updateDate();
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 10, 15));

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        
        speechBubble = new JLabel("<html><center>YOUR FAVORITE STORE<br>IS NOW ONLINE!!</center></html>");
        speechBubble.setBackground(Color.WHITE);
        speechBubble.setOpaque(true);
        speechBubble.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK, 1, false), new EmptyBorder(10, 20, 10, 20)));
        leftPanel.add(speechBubble);

        centerNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        centerNavPanel.setOpaque(false);
        centerNavPanel.add(btnHome);
        centerNavPanel.add(btnDescuentos);
        centerNavPanel.add(btnOutstanding);

        rightPanel = new JPanel(new BorderLayout(15, 0)); 
        rightPanel.setOpaque(false);

        // Contenedor con borde redondeado estilo "píldora" (Google)
        searchBox = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight()); // Completamente redondeado
                super.paintComponent(g);
                g2.dispose();
            }
        };
        searchBox.setOpaque(false);
        searchBox.setBorder(new EmptyBorder(6, 12, 6, 12));
        searchBox.add(btnSearchIcon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        searchPanel.setOpaque(false);
        searchPanel.add(btnFilters);
        searchPanel.add(searchBox);

        JPanel iconsTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconsTop.setOpaque(false);
        iconsTop.add(btnCarrito);
        iconsTop.add(btnIntercambios);
        iconsTop.add(btnPerfil);

        JPanel iconsBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconsBottom.setOpaque(false);
        iconsBottom.add(btnNotificaciones);

        JPanel iconsContainer = new JPanel(new BorderLayout());
        iconsContainer.setOpaque(false);
        iconsContainer.add(iconsTop, BorderLayout.NORTH);
        iconsContainer.add(iconsBottom, BorderLayout.SOUTH);

        rightPanel.add(searchPanel, BorderLayout.CENTER);
        rightPanel.add(iconsContainer, BorderLayout.EAST);

        add(lblDate, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerNavPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Añade un botón secundario al grupo de navegación central junto al HOME.
     * Devuelve el botón creado para que el controlador pueda añadir listeners.
     *
     * @param text the text
     * @return the j button
     */
    public JButton addSecondaryTopButton(String text) {
        JButton btn = createTopNavButton(text);
        // Insertamos justo después del HOME (posición 1)
        centerNavPanel.add(btn, 1);
        return btn;
    }

    /**
     * Oculta las cosas de comprar cuando entra un empleado.
     */
    public void configurarMenuEmpleado() {
        btnDescuentos.setVisible(false);
        btnOutstanding.setVisible(false);
        txtSearch.setVisible(false);
        btnSearchIcon.setVisible(false);
        if (searchBox != null) searchBox.setVisible(false);
        btnFilters.setVisible(false);
        btnCarrito.setVisible(false);
        btnIntercambios.setVisible(false);
        btnNotificaciones.setVisible(true);
        speechBubble.setText("<html><center>EMPLOYEE DASHBOARD<br>MANAGE THE STORE</center></html>");
        
        // Forzar simetría para que el botón Home quede perfectamente centrado
        leftPanel.setPreferredSize(new Dimension(380, leftPanel.getPreferredSize().height));
        rightPanel.setPreferredSize(new Dimension(380, rightPanel.getPreferredSize().height));
    }

    /**
     * Configurar menu gestor.
     */
    public void configurarMenuGestor() {
        configurarMenuEmpleado();
        speechBubble.setText("<html><center>MANAGER DASHBOARD<br>ADMINISTRATE THE STORE</center></html>");
        btnNotificaciones.setVisible(false);
    }

    /**
     * Deja la cabecera casi vacía para no estorbar en notificaciones.
     */
    public void configurarMenuNotificaciones() {
        // Ocultamos botones de navegación secundaria
        btnDescuentos.setVisible(false);
        btnOutstanding.setVisible(false);

        // Ocultamos la caja de búsqueda y filtros
        txtSearch.setVisible(false);
        btnSearchIcon.setVisible(false);
        if (searchBox != null) searchBox.setVisible(false);
        btnFilters.setVisible(false);

        // Ocultamos iconos de la derecha (carrito, intercambios, perfil, notificaciones)
        btnCarrito.setVisible(false);
        btnIntercambios.setVisible(false);
        btnPerfil.setVisible(false);
        btnNotificaciones.setVisible(false);

        // Ocultamos elementos del left panel (logo y bocadillo) para dejar sólo el HOME centrado
        logoLabel.setVisible(false);
        speechBubble.setVisible(false);

        // Forzar simetría para centrar el HOME perfectamente
        leftPanel.setPreferredSize(new Dimension(380, leftPanel.getPreferredSize().height));
        rightPanel.setPreferredSize(new Dimension(380, rightPanel.getPreferredSize().height));
    }

    /**
     * Creates the top nav button.
     *
     * @param text the text
     * @return the j button
     */
    private JButton createTopNavButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); super.paintComponent(g); g2.dispose(); }
            @Override protected void paintBorder(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(Color.BLACK); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); g2.dispose(); }
        };
        btn.setContentAreaFilled(false); btn.setBackground(Color.WHITE); btn.setFocusPainted(false); btn.setFont(new Font("Comic Sans MS", Font.BOLD, 14)); btn.setBorder(new EmptyBorder(10, 25, 10, 25)); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }

    /**
     * Creates the image icon button.
     *
     * @param imagePath the image path
     * @param width the width
     * @param height the height
     * @param fallbackText the fallback text
     * @return the j button
     */
    private JButton createImageIconButton(String imagePath, int width, int height, String fallbackText) {
        JButton btn = new JButton(); btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); btn.setMargin(new Insets(0, 0, 0, 0)); btn.setPreferredSize(new Dimension(width + 5, height + 5)); 
        try { File imgFile = new File(imagePath); if (!imgFile.exists()) { setFallback(btn, fallbackText); return btn; } Image img = javax.imageio.ImageIO.read(imgFile); if (img == null) { setFallback(btn, fallbackText); return btn; } Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); btn.setIcon(new ImageIcon(scaledImg)); } catch (Exception e) { setFallback(btn, fallbackText); } return btn;
    }

    /**
     * Sets the fallback.
     *
     * @param btn the btn
     * @param text the text
     */
    private void setFallback(JButton btn, String text) {
        btn.setText(text); btn.setFont(new Font("Comic Sans MS", Font.BOLD, 10)); btn.setForeground(Color.RED);
    }

    /**
     * Adds the home listener.
     *
     * @param l the l
     */
    // --- Delegación de Listeners ---
    /** @param l listener para el botón de inicio */
    public void addHomeListener(ActionListener l) { btnHome.addActionListener(l); }
    
    /**
     * Adds the outstanding listener.
     *
     * @param l the l
     */
    public void addOutstandingListener(ActionListener l) { btnOutstanding.addActionListener(l); }
    
    /**
     * Adds the search listener.
     *
     * @param l the l
     */
    public void addSearchListener(ActionListener l) { 
        txtSearch.addActionListener(l); 
        btnSearchIcon.addActionListener(e -> {
            l.actionPerformed(new java.awt.event.ActionEvent(txtSearch, java.awt.event.ActionEvent.ACTION_PERFORMED, getSearchText()));
        });
    }
    
    /**
     * Adds the filtros listener.
     *
     * @param l the l
     */
    public void addFiltrosListener(ActionListener l) { btnFilters.addActionListener(l); }
    
    /**
     * Adds the carrito listener.
     *
     * @param l the l
     */
    public void addCarritoListener(ActionListener l) { btnCarrito.addActionListener(l); }
    
    /**
     * Adds the intercambios listener.
     *
     * @param l the l
     */
    public void addIntercambiosListener(ActionListener l) { btnIntercambios.addActionListener(l); }
    
    /**
     * Adds the perfil listener.
     *
     * @param l the l
     */
    public void addPerfilListener(ActionListener l) { btnPerfil.addActionListener(l); }
    
    /**
     * Adds the notificaciones listener.
     *
     * @param l the l
     */
    public void addNotificacionesListener(ActionListener l) { btnNotificaciones.addActionListener(l); }
    
    /**
     * Adds the discounted listener.
     *
     * @param l the l
     */
    public void addDiscountedListener(ActionListener l) { btnDescuentos.addActionListener(l); }
    
    /**
     * Gets the search text.
     *
     * @return the search text
     */
    public String getSearchText() { 
        String text = txtSearch.getText().trim(); 
        return text.equals("Search...") ? "" : text;
    }
}
