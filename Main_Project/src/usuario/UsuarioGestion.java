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
	}

	public void añadirPack(String nombre, String descripcion, File foto, Integer stock, Double precio,
			Map<LineaProductoVenta, Integer> prods) {
		Pack pack = new Pack(nombre, descripcion, foto, stock, precio);
    pack.añadirProductosAPack(prods);
		Aplicacion.getInstancia().getCatalogo().añadirProducto(pack);
	}

	public void eliminarProducto(LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarProducto(producto);
	}

	public void añadirProductosDesdeFichero(File f) throws IOException, IllegalArgumentException {
		Aplicacion.getInstancia().getCatalogo().añadirProductosDesdeFichero(f);
	}

	public void modificarProducto(LineaProductoVenta p, String nuevoNombre, String nuevaDescripcion, Integer nuevoStock,
			Double nuevoPrecio, File nuevaFoto) {
		Aplicacion.getInstancia().getCatalogo().modificarProducto(p, nuevoNombre, nuevaDescripcion, nuevoStock,
				nuevoPrecio, nuevaFoto);
	}

	// Metodos para categorias

	public void añadirCategoria(String nombre) {
		Aplicacion.getInstancia().getCatalogo().añadirCategoria(new Categoria(nombre));
	}

	public void eliminarCategoria(Categoria c) {
		Aplicacion.getInstancia().getCatalogo().eliminarCategoria(c);
	}

	public void modificarCategoria(Categoria c, String nombreNuevo) {
		Aplicacion.getInstancia().getCatalogo().modificarCategoria(c, nombreNuevo);
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

	}

	public void eliminarDescuento(Descuento d) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d);
	}

	public void eliminarDescuentoProducto(Descuento d, LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d, producto);
	}

	public void eliminarDescuentoCategoria(Descuento d, Categoria categoria) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d, categoria);
	}

}
