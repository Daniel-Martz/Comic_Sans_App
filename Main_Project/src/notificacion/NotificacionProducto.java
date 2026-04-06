package notificacion;

import producto.LineaProductoVenta;
import tiempo.DateTimeSimulado;

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
	
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder(super.toString());
	    sb.append("\nProductos: ");
	    for (LineaProductoVenta p : productos) {
	        sb.append("\n  - " + p.getNombre() + " (" + p.getPrecio() + " €)");
	    }
	    return sb.toString();
	}
}
