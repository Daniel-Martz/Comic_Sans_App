package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.notificacion.Notificacion;
import modelo.usuario.*;
import modelo.usuario.Empleado;
import modelo.usuario.Gestor;
import modelo.usuario.Usuario;
import modelo.usuario.Permiso;
import vista.main.MainFrame;
import javax.swing.JOptionPane;
import vista.userPanels.HeaderPanel;
import javax.swing.Timer;

import java.util.Set;
import vista.userPanels.*;
import vista.clienteWindows.*;


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
    private CrearUsuarioWindow crearUsuarioDialog;
    private LoginWindow loginDialog;
    private EditProfileWindow editProfileDialog;
    private FiltrosWindow dialogFiltros;
    private UsuarioOptionsWIndow dialogOpcionesUsuario;
    private EmpleadoOptionsDialog dialogOpcionesEmpleado;
    private ControladorFiltros controladorFiltros;
    private ControladorSearchInterchanges controladorSearchInterchanges;
    private ControladorMakeOffer controladorMakeOffer;
    private ControladorManageAccounts ctrlManageAccounts;
    private NotificacionWindow notificacionDialog;

    /**
     * Constructor del controlador principal. Recibe la referencia al MainFrame y
     * obtiene la instancia del modelo (Aplicacion).
     * @param mainFrame ventana principal de la aplicación, que este controlador gestionará
     */
    public MainController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.modelo    = Aplicacion.getInstancia();
    }

    /**
     * 
     * Inicia el controlador principal: registra listeners, muestra el panel inicial
     * y arranca un timer para refrescar la fecha en las cabeceras.
     */
    public void iniciar() {
        registrarListeners();
        
        // Mostrar panel inicial
        mostrarMenuPrincipal();
        
        // Iniciar un timer para actualizar el tiempo simulado en todas las cabeceras cada segundo
        Timer timer = new Timer(1000, e -> refreshDateGlobal());
        timer.start();
        
        // Hacer visible la ventana
        mainFrame.setVisible(true);
    }
    
    /**
     * Registra los listeners globales de la interfaz y crea los controladores
     * secundarios que necesita la aplicación.
     */
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
        conectarHeaderEmpleado(mainFrame.getDescuentosCategoriaPanel().getHeaderPanel());
        conectarHeaderEmpleado(mainFrame.getCreatePackPanel().getHeaderPanel());
        conectarHeaderEmpleado(mainFrame.getModifyPacksPanel().getHeaderPanel());
        conectarHeaderGestor(mainFrame.getManageAccountsPanel().getHeaderPanel());
        conectarHeaderGestor(mainFrame.getManageStatisticsPanel().getHeaderPanel());
        conectarHeaderGestor(mainFrame.getManageRecommendationsPanel().getHeaderPanel());
        conectarHeaderGestor(mainFrame.getManageTimePanel().getHeaderPanel());
        
        new ControladorManageProducts(mainFrame, this);
        new ControladorAddProducts(mainFrame, this);
        new ControladorManageOrders(mainFrame.getManageOrdersPanel(), mainFrame);
        new ControladorValidationRequests(mainFrame.getValidationRequestsPanel(), mainFrame, this);
        new ControladorManageInterchanges(mainFrame.getManageInterchangesPanel(), mainFrame, this);
        new ControladorCreatePack(mainFrame.getCreatePackPanel(), mainFrame, this);
        new ControladorModifyPacks(mainFrame.getModifyPacksPanel(), mainFrame, this);
        
        this.ctrlManageAccounts = new ControladorManageAccounts(mainFrame.getManageAccountsPanel(), mainFrame);
        // Cargar inicialmente la lista de empleados
        this.ctrlManageAccounts.cargarCuentas();
        
        new ControladorManageStatistics(mainFrame.getManageStatisticsPanel(), mainFrame, this);
        new ControladorManageRecommendations(mainFrame.getManageRecommendationsPanel(), mainFrame, this);
        new ControladorDescuentos(mainFrame.getDescuentosPanel(), mainFrame, this);
        new ControladorDescuentosCategoria(mainFrame.getDescuentosCategoriaPanel(), mainFrame, this);
        
        ControladorManageAccounts ctrlManageAccounts = new ControladorManageAccounts(mainFrame.getManageAccountsPanel(), mainFrame);
        ctrlManageAccounts.cargarCuentas();
        
        new ControladorManageStatistics(mainFrame.getManageStatisticsPanel(), mainFrame, this);
        new ControladorManageTime(mainFrame.getManageTimePanel(), mainFrame, this);
        
        mainFrame.getMenuGestorPanel().addManageAccountsListener(e -> {
            // Asegurarnos de recargar la lista de cuentas (solo empleados) cada vez que se abre el panel
            if (this.ctrlManageAccounts != null) this.ctrlManageAccounts.cargarCuentas();
            navegarA(MainFrame.PANEL_MANAGE_ACCOUNTS);
        });
        
        mainFrame.getMenuGestorPanel().addManageStatisticsListener(e -> {
            navegarA(MainFrame.PANEL_MANAGE_STATISTICS);
        });
        
        mainFrame.getMenuGestorPanel().addManageRecommendationsListener(e -> {
            navegarA(MainFrame.PANEL_MANAGE_RECOMMENDATIONS);
        });
        
        mainFrame.getMenuEmpleadoPanel().addManageProductsListener(e -> {
                navegarA(MainFrame.PANEL_MANAGE_PRODUCTS);
        });

        mainFrame.getMenuGestorPanel().addManageTimeListener(e -> {
            navegarA(MainFrame.PANEL_MANAGE_TIME);
        });
        
        mainFrame.getMenuEmpleadoPanel().addManageOrdersListener(e -> {
            if (verificarPermisoEmpleado(Permiso.PEDIDOS, "Orders management")) {
                ControladorManageOrders ctrl = mainFrame.getManageOrdersPanel().getControlador();
                if (ctrl != null) {
                    ctrl.actualizarPedidos();
                }
                navegarA(MainFrame.PANEL_MANAGE_ORDERS);
            }
        });
        
        mainFrame.getMenuEmpleadoPanel().addValidationRequestsListener(e -> {
            if (verificarPermisoEmpleado(Permiso.VALIDACIONES, "Validation management")) {
                ControladorValidationRequests ctrl = mainFrame.getValidationRequestsPanel().getControlador();
                if (ctrl != null) {
                    ctrl.actualizarSolicitudes();
                }
                navegarA(MainFrame.PANEL_VALIDATION_REQUESTS);
            }
        });
        
        mainFrame.getMenuEmpleadoPanel().addManageInterchangesListener(e -> {
            if (verificarPermisoEmpleado(Permiso.INTERCAMBIOS, "Interchange management")) {
                ControladorManageInterchanges ctrl = mainFrame.getManageInterchangesPanel().getControlador();
                if (ctrl != null) {
                    ctrl.actualizarSolicitudes();
                }
                navegarA(MainFrame.PANEL_MANAGE_INTERCHANGES);
            }
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
                        vista.clienteWindows.VentanaDetallesProductoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoWindow(mainFrame, p);
                        dialog.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            } else if (cmd != null && cmd.startsWith("DESCINFO_")) {
                try {
                    int id = Integer.parseInt(cmd.substring(9));
                    modelo.producto.LineaProductoVenta p = Catalogo.getInstancia().buscarProductoNuevo(id);
                    if (p != null) {
                        vista.clienteWindows.DiscountInfoWindow dialog = new vista.clienteWindows.DiscountInfoWindow(mainFrame, p);
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
     * Configura los handlers comunes para una cabecera en modo empleado.
     *
     * @param header cabecera a configurar
     */
    private void conectarHeaderEmpleado(HeaderPanel header) {
        header.addHomeListener(e -> mostrarMenuPrincipal());
        header.addPerfilListener(e -> navegarBotonPerfil());
        header.addNotificacionesListener(e ->
        { gestionarAccesoNotificaciones();
      });
    }

    /**
     * Configura los handlers comunes para una cabecera en modo gestor.
     *
     * @param header cabecera a configurar
     */
    private void conectarHeaderGestor(HeaderPanel header) {
        header.addHomeListener(e -> mostrarMenuPrincipal());
        header.addPerfilListener(e -> navegarBotonPerfil());
    }

    /**
     * Configura una cabecera "normal" con buscador y filtros.
     *
     * @param header cabecera a configurar
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
     * Configura acciones comunes a todas las cabeceras globales (navegación
     * rápida, notificaciones, etc.).
     *
     * @param header cabecera a configurar
     */
    private void conectarHeaderGlobal(HeaderPanel header) {
        header.addHomeListener(e -> mostrarMenuPrincipal());
        header.addOutstandingListener(e -> mostrarProductosOutstanding());
        header.addDiscountedListener(e -> mostrarProductosDescontados());
        header.addIntercambiosListener(e -> mostrarVentanaOpcionesIntercambio());
        header.addPerfilListener(e -> navegarBotonPerfil());
        header.addNotificacionesListener(e -> 
        { gestionarAccesoNotificaciones();
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
        
        // Configurar el header de Manage Products dinámicamente según el rol
        if (nombrePanel.equals(MainFrame.PANEL_MANAGE_PRODUCTS)) {
            if (modelo.getUsuarioActual() instanceof Gestor) {
                mainFrame.getManageProductsPanel().getHeaderPanel().configurarMenuGestor();
            } else {
                mainFrame.getManageProductsPanel().getHeaderPanel().configurarMenuEmpleado();
            }
        }
        
        if (nombrePanel.equals(MainFrame.PANEL_DESCUENTOS)) {
            if (modelo.getUsuarioActual() instanceof Gestor) {
                mainFrame.getDescuentosPanel().getHeaderPanel().configurarMenuGestor();
            } else {
                mainFrame.getDescuentosPanel().getHeaderPanel().configurarMenuEmpleado();
            }
        }
        
        if (nombrePanel.equals(MainFrame.PANEL_DESCUENTOS_CATEGORIA)) {
            if (modelo.getUsuarioActual() instanceof Gestor) {
                mainFrame.getDescuentosCategoriaPanel().getHeaderPanel().configurarMenuGestor();
            } else {
                mainFrame.getDescuentosCategoriaPanel().getHeaderPanel().configurarMenuEmpleado();
            }
        }

        if (nombrePanel.equals(MainFrame.PANEL_ADD_PRODUCTS)) {
            if (modelo.getUsuarioActual() instanceof Gestor) {
                mainFrame.getAddProductsPanel().getHeaderPanel().configurarMenuGestor();
            } else {
                mainFrame.getAddProductsPanel().getHeaderPanel().configurarMenuEmpleado();
            }
        }

        if (nombrePanel.equals(MainFrame.PANEL_CREATE_PACK)) {
            if (modelo.getUsuarioActual() instanceof Gestor) {
                mainFrame.getCreatePackPanel().getHeaderPanel().configurarMenuGestor();
            } else {
                mainFrame.getCreatePackPanel().getHeaderPanel().configurarMenuEmpleado();
            }
        }
        
        if (nombrePanel.equals(MainFrame.PANEL_MODIFY_PACKS)) {
            if (modelo.getUsuarioActual() instanceof Gestor) {
                mainFrame.getModifyPacksPanel().getHeaderPanel().configurarMenuGestor();
            } else {
                mainFrame.getModifyPacksPanel().getHeaderPanel().configurarMenuEmpleado();
            }
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

    /**
     * Abre la ventana de gestión de categorías.
     */
    public void mostrarManageCategories() {
        ManageCategoriesController mcc = new ManageCategoriesController(mainFrame);
        mcc.mostrarVentana();
    }

    // -------------------------------------------------------
    // Gestión de Ventanas y Flujos de Negocio
    // -------------------------------------------------------

    /**
     * Crea y muestra el dialog de propuestas (intercambios) para el cliente.
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
     * Abre la ventana de propuestas si el usuario actual es cliente registrado.
     */
    public void mostrarMisIntercambios() {
        if (!(modelo.getUsuarioActual() instanceof ClienteRegistrado)) {
            return;
        }
        mostrarVentanaPropuestas();
    }

    /**
     * Muestra la ventana modal de filtros avanzados (la crea si hace falta).
     */
    public void abrirVentanaFiltros() {
        if (this.dialogFiltros == null) {
            // 1. Creamos la vista (JDialog), pasándole el MainFrame como padre
            this.dialogFiltros = new vista.clienteWindows.FiltrosWindow(mainFrame);
            // 2. Creamos su controlador específico solo una vez
            this.controladorFiltros = new ControladorFiltros(this.dialogFiltros);
        }
        
        // 3. Mostramos la ventana (se quedará bloqueada hasta que el usuario la cierre porque es modal)
        this.controladorFiltros.mostrarVentana();
    }

    /**
     * Prepara y muestra el panel de productos filtrados para una categoría.
     *
     * @param categoria identificador de categoría (COMICS, FIGURES, BOARD_GAMES)
     */
    public void mostrarProductosPorCategoria(String categoria) {
        if (this.dialogFiltros == null) {
            this.dialogFiltros = new vista.clienteWindows.FiltrosWindow(mainFrame);
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
     * Muestra el panel de productos filtrados y lo rellena con el texto dado.
     *
     * @param prompt texto de búsqueda
     */
    public void mostrarProductosFiltrados(String prompt) {
        ProductosFiltradosPanel panel = mainFrame.getProductosFiltradosPanel();
        // Creamos un controlador específico para manejar acciones dentro del panel
        ControladorProductosFiltrados controlador = new ControladorProductosFiltrados(panel, this.dialogFiltros);
        controlador.buscarYActualizar(prompt);
        mainFrame.mostrarPanel(MainFrame.PANEL_PRODUCTOS_FILTRADOS);
    }

    /**
     * Muestra los productos destacados (alto rating) en su panel.
     */
    public void mostrarProductosOutstanding() {
        vista.userPanels.OutstandingPanel panel = mainFrame.getOutstandingPanel();
        new ControladorOutstanding(panel);
        mainFrame.mostrarPanel(MainFrame.PANEL_OUTSTANDING);
    }
  
    /**
     * Muestra el panel de productos con descuento (aplica filtros de descuento).
     */
    public void mostrarProductosDescontados() {
        if (this.dialogFiltros == null) {
            this.dialogFiltros = new vista.clienteWindows.FiltrosWindow(mainFrame);
            this.controladorFiltros = new ControladorFiltros(this.dialogFiltros);
        }
        
        this.dialogFiltros.resetFiltros();
        this.dialogFiltros.activarFiltrosDescuento();
        
        mostrarProductosFiltrados("");
    }
    
    /**
     * Verifica que el usuario actual sea un cliente registrado. Si no lo es,
     * muestra un diálogo que propone iniciar sesión o registrarse.
     *
     * @return true si es cliente registrado, false en caso contrario
     */
    private boolean verificarAccesoClienteRegistrado() {
        if (!(modelo.getUsuarioActual() instanceof ClienteRegistrado)) {
            VentanaRegistroRequeridoWindow dialogoRequerido = new VentanaRegistroRequeridoWindow(mainFrame);
            int eleccion = dialogoRequerido.mostrarVentana();

            if (eleccion == VentanaRegistroRequeridoWindow.INICIAR_SESION) {
                abrirVentanaLogIn();
            } else if (eleccion == VentanaRegistroRequeridoWindow.REGISTRARSE) {
                abrirVentanaCrearUsuario();
            }
            return false;
        }
        return true;
    }

    /**
     * Verifica que exista un usuario en sesión. Si no, ofrece iniciar sesión
     * o registrarse.
     *
     * @return true si hay usuario en sesión, false en caso contrario
     */
    private boolean verificarAccesoUsuarioRegistrado() {
        if (modelo.getUsuarioActual() == null) {
            VentanaRegistroRequeridoWindow dialogoRequerido = new VentanaRegistroRequeridoWindow(mainFrame);
            int eleccion = dialogoRequerido.mostrarVentana();

            if (eleccion == VentanaRegistroRequeridoWindow.INICIAR_SESION) {
                abrirVentanaLogIn();
            } else if (eleccion == VentanaRegistroRequeridoWindow.REGISTRARSE) {
                abrirVentanaCrearUsuario();
            }
            return false;
        }
        return true;
    }

    /**
     * Muestra un diálogo con las opciones relacionadas con intercambios y
     * navega según la elección del usuario.
     */
    public void mostrarVentanaOpcionesIntercambio() {
        if (!verificarAccesoClienteRegistrado()) {
            return;
        }

        VentanaInterchangeOptionsWindow v = new VentanaInterchangeOptionsWindow(this.mainFrame);
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
     * Acceso centralizado al carrito: verifica permisos y, si procede,
     * refresca la vista del carrito y navega a ella.
     *
     * @param ctrlCarrito controlador del carrito
     */
    public void gestionarAccesoCarrito(ControladorCarrito ctrlCarrito) {
        if (verificarAccesoClienteRegistrado()) {
            ctrlCarrito.refrescarVista();
            navegarA(MainFrame.PANEL_CARRITO);
        }
    }

    /**
     * Acceso centralizado a las notificaciones: verifica usuario y navega al
     * panel de notificaciones tras refrescarlo.
     */
    public void gestionarAccesoNotificaciones() {
        if (verificarAccesoUsuarioRegistrado()) {
            mainFrame.getNotificacionesPanel().getControladorPrincipal().refrescarNotificaciones();
            navegarA(MainFrame.PANEL_NOTIFICACIONES);
        }
    }

    /**
     * Registra el listener del botón de carrito en múltiples cabeceras para
     * que el acceso sea consistente en toda la aplicación.
     *
     * @param ctrlCarrito controlador del carrito que se usará al pulsar
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
     * Navega al panel con los productos de segunda mano del usuario.
     */
    public void mostrarMisProductosSegundaMano() {
		navegarA(MainFrame.PANEL_MY_SECOND_HAND_PRODUCTS);
	}

    /**
     * Muestra el panel para buscar intercambios, creando o recargando su
     * controlador según sea necesario.
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
     * Navega al panel para confeccionar una oferta, precargando los
     * productos seleccionados.
     *
     * @param preseleccionados conjunto de productos preseleccionados
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
    
    /**
     * Abre el diálogo para crear un nuevo usuario.
     */
    public void abrirVentanaCrearUsuario(){
    	this.crearUsuarioDialog = new CrearUsuarioWindow(mainFrame);
      this.crearUsuarioDialog.addListener(new CreateAccountController( this));
    	crearUsuarioDialog.setVisible(true);
    }
    
    /**
     * Abre el diálogo de inicio de sesión.
     */
    public void abrirVentanaLogIn(){
    	this.loginDialog = new LoginWindow(mainFrame );
      this.loginDialog.addListenerLogin(new LoginController(this));
      this.loginDialog.addListenerCreateAccount(new LoginToCreateAccountController(this));
    	loginDialog.setVisible(true);
    }
    
    /**
     * Abre el diálogo de opciones del usuario según el tipo (cliente/empleado).
     */
    public void abrirVentanaOpcionesUsuario(){
      if(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado){
        this.dialogOpcionesUsuario = new UsuarioOptionsWIndow(mainFrame);
        //Aquí pasamos como argumento el mainFrame para que el dialog de cerrar sesión tenga un padre
        this.dialogOpcionesUsuario.addListener(new UsuarioOptionsController(mainFrame, this));
        dialogOpcionesUsuario.setVisible(true);
      }
      else{
        this.dialogOpcionesEmpleado = new EmpleadoOptionsDialog(mainFrame);
        //Aquí pasamos como argumento el mainFrame para que el dialog de cerrar sesión tenga un padre
        this.dialogOpcionesEmpleado.addListener(new UsuarioOptionsController(mainFrame, this));
        dialogOpcionesEmpleado.setVisible(true);
      }
    }

    /**
     * Cierra el diálogo de opciones del usuario que esté abierto.
     */
    public void cerrarVentanaOpcionesUsuario(){
      if(Aplicacion.getInstancia().getUsuarioActual() instanceof Empleado){
        dialogOpcionesEmpleado.setVisible(false);
      }
      else{
        dialogOpcionesUsuario.setVisible(false);
      }
    }
    
    /**
     * Cierra el diálogo de creación de usuario si está abierto.
     */
    public void cerrarVentanaCrearUsuario() {
    	this.crearUsuarioDialog.setVisible(false);
    }

    /**
     * Abre la ventana para editar los datos del usuario.
     */
    public void abrirVentanaEditarUsuario(){
    	this.editProfileDialog = new EditProfileWindow(mainFrame);
    	this.editProfileDialog.addListenerChangeData(new EditProfileController( this));
    	editProfileDialog.setVisible(true);
    } 

    /**
     * Cierra la ventana de edición de usuario.
     */
    public void cerrarVentanaEditarUsuario(){
    	editProfileDialog.setVisible(false);
    } 

    /**
     * Cierra el diálogo de login, actualiza el icono y vuelve al menú.
     */
    public void cerrarVentanaLogIn() {
    	this.loginDialog.setVisible(false);
    	refreshIconImage(true);
    	mostrarMenuPrincipal();
    }
    
    /**
     * Maneja la acción del botón de perfil: abre login si no hay sesión, o
     * las opciones del usuario si hay sesión.
     */
    public void navegarBotonPerfil() {
    	if(Aplicacion.getInstancia().getUsuarioActual() == null) {
    		abrirVentanaLogIn();
    	}else {
    		abrirVentanaOpcionesUsuario();
    	}
    }
    
    /**
     * Refresca los iconos de estado (login) en todas las cabeceras.
     *
     * @param isLoggedIn true si hay sesión iniciada
     */
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
    	mainFrame.getManageAccountsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageStatisticsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageTimePanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	
    	// Paneles de empleado
    	mainFrame.getMenuEmpleadoPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageProductsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getAddProductsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageOrdersPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getValidationRequestsPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getManageInterchangesPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getDescuentosCategoriaPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
        mainFrame.getCreatePackPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
        mainFrame.getModifyPacksPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	
    	// Placeholders
    	mainFrame.getDescuentosPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getConfiguracionPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getPerfilPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    	mainFrame.getNotificacionesPanel().getHeaderPanel().refreshIconImage(isLoggedIn);
    }

    /**
     * Actualiza la fecha mostrada en todas las cabeceras.
     */
    public void refreshDateGlobal() {
        mainFrame.getMenuPrincipalPanel().getHeaderPanel().updateDate();
        mainFrame.getMySecondHandProductsPanel().getHeaderPanel().updateDate();
        mainFrame.getCarritoPanel().getHeaderPanel().updateDate();
        mainFrame.getOutstandingPanel().getHeaderPanel().updateDate();
        mainFrame.getProductosFiltradosPanel().getHeaderPanel().updateDate();
        mainFrame.getSearchInterchangesPanel().getHeaderPanel().updateDate();
        mainFrame.getMakeOfferPanel().getHeaderPanel().updateDate();
        mainFrame.getHistorialPedidosPanel().getHeaderPanel().updateDate();
        
        mainFrame.getMenuGestorPanel().getHeaderPanel().updateDate();
        mainFrame.getManageAccountsPanel().getHeaderPanel().updateDate();
        mainFrame.getManageStatisticsPanel().getHeaderPanel().updateDate();
        mainFrame.getManageTimePanel().getHeaderPanel().updateDate();
        
        // Paneles de empleado
        mainFrame.getMenuEmpleadoPanel().getHeaderPanel().updateDate();
        mainFrame.getManageProductsPanel().getHeaderPanel().updateDate();
        mainFrame.getAddProductsPanel().getHeaderPanel().updateDate();
        mainFrame.getManageOrdersPanel().getHeaderPanel().updateDate();
        mainFrame.getValidationRequestsPanel().getHeaderPanel().updateDate();
        mainFrame.getManageInterchangesPanel().getHeaderPanel().updateDate();
        mainFrame.getDescuentosCategoriaPanel().getHeaderPanel().updateDate();
        mainFrame.getCreatePackPanel().getHeaderPanel().updateDate();
        mainFrame.getModifyPacksPanel().getHeaderPanel().updateDate();
        
        // Placeholders
        mainFrame.getDescuentosPanel().getHeaderPanel().updateDate();
        mainFrame.getConfiguracionPanel().getHeaderPanel().updateDate();
        mainFrame.getPerfilPanel().getHeaderPanel().updateDate();
        mainFrame.getNotificacionesPanel().getHeaderPanel().updateDate();
    }


    /**
     * Abre la ventana que muestra una notificación concreta.
     *
     * @param n notificación a mostrar
     */
    public void abrirVentanaNotificacion(Notificacion n){
    	this.notificacionDialog = new NotificacionWindow(mainFrame, n, this);
      this.notificacionDialog.setVisible(true);
    } 

    /**
     * Verifica que el usuario actual tenga el permiso requerido (o sea Gestor).
     * Si no lo tiene, muestra un diálogo informativo.
     *
     * @param p permiso requerido
     * @param nombrePermiso texto para mostrar en el diálogo si falta permiso
     * @return true si tiene permiso, false en caso contrario
     */
    private boolean verificarPermisoEmpleado(Permiso p, String nombrePermiso) {
        Usuario u = modelo.getUsuarioActual();
        if (u instanceof Gestor) return true;
        
        if (u instanceof Empleado emp && emp.tienePermiso(p)) return true;

        // Reusable dialog for permission denial (keeps UI consistent)
        vista.clienteWindows.PermissionRequiredWindow dlg = new vista.clienteWindows.PermissionRequiredWindow(mainFrame, nombrePermiso);
        dlg.mostrar();
        return false;
    }
    
    
}
