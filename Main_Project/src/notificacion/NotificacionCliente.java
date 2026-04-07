package notificacion;

import tiempo.DateTimeSimulado;

/**
 * Clase abstracta base para todas las notificaciones dirigidas a un cliente.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class NotificacionCliente extends Notificacion {
	
	/**
	 * Crea una notificación para un cliente.
	 *
	 * @param mensaje   el texto de la notificación
	 * @param horaEnvio la fecha y hora de envío
	 */
	public NotificacionCliente(String mensaje, DateTimeSimulado horaEnvio)
	{
		//llama al constructor de la clase Notificacion y le paso los datos
		super(mensaje, horaEnvio);
	}
}