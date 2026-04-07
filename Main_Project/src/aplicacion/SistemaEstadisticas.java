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

/**
 * Sistema centralizado para la gestión y exportación de estadísticas de la aplicación.
 * Implementa el patrón Singleton y permite la generación de informes en ficheros de texto
 * sobre recaudación mensual, recaudación por ámbito, ventas por productos y gastos de clientes.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class SistemaEstadisticas implements Serializable{

	private static final long serialVersionUID = 1L;

	/** Instancia única de la clase (Singleton). */
	private static SistemaEstadisticas instancia;
	
	/** Lista que almacena el histórico de todos los pagos registrados en el sistema. */
	private List<Pago> pagos = new ArrayList<>();

	/**
	 * Constructor privado para evitar la instanciación externa y garantizar el patrón Singleton.
	 */
	private SistemaEstadisticas() {
	}

	/**
	 * Obtiene la instancia única del SistemaEstadisticas.
	 *
	 * @return la instancia global de las estadísticas.
	 */
	public static SistemaEstadisticas getInstancia() {
		if (instancia == null) {
			instancia = new SistemaEstadisticas();
		}
		return instancia;
	}

	/**
	 * Añade un nuevo pago al histórico del sistema para futuras estadísticas.
	 *
	 * @param p el pago a registrar.
	 * @throws IllegalArgumentException si el pago introducido es nulo.
	 */
	public void añadirPago(Pago p) {
		if (p == null)
			throw new IllegalArgumentException("El pago añadido no puede ser null");
		pagos.add(p);
	}

	/**
	 * Genera un informe en un fichero de texto con la recaudación agrupada por meses 
	 * dentro de un periodo de tiempo determinado.
	 *
	 * @param periodoInicio fecha inicial del periodo a evaluar.
	 * @param periodoFin fecha final del periodo a evaluar.
	 * @param fichero archivo donde se escribirá el informe generado.
	 * @throws IOException si ocurre un error de escritura en el fichero.
	 * @throws IllegalArgumentException si algún parámetro es nulo o si la fecha de inicio es posterior a la de fin.
	 */
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

	/**
	 * Genera un informe detallando la recaudación clasificada por ámbitos o tipos 
	 * de operación (por ejemplo: Venta de productos vs Validaciones).
	 *
	 * @param periodoInicio fecha inicial del periodo a evaluar.
	 * @param periodoFin fecha final del periodo a evaluar.
	 * @param fichero archivo donde se guardará el informe.
	 * @throws IOException si ocurre un error de escritura.
	 * @throws IllegalArgumentException si los parámetros son nulos o la fecha inicial es mayor a la final.
	 */
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

	/**
	 * Genera un informe detallado con las ventas realizadas por cada producto de la tienda.
	 * Permite organizar el documento por IDs de producto o por nivel de recaudación.
	 *
	 * @param periodoInicio fecha de inicio de la estadística.
	 * @param periodoFin fecha de fin de la estadística.
	 * @param porIds true si se desea ordenar el informe por ID, false para ordenar por mayor recaudación.
	 * @param fichero archivo de destino para el informe.
	 * @throws IOException si existe un error al crear o escribir el archivo.
	 * @throws IllegalArgumentException si los datos proporcionados no son válidos.
	 */
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

	/**
	 * Genera un informe donde se detalla la cantidad económica aportada por cada cliente.
	 * Tiene en cuenta los gastos de compra en pedidos y los gastos por tasación o validación.
	 *
	 * @param periodoInicio fecha inicial para el registro del gasto.
	 * @param periodoFin fecha final para el registro del gasto.
	 * @param fichero ubicación y nombre del archivo de texto a generar.
	 * @throws IOException si existe algún problema de escritura con el disco.
	 * @throws IllegalArgumentException si existe algún problema de lógica en los parámetros.
	 */
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
    /**
     * Define el comportamiento de escritura de la instancia durante la serialización.
     * @param oos stream de salida donde se guardará el estado de las estadísticas.
     * @throws IOException si existe algún error de lectura/escritura de los objetos.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Define el comportamiento de recuperación del objeto para mantener la coherencia
     * del patrón de diseño Singleton.
     * @param ois stream de entrada desde donde se leerán los objetos de estadísticas.
     * @throws IOException si existe un problema con el sistema de archivos.
     * @throws ClassNotFoundException si la clase no existe en el contexto.
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instancia = this;
    }

	/**
	 * Devuelve una representación en texto del estado del sistema de estadísticas.
	 *
	 * @return cadena de texto con la información y registros de pagos del sistema.
	 */
	@Override
	public String toString() {
		return "\n\n\n************SISTEMA DE ESTADISTICAS DE LA APLICACION************ \nPagos:\n" + pagos + "]";
	}
}
