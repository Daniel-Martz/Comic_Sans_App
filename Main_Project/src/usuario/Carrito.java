package usuario;
import java.util.*;

import producto.*;

public class Carrito implements Contenedor<Map<LineaProductoVenta, Integer>> {
	private Map<LineaProductoVenta, Integer> productos = new HashMap<>(); 
	
	public void añadirProducto(LineaProductoVenta o, Integer cantidad) {
		this.productos.put(o, cantidad);
	}
	
	public void eliminarProducto(Map<LineaProductoVenta, Integer> o) {
		this.productos.remove(o);
	}
}
