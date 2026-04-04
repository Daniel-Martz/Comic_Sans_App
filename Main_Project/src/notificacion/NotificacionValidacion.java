package notificacion;

import java.util.*;
import producto.*;
import solicitud.SolicitudValidacion;
import tiempo.DateTimeSimulado;

public class NotificacionValidacion extends NotificacionCliente {
	private SolicitudValidacion solicitudProductoSegundaMano;

	public NotificacionValidacion(String mensaje, DateTimeSimulado horaEnvio,
			SolicitudValidacion solicitudProductoSegundaMano) {
		super(mensaje, horaEnvio);
		this.solicitudProductoSegundaMano = solicitudProductoSegundaMano;
	}

	public SolicitudValidacion getSolicitudProductoSegundaMano() {
		return solicitudProductoSegundaMano;
	}

	public void setSolicitudProductoSegundaMano(SolicitudValidacion solicitudProductoSegundaMano) {
		this.solicitudProductoSegundaMano = solicitudProductoSegundaMano;
	}

}
