package modelo.usuario;

import java.io.Serializable;
import java.util.*;

import modelo.producto.ProductoSegundaMano;

/**
 * Implementa la clase Cartera.
 * Representa el conjunto de productos de segunda mano que un cliente 
 * tiene disponibles para realizar intercambios.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Cartera implements Serializable{
	
	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;
	/**Set de productos de la cartera*/
	private Set<ProductoSegundaMano> productos = new HashSet<>(); 
	
	/**
	 * Añade un producto de segunda mano a la cartera.
	 *
	 * @param o el producto a añadir
	 */
	public void añadirProducto(ProductoSegundaMano o) {
		this.productos.add(o);
	}
	
	/**
	 * Elimina un producto de segunda mano de la cartera.
	 *
	 * @param o el producto a eliminar
	 */
	public void eliminarProducto(ProductoSegundaMano o) {
		this.productos.remove(o);
	}
	
	/**
	 * Obtiene los productos almacenados en la cartera.
	 * Devuelve un conjunto de solo lectura para proteger los datos internos.
	 *
	 * @return el conjunto inmodificable de productos
	 */
	public Set<ProductoSegundaMano> getProductos(){
		return Collections.unmodifiableSet(productos);
	}
	
	/**
	 * Devuelve una representación en formato texto de la cartera.
	 *
	 * @return la cadena de texto con la información de la cartera
	 */
	@Override
	public String toString() {
		return "Cartera [productos=" + productos + "]";
	}
	
}