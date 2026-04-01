package solicitud;

import java.util.*;
import usuario.*;
import producto.*;
import tiempo.DateTimeSimulado;

public class Oferta {
	private DateTimeSimulado fechaRealizacion;
	private ClienteRegistrado destinatario;
	private ClienteRegistrado ofertante;
	private SolicitudIntercambio intercambio;
	private Set<ProductoSegundaMano> productosOfertados;
	private Set<ProductoSegundaMano> productosSolicitados;
	
	public Oferta(DateTimeSimulado fechaRealizacion, ClienteRegistrado destinatario, ClienteRegistrado ofertante,
			Set<ProductoSegundaMano> productosOfertados, Set<ProductoSegundaMano> productosSolicitados) {
		this.fechaRealizacion = fechaRealizacion;
		this.destinatario = destinatario;
		this.ofertante = ofertante;
		this.intercambio = intercambio;
		this.productosOfertados = productosOfertados;
		this.productosSolicitados = productosSolicitados;
	}
	
	public ClienteRegistrado getOfertante() {
		return this.ofertante;
	}

	public ClienteRegistrado getDestinatario() {
		return this.destinatario;
	}
	
	public void añadirSolicitudIntercambio(SolicitudIntercambio solicitud) {
		this.intercambio = solicitud;
	}
	
	public Set<ProductoSegundaMano> productosOfertados(){
		return Collections.unmodifiableSet(this.productosOfertados);
	}
	
	public Set<ProductoSegundaMano> productosSolicitados(){
		return Collections.unmodifiableSet(this.productosSolicitados);
	}
	
	public DateTimeSimulado fechaRealizacion() {
		return this.fechaRealizacion;
	}
	
}