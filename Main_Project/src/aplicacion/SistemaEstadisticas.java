package aplicacion; 

import java.util.*;
import producto.*;
import producto.*;
import usuario.*;
import tiempo.DateTimeSimulado;

public class SistemaEstadisticas {

	private static SistemaEstadisticas instancia;
	
	private SistemaEstadisticas() {
	}
	
	public static SistemaEstadisticas getInstancia() {
		if (instancia == null) {
			instancia = new SistemaEstadisticas();
		}
		return instancia;
	}

	public Map<String, Double> obtenerRecaudacionMensual(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, List<Producto> productos) {
		return null;
	}

	public Map<String, Double> obtenerRecaudacionAmbito(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, List<Producto> productos) {
		return null;
	}

	public Map<LineaProductoVenta, Integer> obtenerVentasProductos(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, List<LineaProductoVenta> productos) {
		return null;
	}

	public Map<ClienteRegistrado, Double> obtenerGastoClientes(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, List<ClienteRegistrado> clientes) {
		return null;
	}
}
