package modelo.notificacion;

import modelo.solicitud.*;
import modelo.tiempo.DateTimeSimulado;
import java.util.*;

/**
 * Notificación dirigida a un empleado informándole sobre solicitudes pendientes.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class NotificacionEmpleado extends Notificacion {
	
	private static final long serialVersionUID = 1L;
	/** Conjunto de solicitudes asociadas a esta notificación. */
	private Set<Solicitud> solicitudes = new HashSet<>();
	
	/**
	 * Crea una notificación para un empleado.
	 *
	 * @param mensaje   el texto de la notificación
	 * @param horaEnvio la fecha y hora de envío
	 */
	public NotificacionEmpleado(String mensaje, DateTimeSimulado horaEnvio)
	{
		//llama al constructor de la clase Notificacion y le paso los datos
		super(mensaje, horaEnvio);
	}

	/**
	 * Añade una solicitud a la notificación.
	 *
	 * @param solicitud la solicitud a añadir
	 */
	// Método para añadir una solicitud 
	public void addSolicitud(Solicitud solicitud) {
		this.solicitudes.add(solicitud);
	}

	/**
	 * Elimina una solicitud de la notificación.
	 *
	 * @param solicitud la solicitud a quitar
	 */
	// Método para quitar una solicitud 
	public void removeSolicitud(Solicitud solicitud) {
		this.solicitudes.remove(solicitud);
	}

	/**
	 * Obtiene todas las solicitudes asociadas a la notificación.
	 *
	 * @return el conjunto de solicitudes
	 */
	// Getter para obtener la lista entera
	public Set<Solicitud> getSolicitudes() {
		return solicitudes;
	}
	
	/**
	 * Devuelve el texto de la notificación indicando el número de solicitudes pendientes.
	 *
	 * @return texto representativo
	 */
	@Override
	public String toString() {
		return super.toString() + 
		       "\nSolicitudes pendientes: " + solicitudes.size();
	}
}
