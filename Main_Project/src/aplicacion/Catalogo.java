package aplicacion;

import java.util.*;
import descuento.*;
import categoria.*;
import producto.*;
import solicitud.*;
import filtro.*;
import java.io.File;

public class Catalogo {
	
	private static Catalogo instancia; 

	private Set<Descuento> descuentos = new HashSet<>();
	private Set<Categoria> categoriasTienda = new HashSet<>();
	private Set<LineaProductoVenta> productosNuevos = new HashSet<>();
	private Set<ProductoSegundaMano> productosSegundaMano = new HashSet<>();

	private FiltroVenta filtroProductosGestion;
	private FiltroIntercambio filtroProductosSegundaMano;
	private FiltroVentaCliente filtroProductosVenta;

	private Catalogo() {

	}
	
	public static Catalogo getInstancia() {
		if(instancia == null)
		{
			instancia = new Catalogo();
		}
		return instancia;
	}

	// Métodos para los productos
	public void añadirProducto(ProductoVenta p) {
	}

	public void añadirPack(ProductoVenta pack, List<ProductoVenta> prods) {
	}

	public void eliminarProducto(Producto p) {
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
