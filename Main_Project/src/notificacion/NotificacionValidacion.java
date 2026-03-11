package notificacion;

import java.sql.Date;

public class NotificacionValidacion extends NotificacionCliente {
	private Producto solicitudProductoSegundaMano; 
	
	public NotificacionValidacion(String mensaje, Date horaEnvio, Producto solicitudProductoSegundaMano) {
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
