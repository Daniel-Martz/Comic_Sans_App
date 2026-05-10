package modelo.aplicacion;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import modelo.notificacion.*;
import modelo.producto.*;
import modelo.solicitud.*;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.*;
/**
 * Clase principal que agrupa y gestiona los datos globales de la aplicación.
 * Implementa el patrón singleton para asegurar una única instancia en
 * tiempo de ejecución. Proporciona operaciones para la gestión de usuarios,
 * pedidos, notificaciones y acceso a subsistemas compartidos como el
 * catálogo, el sistema de pago y el gestor de solicitudes.
 *
 */
public class Aplicacion implements Serializable {

  /** La constante serialVersionUID. */
  private static final long serialVersionUID = 1L;
	/**
	 * Instancia única de la aplicación (singleton).
	 */
	private static Aplicacion instancia;

	/** Nombre comercial de la aplicación. */
	private String nombre;

	/** Configuración que controla el sistema de recomendaciones. */
	private ConfiguracionRecomendacion criterioRecomendacion;

	/** Subsistema encargado de procesar pagos. */
	private SistemaPago sistemaPago;

	/** Subsistema encargado de recopilar y proporcionar estadísticas. */
	private SistemaEstadisticas sistemaEstadisticas;

	/** Gestor centralizado de solicitudes (pedidos, intercambios, validaciones). */
	private GestorSolicitudes gestorSolicitud;

	/** Catálogo que contiene y filtra los productos de la tienda. */
	private Catalogo catalogo;

	/** Usuario que ha iniciado sesión; null si no hay sesión activa. */
	private Usuario usuarioActual;

	/**
	 * Colección de todos los usuarios registrados en la aplicación. Incluye
	 * el gestor, empleados y clientes registrados.
	 */
	private List<Usuario> usuariosRegistrados = new ArrayList<>();

	/**
	 * Construye la instancia de {@code Aplicacion} inicializando los
	 * subsistemas que dependen de la aplicación.
	 *
	 *
	 * @param nombre nombre de la aplicación
	 * @param criterioRecomendacion instancia de configuración de recomendaciones
	 * @param sistemaPago subsistema de pago
	 * @param sistemaEstadisticas subsistema de estadísticas
	 * @param gestorSolicitud gestor central de solicitudes
	 * @param catalogo catálogo de productos
	 * @param username nombre de usuario del gestor inicial
	 * @param DNI DNI del gestor inicial
	 * @param password contraseña del gestor inicial
	 */
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

	/**
	 * Devuelve la instancia única de la aplicación (singleton).
	 *
	 * <p>Si todavía no existe, inicializa los subsistemas por defecto y crea
	 * la cuenta del gestor con credenciales por defecto.
	 *
	 * @return la instancia única de {@code Aplicacion}
	 */
	public static Aplicacion getInstancia() {
		if (instancia == null) {
			instancia = new Aplicacion("Comic Sans", ConfiguracionRecomendacion.getInstancia(),
					SistemaPago.getInstancia(), SistemaEstadisticas.getInstancia(), GestorSolicitudes.getInstancia(),
					Catalogo.getInstancia(), "gestor", "123456789A", "123456");
		}
		return instancia;
	}


	// Getters y setters

	/**
	 * Devuelve el catálogo de productos asociado a la aplicación.
	 *
	 * @return el catálogo (no {@code null})
	 */
	public Catalogo getCatalogo() {
		return catalogo;
	}

	// Métodos de inicio y cierre de sesión

