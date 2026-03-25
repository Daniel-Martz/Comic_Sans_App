package notificacion;
import solicitud.*;
import java.util.*;
import tiempo.DateTimeSimulado;

public class NotificacionIntercambio extends NotificacionCliente {

	private String codigoIntercambio;	
	private DetallesIntercambio detallesIntercambio; 
	
	public NotificacionIntercambio(String mensaje, DateTimeSimulado horaEnvio, String codigoIntercambio, DetallesIntercambio detalleIntercambio)
	{
		super(mensaje, horaEnvio); 
		this.codigoIntercambio = codigoIntercambio;
		this.detallesIntercambio = detalleIntercambio;
	}

	public String getCodigoIntercambio() {
		return codigoIntercambio;
	}

	public void setCodigoIntercambio(String codigoIntercambio) {
		this.codigoIntercambio = codigoIntercambio;
	}

	public DetallesIntercambio getDetallesIntercambio() {
		return detallesIntercambio;
	}

	public void setDetallesIntercambio(DetallesIntercambio detallesIntercambio) {
		this.detallesIntercambio = detallesIntercambio;
	}
	
}
