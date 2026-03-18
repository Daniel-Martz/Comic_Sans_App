package aplicacion;

import java.util.*;
import solicitud.*;

public class GestorSolicitudes {
	
	private static GestorSolicitudes instancia;
	private List<SolicitudIntercambio> intercambios = new ArrayList<>();
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	private List<SolicitudValidacion> validaciones = new ArrayList<>();
	
	private GestorSolicitudes() {
		
	}
	
	public GestorSolicitudes getInstancia() {
		if(instancia == null)
		{
		instancia = new GestorSolicitudes();	
		}
		return instancia;
	}

	public void añadirPedido(SolicitudPedido p) {
	}
	
	public void validarProducto(SolicitudValidacion s, double precio, EstadoConservacion estado) {

	}
	
	public void aprobarIntercambio(SolicitudIntercambio s, String codigoOfertante, String codigoDestinatario) {
	}
	
}

