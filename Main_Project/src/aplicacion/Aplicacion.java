package aplicacion;

import producto.*;

import usuario.*;
import solicitud.*;
import notificacion.*;

import java.security.SecureRandom;
import java.util.*;
import tiempo.DateTimeSimulado;

public class Aplicacion {

	// Uso de Singleton
	private static Aplicacion instancia;

	private String nombre;
	private ConfiguracionRecomendacion criterioRecomendacion;
	private SistemaPago sistemaPago;
	private SistemaEstadisticas sistemaEstadisticas;
	private GestorSolicitudes gestorSolicitud;
	private Catalogo catalogo;
	private Usuario usuarioActual;
	private List<Usuario> usuariosRegistrados = new ArrayList<>();

	private Aplicacion(String nombre, ConfiguracionRecomendacion criterioRecomendacion, SistemaPago sistemaPago,
			SistemaEstadisticas sistemaEstadisticas, GestorSolicitudes gestorSolicitud, Catalogo catalogo, String username, String DNI, String password) {
		this.nombre = nombre;
		this.criterioRecomendacion = criterioRecomendacion;
		this.sistemaPago = sistemaPago;
		this.sistemaEstadisticas = sistemaEstadisticas;
		this.gestorSolicitud = gestorSolicitud;
		this.catalogo = catalogo;
		añadirGestor(username, DNI, password);
	}

	public static Aplicacion getInstancia() {
		if (instancia == null) {
			instancia = new Aplicacion("Comic Sans", ConfiguracionRecomendacion.getInstancia(), SistemaPago.getInstancia(),
					SistemaEstadisticas.getInstancia(), GestorSolicitudes.getInstancia(), Catalogo.getInstancia(), "gestor", "123456789A", "123456");
		}
		return instancia;
	}
	// Getters y setters
	
	public Catalogo getCatalogo() {
		return catalogo;
	}

