package vista.userPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * Panel placeholder simple para zonas no implementadas aún.
 * Muestra un título y un botón "Volver" que puede recibir un listener.
 */
public class PlaceholderPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The lbl title. */
    private JLabel lblTitle;
    
    /** The header panel. */
    private HeaderPanel headerPanel;

    /**
     * Instantiates a new placeholder panel.
     *
     * @param title the title
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
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}
