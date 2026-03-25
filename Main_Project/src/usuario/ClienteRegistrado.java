package usuario;
import java.util.*;
import notificacion.NotificacionCliente;
import solicitud.SolicitudPedido;
import producto.Reseña;
import solicitud.Oferta;
import producto.ProductoVenta;
import producto.LineaProductoVenta;
import producto.ProductoSegundaMano;
import producto.Reseña;
import tiempo.DateTimeSimulado;
import aplicacion.Aplicacion;
import solicitud.*;

public class ClienteRegistrado extends Usuario {
	private Set<NotificacionDeseada> configuracionNotificacionClientees = new HashSet<NotificacionDeseada>();
	private Cartera cartera;
	private Carrito carrito;
	private List<NotificacionCliente> notificaciones = new ArrayList<>();
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	private List<Reseña> reseñas = new ArrayList<>();
	private List<Oferta> ofertasRealizadas = new ArrayList<>();
	private List<Oferta> ofertasRecibidas = new ArrayList<>();
	


	public ClienteRegistrado(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
	public double getGastoTotal() {
		double total = 0;
		for(SolicitudPedido sP : this.pedidos) {
			if (sP.pagado() == true) {
				total += sP.getCostePedido();
			}
		}
		return total;
	}
	
	public void añadirProductoACarrito(ProductoVenta p) {
		this.carrito.añadirProducto(p);
	}

	public void eliminarProductoDelCarrito(ProductoVenta p) {
		this.carrito.eliminarProducto(p);
	}
	
	
	public void añadirProductoACarteraDeIntercambio(ProductoSegundaMano p) {
		this.cartera.añadirProducto(p);
	}

	public void eliminarProductoDeCarteraDeIntercambio(ProductoSegundaMano p) {
		this.cartera.eliminarProducto(p);
	}
	
	public void aceptarOferta(Oferta o) {
		Aplicacion app = Aplicacion.getInstancia();
		app.crearSolicitudIntercambio(o);
	}
	
	public void rechazarOferta(Oferta o) {
		
	}
	
	public void escribirReseña(LineaProductoVenta p, String descripcion, double puntuacion, DateTimeSimulado fecha) {
		Reseña r = new Reseña(descripcion, puntuacion, fecha, p);
		p.añadirReseña(r);
		this.reseñas.add(r);
	}
	
	public void eliminarReseña(LineaProductoVenta p, Reseña r) {
		p.eliminarReseña(r);
		this.reseñas.remove(r);
	}
	
	public void anadirNotificacion(NotificacionCliente n) {
		this.notificaciones.add(n);
	}
	public void eliminarNotificacion(NotificacionCliente n) {
		this.notificaciones.remove(n);
	}
	
	public void cancelarPedido(SolicitudPedido pedido) {
		this.pedidos.remove(pedido);
	}
	
	public void pagarPedido(SolicitudPedido pedido, int numTarjeta, int cvv, DateTimeSimulado fechaCaducidad) {
		
	}
	
	public void realizarPedido() {
		
	}
	
	public void pagarValidacion(SolicitudValidacion validacion, int numTarjeta, int cvv, DateTimeSimulado fechaCaducidad) {
		
	}
	
}
