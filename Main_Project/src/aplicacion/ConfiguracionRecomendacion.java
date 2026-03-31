package aplicacion;

import java.util.*;

import producto.LineaProductoVenta;
import usuario.ClienteRegistrado;
import usuario.Usuario;

public class ConfiguracionRecomendacion {

	private static ConfiguracionRecomendacion instancia;
	private int importanciaInteres;
	private int importanciaResena;
	private int importanciaNovedad;
	private int unidadesRecomendadas;

	private Map<LineaProductoVenta, Long> rankingNovedad = new HashMap<>();
	private Map<LineaProductoVenta, Double> rankingValoracion = new HashMap<>();
	private Double maxValoracion = 0.0;

	private ConfiguracionRecomendacion(int importanciaInteres, int importanciaResena, int importanciaNovedad,
			int unidadesRecomendadas) {
		this.importanciaInteres = importanciaInteres;
		this.importanciaResena = importanciaResena;
		this.importanciaNovedad = importanciaNovedad;
		this.unidadesRecomendadas = unidadesRecomendadas;
	}

	public static ConfiguracionRecomendacion getInstancia() {
		if (instancia == null) {
			instancia = new ConfiguracionRecomendacion(1, 2, 3, 5);
		}
		return instancia;
	}

	public void configurarImportancia(int importanciaInteres, int importanciaResena, int importanciaNovedad) {
		if (importanciaInteres < 0 || importanciaResena < 0 || importanciaNovedad < 0) {
			throw new IllegalArgumentException("Las importancias no pueden ser negativas");
		}
		this.importanciaInteres = importanciaInteres;
		this.importanciaResena = importanciaResena;
		this.importanciaNovedad = importanciaNovedad;
	}

	public void configurarUnidades(int unidades) {
		if (unidades <= 0) {
			throw new IllegalArgumentException("El número de unidades recomendadas no pueden ser menores o iguales");
		}
		unidadesRecomendadas = unidades;
	}

	public void actualizarRankingNovedad(LineaProductoVenta p) {
		rankingNovedad.put(p, p.getFechaSubida().dateTimeEnSegundos());
	}
	
	public void eliminarProductoNovedad(LineaProductoVenta p) {
		rankingNovedad.remove(p);
	}

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
	
	public void actualizarRankingValoracion(LineaProductoVenta p) {
		double nuevaValoracion = p.obtenerPuntuacionMedia();
		rankingValoracion.put(p, nuevaValoracion);
		
		if(nuevaValoracion > maxValoracion) {
			maxValoracion = nuevaValoracion;
		}
	}

	
	public void eliminarProductoValoracion(LineaProductoVenta p) {
		rankingValoracion.remove(p);
	}
	
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
	    
	    Set<LineaProductoVenta> recomendacion = new HashSet<>();
	    for(int i = 0; i<unidadesRecomendadas; i++) {
	    	LineaProductoVenta best = obtenerMejorProducto(rankingFinal);
	    	rankingFinal.remove(best);
	    	recomendacion.add(best);
	    }
	    return recomendacion;
	}

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
	
}