package aplicacion; 

import producto.*;
import usuario.*;
import solicitud.*;
import notificacion.*;
import tiempo.DateTimeSimulado;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Aplicacion {

	//Uso de Singleton
	private static Aplicacion instancia;

	private String nombre;
	private ConfiguracionRecomendacion criterioRecomendacion;
	private SistemaPago sistemaPago;
	private SistemaEstadisticas sistemaEstadisticas;
	private GestorSolicitudes gestorSolicitud;
	private Catalogo catalogo;
	private Usuario usuarioActual;	
	private List<Usuario> usuariosRegistrados;

	
	private Aplicacion(String nombre, ConfiguracionRecomendacion criterioRecomendacion, SistemaPago sistemaPago, SistemaEstadisticas sistemaEstadisticas, GestorSolicitudes gestorSolicitud, Catalogo catalogo) {
		this.nombre = nombre;
		this.criterioRecomendacion = criterioRecomendacion; 
		this.sistemaPago = sistemaPago;
		this.sistemaEstadisticas = sistemaEstadisticas;
		this.gestorSolicitud = gestorSolicitud;
		this.catalogo = catalogo;
	}

	public static Aplicacion getInstancia() {
		if (instancia == null) {
			instancia = new Aplicacion();
		}
		return instancia;
	}
	// Getters y setters
	
	public Catalogo getCatalogo() {
		return catalogo;
	}

	// Métodos de inicio y cierre de sesión
	public void crearCuenta(String nombreUsuario, String contraseña) {
	}

	public void iniciarSesion(String nombreUsuario, String contraseña) {
	}

	public void cerrarSesion() {
	}

	public void cambiarContraseña(String nombreUsuario, String contraseñaAntigua, String contraseñaNueva) {
	}

	// Métodos exclusivos del gestor
	public void añadirEmpleado(Empleado empleado) {
	}

	public void eliminarEmpleado(Empleado empleado) {
	}

	// Métodos del Cliente Venta
	public List<LineaProductoVenta> buscarProductosNuevos(String prompt) {
		return new ArrayList<>();
	}

	public void crearPedidoAPartirDeCarrito() {
	}

	public void cancelarPedido(SolicitudPedido pedido, Usuario usuario) {
	}

	// Métodos del Cliente Intercambio
	public List<ProductoSegundaMano> buscarProductoIntercambio(String prompt, Usuario usuario) {
		return new ArrayList<>();
	}

	public void aceptarOferta(Oferta oferta, Usuario usuario) {
	}

	public void rechazarOferta(Oferta oferta, Usuario usuario) {
	}


	// Métodos de recomendación
	public List<Producto> getRecomendacion() {
		return new ArrayList<>();
	}

	// Métodos de Usuarios Gestión

	public List<LineaProductoVenta> buscarProductoAGestionar(int promptId) {
		return new ArrayList<>();
	}

	// Métodos de pagos
	public void gestionarPagoPedido(SolicitudPedido pedido, int numTarjeta, int cvv, int mesCaducidad, int añoCaducidad) {
	}

	public void gestionarPagoValidacion(SolicitudValidacion solicitud, int numTarjeta, int cvv, int mesCaducidad, int añoCaducidad) {
	}


	// Notificación Empleado
	private Notificacion crearNotificacionEmpleado(Solicitud solicitud) {
		return null;
	}


	// Notificación Cliente
	private Notificacion crearNotificacionIntercambio(SolicitudIntercambio solicitud) {
		return null;
	}

	private Notificacion crearNotificacionOferta(Oferta oferta) {
		return null;
	}

	private Notificacion crearNotificacionProducto(LineaProductoVenta producto) {
		return null;
	}

	private Notificacion crearNotificacionValidacion(ProductoSegundaMano producto) {
		return null;
	}

	private Notificacion crearNotificacionPedido(SolicitudPedido solicitud) {
		return null;
	}
	
	// Envío de notificaciones
	
	public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
	}
	
	
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	public void setUsuarioActual(Usuario ususario)
	{
		this.usuarioActual = ususario;
	}
	
	
	public void crearSolicitudIntercambio(Oferta o) {
		String codigo1 = generateToken(8);
		String codigo2 = generateToken(8);
		ClienteRegistrado ofertante = o.getOfertante();
		ClienteRegistrado destinatario = o.getDestinatario();
		
		//Registramos el momento de creacion de las solicitudes
		DateTimeSimulado ahora = new DateTimeSimulado();
		
		//Determinamos los detalles del intercambio
		String lugarIntercambio = "Tienda física";
		TiempoSimulado fechaIntercambio = ;
		DetallesIntercambio detalles = new DetallesIntercambio(fechaIntercambio, lugarIntercambio);

		//Creamos la solicitud de intercambio
		SolicitudIntercambio solicitud = new SolicitudIntercambio(codigo1, codigo2, lugarIntercambio, o);

		//Enviamos una notificación a los usuarios con los datos del intercambio
		String mensajeOfertante = "Su oferta ha sido aceptada por " + o.getDestinatario().getNombreUsuario(); 
		String mensajeDestinatario = "Has aceptado una oferta de " + o.getOfertante().getNombreUsuario(); 
		NotificacionIntercambio notifOfertante = new NotificacionIntercambio(mensajeOfertante, ahora, codigo1, detalles);
		NotificacionIntercambio notifDestinatario = new NotificacionIntercambio(mensajeDestinatario, ahora, codigo2, detalles);
		
		//Enviamos una notificacion al empleado con la solicitud de intercambio
		SolicitudIntercambio s = new SolicitudIntercambio(codigo1, codigo2, lugarIntercambio, o);
		NotificacionEmpleado notifEmpleado = new NotificacionEmpleado("Hay una nueva solicitud de intercambio en la tienda", ahora); 
		notifEmpleado.addSolicitud(solicitud);
		
		
		//Enviamos las notificaciones
		enviarNotificacion(ofertante, notifOfertante);
		enviarNotificacion(destinatario, notifDestinatario);
		
		List<Empleado> lista = this.getEmpleados();
		for(Empleado e : lista) {
			e.anadirNotificacion(notifEmpleado);
		}
		
	}
	
	private String generateToken(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
	
	private List<Empleado> getEmpleados(){
		List<Empleado> retorno = new ArrayList<>();
		for (Usuario u : this.usuariosRegistrados) {
			if (u instanceof Empleado == true) {
				retorno.add((Empleado)u);
			}
		}
		List<Empleado> uList = Collections.unmodifiableList(retorno);
		return uList;
	}
	
}