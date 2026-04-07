package usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aplicacion.Aplicacion;
import categoria.Categoria;
import producto.LineaProductoVenta;

/**
 * Implementa la clase Interes. Gestiona y calcula el nivel de interés de un
 * usuario por productos y categorías basándose en sus búsquedas, compras y
 * actividad en el carrito.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class Interes implements Serializable {

	/** Peso asignado a una búsqueda de producto. */
	public static final int PESO_BUSQUEDA = 5;

	/** Peso asignado a la compra de un producto. */
	public static final int PESO_COMPRA = 20;

	/** Peso asignado a la realización de un pedido. */
	public static final int PESO_PEDIDO = 15;

	/** Peso asignado a la acción de añadir al carrito. */
	public static final int PESO_CARRITO = 10;

	/** Factor de división para evitar desbordamiento (Overflow). */
	public static final int REAUJESTE_OVERFLOW = 2;

	private Map<LineaProductoVenta, Integer> rankingInteresBusquedaVenta = new HashMap<>();
	private Map<Categoria, Integer> rankingInteresCategoriaVenta = new HashMap<>();
	private int rankMaxProd;
	private int rankMaxCat;

	/**
	 * Instancia un nuevo objeto Interes, inicializando los rankings a 0.
	 */
	public Interes() {
		for (LineaProductoVenta p : Aplicacion.getInstancia().getCatalogo().getProductosNuevos()) {
			rankingInteresBusquedaVenta.put(p, 0);
		}
		for (Categoria c : Aplicacion.getInstancia().getCatalogo().getCategoriasTienda()) {
			rankingInteresCategoriaVenta.put(c, 0);
		}
		rankMaxProd = 0;
		rankMaxCat = 0;
	}

	/**
	 * Actualiza el ranking de interés de los productos tras una búsqueda.
	 *
	 * @param productos los productos a actualizar
	 */
	public void actualizarInteresBusquedaVenta(List<LineaProductoVenta> productos) {
		for (LineaProductoVenta p : productos) {
			int rank = rankingInteresBusquedaVenta.getOrDefault(p, 0);
			rank += PESO_BUSQUEDA;
			if (rank > rankMaxProd) {
				rankMaxProd = rank;
			}
			rankingInteresBusquedaVenta.put(p, rank);
		}

		/* Evitamos OVERFLOW */
		if (rankMaxProd > (Integer.MAX_VALUE - PESO_BUSQUEDA)) {
			for (Map.Entry<LineaProductoVenta, Integer> entry : rankingInteresBusquedaVenta.entrySet()) {
				entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
			}
			rankMaxProd /= REAUJESTE_OVERFLOW;
		}

	}

	/**
	 * Actualiza el interés de las categorías asociadas a un producto comprado.
	 *
	 * @param producto el producto comprado
	 */
	public void actualizarInteresCompraCategorias(LineaProductoVenta producto) {
		for (Categoria c : producto.getCategorias()) {
			int rank = rankingInteresCategoriaVenta.getOrDefault(c, 0);
			rank += PESO_COMPRA;
			if (rank > rankMaxCat) {
				rankMaxCat = rank;
			}
			rankingInteresCategoriaVenta.put(c, rank);
		}

		/* Evitamos OVERFLOW */
		if (rankMaxCat > (Integer.MAX_VALUE - PESO_COMPRA)) {
			for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
				entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
			}
			rankMaxCat /= REAUJESTE_OVERFLOW;
		}
	}

	/**
	 * Actualiza el interés de las categorías asociadas a un producto pedido.
	 *
	 * @param producto el producto pedido
	 */
	public void actualizarInteresPedidoCategorias(LineaProductoVenta producto) {
		for (Categoria c : producto.getCategorias()) {
			int rank = rankingInteresCategoriaVenta.getOrDefault(c, 0);
			rank += PESO_PEDIDO;
			if (rank > rankMaxCat) {
				rankMaxCat = rank;
			}
			rankingInteresCategoriaVenta.put(c, rank);
		}

		/* Evitamos OVERFLOW */
		if (rankMaxCat > (Integer.MAX_VALUE - PESO_COMPRA)) {
			for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
				entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
			}
			rankMaxCat /= REAUJESTE_OVERFLOW;
		}
	}

	/**
	 * Actualiza el interés de las categorías al añadir un producto al carrito.
	 *
	 * @param producto el producto añadido al carrito
	 */
	public void actualizarInteresCarritoCategorias(LineaProductoVenta producto) {
		for (Categoria c : producto.getCategorias()) {
			int rank = rankingInteresCategoriaVenta.getOrDefault(c, 0);
			rank += PESO_CARRITO;
			if (rank > rankMaxCat) {
				rankMaxCat = rank;
			}
			rankingInteresCategoriaVenta.put(c, rank);
		}

		/* Evitamos OVERFLOW */
		if (rankMaxCat > (Integer.MAX_VALUE - PESO_COMPRA)) {
			for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
				entry.setValue(entry.getValue() / REAUJESTE_OVERFLOW);
			}
			rankMaxCat /= REAUJESTE_OVERFLOW;
		}
	}

	/**
	 * Asigna el valor mínimo de interés actual a un producto nuevo.
	 *
	 * @param producto el nuevo producto
	 */
	public void actualizarInteresNuevoVenta(LineaProductoVenta producto) {
		rankingInteresBusquedaVenta.put(producto, rankMinProd());
	}

	/**
	 * Elimina un producto del ranking de interés.
	 *
	 * @param producto el producto a eliminar
	 */
	public void eliminarProductoInteres(LineaProductoVenta producto) {
		rankingInteresBusquedaVenta.remove(producto);
	}

	/**
	 * Asigna el valor mínimo de interés actual a una nueva categoría.
	 *
	 * @param categoria la nueva categoría
	 */
	public void actualizarInteresCategoriaNueva(Categoria categoria) {
		rankingInteresCategoriaVenta.put(categoria, rankMinCat());
	}

	/**
	 * Elimina una categoría del ranking de interés.
	 *
	 * @param categoria la categoría a eliminar
	 */
	public void eliminarCategoriaInteres(Categoria categoria) {
		rankingInteresCategoriaVenta.remove(categoria);
	}

	private Integer rankMinProd() {
		int min = Integer.MAX_VALUE;
		int val = 0;
		if (rankingInteresBusquedaVenta.isEmpty()) {
			return 0;
		}
		for (Map.Entry<LineaProductoVenta, Integer> entry : rankingInteresBusquedaVenta.entrySet()) {
			val = entry.getValue();
			if (val < min) {
				min = val;
			}
		}
		return min;
	}

	private Integer rankMinCat() {
		int min = Integer.MAX_VALUE;
		int val = 0;
		if (rankingInteresCategoriaVenta.isEmpty()) {
			return 0;
		}
		for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
			val = entry.getValue();
			if (val < min) {
				min = val;
			}
		}
		return min;
	}

	/**
	 * Devolverá el interés de cada usuario unificado y normalizado. Para ello se
	 * suma el interés de cada categoría a cada producto que la contenga y
	 * finnalmente se divide entre el máximo ranking para así acotar entre 0 y 1 el
	 * interés en cada producto. Se controla también un posible Overflow a la hora
	 * de unificar los mapas
	 * 
	 * @return el mapa de interés normalizado (valores entre 0 y 1)
	 */
	public Map<LineaProductoVenta, Double> obtenerRankingDeInteres() {
		Map<LineaProductoVenta, Double> normalizado = new HashMap<>();
		Map<LineaProductoVenta, Integer> auxiliar = new HashMap<>(rankingInteresBusquedaVenta);
		int maxAux = rankMaxProd; /* variable local, no toca el atributo */

		for (Map.Entry<Categoria, Integer> entry : rankingInteresCategoriaVenta.entrySet()) {
			for (LineaProductoVenta p : entry.getKey().obtenerProductosCategoria()) {
				int rank = auxiliar.getOrDefault(p, 0);
				rank += entry.getValue();
				if (rank > maxAux) {
					maxAux = rank;
				}
				auxiliar.put(p, rank);
			}

			/* Evitamos OVERFLOW */
			if (maxAux > (Integer.MAX_VALUE - rankMaxCat)) {
				for (Map.Entry<LineaProductoVenta, Integer> ent : auxiliar.entrySet()) {
					ent.setValue(ent.getValue() / REAUJESTE_OVERFLOW);
				}
				maxAux /= REAUJESTE_OVERFLOW;
			}
		}
		/* Se devuelve un Empty map si no se ha modificado el interés */
		if (maxAux == 0)
			return normalizado;

		for (Map.Entry<LineaProductoVenta, Integer> entry : auxiliar.entrySet()) {
			normalizado.put(entry.getKey(), (double) entry.getValue() / maxAux);
		}
		return normalizado;
	}

}
