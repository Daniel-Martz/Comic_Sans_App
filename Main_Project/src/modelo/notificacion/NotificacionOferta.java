package modelo.notificacion;

import modelo.solicitud.*;
import modelo.tiempo.DateTimeSimulado;

/**
 * Notificación dirigida a un cliente sobre una oferta de intercambio.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class NotificacionOferta extends NotificacionCliente {

	private static final long serialVersionUID = 1L;
	/** La oferta vinculada a esta notificación. */
	private Oferta oferta;
	
	/**
	 * Crea una notificación de oferta.
	 *
	 * @param mensaje   el texto de la notificación
	 * @param horaEnvio la fecha y hora de envío
	 * @param o         la oferta asociada
	 */
	public NotificacionOferta(String mensaje, DateTimeSimulado horaEnvio, Oferta o) {
		super(mensaje, horaEnvio);
		this.oferta = o;
	}

	/**
	 * Obtiene la oferta asociada a la notificación.
	 *
	 * @return la oferta
	 */
	public Oferta getOferta() {
		return oferta;
	}
	
	/**
	 * Devuelve el texto de la notificación incluyendo la información de la oferta.
	 *
	 * @return texto representativo
	 */
	@Override
	public String toString() {
		return super.toString() + "\nOferta = " + oferta;
	}
}