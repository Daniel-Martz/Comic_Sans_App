package usuario;
import java.util.*;
import notificacion.NotificacionEmpleado;
public class Empleado extends UsuarioGestion {
	private List<NotificacionEmpleado> notificaciones = new ArrayList<>();
	public Empleado(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	private Set<Permiso> permisos= new HashSet<Permiso>();
	
	public void añadirPermiso(Permiso permiso) {
		this.permisos.add(permiso);
	}
	
	public void eliminarPermiso(Permiso permiso) {
		this.permisos.remove(permiso);
	}
	
	public void anadirNotificacion(NotificacionEmpleado n) {
		this.notificaciones.add(n);
	}
	public void eliminarNotificacion(NotificacionEmpleado n) {
		this.notificaciones.remove(n);
	}
}

