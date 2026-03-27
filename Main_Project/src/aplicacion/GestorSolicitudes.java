package aplicacion;

import java.util.*;
import producto.*;
import solicitud.*;

public class GestorSolicitudes {
	
	private static GestorSolicitudes instancia;
	private List<SolicitudIntercambio> intercambios = new ArrayList<>();
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	private List<SolicitudValidacion> validaciones = new ArrayList<>();
	
	private GestorSolicitudes() {
		
	}
	
	public static GestorSolicitudes getInstancia() {
		if(instancia == null)
		{
		instancia = new GestorSolicitudes();	
		}
		return instancia;
	}

	public void añadirPedido(SolicitudPedido p) {
		if(p == null)
		{
			throw new IllegalArgumentException("El pedido no es valido");
		}
		pedidos.add(p);
	}
	
	public void eliminarPedido(SolicitudPedido p) {
		pedidos.remove(p);
	}
	
	public void añadirSolicitudIntercambio(SolicitudIntercambio s)
	{
		if(s == null)
		{
			throw new IllegalArgumentException("La solicitud no es valida");
		}
		intercambios.add(s);
	}
	
	public void eliminarSolicitudIntercambio(SolicitudIntercambio s)
	{
	 intercambios.remove(s);
	}
	
	public void añadirSolicitudValidacion(SolicitudValidacion s)
	{
		if(s == null)
		{
			throw new IllegalArgumentException("La solicitud no es valida");
		}
		validaciones.add(s);
	}
	
	public void eliminarSolicitudValidacion(SolicitudValidacion s)
	{
		validaciones.remove(s);
	}
}

