package solicitud;

import java.util.*;
import usuario.*;
import producto.*;

public class Oferta {
	private String fechaRealizacion;
	private ClienteRegistrado destinatario;
	private ClienteRegistrado ofertante;
	private SolicitudIntercambio intercambio;
	private Set<ProductoSegundaMano> productosOfertados;
	private Set<ProductoSegundaMano> productosSolicitados;
	
	public Oferta(String fechaRealizacion, ClienteRegistrado destinatario, ClienteRegistrado ofertante, SolicitudIntercambio intercambio,
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
	
}