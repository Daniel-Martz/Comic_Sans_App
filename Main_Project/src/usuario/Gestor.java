package usuario;

import java.io.File;
import java.io.IOException;

import aplicacion.Aplicacion;
import tiempo.DateTimeSimulado;

public class Gestor extends UsuarioGestion {
	
	public Gestor(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	public Empleado crearEmpleado(String nombre, String dni) {
		Empleado e = Aplicacion.getInstancia().añadirEmpleado(nombre, dni, "123456");
		System.out.println("Se ha creado el empleado: " + nombre + " con DNI: " + dni);
		return e;
	}
	
	public void eliminarEmpleado(Empleado e) {
		Aplicacion.getInstancia().eliminarEmpleado(e);
		System.out.println("Se ha eliminado al empleado del sistema.");
	}
	
	public void añadirPermiso(Empleado e, Permiso p) {
		e.añadirPermiso(p);
		System.out.println("Se ha añadido el permiso " + p + " al empleado " + e.getNombreUsuario());
	}
	
	public void eliminarPermiso(Empleado e, Permiso p) {
		e.eliminarPermiso(p);
		System.out.println("Se ha revocado el permiso " + p + " al empleado " + e.getNombreUsuario());
	}
	
	public void configurarImportancia(int interes, int reseña, int novedad) {
		Aplicacion.getInstancia().getConfiguracionRecomendacion().configurarImportancia(interes, reseña, novedad);
		System.out.println("Configuración de recomendación actualizada -> Interés: " + interes + ", Reseñas: " + reseña + ", Novedad: " + novedad);
	}
	
	public void configurarUnidadesRecomendadas(int unidades) {
		Aplicacion.getInstancia().getConfiguracionRecomendacion().configurarUnidades(unidades);
		System.out.println("Se ha cambiado el número de unidades recomendadas a: " + unidades);
	}
	
	public void estadisticasRecaudacion(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, boolean porMes, File ficheroSalida) throws IOException {
		if(porMes) {
			Aplicacion.getInstancia().getSistemaEstadisticas().obtenerRecaudacionMensual(periodoInicio, periodoFin, ficheroSalida);
			System.out.println("Se ha generado el informe de recaudación mensual en: " + ficheroSalida.getName());
		}
		else {
			Aplicacion.getInstancia().getSistemaEstadisticas().obtenerRecaudacionAmbito(periodoInicio, periodoFin, ficheroSalida);
			System.out.println("Se ha generado el informe de recaudación por ámbito en: " + ficheroSalida.getName());
		}
	}
	
	public void estadisticasVentasProductos(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, boolean porId, File ficheroSalida) throws IOException {
		Aplicacion.getInstancia().getSistemaEstadisticas().obtenerVentasProductos(periodoInicio, periodoFin, porId, ficheroSalida);
		System.out.println("Se ha generado el informe de ventas por productos en: " + ficheroSalida.getName());
	}
	
	public void estadisticasGastoClientes(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, File ficheroSalida) throws IOException {
		Aplicacion.getInstancia().getSistemaEstadisticas().obtenerGastoClientes(periodoInicio, periodoFin, ficheroSalida);
		System.out.println("Se ha generado el informe de gasto por clientes en: " + ficheroSalida.getName());
	}
}
