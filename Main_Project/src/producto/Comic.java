package producto;

import java.util.*;
import java.io.File;
import tiempo.DateTimeSimulado;

public class Comic extends LineaProductoVenta {
	private final int numeroPaginas;
	private final String autor;
	private final String editorial;
	private final int añoPublicacion;
	

	public Comic(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
				 int numeroPaginas, String autor, String editorial, int añoPublicacion) {
		
		super(nombre, descripcion, foto, stock, precio);
		this.numeroPaginas = numeroPaginas;
		this.autor = autor;
		this.editorial = editorial;
		this.añoPublicacion = añoPublicacion;
	}
public int getNumeroPaginas() {
		return numeroPaginas;
	}


	public String getAutor() {
		return autor;
	}


	public String getEditorial() {
		return editorial;
	}


	public int getAñoPublicacion() {
		return añoPublicacion;
	}

}
