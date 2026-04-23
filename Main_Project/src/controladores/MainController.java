package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.Oferta;
import modelo.usuario.ClienteRegistrado;
import vista.main.MainFrame;
import vista.userPanels.InterchangeCardPanel;

import java.util.List;

/**
 * Controlador Principal (MainController).
 *
 * Es el "director de orquesta" de la aplicación:
 *  - Gestiona la navegación entre paneles (CardLayout).
 *  - Crea los controladores específicos cuando se necesitan.
 *  - Es el único que conoce el MainFrame.
 *
 * ┌─────────────────────────────────────────────────────┐
 * │                   MainFrame                         │
 * │  (contiene CardLayout con todos los paneles)        │
 * └──────────────────────┬──────────────────────────────┘
 *                        │ usa
 * ┌──────────────────────▼──────────────────────────────┐
 * │                 MainController                      │
 * │  - navegarA(panel)                                  │
 * │  - mostrarOferta(oferta)  → crea ControladorIntercambio│
 * └──────────────────────┬──────────────────────────────┘
 *                        │ crea
 * ┌──────────────────────▼──────────────────────────────┐
 * │          ControladorIntercambio                     │
 * │  - Conecta InterchangeCardPanel ↔ Oferta (modelo)   │
 * └─────────────────────────────────────────────────────┘
 */
public class MainController {

    // -------------------------------------------------------
    // Constantes de navegación (nombres de paneles en CardLayout)
    // -------------------------------------------------------
    public static final String PANEL_LOGIN              = "LOGIN";
    public static final String PANEL_MENU_PRINCIPAL     = "MENU_PRINCIPAL";
    public static final String PANEL_MIS_INTERCAMBIOS   = "MIS_INTERCAMBIOS";
    public static final String PANEL_DETALLE_INTERCAMBIO = "DETALLE_INTERCAMBIO";
    // Paneles añadidos para navegación desde el menú principal
    public static final String PANEL_DESCUENTOS         = "DESCUENTOS";
    public static final String PANEL_PRODUCTOS_FILTRADOS= "PRODUCTOS_FILTRADOS";
    public static final String PANEL_CARRITO            = "CARRITO";
    public static final String PANEL_CONFIGURACION      = "CONFIGURACION";
    public static final String PANEL_PERFIL             = "PERFIL";
    public static final String PANEL_NOTIFICACIONES     = "NOTIFICACIONES";
    // Añade aquí más paneles según los necesites

    // -------------------------------------------------------
    // Referencias
    // -------------------------------------------------------
    private final MainFrame mainFrame;
    private final Aplicacion modelo;

    // Controlador activo de intercambio (se recrea cada vez)
    private ControladorIntercambio controladorIntercambioActual;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public MainController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.modelo    = Aplicacion.getInstancia();
    }

    // -------------------------------------------------------
    // Navegación
    // -------------------------------------------------------

    /**
     * Cambia el panel visible en el CardLayout del MainFrame.
     * @param nombrePanel una de las constantes PANEL_* definidas arriba
     */
    public void navegarA(String nombrePanel) {
        mainFrame.mostrarPanel(nombrePanel);
    }

    // -------------------------------------------------------
    // Métodos de negocio que crean controladores específicos
    // -------------------------------------------------------

    /**
     * Muestra el detalle de una oferta concreta.
     * Crea un nuevo ControladorIntercambio que conecta
     * el panel de la vista con la oferta del modelo.
     *
     * @param oferta la oferta que se quiere visualizar
     */
    public void mostrarDetalleOferta(Oferta oferta) {
        // 1. Obtenemos el panel de la vista desde el MainFrame
        InterchangeCardPanel panel = mainFrame.getInterchangeCardPanel();

        // 2. Creamos el controlador específico.
        //    Él solo se encarga de cargar datos y registrar listeners.
        controladorIntercambioActual = new ControladorIntercambio(panel, oferta, this);

        // 3. Navegamos a ese panel
        navegarA(PANEL_DETALLE_INTERCAMBIO);
    }

    /**
     * Muestra la lista de ofertas recibidas por el cliente actual.
     * Aquí crearías otro controlador específico (ControladorListaOfertas, etc.)
     */
    public void mostrarMisIntercambios() {
        if (!(modelo.getUsuarioActual() instanceof ClienteRegistrado)) {
            return;
        }
        ClienteRegistrado cliente = (ClienteRegistrado) modelo.getUsuarioActual();
        List<Oferta> ofertas = cliente.getOfertasRecibidas();

        // Aquí pasarías las ofertas a un panel de lista
        // Por ejemplo: mainFrame.getListaOfertasPanel().cargar(ofertas);
        // y crearías un ControladorListaOfertas

        navegarA(PANEL_MIS_INTERCAMBIOS);
    }
}
