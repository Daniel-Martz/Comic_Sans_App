package filtro;
import categoria.Categoria;
import java.util.*;

public class FiltroVenta extends Filtro {
	private Set<Categoria> categoriasFiltradas = new HashSet<Categoria>();
	private Set<TipoProducto> tipoFiltrado = new HashSet<TipoProducto>();
	

	public FiltroVenta(boolean ordenAscendente) {
		super(ordenAscendente);
	}
	
	public void añadirCategoria(Categoria categ) {
		categoriasFiltradas.add(categ);
	}

	public void eliminarCategoria(Categoria categ) {
		categoriasFiltradas.remove(categ);
	}
	
	public void añadirTipoProducto(TipoProducto tipo) {
		tipoFiltrado.add(tipo);
	}

	public void eliminarTipoProducto(TipoProducto tipo) {
		tipoFiltrado.remove(tipo);
	}
}
