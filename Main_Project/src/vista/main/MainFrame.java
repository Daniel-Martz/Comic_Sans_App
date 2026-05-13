package vista.main;

import vista.userPanels.*;
import vista.GestorPanel.*;
import vista.empleadoPanel.*;
import javax.swing.*;

import modelo.aplicacion.Aplicacion;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class MainFrame.
 */
public class MainFrame extends JFrame {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The card layout. */
    // Layout
    private CardLayout cardLayout;
    
    /** The contenedor paneles. */
    private JPanel contenedorPaneles;

    /** The interchange card panel. */
    // Paneles (Vistas)
    private InterchangeCardPanel interchangeCardPanel;
    
    /** The menu empleado panel. */
    private mainMenuEmpleadoPanel menuEmpleadoPanel;
    
    /** The menu gestor panel. */
    private MainMenuGestorPanel menuGestorPanel;
    
    /** The menu principal panel. */
    private MenuPrincipalPanel menuPrincipalPanel;
    
    /** The my second hand products panel. */
    private MySecondHandProductsPanel mySecondHandProductsPanel;
    
    /** The carrito panel. */
    private CarritoPanel carritoPanel;
    
    /** The productos filtrados panel. */
    private ProductosFiltradosPanel productosFiltradosPanel;
    
    /** The outstanding panel. */
    private OutstandingPanel outstandingPanel;
    
    /** The search interchanges panel. */
    private SearchInterchangesPanel searchInterchangesPanel;
    
    /** The make offer panel. */
    private MakeOfferPanel makeOfferPanel;
    
    /** The historial pedidos panel. */
    private HistorialPedidosPanel historialPedidosPanel;
    
    /** The notificaciones panel. */
    private NotificacionesPanel notificacionesPanel;
    
    /** The discounted panel. */
    private DiscountedPanel discountedPanel;
    
    /** The manage products panel. */
    private ManageProductsPanel manageProductsPanel;
    
    /** The modify products panel. */
    private ModifyProductsPanel modifyProductsPanel;
    
    /** The manage orders panel. */
    private ManageOrdersPanel manageOrdersPanel;
    
    /** The validation requests panel. */
    private ValidationRequestsPanel validationRequestsPanel;
    
    /** The manage interchanges panel. */
    private ManageInterchangesPanel manageInterchangesPanel;
    
    /** The add products panel. */
    private AddProductsPanel addProductsPanel;
    
    /** The create pack panel. */
    private CreatePackPanel createPackPanel;
    
    /** The modify packs panel. */
    private ModifyPacksPanel modifyPacksPanel;
    // El formulario de añadir producto manualmente ahora es una ventana independiente (AddProductManuallyWindow)
    // por tanto no mantenemos aquí el panel como componente del CardLayout.
    
    /** The manage accounts panel. */
    private ManageAccountsPanel manageAccountsPanel;
    
    /** The manage statistics panel. */
    private ManageStatisticsPanel manageStatisticsPanel;
    
    /** The manage recommendations panel. */
    private ManageRecommendationsPanel manageRecommendationsPanel;
    
    /** The manage time panel. */
    private ManageTimePanel manageTimePanel;
    
    /** The descuentos panel. */
    private DescuentosPanel descuentosPanel;
    
    /** The descuentos categoria panel. */
    private DescuentosCategoriaPanel descuentosCategoriaPanel;
    
    /** The configuracion panel. */
    private PlaceholderPanel configuracionPanel;
    
    /** The perfil panel. */
    private PlaceholderPanel perfilPanel;

    /** The fichero guardado. */
    private final String ficheroGuardado = "AppGuardada.txt";

    /** The Constant PANEL_MENU_EMPLEADO. */
    // Nombres estáticos de los paneles para el CardLayout (antes estaban en el controlador)
    public static final String PANEL_MENU_EMPLEADO = "MenuEmpleado";
    
    /** The Constant PANEL_MENU_GESTOR. */
    public static final String PANEL_MENU_GESTOR = "MenuGestor";
    
    /** The Constant PANEL_MENU_PRINCIPAL. */
    public static final String PANEL_MENU_PRINCIPAL = "MenuPrincipal";
    
