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
		if (instancia == null) {
			instancia = new Catalogo();
		}
		return instancia;
	}

	//Métodos para los descuentos
	public void añadirDescuento(Descuento d)
	{
		if (d == null) {
			throw new IllegalArgumentException("El descuento introducido no es valido");
		}

		descuentos.add(d);
	}
	
	public void eliminarDescuento(Descuento d) {
		descuentos.remove(d);
	}
	
	// Métodos para los productos
	public void añadirProducto(LineaProductoVenta p) {
		if (p == null) {
			throw new IllegalArgumentException("El producto introducido no es valido");
		}

		productosNuevos.add(p);
	}

	public void añadirPack(LineaProductoVenta pack, Map<LineaProductoVenta, Integer> prods) {
		if (pack == null) {
			throw new IllegalArgumentException("El pack introducido no es valido");
		}
		if (prods == null) {
			throw new IllegalArgumentException("La lista de productos introducidos no es válida");
		}

		pack.añadirProductosPack(prods);

		productosNuevos.add(pack);
	}

	public void eliminarProducto(Producto p) {
		productosNuevos.remove(p);
	}

	// falta
	// esta::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public void añadirProductosDesdeFichero(File f) {
	}

	// Métodos para las categorías
	public void añadirCategoria(Categoria c) {
		if (c == null) {
			return;
		}
		categoriasTienda.add(c);
	}

	public void eliminarCategoria(Categoria c) {
		if (c == null) {
			return;
		}
		categoriasTienda.remove(c);
	}

	public void modificarCategoria(Categoria c, String nombreNuevo) {
		if (c == null || nombreNuevo == null || nombreNuevo.trim().isEmpty()) {
			return;
		}
		if (categoriasTienda.contains(c)) {
			c.setNombre(nombreNuevo);
		}
	}

	// Métodos para los descuentos
	public void aplicarDescuento(LineaProductoVenta p, Descuento d) {
		if (p == null || d == null) {
			throw new IllegalArgumentException("El producto y el descuento no pueden ser nulos.");
		}

		if (!productosNuevos.contains(p) || !descuentos.contains(d)) {
			throw new IllegalStateException("El producto y el descuento debe perternecer al catalogo");
		}

		// Un producto no puede tener más de un descuento activo
		if (p.getDescuento() != null) {
			throw new IllegalStateException("El producto '" + p.getNombre() + "' ya tiene un descuento activo.");
		}

		// Si alguna categoría del producto ya tiene descuento, hay conflicto
		for (Categoria cat : p.getCategorias()) {
			if (cat.getDescuento() != null) {
				throw new IllegalStateException("El producto '" + p.getNombre() + "' pertenece a la categoría '"
						+ cat.getNombre() + "' que ya tiene un descuento. No se pueden acumular descuentos.");
			}
		}
		p.setDescuento(d);
		d.añadirProductoRebajado(p);
	}

	public void eliminarDescuento(LineaProductoVenta p, Descuento d) {
		if (p == null || d == null) {
			throw new IllegalArgumentException("El producto y el descuento no pueden ser nulos.");
		}

		if (!productosNuevos.contains(p) || !descuentos.contains(d)) {
			throw new IllegalStateException("El producto y el descuento debe perternecer al catalogo");
		}

		if (p.getDescuento() == null) {
			throw new IllegalStateException("El producto '" + p.getNombre() + "' no tiene ningún descuento activo.");
		}

		if (!p.getDescuento().equals(d)) {
			throw new IllegalStateException(
					"El descuento indicado no coincide con el descuento activo del producto '" + p.getNombre() + "'.");
		}

		p.setDescuento(null);
		d.eliminarProductoRebajado(p);
	}

	public void aplicarDescuento(Descuento d, Categoria c) {
		if (d == null || c == null) {
			throw new IllegalArgumentException("El descuento y la categoría no pueden ser nulos.");
		}

		if (!categoriasTienda.contains(c) || !descuentos.contains(d)) {
			throw new IllegalStateException("El descuento y la categoria debe perternecer al catalogo");
		}
		
		if (c.getDescuento() != null) {
			throw new IllegalStateException("La categoría '" + c.getNombre() + "' ya tiene un descuento activo.");
		}

		// Comprobamos que ningún producto de la categoría tenga ya un descuento individual
		for (LineaProductoVenta p : c.obtenerProductosCategoria()) {
			if (p.getDescuento() != null) {
				throw new IllegalStateException("El producto '" + p.getNombre() + "' de la categoría '" + c.getNombre()
						+ "' ya tiene un descuento individual. No se pueden acumular descuentos.");
			}
		}
		c.añadirDescuento(d);
		d.añadirCategoria(c);
	}

	public void eliminarDescuento(Descuento d, Categoria c) {
		if (d == null || c == null) {
			throw new IllegalArgumentException("El descuento y la categoría no pueden ser nulos.");
		}

		if (!categoriasTienda.contains(c) || !descuentos.contains(d)) {
			throw new IllegalStateException("El descuento y la categoria debe perternecer al catalogo");
		}
		
		if (c.getDescuento() == null) {
			throw new IllegalStateException("La categoría '" + c.getNombre() + "' no tiene ningún descuento activo.");
		}

		if (!c.getDescuento().equals(d)) {
			throw new IllegalStateException(
					"El descuento indicado no coincide con el descuento activo de la categoría '" + c.getNombre()
							+ "'.");
		}
		c.eliminarDescuento(d);
		d.eliminarCategoria(c);
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
