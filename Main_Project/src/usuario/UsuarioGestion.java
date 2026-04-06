package usuario;

import producto.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import aplicacion.*;
import categoria.Categoria;
import descuento.Descuento;

/**
 * Implementa la clase UsuarioGestion.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public abstract class UsuarioGestion extends Usuario {

	/**
	 * Instancia un nuevo usuario gestion.
	 *
	 * @param username el nombre de usuario del nuevo usuario
	 * @param DNI      el DNI
	 * @param password la contraseña
	 */
	public UsuarioGestion(String username, String DNI, String password) {
		super(username, DNI, password);
	}

	// Metodos para productos

	/**
	 * Añade un producto.
	 *
	 * @param nombre      el nombre
	 * @param descripcion la descripción
	 * @param foto        la foto
	 * @param stock       el stock
	 * @param precio      el precio
	 */
	public LineaProductoVenta añadirProducto(String nombre, String descripcion, File foto, Integer stock,
			Double precio) {
		LineaProductoVenta producto = new LineaProductoVenta(nombre, descripcion, foto, stock, precio);
		Aplicacion.getInstancia().getCatalogo().añadirProducto(producto);
		System.out.println("Producto añadido al catálogo: " + nombre);
		return producto;
	}

	/**
	 * Añade un pack.
	 *
	 * @param nombre      el nombre
	 * @param descripcion la descripción
	 * @param foto        la foto
	 * @param stock       el stock
	 * @param precio      el precio
	 * @param prods       los productos
	 */
	public Pack añadirPack(String nombre, String descripcion, File foto, Integer stock, Double precio,
			Map<LineaProductoVenta, Integer> prods) {
		Pack pack = new Pack(nombre, descripcion, foto, stock, precio);
		pack.añadirProductosAPack(prods);
		Aplicacion.getInstancia().getCatalogo().añadirProducto(pack);
		System.out.println("Pack añadido al catálogo: " + nombre);
		return pack;
	}

	/**
	 * Elimina un producto.
	 *
	 * @param producto el producto
	 */
	public void eliminarProducto(LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarProducto(producto);
		System.out.println("Producto eliminado del catálogo.");
	}

	/**
	 * Añade productos desde fichero.
	 *
	 * @param f el fichero
	 * @throws IOException              si hay un error de entrada/salida
	 * @throws IllegalArgumentException si el argumento no es válido
	 */
	public List<LineaProductoVenta> añadirProductosDesdeFichero(File f) throws IOException, IllegalArgumentException {
		List<LineaProductoVenta> productos = Aplicacion.getInstancia().getCatalogo().añadirProductosDesdeFichero(f);
		System.out.println("Productos importados desde fichero correctamente.");
		return productos;
	}

	/**
	 * Modifica un producto.
	 *
	 * @param p                el producto
	 * @param nuevoNombre      el nuevo nombre
	 * @param nuevaDescripcion la nueva descripción
	 * @param nuevoStock       el nuevo stock
	 * @param nuevoPrecio      el nuevo precio
	 * @param nuevaFoto        la nueva foto
	 */
	public void modificarProducto(LineaProductoVenta p, String nuevoNombre, String nuevaDescripcion, Integer nuevoStock,
			Double nuevoPrecio, File nuevaFoto) {
		Aplicacion.getInstancia().getCatalogo().modificarProducto(p, nuevoNombre, nuevaDescripcion, nuevaFoto,
				nuevoStock, nuevoPrecio);
		System.out.println("Producto modificado correctamente.");
	}

	// Metodos para categorias

	/**
	 * Añade una categoria.
	 *
	 * @param nombre el nombre
	 */
	public void añadirCategoria(String nombre) {
		Aplicacion.getInstancia().getCatalogo().añadirCategoria(new Categoria(nombre));
		System.out.println("Categoría añadida: " + nombre);
	}

	/**
	 * Elimina una categoria.
	 *
	 * @param c la categoría
	 */
	public void eliminarCategoria(Categoria c) {
		Aplicacion.getInstancia().getCatalogo().eliminarCategoria(c);
		System.out.println("Categoría eliminada.");
	}

	/**
	 * Modifica una categoria.
	 *
	 * @param c           la categoría
	 * @param nombreNuevo el nuevo nombre
	 */
	public void modificarCategoria(Categoria c, String nombreNuevo) {
		Aplicacion.getInstancia().getCatalogo().modificarCategoria(c, nombreNuevo);
		System.out.println("Categoría modificada correctamente.");
	}

	// Metodos para descuentos

	/**
	 * Aplica un descuento.
	 *
	 * @param d          el descuento
	 * @param productos  los productos
	 * @param categorias las categorías
	 */
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

	/**
	 * Elimina un descuento general.
	 *
	 * @param d el descuento
	 */
	public void eliminarDescuento(Descuento d) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d);
		System.out.println("Descuento general eliminado.");
	}

	/**
	 * Elimina el descuento de un producto.
	 *
	 * @param d        el descuento
	 * @param producto el producto
	 */
	public void eliminarDescuentoProducto(Descuento d, LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d, producto);
		System.out.println("Descuento eliminado del producto.");
	}

	/**
	 * Elimina el descuento de una categoria.
	 *
	 * @param d         el descuento
	 * @param categoria la categoría
	 */
	public void eliminarDescuentoCategoria(Descuento d, Categoria categoria) {
		Aplicacion.getInstancia().getCatalogo().eliminarDescuento(d, categoria);
		System.out.println("Descuento eliminado de la categoría.");
	}

}
