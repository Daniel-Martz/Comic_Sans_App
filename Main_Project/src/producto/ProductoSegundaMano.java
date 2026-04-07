package producto;

import java.io.File;
import solicitud.*;
import usuario.ClienteRegistrado;
import aplicacion.Aplicacion;

/**
 * Representa un producto de segunda mano publicado por un cliente.
 * 
 * Puede recibir y enviar ofertas, y necesita validación antes de poder operar.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0

 */
public class ProductoSegundaMano extends Producto {

	private static final long serialVersionUID = 1L;
	private boolean validado;
	private Oferta ofertaRecibida;
	private Oferta ofertaEnviada;
	private final SolicitudValidacion solicitudValidacion;
	private DatosValidacion datosValidacion;
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

		Aplicacion.getInstancia().getGestorSolicitud().añadirSolicitudValidacion(solicitudValidacion);
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
