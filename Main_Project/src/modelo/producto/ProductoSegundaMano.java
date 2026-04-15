package modelo.producto;

import java.io.File;
import modelo.solicitud.*;
import modelo.usuario.ClienteRegistrado;
import modelo.aplicacion.Aplicacion;

/**
 * Representa un producto de segunda mano publicado por un cliente.
 * 
 * Puede recibir y enviar ofertas, y necesita validación antes de poder operar.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0

 */
public class ProductoSegundaMano extends Producto {

	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;
	
	/** Indica si el producto de segunda mano ha sido validado por un empleado. */
	private boolean validado;
	
	/** Oferta de intercambio que ha recibido este producto. */
	private Oferta ofertaRecibida;
	
	/** Oferta de intercambio en la que se ha incluido este producto. */
	private Oferta ofertaEnviada;
	
	/** Solicitud de validación generada para este producto. */
	private final SolicitudValidacion solicitudValidacion;
	
	/** Datos resultantes del proceso de validación (estado, precio estimado, etc.). */
	private DatosValidacion datosValidacion;
	
	/** Cliente propietario de este producto de segunda mano. */
	private ClienteRegistrado clienteProducto;

	/**
	 * Construye un producto de segunda mano y genera automáticamente
	 * su solicitud de validación.
	 *
	 * @param nombre nombre del producto
	 * @param descripcion descripción del producto
	 * @param foto imagen asociada
	 * @param cliente propietario del producto
	 */
	public ProductoSegundaMano(String nombre, String descripcion, File foto, ClienteRegistrado cliente) {
		super(nombre, descripcion, foto);
		this.validado = false;
		this.clienteProducto = cliente;
		this.solicitudValidacion = new SolicitudValidacion(this, cliente);

		Aplicacion.getInstancia().añadirSolicitudValidacion(solicitudValidacion);
	}

	/**
	 * Devuelve el propietario del producto.
	 *
	 * @return cliente propietario
	 */
	public ClienteRegistrado getClienteProducto() {
		return clienteProducto;
	}

	/**
	 * Indica si el producto ha sido validado.
	 *
	 * @return true si está validado
	 */
	public boolean isValidado() {
		return validado;
	}

	/**
	 * Devuelve la oferta recibida.
	 *
	 * @return oferta recibida
	 */
	public Oferta getOfertaRecibida() {
		return ofertaRecibida;
	}

	/**
	 * Añade una oferta recibida.
	 *
	 * @param ofertaRecibida oferta a añadir
	 * @throws IllegalStateException si el producto está bloqueado
	 */
	public void addOfertaRecibida(Oferta ofertaRecibida) {
		if (this.estaBloqueado()) {
			throw new IllegalStateException("El producto se encuentra bloqueado");
		}
		this.ofertaRecibida = ofertaRecibida;
	}

	/**
	 * Elimina la oferta recibida.
	 */
	public void eliminarOfertaRecibida() {
		this.ofertaRecibida = null;
	}

	/**
	 * Devuelve la oferta enviada.
	 *
	 * @return oferta enviada
	 */
	public Oferta getOfertaEnviada() {
		return ofertaEnviada;
	}

	/**
	 * Añade una oferta enviada.
	 *
	 * @param ofertaEnviada oferta a añadir
	 * @throws IllegalStateException si el producto está bloqueado
	 */
	public void addOfertaEnviada(Oferta ofertaEnviada) {
		if (this.estaBloqueado()) {
			throw new IllegalStateException("El producto se encuentra bloqueado");
		}
		this.ofertaEnviada = ofertaEnviada;
	}

	/**
	 * Elimina la oferta enviada.
	 */
	public void eliminarOfertaEnviada() {
		this.ofertaEnviada = null;
	}

	/**
	 * Devuelve la solicitud de validación.
	 *
	 * @return solicitud de validación
	 */
	public SolicitudValidacion getSolicitudValidacion() {
		return solicitudValidacion;
	}

	/**
	 * Devuelve los datos de validación.
	 *
	 * @return datos de validación
	 */
	public DatosValidacion getDatosValidacion() {
		return datosValidacion;
	}

	/**
	 * Modifica los datos de validación.
	 *
	 * @param datosValidacion nuevos datos
	 */
	public void setDatosValidacion(DatosValidacion datosValidacion) {
		this.datosValidacion = datosValidacion;
	}

	/**
	 * Valida el producto asignando precio y estado.
	 *
	 * @param precio precio asignado
	 * @param estado estado de conservación
	 */
	public void validarProducto(double precio, EstadoConservacion estado) {
		if (precio < 0 || estado == null) {
			throw new IllegalArgumentException("Datos de validación no válidos");
		}
		this.validado = true;
		this.datosValidacion = new DatosValidacion(precio, estado);
	}

	/**
	 * Indica si el producto está bloqueado.
	 *
	 * Está bloqueado si:
	 * - tiene oferta recibida
	 * - tiene oferta enviada
	 * - no ha sido validado
	 *
	 * @return true si está bloqueado
	 */
	public boolean estaBloqueado() {
		return (ofertaRecibida != null || ofertaEnviada != null || this.datosValidacion == null);
	}

	@Override
	public String toString() {
		return "ProductoSegundaMano [validado=" + validado +
				", ofertaRecibida=" + ofertaRecibida +
				", ofertaEnviada=" + ofertaEnviada +
				", datosValidacion=" + datosValidacion + "]";
	}
}
