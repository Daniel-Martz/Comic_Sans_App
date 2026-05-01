package vista.empleadoPanel;

import vista.userPanels.HeaderPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ManageProductsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private HeaderPanel headerPanel;
    private JButton btnAddProducts;
    private JButton btnModifyProducts;
    private JButton btnManageCategories;
    private JButton btnManagePacks;
    private JButton btnDiscounts;

    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    public ManageProductsPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();

        Color colorAzul = new Color(74, 144, 226);
        btnAddProducts = createStyledButton("Add Products", colorAzul);
        btnModifyProducts = createStyledButton("Modify Products", colorAzul);
        btnManageCategories = createStyledButton("Manage Categories", colorAzul);
        btnManagePacks = createStyledButton("Manage Packs", colorAzul);
        btnDiscounts = createStyledButton("Discounts", colorAzul);
    }

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
        JLabel lblTitle = new JLabel("Manage Products", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(5, 0, 5, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.10;
        gbc.insets = new Insets(0, 0, 30, 0);
        bodyContent.add(bannerPanel, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 20));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(btnAddProducts);
        buttonPanel.add(btnModifyProducts);
        buttonPanel.add(btnManageCategories);
        buttonPanel.add(btnManagePacks);
        buttonPanel.add(btnDiscounts);

        gbc.gridy = 1;
        gbc.weighty = 0.90;
        gbc.insets = new Insets(0, 80, 0, 80);
        bodyContent.add(buttonPanel, gbc);

        add(bodyContent, BorderLayout.CENTER);
    }

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

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
    
    public void addModifyProductsListener(java.awt.event.ActionListener l) {
        btnModifyProducts.addActionListener(l);
    }
}
