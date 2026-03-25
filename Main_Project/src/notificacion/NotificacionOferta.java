package notificacion;

import solicitud.*;
import java.util.*;
import tiempo.DateTimeSimulado;

public class NotificacionOferta extends NotificacionCliente {

	private Set<Oferta> ofertasAsociadas = new HashSet<>();;
	public NotificacionOferta(String mensaje, DateTimeSimulado horaEnvio) {
		super(mensaje, horaEnvio);
	}

	public void addOferta(Oferta oferta) {
		this.ofertasAsociadas.add(oferta);
	}

	public void removeOferta(Oferta oferta) {
		this.ofertasAsociadas.remove(oferta);
	}

	public Set<Oferta> getOfertasAsociadas() {
		return ofertasAsociadas;
	}
}
