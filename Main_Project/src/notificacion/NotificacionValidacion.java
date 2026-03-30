package notificacion;

import java.util.*;
import producto.*;
import tiempo.DateTimeSimulado;

public class NotificacionValidacion extends NotificacionCliente {
	private Producto solicitudProductoSegundaMano; 
	
	public NotificacionValidacion(String mensaje, DateTimeSimulado horaEnvio, Producto solicitudProductoSegundaMano) {
		super(mensaje, horaEnvio);
		this.solicitudProductoSegundaMano = solicitudProductoSegundaMano;
	}

	public Producto getSolicitudProductoSegundaMano() {
		return solicitudProductoSegundaMano;
	}

	public void setSolicitudProductoSegundaMano(Producto solicitudProductoSegundaMano) {
		this.solicitudProductoSegundaMano = solicitudProductoSegundaMano;
	}
}
