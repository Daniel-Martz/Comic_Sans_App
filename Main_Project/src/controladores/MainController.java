package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.Oferta;
import modelo.usuario.ClienteRegistrado;
import vista.main.MainFrame;
import vista.userWindows.CrearUsuarioDialog;
import vista.userWindows.LoginDialog;
import vista.userWindows.ProposalsWindow;
import vista.userWindows.VentanaInterchangeOptions;
import vista.userWindows.VentanaRegistroRequerido;

import javax.swing.JOptionPane;

import java.util.List;
import vista.userPanels.ProductosFiltradosPanel;
import controlador.CreateAccountController;


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
    public static final String PANEL_MY_SECOND_HAND_PRODUCTS = "MY_SECOND_HAND_PRODUCTS";
    // Añade aquí más paneles según los necesites

    // -------------------------------------------------------
    // Referencias
    // -------------------------------------------------------
    private final MainFrame mainFrame;
    private final Aplicacion modelo;
    private vista.userWindows.CrearUsuarioDialog crearUsuarioDialog;
    private vista.userWindows.LoginDialog loginDialog;
    private vista.userWindows.FiltrosDialog dialogFiltros;
    private ControladorFiltros controladorFiltros;

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
        if (this.dialogFiltros == null) {
            // 1. Creamos la vista (JDialog), pasándole el MainFrame como padre
            this.dialogFiltros = new vista.userWindows.FiltrosDialog(mainFrame);
            // 2. Creamos su controlador específico solo una vez
            this.controladorFiltros = new ControladorFiltros(this.dialogFiltros);
        }
        
        // 3. Mostramos la ventana (se quedará bloqueada hasta que el usuario la cierre porque es modal)
        this.controladorFiltros.mostrarVentana();
    }

    /**
     * Configura el filtro para una categoría y muestra los productos.
     */
    public void mostrarProductosPorCategoria(String categoria) {
        if (this.dialogFiltros == null) {
            this.dialogFiltros = new vista.userWindows.FiltrosDialog(mainFrame);
            this.controladorFiltros = new ControladorFiltros(this.dialogFiltros);
        }
        
        // Reset old filters first
        this.dialogFiltros.resetFiltros();
        
        // Check the requested category
        if ("COMICS".equals(categoria)) {
            this.dialogFiltros.getChkComics().setSelected(true);
        } else if ("FIGURES".equals(categoria)) {
            this.dialogFiltros.getChkFigures().setSelected(true);
        } else if ("BOARD_GAMES".equals(categoria)) {
            this.dialogFiltros.getChkBoardGames().setSelected(true);
        }
        
        // Show filtered products
        mostrarProductosFiltrados("");
    }

    /**
     * Muestra el panel de productos filtrados y lo actualiza con el prompt dado.
     */
    public void mostrarProductosFiltrados(String prompt) {
        ProductosFiltradosPanel panel = mainFrame.getProductosFiltradosPanel();
        // Creamos un controlador específico para manejar acciones dentro del panel
        ControladorProductosFiltrados controlador = new ControladorProductosFiltrados(panel, this.dialogFiltros);
        controlador.buscarYActualizar(prompt);
        mainFrame.mostrarPanel(PANEL_PRODUCTOS_FILTRADOS);
    }
    
	public void mostrarVentanaOpcionesIntercambio() {
      // Si el usuario no es un ClienteRegistrado, informamos y ofrecemos registrar
      if (!(modelo.getUsuarioActual() instanceof ClienteRegistrado)) {
       // Instanciamos nuestra nueva ventana bonita
          VentanaRegistroRequerido dialogoRequerido = new VentanaRegistroRequerido(mainFrame);

          // Mostramos la ventana y guardamos la elección del usuario
          int eleccion = dialogoRequerido.mostrarVentana();

          // Procesamos la decisión
          if (eleccion == VentanaRegistroRequerido.INICIAR_SESION) {
              // Aquí abrimos la ventana de login. 
              // abrirVentanaLogin();
              System.out.println("Llevando al usuario a Iniciar Sesión...");
          } 
          else if (eleccion == VentanaRegistroRequerido.REGISTRARSE) {
              abrirVentanaCrearUsuario();
          }
          // Si elige CANCELAR o cierra la ventana, simplemente no hacemos nada (o vuelves a la vista anterior)
          return;
      }

      VentanaInterchangeOptions v = new VentanaInterchangeOptions(this.mainFrame);
	    v.setControlador(e -> {
	        String command = e.getActionCommand();
	        if (command.equals("PROPOSALS")) {
	            v.cerrar();
	            mostrarMisIntercambios();
	        }
	        if(command.equals("MY SECOND-HAND PRODUCTS")) {
	        	v.cerrar();
	        	mostrarMisProductosSegundaMano();
	        }
	    });
	    v.setVisible(true);
	}

    /**
     * Navega al panel que muestra los productos de segunda mano del usuario.
     */
	public void mostrarMisProductosSegundaMano() {
		navegarA(PANEL_MY_SECOND_HAND_PRODUCTS);
	}

    
    public void abrirVentanaCrearUsuario(){
    	this.crearUsuarioDialog = new CrearUsuarioDialog(mainFrame, this);
    	crearUsuarioDialog.setVisible(true);
    }
    
    public void abrirVentanaLogIn(){
    	this.loginDialog = new LoginDialog(mainFrame, this);
    	loginDialog.setVisible(true);
    }
    
    public void cerrarVentanaCrearUsuario() {
    	this.crearUsuarioDialog.setVisible(false);
    }

    public void cerrarVentanaLogIn() {
    	this.loginDialog.setVisible(false);
    }
    
    public void navegarBotonPerfil() {
    	if(Aplicacion.getInstancia().getUsuarioActual() != null) {
    		abrirVentanaLogIn();
    	}else {
    		
    	}
    }
}
