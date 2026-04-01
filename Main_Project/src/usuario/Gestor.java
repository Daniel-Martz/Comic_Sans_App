package usuario;

import aplicacion.Aplicacion;

public class Gestor extends UsuarioGestion {
	
	public Gestor(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	public Empleado crearEmpleado(String nombre, String dni) {
		return Aplicacion.getInstancia().añadirEmpleado(nombre, dni, "123456");
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
	
	public void configurarImportancia(int interes, int reseña, int novedad) {
		Aplicacion.getInstancia().getConfiguracionRecomendacion().configurarImportancia(interes, reseña, novedad);
	}
	
	public void configurarUnidadesRecomendadas(int unidades) {
		Aplicacion.getInstancia().getConfiguracionRecomendacion().configurarUnidades(unidades);
	}
	
}