    /** The Constant PANEL_DETALLE_INTERCAMBIO. */
    public static final String PANEL_DETALLE_INTERCAMBIO = "DetalleIntercambio";
    
    /** The Constant PANEL_MY_SECOND_HAND_PRODUCTS. */
    public static final String PANEL_MY_SECOND_HAND_PRODUCTS = "MySecondHandProducts";
    
    /** The Constant PANEL_DESCUENTOS. */
    public static final String PANEL_DESCUENTOS = "Descuentos";
    
    /** The Constant PANEL_PRODUCTOS_FILTRADOS. */
    public static final String PANEL_PRODUCTOS_FILTRADOS = "ProductosFiltrados";
    
    /** The Constant PANEL_CARRITO. */
    public static final String PANEL_CARRITO = "Carrito";
    
    /** The Constant PANEL_CONFIGURACION. */
    public static final String PANEL_CONFIGURACION = "Configuracion";
    
    /** The Constant PANEL_PERFIL. */
    public static final String PANEL_PERFIL = "Perfil";
    
    /** The Constant PANEL_NOTIFICACIONES. */
    public static final String PANEL_NOTIFICACIONES = "Notificaciones";
    
    /** The Constant PANEL_OUTSTANDING. */
    public static final String PANEL_OUTSTANDING = "Outstanding";
    
    /** The Constant PANEL_DISCOUNTED. */
    public static final String PANEL_DISCOUNTED = "Discounted";
    
    /** The Constant PANEL_SEARCH_INTERCHANGES. */
    public static final String PANEL_SEARCH_INTERCHANGES = "SearchInterchanges";
    
    /** The Constant PANEL_MAKE_OFFER. */
    public static final String PANEL_MAKE_OFFER = "MakeOffer";
    
    /** The Constant PANEL_HISTORIAL_PEDIDOS. */
    public static final String PANEL_HISTORIAL_PEDIDOS = "HistorialPedidos";
    
    /** The Constant PANEL_MANAGE_PRODUCTS. */
    public static final String PANEL_MANAGE_PRODUCTS = "ManageProducts";
    
    /** The Constant PANEL_MODIFY_PRODUCTS. */
    public static final String PANEL_MODIFY_PRODUCTS = "ModifyProducts";
    
    /** The Constant PANEL_MANAGE_ORDERS. */
    public static final String PANEL_MANAGE_ORDERS = "ManageOrders";
    
    /** The Constant PANEL_MODIFY_A_PRODUCT. */
    public static final String PANEL_MODIFY_A_PRODUCT = "ModifyAProduct";
    
    /** The Constant PANEL_ADD_PRODUCTS. */
    public static final String PANEL_ADD_PRODUCTS = "AddProducts";
    
    /** The Constant PANEL_CREATE_PACK. */
    public static final String PANEL_CREATE_PACK = "CreatePack";
    
    /** The Constant PANEL_MODIFY_PACKS. */
    public static final String PANEL_MODIFY_PACKS = "ModifyPacks";
    
    /** The Constant PANEL_LOAD_FROM_FILE. */
    public static final String PANEL_LOAD_FROM_FILE = "LoadFromFile";
    
    /** The Constant PANEL_ADD_PRODUCT_MANUALLY. */
    public static final String PANEL_ADD_PRODUCT_MANUALLY = "AddProductManually";
    
    /** The Constant PANEL_VALIDATION_REQUESTS. */
    public static final String PANEL_VALIDATION_REQUESTS = "ValidationRequestsPanel";
    
    /** The Constant PANEL_MANAGE_INTERCHANGES. */
    public static final String PANEL_MANAGE_INTERCHANGES = "ManageInterchanges";
    
    /** The Constant PANEL_MANAGE_ACCOUNTS. */
    public static final String PANEL_MANAGE_ACCOUNTS = "ManageAccounts";
    
    /** The Constant PANEL_MANAGE_STATISTICS. */
    public static final String PANEL_MANAGE_STATISTICS = "ManageStatistics";
    
    /** The Constant PANEL_MANAGE_RECOMMENDATIONS. */
    public static final String PANEL_MANAGE_RECOMMENDATIONS = "ManageRecommendations";
    
