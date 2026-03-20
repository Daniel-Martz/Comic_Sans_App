package notificacion;

import java.util.*;

public abstract class NotificacionCliente extends Notificacion {
	
	public NotificacionCliente(String mensaje, DateTimeSimulado horaEnvio)
	{
		//llama al constructor de la clase Notificacion y le paso los datos
		super(mensaje, horaEnvio);
	}
}
