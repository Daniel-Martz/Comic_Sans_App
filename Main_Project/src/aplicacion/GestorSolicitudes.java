package aplicacion;

import java.util.*;
import producto.EstadoConservacion;
import solicitud.*;

public class GestorSolicitudes {
	private List<SolicitudIntercambio> intercambios = new ArrayList<>();
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	private List<SolicitudValidacion> validaciones = new ArrayList<>();
	
	public void añadirSolicitudValidacion(SolicitudValidacion s) {
		this.validaciones.add(s);
	}

	public void eliminarSolicitudValidacion(SolicitudValidacion s) {
		this.validaciones.remove(s);
	}
	
	public void añadirSolicitudPedido(SolicitudPedido s) {
		this.pedidos.add(s);
	}

	public void eliminarSolicitudPedido(SolicitudPedido s) {
		this.pedidos.remove(s);
	}

	public void añadirSolicitudIntercambio(SolicitudIntercambio s) {
		this.intercambios.add(s);
	}

	public void eliminarSolicitudIntercambio(SolicitudIntercambio s) {
		this.intercambios.remove(s);
	}
	
	public GestorSolicitudes() {
		
	}

	public void añadirPedido(SolicitudPedido p) {
	}
	
	public void validarProducto(SolicitudValidacion s, double precio, EstadoConservacion estado) {

	}
	
	public void aprobarIntercambio(SolicitudIntercambio s, String codigoOfertante, String codigoDestinatario) {
	}

	
}

