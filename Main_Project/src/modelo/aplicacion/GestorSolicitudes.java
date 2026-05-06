package modelo.aplicacion;

import java.util.*;

import modelo.notificacion.NotificacionEmpleado;
import modelo.producto.*;
import modelo.solicitud.*;
import java.io.*;

/**
 * Gestor centralizado que administra todas las solicitudes de la aplicación.
 * Gestiona de forma unificada los pedidos, intercambios y validaciones mediante el patrón Singleton.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class GestorSolicitudes implements Serializable{

	/**
	 * Devuelve una representación textual de todas las solicitudes gestionadas.
	 * @return Cadena de texto con las listas de intercambios, pedidos y validaciones.
	 */
  @Override
	public String toString() {
		return "\n\n\n====================================================" +
    "\n************GESTOR DE SOLICITUDES DE la APLICACION************" +
    "\n====================================================" +
    "[\n\nIntercambios" + intercambios + "\n\nPedidos:" + pedidos + "\n\nValidaciones:"
				+ validaciones + "]";

	}

	/** Identificador único para la serialización de la clase. */
  private static final long serialVersionUID = 1L;
	
	/** Instancia única de la clase (Singleton). */
	private static GestorSolicitudes instancia;
	
	/** Lista que almacena todas las solicitudes de intercambio del sistema. */
	private List<SolicitudIntercambio> intercambios = new ArrayList<>();
	
	/** Lista que almacena todos los pedidos de compra del sistema. */
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	
	/** Lista que almacena todas las solicitudes de validación de productos. */
	private List<SolicitudValidacion> validaciones = new ArrayList<>();
	
	/**
	 * Constructor privado para evitar la instanciación externa.
	 */
	private GestorSolicitudes() {
		
	}
	
	/**
	 * Obtiene la instancia única del GestorSolicitudes.
	 *
	 * @return La instancia global del gestor.
	 */
	public static GestorSolicitudes getInstancia() {
		if(instancia == null)
		{
		instancia = new GestorSolicitudes();	
		}
		return instancia;
	}

	/**
	 * Registra un nuevo pedido en el sistema.
	 *
	 * @param p el pedido a añadir.
	 * @throws IllegalArgumentException si el pedido es nulo.
	 */
	public void añadirPedido(SolicitudPedido p) {
		if(p == null)
		{
			throw new IllegalArgumentException("El pedido no es valido");
		}
		pedidos.add(p);
	}
	
	/**
	 * Elimina un pedido del registro del sistema.
	 *
	 * @param p el pedido a eliminar.
	 */
	public void eliminarPedido(SolicitudPedido p) {
		pedidos.remove(p);
	}
	
	/**
	 * Registra una nueva solicitud de intercambio en el sistema.
	 *
	 * @param s la solicitud de intercambio a añadir.
	 * @throws IllegalArgumentException si la solicitud es nula.
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
	 * Elimina una solicitud de intercambio del registro del sistema.
	 *
	 * @param s la solicitud de intercambio a eliminar.
	 */
	public void eliminarSolicitudIntercambio(SolicitudIntercambio s)
	{
	 intercambios.remove(s);
	}
	
	/**
	 * Registra una nueva solicitud de validación en el sistema.
	 *
	 * @param s la solicitud de validación a añadir.
	 * @throws IllegalArgumentException si la solicitud es nula.
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
	 * Elimina una solicitud de validación del registro del sistema.
	 *
	 * @param s la solicitud de validación a eliminar.
	 */
	public void eliminarSolicitudValidacion(SolicitudValidacion s)
	{
		validaciones.remove(s);
	}

	/**
	 * Obtiene la lista completa de intercambios registrados.
	 *
	 * @return Lista de solicitudes de intercambio.
	 */
	public List<SolicitudIntercambio> getIntercambios() {
		return intercambios;
	}

	/**
	 * Obtiene una lista filtrada únicamente con los intercambios que aún no han sido aprobados.
	 *
	 * @return Lista de solicitudes de intercambio pendientes de resolución.
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
	 * Obtiene la lista completa de pedidos registrados.
	 *
	 * @return Lista de solicitudes de pedido.
	 */
	public List<SolicitudPedido> getPedidos() {
		return pedidos;
	}

	/**
	 * Obtiene la lista completa de validaciones registradas.
	 *
	 * @return Lista de solicitudes de validación.
	 */
	public List<SolicitudValidacion> getValidaciones() {
		return validaciones;
	}

	/**
	 * Obtiene una lista filtrada únicamente con las validaciones pendientes de aprobar.
	 *
	 * @return Lista de solicitudes de validación pendientes.
	 */
	public List<SolicitudValidacion> getValidacionesPendientes() {
		List<SolicitudValidacion> validacionesPendientes = new ArrayList<>();
		for(SolicitudValidacion s: validaciones) {
			if(!s.getProductoAValidar().isValidado()) {
				validacionesPendientes.add(s);
			}
		}
		return validacionesPendientes;
	}
	
	/**
	 * Personaliza el proceso de serialización para guardar el estado del gestor.
	 * @param oos stream de salida de objetos.
	 * @throws IOException si existe un error de escritura.
	 */
  private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

	/**
	 * Personaliza el proceso de deserialización para restaurar el Singleton correctamente.
	 * @param ois stream de entrada de objetos.
	 * @throws IOException si existe un error de lectura.
	 * @throws ClassNotFoundException si no se encuentra la clase durante la carga.
	 */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        instancia = this;
    }  

}