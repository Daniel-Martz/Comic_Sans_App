package notificacion;

import producto.LineaProductoVenta;
import java.util.*;

public class NotificacionProducto extends NotificacionCliente {
	
	private Set<LineaProductoVenta> productos = new HashSet<>();

	public NotificacionProducto(String mensaje, DateTimeSimulado horaEnvio) {
		super(mensaje, horaEnvio);
	}

	
	public void addProducto(LineaProductoVenta producto) {
		this.productos.add(producto);
	}

	public void removeProducto(LineaProductoVenta producto) {
		this.productos.remove(producto);
	}

	public Set<LineaProductoVenta> getProductos() {
		return productos;
	}

	public void setProductos(Set<LineaProductoVenta> productos) {
		this.productos = productos;
	}
}
