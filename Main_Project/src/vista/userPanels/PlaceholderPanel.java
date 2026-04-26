package vista.userPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel placeholder simple para zonas no implementadas aún.
 * Muestra un título y un botón "Volver" que puede recibir un listener.
 */
public class PlaceholderPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel lblTitle;
    private JButton btnVolver;

    public PlaceholderPanel(String title) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        topBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnVolver.setBackground(new Color(74, 118, 201));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setOpaque(true);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(130, 32));
        
        topBar.add(btnVolver);
        add(topBar, BorderLayout.NORTH);

        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(lblTitle, BorderLayout.CENTER);
    }

    public void addVolverListener(ActionListener l) {
        btnVolver.addActionListener(l);
    }
}
