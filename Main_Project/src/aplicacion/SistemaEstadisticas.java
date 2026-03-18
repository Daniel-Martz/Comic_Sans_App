package aplicacion; 

import java.util.*;
import producto.*;
import producto.*;
import usuario.*;

public class SistemaEstadisticas {

	private static SistemaEstadisticas instancia;
	private SistemaEstadisticas() {
	}
	
	public SistemaEstadisticas getInstancia() {
		if (instancia == null) {
			instancia = new SistemaEstadisticas();
		}
		return instancia;
	}

	public Map<String, Double> obtenerRecaudacionMensual(Date periodoInicio, Date periodoFin, List<Producto> productos) {
	}

	public Map<String, Double> obtenerRecaudacionAmbito(Date periodoInicio, Date periodoFin, List<Producto> productos) {
	}

	public Map<LineaProductoVenta, Integer> obtenerVentasProductos(Date periodoInicio, Date periodoFin, List<LineaProductoVenta> productos) {
	}

	public Map<ClienteRegistrado, Double> obtenerGastoClientes(Date periodoInicio, Date periodoFin, List<ClienteRegistrado> clientes) {

	}
}