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
        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(lblTitle, BorderLayout.CENTER);

        btnVolver = new JButton("Volver");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(btnVolver);
        add(bottom, BorderLayout.SOUTH);
    }

    public void addVolverListener(ActionListener l) {
        btnVolver.addActionListener(l);
    }
}
