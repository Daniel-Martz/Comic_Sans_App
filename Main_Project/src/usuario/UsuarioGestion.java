package usuario;

import producto.*;
import tiempo.DateTimeSimulado;

import java.io.File;
import java.io.IOException;
import java.util.*;

import aplicacion.*;
import categoria.Categoria;
import descuento.Descuento;

public abstract class UsuarioGestion extends Usuario {
	public UsuarioGestion(String username, String DNI, String password) {
		super(username, DNI, password);
	}

	// Metodos para productos
	public void añadirProducto(String nombre, String descripcion, File foto, Integer stock, Double precio) {
		LineaProductoVenta producto = new LineaProductoVenta(nombre, descripcion, foto, stock, precio);
		Aplicacion.getInstancia().getCatalogo().añadirProducto(producto);
		System.out.println("Producto añadido al catálogo: " + nombre);
	}

	public void añadirPack(String nombre, String descripcion, File foto, Integer stock, Double precio,
			Map<LineaProductoVenta, Integer> prods) {
		Pack pack = new Pack(nombre, descripcion, foto, stock, precio);
    pack.añadirProductosAPack(prods);
		Aplicacion.getInstancia().getCatalogo().añadirProducto(pack);
		System.out.println("Pack añadido al catálogo: " + nombre);
	}

	public void eliminarProducto(LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarProducto(producto);
		System.out.println("Producto eliminado del catálogo.");
	}

	public void añadirProductosDesdeFichero(File f) throws IOException, IllegalArgumentException {
		Aplicacion.getInstancia().getCatalogo().añadirProductosDesdeFichero(f);
		System.out.println("Productos importados desde fichero correctamente.");
	}

	public void modificarProducto(LineaProductoVenta p, String nuevoNombre, String nuevaDescripcion, Integer nuevoStock,
			Double nuevoPrecio, File nuevaFoto) {
		Aplicacion.getInstancia().getCatalogo().modificarProducto(p, nuevoNombre, nuevaDescripcion, nuevaFoto, nuevoStock,
				nuevoPrecio );
		System.out.println("Producto modificado correctamente.");
	}

	// Metodos para categorias

	public void añadirCategoria(String nombre) {
		Aplicacion.getInstancia().getCatalogo().añadirCategoria(new Categoria(nombre));
		System.out.println("Categoría añadida: " + nombre);
	}

	public void eliminarCategoria(Categoria c) {
		Aplicacion.getInstancia().getCatalogo().eliminarCategoria(c);
		System.out.println("Categoría eliminada.");
	}

	public void modificarCategoria(Categoria c, String nombreNuevo) {
		Aplicacion.getInstancia().getCatalogo().modificarCategoria(c, nombreNuevo);
		System.out.println("Categoría modificada correctamente.");
	}

	// Metodos para descuentos

	public void aplicarDescuento(Descuento d, Set<LineaProductoVenta> productos, Set<Categoria> categorias) {
		if (categorias != null && !categorias.isEmpty()) {
			for (Categoria cat : categorias) {
				Aplicacion.getInstancia().getCatalogo().aplicarDescuento(d, cat);
			}
		}

		if (productos != null && !productos.isEmpty()) {
			for (LineaProductoVenta prod : productos) {
				Aplicacion.getInstancia().getCatalogo().aplicarDescuento(prod, d);
			}
		}
		System.out.println("Descuento aplicado correctamente.");
	}

	public void eliminarDescuento(Descuento d) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d);
		System.out.println("Descuento general eliminado.");
	}

	public void eliminarDescuentoProducto(Descuento d, LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d, producto);
		System.out.println("Descuento eliminado del producto.");
	}

	public void eliminarDescuentoCategoria(Descuento d, Categoria categoria) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d, categoria);
		System.out.println("Descuento eliminado de la categoría.");
	}

}
