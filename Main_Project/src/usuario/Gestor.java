package usuario;

import aplicacion.Aplicacion;

public class Gestor extends UsuarioGestion {
	
	public Gestor(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	public Empleado crearEmpleado(String nombre, String dni) {
		return Aplicacion.getInstancia().añadirEmpleado(nombre, dni, "123456");
	}
	
	public void añadirPermiso(Empleado e, Permiso p) {
		e.añadirPermiso(p);
	}
}
