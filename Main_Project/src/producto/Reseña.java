package producto;
import java.io.Serializable;
import tiempo.DateTimeSimulado;

/**
 * Representa una reseña realizada sobre un producto.
 * Contiene una descripción, una puntuación, la fecha y el producto reseñado.
 *
 * La puntuación debe estar entre 0.0 y 5.0.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Reseña implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String descripcion;
	private final double puntuacion;
	private final DateTimeSimulado fecha;
	private final LineaProductoVenta producto;
	
	/**
	 * Construye una nueva reseña.
	 *
	 * @param descripcion texto descriptivo de la reseña
	 * @param puntuacion puntuación entre 0 y 5
	 * @param fecha fecha de creación de la reseña
	 * @param producto producto reseñado
	 * @throws IllegalArgumentException si la puntuación no está entre 0 y 5
	 */
	public Reseña(String descripcion, double puntuacion, DateTimeSimulado fecha, LineaProductoVenta producto) {
		if (puntuacion < 0 || puntuacion > 5) {
			throw new IllegalArgumentException("La puntuación debe ser un real entre 0.00 y 5.00");
		}
		
		this.descripcion = descripcion;
		this.puntuacion = puntuacion;
		this.fecha = fecha;
		this.producto = producto;
	}

	/**
	 * Devuelve la descripción de la reseña.
	 *
	 * @return descripción
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Modifica la descripción de la reseña.
	 *
	 * @param descripcion nueva descripción
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve la puntuación.
	 *
	 * @return puntuación
	 */
	public double getPuntuacion() {
		return puntuacion;
	}

	/**
	 * Devuelve la fecha de la reseña.
	 *
	 * @return fecha
	 */
	public DateTimeSimulado getFecha() {
		return fecha;
	}

	/**
	 * Devuelve el producto reseñado.
	 *
	 * @return producto
	 */
	public LineaProductoVenta getProducto() {
		return producto;
	}

	@Override
	public String toString() {
	    return "Reseña [puntuacion=" + puntuacion + "/5" +
	           " | fecha=" + fecha.toStringFecha() +
	           " | comentario=\"" + descripcion + "\"]";
	}
}
