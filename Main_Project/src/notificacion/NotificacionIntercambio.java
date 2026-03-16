package notificacion;
import solicitud.*;
import java.util.*;

public class NotificacionIntercambio extends NotificacionCliente {

	private String codigoIntercambio;	
	private Solicitud detallesIntercambio; 
	
	public NotificacionIntercambio(String mensaje, Date horaEnvio, String codigoIntercambio, Solicitud detalleIntercambio)
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

	public Solicitud getDetallesIntercambio() {
		return detallesIntercambio;
	}

	public void setDetallesIntercambio(Solicitud detallesIntercambio) {
		this.detallesIntercambio = detallesIntercambio;
	}
	
}