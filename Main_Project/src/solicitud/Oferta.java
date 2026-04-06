package solicitud;

import java.util.*;
import usuario.*;
import producto.*;
import tiempo.DateTimeSimulado;

public class Oferta implements Caducable{
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

  public boolean haCaducado(){
    //Si la fecha de realización de la oferta + 7 días es anterior a la fecha actual, la oferta ha caducado. 
    //Se tiene que cumplir también que no haya ya un intercambio confirmado para el producto
    if(fechaRealizacion.diasDespuesDeFecha(7).compareTo(new DateTimeSimulado()) < 0 && this.intercambio == null){
      return true;
    }else{
      return false;
    }
  }
}
