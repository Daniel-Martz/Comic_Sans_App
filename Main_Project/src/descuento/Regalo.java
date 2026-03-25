package descuento;
import java.time.DateTimeSimulado;
import java.util.*;
import producto.ProductoVenta;

public class Regalo extends UmbralGasto {
	private Set<ProductoVenta> productosRegalo= new HashSet<ProductoVenta>();

	public Regalo(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral) {
		super(fechaInicio, fechaFin, umbral);
	}
	
	public void añadirProductoRegalo(ProductoVenta prod) {
		this.productosRegalo.add(prod);
	}

	public void eliminarProductoRegalo(ProductoVenta prod) {
		this.productosRegalo.remove(prod);
	}
}
