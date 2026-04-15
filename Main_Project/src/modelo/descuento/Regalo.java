package modelo.descuento;
import modelo.tiempo.*;
import java.util.*;
import modelo.producto.*;

/**
 * Descuento que obsequia productos gratis al superar un umbral de gasto.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Regalo extends UmbralGasto {
	
	private static final long serialVersionUID = 1L;
	/** Mapa que asocia cada producto de regalo con su cantidad de unidades. */
	private Map<LineaProductoVenta, Integer> productosRegalo;

	/**
	 * Crea un descuento de tipo regalo.
	 *
	 * @param fechaInicio     la fecha en la que empieza a ser válido
	 * @param fechaFin        la fecha en la que caduca
	 * @param umbral          el gasto mínimo requerido para obtener el regalo
	 * @param productosRegalo el mapa con los productos que se van a regalar
	 */
	public Regalo(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral, Map<LineaProductoVenta, Integer> productosRegalo) {
		super(fechaInicio, fechaFin, umbral);
		this.productosRegalo = productosRegalo;
	}
	
	/**
	 * Añade o suma unidades de un producto a la lista de regalos.
	 *
	 * @param prod     el producto a regalar
	 * @param unidades la cantidad a añadir
	 */
	public void añadirProductoRegalo(LineaProductoVenta prod, int unidades) {
		productosRegalo.merge(prod, unidades, Integer::sum);
	}

	/**
	 * Resta unidades de un producto de regalo o lo elimina si llega a cero.
	 *
	 * @param prod     el producto a modificar
	 * @param unidades la cantidad a restar
	 */
	public void eliminarProductoRegalo(LineaProductoVenta prod, int unidades) {
		int unidadesActual = productosRegalo.getOrDefault(prod, 0);
		if (unidadesActual <= unidades) {
			productosRegalo.remove(prod);
		} else {
			productosRegalo.put(prod, unidadesActual - unidades);
		}
	}

	/**
	 * Obtiene los productos de regalo y sus cantidades.
	 * Se devuelve como un mapa de solo lectura para evitar modificaciones externas.
	 *
	 * @return mapa inmodificable con los productos de regalo
	 */
	public Map<LineaProductoVenta, Integer> getProductosRegalo(){
		return Collections.unmodifiableMap(productosRegalo);
	}
}