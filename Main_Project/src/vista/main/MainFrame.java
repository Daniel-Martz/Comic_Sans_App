package vista.main;

import controladores.MainController;
import vista.userPanels.InterchangeCardPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 *
 * Responsabilidades en MVC:
 *  - Contiene y gestiona todos los paneles (vistas) mediante CardLayout.
 *  - Expone métodos para que el MainController pueda navegar y acceder a los paneles.
 *  - Crea el MainController y se lo pasa a sí misma.
 *
 * NO contiene lógica de negocio.
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------
    // Layout de navegación
    // -------------------------------------------------------
    private CardLayout cardLayout;
    private JPanel contenedorPaneles;

    // -------------------------------------------------------
    // Paneles (vistas)
    // -------------------------------------------------------
    private InterchangeCardPanel interchangeCardPanel;
    // Aquí irías añadiendo el resto de paneles:
    // private LoginPanel loginPanel;
    // private MenuPrincipalPanel menuPrincipalPanel;
    // private ListaOfertasPanel listaOfertasPanel;

    // -------------------------------------------------------
    // Controlador principal
    // -------------------------------------------------------
    private MainController mainController;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public MainFrame() {
<<<<<<< Updated upstream
        super("Java Swing MVC");
        cardLayout = new CardLayout();
         JPanel crearUsuarioPanel = new mainMenuEmpleadoPanel();
        // sets our layout as a card layout
        setLayout(cardLayout);
=======
        super("Comic Sans App");
>>>>>>> Stashed changes

        // 1. Crear el layout
        cardLayout         = new CardLayout();
        contenedorPaneles  = new JPanel(cardLayout);

<<<<<<< Updated upstream
        // icon for our application
        ImageIcon imageIcon = new ImageIcon("src/assets/appicon.png");
        setIconImage(imageIcon.getImage());
        // frame width & heightW
        int FRAME_WIDTH = 1200;
        int FRAME_HEIGHT = 700;
        // size of our application frame
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
=======
        // 2. Crear las vistas
        interchangeCardPanel = new InterchangeCardPanel();
        // loginPanel = new LoginPanel();
        // menuPrincipalPanel = new MenuPrincipalPanel();

        // 3. Añadir vistas al contenedor con sus nombres
        contenedorPaneles.add(interchangeCardPanel, MainController.PANEL_DETALLE_INTERCAMBIO);
        // contenedorPaneles.add(loginPanel, MainController.PANEL_LOGIN);
        // contenedorPaneles.add(menuPrincipalPanel, MainController.PANEL_MENU_PRINCIPAL);

        // 4. Crear el controlador principal y pasarle this
        mainController = new MainController(this);

        // 5. Configurar la ventana
        setContentPane(contenedorPaneles);
        setSize(1200, 700);
>>>>>>> Stashed changes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/assets/appicon.png");
        setIconImage(icon.getImage());

        // 6. Mostrar panel inicial
        mainController.navegarA(MainController.PANEL_DETALLE_INTERCAMBIO);

        setVisible(true);
    }

    // -------------------------------------------------------
    // Métodos públicos para el MainController
    // -------------------------------------------------------

    /**
     * Cambia el panel visible. Lo llama el MainController.
     */
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(contenedorPaneles, nombrePanel);
    }

    /**
     * Devuelve el panel de detalle de intercambio.
     * El MainController lo usa para pasárselo al ControladorIntercambio.
     */
    public InterchangeCardPanel getInterchangeCardPanel() {
        return interchangeCardPanel;
    }

    // Aquí irías añadiendo getters para el resto de paneles:
    // public LoginPanel getLoginPanel() { return loginPanel; }
    // public MenuPrincipalPanel getMenuPrincipalPanel() { return menuPrincipalPanel; }
}
