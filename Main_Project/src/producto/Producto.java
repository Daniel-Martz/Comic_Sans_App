package producto;

import java.util.*;
import java.io.File;


public abstract class Producto {
	private static int contadorID = 1;
	private int ID;
	private String nombre;
	private String descripcion;
	private File foto;
	
	public Producto(String nombre, String descripcion, File foto)
	{
		this.ID = contadorID++; 
		this.nombre = nombre; 
		this.descripcion = descripcion;
		this.foto = foto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public File getFoto() {
		return foto;
	}

	public void setFoto(File foto) {
		this.foto = foto;
	}

	public int getID() {
		return ID;
	}
	
}