	/**
	 * Crea una cuenta nueva de tipo {@link ClienteRegistrado} y la añade a la
	 * lista de usuarios registrados.
	 *
	 * @param nombreUsuario nombre de usuario deseado; no {@code null} ni vacío
	 * @param DNI            DNI del usuario; debe tener longitud 10
	 * @param contraseña     contraseña; longitud mínima 4
	 * @return el cliente registrado recién creado
	 * @throws IllegalArgumentException si alguno de los parámetros no cumple las
	 *                                  validaciones indicadas
	 * @throws IllegalStateException    si ya existe un usuario con el mismo
	 *                                  nombre de usuario
	 */
	public ClienteRegistrado crearCuenta(String nombreUsuario, String DNI, String contraseña, String confirmedContraseña) {
		if (nombreUsuario == null) {
			throw new IllegalArgumentException("Nombre de usuario no puede ser nulo");
		}

		String nombreUser = nombreUsuario.trim();
		if (nombreUser.isEmpty()) {
			throw new IllegalArgumentException("Nombre de usuario inválido");
		}

		if (contraseña == null) {
			throw new IllegalArgumentException("La contraseña no puede ser nula");
		}

		// Validaciones de la contraseña; cada método lanza su propia excepción si falla
		validarContraseñaLength(contraseña);
		validarContraseñaLower(contraseña);
		validarContraseñaUpper(contraseña);
		validarContraseñaNumber(contraseña);
		validarContraseñaSymbol(contraseña);

		if (confirmedContraseña == null || confirmedContraseña.isEmpty()) {
			throw new IllegalArgumentException("Debes confirmar la contraseña.");
		}

		if (!contraseña.equals(confirmedContraseña)) {
			throw new IllegalArgumentException("Las contraseñas no coinciden.");
		}

		if (DNI == null || !dniFormat(DNI)) {
			throw new IllegalArgumentException("El DNI introducido no es válido");
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

  /**
	 * Crea una cuenta nueva de tipo {@link ClienteRegistrado} y la añade a la
	 * lista de usuarios registrados.
	 *
	 * @param nombreUsuario nombre de usuario deseado; no {@code null} ni vacío
	 * @param DNI            DNI del usuario; debe tener longitud 10
	 * @param contraseña     contraseña; longitud mínima 4
	 * @return el cliente registrado recién creado
	 * @throws IllegalArgumentException si alguno de los parámetros no cumple las
	 *                                  validaciones indicadas
	 * @throws IllegalStateException    si ya existe un usuario con el mismo
	 *                                  nombre de usuario
	 */
	public ClienteRegistrado modificarCuenta(String nombreUsuario, String DNI, String contraseñaAntigua, String contraseñaNueva) {
		if (nombreUsuario == null || contraseñaAntigua == null || contraseñaNueva == null || DNI == null) {
			throw new IllegalArgumentException("Ningún parámetro puede ser nulo para modificar la cuenta");
		}

		// Solo permitimos modificar cuentas de clientes registrados mediante este método
		if (!(usuarioActual instanceof ClienteRegistrado)) {
			throw new IllegalStateException("Solo se pueden modificar cuentas de cliente con este método");
		}


		// Verificamos contraseña antigua
		if (!usuarioActual.verificarContraseña(contraseñaAntigua)) {
			throw new IllegalArgumentException("La contraseña antigua no es correcta");
		}

		// Validamos nueva contraseña (cada método lanzará excepción con mensaje propio si falla)
		validarContraseñaLength(contraseñaNueva);
		validarContraseñaLower(contraseñaNueva);
		validarContraseñaUpper(contraseñaNueva);
		validarContraseñaNumber(contraseñaNueva);
		validarContraseñaSymbol(contraseñaNueva);

		// Aplicamos el cambio de contraseña
		usuarioActual.setContraseña(contraseñaAntigua, contraseñaNueva);

		System.out.println("Cuenta actualizada correctamente para: " + nombreUsuario);
		return (ClienteRegistrado) usuarioActual;
	}
	
	/**
	 * Verifica tamaño de contraseña
	 * @param contraseña
	 * @return true o false
	 */
	public void validarContraseñaLength(String contraseña) {
		if (contraseña == null || contraseña.length() < 10) {
			throw new IllegalArgumentException("La contraseña debe tener al menos 10 caracteres");
		}
	}
	
	/**
	 * Verifica que hay minusculas
	 * @param contraseña
	 * @return true o false
	 */
	public void validarContraseñaLower(String contraseña) {
		if (contraseña == null || !contraseña.matches(".*[a-z].*")) {
			throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula");
		}
	}
	
	/**
	 * Verifica que hay mayúsculas
	 * @param contraseña
	 * @return true o false
	 */
	public void validarContraseñaUpper(String contraseña) {
		if (contraseña == null || !contraseña.matches(".*[A-Z].*")) {
			throw new IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula");
		}
	}
	
	/**
	 * Verifica que hay al menos un símbolo (carácter especial)
	 * @param contraseña
	 * @return true o false
	 */
	public void validarContraseñaNumber(String contraseña) {
		if (contraseña == null || !contraseña.matches(".*[0-9].*")) {
			throw new IllegalArgumentException("La contraseña debe contener al menos un dígito");
		}
	}
	
	/**
	 * Verifica que hay al menos un símbolo (carácter especial)
	 * @param contraseña
	 * @return true o false
	 */
	public void validarContraseñaSymbol(String contraseña) {
		if (contraseña == null || !contraseña.matches(".*[^a-zA-Z0-9].*")) {
			throw new IllegalArgumentException("La contraseña debe contener al menos un símbolo (carácter especial)");
		}
	}
	
	/**
	 * Verifica que el dni introducido sea válido
	 * @param dni 
	 * @return true o false
	 */
	public boolean dniFormat(String dni) {
		String dniPattern = "\\d{8}[A-HJ-NP-TV-Z]";
		if (!dni.matches(dniPattern))
			return false;
	
		String letters = "TRWAGMYFPDXBNJZSQVHLCKE";
		int dniNumber = Integer.valueOf(dni.substring(0, 8));
		char letterDNI = letters.charAt(dniNumber % 23);
		if (dni.charAt(8)!=letterDNI) return false;
		return true;
	}

	/**
	 * Devuelve los usuarios registrados.
	 *
	 * @return lista inmodificable con los usuarios registrados en la aplicación
	 */
	public List<Usuario> getUsuariosRegistrados() {
		return Collections.unmodifiableList(this.usuariosRegistrados);
	}

	/**
	 * Devuelve los clientes registrados, esto es, devuelve todos los usuarios
	 * registrados que son instancias de {@link ClienteRegistrado}.
	 *
	 * @return lista inmodificable con los clientes registrados
	 */
	public List<ClienteRegistrado> getClientesRegistrados() {
		List<ClienteRegistrado> clientes = new ArrayList<>();
		for (Usuario u : usuariosRegistrados) {
			if (u instanceof ClienteRegistrado) {
				clientes.add((ClienteRegistrado) u);
			}
		}
		return Collections.unmodifiableList(clientes);
	}

	/**
	 * Añade la cuenta del gestor a la aplicación si los parámetros son válidos
	 * y no existe un usuario con el mismo nombre.
	 *
	 * <p>El método realiza validaciones mínimas y no lanza excepciones en
	 * caso de parámetros inválidos; simplemente no crea la cuenta.
	 *
	 * @param nombreUsuario nombre de usuario del gestor; no {@code null} ni vacío
	 * @param DNI            DNI del gestor; debe tener longitud 10
	 * @param contraseña     contraseña del gestor; longitud mínima 4
	 */
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

	/**
	 * Inicia una sesión para el usuario identificado por {@code nombreUsuario}
	 * si la contraseña proporcionada es correcta.
	 *
	 * @param nombreUsuario nombre de usuario
	 * @param contraseña    contraseña del usuario
	 * @throws IllegalStateException    si ya existe una sesión activa
	 * @throws IllegalArgumentException si {@code nombreUsuario} o {@code contraseña}
	 *                                  son {@code null} o las credenciales son inválidas
	 */
	public void iniciarSesion(String nombreUsuario, String contraseña) {

		if (this.usuarioActual != null) {
			throw new IllegalStateException("Ya hay una sesión activa. Cierra sesión primero.");
		}

		if (nombreUsuario == null || contraseña == null) {
			throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos.");
		}

		for (Usuario u : usuariosRegistrados) {
	        if (u.getNombreUsuario().equals(nombreUsuario)) {
	            if (u.verificarContraseña(contraseña)) {
	                this.usuarioActual = u;
                  System.out.println("Sesión iniciada con éxito para: " + nombreUsuario);
	                return;
	            }else {
	            	throw new IllegalArgumentException("La contraseña introducida no es correcta");
	            }
	        }
	    }
	  throw new IllegalArgumentException("El usuario introducido no existe en la aplicación");
	}

	/**
	 * Cierra la sesión activa y limpia el usuario actual. También restablece
	 * los filtros del catálogo asociados a la sesión.
	 *
	 * @throws IllegalStateException si no hay ninguna sesión activa
	 */
	public void cerrarSesion() {    
		if (this.usuarioActual == null) {
			throw new IllegalStateException("No hay ninguna sesión activa.");
		}

		System.out.println("Sesión cerrada con éxito para: " + this.usuarioActual.getNombreUsuario());

		this.usuarioActual = null;
		catalogo.limpiarFiltros();
	}

	/**
	 * Cambia la contraseña de un usuario existente tras verificar la
	 * contraseña antigua.
	 *
	 * @param nombreUsuario        nombre de usuario cuyo password se cambia
	 * @param contraseñaAntigua    contraseña actual del usuario
	 * @param contraseñaNueva      nueva contraseña (longitud mínima 4)
	 * @throws IllegalArgumentException si alguno de los parámetros es nulo,
	 *                                  si la nueva contraseña es demasiado corta
	 *                                  o si la contraseña antigua es incorrecta
	 */
	public void cambiarContraseña(String nombreUsuario, String contraseñaAntigua, String contraseñaNueva) {

		if (nombreUsuario == null || contraseñaAntigua == null || contraseñaNueva == null) {
			throw new IllegalArgumentException("Los parámetros no pueden ser nulos.");
		}

		if (contraseñaNueva.length() < 4) {
			throw new IllegalArgumentException("La nueva contraseña debe tener al menos 4 caracteres.");
		}

		for (Usuario u : usuariosRegistrados) {
			if (u.getNombreUsuario().equals(nombreUsuario)) {
				if (u.verificarContraseña(contraseñaAntigua)) {
					u.setContraseña(contraseñaAntigua, contraseñaNueva);
					System.out.println("Contraseña cambiada con éxito para: " + nombreUsuario);
					return;
				} else {
					throw new IllegalArgumentException("La contraseña antigua es incorrecta.");
				}
			}
		}
		
		throw new IllegalArgumentException("El usuario no existe.");
	}

	/**
	 * Añade un nuevo empleado a la aplicación. El empleado se crea sin
	 * permisos; estos pueden añadirse posteriormente.
	 *
	 * <p>Solo el gestor puede invocar este método. Se validan los
	 * parámetros y se comprueba que no exista ya un usuario con el mismo
	 * nombre de usuario.
	 *
	 * @param nombreUsuario nombre de usuario del empleado
	 * @param DNI            DNI del empleado; debe tener longitud 10
	 * @param contraseña     contraseña inicial del empleado; longitud mínima 4
	 * @return la instancia de {@link Empleado} creada
	 * @throws IllegalArgumentException si no hay un usuario logueado o si los
	 *                                  parámetros no son válidos
	 * @throws IllegalStateException    si el usuario actual no es el gestor o  
	 *                                  si ya existe un usuario con el mismo nombre
	 */
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

		if (DNI == null || DNI.length() != 9) {
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

	/**
	 * Elimina la cuenta de un empleado del sistema.
	 *
	 * <p>Solo el gestor puede invocar este método. Si {@code empleado} es
	 * {@code null} o no está registrado, el método no realiza ninguna acción.
	 *
	 * @param empleado el empleado a eliminar (puede ser {@code null})
	 * @throws IllegalArgumentException si no hay ningún usuario logueado
	 * @throws IllegalStateException    si el usuario actual no es el gestor
	 */
	public void eliminarUsuario(Usuario usuario) {
		if (this.usuarioActual == null) {
			throw new IllegalArgumentException("No hay ningún usuario logueado.");
		}

		if (!(this.usuarioActual instanceof Gestor)) {
			throw new IllegalStateException("Solo el gestor puede eliminar un usuario.");
		}

		if (usuario == null) {
			return;
		}

		if (usuariosRegistrados.contains(usuario)) {
			usuariosRegistrados.remove(usuario);
			System.out.println("Usuario eliminado del sistema: " + usuario.getNombreUsuario());
		}
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
 
	/**
	 * Buscar productos nuevos (no de segunda mano) en la tienda utilizando
	 * un prompt que permite filtrar por aquellos productos que contengan la
	 * cadena introducida en el prompt, añadiendo también las condiciones
	 * del filtro actual de la aplicación.
	 *
	 * @param prompt texto de búsqueda; puede ser {@code null} o vacío para
	 *               devolver todos los productos según los filtros actuales
	 * @return la lista de productos nuevos que se corresponden con el filtro
	 *         actual y el prompt
	 */
	// Métodos del Cliente Venta
	public List<LineaProductoVenta> buscarProductosNuevos(String prompt) {
		List<LineaProductoVenta> productosBuscados = catalogo.obtenerProductosNuevosFiltrados(prompt);
		Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
		if(usuarioActual instanceof ClienteRegistrado) {
			((ClienteRegistrado)usuarioActual).getInteres().actualizarInteresBusquedaVenta(productosBuscados);
		}
		return productosBuscados;
	}

	/**
	 * Crea un pedido a partir del carrito del cliente actualmente autenticado
	 * y lo registra en el {@link GestorSolicitudes}. Además notifica a los
	 * empleados de la tienda que existe un nuevo pedido.
	 *
	 * @throws IllegalArgumentException si no hay ningún usuario logueado
	 * @throws IllegalStateException    si el usuario actual no es un cliente registrado
	 * @return el pedido creado
	 */
	public SolicitudPedido crearPedidoAPartirDeCarrito() {
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
    //Notificamos a los empleados de la tienda de que hay un nuevo pedido
		NotificacionEmpleado notifEmpleado = new NotificacionEmpleado("Hay un nuevo pedido en la tienda", new DateTimeSimulado());
    notifEmpleado.addSolicitud(pedido);
    List<Empleado> lista = this.getEmpleados();
		for (Empleado e : lista) {
			e.añadirNotificacion(notifEmpleado);
		}
    return pedido;
	}

	/**
	 * Cancela un pedido realizado por el cliente autenticado. Se restablece
	 * el stock de los productos y se elimina el pedido del cliente y del
	 * {@link GestorSolicitudes} si se cumplen las condiciones.
	 *
	 * @param pedido el pedido a cancelar; no puede ser {@code null}
	 * @throws IllegalArgumentException si no hay ningún usuario logueado o si
	 *                                  {@code pedido} es {@code null}
	 * @throws IllegalStateException    si el usuario actual no es un cliente
	 *                                  registrado, si el pedido no pertenece
	 *                                  al cliente o si el pedido no está en
	 *                                  estado {@link EstadoPedido#PENDIENTE_DE_PAGO}
	 */
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

		// Reestablcemos el stock
		for (LineaProductoVenta producto : pedido.getProductosDiferentes().keySet()) {
			int unidades = pedido.getProductosDiferentes().get(producto);
			producto.setStock(unidades + producto.getStock());
		}

		// Eliminamos el pedido tanto en el cliente como en el Gestor de Solicitudes
		cliente.cancelarPedido(pedido);
		GestorSolicitudes.getInstancia().eliminarPedido(pedido);
	}

	/**
	 * Busca productos de segunda mano en la tienda utilizando un prompt que
	 * permite filtrar por aquellos productos que contengan la cadena
	 * introducida en el prompt, añadiendo también las condiciones del
	 * filtro actual de la aplicación.
	 *
	 * @param prompt texto de búsqueda; puede ser {@code null} o vacío
	 * @return lista de productos de segunda mano que cumplen los criterios
	 */
	// Métodos del Cliente Intercambio
	public List<ProductoSegundaMano> buscarProductoIntercambio(String prompt) {
		return catalogo.obtenerProductosIntercambioFiltrados(prompt);
	}

	/**
	 * Devuelve la configuracion de recomendaciones del sistema.
	 *
	 * @return la configuracion de recomendaciones
	 */
	public ConfiguracionRecomendacion getConfiguracionRecomendacion() {
		return criterioRecomendacion;
	}

	/**
	 * Busca un producto nuevo por su identificador.
	 *
	 * <p>Solo usuarios de gestión (gestor o empleado) pueden invocar este
	 * método.
	 *
	 * @param promptId el identificador del producto (debe ser >= 0)
	 * @return la línea de producto a la venta con dicho identificador
	 * @throws IllegalArgumentException si no hay sesión o el id es inválido
	 * @throws IllegalStateException    si el usuario actual no tiene permisos de gestión
	 */
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

	/**
	 * Buscar un producto de segunda mano por su identificador.
	 *
	 * @param promptId el identificador del producto
	 * @return el producto de segunda mano a la venta con dicho identificador
	 * @throws IllegalArgumentException si no hay sesión o el id no es válido
	 * @throws IllegalStateException    si el usuario actual no tiene permisos de gestión
	 */
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

	/**
	 * Busca productos nuevos como usuario de gestión, esto es, como el gestor o el empleado, usando un prompt que permite filtrar por aquellos productos cuyo nombre contenga el string del prompt.
	 *
	 * @param prompt el prompt
	 * @return la lista de productos que se ajustan a los filtros realizados
	 */
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

	/**
	 * Realiza el pago de un pedido llamando al sistema de pago de la aplicación.
	 *
	 * @param pedido      el pedido
	 * @param numTarjeta  número de tarjeta
	 * @param cvv         código CVV de la tarjeta
	 * @param caducidad   fecha de caducidad (simulada)
	 * @throws IllegalStateException    si no hay sesión o el usuario no es un cliente registrado
	 * @throws IllegalArgumentException si algún parámetro es {@code null} o el pedido no pertenece al cliente
	 */
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

	/**
	 * Realiza el pago de una validación (por ejemplo, pago asociado a una
	 * solicitud de validación) mediante el subsistema de pago.
	 *
	 * @param solicitud la solicitud de validación a pagar
	 * @param numTarjeta número de tarjeta
	 * @param cvv código CVV
	 * @param caducidad fecha de caducidad (simulada)
	 * @throws IllegalStateException    si no hay sesión o el usuario no es un cliente registrado
	 * @throws IllegalArgumentException si algún parámetro es {@code null}
	 */
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

	/**
	 * Envía una notificación a un {@link Usuario}. Dependiendo del tipo de
	 * usuario, la notificación se añadirá a su buzón correspondiente.
	 *
	 * @param usuario      destinatario de la notificación (no {@code null})
	 * @param notificacion notificación a enviar (no {@code null})
	 * @throws IllegalArgumentException si {@code usuario} o {@code notificacion} es {@code null}
	 */
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

	/**
	 * Devuelve el usuario actual.
	 *
	 * @return el usuario actual, o {@code null} si no hay sesión iniciada
	 */
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	/**
	 * Establece el usuario actual.
	 *
	 * @param ususario el nuevo usuario actual (puede ser {@code null} para cerrar sesión programáticamente)
	 */
	public void setUsuarioActual(Usuario ususario) {
		this.usuarioActual = ususario;
	}

	/**
	 * Crea y registra una solicitud de intercambio a partir de una
	 * {@link Oferta}. Genera códigos de intercambio, crea las
	 * notificaciones para ofertante, destinatario y empleados y añade la
	 * solicitud al {@link GestorSolicitudes}.
	 *
	 * @param o la oferta aceptada que da lugar a la solicitud de intercambio
	 * @throws IllegalArgumentException si {@code o} es {@code null} (implícito al usar sus métodos)
	 */
	public void crearSolicitudIntercambio(Oferta o) {
		String codigo1 = generateToken(8);
		String codigo2 = generateToken(8);
		ClienteRegistrado ofertante = o.getOfertante();
		ClienteRegistrado destinatario = o.getDestinatario();

		// Registramos el momento de creacion de las solicitudes
		DateTimeSimulado ahora = new DateTimeSimulado();

		// Determinamos los detalles del intercambio
		String lugarIntercambio = "Tienda física";
		DateTimeSimulado fechaIntercambio = DateTimeSimulado.DateTimeDiasDespues(7);
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
		GestorSolicitudes.getInstancia().añadirSolicitudIntercambio(solicitud);

		// Enviamos las notificaciones
		enviarNotificacion(ofertante, notifOfertante);
		enviarNotificacion(destinatario, notifDestinatario);

		List<Empleado> lista = this.getEmpleados();
		for (Empleado e : lista) {
			e.añadirNotificacion(notifEmpleado);
		}

	}

	/**
	 * Generate token.
	 *
	 * @param length el length
	 * @return el string
	 */
	private String generateToken(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	/**
	 * Devuelve el empleados.
	 *
	 * @return el empleados
	 */
	public List<Empleado> getEmpleados() {
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
	 * Devuelve el sistema estadisticas.
	 *
	 * @return the sistemaEstadisticas
	 */
	public SistemaEstadisticas getSistemaEstadisticas() {
		return sistemaEstadisticas;
	}

	/**
	 * Devuelve el nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve el criterio recomendacion.
	 *
	 * @return the criterioRecomendacion
	 */
	public ConfiguracionRecomendacion getCriterioRecomendacion() {
		return criterioRecomendacion;
	}

	/**
	 * Devuelve el sistema pago.
	 *
	 * @return the sistemaPago
	 */
	public SistemaPago getSistemaPago() {
		return sistemaPago;
	}

	/**
	 * Devuelve el gestor solicitud.
	 *
	 * @return the gestorSolicitud
	 */
	public GestorSolicitudes getGestorSolicitud() {
		return gestorSolicitud;
	}
	
	
  /**
   * Escribe el objeto aplicacion en un fichero siguiendo la forma de guardado de Serializable
   *
   * @param oos el ObjectOutputStream donde se guardará la aplicación
   * @throws IOException Señala que la I/O exception ha ocurrido
   */
  private void writeObject(ObjectOutputStream oos) throws IOException {
      oos.defaultWriteObject();
  }
  
  /**
   * Lee el objeto aplicacion de un fichero siguiendo la forma de guardado de Serializable
   *
   * @param ois el ObjectInputStream donde se leerá la aplicación
   * @throws IOException Señala que la I/O exception ha ocurrido
   * @throws ClassNotFoundException señala que ha habido un error encontrando la clase
   */
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
      ois.defaultReadObject();
      instancia = this;
  }

  /**
   * Guarda el estado de la aplicacion. Este método es a través del cual clases externas podrán guardar la aplicación.
   *
   * @param file el archivo donde se guardará la aplicación
   * @throws IOException Señala que la I/O exception ha ocurrido
   */
  public void guardarEstadoAplicacion(String file) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
        out.writeObject(this);
    }
}

  /**
   * Carga el estado de la aplicacion. Este método es a través del cual clases externas podrán cargar la aplicación.
   *
   * @param file el archivo del que se leerá la aplicación.
   */
  public void cargarEstadoAplicacion(String file) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
        instancia = (Aplicacion) in.readObject();
    } catch (FileNotFoundException e) {
        instancia = Aplicacion.getInstancia();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
        instancia = Aplicacion.getInstancia();
    }
  }
  
  /**
   * Escribe los datos de la aplicación
   */
  @Override
  public String toString() {
	return "\n\nAplicacion: nombre de la aplicación = " + nombre + " \n\nCriterio de recomendacion de la aplicación = " + criterioRecomendacion + "\n\nSistema de pago = "
			+  sistemaEstadisticas + "\n\nGestor de solicitudes de la aplicación = " + gestorSolicitud
			+ "\n\nCatalogo=" + catalogo + "\n\nUsuarioActual=" + usuarioActual + "\n\nUsuariosRegistrados="
			+ usuariosRegistrados + "]";
  }

  /**
   * Añade una solictud de validacion
 * @param v la solicitud
 */
public void añadirSolicitudValidacion(SolicitudValidacion v){
    this.gestorSolicitud.añadirSolicitudValidacion(v);
    //Notificamos a los empleados de la tienda de que hay un nuevo validacion por realizar
		NotificacionEmpleado notifEmpleado = new NotificacionEmpleado("Hay una nueva validación pendiente en la tienda", new DateTimeSimulado());
    notifEmpleado.addSolicitud(v);
    List<Empleado> lista = this.getEmpleados();
		for (Empleado e : lista) {
			e.añadirNotificacion(notifEmpleado);
		}
  }
}
