package vista.userPanels;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class mainMenuEmpleadoPanel extends JPanel{

    public mainMenuEmpleadoPanel() {
        setBackground(new Color(153, 180, 209));
        setBorder(new EmptyBorder(20, 50, 30, 50));
        setLayout(new BorderLayout(0, 25));

        JLabel lblTitle = new JLabel("MAIN MENU");
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(74, 118, 201));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(new JButton("Manage Interchanges"));
        buttonPanel.add(new JButton("Manage Products"));
        buttonPanel.add(new JButton("Manage Orders"));
        buttonPanel.add(new JButton("Validation Requests"));
        
        add(buttonPanel, BorderLayout.CENTER);
    }
}
