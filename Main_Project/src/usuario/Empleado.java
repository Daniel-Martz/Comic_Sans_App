package usuario;

import java.util.*;

import notificacion.*;
import producto.*;
import solicitud.*;

public class Empleado extends UsuarioGestion {

  private List<NotificacionEmpleado> notificaciones = new ArrayList<>();
  private Set<Permiso> permisos = new HashSet<Permiso>();

  public Empleado(String username, String DNI, String password) {
    super(username, DNI, password);
  }

  public void añadirPermiso(Permiso permiso) {
    this.permisos.add(permiso);
  }

  public void eliminarPermiso(Permiso permiso) {
    this.permisos.remove(permiso);
  }

  public void añadirNotificacion(NotificacionEmpleado n) {
    this.notificaciones.add(n);
  }

  public void eliminarNotificacion(NotificacionEmpleado n) {
    this.notificaciones.remove(n);
  }

  public void aprobarIntercambio(SolicitudIntercambio s, String codigoOfertante, String codigoDestinatario) {
    if (!permisos.contains(Permiso.INTERCAMBIOS)) {
      throw new IllegalStateException("No tienes permisos para aprobar intercambios");
    }
    s.aprobarIntercambio(codigoOfertante, codigoDestinatario);
    System.out.println("Intercambio realizado!");

  }

  public void validarProducto(SolicitudValidacion s, double precioValidacion, double precioProducto, EstadoConservacion estadoProducto) {
    if (!permisos.contains(Permiso.VALIDACIONES)) {
      throw new IllegalStateException("No tienes permisos para aprobar validar productos");
    }
    s.validarProducto(precioValidacion, precioProducto, estadoProducto);
    System.out.println("La solicitud ha sido validada correctamente. Se le ha asociado un precio " + precioProducto
        + " y un estado " + estadoProducto);
  }
	public void actualizarEstadoPedido(SolicitudPedido p, EstadoPedido nuevoEstado) {
		if(p == null) {
			throw new IllegalArgumentException("El pedido no puede ser null");
		}
		p.actualizarEstado(nuevoEstado);
	}
}
