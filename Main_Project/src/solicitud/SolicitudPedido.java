package solicitud;
import java.util.*;

import producto.LineaProductoVenta;
import usuario.ClienteRegistrado;

public class SolicitudPedido extends Solicitud{

	private Map<LineaProductoVenta, Integer> productosDiferentes = new HashMap<>();
	private ClienteRegistrado cliente;
	private Pago pagoPedido;
	private EstadoPedido estado;
	

	public SolicitudPedido(ClienteRegistrado cliente, Map<LineaProductoVenta, Integer> productos) {
		super();
		this.cliente = cliente;
		this.productosDiferentes.putAll(productos);
		this.estado = EstadoPedido.PENDIENTE_DE_PAGO;
	}

	public double getCostePedido() {
		int i;
		double precioFinal= 0;
		for (LineaProductoVenta p: productosDiferentes.keySet()){
			precioFinal += p.getPrecio()*productosDiferentes.get(p);
		}
		return precioFinal;
	}
	
	public void actualizarPagoPedido(EstadoPedido estado) {
		this.estado = estado;
	}
		
	public void añadirPagoPedido(Pago pagoPedido) {
		this.pagoPedido = pagoPedido;
	}
	
	public boolean pagado()	{
		if(this.pagoPedido == null) {
			return false;
		}
		return true;
	}
	
	public void actualizarEstado(EstadoPedido estado) {
	    if (estado == null) {
	        throw new IllegalArgumentException("El estado no puede ser null");
	    }
	    this.estado = estado;
	}
	
	public EstadoPedido getEstado() {
	    return estado;
	}
}
