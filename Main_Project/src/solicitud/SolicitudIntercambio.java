package solicitud;

import tiempo.*;

public class SolicitudIntercambio extends Solicitud{
	private DetallesIntercambio informacionIntercambio;
	private CodigoIntercambio codigoOfertante;
	private CodigoIntercambio codigoDestinatario;
	private Oferta oferta;
	
	public SolicitudIntercambio(String codigoOfertante, String codigoDestinatario, String lugarIntercambio, Oferta oferta) {
		this.codigoDestinatario = new CodigoIntercambio(codigoDestinatario);
		this.codigoOfertante = new CodigoIntercambio(codigoOfertante);
		this.informacionIntercambio = new DetallesIntercambio(lugarIntercambio);
		this.oferta = oferta;
	}
	
	public boolean aprobarIntercambio(String codigoOfertante, String codigoDestinatario) {
		if(codigoOfertante != this.codigoOfertante.getCodigo() || codigoDestinatario != this.codigoDestinatario.getCodigo()) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public void addOferta(Oferta o) {
		this.oferta = oferta;
	}
}
