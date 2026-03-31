package producto;

import java.util.*;
import java.io.File;
//import Descuento;
import categoria.Categoria;
import filtro.FiltroVenta;
import descuento.*;

public class LineaProductoVenta extends Producto {
	protected List<Reseña> reseña = new ArrayList<>();
	protected Set<Categoria> categorias = new HashSet<>();
	
	protected int stock;
	protected double precio;
	protected int unidadesVendidas;
	protected Descuento descuento;

	public LineaProductoVenta(String nombre, String descripcion, File foto, int stock, double precio) {
		super(nombre, descripcion, foto);
		this.stock = stock;
		this.precio = precio;
		unidadesVendidas = 0;

	}

	public boolean añadirCategoria(Categoria categoria) {
		return this.categorias.add(categoria);
	}

	public boolean eliminarCategoria(Categoria categoria) {
		return this.categorias.remove(categoria);
	}

	public boolean añadirReseña(Reseña reseña) {
		return this.reseña.add(reseña);
	}

	public boolean eliminarReseña(Reseña reseña) {
		return this.reseña.remove(reseña);
	}
	
	public double obtenerPuntuacionMedia()
	{
		if(this.reseña.isEmpty())
		{
			return 0.0;
		}

		double total = 0;
		for (Reseña r : reseña) {
			total += r.getPuntuacion();
		}
		return total / this.reseña.size();
	}

	// public boolean pasaFiltro(Filtro filtro, String prompt)

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Descuento getDescuento() {
		return descuento;
	}

	public void setDescuento(Descuento descuento) {
		this.descuento = descuento;
	}

	public List<Reseña> getReseña() {
		return reseña;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public int getUnidadesVendidas() {
		return unidadesVendidas;
	}

}
