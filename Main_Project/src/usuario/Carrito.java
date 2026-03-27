package usuario;
import java.util.*;

import producto.*;

public class Carrito implements Contenedor<LineaProductoVenta> {
	private Map<LineaProductoVenta, Integer> productos = new HashMap<>(); 
	
	@Override
	public void añadirProducto(LineaProductoVenta o, Integer cantidad) {
		//Usando esta función si existe la clave ya suma la cantidad, si no existe la crea
	    this.productos.merge(o, cantidad, Integer::sum);
	}
	
	@Override
	public void eliminarProducto(LineaProductoVenta o, Integer cantidad) {
	    int cantidadActual = this.productos.getOrDefault(o, 0);
	    if (cantidadActual <= cantidad) {
	        this.productos.remove(o);
	    } else {
	        this.productos.put(o, cantidadActual - cantidad);
	    }
	}
	
	public void vaciarCarrito() {
		this.productos.clear();
	}
	
	public double getPrecioProductos() {
	    double precio = 0;
	    for (Map.Entry<LineaProductoVenta, Integer> entrada : this.productos.entrySet()) {
	        precio += entrada.getKey().getPrecio() * entrada.getValue();
	    }
	    return precio;
	}

	/**
	 * @return the productos
	 */
	public Map<LineaProductoVenta, Integer> getProductos() {
		return productos;
	}
	
}
	

