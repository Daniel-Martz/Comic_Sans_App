package modelo.notificacion;

import modelo.producto.LineaProductoVenta;
import modelo.tiempo.DateTimeSimulado;

import java.util.*;

/**
 * Notificación sobre productos de venta dirigida a un cliente.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class NotificacionProducto extends NotificacionCliente {
	
	private static final long serialVersionUID = 1L;
	/** Conjunto de productos asociados a la notificación. */
	private Set<LineaProductoVenta> productos = new HashSet<>();

	/**
	 * Crea una notificación de producto.
	 *
	 * @param mensaje   texto de la notificación
	 * @param horaEnvio fecha y hora de envío
	 */
	public NotificacionProducto(String mensaje, DateTimeSimulado horaEnvio) {
		super(mensaje, horaEnvio);
	}

	/**
	 * Añade un producto a la notificación.
	 *
	 * @param producto el producto a añadir
	 */
	public void addProducto(LineaProductoVenta producto) {
		this.productos.add(producto);
	}

	/**
	 * Elimina un producto de la notificación.
	 *
	 * @param producto el producto a eliminar
	 */
	public void removeProducto(LineaProductoVenta producto) {
		this.productos.remove(producto);
	}

	/**
	 * Obtiene los productos de la notificación.
	 *
	 * @return conjunto de productos
	 */
	public Set<LineaProductoVenta> getProductos() {
		return productos;
	}

	/**
	 * Sustituye el conjunto de productos de la notificación.
	 *
	 * @param productos nuevo conjunto de productos
	 */
	public void setProductos(Set<LineaProductoVenta> productos) {
		this.productos = productos;
	}
	
	/**
	 * Devuelve el texto de la notificación con la lista de productos.
	 *
	 * @return texto representativo
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append("\nProductos: ");
		for (LineaProductoVenta p : productos) {
			sb.append("\n  - " + p.getNombre() + " (" + p.getPrecio() + " €)");
		}
		return sb.toString();
	}
}