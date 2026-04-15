package filtro;

import java.util.*;

import categoria.Categoria;

/**
 * Filtro de ventas avanzado para clientes.
 * Permite filtrar por rangos de puntuación, precio y tipos de descuento.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
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
	 * Instancia el filtro de venta para clientes con parámetros de ordenación y rangos 
	 * de búsqueda específicos.
	 *
	 * @param ordenAscendente      {@code true} para que el orden de los resultados sea ascendente, 
	 * {@code false} para descendente.
	 * @param puntuacionMin        Límite inferior de la puntuación media (incluido).
	 * @param puntuacionMax        Límite superior de la puntuación media (incluido).
	 * @param precioMin            Límite inferior del precio del producto (incluido).
	 * @param precioMax            Límite superior del precio del producto (incluido).
	 * @param ordenarPorPrecio     {@code true} si se desea aplicar un criterio de ordenación basado en el precio.
	 * @param ordenarPorPuntuacion {@code true} si se desea aplicar un criterio de ordenación basado en la valoración.
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

	/** * Obtiene la puntuación mínima permitida.
	 * @return Puntuación mínima. 
	 */
	public double getPuntuacionMin() {
		return puntuacionMin;
	}

	/** * Establece la puntuación mínima permitida.
	 * @param puntuacionMin Nueva puntuación mínima. 
	 */
	public void setPuntuacionMin(double puntuacionMin) {
		this.puntuacionMin = puntuacionMin;
	}

	/** * Obtiene la puntuación máxima permitida.
	 * @return Puntuación máxima. 
	 */
	public double getPuntuacionMax() {
		return puntuacionMax;
	}

	/** * Establece la puntuación máxima permitida.
	 * @param puntuacionMax Nueva puntuación máxima. 
	 */
	public void setPuntuacionMax(double puntuacionMax) {
		this.puntuacionMax = puntuacionMax;
	}

	/** * Obtiene el precio mínimo del rango.
	 * @return Precio mínimo. 
	 */
	public double getPrecioMin() {
		return precioMin;
	}

	/** * Establece el precio mínimo del rango.
	 * @param precioMin Nuevo precio mínimo. 
	 */
	public void setPrecioMin(double precioMin) {
		this.precioMin = precioMin;
	}

	/** * Obtiene el precio máximo del rango.
	 * @return Precio máximo. 
	 */
	public double getPrecioMax() {
		return precioMax;
	}

	/** * Establece el precio máximo del rango.
	 * @param precioMax Nuevo precio máximo. 
	 */
	public void setPrecioMax(double precioMax) {
		this.precioMax = precioMax;
	}

	/** * Comprueba si el orden de los resultados es por precio.
	 * @return true si el orden es por precio. 
	 */
	public boolean isOrdenarPorPrecio() {
		return ordenarPorPrecio;
	}

	/** * Define si los resultados deben ordenarse por precio.
	 * @param ordenarPorPrecio Define si ordena por precio. 
	 */
	public void setOrdenarPorPrecio(boolean ordenarPorPrecio) {
		this.ordenarPorPrecio = ordenarPorPrecio;
	}

	/** * Obtiene el conjunto de descuentos seleccionados para filtrar.
	 * @return Set de descuentos para filtrar. 
	 */
	public Set<TipoDescuento> getDescuentoFiltrado() {
		return descuentoFiltrado;
	}

	/** * Comprueba si el orden de los resultados es por puntuación.
	 * @return true si el orden es por puntuación. 
	 */
	public boolean isOrdenarPorPuntuacion() {
		return ordenarPorPuntuacion;
	}

	/** * Define si los resultados deben ordenarse por puntuación.
	 * @param ordenarPorPuntuacion Define si ordena por puntuación. 
	 */
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
	 * @param ordenAscendente      true para ordenar los resultados de forma ascendente, false para descendente.
	 * @param categorias           conjunto de categorías permitidas en el filtro.
	 * @param tipos                conjunto de tipos de producto permitidos en el filtro.
	 * @param puntuacionMin        puntuación mínima requerida para los productos.
	 * @param puntuacionMax        puntuación máxima permitida para los productos.
	 * @param precioMin            precio mínimo requerido para los productos.
	 * @param precioMax            precio máximo permitido para los productos.
	 * @param ordenarPorPrecio     true para aplicar ordenación basada en el precio.
	 * @param ordenarPorPuntuacion true para aplicar ordenación basada en la puntuación.
	 * @param descuentoFiltrado    conjunto de tipos de descuento permitidos en el filtro.
	 * @throws IllegalArgumentException si los rangos de puntuación o precio son inválidos o incoherentes.
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

	/**
	 * Devuelve una representación en formato de cadena de texto del estado actual del filtro.
	 * * @return Cadena de texto con los valores actuales de los atributos del filtro.
	 */
	@Override
	public String toString() {
		return "FiltroVentaCliente [categoriasFiltradas=" + categoriasFiltradas + ", ordenAscendente=" + ordenAscendente
				+ ", puntuacionMin=" + puntuacionMin + ", puntuacionMax=" + puntuacionMax + ", precioMin=" + precioMin
				+ ", precioMax=" + precioMax + ", ordenarPorPrecio=" + ordenarPorPrecio + ", ordenarPorPuntuacion="
				+ ordenarPorPuntuacion + ", descuentoFiltrado=" + descuentoFiltrado + ", getTipoFiltrado()=" + getTipoFiltrado()
				+ "]";
	}
		
}
