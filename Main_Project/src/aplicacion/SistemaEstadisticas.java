package aplicacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import producto.*;
import solicitud.Pago;
import solicitud.Solicitud;
import solicitud.SolicitudPedido;
import solicitud.SolicitudValidacion;
import usuario.*;
import tiempo.DateTimeSimulado;
import tiempo.TiempoSimulado;

import java.io.Serializable;
public class SistemaEstadisticas implements Serializable{

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
				if (p.getObjetoPagado() instanceof SolicitudPedido) {
					recaudacion.merge("Venta de productos", p.getImporte(), Double::sum);
				}
				if (p.getObjetoPagado() instanceof SolicitudValidacion) {
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

	public void obtenerVentasProductos(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, boolean porIds, File fichero)
			throws IOException {

		if (periodoInicio == null || periodoFin == null || fichero == null) {
			throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
		}
		if (periodoInicio.dateTimeEnSegundos() > periodoFin.dateTimeEnSegundos()) {
			throw new IllegalArgumentException("El periodo de inicio no puede ser posterior al periodo de fin");
		}

		Map<Double, Set<LineaProductoVenta>> recaudacion = new TreeMap<>(Collections.reverseOrder());
		// Inicializamos el mapa de productos auxiliar (es un treeMap porque quiero asegurarme de que esté ordenado por ID)
		// En caso de que se quieran la recaudación ordenada por id usaremos este mapa
		Map<LineaProductoVenta, Double> mapaAuxiliar = new TreeMap<>();


		for (LineaProductoVenta p : Aplicacion.getInstancia().getCatalogo().getProductosNuevos()) {
			Set<LineaProductoVenta> set = recaudacion.getOrDefault(0.0, new LinkedHashSet<>());
			set.add(p);
			recaudacion.put(0.0, set);
			mapaAuxiliar.put(p, 0.0);
		}

		for (Pago p : pagos) {
		    Solicitud solicitudPago = p.getObjetoPagado();
		    DateTimeSimulado fecha = p.getFechaPago();
		    if ((solicitudPago instanceof SolicitudPedido)
		            && fecha.dateTimeEnSegundos() >= periodoInicio.dateTimeEnSegundos()
		            && fecha.dateTimeEnSegundos() <= periodoFin.dateTimeEnSegundos()) {
		        
		        for (Map.Entry<SimpleEntry<LineaProductoVenta, Integer>, Double> entry : 
		                ((SolicitudPedido) solicitudPago).getRecaudacionProductos().entrySet()) {
		            
		            LineaProductoVenta producto = entry.getKey().getKey(); // extraemos el producto del SimpleEntry
		            double recaudacion_valor = entry.getValue();
		            
		            double clave = mapaAuxiliar.getOrDefault(producto, 0.0);
		            double nuevaClave = clave + recaudacion_valor;
		            
		            // Elimino el producto de la lista con recaudacion anterior
		            Set<LineaProductoVenta> setAntiguo = recaudacion.getOrDefault(clave, null);
		            if (setAntiguo != null) {
		                setAntiguo.remove(producto);
		                if (setAntiguo.isEmpty()) {
		                    recaudacion.remove(clave);
		                }
		            }
		            
		            // Añado producto a la lista con nueva recaudacion
		            Set<LineaProductoVenta> setNuevo = recaudacion.getOrDefault(nuevaClave, new LinkedHashSet<>());
		            setNuevo.add(producto);
		            recaudacion.put(nuevaClave, setNuevo);
		            
		            // Actualizo mapa auxiliar
		            mapaAuxiliar.put(producto, nuevaClave);
		        }
		    }
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))) {
			bw.write("------- INFORME DE RECAUDACION POR PRODUCTOS ( " + periodoInicio.toStringFecha() + " - "
					+ periodoFin.toStringFecha() + " ) -------");
			bw.newLine();
			bw.write(String.format("%-5s %-35s %12s", "ID", "NOMBRE DEL PRODUCTO", "RECAUDACION"));
			bw.newLine();
			bw.write("----------------------------------------------------------------------");
			bw.newLine();
			
			double totalAcumulado = 0.0;
			if(porIds) {
				for (Map.Entry<LineaProductoVenta, Double> entry : mapaAuxiliar.entrySet()) {
					LineaProductoVenta producto = entry.getKey();
					Double valor = entry.getValue();
					bw.write(String.format("%-5d %-35.35s %10.2f €", producto.getID(), producto.getNombre(), valor));
					bw.newLine();
					totalAcumulado += valor;
				}
				bw.write("----------------------------------------------------------------------");
				bw.newLine();
				bw.write(String.format("%-41s %10.2f €", "TOTAL RECAUDADO:", totalAcumulado));
				bw.newLine();
			}
			else {
			    for (Map.Entry<Double, Set<LineaProductoVenta>> entry : recaudacion.entrySet()) {
			        double gasto = entry.getKey();
			        for (LineaProductoVenta p : entry.getValue()) {
						bw.write(String.format("%-5d %-35.35s %10.2f €", p.getID(), p.getNombre(), gasto));
			            bw.newLine();
			            totalAcumulado += gasto;
			        }
			    }
			        
				bw.write("----------------------------------------------------------------------");
				bw.newLine();
				bw.write(String.format("%-41s %10.2f €", "TOTAL RECAUDADO:", totalAcumulado));
				bw.newLine();
			}
		}
	}

	public void obtenerGastoClientes(DateTimeSimulado periodoInicio, DateTimeSimulado periodoFin, File fichero)
			throws IOException {

		if (periodoInicio == null || periodoFin == null || fichero == null) {
			throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
		}
		if (periodoInicio.dateTimeEnSegundos() > periodoFin.dateTimeEnSegundos()) {
			throw new IllegalArgumentException("El periodo de inicio no puede ser posterior al periodo de fin");
		}

		Map<Double, List<ClienteRegistrado>> recaudacion = new TreeMap<>(Collections.reverseOrder());

		for (ClienteRegistrado c : Aplicacion.getInstancia().getClientesRegistrados()) {
			double gasto = 0.0;
			
			//Gasto en compras 
			for (SolicitudPedido s : c.getPedidos()) {
				DateTimeSimulado fecha = s.getPagoPedido().getFechaPago();
				if (fecha.dateTimeEnSegundos() >= periodoInicio.dateTimeEnSegundos()
						&& fecha.dateTimeEnSegundos() <= periodoFin.dateTimeEnSegundos()) {
					gasto += s.getCostePedido();
				}
			}
			
			//Gasto en validaciones
			for(ProductoSegundaMano p : c.getCartera().getProductos()) {
				if(p.isValidado()) {
					Pago pago = p.getSolicitudValidacion().getPagoValidacion();
					DateTimeSimulado fecha = pago.getFechaPago();
					if (fecha.dateTimeEnSegundos() >= periodoInicio.dateTimeEnSegundos()
							&& fecha.dateTimeEnSegundos() <= periodoFin.dateTimeEnSegundos()) {
						gasto += pago.getImporte();
					}
				}
			}
			List<ClienteRegistrado> lista = recaudacion.getOrDefault(gasto, new LinkedList<>());
			lista.add(c);
			recaudacion.put(gasto, lista);
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))) {
		    bw.write("------- INFORME DE RECAUDACION POR CLIENTES ( " + periodoInicio.toStringFecha() + " - " + periodoFin.toStringFecha() + " ) -------");
		    bw.newLine();
		    bw.newLine();

		    bw.write(String.format("%-12s %-35s %12s", "DNI", "NOMBRE DE USUARIO", "RECAUDACION"));
		    bw.newLine();
		    bw.write("----------------------------------------------------------------------");
		    bw.newLine();

		    double totalAcumulado = 0.0;
		    for (Map.Entry<Double, List<ClienteRegistrado>> entry : recaudacion.entrySet()) {
		        double gasto = entry.getKey();
		        for (ClienteRegistrado c : entry.getValue()) {
		            bw.write(String.format("%-12s %-35.35s %10.2f €", 
		                c.getDNI(), 
		                c.getNombreUsuario(), 
		                gasto));
		            bw.newLine();
		            totalAcumulado += gasto;
		        }
		    }

		    bw.write("----------------------------------------------------------------------");
		    bw.newLine();
		    bw.write(String.format("%-48s %10.2f €", "TOTAL RECAUDADO EN EL PERIODO:", totalAcumulado));
		    bw.newLine();
		}
	}

    // Persist singleton instance across serialization
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instancia = this;
    }

	@Override
	public String toString() {
		return "\n\n\n************SISTEMA DE ESTADISTICAS DE LA APLICACION************ \nPagos:\n" + pagos + "]";
	}
}
