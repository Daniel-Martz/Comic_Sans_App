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
 * The Class AddProductsPanel.
 */
public class AddProductsPanel extends JPanel {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The header panel. */
    private HeaderPanel headerPanel;
    
    /** The btn back to manage. */
    private JButton btnBackToManage;
    
    /** The btn add manually. */
    private JButton btnAddManually;
    
    /** The btn load from file. */
    private JButton btnLoadFromFile;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);      
    
    /** The banner main color. */
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    /**
     * Instantiates a new adds the products panel.
     */
    public AddProductsPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();

        // Añadimos un botón BACK junto al HOME en la cabecera para volver a ManageProducts
        btnBackToManage = headerPanel.addSecondaryTopButton("BACK");

        Color colorAzul = new Color(74, 144, 226);
        btnAddManually = createStyledButton("Add Manually", colorAzul);
        btnLoadFromFile = createStyledButton("Load From a File", colorAzul);
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
        bodyContent.setBorder(new EmptyBorder(20, 100, 40, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_MAIN_COLOR);
        bannerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        JLabel lblTitle = new JLabel("Add products", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.10;
        gbc.insets = new Insets(0, 0, 50, 0);
        bodyContent.add(bannerPanel, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 40));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(btnAddManually);
        buttonPanel.add(btnLoadFromFile);

        gbc.gridy = 1;
        gbc.weighty = 0.90;
        gbc.insets = new Insets(0, 80, 0, 80);
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
                new EmptyBorder(20, 20, 20, 20)
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
     * Gets the btn add manually.
     *
     * @return the btn add manually
     */
    public JButton getBtnAddManually() {
        return btnAddManually;
    }
    
    /**
     * Gets the btn load from file.
     *
     * @return the btn load from file
     */
    public JButton getBtnLoadFromFile() {
        return btnLoadFromFile;
    }

    /**
     * Gets the btn back to manage.
     *
     * @return the btn back to manage
     */
    public JButton getBtnBackToManage() { return btnBackToManage; }
}