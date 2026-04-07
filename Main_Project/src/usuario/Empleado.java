package usuario;

import java.util.*;

import notificacion.*;
import producto.*;
import solicitud.*;
import tiempo.DateTimeSimulado;

/**
 * Implementa la clase Empleado.
 * Representa a un trabajador del sistema que, según sus permisos, 
 * puede gestionar validaciones, intercambios y estados de pedidos.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class Empleado extends UsuarioGestion {

	private List<NotificacionEmpleado> notificaciones = new ArrayList<>();
	private Set<Permiso> permisos = new HashSet<Permiso>();

	/**
	 * Instancia un nuevo empleado.
	 *
	 * @param username el nombre de usuario
	 * @param DNI el DNI
	 * @param password la contraseña
	 */
	public Empleado(String username, String DNI, String password) {
		super(username, DNI, password);
	}

	/**
	 * Añade un permiso al empleado.
	 *
	 * @param permiso el permiso a añadir
	 */
	public void añadirPermiso(Permiso permiso) {
		this.permisos.add(permiso);
	}

	/**
	 * Elimina un permiso del empleado.
	 *
	 * @param permiso el permiso a eliminar
	 */
	public void eliminarPermiso(Permiso permiso) {
		this.permisos.remove(permiso);
	} 

	/**
	 * Añade una notificación a la bandeja del empleado.
	 *
	 * @param n la notificación
	 */
	public void añadirNotificacion(NotificacionEmpleado n) {
		this.notificaciones.add(n);
	}

	/**
	 * Elimina una notificación de la bandeja del empleado.
	 *
	 * @param n la notificación
	 */
	public void eliminarNotificacion(NotificacionEmpleado n) {
		this.notificaciones.remove(n);
	}

	/**
	 * Aprueba una solicitud de intercambio entre dos usuarios.
	 *
	 * @param s la solicitud de intercambio
	 * @param codigoOfertante el código de seguimiento del ofertante
	 * @param codigoDestinatario el código de seguimiento del destinatario
	 * @throws IllegalStateException si el empleado no tiene el permiso de intercambios
	 */
	public void aprobarIntercambio(SolicitudIntercambio s, String codigoOfertante, String codigoDestinatario) {
		if (!permisos.contains(Permiso.INTERCAMBIOS)) {
			throw new IllegalStateException("No tienes permisos para aprobar intercambios");
		}
		s.aprobarIntercambio(codigoOfertante, codigoDestinatario);
		System.out.println("Intercambio realizado!");
	}

	/**
	 * Valida un producto de segunda mano y le asigna un precio y estado.
	 *
	 * @param s la solicitud de validación
	 * @param precioValidacion el coste de la validación
	 * @param precioProducto el precio de venta asignado al producto
	 * @param estadoProducto el estado de conservación del producto
	 * @throws IllegalStateException si el empleado no tiene el permiso de validaciones
	 */
	public void validarProducto(SolicitudValidacion s, double precioValidacion, double precioProducto, EstadoConservacion estadoProducto) {
		if (!permisos.contains(Permiso.VALIDACIONES)) {
			throw new IllegalStateException("No tienes permisos para aprobar validar productos");
		}
		
		s.validarProducto(precioValidacion, precioProducto, estadoProducto);
		System.out.println("La solicitud ha sido validada correctamente. Se le ha asociado un precio " + precioProducto
				+ " y un estado " + estadoProducto);
		
		NotificacionValidacion noti = new NotificacionValidacion("La solicitud ha sido validada correctamente", new DateTimeSimulado(),s);
		s.getCliente().anadirNotificacion(noti);
	}

	/**
	 * Actualiza el estado de un pedido y notifica al cliente.
	 *
	 * @param p la solicitud de pedido
	 * @param nuevoEstado el nuevo estado del pedido
	 * @throws IllegalStateException si el empleado no tiene el permiso de pedidos
	 * @throws IllegalArgumentException si el pedido es nulo
	 */
	public void actualizarEstadoPedido(SolicitudPedido p, EstadoPedido nuevoEstado) {
		if (!permisos.contains(Permiso.PEDIDOS)) {
			throw new IllegalStateException("No tienes permisos para actualizar pedidos");
		}
		
		if(p == null) {
			throw new IllegalArgumentException("El pedido no puede ser null");
		}
		p.actualizarEstado(nuevoEstado);
		System.out.println("El estado del pedido se ha actualizado a: " + nuevoEstado);
		
		NotificacionPedido noti = new NotificacionPedido("El estado del pedido se ha actualizado a " + nuevoEstado, new DateTimeSimulado(), p);
		p.getCliente().anadirNotificacion(noti);
	}

	public List<NotificacionEmpleado> getNotificaciones() {
		return notificaciones;
	}
	
}
