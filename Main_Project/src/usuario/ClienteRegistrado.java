package usuario;

import java.util.*;

import notificacion.*;
import producto.*;
import solicitud.*;
import tiempo.*;
import aplicacion.*;

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
	
	public void añadirProductoACarrito(LineaProductoVenta p, Integer cantidad) {
		this.carrito.añadirProducto(p, cantidad);
	}

	public void eliminarProductoDelCarrito(LineaProductoVenta p, Integer cantidad) {
		this.carrito.eliminarProducto(p, cantidad);
	}
	
	
	public void añadirProductoACarteraDeIntercambio(ProductoSegundaMano p) {
		this.cartera.añadirProducto(p);
	}

	public void eliminarProductoDeCarteraDeIntercambio(ProductoSegundaMano p) {
		this.cartera.eliminarProducto(p);
	}

	public void realizarOferta(Set<ProductoSegundaMano> productosOfertados, Set<ProductoSegundaMano> productosSolicitados, ClienteRegistrado destinatario) {
		Oferta ofertaRealizada = new Oferta(new DateTimeSimulado(),  destinatario, this, productosOfertados, productosSolicitados);
		destinatario.recibirOferta(ofertaRealizada);
		NotificacionOferta notif = new NotificacionOferta("Nueva oferta recibida de " + this.nombreUsuario, new DateTimeSimulado(), ofertaRealizada);
		destinatario.anadirNotificacion(notif);
		for(ProductoSegundaMano prod : productosSolicitados) {
			prod.addOfertaRecibida(ofertaRealizada);
		}
		for(ProductoSegundaMano prod : productosOfertados) {
			prod.addOfertaEnviada(ofertaRealizada);
		}
	}
	
	public void recibirOferta(Oferta o) {
		this.ofertasRecibidas.add(o);
	}
	
	public void aceptarOferta(Oferta o) {
		Aplicacion app = Aplicacion.getInstancia();
		app.crearSolicitudIntercambio(o);
	}
	
	public void rechazarOferta(Oferta o) {
		this.ofertasRecibidas.remove(o);
		o.getDestinatario().eliminarOferta(o);
	}
	
	public void eliminarOferta(Oferta o) {
		this.ofertasRealizadas.remove(o);
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
		new SolicitudPedido(this, carrito.getProductos());
		carrito.vaciarCarrito();
	}
	
	public void pagarValidacion(SolicitudValidacion validacion, int numTarjeta, int cvv, DateTimeSimulado fechaCaducidad) {
		
	}
	
	public List<ProductoSegundaMano> getProductosSegundaManoDisponibles(){
		List<ProductoSegundaMano> prods = new LinkedList<ProductoSegundaMano>();
		for(ProductoSegundaMano p : this.cartera.getProductosSegundaMano()) {
			if(!p.estaBloqueado()) {
				prods.add(p);
			}
		}
		return prods;
	}
	
}
