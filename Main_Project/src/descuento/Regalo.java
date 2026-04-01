package descuento;
import tiempo.*;
import java.util.*;
import producto.*;

public class Regalo extends UmbralGasto {
	private Map<LineaProductoVenta, Integer> productosRegalo;

	public Regalo(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral, Map<LineaProductoVenta, Integer> productosRegalo) {
		super(fechaInicio, fechaFin, umbral);
		this.productosRegalo = productosRegalo;
	}
	
	public void añadirProductoRegalo(LineaProductoVenta prod, int unidades) {
		productosRegalo.merge(prod, unidades, Integer::sum);
	}

	public void eliminarProductoRegalo(LineaProductoVenta prod, int unidades) {
	    int unidadesActual = productosRegalo.getOrDefault(prod, 0);
	    if (unidadesActual <= unidades) {
	        productosRegalo.remove(prod);
	    } else {
	        productosRegalo.put(prod, unidadesActual - unidades);
	    }
	}
}
