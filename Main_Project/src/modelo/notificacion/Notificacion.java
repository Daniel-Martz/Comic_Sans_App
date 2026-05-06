package modelo.notificacion;

import modelo.tiempo.DateTimeSimulado;
import java.io.*;

/**
 * Clase base que representa una notificación genérica en el sistema.
 * Todas las demás notificaciones heredan de esta clase.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Notificacion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Variable compartida por todas las notificaciones para el autoincremento y asi no repetir id
	/** Contador estático para generar identificadores únicos automáticamente. */
	private static int contadorID = 1; 
	
	/** Identificador único de la notificación. */
	private int id;
	
	/** Texto principal de la notificación. */
	private String mensaje;
	
	/** Fecha y hora simulada en la que se envía la notificación. */
	private DateTimeSimulado horaEnvio;

  /** Describe si la notificacion ha sido leída o no*/
  private boolean read = false;
	
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

	/**
	 * Serializa el objeto y guarda el contador de identificadores.
	 *
	 * @param oos flujo de salida de objetos
	 * @throws IOException si ocurre un error de escritura
	 */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(contadorID);
    }

    /**
     * Deserializa el objeto y recupera el contador de identificadores.
     *
     * @param ois flujo de entrada de objetos
     * @throws IOException si ocurre un error de lectura
     * @throws ClassNotFoundException si no se encuentra una clase durante la deserialización
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        contadorID = ois.readInt();
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

  /**
   * Devuelve si una notificación ha sido leída o no
   */
  public boolean getRead(){ return read; }

  /**
   * Cambia el estado de la notificación a leída
   */
  public void read(){ read = true; }
}
