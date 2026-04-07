package producto;

import java.util.*;
import aplicacion.Aplicacion;
import java.io.File;
import categoria.Categoria;
import descuento.*;

/**
 * Representa un producto de venta directa dentro de la aplicación.
 * 
 * Incluye stock, precio, categorías, reseñas, ventas y descuentos.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class LineaProductoVenta extends Producto {

	protected List<Reseña> reseña = new ArrayList<>();
	protected Set<Categoria> categorias = new HashSet<>();

	protected int stock;
	protected double precio;
	protected int unidadesVendidas;
	protected Descuento descuento;

	/**
	 * Construye una línea de producto de venta.
	 *
	 * @param nombre nombre del producto
	 * @param descripcion descripción del producto
	 * @param foto imagen asociada
	 * @param stock unidades disponibles
	 * @param precio precio unitario
	 */
	public LineaProductoVenta(String nombre, String descripcion, File foto, int stock, double precio) {
		super(nombre, descripcion, foto);
		this.stock = stock;
		this.precio = precio;
		this.unidadesVendidas = 0;
	}

	/**
	 * Añade una categoría al producto.
	 *
	 * @param categoria categoría a añadir
	 */
	public void añadirCategoria(Categoria categoria) {
		this.categorias.add(categoria);
	}

	/**
	 * Elimina una categoría del producto.
	 *
	 * @param categoria categoría a eliminar
	 */
	public void eliminarCategoria(Categoria categoria) {
		this.categorias.remove(categoria);
	}

	/**
	 * Añade una reseña al producto y actualiza ranking.
	 *
	 * @param reseña reseña a añadir
	 */
	public void añadirReseña(Reseña reseña) {
		this.reseña.add(reseña);
		Aplicacion.getInstancia().getConfiguracionRecomendacion().actualizarRankingValoracion(this);
	}

	/**
	 * Elimina una reseña del producto.
	 *
	 * @param reseña reseña a eliminar
	 */
	public void eliminarReseña(Reseña reseña) {
		this.reseña.remove(reseña);
	}

	/**
	 * Calcula la puntuación media de reseñas.
	 *
	 * @return puntuación media o 0 si no hay reseñas
	 */
	public double obtenerPuntuacionMedia() {
		if (this.reseña.isEmpty()) {
			return 0.0;
		}

		double total = 0;
		for (Reseña r : reseña) {
			total += r.getPuntuacion();
		}

		return total / this.reseña.size();
	}

	/**
	 * Devuelve el stock disponible.
	 *
	 * @return stock
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * Modifica el stock.
	 *
	 * @param stock nuevo stock
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * Devuelve el precio.
	 *
	 * @return precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * Modifica el precio.
	 *
	 * @param precio nuevo precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * Devuelve el descuento activo.
	 *
	 * @return descuento
	 */
	public Descuento getDescuento() {
		return descuento;
	}

	/**
	 * Asigna un descuento.
	 *
	 * @param descuento descuento nuevo
	 */
	public void setDescuento(Descuento descuento) {
		this.descuento = descuento;
	}

	/**
	 * Devuelve las reseñas del producto.
	 *
	 * @return lista de reseñas
	 */
	public List<Reseña> getReseña() {
		return reseña;
	}

	/**
	 * Devuelve las categorías del producto.
	 *
	 * @return conjunto de categorías
	 */
	public Set<Categoria> getCategorias() {
		return categorias;
	}

	/**
	 * Devuelve unidades vendidas.
	 *
	 * @return unidades vendidas
	 */
	public int getUnidadesVendidas() {
		return unidadesVendidas;
	}

	@Override
	public String toString() {
		return "ID: " + getID() + " | " + getNombre() + "\n" +
				"Descripción: " + getDescripcion() + "\n" +
				"Precio: " + String.format("%.2f €", precio) +
				(descuento != null ? " (Con descuento activo)" : "") + "\n" +
				"Stock disponible: " + stock + "\n" +
				"Valoración media: " + String.format("%.1f/5.0", obtenerPuntuacionMedia()) +
				" (" + reseña.size() + " reseñas)\n" +
				"Ventas: " + unidadesVendidas + " unidades\n" +
				"Fecha de subida: " + getFechaSubida().toStringFecha() + "\n";
	}
}
