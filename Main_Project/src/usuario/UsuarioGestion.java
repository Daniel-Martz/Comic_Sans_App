package usuario;

import producto.*;

import java.util.*;

import aplicacion.*;

public abstract class UsuarioGestion extends Usuario {
	public UsuarioGestion(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	public void añadirProducto(LineaProductoVenta producto) {
		Aplicacion.getInstancia().getCatalogo().añadirProducto(producto);
	}
	
	public void añadirPack(LineaProductoVenta pack, List<ProductoVenta> prods) {
		Aplicacion.getInstancia().getCatalogo().añadirPack(pack, prods);
	}
	
}
