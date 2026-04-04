package notificacion;

import solicitud.*;
import java.util.*;
import tiempo.DateTimeSimulado;

public class NotificacionOferta extends NotificacionCliente {

	private Oferta oferta;
	public NotificacionOferta(String mensaje, DateTimeSimulado horaEnvio, Oferta o) {
		super(mensaje, horaEnvio);
		this.oferta = o;
	}

	public Oferta getOferta() {
		return oferta;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\nOferta = " + oferta;
	}
}
