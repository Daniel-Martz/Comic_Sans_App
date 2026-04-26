package vista.main;

import controladores.*;
import vista.userPanels.*;

import javax.swing.*;
import java.awt.*;
import vista.userPanels.*;

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
    // Referencia al panel de detalle de intercambio
    private InterchangeCardPanel interchangeCardPanel;

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
    
    private ControladorCarrito controladorCarrito;
    private vista.userPanels.ProductosFiltradosPanel productosFiltradosPanel;

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
        MySecondHandProductsPanel mySecondHandProductsPanel = new MySecondHandProductsPanel();

        CarritoPanel carritoPanel = new CarritoPanel();
        controladorCarrito = new ControladorCarrito(carritoPanel, this);
        
        // Paneles placeholder para zonas no implementadas aún
        PlaceholderPanel descuentosPanel = new vista.userPanels.PlaceholderPanel("Descuentos");
        this.productosFiltradosPanel = new vista.userPanels.ProductosFiltradosPanel();
        PlaceholderPanel configuracionPanel = new vista.userPanels.PlaceholderPanel("Configuración");
        PlaceholderPanel perfilPanel = new vista.userPanels.PlaceholderPanel("Perfil");
        PlaceholderPanel notificacionesPanel = new vista.userPanels.PlaceholderPanel("Notificaciones");

        // 3. Añadir vistas al contenedor con sus nombres
        contenedorPaneles.add(interchangeCardPanel, MainController.PANEL_DETALLE_INTERCAMBIO);
        contenedorPaneles.add(menuEmpleadoPanel, MainController.PANEL_MENU_PRINCIPAL);
        contenedorPaneles.add(mySecondHandProductsPanel, MainController.PANEL_MY_SECOND_HAND_PRODUCTS);
        contenedorPaneles.add(descuentosPanel, MainController.PANEL_DESCUENTOS);
        contenedorPaneles.add(productosFiltradosPanel, MainController.PANEL_PRODUCTOS_FILTRADOS);
        contenedorPaneles.add(carritoPanel, MainController.PANEL_CARRITO);
        contenedorPaneles.add(configuracionPanel, MainController.PANEL_CONFIGURACION);
        contenedorPaneles.add(perfilPanel, MainController.PANEL_PERFIL);
        contenedorPaneles.add(notificacionesPanel, MainController.PANEL_NOTIFICACIONES);
        
        // 4. Crear el controlador principal y pasarle this
        mainController = new MainController(this);

        // Crear el controlador específico para el panel de productos de segunda mano
        new ControladorMySecondHandProducts(mySecondHandProductsPanel, this);

        // Registrar listeners del panel principal para navegar entre vistas
        menuEmpleadoPanel.addHomeListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        menuEmpleadoPanel.addDescuentosListener(e -> mainController.navegarA(MainController.PANEL_DESCUENTOS));
        menuEmpleadoPanel.addOutstandingListener(e -> mainController.navegarA(MainController.PANEL_PRODUCTOS_FILTRADOS));
        
        menuEmpleadoPanel.addCarritoListener(e -> {
            controladorCarrito.refrescarVista(); 
            mainController.navegarA(MainController.PANEL_CARRITO);
        });

        menuEmpleadoPanel.addIntercambiosListener(e -> mainController.mostrarVentanaOpcionesIntercambio());
        menuEmpleadoPanel.addConfiguracionListener(e -> mainController.navegarA(MainController.PANEL_CONFIGURACION));
        menuEmpleadoPanel.addPerfilListener(e -> mainController.navegarA(MainController.PANEL_PERFIL));
        menuEmpleadoPanel.addNotificacionesListener(e -> mainController.navegarA(MainController.PANEL_NOTIFICACIONES));

        // Búsqueda en texto -> busca y muestra el panel de productos filtrados
        menuEmpleadoPanel.addSearchListener(e -> {
            String prompt = ((java.awt.event.ActionEvent)e).getActionCommand();
            mainController.mostrarProductosFiltrados(prompt);
        });

        // Botón de engranaje (Filtros) -> ABRE LA VENTANA EMERGENTE
        menuEmpleadoPanel.addFiltrosListener(e -> mainController.abrirVentanaFiltros());
        // Buy Now -> mostrar panel productos filtrados (en una app real abriría detalle compra)
        menuEmpleadoPanel.addBuyNowListener(e -> mainController.navegarA(MainController.PANEL_PRODUCTOS_FILTRADOS));

        // Categorías: usar mismo panel de productos filtrados
        menuEmpleadoPanel.addCategoryListener(e -> {
            // el action command contiene la categoría seleccionada
            String categoria = ((java.awt.event.ActionEvent)e).getActionCommand();
            mainController.mostrarProductosPorCategoria(categoria);
        });

        // Listener para el botón "Volver" del panel de productos de segunda mano
        mySecondHandProductsPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));

        // 5. Configurar la ventana
        setContentPane(contenedorPaneles);
        
        // --- CAMBIO AQUÍ: Tamaño de respaldo y arranque maximizado ---
        setSize(1200, 700); // Este tamaño se usará si el usuario quita la pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH); // <--- ESTA ES LA LÍNEA QUE LO MAXIMIZA
        // -------------------------------------------------------------
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Esto centra la ventana en la pantalla

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
        //VAMOS A TENER QUE IR BORRANDO A MEDIDA QUE IMPLEMENTEMOS
        descuentosPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        productosFiltradosPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        configuracionPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        perfilPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
        notificacionesPanel.addVolverListener(e -> mainController.navegarA(MainController.PANEL_MENU_PRINCIPAL));
    }

    /**
     * Cambia el panel visible. Lo llama el MainController.
     */
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(contenedorPaneles, nombrePanel);
    }

    public ControladorCarrito getControladorCarrito() {
        return controladorCarrito;
    }

    public vista.userPanels.ProductosFiltradosPanel getProductosFiltradosPanel() {
        return this.productosFiltradosPanel;
    }
    
    // Aquí irías añadiendo getters para tus paneles principales si el MainController necesita
    // pasarles datos al iniciar la aplicación:
    // public LoginPanel getLoginPanel() { return loginPanel; }
    // public MenuPrincipalPanel getMenuPrincipalPanel() { return menuPrincipalPanel; }
}
