package notificacion;
import solicitud.*;
import java.util.*;
import tiempo.DateTimeSimulado;

/**
 * Notificación dirigida a un cliente con la información para realizar un intercambio.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class NotificacionIntercambio extends NotificacionCliente {

	/** Código único utilizado para verificar el intercambio. */
	private String codigoIntercambio;	
	
	/** Detalles sobre la fecha y el lugar del intercambio. */
	private DetallesIntercambio detallesIntercambio; 
	
	/**
	 * Crea una notificación de intercambio.
	 *
	 * @param mensaje           el texto de la notificación
	 * @param horaEnvio         la fecha y hora de envío
	 * @param codigoIntercambio el código de verificación
	 * @param detalleIntercambio los detalles de fecha y lugar
	 */
	public NotificacionIntercambio(String mensaje, DateTimeSimulado horaEnvio, String codigoIntercambio, DetallesIntercambio detalleIntercambio)
	{
		super(mensaje, horaEnvio); 
		this.codigoIntercambio = codigoIntercambio;
		this.detallesIntercambio = detalleIntercambio;
	}

	/**
	 * Obtiene el código de verificación del intercambio.
	 *
	 * @return el código de intercambio
	 */
	public String getCodigoIntercambio() {
		return codigoIntercambio;
	}

	/**
	 * Obtiene los detalles de fecha y lugar del intercambio.
	 *
	 * @return los detalles del intercambio
	 */
	public DetallesIntercambio getDetallesIntercambio() {
		return detallesIntercambio;
	}

	/**
	 * Devuelve el texto de la notificación junto con el código y los detalles.
	 *
	 * @return texto representativo
	 */
	@Override
	public String toString() {
		return super.toString() + "\ncodigoIntercambio=" + codigoIntercambio + ", detallesIntercambio="
				+ detallesIntercambio;
	}
}