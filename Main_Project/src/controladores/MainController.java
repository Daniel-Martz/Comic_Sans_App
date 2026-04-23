package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.Oferta;
import modelo.usuario.ClienteRegistrado;
import vista.main.MainFrame;
import vista.userWindows.ProposalsWindow;

import java.util.List;

/**
 * Controlador Principal (MainController).
 *
 * Es el "director de orquesta" de la aplicación:
 * - Gestiona la navegación entre paneles del MainFrame (CardLayout).
 * - Coordina la apertura de ventanas modales (JDialog).
 * - Crea los controladores de nivel superior cuando se necesitan.
 *
 * ┌─────────────────────────────────────────────────────┐
 * │                   MainFrame                         │
 * │  (contiene CardLayout con paneles: Login, Menú...)  │
 * └──────────────────────┬──────────────────────────────┘
 * │ usa
 * ┌──────────────────────▼──────────────────────────────┐
 * │                 MainController                      │
 * │  - navegarA(panel)                                  │
 * │  - mostrarVentanaPropuestas() ──┐                   │
 * └─────────────────────────────────│───────────────────┘
 * │ crea
 * ▼
 * ┌─────────────────────────────────────────────────────┐
 * │             ControladorProposals                    │
 * │  (Gestiona la lógica de la lista de intercambios)   │
 * └─────────────────────────────────────────────────────┘
 */
public class MainController {

    // -------------------------------------------------------
    // Constantes de navegación (nombres de paneles en CardLayout)
    // -------------------------------------------------------
    public static final String PANEL_LOGIN              = "LOGIN";
    public static final String PANEL_MENU_PRINCIPAL     = "MENU_PRINCIPAL";
    // Si la lista de intercambios ahora es un JDialog, PANEL_MIS_INTERCAMBIOS 
    // podría ser redundante si no hay un panel físico en el MainFrame para ello.

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
}