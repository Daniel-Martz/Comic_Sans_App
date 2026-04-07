package notificacion;

import java.util.*;
import tiempo.DateTimeSimulado;

/**
 * Clase base que representa una notificación genérica en el sistema.
 * Todas las demás notificaciones heredan de esta clase.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Notificacion {
	
	// Variable compartida por todas las notificaciones para el autoincremento y asi no repetir id
	/** Contador estático para generar identificadores únicos automáticamente. */
	private static int contadorID = 1; 
	
	/** Identificador único de la notificación. */
	private int id;
	
	/** Texto principal de la notificación. */
	private String mensaje;
	
	/** Fecha y hora simulada en la que se envía la notificación. */
	private DateTimeSimulado horaEnvio;
	
	/**
	 * Crea una nueva notificación asignándole un identificador único automático.
	 *
	 * @param mensaje   el texto de la notificación
	 * @param horaEnvio la fecha y hora de envío
	 */
	public Notificacion(String mensaje, DateTimeSimulado horaEnvio)
	{
		this.id = contadorID++; 
		this.mensaje = mensaje;
		this.horaEnvio = horaEnvio;
	}

	//Getters and setters
	
	/**
	 * Obtiene el identificador único de la notificación.
	 *
	 * @return el ID de la notificación
	 */
	public int getId() {
		return id;
	}

	/**
	 * Obtiene el mensaje de la notificación.
	 *
	 * @return el texto del mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Sustituye el mensaje de la notificación.
	 *
	 * @param mensaje el nuevo texto del mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Obtiene la hora de envío de la notificación.
	 *
	 * @return la fecha y hora de envío
	 */
	public DateTimeSimulado getHoraEnvio() {
		return horaEnvio;
	}

	/**
	 * Sustituye la hora de envío de la notificación.
	 *
	 * @param horaEnvio la nueva fecha y hora
	 */
	public void setHoraEnvio(DateTimeSimulado horaEnvio) {
		this.horaEnvio = horaEnvio;
	}

	/**
	 * Devuelve una representación en texto de la notificación básica.
	 *
	 * @return texto representativo con ID, mensaje y hora
	 */
	@Override
	public String toString() {
	    return "\n--- NOTIFICACIÓN ---\n" +
	           "Mensaje: " + mensaje + "\n" +
	           "Fecha: " + horaEnvio.toStringFecha() + "\n" +
	           "--------------------";
	}
}
