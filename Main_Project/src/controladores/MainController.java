package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.notificacion.Notificacion;
import modelo.solicitud.Oferta;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Empleado;
import modelo.usuario.Gestor;
import modelo.usuario.Usuario;
import vista.main.MainFrame;
import vista.userWindows.CrearUsuarioDialog;
import vista.userWindows.EditProfileDialog;
import vista.userWindows.FiltrosDialog;
import vista.userWindows.LoginDialog;
import vista.userWindows.NotificacionDialog;
import vista.userWindows.ProposalsWindow;
import vista.userWindows.VentanaInterchangeOptions;
import vista.userWindows.VentanaRegistroRequerido;
import vista.userWindows.UsuarioOptionsDialog;
import javax.swing.JOptionPane;

import java.util.List;
import java.util.Set;
import vista.userPanels.*;
import vista.empleadoPanel.*;
import controlador.*;
import controlador.LoginController;
import controlador.LoginToCreateAccountController;


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
    private vista.userWindows.EditProfileDialog editProfileDialog;
    private vista.userWindows.FiltrosDialog dialogFiltros;
    private vista.userWindows.UsuarioOptionsDialog dialogOpcionesUsuario;
    private ControladorFiltros controladorFiltros;
    private ControladorSearchInterchanges controladorSearchInterchanges;
    private ControladorMakeOffer controladorMakeOffer;
    private NotificacionDialog notificacionDialog;

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
        mostrarMenuPrincipal();
        
        // Hacer visible la ventana
        mainFrame.setVisible(true);
    }
    
    private void registrarListeners() {
        // --- Conectar Cabeceras de Navegación Global ---
        conectarHeaderEmpleado(mainFrame.getMenuEmpleadoPanel().getHeaderPanel());
        conectarHeaderGestor(mainFrame.getMenuGestorPanel().getHeaderPanel());

        conectarHeaderNormal(mainFrame.getMenuPrincipalPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getMySecondHandProductsPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getCarritoPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getOutstandingPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getProductosFiltradosPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getDescuentosPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getConfiguracionPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getPerfilPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getNotificacionesPanel().getHeaderPanel());
        conectarHeaderNormal(mainFrame.getHistorialPedidosPanel().getHeaderPanel());
        
        // La de búsqueda de intercambios lleva una búsqueda especial propia, así que la normal no
        conectarHeaderGlobal(mainFrame.getSearchInterchangesPanel().getHeaderPanel());
        conectarHeaderGlobal(mainFrame.getMakeOfferPanel().getHeaderPanel());
        
        conectarHeaderEmpleado(mainFrame.getManageProductsPanel().getHeaderPanel());
        conectarHeaderEmpleado(mainFrame.getAddProductsPanel().getHeaderPanel());
        conectarHeaderEmpleado(mainFrame.getManageOrdersPanel().getHeaderPanel());
        conectarHeaderEmpleado(mainFrame.getValidationRequestsPanel().getHeaderPanel());
        conectarHeaderEmpleado(mainFrame.getManageInterchangesPanel().getHeaderPanel());
        
        new ControladorManageProducts(mainFrame, this);
        new ControladorAddProducts(mainFrame, this);
        new ControladorManageOrders(mainFrame.getManageOrdersPanel(), mainFrame);
        new ControladorValidationRequests(mainFrame.getValidationRequestsPanel(), mainFrame, this);
        new ControladorManageInterchanges(mainFrame.getManageInterchangesPanel(), mainFrame, this);
        
        mainFrame.getMenuEmpleadoPanel().addManageOrdersListener(e -> {
            ControladorManageOrders ctrl = mainFrame.getManageOrdersPanel().getControlador();
            if (ctrl != null) {
                ctrl.actualizarPedidos();
            }
            navegarA(MainFrame.PANEL_MANAGE_ORDERS);
        });
        
        mainFrame.getMenuEmpleadoPanel().addValidationRequestsListener(e -> {
            ControladorValidationRequests ctrl = mainFrame.getValidationRequestsPanel().getControlador();
            if (ctrl != null) {
                ctrl.actualizarSolicitudes();
            }
            navegarA(MainFrame.PANEL_VALIDATION_REQUESTS);
        });
        
        mainFrame.getMenuEmpleadoPanel().addManageInterchangesListener(e -> {
            ControladorManageInterchanges ctrl = mainFrame.getManageInterchangesPanel().getControlador();
            if (ctrl != null) {
                ctrl.actualizarSolicitudes();
            }
            navegarA(MainFrame.PANEL_MANAGE_INTERCHANGES);
        });
        
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

        mainFrame.getHistorialPedidosPanel().addListenerForButtons(new ControladorHistorialPedidos(mainFrame.getHistorialPedidosPanel(), this));
        mainFrame.getNotificacionesPanel().addListenerForElements(new ControladorNotificaciones(mainFrame.getNotificacionesPanel(), this));
    }

    /**
     * Asigna la lógica global básica para un empleado/gestor.
     */
    private void conectarHeaderEmpleado(HeaderPanel header) {
        header.addHomeListener(e -> mostrarMenuPrincipal());
        header.addPerfilListener(e -> navegarBotonPerfil());
        header.addNotificacionesListener(e ->
        { navegarA(MainFrame.PANEL_NOTIFICACIONES); 
          Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
          if((usuarioActual instanceof Empleado emp)){
            emp.getNotificaciones().forEach(n -> mainFrame.getNotificacionesPanel().agregarNotificacion(n)); 
          }
          if((usuarioActual instanceof ClienteRegistrado c)){
            c.getNotificaciones().forEach(n -> mainFrame.getNotificacionesPanel().agregarNotificacion(n)); 
          }
      });
    }

    /**
     * Asigna la lógica global básica para un gestor.
     */
    private void conectarHeaderGestor(HeaderPanel header) {
        header.addHomeListener(e -> mostrarMenuPrincipal());
        header.addPerfilListener(e -> navegarBotonPerfil());
    }

    /**
     * Asigna la lógica global junto con el buscador y filtros del catálogo.
     */
    private void conectarHeaderNormal(HeaderPanel header) {
        conectarHeaderGlobal(header);
        header.addSearchListener(e -> {
            String prompt = e.getActionCommand();
            mostrarProductosFiltrados(prompt);
        });
        header.addFiltrosListener(e -> abrirVentanaFiltros());
    }

    /**
     * Asigna la lógica de navegación a todas las opciones de una cabecera global.
     */
    private void conectarHeaderGlobal(HeaderPanel header) {
        header.addHomeListener(e -> mostrarMenuPrincipal());
        header.addDescuentosListener(e -> navegarA(MainFrame.PANEL_DESCUENTOS));
        header.addOutstandingListener(e -> mostrarProductosOutstanding());
        header.addIntercambiosListener(e -> mostrarVentanaOpcionesIntercambio());
        header.addPerfilListener(e -> navegarBotonPerfil());
        header.addNotificacionesListener(e -> 
        { navegarA(MainFrame.PANEL_NOTIFICACIONES); 
          Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
          if((usuarioActual instanceof Empleado emp)){
            emp.getNotificaciones().forEach(n -> mainFrame.getNotificacionesPanel().agregarNotificacion(n)); 
          }
          if((usuarioActual instanceof ClienteRegistrado c)){
            c.getNotificaciones().forEach(n -> mainFrame.getNotificacionesPanel().agregarNotificacion(n)); 
          }
      });
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

    /**
     * Navega al menú principal correspondiente según el tipo de usuario activo.
     */
    public void mostrarMenuPrincipal() {
        Usuario u = modelo.getUsuarioActual();
        if (u instanceof Gestor) {
            navegarA(MainFrame.PANEL_MENU_GESTOR);
        } else if (u instanceof Empleado) {
            navegarA(MainFrame.PANEL_MENU_EMPLEADO);
        } else {
            navegarA(MainFrame.PANEL_MENU_PRINCIPAL);
        }
    }

    public void mostrarManageCategories() {
        ManageCategoriesController mcc = new ManageCategoriesController(mainFrame);
        mcc.mostrarVentana();
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
	        if(command.equals("SEARCH_INTERCHANGES")) {
	            v.cerrar();
	            mostrarBuscarIntercambios();
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
     * Asigna el listener del botón del carrito de compra a todas las cabeceras.
     */
    public void registrarListenerCarritoGlobal(ControladorCarrito ctrlCarrito) {
        mainFrame.getMenuPrincipalPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getSearchInterchangesPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getMySecondHandProductsPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getCarritoPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getOutstandingPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getProductosFiltradosPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getDescuentosPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getConfiguracionPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getPerfilPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getNotificacionesPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
        mainFrame.getMakeOfferPanel().getHeaderPanel().addCarritoListener(e -> gestionarAccesoCarrito(ctrlCarrito));
    }

    /**
     * Navega al panel que muestra los productos de segunda mano del usuario.
     */
	public void mostrarMisProductosSegundaMano() {
		navegarA(MainFrame.PANEL_MY_SECOND_HAND_PRODUCTS);
	}
    
    /**
     * Navega al panel para buscar nuevos intercambios e instancia su controlador.
     */
    public void mostrarBuscarIntercambios() {
        if (this.controladorSearchInterchanges == null) {
            SearchInterchangesPanel panel = mainFrame.getSearchInterchangesPanel();
            this.controladorSearchInterchanges = new ControladorSearchInterchanges(panel, this);
        } else {
            this.controladorSearchInterchanges.recargar();
        }
        navegarA(MainFrame.PANEL_SEARCH_INTERCHANGES);
    }

    /**
     * Navega al panel final para conformar la oferta, pre-cargando los productos seleccionados.
     */
    public void mostrarMakeOffer(Set<modelo.producto.ProductoSegundaMano> preseleccionados) {
        if (this.controladorMakeOffer == null) {
            vista.userPanels.MakeOfferPanel panel = mainFrame.getMakeOfferPanel();
            this.controladorMakeOffer = new ControladorMakeOffer(panel, this, preseleccionados);
        } else {
            this.controladorMakeOffer.recargar(preseleccionados);
        }
        navegarA(MainFrame.PANEL_MAKE_OFFER);
    }
    
    public void abrirVentanaCrearUsuario(){
    	this.crearUsuarioDialog = new CrearUsuarioDialog(mainFrame);
      this.crearUsuarioDialog.addListener(new CreateAccountController( this));
    	crearUsuarioDialog.setVisible(true);
    }
    
    public void abrirVentanaLogIn(){
    	this.loginDialog = new LoginDialog(mainFrame );
      this.loginDialog.addListenerLogin(new LoginController(this));
      this.loginDialog.addListenerCreateAccount(new LoginToCreateAccountController(this));
    	loginDialog.setVisible(true);
    }
    
    public void abrirVentanaOpcionesUsuario(){
    	this.dialogOpcionesUsuario = new UsuarioOptionsDialog(mainFrame);
      //Aquí pasamos como argumento el mainFrame para que el dialog de cerrar sesión tenga un padre
      this.dialogOpcionesUsuario.addListener(new UsuarioOptionsController(mainFrame, this));
    	dialogOpcionesUsuario.setVisible(true);
    }

	public void cerrarVentanaOpcionesUsuario(){
    	dialogOpcionesUsuario.setVisible(false);
    }
    
    public void cerrarVentanaCrearUsuario() {
    	this.crearUsuarioDialog.setVisible(false);
    }

    public void abrirVentanaEditarUsuario(){
    	this.editProfileDialog = new EditProfileDialog(mainFrame);
    	this.editProfileDialog.addListenerChangeData(new EditProfileController( this));
    	editProfileDialog.setVisible(true);
    } 

    public void cerrarVentanaEditarUsuario(){
    	editProfileDialog.setVisible(false);
    } 

    public void cerrarVentanaLogIn() {
    	this.loginDialog.setVisible(false);
    	refreshIconImage(true);
    	mostrarMenuPrincipal();
    }
    
    public void navegarBotonPerfil() {
    	if(Aplicacion.getInstancia().getUsuarioActual() == null) {
    		abrirVentanaLogIn();
    	}else {
    		abrirVentanaOpcionesUsuario();
    	}
    }
    
    public void refreshIconImage(boolean isLoggedIn) {
    	mainFrame.getMenuPrincipalPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getMySecondHandProductsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getCarritoPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getOutstandingPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getProductosFiltradosPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getSearchInterchangesPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getMakeOfferPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getHistorialPedidosPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	
    	mainFrame.getMenuGestorPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	
    	// Paneles de empleado
    	mainFrame.getMenuEmpleadoPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageProductsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getAddProductsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageOrdersPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getValidationRequestsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageInterchangesPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	
    	// Placeholders
    	mainFrame.getDescuentosPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getConfiguracionPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getPerfilPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getNotificacionesPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    }


    public void abrirVentanaNotificacion(Notificacion n){
    	this.notificacionDialog = new NotificacionDialog(mainFrame, n);
      this.notificacionDialog.setVisible(true);
    } 
}
