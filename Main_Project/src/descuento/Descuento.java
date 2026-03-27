package descuento;

import java.time.DateTimeSimulado;
import java.util.Set;
import java.util.HashSet;
import categoria.Categoria;
import producto.LineaProductoVenta;

public abstract class Descuento {
	private DateTimeSimulado fechaInicio;
	private DateTimeSimulado fechaFin;
	private Set<Categoria> categoriasRebajadas = new HashSet<Categoria>();
	private Set<LineaProductoVenta> productosRebajados = new HashSet<LineaProductoVenta>();
	
	public Descuento(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	
	public void añadirCategoria(Categoria categ) {
		this.categoriasRebajadas.add(categ);
	}

	public void eliminarCategoria(Categoria categ) {
		this.categoriasRebajadas.remove(categ);
	}
	
	public void añadirProductoRebajado(LineaProductoVenta producto) {
		this.productosRebajados.add(producto);
	}

	public void eliminarProductoRebajado(LineaProductoVenta producto) {
		this.productosRebajados.remove(producto);
	}


	public DateTimeSimulado getFechaInicio() {
		return fechaInicio;
	}


	public DateTimeSimulado getFechaFin() {
		return fechaFin;
	}


	public Set<LineaProductoVenta> getProductosRebajados() {
		return productosRebajados;
	}


	public Set<Categoria> getCategoriasRebajadas() {
		return categoriasRebajadas;
	}
	


}
