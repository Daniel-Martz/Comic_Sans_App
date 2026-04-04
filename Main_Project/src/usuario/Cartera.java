package usuario;
import java.util.*;

import producto.ProductoSegundaMano;


public class Cartera{
	private Set<ProductoSegundaMano> productos = new HashSet<>(); 
	
	public void añadirProducto(ProductoSegundaMano o) {
		this.productos.add(o);
	}
	public void eliminarProducto(ProductoSegundaMano o) {
		this.productos.remove(o);
	}
	
	public Set<ProductoSegundaMano> getProductos(){
		return Collections.unmodifiableSet(productos);
	}
	@Override
	public String toString() {
		return "Cartera [productos=" + productos + "]";
	}
	
}
