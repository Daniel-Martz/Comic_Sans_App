package descuento;
import java.time.LocalDateTime;
import java.util.*;
import producto.ProductoVenta;

public class Regalo extends UmbralGasto {
	private Set<ProductoVenta> productosRegalo= new HashSet<ProductoVenta>();

	public Regalo(LocalDateTime fechaInicio, LocalDateTime fechaFin, double umbral) {
		super(fechaInicio, fechaFin, umbral);
	}
	
	public void añadirProductoRegalo(ProductoVenta prod) {
		this.productosRegalo.add(prod);
	}

	public void eliminarProductoRegalo(ProductoVenta prod) {
		this.productosRegalo.remove(prod);
	}
}
