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

public class HeaderPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel logoLabel;
    private JButton btnHome;
    private JButton btnDescuentos;
    private JButton btnOutstanding;
    private JTextField txtSearch;
    private JButton btnFilters;
    private JButton btnCarrito;
    private JButton btnIntercambios;
    private JButton btnPerfil;
    private JButton btnNotificaciones;
    private JButton btnSearchIcon;
    private JLabel speechBubble;
    private JLabel lblDate;
    private JPanel searchBox;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel centerNavPanel;

    ImageIcon iconoSinLogIn = getSclaedIcon("src/assets/fotoperfil.png");
    ImageIcon iconoConLogIn = getSclaedIcon("src/assets/fotoperfilLoggedIn.png");

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
    
    public void refreshIconImage(boolean isLoggedIn) {
    	if(isLoggedIn) {
			btnPerfil.setIcon(this.iconoConLogIn);
    	}else {
    		btnPerfil.setIcon(this.iconoSinLogIn);
    	}
    }

    public void updateDate() {
        if (lblDate != null) {
            DateTimeSimulado ahora = new DateTimeSimulado();
            String timeStr = String.format("%02d:%02d", ahora.getHora(), ahora.getMinuto());
            lblDate.setText("Simulated Time: " + ahora.toStringFecha() + "   " + timeStr);
        }
    }

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

        btnFilters = new JButton("⚙ Filtros");
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
     */
    public JButton addSecondaryTopButton(String text) {
        JButton btn = createTopNavButton(text);
        // Insertamos justo después del HOME (posición 1)
        centerNavPanel.add(btn, 1);
        return btn;
    }

    /**
     * Configura la cabecera para la vista de empleados ocultando elementos de compra.
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

    public void configurarMenuGestor() {
        configurarMenuEmpleado();
        speechBubble.setText("<html><center>MANAGER DASHBOARD<br>ADMINISTRATE THE STORE</center></html>");
        btnNotificaciones.setVisible(false);
    }

    /**
     * Configura la cabecera para la vista de Notificaciones: sólo muestra el botón HOME.
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

    private JButton createTopNavButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); super.paintComponent(g); g2.dispose(); }
            @Override protected void paintBorder(Graphics g) { Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g2.setColor(Color.BLACK); g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); g2.dispose(); }
        };
        btn.setContentAreaFilled(false); btn.setBackground(Color.WHITE); btn.setFocusPainted(false); btn.setFont(new Font("Comic Sans MS", Font.BOLD, 14)); btn.setBorder(new EmptyBorder(10, 25, 10, 25)); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); return btn;
    }

    private JButton createImageIconButton(String imagePath, int width, int height, String fallbackText) {
        JButton btn = new JButton(); btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); btn.setMargin(new Insets(0, 0, 0, 0)); btn.setPreferredSize(new Dimension(width + 5, height + 5)); 
        try { File imgFile = new File(imagePath); if (!imgFile.exists()) { setFallback(btn, fallbackText); return btn; } Image img = javax.imageio.ImageIO.read(imgFile); if (img == null) { setFallback(btn, fallbackText); return btn; } Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); btn.setIcon(new ImageIcon(scaledImg)); } catch (Exception e) { setFallback(btn, fallbackText); } return btn;
    }

    private void setFallback(JButton btn, String text) {
        btn.setText(text); btn.setFont(new Font("Comic Sans MS", Font.BOLD, 10)); btn.setForeground(Color.RED);
    }

    // --- Delegación de Listeners ---
    public void addHomeListener(ActionListener l) { btnHome.addActionListener(l); }
    public void addOutstandingListener(ActionListener l) { btnOutstanding.addActionListener(l); }
    public void addSearchListener(ActionListener l) { 
        txtSearch.addActionListener(l); 
        btnSearchIcon.addActionListener(e -> {
            l.actionPerformed(new java.awt.event.ActionEvent(txtSearch, java.awt.event.ActionEvent.ACTION_PERFORMED, getSearchText()));
        });
    }
    public void addFiltrosListener(ActionListener l) { btnFilters.addActionListener(l); }
    public void addCarritoListener(ActionListener l) { btnCarrito.addActionListener(l); }
    public void addIntercambiosListener(ActionListener l) { btnIntercambios.addActionListener(l); }
    public void addPerfilListener(ActionListener l) { btnPerfil.addActionListener(l); }
    public void addNotificacionesListener(ActionListener l) { btnNotificaciones.addActionListener(l); }
    public String getSearchText() { 
        String text = txtSearch.getText().trim(); 
        return text.equals("Search...") ? "" : text;
    }
}
