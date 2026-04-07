package producto;

import java.util.*;
import java.io.*;

import tiempo.DateTimeSimulado;

import java.io.File;


public abstract class Producto implements Comparable<Producto>, Serializable{
    private static final long serialVersionUID = 1L;
	private static int contadorID = 1;
	private final int ID;
	private String nombre;
	private String descripcion;
	private File foto;
	private DateTimeSimulado fechaSubida;
	
	public Producto(String nombre, String descripcion, File foto)
	{
		this.ID = contadorID++; 
		this.nombre = nombre; 
		this.descripcion = descripcion;
		this.foto = foto;
		fechaSubida = new DateTimeSimulado();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeInt(contadorID);
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		contadorID = ois.readInt();
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
	
	public DateTimeSimulado getFechaSubida() {
		return fechaSubida;
	}
	
	@Override
    public int compareTo(Producto otro) {
        return Integer.compare(this.ID, otro.ID);
    }
}