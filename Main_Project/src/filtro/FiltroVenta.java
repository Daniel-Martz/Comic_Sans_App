package filtro;

import categoria.Categoria;
import java.util.*;

/**
 * Clase base para gestionar los criterios de filtrado de productos en venta.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class FiltroVenta extends Filtro {
	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;

	/** Categorías seleccionadas para el filtrado. */
	protected Set<Categoria> categoriasFiltradas = new HashSet<Categoria>();
	
	/** Tipos de producto seleccionados para el filtrado. */
	protected Set<TipoProducto> tipoFiltrado = new HashSet<TipoProducto>();

	/**
	 * Constructor que define el orden de los resultados.
	 * @param ordenAscendente true para orden ascendente, false para descendente.
	 */
	public FiltroVenta(boolean ordenAscendente) {
		super(ordenAscendente);
	}
	
	/** @return El conjunto de categorías aplicadas al filtro. */
	public Set<Categoria> getCategoriasFiltradas() {
	    return categoriasFiltradas;
	}

	/** @return El conjunto de tipos de producto aplicados al filtro. */
	public Set<TipoProducto> getTipoFiltrado() {
		return tipoFiltrado;
	}
	
	/** Restablece los criterios de categorías y tipos de producto. */
	@Override
	public void limpiarFiltro() {
	    categoriasFiltradas.clear();
	    tipoFiltrado.clear();
	    super.limpiarFiltro();
	}
	
	/**
	 * Actualiza los parámetros de filtrado y el orden de visualización.
	 * @param ordenAscendente Nuevo orden de los elementos.
	 * @param categorias Nuevas categorías a filtrar.
	 * @param tipos Nuevos tipos de producto a filtrar.
	 */
	public void cambiarFiltro(boolean ordenAscendente, Set<Categoria> categorias, Set<TipoProducto> tipos) {
	    this.categoriasFiltradas.clear();
	    if (categorias != null) {
	        this.categoriasFiltradas.addAll(categorias);
	    }

	    this.tipoFiltrado.clear();
	    if (tipos != null) {
	        this.tipoFiltrado.addAll(tipos);
	    }

	    this.ordenAscendente = ordenAscendente;
	}
}