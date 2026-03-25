package notificacion;

import java.util.*;
import tiempo.DateTimeSimulado;

public class Notificacion {
	
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
		return "Notificacion [id=" + id + ", mensaje=" + mensaje + ", horaEnvio=" + horaEnvio + "]";
	}
}
