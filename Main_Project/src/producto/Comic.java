package producto;

import java.util.*;
import java.io.File;

public class Comic extends LineaProductoVenta {
	
	private int numeroPaginas;
	private String autor;
	private String editorial;
	private Date añoPublicacion;
	

	public Comic(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
				 int numeroPaginas, String autor, String editorial, Date añoPublicacion) {
		
		super(nombre, descripcion, foto, stock, precio, unidadesVendidas);
		this.numeroPaginas = numeroPaginas;
		this.autor = autor;
		this.editorial = editorial;
		this.añoPublicacion = añoPublicacion;
	}

}