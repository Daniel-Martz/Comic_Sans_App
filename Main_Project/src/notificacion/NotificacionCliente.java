package notificacion;

import java.util.*;

public abstract class NotificacionCliente extends Notificacion {
	
	public NotificacionCliente(String mensaje, Date horaEnvio)
	{
		//llama al constructor de la clase Notificacion y le paso los datos
		super(mensaje, horaEnvio);
	}
}
