package modelo.solicitud;

import modelo.tiempo.*;

/**
 * Clase que representa una solicitud de intercambio.
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class SolicitudIntercambio extends Solicitud {

    private static final long serialVersionUID = 1L;

	/**
     * Detalles del intercambio, incluyendo fecha y lugar.
     */
	private DetallesIntercambio informacionIntercambio;

    /**
     * Código de validación del ofertante.
     */
	private CodigoIntercambio codigoOfertante;

    /**
     * Código de validación del destinatario.
     */
	private CodigoIntercambio codigoDestinatario;

    /**
     * Oferta asociada al intercambio.
     */
	private Oferta oferta;

    /**
     * Indica si el intercambio ha sido aprobado.
     */
	private boolean aprobado = false;

    /**
     * Constructor de la clase.
     * Inicializa los códigos de las partes, la información del intercambio y la oferta.
     * 
     * @param codigoOfertante Código del ofertante.
     * @param codigoDestinatario Código del destinatario.
     * @param lugarIntercambio Lugar donde se realizará el intercambio.
     * @param oferta Oferta asociada al intercambio.
     */
	public SolicitudIntercambio(String codigoOfertante, String codigoDestinatario, String lugarIntercambio, Oferta oferta) {
		this.codigoDestinatario = new CodigoIntercambio(codigoDestinatario);
		this.codigoOfertante = new CodigoIntercambio(codigoOfertante);
		this.informacionIntercambio = new DetallesIntercambio(new DateTimeSimulado(), lugarIntercambio);
		this.oferta = oferta;
	}

    /**
     * Aprueba el intercambio si los códigos proporcionados coinciden con los códigos almacenados.
     * 
     * @param codigoOfertante Código del ofertante para validar.
     * @param codigoDestinatario Código del destinatario para validar.
     */
	public void aprobarIntercambio(String codigoOfertante, String codigoDestinatario) {
		// 1. Validamos que los códigos sean correctos
		if (!this.codigoOfertante.validarCodigo(codigoOfertante) || !this.codigoDestinatario.validarCodigo(codigoDestinatario)) {
			throw new IllegalArgumentException("The security codes provided are incorrect.");
		}		
		// 2. Marcamos el intercambio como completado/aprobado
		this.aprobado = true;
		
		// 3. Liberamos los productos de su estado "Pendiente" y limpiamos las referencias de la oferta
		for (modelo.producto.ProductoSegundaMano p : this.oferta.productosOfertados()) {
			p.setPendienteAprobacionIntercambio(false);
			p.eliminarOfertaEnviada();
		}
		
		for (modelo.producto.ProductoSegundaMano p : this.oferta.productosSolicitados()) {
			p.setPendienteAprobacionIntercambio(false);
			p.eliminarOfertaRecibida();
		}
	}

    /**
     * Asigna o actualiza la oferta asociada a la solicitud de intercambio.
     * 
     * @param o Nueva oferta a asignar.
     */
	public void addOferta(Oferta o) {
		this.oferta = o;
	}

    /**
     * Indica si la solicitud de intercambio ha sido aprobada.
     * 
     * @return true si está aprobada, false en caso contrario.
     */
	public boolean esAprobado() {
		return aprobado;
	}

    /**
     * Devuelve los detalles del intercambio, incluyendo fecha y lugar.
     * 
     * @return Detalles del intercambio.
     */
	public DetallesIntercambio getInformacionIntercambio() {
		return informacionIntercambio;
	}

    /**
     * Devuelve el código del ofertante para validación.
     * 
     * @return Código del ofertante.
     */
	public CodigoIntercambio getCodigoOfertante() {
		return codigoOfertante;
	}
	
    /**
     * Devuelve el código del destinatario para validación.
     * 
     * @return Código del destinatario.
     */
	public CodigoIntercambio getCodigoDestinatario() {
		return codigoDestinatario;
	}
	

	
	/**
	 * 
	 * Devuelve la oferta asociada a la solicitud de intercambio.
	 * @return the oferta
	 */
	public Oferta getOferta() {
		return oferta;
	}


	@Override
	public String toString() {
	    return "SolicitudIntercambio [\n" +
	           "  aprobado=" + aprobado + "\n" +
	           "  codigoOfertante=" + codigoOfertante + "\n" +
	           "  codigoDestinatario=" + codigoDestinatario + "\n" +
	           "  intercambio=" + informacionIntercambio + "\n" +
	           "  oferta=" + oferta + "\n" +
	           "]";
	}
}
