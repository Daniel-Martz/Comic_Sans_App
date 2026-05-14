package vista.elements;

import javax.swing.*;
import java.awt.*;

// TODO: Auto-generated Javadoc
/**
 * The Class DiscountBadge.
 */
public class DiscountBadge extends JPanel {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The text. */
    private String text;
    
    /** The badge color. */
    private Color badgeColor;

    /**
     * Instantiates a new discount badge.
     *
     * @param text the text
     * @param badgeColor the badge color
     */
    public DiscountBadge(String text, Color badgeColor) {
        this.text = text;
        this.badgeColor = badgeColor;
        setOpaque(false);
        setPreferredSize(new Dimension(60, 60));
    }

    /**
     * Paint component.
     *
     * @param g the g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(badgeColor);
        g2.fillOval(5, 20, 50, 50); // Círculo centrado verticalmente
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int x = 5 + (50 - fm.stringWidth(text)) / 2;
        int y = 20 + ((50 - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, x, y);
    }
}