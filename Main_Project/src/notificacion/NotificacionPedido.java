package notificacion;
import solicitud.*;
import tiempo.DateTimeSimulado;

/**
 * Notificación dirigida a un cliente sobre el estado de un pedido.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class NotificacionPedido extends NotificacionCliente {
	
	/** El pedido asociado a esta notificación. */
	private Solicitud pedido; 
	
	/**
	 * Crea una notificación de pedido.
	 *
	 * @param mensaje   el texto de la notificación
	 * @param horaEnvio la fecha y hora de envío
	 * @param pedido    el pedido vinculado
	 */
	public NotificacionPedido(String mensaje, DateTimeSimulado horaEnvio, Solicitud pedido) {
		super(mensaje, horaEnvio);
		this.pedido = pedido;
	}

	/**
	 * Obtiene el pedido asociado a la notificación.
	 *
	 * @return el pedido
	 */
	public Solicitud getPedido() { 
		return pedido;
	}

	/**
	 * Sustituye el pedido asociado a la notificación.
	 *
	 * @param pedido el nuevo pedido
	 */
	public void setPedido(Solicitud pedido) {
		this.pedido = pedido;
	}
	
	/**
	 * Devuelve el texto de la notificación incluyendo la información del pedido.
	 *
	 * @return texto representativo
	 */
	@Override
	public String toString() {
		return super.toString() + 
		       "\nPedido: " + pedido;
	}
}