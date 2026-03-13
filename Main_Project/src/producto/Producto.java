package producto;

import java.util.*;
import java.io.File;

public abstract class Producto {
	private static int contadorID = 1;
	private int ID;
	private String name;
	private String descripcion;
	private File foto;
	
	public Producto(String name, String descripcion, File foto)
	{
		this.ID = contadorID++; 
		this.name = name; 
		this.descripcion = descripcion;
		this.foto = foto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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