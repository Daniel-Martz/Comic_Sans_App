package vista.main;

import controladores.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 *
 * Responsabilidades en MVC:
 * - Contiene y gestiona todos los paneles principales (Login, Menú, etc.) mediante CardLayout.
 * - Expone métodos para que el MainController pueda navegar entre paneles.
 * - Crea el MainController y se lo pasa a sí misma.
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
    // Paneles (vistas principales)
    // -------------------------------------------------------
    // Aquí es donde instanciarás tus verdaderos paneles a pantalla completa:
    // private LoginPanel loginPanel;
    // private MenuPrincipalPanel menuPrincipalPanel;

    // -------------------------------------------------------
    // Controlador principal
    // -------------------------------------------------------
    private MainController mainController;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public MainFrame() {
        super("Comic Sans App");

        // 1. Crear el layout
        cardLayout         = new CardLayout();
        contenedorPaneles  = new JPanel(cardLayout);

        // 2. Crear las vistas
        // loginPanel = new LoginPanel();
        // menuPrincipalPanel = new MenuPrincipalPanel();

        // 3. Añadir vistas al contenedor con sus nombres
        // contenedorPaneles.add(loginPanel, MainController.PANEL_LOGIN);
        // contenedorPaneles.add(menuPrincipalPanel, MainController.PANEL_MENU_PRINCIPAL);

        // 4. Crear el controlador principal y pasarle this
        mainController = new MainController(this);

        // 5. Configurar la ventana
        setContentPane(contenedorPaneles);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Esto centra la ventana en la pantalla

        ImageIcon icon = new ImageIcon("src/assets/appicon.png");
        setIconImage(icon.getImage());

        // 6. Mostrar panel inicial (Debería ser tu Login o Menú)
        // mainController.navegarA(MainController.PANEL_LOGIN);

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

    // Aquí irías añadiendo getters para tus paneles principales si el MainController necesita
    // pasarles datos al iniciar la aplicación:
    // public LoginPanel getLoginPanel() { return loginPanel; }
    // public MenuPrincipalPanel getMenuPrincipalPanel() { return menuPrincipalPanel; }
}