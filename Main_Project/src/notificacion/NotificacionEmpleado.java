package notificacion;

import solicitud.*;
import tiempo.DateTimeSimulado;
import java.util.*;

public class NotificacionEmpleado extends Notificacion {
	private Set<Solicitud> solicitudes = new HashSet<>();
	
	public NotificacionEmpleado(String mensaje, DateTimeSimulado horaEnvio)
	{
		//llama al constructor de la clase Notificacion y le paso los datos
		super(mensaje, horaEnvio);
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
	public Set<Solicitud> getSolicitudes() {
		return solicitudes;
	}
}
