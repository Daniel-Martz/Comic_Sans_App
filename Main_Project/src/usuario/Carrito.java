package usuario;
import java.util.*;

public class Carrito implements Contenedor<ProductoVenta> {
	private List<ProductoVenta> productos = new ArrayList<>(); 
	
	public void añadirProducto(ProductoVenta o) {
		this.productos.add(o);
	}
	
	public void eliminarProducto(ProductoVenta o) {
		this.productos.remove(o);
	}
}
