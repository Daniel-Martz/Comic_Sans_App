package aplicacion;

import java.util.*;
import producto.*;
import solicitud.*;
import java.io.*;

// TODO: Auto-generated Javadoc
/**
 * The Class GestorSolicitudes.
 */
public class GestorSolicitudes {
	
	/** El instancia. */
	private static GestorSolicitudes instancia;
	
	/** El intercambios. */
	private List<SolicitudIntercambio> intercambios = new ArrayList<>();
	
	/** El pedidos. */
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	
	/** El validaciones. */
	private List<SolicitudValidacion> validaciones = new ArrayList<>();
	
	/**
	 * Instancia un nuevo gestor solicitudes.
	 */
	private GestorSolicitudes() {
		
	}
	
	/**
	 * Devuelve el instancia.
	 *
	 * @return el instancia
	 */
	public static GestorSolicitudes getInstancia() {
		if(instancia == null)
		{
		instancia = new GestorSolicitudes();	
		}
		return instancia;
	}

	/**
	 * Añadir pedido.
	 *
	 * @param p el p
	 */
	public void añadirPedido(SolicitudPedido p) {
		if(p == null)
		{
			throw new IllegalArgumentException("El pedido no es valido");
		}
		pedidos.add(p);
	}
	
	/**
	 * Eliminar pedido.
	 *
	 * @param p el p
	 */
	public void eliminarPedido(SolicitudPedido p) {
		pedidos.remove(p);
	}
	
	/**
	 * Añadir solicitud intercambio.
	 *
	 * @param s el s
	 */
	public void añadirSolicitudIntercambio(SolicitudIntercambio s)
	{
		if(s == null)
		{
			throw new IllegalArgumentException("La solicitud no es valida");
		}
		intercambios.add(s);
	}
	
	/**
	 * Eliminar solicitud intercambio.
	 *
	 * @param s el s
	 */
	public void eliminarSolicitudIntercambio(SolicitudIntercambio s)
	{
	 intercambios.remove(s);
	}
	
	/**
	 * Añadir solicitud validacion.
	 *
	 * @param s el s
	 */
	public void añadirSolicitudValidacion(SolicitudValidacion s)
	{
		if(s == null)
		{
			throw new IllegalArgumentException("La solicitud no es valida");
		}
		validaciones.add(s);
	}
	
	/**
	 * Eliminar solicitud validacion.
	 *
	 * @param s el s
	 */
	public void eliminarSolicitudValidacion(SolicitudValidacion s)
	{
		validaciones.remove(s);
	}
/**
	 * Devuelve el intercambios.
	 *
	 * @return el intercambios
	 */
	public List<SolicitudIntercambio> getIntercambios() {
		return intercambios;
	}

	/**
	 * Devuelve el intercambios pendientes.
	 *
	 * @return el intercambios pendientes
	 */
	public List<SolicitudIntercambio> getIntercambiosPendientes() {
		List<SolicitudIntercambio> intercambiosPendientes = new ArrayList<>();
		for(SolicitudIntercambio s: intercambios)
		{
			if(!s.esAprobado())
			{
				intercambiosPendientes.add(s);
			}
		}
		return intercambiosPendientes;
	}
	
	/**
	 * Devuelve el pedidos.
	 *
	 * @return el pedidos
	 */
	public List<SolicitudPedido> getPedidos() {
		return pedidos;
	}

	/**
	 * Devuelve el validaciones.
	 *
	 * @return el validaciones
	 */
	public List<SolicitudValidacion> getValidaciones() {
		return validaciones;
	}
	


}

