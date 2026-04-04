package notificacion;
import solicitud.*;
import tiempo.DateTimeSimulado;

import java.util.*;

public class NotificacionPedido extends NotificacionCliente {
	private Solicitud pedido; 
	
	public NotificacionPedido(String mensaje, DateTimeSimulado horaEnvio, Solicitud pedido) {
		super(mensaje, horaEnvio);
		this.pedido = pedido;
	}

	public Solicitud getPedido() { 
		return pedido;
	}

	public void setPedido(Solicitud pedido) {
		this.pedido = pedido;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + "\nPedido = " + pedido;
	}
}
