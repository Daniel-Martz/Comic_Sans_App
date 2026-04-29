package vista.main;

import vista.userPanels.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    // Layout
    private CardLayout cardLayout;
    private JPanel contenedorPaneles;

    // Paneles (Vistas)
    private InterchangeCardPanel interchangeCardPanel;
    private MenuPrincipalPanel menuEmpleadoPanel;
    private MySecondHandProductsPanel mySecondHandProductsPanel;
    private CarritoPanel carritoPanel;
    private ProductosFiltradosPanel productosFiltradosPanel;
    private PlaceholderPanel descuentosPanel;
    private PlaceholderPanel configuracionPanel;
    private PlaceholderPanel perfilPanel;
    private PlaceholderPanel notificacionesPanel;

    // Nombres estáticos de los paneles para el CardLayout (antes estaban en el controlador)
    public static final String PANEL_MENU_PRINCIPAL = "MenuPrincipal";
    public static final String PANEL_DETALLE_INTERCAMBIO = "DetalleIntercambio";
    public static final String PANEL_MY_SECOND_HAND_PRODUCTS = "MySecondHandProducts";
    public static final String PANEL_DESCUENTOS = "Descuentos";
    public static final String PANEL_PRODUCTOS_FILTRADOS = "ProductosFiltrados";
    public static final String PANEL_CARRITO = "Carrito";
    public static final String PANEL_CONFIGURACION = "Configuracion";
    public static final String PANEL_PERFIL = "Perfil";
    public static final String PANEL_NOTIFICACIONES = "Notificaciones";

    public MainFrame() {
        super("Comic Sans App");

        // 1. Crear el layout
        cardLayout = new CardLayout();
        contenedorPaneles = new JPanel(cardLayout);

        // 2. Instanciar SÓLO los paneles (cero Controladores)
        interchangeCardPanel = new InterchangeCardPanel();
        menuEmpleadoPanel = new MenuPrincipalPanel();
        mySecondHandProductsPanel = new MySecondHandProductsPanel();
        carritoPanel = new CarritoPanel();
        productosFiltradosPanel = new ProductosFiltradosPanel();
        
        descuentosPanel = new PlaceholderPanel("Descuentos");
        configuracionPanel = new PlaceholderPanel("Configuración");
        perfilPanel = new PlaceholderPanel("Perfil");
        notificacionesPanel = new PlaceholderPanel("Notificaciones");

        // 3. Añadir vistas al contenedor
        contenedorPaneles.add(interchangeCardPanel, PANEL_DETALLE_INTERCAMBIO);
        contenedorPaneles.add(menuEmpleadoPanel, PANEL_MENU_PRINCIPAL);
        contenedorPaneles.add(mySecondHandProductsPanel, PANEL_MY_SECOND_HAND_PRODUCTS);
        contenedorPaneles.add(descuentosPanel, PANEL_DESCUENTOS);
        contenedorPaneles.add(productosFiltradosPanel, PANEL_PRODUCTOS_FILTRADOS);
        contenedorPaneles.add(carritoPanel, PANEL_CARRITO);
        contenedorPaneles.add(configuracionPanel, PANEL_CONFIGURACION);
        contenedorPaneles.add(perfilPanel, PANEL_PERFIL);
        contenedorPaneles.add(notificacionesPanel, PANEL_NOTIFICACIONES);

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
    public MenuPrincipalPanel getMenuEmpleadoPanel() { return menuEmpleadoPanel; }
    public CarritoPanel getCarritoPanel() { return carritoPanel; }
    public MySecondHandProductsPanel getMySecondHandProductsPanel() { return mySecondHandProductsPanel; }
    public ProductosFiltradosPanel getProductosFiltradosPanel() { return productosFiltradosPanel; }
    public PlaceholderPanel getDescuentosPanel() { return descuentosPanel; }
    public PlaceholderPanel getConfiguracionPanel() { return configuracionPanel; }
    public PlaceholderPanel getPerfilPanel() { return perfilPanel; }
    public PlaceholderPanel getNotificacionesPanel() { return notificacionesPanel; }
}
