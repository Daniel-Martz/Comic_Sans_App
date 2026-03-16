package aplicacion;

import java.util.*;
import producto.*;
import usuario.*;
import solicitud.*;
import notificacion.*;


public class Aplicacion {
	private String nombre;
	private ConfiguracionRecomendacion criterioRecomendacion;
	private SistemaPago sistemaPago;
	private SistemaEstadisticas sistemaEstadisticas; 
	
	public Aplicacion(String nombre, ConfiguracionRecomendacion criterioRecomendacion, SistemaPago sistemaPago, SistemaEstadisticas sistemaEstadisticas) {
		this.nombre = nombre;
		this.criterioRecomendacion = criterioRecomendacion; 
		this.sistemaPago = sistemaPago;
		this.sistemaEstadisticas = sistemaEstadisticas;
	}

	// Métodos de inicio y cierre de sesión
	public boolean crearCuenta(String nombreUsuario, String contraseña) {

	}

	public boolean iniciarSescion(String nombreUsuario, String contraseña) {

	}

	public boolean cerrarSesion() {

	}

	public boolean cambiarContraseña(String nombreUsuario, String constraseñaAntigua, String contraseñaNueva) {

	}

	// Métodos exclusivos del gestor
	public boolean añadirEmpleado(Empleado empleado) {

	}

	public boolean eliminarEmpleado(Empleado empleado) {

	}

	// Métodos del Cliente Venta
	public List<LineaProductoVenta> buscarProductosNuevos(String prompt) {
	}

	public boolean crearPedidoAPartirDeCarrito() {
	}

	public boolean cancelarPedido(SolicitudPedido pedido, Usuario usuario) {

	}

	// Métodos del Cliente Intercambio
	public List<ProductoSegundaMano> buscarProductoIntercambio(String prompt, Usuario ususario) {
	}

	public boolean aceptarOferta(Oferta oferta, Usuario usuario) {
	}

	public boolean rechazarOferta(Oferta oferta, Usuario usuario) {
	}

	// Métodos de recomendación
	public List<Producto> getRecomendacion() {
	}

	// Métodos de Usuarios Gestión
	public List<LineaProductoVenta> buscarProductoAGestionar(int promptId) {
	}

	// Métodos de pagos
	public boolean gestionarPagoPedido(SolicitudPedido pedido, int numTarjeta, int cvv, int mesCaducidad,
			int añoCaducidad) {

	}

	public boolean gestionarPagoValidacion(SolicitudValidacion solicitud, int numTarjeta, int cvv, int mesCaducidad,
			int añoCaducidad) {

	}

	// Notificación Empleado

	private Notificacion crearNotificacionEmpleado(Solicitud solicutud) {
	}

	// Notificación Cliente

	private Notificacion crearNotificacionIntercambio(SolicitudIntercambio solicitud) {
	}

	private Notificacion crearNotificacionOferta(Oferta oferta) {
	}

	private Notificacion crearNotificacionProducto(LineaProductoVenta producto) {
	}

	private Notificacion crearNotificacionValidacion(ProductoSegundaMano producto) {
	}

	private Notificacion crearNotificacionPedido(SolicitudPedido solicitud) {
	}

	// Envío de notificaciones
	public boolean enviarNotificacion(Usuario usuario, NotificacionUsuario notifificacion) {
		return false;
	}

}
