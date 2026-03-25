package aplicacion;

import java.util.*;
import descuento.*;
import categoria.*;
import producto.*;
import solicitud.*;
import filtro.*;
import java.io.File;

public class Catalogo {

	private Set<Descuento> descuentos = new HashSet<>();
	private Set<Categoria> categoriasTienda = new HashSet<>();
	private Set<LineaProductoVenta> productosNuevos = new HashSet<>();
	private Set<ProductoSegundaMano> productosSegundaMano = new HashSet<>();

	private FiltroVenta filtroProductosGestion;
	private FiltroIntercambio filtroProductosSegundaMano;
	private FiltroVentaCliente filtroProductosVenta;

	public Catalogo() {

	}

	// Métodos para los productos
	public void añadirProducto(LineaProductoVenta p) {
		if(p == null) {
			throw new IllegalArgumentException("El producto introducido no es valido");
		}
		
		productosNuevos.add(p);
	}

	public void añadirPack(LineaProductoVenta pack, Map<LineaProductoVenta, Integer> prods) {
		if(pack == null) {
			throw new IllegalArgumentException("El pack introducido no es valido");
		}
		if(prods == null) {
			throw new IllegalArgumentException("La lista de productos introducidos no es válida");
		}
		
		pack.añadirProductosPack(prods);
		
		productosNuevos.add(pack);
	}

	public void eliminarProducto(Producto p) {
		productosNuevos.remove(p);
	}

	public void añadirProductosDesdeFichero(File f) {
	}

	// Métodos para las categorías
	public void añadirCategoria(Categoria c) {
	}

	public void eliminarCategoria(Categoria c) {
	}

	public void modificarCategoria(Categoria c, String nombreNuevo) {
	}

	// Métodos para los descuentos

	public void aplicarDescuento(Producto p, Descuento d) {
	}

	public void eliminarDescuento(Producto p, Descuento d) {
	}

	public void aplicarDescuento(Producto p, Categoria c) {
	}

	public void eliminarDescuento(Producto p, Categoria c) {
	}


	// Métodos filtros
	public void cambiarFiltroVenta(FiltroVentaCliente filtro) {
		this.filtroProductosVenta = filtro;
	}

	public void cambiarFiltroGestion(FiltroVenta filtro) {
		this.filtroProductosGestion = filtro;
	}

	public void cambiarFiltroIntercambio(FiltroIntercambio filtro) {
		this.filtroProductosSegundaMano = filtro;
	}

	// Métodos de búsqueda
	public List<LineaProductoVenta> obtenerProductosNuevosFiltrados(String prompt) {
		return new ArrayList<>();
	}

	public List<ProductoSegundaMano> obtenerProductosIntercambioFiltrados(String prompt) {
		return new ArrayList<>();
	}

	public List<LineaProductoVenta> obtenerProductosAModificarFiltrados(String prompt) {
		return new ArrayList<>();
	}
}
