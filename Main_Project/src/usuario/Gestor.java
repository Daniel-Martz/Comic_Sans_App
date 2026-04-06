package usuario;

import java.io.File;
import java.io.IOException;

import aplicacion.Aplicacion;
import tiempo.DateTimeSimulado;

/**
 * Implementa la clase Gestor.
 * Representa al administrador principal con capacidad para gestionar empleados, 
 * configurar el sistema de recomendaciones y generar estadísticas.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class Gestor extends UsuarioGestion {
	
	/**
	 * Instancia un nuevo gestor.
	 *
	 * @param username el nombre de usuario
	 * @param DNI el DNI
	 * @param password la contraseña
	 */
	public Gestor(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	/**
	 * Crea un empleado en el sistema.
	 *
	 * @param nombre el nombre del empleado
	 * @param dni el DNI del empleado
	 * @return el empleado creado
	 */
	public Empleado crearEmpleado(String nombre, String dni) {
		Empleado e = Aplicacion.getInstancia().añadirEmpleado(nombre, dni, "123456");
		System.out.println("Se ha creado el empleado: " + nombre + " con DNI: " + dni);
		return e;
	}
	
	/**
	 * Elimina un empleado del sistema.
	 *
	 * @param e el empleado a eliminar
	 */
	public void eliminarEmpleado(Empleado e) {
		Aplicacion.getInstancia().eliminarEmpleado(e);
		System.out.println("Se ha eliminado al empleado del sistema.");
	}
	
	/**
	 * Añade un permiso específico a un empleado.
	 *
	 * @param e el empleado
	 * @param p el permiso a añadir
	 */
	public void añadirPermiso(Empleado e, Permiso p) {
		e.añadirPermiso(p);
		System.out.println("Se ha añadido el permiso " + p + " al empleado " + e.getNombreUsuario());
	}
	
	/**
	 * Elimina un permiso específico de un empleado.
	 *
	 * @param e el empleado
	 * @param p el permiso a eliminar
	 */
	public void eliminarPermiso(Empleado e, Permiso p) {
		e.eliminarPermiso(p);
		System.out.println("Se ha revocado el permiso " + p + " al empleado " + e.getNombreUsuario());
	}
	
	/**
	 * Configura los pesos de importancia para el sistema de recomendaciones.
	 *
	 * @param interes el peso del interés
	 * @param reseña el peso de las reseñas
	 * @param novedad el peso de la novedad
	 */
	public void configurarImportancia(int interes, int reseña, int novedad) {
		Aplicacion.getInstancia().getConfiguracionRecomendacion().configurarImportancia(interes, reseña, novedad);
		System.out.println("Configuración de recomendación actualizada -> Interés: " + interes + ", Reseñas: " + reseña + ", Novedad: " + novedad);
	}
	
	/**
	 * Configura el número máximo de unidades que se van a recomendar.
	 *
	 * @param unidades el número de unidades
	 */
	public void configurarUnidadesRecomendadas(int unidades) {
		Aplicacion.getInstancia().getConfiguracionRecomendacion().configurarUnidades(unidades);
		System.out.println("Se ha cambiado el número de unidades recomendadas a: " + unidades);
	}
	
	/**
	 * Genera un informe de estadísticas de recaudación.
	 *
	 * @param periodoInicio la fecha de inicio
	 * @param periodoFin la fecha de fin
	 * @param porMes true para desglose mensual, false para desglose por ámbito
	 * @param ficheroSalida el fichero donde se guardará el informe
	 * @throws IOException si hay un error de entrada/salida
	 */
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
	
	/**
	 * Genera un informe de estadísticas de ventas de productos.
	 *
	 * @param periodoInicio la fecha de inicio
	 * @param periodoFin la fecha de fin
	 * @param porId true para ordenar por ID, false para otro criterio
	 * @param ficheroSalida el fichero donde se guardará el informe
	 * @throws IOException si hay un error de entrada/salida
	 */
	public void estadisticasVentasProductos(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, boolean porId, File ficheroSalida) throws IOException {
		Aplicacion.getInstancia().getSistemaEstadisticas().obtenerVentasProductos(periodoInicio, periodoFin, porId, ficheroSalida);
		System.out.println("Se ha generado el informe de ventas por productos en: " + ficheroSalida.getName());
	}
	
	/**
	 * Genera un informe de estadísticas de gasto por clientes.
	 *
	 * @param periodoInicio la fecha de inicio
	 * @param periodoFin la fecha de fin
	 * @param ficheroSalida el fichero donde se guardará el informe
	 * @throws IOException si hay un error de entrada/salida
	 */
	public void estadisticasGastoClientes(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, File ficheroSalida) throws IOException {
		Aplicacion.getInstancia().getSistemaEstadisticas().obtenerGastoClientes(periodoInicio, periodoFin, ficheroSalida);
		System.out.println("Se ha generado el informe de gasto por clientes en: " + ficheroSalida.getName());
	}
}
