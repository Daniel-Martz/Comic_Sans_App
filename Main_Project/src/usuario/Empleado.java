package usuario;
import java.util.*;

public class Empleado extends UsuarioGestion {
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
}

