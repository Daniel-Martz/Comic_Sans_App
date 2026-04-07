package descuento;

import tiempo.*;
import java.util.Set;
import java.io.Serializable;
import java.util.HashSet;
import categoria.Categoria;
import producto.LineaProductoVenta;
import solicitud.Caducable;

/**
 * Clase abstracta base que representa un descuento aplicable en el sistema.
 * Implementa la interfaz Caducable para controlar su periodo de validez.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class Descuento implements Caducable, Serializable{
	
	/** Fecha en la que el descuento entra en vigor. */
	private DateTimeSimulado fechaInicio;
	
	/** Fecha en la que el descuento deja de ser válido. */
	private DateTimeSimulado fechaFin;
	
	/** Conjunto de categorías a las que se aplica este descuento. */
	private Set<Categoria> categoriasRebajadas = new HashSet<Categoria>();
	
	/** Conjunto de productos concretos a los que se aplica este descuento. */
	private Set<LineaProductoVenta> productosRebajados = new HashSet<LineaProductoVenta>();
	
	/**
	 * Crea un descuento definiendo su periodo de validez.
	 *
	 * @param fechaInicio la fecha de inicio del descuento
	 * @param fechaFin    la fecha de fin del descuento
	 */
	public Descuento(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	/**
	 * Añade una categoría a la lista de categorías afectadas por el descuento.
	 *
	 * @param categ la categoría a añadir
	 */
	public void añadirCategoria(Categoria categ) {
		this.categoriasRebajadas.add(categ);
	}

	/**
	 * Elimina una categoría de la lista de categorías rebajadas.
	 *
	 * @param categ la categoría a eliminar
	 */
	public void eliminarCategoria(Categoria categ) {
		this.categoriasRebajadas.remove(categ);
	}
	
	/**
	 * Añade un producto concreto a la lista de productos rebajados.
	 *
	 * @param producto el producto a añadir
	 */
	public void añadirProductoRebajado(LineaProductoVenta producto) {
		this.productosRebajados.add(producto);
	}

	/**
	 * Elimina un producto de la lista de productos rebajados.
	 *
	 * @param producto el producto a eliminar
	 */
	public void eliminarProductoRebajado(LineaProductoVenta producto) {
		this.productosRebajados.remove(producto);
	}

	/**
	 * Obtiene la fecha de inicio del descuento.
	 *
	 * @return la fecha de inicio
	 */
	public DateTimeSimulado getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * Obtiene la fecha de fin del descuento.
	 *
	 * @return la fecha límite
	 */
	public DateTimeSimulado getFechaFin() {
		return fechaFin;
	}

	/**
	 * Obtiene el conjunto de productos que tienen este descuento.
	 *
	 * @return los productos rebajados
	 */
	public Set<LineaProductoVenta> getProductosRebajados() {
		return productosRebajados;
	}

	/**
	 * Obtiene el conjunto de categorías que tienen este descuento.
	 *
	 * @return las categorías rebajadas
	 */
	public Set<Categoria> getCategoriasRebajadas() {
		return categoriasRebajadas;
	}
	
	/**
	 * Comprueba si el descuento ha caducado comparando su fecha límite con la fecha actual.
	 *
	 * @return true si el descuento ya no es válido, false en caso contrario
	 */
	public boolean haCaducado(){
		//Si la fecha de caducidad es anterior a la actual, el descuento ha caducado
		if(this.fechaFin.compareTo(new DateTimeSimulado()) < 0){
			return true;
		}else{
			return false;
		}
	}
}
