package producto;

public class ProductoVenta {
	private int unidades;
	private LineaProductoVenta producto;
	
	public ProductoVenta(LineaProductoVenta producto, int unidades) {
		this.producto = producto;
		this.unidades = unidades;
	}
	
	public double getCosteTotal() {
		if (this.producto != null) {
			return this.producto.getPrecio() * this.unidades;
		}
		return 0.0;
	}

}
