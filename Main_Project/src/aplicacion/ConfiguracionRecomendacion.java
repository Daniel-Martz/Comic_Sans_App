package aplicacion;

import java.util.*;
import java.io.*;

import producto.LineaProductoVenta;
import usuario.ClienteRegistrado;
import usuario.Usuario;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfiguracionRecomendacion.
 */
public class ConfiguracionRecomendacion implements Serializable {

    private static final long serialVersionUID = 1L;

	/** El instancia. */
	private static ConfiguracionRecomendacion instancia;
	
	/** El importancia interes. */
	private int importanciaInteres;
	
	/** El importancia resena. */
	private int importanciaResena;
	
	/** El importancia novedad. */
	private int importanciaNovedad;
	
	/** El unidades recomendadas. */
	private int unidadesRecomendadas;

	/** El ranking novedad. */
	private Map<LineaProductoVenta, Long> rankingNovedad = new HashMap<>();
	
	/** El ranking valoracion. */
	private Map<LineaProductoVenta, Double> rankingValoracion = new HashMap<>();
	
	/** El max valoracion. */
	private Double maxValoracion = 0.0;

	/**
	 * Instancia un nuevo configuracion recomendacion.
	 *
	 * @param importanciaInteres el importancia interes
	 * @param importanciaResena el importancia resena
	 * @param importanciaNovedad el importancia novedad
	 * @param unidadesRecomendadas el unidades recomendadas
	 */
	private ConfiguracionRecomendacion(int importanciaInteres, int importanciaResena, int importanciaNovedad,
			int unidadesRecomendadas) {
		this.importanciaInteres = importanciaInteres;
		this.importanciaResena = importanciaResena;
		this.importanciaNovedad = importanciaNovedad;
		this.unidadesRecomendadas = unidadesRecomendadas;
	}

	/**
	 * Devuelve el instancia.
	 *
	 * @return el instancia
	 */
	public static ConfiguracionRecomendacion getInstancia() {
		if (instancia == null) {
			instancia = new ConfiguracionRecomendacion(1, 2, 3, 5);
		}
		return instancia;
	}

	/**
	 * Configurar importancia.
	 *
	 * @param importanciaInteres el importancia interes
	 * @param importanciaResena el importancia resena
	 * @param importanciaNovedad el importancia novedad
	 */
	public void configurarImportancia(int importanciaInteres, int importanciaResena, int importanciaNovedad) {
		if (importanciaInteres < 0 || importanciaResena < 0 || importanciaNovedad < 0) {
			throw new IllegalArgumentException("Las importancias no pueden ser negativas");
		}
		this.importanciaInteres = importanciaInteres;
		this.importanciaResena = importanciaResena;
		this.importanciaNovedad = importanciaNovedad;
	}

	/**
	 * Configurar unidades.
	 *
	 * @param unidades el unidades
	 */
	public void configurarUnidades(int unidades) {
		if (unidades <= 0) {
			throw new IllegalArgumentException("El número de unidades recomendadas no pueden ser menores o iguales");
		}
		unidadesRecomendadas = unidades;
	}

	/**
	 * Actualizar ranking novedad.
	 *
	 * @param p el p
	 */
	public void actualizarRankingNovedad(LineaProductoVenta p) {
		rankingNovedad.put(p, p.getFechaSubida().dateTimeEnSegundos());
	}
	
	/**
	 * Eliminar producto novedad.
	 *
	 * @param p el p
	 */
	public void eliminarProductoNovedad(LineaProductoVenta p) {
		rankingNovedad.remove(p);
	}

	/**
	 * Obtener ranking novedad unificado.
	 *
	 * @return el map
	 */
	private Map<LineaProductoVenta, Double> obtenerRankingNovedadUnificado() {
		Map<LineaProductoVenta, Double> ranking = new HashMap<>();
		
		if(rankingNovedad.isEmpty()) {
			return ranking;
		}
		
		double divisor = (double) (Aplicacion.getInstancia().getCatalogo().getUltimoLanzamiento().dateTimeEnSegundos()
				- Aplicacion.getInstancia().getCatalogo().getPrimerLanzamiento().dateTimeEnSegundos());
		
		if (divisor == 0.0) {
		    for (LineaProductoVenta p : rankingNovedad.keySet()) {
		        ranking.put(p, 1.0); // si solo hay un producto, le damos el máximo
		    }
		    return ranking;
		}
		
		for(Map.Entry<LineaProductoVenta, Long> entry : rankingNovedad.entrySet()) {
			ranking.put(entry.getKey(), entry.getValue()/divisor);
		}
		
		return ranking;
	}
	
