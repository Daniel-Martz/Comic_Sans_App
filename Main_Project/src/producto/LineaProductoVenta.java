package producto;

import java.util.*;
import java.io.File;
//import Descuento;
//import Reseña;
//import Categoria;

public class LineaProductoVenta extends Producto {
	private List<Reseña> reseña = new ArrayList<>();
	private Set<Categoria> categorias = new HashSet<>();
	private List<ProductoVenta> productos = new ArrayList<>();
	
	private int stock;
	private double precio;
	private int unidadesVendidas;
	private Descuento descuento;

	
	public LineaProductoVenta(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas)
	{
		super(nombre, descripcion, foto);
		this.stock = stock;
		this.precio = precio;
		this.unidadesVendidas = unidadesVendidas;
	
	}

	public void añadirCategoria(Categoria categoria) {
		
	}
	
}
