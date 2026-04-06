package producto;

import tiempo.DateTimeSimulado;

/**
 * Representa una reseña realizada sobre un producto.
 * Contiene una descripción, una puntuación, la fecha y el producto reseñado.
 *
 * La puntuación debe estar entre 0.0 y 5.0.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class Reseña {
	
	private String descripcion;
	private double puntuacion;
	private DateTimeSimulado fecha;
	private LineaProductoVenta producto;
	
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
	 * Modifica la puntuación.
	 *
	 * @param puntuacion nueva puntuación
	 * @throws IllegalArgumentException si no está entre 0 y 5
	 */
	public void setPuntuacion(double puntuacion) {
		if (puntuacion < 0 || puntuacion > 5) {
			throw new IllegalArgumentException("La puntuación debe ser un real entre 0.00 y 5.00");
		}
		this.puntuacion = puntuacion;
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
	 * Modifica la fecha de la reseña.
	 *
	 * @param fecha nueva fecha
	 */
	public void setFecha(DateTimeSimulado fecha) {
		this.fecha = fecha;
	}

	/**
	 * Devuelve el producto reseñado.
	 *
	 * @return producto
	 */
	public LineaProductoVenta getProducto() {
		return producto;
	}

	/**
	 * Modifica el producto reseñado.
	 *
	 * @param producto nuevo producto
	 */
	public void setProducto(LineaProductoVenta producto) {
		this.producto = producto;
	}
}
