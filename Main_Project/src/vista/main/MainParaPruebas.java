package vista.main;
import vista.userPanels.*;
import javax.swing.*;
import java.awt.*;

public class MainParaPruebas extends JFrame {

    // Card layout for switching view
    private CardLayout cardLayout;

    public MainParaPruebas() {
        super("Java Swing MVC");
        cardLayout = new CardLayout();
         JPanel panel = new LogInPanel();
        // sets our layout as a card layout
        setLayout(cardLayout);

        // initialize user controller
        // adds view to card layout with unique constraints
        this.add(panel, "Creación de Usuario");

        // icon for our application
        ImageIcon imageIcon = new ImageIcon("src/assets/appicon.png");
        setIconImage(imageIcon.getImage());
        // frame width & height
        int FRAME_WIDTH = 1200;
        int FRAME_HEIGHT = 700;
        // size of our application frame
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
