package producto;

import java.io.File;

/**
 * Representa un cómi para la venta en la tienda.
 * *@author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Comic extends LineaProductoVenta {
	
	private static final long serialVersionUID = 1L;

	/** Cantidad total de páginas del ejemplar. */
	private final int numeroPaginas;
	
	/** Nombre del autor o autores (guionistas/dibujantes). */
	private final String autor;
	
	/** Empresa encargada de la edición y publicación. */
	private final String editorial;
	
	/** Año en el que se publicó esta edición específica. */
	private final int añoPublicacion;
	
	/**
	 * Construye un nuevo Comic con sus atributos detallados.
	 * @param nombre           Nombre o título del cómic.
	 * @param descripcion      Breve reseña o sinopsis.
	 * @param foto             Archivo de imagen de la portada.
	 * @param stock            Unidades disponibles en el almacén.
	 * @param precio           Precio de venta al público.
	 * @param unidadesVendidas Unidades vendidas
	 * @param numeroPaginas    Extensión del cómic.
	 * @param autor            Creador/es de la obra.
	 * @param editorial        Sello editorial.
	 * @param añoPublicacion   Año de edición.
	 */
	public Comic(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
				 int numeroPaginas, String autor, String editorial, int añoPublicacion) {
		
		super(nombre, descripcion, foto, stock, precio);
		
		if (numeroPaginas <= 0) throw new IllegalArgumentException("El número de páginas debe ser positivo.");
		if (autor == null || editorial == null) throw new IllegalArgumentException("Autor y editorial son obligatorios.");
		
		this.numeroPaginas = numeroPaginas;
		this.autor = autor;
		this.editorial = editorial;
		this.añoPublicacion = añoPublicacion;
	}

	/**
	 * Obtiene el número de páginas del cómic.
	 * @return cantidad de páginas.
	 */
	public int getNumeroPaginas() {
		return numeroPaginas;
	}

	/**
	 * Obtiene el autor o autores de la obra.
	 * @return string con el nombre del autor.
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * Obtiene la editorial que publica el cómic.
	 * @return nombre de la editorial.
	 */
	public String getEditorial() {
		return editorial;
	}

	/**
	 * Obtiene el año de publicación.
	 * @return año en formato entero.
	 */
	public int getAñoPublicacion() {
		return añoPublicacion;
	}
}
