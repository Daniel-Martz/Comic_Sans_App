package notificacion;

import java.sql.Date;
import java.util.*;

public class NotificacionEmpleado extends Notificacion {
	private ArrayList<Solicitud> solicitudes;
	
	public NotificacionEmpleado(String mensaje, Date horaEnvio)
	{
		//llama al constructor de la clase Notificacion y le paso los datos
		super(mensaje, horaEnvio);
		this.solicitudes = new ArrayList<>();
	}

	// Método para añadir una solicitud 
	public void addSolicitud(Solicitud solicitud) {
		this.solicitudes.add(solicitud);
	}

	// Método para quitar una solicitud 
	public void removeSolicitud(Solicitud solicitud) {
		this.solicitudes.remove(solicitud);
	}

	// Getter para obtener la lista entera
	public ArrayList<Solicitud> getSolicitudes() {
		return solicitudes;
	}
}
