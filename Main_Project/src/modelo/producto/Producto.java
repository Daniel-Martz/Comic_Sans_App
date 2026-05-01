package modelo.producto;

import java.io.*;

import modelo.tiempo.DateTimeSimulado;

import java.io.File;

/**
 * Clase base abstracta que representa un producto genérico en el catálogo.
 * Define los atributos básicos y proporciona ordenación por ID y capacidad de serialización.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class Producto implements Comparable<Producto>, Serializable{
    
    /** Identificador único para la serialización de la clase. */
    private static final long serialVersionUID = 1L;
    
    /** Contador estático para la generación de IDs únicos auto-incrementales. */
	private static int contadorID = 1;
	
	/** Identificador único e inmutable del producto. */
	private int ID;
	
	/** Nombre del producto. */
	private String nombre;
	
	/** Descripción detallada del producto. */
	private String descripcion;
	
	/** Archivo de imagen asociado al producto. */
	private File foto;
	
	/** Fecha en la que el producto se registró en el sistema. */
	private DateTimeSimulado fechaSubida;
	
	/**
	 * Construye un nuevo producto asignándole un ID único y la fecha actual.
	 *
	 * @param nombre el nombre del producto.
	 * @param descripcion la descripción del producto.
	 * @param foto el archivo de imagen del producto.
	 */
	public Producto(String nombre, String descripcion, File foto)
	{
		this.ID = contadorID++; 
		this.nombre = nombre; 
		this.descripcion = descripcion;
		this.foto = foto;
		fechaSubida = new DateTimeSimulado();
	}

	/**
	 * Personaliza el proceso de serialización para guardar el contador estático de IDs.
	 * @param oos Flujo de salida de objetos.
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		oos.writeInt(contadorID);
	}

	/**
	 * Personaliza el proceso de deserialización para restaurar el contador estático de IDs.
	 * @param ois Flujo de entrada de objetos.
	 * @throws IOException Si ocurre un error de lectura.
	 * @throws ClassNotFoundException Si no se encuentra la clase.
	 */
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		contadorID = ois.readInt();
	}
	
	/** 
	 * getter de nombre
	 * @return El nombre del producto. */
	public String getNombre() {
		return nombre;
	}

	/** 
	 * setter del nuevo nombre
	 * @param nombre El nuevo nombre del producto. */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/** 
	 * getter de descripcion
	 * @return La descripción del producto. */
	public String getDescripcion() {
		return descripcion;
	}

	/** 
	 * setter de descripcion
	 * @param descripcion La nueva descripción del producto. */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/** 
	 * getter de foto
	 * @return El archivo de imagen del producto. */
	public File getFoto() {
		return foto;
	}

	/** 
	 * setter de foto
	 * @param foto El nuevo archivo de imagen del producto. */
	public void setFoto(File foto) {
		this.foto = foto;
	}

	/** 
	 * getter del id
	 * @return El identificador único del producto. */
	public int getID() {
		return ID;
	}

	public void setID(int id) {
		this.ID = id;
	}
	
	/** 
	 * getter de la fecha de subida
	 * @return La fecha en la que se subió el producto. */
	public DateTimeSimulado getFechaSubida() {
		return fechaSubida;
	}
	
	/**
	 * Compara dos productos basándose en su ID para ordenarlos.
	 * @param otro El producto con el que se va a comparar.
	 * @return Un entero negativo, cero o positivo si este ID es menor, igual o mayor que el especificado.
	 */
	@Override
    public int compareTo(Producto otro) {
        return Integer.compare(this.ID, otro.ID);
    }
}