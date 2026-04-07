package filtro;

import producto.EstadoConservacion;
import java.util.*;

/**
 * Filtro especializado para la búsqueda y clasificación de intercambios.
 * Permite filtrar por rangos de valor del producto y estados de conservación.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class FiltroIntercambio extends Filtro {
	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Valor mínimo del producto para el intercambio. */
	private double valorMin;
	
	/** Valor máximo del producto para el intercambio. */
	private double valorMax;
	
	/** Conjunto de estados de conservación permitidos en el filtro. */
	private Set<EstadoConservacion> estadosFiltrados = new HashSet<EstadoConservacion>();
	
	/**
	 * Construye un filtro de intercambio con un rango de valores inicial.
	 * @param ordenAscendente define la dirección de la ordenación.
	 * @param valorMin límite inferior de valor.
	 * @param valorMax límite superior de valor.
	 * @throws IllegalArgumentException si los valores son negativos o el máximo es menor al mínimo.
	 */
	public FiltroIntercambio(boolean ordenAscendente, double valorMin, double valorMax) {
		super(ordenAscendente);
		if(valorMin < 0 || valorMax < valorMin) {
			throw new IllegalArgumentException("Los valores minimo y máximo deben de ser positivos y el máximo mayor que el mínimo");
		}
		this.valorMin = valorMin;
		this.valorMax = valorMax;
	}

	/** @return El valor mínimo actual del filtro. */
	public double getValorMin() {
		return valorMin;
	}

	/** @return El valor máximo actual del filtro. */
	public double getValorMax() {
		return valorMax;
	}

	/** @return El conjunto de estados de conservación seleccionados. */
	public Set<EstadoConservacion> getEstadosFiltrados() {
		return estadosFiltrados;
	}
	
	/**
	 * Restablece los valores del filtro a sus estados por defecto (rango total y sin estados).
	 */
	@Override
	public void limpiarFiltro() {
		valorMin = 0;
		valorMax = Double.MAX_VALUE;
		estadosFiltrados.clear();
		super.limpiarFiltro();
	}

	/**
	 * Actualiza los criterios del filtro validando la coherencia del rango de valores.
	 * @param ordenAscendente nuevo orden de visualización.
	 * @param valorMin nuevo valor mínimo.
	 * @param valorMax nuevo valor máximo.
	 * @param estados nuevo conjunto de estados de conservación.
	 * @throws IllegalArgumentException si los límites de valor son inválidos.
	 */
	public void cambiarFiltro(boolean ordenAscendente, double valorMin, double valorMax, Set<EstadoConservacion> estados) {
		if (valorMin < 0) {
			throw new IllegalArgumentException("El valor mínimo no puede ser negativo");
		}
		if (valorMax < valorMin) {
			throw new IllegalArgumentException("El valor máximo no puede ser menor que el mínimo");
		}

		this.ordenAscendente = ordenAscendente;

		this.valorMin = valorMin;
		this.valorMax = valorMax;

		this.estadosFiltrados.clear();
		if (estados != null) {
			this.estadosFiltrados.addAll(estados);
		}
	}
	
}
