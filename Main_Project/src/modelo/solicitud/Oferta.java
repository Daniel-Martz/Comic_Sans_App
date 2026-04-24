package modelo.solicitud;

import java.io.Serializable;
import java.util.*;
import modelo.usuario.*;
import modelo.producto.*;
import modelo.tiempo.DateTimeSimulado;

/**
 * Representa una oferta de intercambio realizada por un cliente a otro.
 * Una oferta contiene los productos que el ofertante está dispuesto a dar
 * a cambio de los productos que solicita del destinatario. 
 * Implementa la interfaz Caducable para gestionar su periodo de validez.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Oferta implements Caducable, Serializable{
	
	private static final long serialVersionUID = 1L;

	/** Fecha en la que se realizó la oferta. */
	private DateTimeSimulado fechaRealizacion;
	
	/** Cliente que recibe la oferta. */
	private ClienteRegistrado destinatario;
	
	/** Cliente que emite la oferta. */
	private ClienteRegistrado ofertante;
	
	/** Solicitud de intercambio vinculada a esta oferta (si es aceptada). */
	private SolicitudIntercambio intercambio = null;
	
	/** Productos que el ofertante está dispuesto a entregar. */
	private Set<ProductoSegundaMano> productosOfertados;
	
	/** Productos que el ofertante desea recibir a cambio. */
	private Set<ProductoSegundaMano> productosSolicitados;
	
	/**
	 * Instancia una nueva oferta de intercambio.
	 *
	 * @param fechaRealizacion     la fecha en la que se crea la oferta
	 * @param destinatario         el cliente al que va dirigida la oferta
	 * @param ofertante            el cliente que realiza la oferta
	 * @param productosOfertados   conjunto de productos que se ofrecen
	 * @param productosSolicitados conjunto de productos que se piden a cambio
	 */
	public Oferta(DateTimeSimulado fechaRealizacion, ClienteRegistrado destinatario, ClienteRegistrado ofertante,
			Set<ProductoSegundaMano> productosOfertados, Set<ProductoSegundaMano> productosSolicitados) {
		this.fechaRealizacion = fechaRealizacion;
		this.destinatario = destinatario;
		this.ofertante = ofertante;
		this.productosOfertados = productosOfertados;
		this.productosSolicitados = productosSolicitados;
	}
	
	/**
	 * Obtiene el cliente que ha realizado la oferta.
	 *
	 * @return el cliente ofertante
	 */
	public ClienteRegistrado getOfertante() {
		return this.ofertante;
	}

	/**
	 * Obtiene el cliente al que va dirigida la oferta.
	 *
	 * @return el cliente destinatario
	 */
	public ClienteRegistrado getDestinatario() {
		return this.destinatario;
	}
	
	/**
	 * Vincula una solicitud de intercambio confirmada a esta oferta.
	 *
	 * @param solicitud la solicitud de intercambio a añadir
	 */
	public void añadirSolicitudIntercambio(SolicitudIntercambio solicitud) {
		this.intercambio = solicitud;
	}
	
	/**
	 * Obtiene los productos que se están ofreciendo.
	 * Se devuelve como un conjunto de solo lectura para evitar modificaciones externas.
	 *
	 * @return un Set inmodificable de ProductoSegundaMano
	 */
	public Set<ProductoSegundaMano> productosOfertados(){
		return Collections.unmodifiableSet(this.productosOfertados);
	}
	
	/**
	 * Obtiene los productos que se están solicitando.
	 * Se devuelve como un conjunto de solo lectura para evitar modificaciones externas.
	 *
	 * @return un Set inmodificable de ProductoSegundaMano
	 */
	public Set<ProductoSegundaMano> productosSolicitados(){
		return Collections.unmodifiableSet(this.productosSolicitados);
	}
	
	/**
	 * Obtiene la fecha en la que se creó la oferta.
	 *
	 * @return la fecha de realización
	 */
	public DateTimeSimulado fechaRealizacion() {
		return this.fechaRealizacion;
	}

  /**
   * Comprueba si la oferta ha caducado.
   * * @return true si la oferta ha caducado, false en caso contrario
   */
  public boolean haCaducado(){
    //Si la fecha de realización de la oferta + 7 días es anterior a la fecha actual, la oferta ha caducado. 
    //Se tiene que cumplir también que no haya ya un intercambio confirmado para el producto
    if(fechaRealizacion.diasDespuesDeFecha(7).compareTo(new DateTimeSimulado()) < 0 && this.intercambio == null){
      return true;
    }else{
      return false;
    }
  }
  
  @Override
  public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("===== OFERTA DE INTERCAMBIO =====\n");
      sb.append(String.format("De: %s  --->  Para: %s\n", 
                ofertante.getNombreUsuario(), destinatario.getNombreUsuario()));
      sb.append("Fecha: ").append(this.fechaRealizacion.toStringFecha()).append("\n");
      
      sb.append("Productos que ofrece ").append(ofertante.getNombreUsuario()).append(":\n");
      for (modelo.producto.ProductoSegundaMano p : productosOfertados) {
          sb.append("  [+] ").append(p.getNombre()).append("\n");
      }
      
      sb.append("Productos que solicita a ").append(destinatario.getNombreUsuario()).append(":\n");
      for (modelo.producto.ProductoSegundaMano p : productosSolicitados) {
          sb.append("  [-] ").append(p.getNombre()).append("\n");
      }
      
      if (this.haCaducado()) {
          sb.append("ESTADO: CADUCADA\n");
      } else {
          sb.append("ESTADO: VIGENTE\n");
      }
      sb.append("=================================");
      return sb.toString();
  }
}
