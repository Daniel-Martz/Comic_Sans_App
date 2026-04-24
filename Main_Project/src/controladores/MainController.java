package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.Oferta;
import modelo.usuario.ClienteRegistrado;
import vista.main.MainFrame;
import vista.userWindows.ProposalsWindow;
import vista.userWindows.VentanaInterchangeOptions;

import java.util.List;

/**
 * Controlador Principal (MainController).
 *
 * Es el "director de orquesta" de la aplicación:
 * - Gestiona la navegación entre paneles del MainFrame (CardLayout).
 * - Coordina la apertura de ventanas modales (JDialog).
 * - Crea los controladores de nivel superior cuando se necesitan.
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

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public MainController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.modelo    = Aplicacion.getInstancia();
    }

    // -------------------------------------------------------
    // Navegación de Paneles (CardLayout)
    // -------------------------------------------------------

    /**
     * Cambia el panel visible en el CardLayout del MainFrame.
     */
    public void navegarA(String nombrePanel) {
        mainFrame.mostrarPanel(nombrePanel);
    }

    // -------------------------------------------------------
    // Gestión de Ventanas y Flujos de Negocio
    // -------------------------------------------------------

    /**
     * Muestra la ventana modal con todas las propuestas de intercambio (enviadas y recibidas).
     * Este es ahora el punto central para gestionar los intercambios.
     */
    public void mostrarVentanaPropuestas() {
        // 1. Creamos la vista (JDialog)
        ProposalsWindow ventanaPropuestas = new ProposalsWindow(mainFrame);
        
        // 2. Creamos su controlador, inyectándole la vista y este MainController
        // El controlador se encarga de cargar las ofertas automáticamente al crearse.
        new ControladorProposals(ventanaPropuestas, this);
        
        // 3. Mostramos la ventana
        ventanaPropuestas.mostrar();
    }

    /**
     * Muestra la lista de ofertas del cliente. 
     * Puedes mantener este método para ser llamado desde los botones del menú,
     * delegando directamente en la apertura de la ventana.
     */
    public void mostrarMisIntercambios() {
        if (!(modelo.getUsuarioActual() instanceof ClienteRegistrado)) {
            return;
        }
        mostrarVentanaPropuestas();
    }
     /**
     * Instancia y muestra el JDialog de los filtros avanzados.
     */
    public void abrirVentanaFiltros() {
        // 1. Creamos la vista (JDialog), pasándole el MainFrame como padre
        vista.userWindows.FiltrosDialog dialogFiltros = new vista.userWindows.FiltrosDialog(mainFrame);
        
        // 2. Creamos su controlador específico
        ControladorFiltros controladorFiltros = new ControladorFiltros(dialogFiltros);
        
        // 3. Mostramos la ventana (se quedará bloqueada hasta que el usuario la cierre porque es modal)
        controladorFiltros.mostrarVentana();
    }
    
	public void mostrarVentanaOpcionesIntercambio() {
	    VentanaInterchangeOptions v = new VentanaInterchangeOptions(this.mainFrame);
	    v.setControlador(e -> {
	        String command = e.getActionCommand();
	        if (command.equals("PROPOSALS")) {
	            v.cerrar();
	            mostrarMisIntercambios();
	        }
	    });
	    v.setVisible(true);
	}
}