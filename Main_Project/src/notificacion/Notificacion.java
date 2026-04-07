package notificacion;

import java.util.*;
import tiempo.DateTimeSimulado;
import java.io.*;

public class Notificacion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Variable compartida por todas las notificaciones para el autoincremento y asi no repetir id
    private static int contadorID = 1;
    
	private int id;
	private String mensaje;
	private DateTimeSimulado horaEnvio;
	
	public Notificacion(String mensaje, DateTimeSimulado horaEnvio)
	{
		this.id = contadorID++; 
		this.mensaje = mensaje;
		this.horaEnvio = horaEnvio;
	}

	// Persist static contadorID
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(contadorID);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        contadorID = ois.readInt();
    }

	//Getters and setters
	public int getId() {
		return id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public DateTimeSimulado getHoraEnvio() {
		return horaEnvio;
	}

	public void setHoraEnvio(DateTimeSimulado horaEnvio) {
		this.horaEnvio = horaEnvio;
	}

	@Override
	public String toString() {
	    return "Notificacion [id=" + id + 
	           ", mensaje=" + mensaje + 
	           ", horaEnvio=" + horaEnvio.toStringFecha() + "]";
	}
}
