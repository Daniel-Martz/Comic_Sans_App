package producto;

import java.util.*;
import java.io.File;
import tiempo.DateTimeSimulado;

public class Comic extends LineaProductoVenta {
	private int numeroPaginas;
	private String autor;
	private String editorial;
	private int añoPublicacion;
	

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


	public void setNumeroPaginas(int numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}


	public String getAutor() {
		return autor;
	}


	public void setAutor(String autor) {
		this.autor = autor;
	}


	public String getEditorial() {
		return editorial;
	}


	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}


	public int getAñoPublicacion() {
		return añoPublicacion;
	}


	public void setAñoPublicacion(int añoPublicacion) {
		this.añoPublicacion = añoPublicacion;
	}

}