    /** The Constant PANEL_MANAGE_TIME. */
    public static final String PANEL_MANAGE_TIME = "ManageTime";
    
    /** The Constant PANEL_DESCUENTOS_CATEGORIA. */
    public static final String PANEL_DESCUENTOS_CATEGORIA = "DescuentosCategoria";

    /**
     * Guardar estado aplicacion.
     */
    public void guardarEstadoAplicacion() {
    	try {
        Aplicacion.getInstancia().guardarEstadoAplicacion(ficheroGuardado);
      } catch (IOException e) {
        System.out.println("No hay un estado previo que cargar");
      }
    }
    
    /**
     * Cargar estado aplicacion.
     */
    public void cargarEstadoAplicacion() {
      Aplicacion.getInstancia().cargarEstadoAplicacion(ficheroGuardado);
    }

    /**
     * Instantiates a new main frame.
     */
    public MainFrame() {
      super("Comic Sans App");
      cargarEstadoAplicacion();
      
      // 0. Hacemos que se guarde el estado de la aplicación al cerrarla
      this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
        //Cuando se cierre la ventana, queremos que se guarde el estado de la aplicación
        guardarEstadoAplicacion();
				
				// Cerrar la ventana y terminar la ejecución
				e.getWindow().dispose();
				System.exit(0);
			}
		});
        // 1. Crear el layout
        cardLayout = new CardLayout();
        contenedorPaneles = new JPanel(cardLayout);

        // 2. Instanciar SÓLO los paneles (cero Controladores)
        menuPrincipalPanel = new MenuPrincipalPanel();
        interchangeCardPanel = new InterchangeCardPanel();
        menuEmpleadoPanel = new mainMenuEmpleadoPanel();
        menuGestorPanel = new MainMenuGestorPanel();
        mySecondHandProductsPanel = new MySecondHandProductsPanel();
        carritoPanel = new CarritoPanel();
        productosFiltradosPanel = new ProductosFiltradosPanel();
        outstandingPanel = new OutstandingPanel();
        discountedPanel = new DiscountedPanel();
        searchInterchangesPanel = new SearchInterchangesPanel();
        makeOfferPanel = new MakeOfferPanel();
        historialPedidosPanel = new HistorialPedidosPanel();
        
        manageProductsPanel = new ManageProductsPanel();
        modifyProductsPanel = new ModifyProductsPanel();
        manageOrdersPanel = new ManageOrdersPanel();
        
        validationRequestsPanel = new ValidationRequestsPanel();
        manageInterchangesPanel = new ManageInterchangesPanel();
        
        addProductsPanel = new AddProductsPanel();
        createPackPanel = new CreatePackPanel();
        modifyPacksPanel = new ModifyPacksPanel();
        manageAccountsPanel = new ManageAccountsPanel();
        manageStatisticsPanel = new ManageStatisticsPanel();
        manageRecommendationsPanel = new ManageRecommendationsPanel();
        manageTimePanel = new ManageTimePanel();
        // addProductManuallyPanel = new AddProductManuallyPanel();
        
        descuentosPanel = new DescuentosPanel();
        descuentosCategoriaPanel = new DescuentosCategoriaPanel();
        configuracionPanel = new PlaceholderPanel("Configuración");
        perfilPanel = new PlaceholderPanel("Perfil");
        notificacionesPanel = new NotificacionesPanel();

        // 3. Añadir vistas al contenedor
        contenedorPaneles.add(menuPrincipalPanel, PANEL_MENU_PRINCIPAL);
        contenedorPaneles.add(interchangeCardPanel, PANEL_DETALLE_INTERCAMBIO);
        contenedorPaneles.add(menuEmpleadoPanel, PANEL_MENU_EMPLEADO);
        contenedorPaneles.add(menuGestorPanel, PANEL_MENU_GESTOR);
        contenedorPaneles.add(mySecondHandProductsPanel, PANEL_MY_SECOND_HAND_PRODUCTS);
        contenedorPaneles.add(descuentosPanel, PANEL_DESCUENTOS);
        contenedorPaneles.add(discountedPanel, PANEL_DISCOUNTED);
        contenedorPaneles.add(descuentosCategoriaPanel, PANEL_DESCUENTOS_CATEGORIA);
        contenedorPaneles.add(productosFiltradosPanel, PANEL_PRODUCTOS_FILTRADOS);
        contenedorPaneles.add(carritoPanel, PANEL_CARRITO);
        contenedorPaneles.add(configuracionPanel, PANEL_CONFIGURACION);
        contenedorPaneles.add(perfilPanel, PANEL_PERFIL);
        contenedorPaneles.add(notificacionesPanel, PANEL_NOTIFICACIONES);
        contenedorPaneles.add(outstandingPanel, PANEL_OUTSTANDING);
        contenedorPaneles.add(searchInterchangesPanel, PANEL_SEARCH_INTERCHANGES);
        contenedorPaneles.add(makeOfferPanel, PANEL_MAKE_OFFER);
        contenedorPaneles.add(historialPedidosPanel, PANEL_HISTORIAL_PEDIDOS);
        
        contenedorPaneles.add(manageProductsPanel, PANEL_MANAGE_PRODUCTS);
        contenedorPaneles.add(modifyProductsPanel, PANEL_MODIFY_PRODUCTS);
        contenedorPaneles.add(manageOrdersPanel, PANEL_MANAGE_ORDERS);
        contenedorPaneles.add(addProductsPanel, PANEL_ADD_PRODUCTS);
        contenedorPaneles.add(createPackPanel, PANEL_CREATE_PACK);
        contenedorPaneles.add(modifyPacksPanel, PANEL_MODIFY_PACKS);
        contenedorPaneles.add(manageAccountsPanel, PANEL_MANAGE_ACCOUNTS);
        contenedorPaneles.add(manageStatisticsPanel, PANEL_MANAGE_STATISTICS);
        contenedorPaneles.add(manageRecommendationsPanel, PANEL_MANAGE_RECOMMENDATIONS);
        contenedorPaneles.add(validationRequestsPanel, PANEL_VALIDATION_REQUESTS);
        contenedorPaneles.add(manageTimePanel, PANEL_MANAGE_TIME);
        contenedorPaneles.add(manageInterchangesPanel, PANEL_MANAGE_INTERCHANGES);
        // El panel para añadir producto manualmente ya no se añade al CardLayout porque se muestra
        // mediante una ventana independiente (AddProductManuallyWindow).

        // 4. Configurar la ventana
        setContentPane(contenedorPaneles);
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ImageIcon icon = new ImageIcon("src/assets/appicon.png");
        // setIconImage(icon.getImage());
    }

    // --- MÉTODOS PARA EL CONTROLADOR ---

    /**
     * Mostrar panel.
     *
     * @param nombrePanel the nombre panel
     */
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(contenedorPaneles, nombrePanel);
    }

    /**
     * Gets the menu principal panel.
     *
     * @return the menu principal panel
     */
    // Getters para que los Controladores puedan suscribirse a los botones
    public MenuPrincipalPanel getMenuPrincipalPanel() { return menuPrincipalPanel; }
    
    /**
     * Gets the menu empleado panel.
     *
     * @return the menu empleado panel
     */
    public mainMenuEmpleadoPanel getMenuEmpleadoPanel() { return menuEmpleadoPanel; }
    
    /**
     * Gets the menu gestor panel.
     *
     * @return the menu gestor panel
     */
    public MainMenuGestorPanel getMenuGestorPanel() { return menuGestorPanel; }
    
    /**
     * Gets the carrito panel.
     *
     * @return the carrito panel
     */
    public CarritoPanel getCarritoPanel() { return carritoPanel; }
    
    /**
     * Gets the my second hand products panel.
     *
     * @return the my second hand products panel
     */
    public MySecondHandProductsPanel getMySecondHandProductsPanel() { return mySecondHandProductsPanel; }
    
    /**
     * Gets the productos filtrados panel.
     *
     * @return the productos filtrados panel
     */
    public ProductosFiltradosPanel getProductosFiltradosPanel() { return productosFiltradosPanel; }
    
    /**
     * Gets the outstanding panel.
     *
     * @return the outstanding panel
     */
    public OutstandingPanel getOutstandingPanel() {return outstandingPanel;}
    
    /**
     * Gets the search interchanges panel.
     *
     * @return the search interchanges panel
     */
    public SearchInterchangesPanel getSearchInterchangesPanel() { return searchInterchangesPanel; }
    
    /**
     * Gets the make offer panel.
     *
     * @return the make offer panel
     */
    public MakeOfferPanel getMakeOfferPanel() { return makeOfferPanel; }
    
    /**
     * Gets the historial pedidos panel.
     *
     * @return the historial pedidos panel
     */
    public HistorialPedidosPanel getHistorialPedidosPanel() { return historialPedidosPanel; }
    
    /**
     * Gets the manage products panel.
     *
     * @return the manage products panel
     */
    public ManageProductsPanel getManageProductsPanel() { return manageProductsPanel; }
    
    /**
     * Gets the modify products panel.
     *
     * @return the modify products panel
     */
    public ModifyProductsPanel getModifyProductsPanel() { return modifyProductsPanel; }
    
    /**
     * Gets the manage orders panel.
     *
     * @return the manage orders panel
     */
    public ManageOrdersPanel getManageOrdersPanel() { return manageOrdersPanel; }
    
    /**
     * Gets the validation requests panel.
     *
     * @return the validation requests panel
     */
    public ValidationRequestsPanel getValidationRequestsPanel() { return validationRequestsPanel; }
    
    /**
     * Gets the manage interchanges panel.
     *
     * @return the manage interchanges panel
     */
    public ManageInterchangesPanel getManageInterchangesPanel() { return manageInterchangesPanel; }
    
    /**
     * Gets the manage accounts panel.
     *
     * @return the manage accounts panel
     */
    public ManageAccountsPanel getManageAccountsPanel() { return manageAccountsPanel; }
    
    /**
     * Gets the manage statistics panel.
     *
     * @return the manage statistics panel
     */
    public ManageStatisticsPanel getManageStatisticsPanel() { return manageStatisticsPanel; }
    
    /**
     * Gets the manage recommendations panel.
     *
     * @return the manage recommendations panel
     */
    public ManageRecommendationsPanel getManageRecommendationsPanel() { return manageRecommendationsPanel; }
    
    /**
     * Gets the manage time panel.
     *
     * @return the manage time panel
     */
    public ManageTimePanel getManageTimePanel() { return manageTimePanel; }
    
    /**
     * Gets the adds the products panel.
     *
     * @return the adds the products panel
     */
    public AddProductsPanel getAddProductsPanel() { return addProductsPanel; }
    
    /**
     * Gets the creates the pack panel.
     *
     * @return the creates the pack panel
     */
    public CreatePackPanel getCreatePackPanel() { return createPackPanel; }
    
    /**
     * Gets the modify packs panel.
     *
     * @return the modify packs panel
     */
    public ModifyPacksPanel getModifyPacksPanel() { return modifyPacksPanel; }
    // Ya no hay getter para AddProductManuallyPanel porque es una ventana independiente.
    
    /**
     * Gets the descuentos panel.
     *
     * @return the descuentos panel
     */
    public DescuentosPanel getDescuentosPanel() { return descuentosPanel; }
    
    /**
     * Gets the discounted panel.
     *
     * @return the discounted panel
     */
    public DiscountedPanel getDiscountedPanel() { return discountedPanel; }
    
    /**
     * Gets the descuentos categoria panel.
     *
     * @return the descuentos categoria panel
     */
    public DescuentosCategoriaPanel getDescuentosCategoriaPanel() { return descuentosCategoriaPanel; }
    
    /**
     * Gets the configuracion panel.
     *
     * @return the configuracion panel
     */
    public PlaceholderPanel getConfiguracionPanel() { return configuracionPanel; }
    
    /**
     * Gets the perfil panel.
     *
     * @return the perfil panel
     */
    public PlaceholderPanel getPerfilPanel() { return perfilPanel; }
    
    /**
     * Gets the notificaciones panel.
     *
     * @return the notificaciones panel
     */
    public NotificacionesPanel getNotificacionesPanel() { return notificacionesPanel; }
    
}
