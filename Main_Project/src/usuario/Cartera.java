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
	
	public List<ProductoSegundaMano> getProductosSegundaMano(){
		return Collections.unmodifiableList(productos);
	}
	@Override
	public String toString() {
		String total;
		return "Cartera [productos=" + productos + "]";
	}
	/**
	 * @return the productos
	 */
	public Set<ProductoSegundaMano> getProductos() {
		return productos;
	}
	
	

}
