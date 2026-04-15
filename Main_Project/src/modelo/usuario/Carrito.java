package modelo.usuario;

import java.io.Serializable;
import java.util.*;

import modelo.producto.*;

/**
 * Implementa la clase Carrito. Gestiona los productos que un cliente desea
 * comprar y sus cantidades.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Carrito implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/** Mapa que asocia cada producto con su cantidad */
	private Map<LineaProductoVenta, Integer> productos = new HashMap<>();

	/**
	 * Añade una cantidad de un producto al carrito. Comprueba que haya stock
	 * suficiente contando lo que ya hay en el carrito.
	 *
	 * @param o        el producto a añadir
	 * @param cantidad la cantidad a añadir
	 * @throws IllegalStateException si no hay stock suficiente
	 */
	public void añadirProducto(LineaProductoVenta o, Integer cantidad) {
		if (o == null || cantidad < 0) {
			throw new IllegalArgumentException("Los argumentos introducidos no son validos");
		}
		/* Se suma la cantidad que ya hay en el carrito para comprobar el stock real */
		int cantidadTotalDeseada = cantidad + this.productos.getOrDefault(o, 0);

		if (cantidadTotalDeseada > o.getStock()) {
			throw new IllegalStateException("No hay suficiente stock de ese producto");
		}

		/*
		 * Usando esta función si existe la clave ya suma la cantidad, si no existe la
		 * crea
		 */
		this.productos.merge(o, cantidad, Integer::sum);
	}

	/**
	 * Elimina una cantidad específica de un producto del carrito. Si la cantidad a
	 * eliminar es mayor o igual a la que hay, se retira el producto.
	 *
	 * @param p        el producto a eliminar
	 * @param cantidad la cantidad a retirar
	 * @throws IllegalArgumentException si el producto es nulo o la cantidad
	 *                                  negativa
	 */
	public void eliminarProducto(LineaProductoVenta p, Integer cantidad) {
		if (p == null || cantidad < 0) {
			throw new IllegalArgumentException("Los argumentos introducidos no son validos");
		}

		int cantidadActual = this.productos.getOrDefault(p, 0);
		if (cantidadActual <= cantidad) {
			this.productos.remove(p);
		} else {
			this.productos.put(p, cantidadActual - cantidad);
		}
	}

	/**
	 * Vacía completamente el carrito eliminando todos los productos.
	 */
	public void vaciarCarrito() {
		this.productos.clear();
	}

	/**
	 * Calcula el precio total de todos los productos en el carrito.
	 *
	 * @return el precio total
	 */
	public double getPrecioProductos() {
		double precio = 0;
		for (Map.Entry<LineaProductoVenta, Integer> entrada : this.productos.entrySet()) {
			precio += entrada.getKey().getPrecio() * entrada.getValue();
		}
		return precio;
	}

	/**
	 * Obtiene el mapa de productos añadidos al carrito.
	 *
	 * @return el mapa con los productos y sus cantidades
	 */
	public Map<LineaProductoVenta, Integer> getProductos() {
		return productos;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("========== CARRITO DE COMPRA ==========\n");
	    
	    if (productos.isEmpty()) {
	        sb.append("El carrito está vacío.\n");
	    } else {
	        // Recorremos el mapa de productos para listar cada ítem
	        for (Map.Entry<LineaProductoVenta, Integer> entrada : productos.entrySet()) {
	            LineaProductoVenta p = entrada.getKey();
	            int cantidad = entrada.getValue();
	            double subtotal = p.getPrecio() * cantidad;

	            sb.append(String.format("- %-20s | Cantidad: %2d | Precio Un.: %6.2f € | Subtotal: %6.2f €\n", 
	                      p.getNombre(), cantidad, p.getPrecio(), subtotal));
	        }
	        
	        sb.append("---------------------------------------\n");
	        // Mostramos el precio total calculado
	        sb.append(String.format("TOTAL CARRITO: %29.2f €\n", getPrecioProductos()));
	    }
	    sb.append("=======================================");
	    
	    return sb.toString();
	}
	
}
