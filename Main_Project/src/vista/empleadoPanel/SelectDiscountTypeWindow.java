package vista.empleadoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SelectDiscountTypeWindow extends JDialog {

    private JButton btnProducts;
    private JButton btnCategories;

    public SelectDiscountTypeWindow(JFrame parent) {
        super(parent, "Select Discount Target", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("What do you want to manage?", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);
        
        btnProducts = new JButton("Products");
        btnProducts.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnProducts.setBackground(new Color(74, 144, 226));
        btnProducts.setForeground(Color.WHITE);
        btnProducts.setFocusPainted(false);
        btnProducts.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProducts.setPreferredSize(new Dimension(130, 40));
        
        btnCategories = new JButton("Categories");
        btnCategories.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCategories.setBackground(new Color(102, 0, 204));
        btnCategories.setForeground(Color.WHITE);
        btnCategories.setFocusPainted(false);
        btnCategories.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCategories.setPreferredSize(new Dimension(130, 40));
        
        btnPanel.add(btnProducts);
        btnPanel.add(btnCategories);
        
        panel.add(btnPanel);
        setContentPane(panel);
    }
    
    public JButton getBtnProducts() { return btnProducts; }
    public JButton getBtnCategories() { return btnCategories; }
}