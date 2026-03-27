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
		this.informacionIntercambio = new DetallesIntercambio(new DateTimeSimulado(), lugarIntercambio);
		this.oferta = oferta;
	}
	
	public boolean aprobarIntercambio(String codigoOfertante, String codigoDestinatario) {
		if(this.codigoOfertante.validarCodigo(codigoOfertante) && this.codigoDestinatario.validarCodigo(codigoDestinatario)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void addOferta(Oferta o) {
		this.oferta = o;
	}

}
