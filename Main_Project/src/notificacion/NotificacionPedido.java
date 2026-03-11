package notificacion;

import java.sql.Date;

public class NotificacionPedido extends NotificacionCliente {
	private Solicitud pedido; 
	
	public NotificacionPedido(String mensaje, Date horaEnvio, Solicitud pedido) {
		super(mensaje, horaEnvio);
		this.pedido = pedido;
	}

	public Solicitud getPedido() {
		return pedido;
	}

	public void setPedido(Solicitud pedido) {
		this.pedido = pedido;
	}
}