	// Métodos de inicio y cierre de sesión
	public void crearCuenta(String nombreUsuario, String DNI, String contraseña) {
		String nombreUser = nombreUsuario.trim();//Hago esto para no guardar nombres iguales pero con espacios (en la vida real se ponen "_"
		if (nombreUsuario == null || nombreUser.isEmpty()) {
			return;
		}

		if (contraseña == null || contraseña.length() < 4) {
			return;
		}

		if (DNI == null || DNI.length() != 10) {
			return;
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUser)) {
				return;
			}
		}

		Usuario nuevoUsuario = new Cliente(nombreUser, DNI, contraseña);
		usuariosRegistrados.add(nuevoUsuario);

		System.out.println("Nueva cuenta de cliente creada con éxito para: " + nombreUser);
		return;
	}
	
	public void añadirGestor(String nombreUsuario, String DNI, String contraseña) {
		String nombreUser = nombreUsuario.trim();//Hago esto para no guardar nombres iguales pero con espacios (en la vida real se ponen "_"
		if (nombreUsuario == null || nombreUser.isEmpty()) {
			return;
		}

		if (contraseña == null || contraseña.length() < 4) {
			return;
		}

		if (DNI == null || DNI.length() != 10) {
			return;
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUser)) {
				return;
			}
		}

		Usuario nuevoUsuario = new Gestor(nombreUser, DNI, contraseña);
		usuariosRegistrados.add(nuevoUsuario);

		System.out.println("Nueva cuenta de gestor creada con éxito para: " + nombreUser);
		return;
	}

	public void iniciarSesion(String nombreUsuario, String contraseña) {
		if (this.usuarioActual != null) {
			return;
		}

		if (nombreUsuario == null || contraseña == null) {
			return;
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUsuario) && u.verificarContraseña(contraseña)) {
				this.usuarioActual = u;
				System.out.println("Sesión iniciada con éxito. Bienvenido, " + nombreUsuario);
				return;
			}
		}

		System.out.println("Error: Usuario o contraseña incorrectos.");
	}

	public void cerrarSesion() {
		if (this.usuarioActual == null) {
			return;
		}

		System.out.println("Sesión cerrada con éxito para: " + this.usuarioActual.getNombreUsuario());

		this.usuarioActual = null;
	}

	public void cambiarContraseña(String nombreUsuario, String contraseñaAntigua, String contraseñaNueva) {

		if (nombreUsuario == null || contraseñaAntigua == null || contraseñaNueva == null) {
			return;
		}

		if (contraseñaNueva.length() < 4) {
			return;
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUsuario)) {
				if (u.verificarContraseña(contraseñaAntigua)) {
					u.setContraseña(contraseñaAntigua, contraseñaNueva);
					System.out.println("Contraseña cambiada con éxito para: " + nombreUsuario);
					return;
				} else {
					return;
				}
			}
		}
	}

	// Métodos exclusivos del gestor
	public void añadirEmpleado(String nombreUsuario, String DNI, String contraseña) {
		String nombreUser = nombreUsuario.trim();//Hago esto para no guardar nombres iguales pero con espacios (en la vida real se ponen "_"
		if (nombreUsuario == null || nombreUser.isEmpty()) {
			return;
		}

		if (contraseña == null || contraseña.length() < 4) {
			return;
		}

		if (DNI == null || DNI.length() != 10) {
			return;
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUser)) {
				return;
			}
		}

		Usuario nuevoUsuario = new Empleado(nombreUser, DNI, contraseña);
		usuariosRegistrados.add(nuevoUsuario);

		System.out.println("Nueva cuenta creada con éxito para: " + nombreUser);
		return;
	}

	public void eliminarEmpleado(Empleado empleado) {
		if (empleado == null) {
			return;
		}

		if (usuariosRegistrados.contains(empleado)) {
			usuariosRegistrados.remove(empleado);
			System.out.println("Empleado eliminado del sistema: " + empleado.getNombreUsuario());
		}
		return;
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
	public void gestionarPagoPedido(SolicitudPedido pedido, int numTarjeta, int cvv, int mesCaducidad,
			int añoCaducidad) {
	}

	public void gestionarPagoValidacion(SolicitudValidacion solicitud, int numTarjeta, int cvv, int mesCaducidad,
			int añoCaducidad) {
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

	public void enviarNotificacion(Usuario usuario, NotificacionCliente notificacion) {
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(Usuario ususario) {
		this.usuarioActual = ususario;
	}

	public void crearSolicitudIntercambio(Oferta o) {
		String codigo1 = generateToken(8);
		String codigo2 = generateToken(8);
		ClienteRegistrado ofertante = o.getOfertante();
		ClienteRegistrado destinatario = o.getDestinatario();

		// Registramos el momento de creacion de las solicitudes
		DateTimeSimulado ahora = new DateTimeSimulado();

		// Determinamos los detalles del intercambio
		String lugarIntercambio = "Tienda física";
		DateTimeSimulado fechaIntercambio = new DateTimeSimulado();
		DetallesIntercambio detalles = new DetallesIntercambio(fechaIntercambio, lugarIntercambio);

		// Creamos la solicitud de intercambio
		SolicitudIntercambio solicitud = new SolicitudIntercambio(codigo1, codigo2, lugarIntercambio, o);

		// Enviamos una notificación a los usuarios con los datos del intercambio
		String mensajeOfertante = "Su oferta ha sido aceptada por " + o.getDestinatario().getNombreUsuario();
		String mensajeDestinatario = "Has aceptado una oferta de " + o.getOfertante().getNombreUsuario();
		NotificacionIntercambio notifOfertante = new NotificacionIntercambio(mensajeOfertante, ahora, codigo1,
				detalles);
		NotificacionIntercambio notifDestinatario = new NotificacionIntercambio(mensajeDestinatario, ahora, codigo2,
				detalles);

		// Enviamos una notificacion al empleado con la solicitud de intercambio
		NotificacionEmpleado notifEmpleado = new NotificacionEmpleado(
				"Hay una nueva solicitud de intercambio en la tienda", ahora);
		notifEmpleado.addSolicitud(solicitud);
		this.gestorSolicitud.añadirSolicitudIntercambio(solicitud);

		// Enviamos las notificaciones
		enviarNotificacion(ofertante, notifOfertante);
		enviarNotificacion(destinatario, notifDestinatario);

		List<Empleado> lista = this.getEmpleados();
		for (Empleado e : lista) {
			e.añadirNotificacion(notifEmpleado);
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

	private List<Empleado> getEmpleados() {
		List<Empleado> retorno = new ArrayList<>();
		for (Usuario u : this.usuariosRegistrados) {
			if (u instanceof Empleado == true) {
				retorno.add((Empleado) u);
			}
		}
		List<Empleado> uList = Collections.unmodifiableList(retorno);
		return uList;
	}

}