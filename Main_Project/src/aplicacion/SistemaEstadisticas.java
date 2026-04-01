package aplicacion; 

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;
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

	public void obtenerRecaudacionMensual(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, List<Producto> productos, File fichero) throws IOException {
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))){
			bw.write("------- INFORME DE RECAUDACIÓN MENSUAL ( " + periodoInicio.toStringFecha() + " - " + periodoFin.toStringFecha() + " ) -------");
		}
		
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
