package aplicacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;
import producto.*;
import solicitud.Pago;
import solicitud.SolicitudPedido;
import solicitud.SolicitudValidacion;
import producto.*;
import usuario.*;
import tiempo.DateTimeSimulado;
import tiempo.TiempoSimulado;

public class SistemaEstadisticas {

	private static SistemaEstadisticas instancia;
	private List<Pago> pagos = new ArrayList<>();

	private SistemaEstadisticas() {
	}

	public static SistemaEstadisticas getInstancia() {
		if (instancia == null) {
			instancia = new SistemaEstadisticas();
		}
		return instancia;
	}

	public void añadirPago(Pago p) {
		if (p == null)
			throw new IllegalArgumentException("El pago añadido no puede ser null");
		pagos.add(p);
	}

	public void obtenerRecaudacionMensual(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, File fichero)
			throws IOException {
		
	    if (periodoInicio == null || periodoFin == null || fichero == null) {
	        throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
	    }
	    if (periodoInicio.dateTimeEnSegundos() > periodoFin.dateTimeEnSegundos()) {
	        throw new IllegalArgumentException("El periodo de inicio no puede ser posterior al periodo de fin");
	    }
	    
		Map<String, Double> recaudacion = new LinkedHashMap<>();

		// Inicializo el mapa para que si un mes no hay pagos aparezca como 0.0
		for (String mes : TiempoSimulado.getInstance().periodoAMeses(periodoInicio, periodoFin)) {
			recaudacion.put(mes, 0.0);
		}

		for (Pago p : pagos) {
			DateTimeSimulado fecha = p.getFechaPago();
			if (fecha.dateTimeEnSegundos() >= periodoInicio.dateTimeEnSegundos()
					&& fecha.dateTimeEnSegundos() <= periodoFin.dateTimeEnSegundos()) {
				recaudacion.merge(fecha.getMes() + "/" + fecha.getAño(), p.getImporte(), Double::sum);
			}
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))) {
			bw.write("------- INFORME DE RECAUDACION MENSUAL ( " + periodoInicio.toStringFecha() + " - "
					+ periodoFin.toStringFecha() + " ) -------");
			bw.newLine();
			bw.write(String.format("%-15s %s", "MES", "RECAUDACION"));
			bw.newLine();
			bw.write("--------------------------------");
			bw.newLine();

			for (Map.Entry<String, Double> entry : recaudacion.entrySet()) {
				bw.write(String.format("%-15s %.2f €", entry.getKey(), entry.getValue()));
				bw.newLine();
			}

			// Total al final
			double total = 0.0;
			for (double valor : recaudacion.values()) {
				total += valor;
			}
			bw.write("--------------------------------");
			bw.newLine();
			bw.write(String.format("%-15s %.2f €", "TOTAL", total));
			bw.newLine();
		}
	}

	public void obtenerRecaudacionAmbito(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, File fichero)
			throws IOException {
		
	    if (periodoInicio == null || periodoFin == null || fichero == null) {
	        throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
	    }
	    if (periodoInicio.dateTimeEnSegundos() > periodoFin.dateTimeEnSegundos()) {
	        throw new IllegalArgumentException("El periodo de inicio no puede ser posterior al periodo de fin");
	    }
	    
		Map<String, Double> recaudacion = new LinkedHashMap<>();

		// Inicializo el mapa para que si un mes no hay pagos aparezca como 0.0
		recaudacion.put("Venta de productos", 0.0);
		recaudacion.put("Validaciones", 0.0);
		for (Pago p : pagos) {
			DateTimeSimulado fecha = p.getFechaPago();
			if (fecha.dateTimeEnSegundos() >= periodoInicio.dateTimeEnSegundos()
					&& fecha.dateTimeEnSegundos() <= periodoFin.dateTimeEnSegundos()) {
				if(p.getObjetoPagado() instanceof SolicitudPedido) {
					recaudacion.merge("Venta de productos", p.getImporte(), Double::sum);
				}
				if(p.getObjetoPagado() instanceof SolicitudValidacion) {
					recaudacion.merge("Validaciones", p.getImporte(), Double::sum);
				}
			}
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))) {
		    bw.write("------- INFORME DE RECAUDACION POR AMBITO ( " + periodoInicio.toStringFecha() + " - "
		            + periodoFin.toStringFecha() + " ) -------");
		    bw.newLine();
		    bw.write(String.format("%-25s %s", "AMBITO", "RECAUDACION"));
		    bw.newLine();
		    bw.write("----------------------------------------");
		    bw.newLine();

		    for (Map.Entry<String, Double> entry : recaudacion.entrySet()) {
		        bw.write(String.format("%-25s %.2f €", entry.getKey(), entry.getValue()));
		        bw.newLine();
		    }

		    // Total al final
		    double total = 0.0;
		    for (double valor : recaudacion.values()) {
		        total += valor;
		    }
		    bw.write("----------------------------------------");
		    bw.newLine();
		    bw.write(String.format("%-25s %.2f €", "TOTAL", total));
		    bw.newLine();
		}
	}

	public Map<LineaProductoVenta, Integer> obtenerVentasProductos(DateTimeSimulado periodoInicio,
			DateTimeSimulado periodoFin, List<LineaProductoVenta> productos) {
		return null;
	}

	public Map<ClienteRegistrado, Double> obtenerGastoClientes(DateTimeSimulado periodoInicio,
			DateTimeSimulado periodoFin, List<ClienteRegistrado> clientes) {
		return null;
	}
}
