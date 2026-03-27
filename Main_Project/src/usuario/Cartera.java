package usuario;
import java.util.*;

import producto.ProductoSegundaMano;


public class Cartera implements Contenedor<ProductoSegundaMano> {
	private List<ProductoSegundaMano> productos = new ArrayList<>(); 
	
	public void añadirProducto(ProductoSegundaMano o) {
		this.productos.add(o);
	}
	
	public void eliminarProducto(ProductoSegundaMano o) {
		this.productos.remove(o);
	}
}
