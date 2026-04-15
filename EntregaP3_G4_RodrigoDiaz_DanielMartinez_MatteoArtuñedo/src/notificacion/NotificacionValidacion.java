package notificacion;

import solicitud.SolicitudValidacion;
import tiempo.DateTimeSimulado;

/**
 * Representa una notificación dirigida a un cliente relacionada con una 
 * solicitud de validación de un producto de segunda mano.
 * Hereda de NotificacionCliente.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class NotificacionValidacion extends NotificacionCliente {
	
	private static final long serialVersionUID = 1L;
	/** La solicitud de validación asociada a esta notificación. */
	private SolicitudValidacion solicitudProductoSegundaMano;

	/**
	 * Instancia una nueva notificación de validación.
	 *
	 * @param mensaje el texto informativo de la notificación
	 * @param horaEnvio la fecha y hora simulada en la que se envía la notificación
	 * @param solicitudProductoSegundaMano la solicitud de validación vinculada
	 */
	public NotificacionValidacion(String mensaje, DateTimeSimulado horaEnvio,
			SolicitudValidacion solicitudProductoSegundaMano) {
		super(mensaje, horaEnvio);
		this.solicitudProductoSegundaMano = solicitudProductoSegundaMano;
	}
	
	/**
	 * Obtiene la solicitud de validación de producto de segunda mano.
	 *
	 * @return la solicitud de validación asociada
	 */
	public SolicitudValidacion getSolicitudProductoSegundaMano() {
		return solicitudProductoSegundaMano;
	}
	/**
	 * Establece o modifica la solicitud de validación de producto de segunda mano.
	 *
	 * @param solicitudProductoSegundaMano la nueva solicitud de validación a asociar
	 */
	public void setSolicitudProductoSegundaMano(SolicitudValidacion solicitudProductoSegundaMano) {
		this.solicitudProductoSegundaMano = solicitudProductoSegundaMano;
	}
	
	/**
	 * Devuelve una representación en texto de la notificación, incluyendo 
	 * la información de la clase padre y el nombre del producto a validar.
	 *
	 * @return una cadena de texto con los detalles de la notificación
	 */
	@Override
	public String toString() {
	    return super.toString() + 
	           "\nProducto a validar: " + 
	           solicitudProductoSegundaMano.getProductoAValidar().getNombre();
	}
}
