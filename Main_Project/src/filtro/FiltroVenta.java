package filtro;
import categoria.Categoria;
import java.util.*;

public class FiltroVenta extends Filtro {
	protected Set<Categoria> categoriasFiltradas = new HashSet<Categoria>();
	protected Set<TipoProducto> tipoFiltrado = new HashSet<TipoProducto>();
	

	public FiltroVenta(boolean ordenAscendente) {
		super(ordenAscendente);
	}
	
	public Set<Categoria> getCategoriasFiltradas() {
	    return categoriasFiltradas;
	}

	public Set<TipoProducto> getTipoFiltrado() {
		return tipoFiltrado;
	}
	
	@Override
	public void limpiarFiltro() {
	    categoriasFiltradas.clear();
	    tipoFiltrado.clear();
	    super.limpiarFiltro();
	}
	
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
