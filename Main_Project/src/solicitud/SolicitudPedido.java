package solicitud;
import java.util.*;

import producto.ProductoVenta;
import usuario.ClienteRegistrado;

public class SolicitudPedido extends Solicitud{

	private Set<ProductoVenta> productosDiferentes = new HashSet<>();
	private ClienteRegistrado cliente;
	private Pago pagoPedido;
	private EstadoPedido estado;
	

	public SolicitudPedido(ClienteRegistrado cliente, ProductoVenta...productos) {
		super();
		this.cliente = cliente;
		for(ProductoVenta p : productos) {
			this.productosDiferentes.add(p);
		}
		this.estado = EstadoPedido.PENDIENTE_DE_PAGO;
	}

	public double getCostePedido() {
		double precioFinal= 0;
		for (ProductoVenta producto:productosDiferentes){
			precioFinal += producto.obtenerPrecio();
		}
		return precioFinal;
	}
	
	public void actualizarPagoPedido(EstadoPedido estado) {
		this.estado = estado;
	}
		
	public void añadirPagoPedido(Pago pagoPedido) {
		this.pagoPedido = pagoPedido;
	}
	
}
