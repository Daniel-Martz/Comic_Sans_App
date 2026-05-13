package vista.userPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Un panel provisional para rellenar huecos mientras no los programamos.
 */
public class PlaceholderPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel lblTitle;
    private HeaderPanel headerPanel;

    /**
     * Constructor que pone un título en el centro.
     * @param title título de la zona
     */
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

    /**
     * @return el header
     */
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}
