package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
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

    public void iniciar() {
        registrarListeners();
        
        // Mostrar panel inicial
        navegarA(MainFrame.PANEL_MENU_PRINCIPAL);
        
        // Hacer visible la ventana
        mainFrame.setVisible(true);
    }
    
    private void registrarListeners() {
        // --- Conectar Menu Empleado ---
        mainFrame.getMenuPrincipalPanel().addHomeListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getMenuPrincipalPanel().addDescuentosListener(e -> navegarA(MainFrame.PANEL_DESCUENTOS));
        mainFrame.getMenuPrincipalPanel().addOutstandingListener(e -> mostrarProductosOutstanding());
        
        // Búsqueda
        mainFrame.getMenuPrincipalPanel().addSearchListener(e -> {
            String prompt = e.getActionCommand();
            mostrarProductosFiltrados(prompt);
        });

        // Botón Intercambios
        mainFrame.getMenuPrincipalPanel().addIntercambiosListener(e -> mostrarVentanaOpcionesIntercambio());
        
        // Botón Perfil y Notificaciones
        mainFrame.getMenuPrincipalPanel().addPerfilListener(e -> navegarBotonPerfil());
        mainFrame.getMenuPrincipalPanel().addNotificacionesListener(e -> navegarA(MainFrame.PANEL_NOTIFICACIONES));
        mainFrame.getMenuPrincipalPanel().addFiltrosListener(e -> abrirVentanaFiltros());
        mainFrame.getMenuPrincipalPanel().addBuyNowListener(e -> {
            String cmd = e.getActionCommand();
            if (cmd != null && cmd.startsWith("ADD_")) {
                try {
                    int id = Integer.parseInt(cmd.substring(4));
                    modelo.producto.LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                    if (p != null) {
                        modelo.usuario.Usuario u = modelo.getUsuarioActual();
                        if (!(u instanceof ClienteRegistrado)) {
                            JOptionPane.showMessageDialog(mainFrame, "You must be logged in as a customer to add products to the cart.", "Not logged in", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        ClienteRegistrado cliente = (ClienteRegistrado) u;
                        cliente.añadirProductoACarrito(p, 1);
                        JOptionPane.showMessageDialog(mainFrame, "Product added to cart.", "Added", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            } else if (cmd != null && cmd.startsWith("INFO_")) {
                try {
                    int id = Integer.parseInt(cmd.substring(5));
                    modelo.producto.LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                    if (p != null) {
                        vista.userWindows.VentanaDetallesProducto dialog = new vista.userWindows.VentanaDetallesProducto(mainFrame, p);
                        dialog.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            } else {
                navegarA(MainFrame.PANEL_PRODUCTOS_FILTRADOS);
            }
        });

        mainFrame.getMenuPrincipalPanel().addCategoryListener(e -> {
            String categoria = e.getActionCommand();
            mostrarProductosPorCategoria(categoria);
        });

        // --- Conectar Volver de otros paneles ---
        mainFrame.getOutstandingPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getMySecondHandProductsPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getDescuentosPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getProductosFiltradosPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getConfiguracionPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getPerfilPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
        mainFrame.getNotificacionesPanel().addVolverListener(e -> navegarA(MainFrame.PANEL_MENU_PRINCIPAL));
    }
    
    // -------------------------------------------------------
    // Navegación de Paneles (CardLayout)
    // -------------------------------------------------------

    /**
     * Cambia el panel visible en el CardLayout del MainFrame.
     */
    public void navegarA(String nombrePanel) {
        if (nombrePanel.equals(MainFrame.PANEL_MENU_PRINCIPAL)) {
            // Actualizar recomendaciones al volver al menú principal
            java.util.Set<modelo.producto.LineaProductoVenta> rec = modelo.getConfiguracionRecomendacion().getRecomendacion();
            mainFrame.getMenuPrincipalPanel().actualizarRecomendados(rec);
        }
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
        mainFrame.mostrarPanel(mainFrame.PANEL_PRODUCTOS_FILTRADOS);
    }

    /**
     * Muestra el panel de productos filtrados pero solo con los productos destacados (valoración 4-5).
     */
    public void mostrarProductosOutstanding() {
        vista.userPanels.OutstandingPanel panel = mainFrame.getOutstandingPanel();
        new ControladorOutstanding(panel);
        mainFrame.mostrarPanel(MainFrame.PANEL_OUTSTANDING);
    }
    
    /**
     * Verifica si el usuario actual es un cliente registrado. 
     * Si no lo es, muestra la ventana de aviso para iniciar sesión o registrarse.
     * @return true si el usuario es cliente registrado, false en caso contrario.
     */
    private boolean verificarAccesoClienteRegistrado() {
        if (!(modelo.getUsuarioActual() instanceof ClienteRegistrado)) {
            VentanaRegistroRequerido dialogoRequerido = new VentanaRegistroRequerido(mainFrame);
            int eleccion = dialogoRequerido.mostrarVentana();

            if (eleccion == VentanaRegistroRequerido.INICIAR_SESION) {
                abrirVentanaLogIn();
            } else if (eleccion == VentanaRegistroRequerido.REGISTRARSE) {
                abrirVentanaCrearUsuario();
            }
            return false;
        }
        return true;
    }

    public void mostrarVentanaOpcionesIntercambio() {
        if (!verificarAccesoClienteRegistrado()) {
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
     * Gestiona el flujo de entrada al carrito.
     * @param ctrlCarrito El controlador del carrito para refrescar los datos.
     */
    public void gestionarAccesoCarrito(ControladorCarrito ctrlCarrito) {
        if (verificarAccesoClienteRegistrado()) {
            ctrlCarrito.refrescarVista();
            navegarA(MainFrame.PANEL_CARRITO);
        }
    }

    /**
     * Navega al panel que muestra los productos de segunda mano del usuario.
     */
	public void mostrarMisProductosSegundaMano() {
		navegarA(MainFrame.PANEL_MY_SECOND_HAND_PRODUCTS);
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
    	if(Aplicacion.getInstancia().getUsuarioActual() == null) {
    		abrirVentanaLogIn();
    	}else {
    		
    	}
    }
}
