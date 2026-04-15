package modelo.producto;

import java.io.Serializable;
/**
 * Almacena los resultados técnicos y económicos tras la revisión de un 
 * producto de segunda mano por parte de un empleado especializado.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class DatosValidacion implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** Precio de mercado sugerido tras la inspección técnica. */
	private final double precioEstimadoProducto;
	
	/** Nivel de conservación física detectado en el producto. */
	private final EstadoConservacion estadoConservacion;
	
	/**
	 * Construye un nuevo registro de validación inmutable.
	 * @param precioEstimadoProducto el valor monetario asignado al producto.
	 * @param estadoConservacion el grado de desgaste (desde PERFECTO hasta DANADO).
	 * @throws IllegalArgumentException si el precio es negativo o el estado es nulo.
	 */
	public DatosValidacion(double precioEstimadoProducto, EstadoConservacion estadoConservacion) {
		if (precioEstimadoProducto < 0) {
			throw new IllegalArgumentException("El precio estimado no puede ser negativo.");
		}
		if (estadoConservacion == null) {
			throw new IllegalArgumentException("El estado de conservación es obligatorio.");
		}
		this.precioEstimadoProducto = precioEstimadoProducto;
		this.estadoConservacion = estadoConservacion;
	}

	/**
	 * Obtiene el precio estimado resultante de la validación.
	 * @return el precio del producto como un valor real.
	 */
	public double getPrecioEstimadoProducto() {
		return precioEstimadoProducto;
	}

	/**
	 * Obtiene el estado de conservación.
	 * @return una constante del enumerado {@link EstadoConservacion}.
	 */
	public EstadoConservacion getEstadoConservacion() {
		return estadoConservacion;
	}

}