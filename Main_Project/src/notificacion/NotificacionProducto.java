package notificacion;

import java.sql.Date;
import java.util.ArrayList;

public class NotificacionProducto extends NotificacionCliente {
	private ArrayList<LineaProductoVenta> productos;

	public NotificacionProducto(String mensaje, Date horaEnvio) {
		super(mensaje, horaEnvio);
		this.productos = new ArrayList<>();
	}

	public void addProducto(LineaProductoVenta producto) {
		this.productos.add(producto);
	}

	public void removeProducto(LineaProductoVenta producto) {
		this.productos.remove(producto);
	}

	public ArrayList<LineaProductoVenta> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<LineaProductoVenta> productos) {
		this.productos = productos;
	}
}