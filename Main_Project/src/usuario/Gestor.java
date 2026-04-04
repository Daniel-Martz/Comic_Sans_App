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
	
	public void estadisticasRecaudacion(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, boolean porMes, File ficheroSalida) throws IOException {
		if(porMes) {
			Aplicacion.getInstancia().getSistemaEstadisticas().obtenerRecaudacionMensual (periodoInicio, periodoFin, ficheroSalida);
		}
		else {
			Aplicacion.getInstancia().getSistemaEstadisticas().obtenerRecaudacionAmbito(periodoInicio, periodoFin, ficheroSalida);
		}
	}
	
	public void estadisticasVentasProductos(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, boolean porId, File ficheroSalida) throws IOException {
		Aplicacion.getInstancia().getSistemaEstadisticas().obtenerVentasProductos(periodoInicio, periodoFin, porId, ficheroSalida);
	}
	
	public void estadisticasGastoClientes(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, File ficheroSalida) throws IOException {
		Aplicacion.getInstancia().getSistemaEstadisticas().obtenerGastoClientes(periodoInicio, periodoFin, ficheroSalida);
	}
}
