package usuario;

import aplicacion.Aplicacion;

public class Gestor extends UsuarioGestion {
	
	public Gestor(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	public void crearEmpleado(String nombre, String dni) {
		Aplicacion.getInstancia().añadirEmpleado(nombre, dni, "123456");
	}
	
	public void eliminarEmpleado(Empleado e) {
		Aplicacion.getInstancia().eliminarEmpleado(e);
	}
	
	public void añadirPermiso(Empleado e, Permiso p) {
		e.añadirPermiso(p);
	}
	
	public void eliminarPermiso(Empleado e, Permiso p) {
		e.eliminarPermiso(p);
	}
	
	
}
