package producto;

import java.util.*;

import aplicacion.Aplicacion;

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

	public void añadirCategoria(Categoria categoria) {
		this.categorias.add(categoria);
	}

	public void eliminarCategoria(Categoria categoria) {
		this.categorias.remove(categoria);
	}

	public void añadirReseña(Reseña reseña) {
		this.reseña.add(reseña);
		Aplicacion.getInstancia().getConfiguracionRecomendacion().actualizarRankingValoracion(this);
	}

	public void eliminarReseña(Reseña reseña) {
		this.reseña.remove(reseña);
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
	
	@Override
	public String toString() {
	    return "ID: " + getID() + " | " + getNombre() + "\n" +
	           "Descripción: " + getDescripcion() + "\n" +
	           "Precio: " + String.format("%.2f €", precio) + 
	           (descuento != null ? " (Con descuento activo)" : "") + "\n" +
	           "Stock disponible: " + stock + "\n" +
	           "Valoración media: " + String.format("%.1f/5.0", obtenerPuntuacionMedia()) + 
	           " (" + reseña.size() + " reseñas)\n" +
	           "Ventas: " + unidadesVendidas + " unidades\n" +
	           "Fecha de subida: " + getFechaSubida().toStringFecha() + "\n";
	}

}
