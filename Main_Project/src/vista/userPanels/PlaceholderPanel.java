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
    private HeaderPanel headerPanel;

    public PlaceholderPanel(String title) {
        setLayout(new BorderLayout());
        
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        contentWrapper.add(lblTitle, BorderLayout.CENTER);
        
        add(contentWrapper, BorderLayout.CENTER);
    }

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}
