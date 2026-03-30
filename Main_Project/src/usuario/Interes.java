package usuario;

import java.util.HashMap;
import java.util.Map;

import aplicacion.Aplicacion;
import categoria.Categoria;
import producto.LineaProductoVenta;

public class Interes {
	public static final int PESO_BUSQUEDA = 5;
	public static final int PESO_CATEGORIA = 2;
	public static final int PESO_COMPRA = 50;
	public static final int REAUJESTE_OVERFLOW = 2;
	private Map<LineaProductoVenta, Integer> rankingInteresBusquedaVenta = new HashMap<>();
	private Map<Categoria, Integer> rankingInteresCategoriaVenta = new HashMap<>();
	private int rankMaxProd;
	private int rankMaxCat;
	

	public Interes() {
		for(LineaProductoVenta p : Aplicacion.getInstancia().getCatalogo().getProductosNuevos()) {
			rankingInteresBusquedaVenta.put(p, 0);
		}
		for(Categoria c : Aplicacion.getInstancia().getCatalogo().getCategoriasTienda()) {
			rankingInteresCategoriaVenta.put(c, 0);
		}
		rankMaxProd = 0;
		rankMaxCat = 0;
	}
	
	//Se actualiza el interés por ser filtrado tras una búsqueda
	public void actualizarInteresBusquedaVenta(LineaProductoVenta... productos) {
		for(LineaProductoVenta p : productos) {
			int rank = rankingInteresBusquedaVenta.getOrDefault(p, 0);
			rank += PESO_BUSQUEDA;
			if(rank > rankMaxProd) {
				rankMaxProd = rank;
			}
			rankingInteresBusquedaVenta.put(p, rank);
		}
		
		//Evitamos OVERFLOW
		if (rankMaxProd > (Integer.MAX_VALUE - PESO_BUSQUEDA)) {
		    for (Map.Entry<LineaProductoVenta, Integer> entry : rankingInteresBusquedaVenta.entrySet()) {
		        entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
		    }
		    rankMaxProd /= REAUJESTE_OVERFLOW;
		}
		
	}
	
	//Tras una compra el interés se atualizará mediante las categorias asociadas a dicho producto
	public void actualizarInteresCompraCategorias(LineaProductoVenta producto) {
		for(Categoria c : producto.getCategorias()) {
			int rank = rankingInteresCategoriaVenta.getOrDefault(c, 0);
			rank += PESO_COMPRA;
			if(rank > rankMaxCat) {
				rankMaxCat = rank;
			}
			rankingInteresCategoriaVenta.put(c, rank);
		}
		
		//Evitamos OVERFLOW
		if (rankMaxCat > (Integer.MAX_VALUE - PESO_COMPRA)) {
		    for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
		        entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
		    }
		    rankMaxCat /= REAUJESTE_OVERFLOW;
		}
	}
	
	//Se actualiza el interés por ser filtrado tras una búsqueda
	public void actualizarInteresPinchaProducto(LineaProductoVenta producto) {
		for(Categoria c : producto.getCategorias()) {
			int rank = rankingInteresCategoriaVenta.getOrDefault(c, 0);
			rank += PESO_CATEGORIA;
			if(rank > rankMaxCat) {
				rankMaxCat = rank;
			}
			rankingInteresCategoriaVenta.put(c, rank);
		}
		
		//Evitamos OVERFLOW
		if (rankMaxCat > (Integer.MAX_VALUE - PESO_COMPRA)) {
		    for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
		        entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
		    }
		    rankMaxCat /= REAUJESTE_OVERFLOW;
		}
	}
	
	//Hago que cualquier producto nuevo parta del minimo valor para que sea justo
	public void actualizarInteresNuevoVenta(LineaProductoVenta producto) {
		rankingInteresBusquedaVenta.put(producto, rankMinProd());
	}
	
	public void actualizarInteresCategoriaNueva(Categoria categoria) {
		rankingInteresCategoriaVenta.put(categoria, rankMinCat());
	}
	
	private Integer rankMinProd() {
		int min = Integer.MAX_VALUE;
		int val = 0;
		if(rankingInteresBusquedaVenta.isEmpty()) {
			return 0;
		}
		for(Map.Entry<LineaProductoVenta, Integer> entry: rankingInteresBusquedaVenta.entrySet()) {
			val = entry.getValue();
			if(val < min) {
				min = val;
			}
		}
		return min;
	}

	private Integer rankMinCat() {
		int min = Integer.MAX_VALUE;
		int val = 0;
		if(rankingInteresCategoriaVenta.isEmpty()) {
			return 0;
		}
		for(Map.Entry<Categoria, Integer> entry: rankingInteresCategoriaVenta.entrySet()) {
			val = entry.getValue();
			if(val < min) {
				min = val;
			}
		}
		return min;
	}
	
}
