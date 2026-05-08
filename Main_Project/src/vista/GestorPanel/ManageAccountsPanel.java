package vista.GestorPanel;

import vista.userPanels.HeaderPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ManageAccountsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private HeaderPanel headerPanel;
    private JTextField searchField;
    private JButton btnSearch;
    private JPanel listContainer;
    private JButton btnAddUser;
    
    private ActionListener manageUserListener;

    private final Color BG_COLOR = new Color(162, 187, 210);
    private final Color BANNER_COLOR = new Color(20, 60, 100); 
    private final Color CENTER_BG = new Color(54, 119, 189); 

    public ManageAccountsPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuGestor(); 
        // El bocadillo será "MANAGER DASHBOARD", pero es lo estándar del Gestor.

        searchField = new JTextField(20);
        searchField.setText("Search"); 
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1, true), new EmptyBorder(8, 8, 8, 8)));

        btnSearch = new JButton();
        btnSearch.setContentAreaFilled(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            File imgFile = new File("src/assets/lupa.png");
            if (imgFile.exists()) {
                Image img = javax.imageio.ImageIO.read(imgFile);
                Image scaledImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btnSearch.setIcon(new ImageIcon(scaledImg));
            } else {
                btnSearch.setText("🔍");
            }
        } catch (Exception e) {
            btnSearch.setText("🔍");
        }

        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(CENTER_BG);

        btnAddUser = new JButton("+") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                super.paintComponent(g);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        btnAddUser.setFont(new Font("SansSerif", Font.BOLD, 30));
        btnAddUser.setForeground(Color.BLACK);
        btnAddUser.setBackground(Color.GREEN);
        btnAddUser.setContentAreaFilled(false);
        btnAddUser.setFocusPainted(false);
        btnAddUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddUser.setPreferredSize(new Dimension(50, 50));
        btnAddUser.setMinimumSize(new Dimension(50, 50));
        btnAddUser.setMaximumSize(new Dimension(50, 50));
        btnAddUser.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyContent = new JPanel(new BorderLayout());
        bodyContent.setOpaque(false);
        
        // Banner
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_COLOR);
        JLabel lblTitle = new JLabel("MANAGE ACCOUNTS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);
        
        bodyContent.add(bannerPanel, BorderLayout.NORTH);

        // Center Container
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(30, 100, 30, 100));

        JPanel centerBlock = new JPanel(new BorderLayout(0, 15));
        centerBlock.setBackground(CENTER_BG);
        centerBlock.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Search Bar Top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        centerBlock.add(searchPanel, BorderLayout.NORTH);

        // List Scroll
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(CENTER_BG);
        scrollPane.getViewport().setBackground(CENTER_BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        centerBlock.add(scrollPane, BorderLayout.CENTER);

        // Bottom Add User
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(btnAddUser);
        centerBlock.add(bottomPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerWrapper.add(centerBlock, gbc);

        bodyContent.add(centerWrapper, BorderLayout.CENTER);

        add(bodyContent, BorderLayout.CENTER);
    }

    public void clearAccounts() {
        listContainer.removeAll();
        listContainer.revalidate();
        listContainer.repaint();
    }

    public void addAccountRow(String name, String dni) {
        JPanel row = createUserRow(name, dni);
        listContainer.add(row);
        listContainer.add(Box.createVerticalStrut(10));
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel createUserRow(String name, String dni) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(new Color(173, 216, 230)); // Fondo azul claro que destaca
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
        row.setMaximumSize(new Dimension(800, 60)); 

        // Izquierda: Icono de perfil
        JLabel lblIcon = new JLabel();
        try {
            File imgFile = new File("src/assets/fotoperfil.png");
            if (imgFile.exists()) {
                Image img = javax.imageio.ImageIO.read(imgFile);
                Image scaledImg = img.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                lblIcon.setIcon(new ImageIcon(scaledImg));
            } else {
                lblIcon.setText("👤");
            }
        } catch (Exception e) {
            lblIcon.setText("👤");
        }
        row.add(lblIcon, BorderLayout.WEST);

        // Centro: Nombre y DNI
        JLabel lblInfo = new JLabel("Name: " + name + "      DNI: " + dni);
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 16));
        row.add(lblInfo, BorderLayout.CENTER);

        // Derecha: Botón MANAGE
        JButton btnManage = new JButton("MANAGE");
        btnManage.setBackground(new Color(250, 128, 114)); // Color salmón/rojo claro
        btnManage.setForeground(Color.WHITE);
        btnManage.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnManage.setFocusPainted(false);
        btnManage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnManage.setActionCommand(dni); 
        
        if (manageUserListener != null) {
            btnManage.addActionListener(manageUserListener);
        }

        row.add(btnManage, BorderLayout.EAST);

        return row;
    }

    // --- Métodos de suscripción para el Controlador ---
    public void addHomeListener(ActionListener l) {
        headerPanel.addHomeListener(l);
    }

    public void addSearchListener(ActionListener l) {
        btnSearch.addActionListener(l);
        searchField.addActionListener(l);
    }

    public void addManageUserListener(ActionListener l) {
        this.manageUserListener = l;
    }

    public void addCreateUserListener(ActionListener l) {
        btnAddUser.addActionListener(l);
    }

    /** Permite al Controlador mostrar/ocultar el botón de crear usuario.
     *  Útil para ocultarlo cuando el usuario actual no tiene permisos (por ejemplo: no es Gestor).
     */
    public void setCreateButtonVisible(boolean visible) {
        btnAddUser.setVisible(visible);
    }
    
    public String getSearchText() {
        String text = searchField.getText().trim();
        return text.equals("Search") ? "" : text;
    }
    
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}