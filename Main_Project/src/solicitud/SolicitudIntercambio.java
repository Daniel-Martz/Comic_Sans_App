package solicitud;

import tiempo.*;

public class SolicitudIntercambio extends Solicitud{
	private DetallesIntercambio informacionIntercambio;
	private CodigoIntercambio codigoOfertante;
	private CodigoIntercambio codigoDestinatario;
	
	public SolicitudIntercambio(String codigoOfertante, String codigoDestinatario, String lugarIntercambio) {
		this.codigoDestinatario = new CodigoIntercambio(codigoDestinatario);
		this.codigoOfertante = new CodigoIntercambio(codigoOfertante);
		this.informacionIntercambio = new DetallesIntercambio(TiempoSimulado.getFechaHora(), lugarIntercambio);
	}
	
	public boolean aprobarIntercambio(String codigoOfertante, String codigoDestinatario) {
		if(codigoOfertante != this.codigoOfertante.getCodigo() || codigoDestinatario != this.codigoDestinatario.getCodigo()) {
			return false;
		}
		else {
			return true;
		}
	}
}
