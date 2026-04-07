package filtro;

import java.util.*;

import categoria.Categoria;

/**
 * Filtro de ventas avanzado para clientes.
 * Permite filtrar por rangos de puntuación, precio y tipos de descuento.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class FiltroVentaCliente extends FiltroVenta {
	
	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;
	
	/** Puntuación mínima del producto (0.0 - 5.0). */
	private double puntuacionMin;
	
	/** Puntuación máxima del producto (0.0 - 5.0). */
	private double puntuacionMax;
	
	/** Límite inferior de precio. */
	private double precioMin;
	
	/** Límite superior de precio. */
	private double precioMax;
	
	/** Indica si los resultados deben ordenarse por precio. */
	private boolean ordenarPorPrecio;
	
	/** Indica si los resultados deben ordenarse por puntuación. */
	private boolean ordenarPorPuntuacion;
	
	/** Conjunto de tipos de descuento seleccionados para filtrar. */
	private Set<TipoDescuento> descuentoFiltrado = new HashSet<TipoDescuento>();

	/**
	 * Instancia el filtro con sus parámetros de orden y rango.
	 */
	public FiltroVentaCliente(boolean ordenAscendente, double puntuacionMin, double puntuacionMax, double precioMin,
			double precioMax, boolean ordenarPorPrecio, boolean ordenarPorPuntuacion) {
		super(ordenAscendente);
		this.puntuacionMin = puntuacionMin;
		this.puntuacionMax = puntuacionMax;
		this.precioMin = precioMin;
		this.precioMax = precioMax;
		this.ordenarPorPrecio = ordenarPorPrecio;
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;
	}

	/** @return Puntuación mínima permitida. */
	public double getPuntuacionMin() {
		return puntuacionMin;
	}

	/** @param puntuacionMin Nueva puntuación mínima. */
	public void setPuntuacionMin(double puntuacionMin) {
		this.puntuacionMin = puntuacionMin;
	}

	/** @return Puntuación máxima permitida. */
	public double getPuntuacionMax() {
		return puntuacionMax;
	}

	/** @param puntuacionMax Nueva puntuación máxima. */
	public void setPuntuacionMax(double puntuacionMax) {
		this.puntuacionMax = puntuacionMax;
	}

	/** @return Precio mínimo del rango. */
	public double getPrecioMin() {
		return precioMin;
	}

	/** @param precioMin Nuevo precio mínimo. */
	public void setPrecioMin(double precioMin) {
		this.precioMin = precioMin;
	}

	/** @return Precio máximo del rango. */
	public double getPrecioMax() {
		return precioMax;
	}

	/** @param precioMax Nuevo precio máximo. */
	public void setPrecioMax(double precioMax) {
		this.precioMax = precioMax;
	}

	/** @return true si el orden es por precio. */
	public boolean isOrdenarPorPrecio() {
		return ordenarPorPrecio;
	}

	/** @param ordenarPorPrecio Define si ordena por precio. */
	public void setOrdenarPorPrecio(boolean ordenarPorPrecio) {
		this.ordenarPorPrecio = ordenarPorPrecio;
	}

	/** @return Set de descuentos para filtrar. */
	public Set<TipoDescuento> getDescuentoFiltrado() {
		return descuentoFiltrado;
	}

	/** @return true si el orden es por puntuación. */
	public boolean isOrdenarPorPuntuacion() {
		return ordenarPorPuntuacion;
	}

	/** @param ordenarPorPuntuacion Define si ordena por puntuación. */
	public void setOrdenarPorPuntuacion(boolean ordenarPorPuntuacion) {
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;
	}

	/**
	 * Resetea todos los valores del filtro a los estados iniciales.
	 */
	@Override
	public void limpiarFiltro() {
		super.limpiarFiltro();
		puntuacionMin = 0.0;
		puntuacionMax = 5.0;
		precioMin = 0.0;
		precioMax = Double.MAX_VALUE;
		ordenarPorPrecio = false;
		ordenarPorPuntuacion = false;
		descuentoFiltrado.clear();
	}

	/**
	 * Modifica los criterios de filtrado validando que los rangos sean coherentes.
	 */
	public void cambiarFiltro(boolean ordenAscendente, Set<Categoria> categorias, Set<TipoProducto> tipos,
			double puntuacionMin, double puntuacionMax, double precioMin, double precioMax, boolean ordenarPorPrecio,
			boolean ordenarPorPuntuacion, Set<TipoDescuento> descuentoFiltrado) {

		super.cambiarFiltro(ordenAscendente, categorias, tipos);
		
		if (puntuacionMin < 0 || puntuacionMax > 5.0) {
			throw new IllegalArgumentException("La puntuación debe estar entre 0 y 5");
		}
		if (puntuacionMin > puntuacionMax) {
			throw new IllegalArgumentException("La puntuación mínima no puede ser mayor que la máxima");
		}

		if (precioMin < 0) {
			throw new IllegalArgumentException("El precio mínimo no puede ser negativo");
		}
		if (precioMin > precioMax) {
			throw new IllegalArgumentException("El precio mínimo no puede ser superior al máximo");
		}

		this.puntuacionMin = puntuacionMin;
		this.puntuacionMax = puntuacionMax;
		this.precioMin = precioMin;
		this.precioMax = precioMax;
		this.ordenarPorPrecio = ordenarPorPrecio;
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;

		this.descuentoFiltrado.clear();
		if (descuentoFiltrado != null) {
			this.descuentoFiltrado.addAll(descuentoFiltrado);
		}
	}

  @Override
  public String toString() {
    return "FiltroVentaCliente [categoriasFiltradas=" + categoriasFiltradas + ", ordenAscendente=" + ordenAscendente
        + ", puntuacionMin=" + puntuacionMin + ", puntuacionMax=" + puntuacionMax + ", precioMin=" + precioMin
        + ", precioMax=" + precioMax + ", ordenarPorPrecio=" + ordenarPorPrecio + ", ordenarPorPuntuacion="
        + ordenarPorPuntuacion + ", descuentoFiltrado=" + descuentoFiltrado + ", getTipoFiltrado()=" + getTipoFiltrado()
        + "]";
  }
		
}
