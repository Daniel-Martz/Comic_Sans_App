package vista.main;

import vista.userPanels.*;
import vista.empleadoPanel.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    // Layout
    private CardLayout cardLayout;
    private JPanel contenedorPaneles;

    // Paneles (Vistas)
    private InterchangeCardPanel interchangeCardPanel;
    private mainMenuEmpleadoPanel menuEmpleadoPanel;
    private MenuPrincipalPanel menuPrincipalPanel;
    private MySecondHandProductsPanel mySecondHandProductsPanel;
    private CarritoPanel carritoPanel;
    private ProductosFiltradosPanel productosFiltradosPanel;
    private OutstandingPanel outstandingPanel;
    private SearchInterchangesPanel searchInterchangesPanel;
    private MakeOfferPanel makeOfferPanel;
    private HistorialPedidosPanel historialPedidosPanel;
    
    private ManageProductsPanel manageProductsPanel;
    private ModifyProductsPanel modifyProductsPanel;
    
    private AddProductsPanel addProductsPanel;
    // El formulario de añadir producto manualmente ahora es una ventana independiente (AddProductManuallyWindow)
    // por tanto no mantenemos aquí el panel como componente del CardLayout.
    
    private PlaceholderPanel descuentosPanel;
    private PlaceholderPanel configuracionPanel;
    private PlaceholderPanel perfilPanel;
    private PlaceholderPanel notificacionesPanel;


    // Nombres estáticos de los paneles para el CardLayout (antes estaban en el controlador)
    public static final String PANEL_MENU_EMPLEADO = "MenuEmpleado";
    public static final String PANEL_MENU_PRINCIPAL = "MenuPrincipal";
    public static final String PANEL_DETALLE_INTERCAMBIO = "DetalleIntercambio";
    public static final String PANEL_MY_SECOND_HAND_PRODUCTS = "MySecondHandProducts";
    public static final String PANEL_DESCUENTOS = "Descuentos";
    public static final String PANEL_PRODUCTOS_FILTRADOS = "ProductosFiltrados";
    public static final String PANEL_CARRITO = "Carrito";
    public static final String PANEL_CONFIGURACION = "Configuracion";
    public static final String PANEL_PERFIL = "Perfil";
    public static final String PANEL_NOTIFICACIONES = "Notificaciones";
    public static final String PANEL_OUTSTANDING = "Outstanding";
    public static final String PANEL_SEARCH_INTERCHANGES = "SearchInterchanges";
    public static final String PANEL_MAKE_OFFER = "MakeOffer";
    public static final String PANEL_HISTORIAL_PEDIDOS = "HistorialPedidos";
    public static final String PANEL_MANAGE_PRODUCTS = "ManageProducts";
    public static final String PANEL_MODIFY_PRODUCTS = "ModifyProducts";
    public static final String PANEL_MODIFY_A_PRODUCT = "ModifyAProduct";
    public static final String PANEL_ADD_PRODUCTS = "AddProducts";
    public static final String PANEL_LOAD_FROM_FILE = "LoadFromFile";
    public static final String PANEL_ADD_PRODUCT_MANUALLY = "AddProductManually";

    public MainFrame() {
        super("Comic Sans App");

        // 1. Crear el layout
        cardLayout = new CardLayout();
        contenedorPaneles = new JPanel(cardLayout);

        // 2. Instanciar SÓLO los paneles (cero Controladores)
        menuPrincipalPanel = new MenuPrincipalPanel();
        interchangeCardPanel = new InterchangeCardPanel();
        menuEmpleadoPanel = new mainMenuEmpleadoPanel();
        mySecondHandProductsPanel = new MySecondHandProductsPanel();
        carritoPanel = new CarritoPanel();
        productosFiltradosPanel = new ProductosFiltradosPanel();
        outstandingPanel = new OutstandingPanel();
        searchInterchangesPanel = new SearchInterchangesPanel();
        makeOfferPanel = new MakeOfferPanel();
        historialPedidosPanel = new HistorialPedidosPanel();
        
        manageProductsPanel = new ManageProductsPanel();
        modifyProductsPanel = new ModifyProductsPanel();
        
        addProductsPanel = new AddProductsPanel();
        // addProductManuallyPanel = new AddProductManuallyPanel();
        
        descuentosPanel = new PlaceholderPanel("Descuentos");
        configuracionPanel = new PlaceholderPanel("Configuración");
        perfilPanel = new PlaceholderPanel("Perfil");
        notificacionesPanel = new PlaceholderPanel("Notificaciones");

        // 3. Añadir vistas al contenedor
        contenedorPaneles.add(menuPrincipalPanel, PANEL_MENU_PRINCIPAL);
        contenedorPaneles.add(interchangeCardPanel, PANEL_DETALLE_INTERCAMBIO);
        contenedorPaneles.add(menuEmpleadoPanel, PANEL_MENU_EMPLEADO);
        contenedorPaneles.add(mySecondHandProductsPanel, PANEL_MY_SECOND_HAND_PRODUCTS);
        contenedorPaneles.add(descuentosPanel, PANEL_DESCUENTOS);
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
        contenedorPaneles.add(addProductsPanel, PANEL_ADD_PRODUCTS);
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

    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(contenedorPaneles, nombrePanel);
    }

    // Getters para que los Controladores puedan suscribirse a los botones
    public MenuPrincipalPanel getMenuPrincipalPanel() { return menuPrincipalPanel; }
    public mainMenuEmpleadoPanel getMenuEmpleadoPanel() { return menuEmpleadoPanel; }
    public CarritoPanel getCarritoPanel() { return carritoPanel; }
    public MySecondHandProductsPanel getMySecondHandProductsPanel() { return mySecondHandProductsPanel; }
    public ProductosFiltradosPanel getProductosFiltradosPanel() { return productosFiltradosPanel; }
    public OutstandingPanel getOutstandingPanel() {return outstandingPanel;}
    public SearchInterchangesPanel getSearchInterchangesPanel() { return searchInterchangesPanel; }
    public MakeOfferPanel getMakeOfferPanel() { return makeOfferPanel; }
    public HistorialPedidosPanel getHistorialPedidosPanel() { return historialPedidosPanel; }
    
    public ManageProductsPanel getManageProductsPanel() { return manageProductsPanel; }
    public ModifyProductsPanel getModifyProductsPanel() { return modifyProductsPanel; }
    
    public AddProductsPanel getAddProductsPanel() { return addProductsPanel; }
    // Ya no hay getter para AddProductManuallyPanel porque es una ventana independiente.
    
    public PlaceholderPanel getDescuentosPanel() { return descuentosPanel; }
    public PlaceholderPanel getConfiguracionPanel() { return configuracionPanel; }
    public PlaceholderPanel getPerfilPanel() { return perfilPanel; }
    public PlaceholderPanel getNotificacionesPanel() { return notificacionesPanel; }
    
}