package aplicacion;

import producto.*;

import usuario.*;
import solicitud.*;
import tiempo.DateTimeSimulado;
import notificacion.*;
import categoria.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

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
			SistemaEstadisticas sistemaEstadisticas, GestorSolicitudes gestorSolicitud, Catalogo catalogo,
			String username, String DNI, String password) {
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
			instancia = new Aplicacion("Comic Sans", ConfiguracionRecomendacion.getInstancia(),
					SistemaPago.getInstancia(), SistemaEstadisticas.getInstancia(), GestorSolicitudes.getInstancia(),
					Catalogo.getInstancia(), "gestor", "123456789A", "123456");
		}
		return instancia;
	}
	// Getters y setters

	public Catalogo getCatalogo() {
		return catalogo;
	}

	// Métodos de inicio y cierre de sesión
	public ClienteRegistrado crearCuenta(String nombreUsuario, String DNI, String contraseña) {
		String nombreUser = nombreUsuario.trim();// Hago esto para no guardar nombres iguales pero con espacios (en la
													// vida real se ponen "_"
		if (nombreUsuario == null || nombreUser.isEmpty()) {
      throw new IllegalArgumentException("Nombre de usuario inválido al crear al empleado");
		}

		if (contraseña == null || contraseña.length() < 4) {
      throw new IllegalArgumentException("Contraseña inválida al crear al empleado");
		}

		if (DNI == null || DNI.length() != 10) {
      throw new IllegalArgumentException("DNI inválido al crear al empleado");
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUser)) {
        throw new IllegalStateException("Ya existe un usuario con el nombre de usuario introducido");
			}
		}

		ClienteRegistrado nuevoUsuario = new ClienteRegistrado(nombreUser, DNI, contraseña);
		usuariosRegistrados.add(nuevoUsuario);

		System.out.println("Nueva cuenta de cliente creada con éxito para: " + nombreUser);
		return nuevoUsuario;
	}

	public List<Usuario> getUsuariosRegistrados() {
		return Collections.unmodifiableList(this.usuariosRegistrados);
	}
	
	public List<ClienteRegistrado> getClientesRegistrados() {
	    List<ClienteRegistrado> clientes = new ArrayList<>();
	    for (Usuario u : usuariosRegistrados) {
	        if (u instanceof ClienteRegistrado) {
	            clientes.add((ClienteRegistrado) u);
	        }
	    }
	    return Collections.unmodifiableList(clientes);
	}
	

	public void añadirGestor(String nombreUsuario, String DNI, String contraseña) {
		String nombreUser = nombreUsuario.trim();// Hago esto para no guardar nombres iguales pero con espacios (en la
													// vida real se ponen "_"
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
	public Empleado añadirEmpleado(String nombreUsuario, String DNI, String contraseña) {
		// Comprobamos que el usuario actual sea el gestor
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof Gestor)) {
			throw new IllegalStateException("Solo el gestor puede añadir un empleado.");
		}

		String nombreUser = nombreUsuario.trim();

		if (nombreUsuario == null || nombreUser.isEmpty()) {
      throw new IllegalArgumentException("Nombre de usuario inválido al crear al empleado");
		}

		if (contraseña == null || contraseña.length() < 4) {
      throw new IllegalArgumentException("Contraseña inválida al crear al empleado");
		}

		if (DNI == null || DNI.length() != 10) {
      throw new IllegalArgumentException("DNI inválido al crear al empleado");
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUser)) {
        throw new IllegalStateException("Ya existe un usuario con el nombre de usuario introducido");
			}
		}

		Empleado nuevoUsuario = new Empleado(nombreUser, DNI, contraseña);
		usuariosRegistrados.add(nuevoUsuario);

		System.out.println("Nueva cuenta creada con éxito para: " + nombreUser);
		return nuevoUsuario;
	}

	public void eliminarEmpleado(Empleado empleado) {
		// Comprobamos que el usuario actual sea el gestor
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof Gestor)) {
			throw new IllegalStateException("Solo el gestor puede eliminar un empleado.");
		}

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
		// Comprobamos que el usuario actual sea un cliente registrado
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo un cliente registrado puede cancelar un pedido.");
		}
		return catalogo.obtenerProductosNuevosFiltrados(prompt);
	}

	public void crearPedidoAPartirDeCarrito() {
		// Comprobamos que el usuario actual sea un cliente registrado
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo un cliente registrado puede crear un pedido.");
		}

		ClienteRegistrado cliente = (ClienteRegistrado) this.usuarioActual;
		SolicitudPedido pedido = cliente.realizarPedido();
		GestorSolicitudes.getInstancia().añadirPedido(pedido);
	}

	public void cancelarPedido(SolicitudPedido pedido) {
		// Comprobamos que el usuario actual sea un cliente registrado
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo un cliente registrado puede cancelar un pedido.");
		}
		// Comprobamos que le hemos pasado un pedido.
		if (pedido == null) {
			throw new IllegalArgumentException("No hay ningún pedido.");
		}

		ClienteRegistrado cliente = (ClienteRegistrado) this.usuarioActual;
		if (!(cliente.getPedidos().contains(pedido))) {
			throw new IllegalStateException("El cliente no ha realizado ese pedido.");
		}

		// Comprobamos que el pedido esté en estado pendiente de pago
		if (pedido.getEstado() != EstadoPedido.PENDIENTE_DE_PAGO) {
			throw new IllegalStateException("Solo se pueden cancelar pedidos en estado pendiente de pago.");
		}

		// Reestablcemes el stock
		for (LineaProductoVenta producto : pedido.getProductosDiferentes().keySet()) {
			int unidades = pedido.getProductosDiferentes().get(producto);
			producto.setStock(unidades + producto.getStock());
		}

		// Eliminamos el pedido tanto en el cliente como en el Gestor de Solicitudes
		cliente.cancelarPedido(pedido);
		GestorSolicitudes.getInstancia().eliminarPedido(pedido);
	}

	// Métodos del Cliente Intercambio
	public List<ProductoSegundaMano> buscarProductoIntercambio(String prompt) {
		// Comprobamos que el usuario actual sea un cliente registrado
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo un cliente registrado puede cancelar un pedido.");
		}
		return catalogo.obtenerProductosIntercambioFiltrados(prompt);
	}
	
	public ConfiguracionRecomendacion getConfiguracionRecomendacion() {
		return criterioRecomendacion;
	}

	// Métodos de Usuarios Gestión
	public LineaProductoVenta buscarProductoNuevo(int promptId) {
		// Comprobamos que el usuario actual sea del tipo UsuarioGestion
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof Gestor) && !(this.usuarioActual instanceof Empleado)) {
			throw new IllegalStateException("Solo el gestor o empleado puede buscar un producto por ID.");
		}

		if (promptId < 0) {
			throw new IllegalArgumentException("No se ha pasado un ID valido");
		}
		return Catalogo.getInstancia().buscarProductoNuevo(promptId);
	}

	public ProductoSegundaMano buscarProductoIntercambio(int promptId) {
		// Comprobamos que el usuario actual sea del tipo UsuarioGestion
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof Gestor) && !(this.usuarioActual instanceof Empleado)) {
			throw new IllegalStateException("Solo el gestor o empleado puede buscar un producto por ID.");
		}

		if (promptId < 0) {
			throw new IllegalArgumentException("No se ha pasado un ID valido");
		}
		return Catalogo.getInstancia().buscarProductoIntercambio(promptId);
	}

	public List<LineaProductoVenta> buscarProductosNuevosGestion(String prompt) {
		// Comprobamos que el usuario actual sea del tipo UsuarioGestion
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof Gestor) && !(this.usuarioActual instanceof Empleado)) {
			throw new IllegalStateException("Solo el gestor o empleado pueden buscar productos.");
		}
		
		return Catalogo.getInstancia().obtenerProductosNuevosGestion(prompt);
	}

	// Métodos de pagos
	public void gestionarPagoPedido(SolicitudPedido pedido, String numTarjeta, String cvv, DateTimeSimulado caducidad) {
		if (this.usuarioActual == null) {
			throw new IllegalStateException("No hay ningún usuario logueado.");
		}
		if (!(this.usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo un cliente registrado puede pagar un pedido.");
		}
		if (pedido == null || numTarjeta == null || cvv == null || caducidad == null) {
			throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
		}

		ClienteRegistrado cliente = (ClienteRegistrado) this.usuarioActual;

		if (!cliente.getPedidos().contains(pedido)) {
			throw new IllegalStateException("El pedido no pertenece al cliente actual.");
		}

		cliente.pagarPedido(pedido, numTarjeta, cvv, caducidad);
	}

	public void gestionarPagoValidacion(SolicitudValidacion solicitud, String numTarjeta, String cvv,
			DateTimeSimulado caducidad) {
		if (this.usuarioActual == null) {
			throw new IllegalStateException("No hay ningún usuario logueado.");
		}
		if (!(this.usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo un cliente registrado puede pagar una validación.");
		}
		if (solicitud == null || numTarjeta == null || cvv == null || caducidad == null) {
			throw new IllegalArgumentException("Ningún parámetro puede ser nulo.");
		}

		ClienteRegistrado cliente = (ClienteRegistrado) this.usuarioActual;

		cliente.pagarValidacion(solicitud, numTarjeta, cvv, caducidad);
	}

	// Notificación Empleado
	// Metodo para cuando se recibe una nueva solicitud que validar
	private Notificacion crearNotificacionSolicitudEmpleado(Solicitud solicitud) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		NotificacionEmpleado notif = new NotificacionEmpleado("Nueva solicitud pendiente de gestión", ahora);
		notif.addSolicitud(solicitud);
		return notif;
	}

	// Notificación Cliente
	// He añadido el string codigo porque se tienen que crear una noti para el
	// ofertante y el destinatario
	private Notificacion crearNotificacionIntercambio(SolicitudIntercambio solicitud, String codigo) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		DetallesIntercambio detalles = solicitud.getInformacionIntercambio();
		return new NotificacionIntercambio("Tu intercambio ha sido confirmado. Tu código de recogida es: " + codigo,
				ahora, codigo, detalles);
	}

	// Metodo para cuando se recibe una oferta
	private Notificacion crearNotificacionOferta(Oferta oferta) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		NotificacionOferta notif = new NotificacionOferta(
				"Has recibido una nueva oferta de " + oferta.getOfertante().getNombreUsuario(), ahora, oferta);
		return notif;
	}

	// Metodo para cuando se añade un producto nuevo
	private Notificacion crearNotificacionNuevoProducto(LineaProductoVenta producto) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		NotificacionProducto notif = new NotificacionProducto("Nuevo producto disponible: " + producto.getNombre(),
				ahora);
		notif.addProducto(producto);
		return notif;
	}

	// Metodo para cuando se rebaja un producto
	private Notificacion crearNotificacionDescuentoProducto(LineaProductoVenta producto) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		NotificacionProducto notif = new NotificacionProducto(
				"¡Nuevo descuento disponible en: " + producto.getNombre() + "!", ahora);
		notif.addProducto(producto);
		return notif;
	}

	// Metodo para cuando se rebaja una categoria
	private Notificacion crearNotificacionDescuentoCategoria(Categoria categoria) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		NotificacionProducto notif = new NotificacionProducto(
				"¡Nueva promoción en la categoría: " + categoria.getNombre() + "!", ahora);
		for (LineaProductoVenta p : categoria.obtenerProductosCategoria()) {
			notif.addProducto(p);
		}
		return notif;
	}

	// Metodo para cuando se recomienda un producto
	private Notificacion crearNotificacionRecomendacion(LineaProductoVenta producto) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		NotificacionProducto notif = new NotificacionProducto("Te recomendamos este producto: " + producto.getNombre(),
				ahora);
		notif.addProducto(producto);
		return notif;
	}

	// Metodo para cuando se ha validado un producto
	private Notificacion crearNotificacionValidacion(ProductoSegundaMano producto) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		return new NotificacionValidacion("Tu producto '" + producto.getNombre() + "' ha sido validado", ahora,
				producto);
	}

	// Metodo para cuando se ha pagado correctamente
	private Notificacion crearNotificacionPagoConfirmado(SolicitudPedido solicitud) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		return new NotificacionPedido("Tu pago ha sido confirmado con éxito. Tu pedido está siendo preparado.", ahora,
				solicitud);
	}

	// Metodo para cuando se ha rechazado un pago
	private Notificacion crearNotificacionPagoRechazado(SolicitudPedido solicitud) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		return new NotificacionPedido(
				"Tu pago ha sido rechazado. Por favor, revisa los datos de tu tarjeta e inténtalo de nuevo.", ahora,
				solicitud);
	}

	// Metodo para cuando se actualiza el estado del pedido
	private Notificacion crearNotificacionEstadoPedido(SolicitudPedido solicitud) {
		DateTimeSimulado ahora = new DateTimeSimulado();
		String mensaje;
		switch (solicitud.getEstado()) {
		case PENDIENTE_DE_PAGO:
			mensaje = "Tu pedido está pendiente de pago. Recuerda completarlo antes de que caduque.";
			break;
		case LISTO_PARA_RECOGER:
			mensaje = "Tu pedido está listo para recoger. Preséntate en la tienda con tu DNI y el código de recogida.";
			break;
		case RECOGIDO:
			mensaje = "Tu pedido ha sido recogido. ¡Gracias por tu compra!";
			break;
		default:
			mensaje = "Tu pedido ha sido actualizado.";
			break;
		}
		return new NotificacionPedido(mensaje, ahora, solicitud);
	}

	// Envío de notificaciones
	public void enviarNotificacion(Usuario usuario, Notificacion notificacion) {
		if (usuario == null || notificacion == null) {
			throw new IllegalArgumentException("La solicitud no es valida");
		}

		if (usuario instanceof ClienteRegistrado) {
			ClienteRegistrado cliente = (ClienteRegistrado) usuario;
			NotificacionCliente notificacionCliente = (NotificacionCliente) notificacion;
			cliente.anadirNotificacion(notificacionCliente);
		}
		if (usuario instanceof Empleado) {
			Empleado empleado = (Empleado) usuario;
			NotificacionEmpleado notificacionEmpleado = (NotificacionEmpleado) notificacion;
			empleado.añadirNotificacion(notificacionEmpleado);
		}
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

	/**
	 * @return the sistemaEstadisticas
	 */
	public SistemaEstadisticas getSistemaEstadisticas() {
		return sistemaEstadisticas;
	}

}
