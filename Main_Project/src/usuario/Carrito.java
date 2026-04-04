package usuario;
import java.util.*;

import producto.*;

public class Carrito implements Contenedor<LineaProductoVenta> {
	private Map<LineaProductoVenta, Integer> productos = new HashMap<>(); 
	private Map<LineaProductoVenta, Double> recaudacionProductos = new HashMap<>(); 
	
	@Override
	public void añadirProducto(LineaProductoVenta p, Integer cantidad) {
    if(p == null || cantidad < 0){
      throw new IllegalArgumentException("Los argumentos introducidos no son validos");
    }
		//Usando esta función si existe la clave ya suma la cantidad, si no existe la crea
	    this.productos.merge(p, cantidad, Integer::sum);
	}
	
	@Override
	public void eliminarProducto(LineaProductoVenta p, Integer cantidad) {
    if(p == null || cantidad < 0){
      throw new IllegalArgumentException("Los argumentos introducidos no son validos");
    }
    int cantidadActual = this.productos.getOrDefault(p, 0);
    if (cantidadActual <= cantidad) {
        this.productos.remove(p);
    } else {
        this.productos.put(p, cantidadActual - cantidad);
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
		if(productos.isEmpty()) {
	        throw new IllegalStateException("Este carrito no contiene productos");
		}
		return productos;
	}
	
}
	

