package vista.empleadoPanel;

import vista.userPanels.HeaderPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class mainMenuEmpleadoPanel.
 */
public class mainMenuEmpleadoPanel extends JPanel {

    /** The header panel. */
    private HeaderPanel headerPanel;
    
    /** The btn manage interchanges. */
    private JButton btnManageInterchanges;
    
    /** The btn manage products. */
    private JButton btnManageProducts;
    
    /** The btn manage orders. */
    private JButton btnManageOrders;
    
    /** The btn validation requests. */
    private JButton btnValidationRequests;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);      
    
    /** The banner main color. */
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    /**
     * Instantiates a new main menu empleado panel.
     */
    public mainMenuEmpleadoPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();

        Color colorAzul = new Color(74, 144, 226);
        btnManageInterchanges = createStyledButton("MANAGE INTERCHANGES", colorAzul);
        btnManageProducts = createStyledButton("MANAGE PRODUCTS", colorAzul);
        btnManageOrders = createStyledButton("MANAGE ORDERS", colorAzul);
        btnValidationRequests = createStyledButton("VALIDATION REQUESTS", colorAzul);
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyContent = new JPanel(new GridBagLayout());
        bodyContent.setBackground(BG_COLOR);
        bodyContent.setBorder(new EmptyBorder(20, 100, 40, 100)); // Margen base de la pantalla
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_MAIN_COLOR);
        bannerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        JLabel lblTitle = new JLabel("EMPLOYEE MAIN MENU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 36)); // Título mucho más grande
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(5, 0, 5, 0)); // Recuadro aún más fino en altura
        bannerPanel.add(lblTitle, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.10; // Menor peso vertical para que el título ocupe menos altura
        gbc.insets = new Insets(0, 0, 30, 0); // Sin márgenes laterales para que tenga mayor longitud
        bodyContent.add(bannerPanel, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(btnManageInterchanges);
        buttonPanel.add(btnManageProducts);
        buttonPanel.add(btnManageOrders);
        buttonPanel.add(btnValidationRequests);

        gbc.gridy = 1;
        gbc.weighty = 0.90; // Mayor peso vertical para los botones
        gbc.insets = new Insets(0, 80, 0, 80); // Márgenes laterales para que sean más estrechos que el título
        bodyContent.add(buttonPanel, gbc);

        add(bodyContent, BorderLayout.CENTER);
    }

    /**
     * Creates the styled button.
     *
     * @param text the text
     * @param baseColor the base color
     * @return the j button
     */
    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 2, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });

        return btn;
    }

    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
    
    /**
     * Adds the manage products listener.
     *
     * @param l the l
     */
    public void addManageProductsListener(java.awt.event.ActionListener l) {
        btnManageProducts.addActionListener(l);
    }
    
    /**
     * Adds the manage orders listener.
     *
     * @param l the l
     */
    public void addManageOrdersListener(java.awt.event.ActionListener l) {
        btnManageOrders.addActionListener(l);
    }

    /**
     * Adds the validation requests listener.
     *
     * @param l the l
     */
    public void addValidationRequestsListener(java.awt.event.ActionListener l) {
        btnValidationRequests.addActionListener(l);
    }

    /**
     * Adds the manage interchanges listener.
     *
     * @param l the l
     */
    public void addManageInterchangesListener(java.awt.event.ActionListener l) {
        btnManageInterchanges.addActionListener(l);
    }
}
