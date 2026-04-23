package vista.main;

import controladores.MainController;
import vista.userPanels.InterchangeCardPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación.
 *
 * Responsabilidades en MVC:
 * - Contiene y gestiona todos los paneles (vistas) mediante CardLayout.
 * - Expone métodos para que el MainController pueda navegar y acceder a los paneles.
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
        super("Comic Sans App");

        // 1. Crear el layout
        cardLayout         = new CardLayout();
        contenedorPaneles  = new JPanel(cardLayout);

        // 2. Crear las vistas
        interchangeCardPanel = new InterchangeCardPanel();
        // panel principal del usuario con toda la UI descrita
        vista.userPanels.MenuPrincipalPanel menuEmpleadoPanel = new vista.userPanels.MenuPrincipalPanel();

        // Paneles placeholder para zonas no implementadas aún
        vista.userPanels.PlaceholderPanel descuentosPanel = new vista.userPanels.PlaceholderPanel("Descuentos");
        vista.userPanels.PlaceholderPanel productosFiltradosPanel = new vista.userPanels.PlaceholderPanel("Productos - Filtros activos");
        vista.userPanels.PlaceholderPanel carritoPanel = new vista.userPanels.PlaceholderPanel("Carrito");
        vista.userPanels.PlaceholderPanel intercambiosPanel = new vista.userPanels.PlaceholderPanel("Intercambios");
        vista.userPanels.PlaceholderPanel configuracionPanel = new vista.userPanels.PlaceholderPanel("Configuración");
        vista.userPanels.PlaceholderPanel perfilPanel = new vista.userPanels.PlaceholderPanel("Perfil");
        vista.userPanels.PlaceholderPanel notificacionesPanel = new vista.userPanels.PlaceholderPanel("Notificaciones");

        // 3. Añadir vistas al contenedor con sus nombres
        contenedorPaneles.add(interchangeCardPanel, MainController.PANEL_DETALLE_INTERCAMBIO);
        contenedorPaneles.add(menuEmpleadoPanel, MainController.PANEL_MENU_PRINCIPAL);
        contenedorPaneles.add(descuentosPanel, MainController.PANEL_DESCUENTOS);
        contenedorPaneles.add(productosFiltradosPanel, MainController.PANEL_PRODUCTOS_FILTRADOS);
        contenedorPaneles.add(carritoPanel, MainController.PANEL_CARRITO);
        contenedorPaneles.add(intercambiosPanel, MainController.PANEL_MIS_INTERCAMBIOS);
        contenedorPaneles.add(configuracionPanel, MainController.PANEL_CONFIGURACION);
        contenedorPaneles.add(perfilPanel, MainController.PANEL_PERFIL);
        contenedorPaneles.add(notificacionesPanel, MainController.PANEL_NOTIFICACIONES);

        // 4. Crear el controlador principal y pasarle this
        mainController = new MainController(this);

        // Registrar listeners del panel principal para navegar entre vistas
        menuEmpleadoPanel.addHomeListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        menuEmpleadoPanel.addDescuentosListener(e -> mainController.navegarA(MainController.PANEL_DESCUENTOS));
        menuEmpleadoPanel.addOutstandingListener(e -> mainController.navegarA(MainController.PANEL_PRODUCTOS_FILTRADOS));
        menuEmpleadoPanel.addCarritoListener(e -> mainController.navegarA(MainController.PANEL_CARRITO));
        menuEmpleadoPanel.addIntercambiosListener(e -> mainController.navegarA(MainController.PANEL_MIS_INTERCAMBIOS));
        menuEmpleadoPanel.addConfiguracionListener(e -> mainController.navegarA(MainController.PANEL_CONFIGURACION));
        menuEmpleadoPanel.addPerfilListener(e -> mainController.navegarA(MainController.PANEL_PERFIL));
        menuEmpleadoPanel.addNotificacionesListener(e -> mainController.navegarA(MainController.PANEL_NOTIFICACIONES));

        // Búsqueda / filtros -> reusar panel productos filtrados
        menuEmpleadoPanel.addSearchListener(e -> mainController.navegarA(MainController.PANEL_PRODUCTOS_FILTRADOS));

        // Buy Now -> mostrar panel productos filtrados (en una app real abriría detalle compra)
        menuEmpleadoPanel.addBuyNowListener(e -> mainController.navegarA(MainController.PANEL_PRODUCTOS_FILTRADOS));

        // Categorías: usar mismo panel de productos filtrados
        menuEmpleadoPanel.addCategoryListener(e -> {
            // el action command contiene la categoría seleccionada
            String categoria = ((java.awt.event.ActionEvent)e).getActionCommand();
            // En una implementación real pasaríamos el filtro al controlador
            mainController.navegarA(MainController.PANEL_PRODUCTOS_FILTRADOS);
        });

        // 5. Configurar la ventana
        setContentPane(contenedorPaneles);
        
        // --- CAMBIO AQUÍ: Tamaño de respaldo y arranque maximizado ---
        setSize(1200, 700); // Este tamaño se usará si el usuario quita la pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH); // <--- ESTA ES LA LÍNEA QUE LO MAXIMIZA
        // -------------------------------------------------------------
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/assets/appicon.png");
        setIconImage(icon.getImage());

        // 6. Mostrar panel inicial (menu principal si existe; si no, detalle de intercambio)
        try {
            mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL);
        } catch (Exception ex) {
            mainController.navegarA(MainController.PANEL_DETALLE_INTERCAMBIO);
        }

        setVisible(true);
        // Registrar listeners "Volver" en los placeholders para regresar al menú
        descuentosPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        productosFiltradosPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        carritoPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        intercambiosPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        configuracionPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        perfilPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        notificacionesPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
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
