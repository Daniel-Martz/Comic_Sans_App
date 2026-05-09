package vista.empleadoWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SelectManagePackOptionWindow extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JButton btnAddPack;
    private JButton btnModifyPack;

    public SelectManagePackOptionWindow(JFrame parent) {
        super(parent, "Manage Packs", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("What do you want to do?", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);
        
        btnAddPack = new JButton("Add New Pack");
        btnAddPack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAddPack.setBackground(new Color(46, 204, 113));
        btnAddPack.setForeground(Color.WHITE);
        btnAddPack.setFocusPainted(false);
        btnAddPack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddPack.setPreferredSize(new Dimension(140, 40));
        
        btnModifyPack = new JButton("Modify Pack");
        btnModifyPack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnModifyPack.setBackground(new Color(74, 144, 226));
        btnModifyPack.setForeground(Color.WHITE);
        btnModifyPack.setFocusPainted(false);
        btnModifyPack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModifyPack.setPreferredSize(new Dimension(140, 40));
        
        btnPanel.add(btnModifyPack);
        btnPanel.add(btnAddPack);
        
        panel.add(btnPanel);
        setContentPane(panel);
    }
    
    public JButton getBtnAddPack() { return btnAddPack; }
    public JButton getBtnModifyPack() { return btnModifyPack; }
}