package usuario;

import producto.*;

import java.io.File;
import java.util.*;

import aplicacion.*;
import categoria.Categoria;

public abstract class UsuarioGestion extends Usuario {
	public UsuarioGestion(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	
	//Metodos para productos
	public void añadirProducto(String nombre, String descripcion, File foto, Integer stock, Double precio) {
		LineaProductoVenta producto = new LineaProductoVenta(nombre, descripcion, foto, stock, precio);
		Aplicacion.getInstancia().getCatalogo().añadirProducto(producto);
	}
	
	public void añadirPack(String nombre, String descripcion, File foto, Integer stock, Double precio, Map<LineaProductoVenta, Integer> prods) {
		LineaProductoVenta pack = new LineaProductoVenta(nombre, descripcion, foto, stock, precio);
		Aplicacion.getInstancia().getCatalogo().añadirPack(pack, prods);
	}
	
	public void eliminarProducto(LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().eliminarProducto(producto);
	}
	
	public void añadirProductosDesdeFichero(File f) {
		
	}
	
	public void modificarProducto() {
		
	}
	
	//Metodos para categorias 
	
	public void añadirCategoria(Categoria c) {
		
	}
	
	
	public void eliminarCategoria(Categoria c) {
		
	}
	
	public void modificarCategoria(Categoria c) {
		
	}
	
	public void aplicarDescuento() {
		
	}
	
	public void eliminarDescuento() {
		
	}
	
}