	/**
	 * Actualizar ranking valoracion.
	 *
	 * @param p el p
	 */
	public void actualizarRankingValoracion(LineaProductoVenta p) {
		double nuevaValoracion = p.obtenerPuntuacionMedia();
		rankingValoracion.put(p, nuevaValoracion);
		
		if(nuevaValoracion > maxValoracion) {
			maxValoracion = nuevaValoracion;
		}
	}

	
	/**
	 * Eliminar producto valoracion.
	 *
	 * @param p el p
	 */
	public void eliminarProductoValoracion(LineaProductoVenta p) {
		rankingValoracion.remove(p);
	}
	
	/**
	 * Obtener ranking valoracion unificado.
	 *
	 * @return el map
	 */
	private Map<LineaProductoVenta, Double> obtenerRankingValoracionUnificado(){
		Map<LineaProductoVenta, Double> ranking = new HashMap<>();
		
		if(rankingValoracion.isEmpty() || maxValoracion == 0.0) {
			return ranking;
		}
		for(Map.Entry<LineaProductoVenta, Double> entry : rankingValoracion.entrySet()) {
			ranking.put(entry.getKey(), entry.getValue()/maxValoracion);
		}
		
		return ranking;
	}

	/**
	 * Devuelve el recomendacion.
	 *
	 * @return el recomendacion
	 */
	public Set<LineaProductoVenta> getRecomendacion() {
		Set<LineaProductoVenta> productos = Aplicacion.getInstancia().getCatalogo().getProductosNuevos();
		Usuario usuario = Aplicacion.getInstancia().getUsuarioActual();
		if(! (usuario instanceof ClienteRegistrado)) {
		    return new HashSet<>();
		}
		ClienteRegistrado cliente = (ClienteRegistrado) usuario;
		Map<LineaProductoVenta, Double> rankInteres = cliente.getInteres().obtenerRankingDeInteres();
	    Map<LineaProductoVenta, Double> rankNovedad = obtenerRankingNovedadUnificado();
	    Map<LineaProductoVenta, Double> rankValoracion = obtenerRankingValoracionUnificado();
	    Map<LineaProductoVenta, Double> rankingFinal = new HashMap<>();
		
	    for (LineaProductoVenta p : productos) {
	        double puntuacion = 0.0;
	        puntuacion += rankNovedad.getOrDefault(p, 0.0) * importanciaNovedad;
	        puntuacion += rankValoracion.getOrDefault(p, 0.0) * importanciaResena;
	        puntuacion += rankInteres.getOrDefault(p, 0.0) * importanciaInteres;
	        rankingFinal.put(p, puntuacion);
	    }
	    if (rankingFinal.size() <= unidadesRecomendadas) {
	        return new HashSet<>(rankingFinal.keySet());
	    }
	    
	    Set<LineaProductoVenta> recomendacion = new LinkedHashSet<>();
	    for(int i = 0; i<unidadesRecomendadas; i++) {
	    	LineaProductoVenta best = obtenerMejorProducto(rankingFinal);
	    	rankingFinal.remove(best);
	    	recomendacion.add(best);
	    }
	    return recomendacion;
	}

	/**
	 * Obtener mejor producto.
	 *
	 * @param ranking el ranking
	 * @return el linea producto venta
	 */
	private LineaProductoVenta obtenerMejorProducto(Map<LineaProductoVenta, Double> ranking) {
	    LineaProductoVenta mejor = null;
	    double maxValor = -1.0;
	    for (Map.Entry<LineaProductoVenta, Double> entry : ranking.entrySet()) {
	        if (entry.getValue() > maxValor) {
	            maxValor = entry.getValue();
	            mejor = entry.getKey();
	        }
	    }
	    return mejor;
	}
	
	// Persist singleton
	private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instancia = this;
    }
}