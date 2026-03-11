package notificacion;

import java.sql.Date;
import java.util.*;


public class Notificacion {
	
	// Variable compartida por todas las notificaciones para el autoincremento y asi no repetir id
    private static int contadorID = 1;
    
	private int id;
	private String mensaje;
	private Date horaEnvio;
	
	public Notificacion(String mensaje, Date horaEnvio)
	{
		this.id = contadorID++; 
		this.mensaje = mensaje;
		this.horaEnvio = horaEnvio;
	}

	//Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Date getHoraEnvio() {
		return horaEnvio;
	}

	public void setHoraEnvio(Date horaEnvio) {
		this.horaEnvio = horaEnvio;
	}

	@Override
	public String toString() {
		return "Notificacion [id=" + id + ", mensaje=" + mensaje + ", horaEnvio=" + horaEnvio + "]";
	}
}
