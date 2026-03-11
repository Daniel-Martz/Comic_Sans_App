package notificacion;

import java.sql.Date;
import java.util.*;

public class NotificacionOferta extends NotificacionCliente {

	private ArrayList<Oferta> ofertasAsociadas;
	public NotificacionOferta(String mensaje, Date horaEnvio) {
		super(mensaje, horaEnvio);
		this.ofertasAsociadas = new ArrayList<>();
	}

	public void addOferta(Oferta oferta) {
		this.ofertasAsociadas.add(oferta);
	}

	public void removeOferta(Oferta oferta) {
		this.ofertasAsociadas.remove(oferta);
	}

	public ArrayList<Oferta> getOfertasAsociadas() {
		return ofertasAsociadas;
	}
}