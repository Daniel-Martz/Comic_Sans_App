package solicitud;

import java.util.*;
import tiempo.DateTimeSimulado;

/**
 * Clase que representa un pago realizado para una solicitud
 * 
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class Pago {

    /**
     * Fecha en la que se realizó el pago.
     */
	private final DateTimeSimulado fechaPago;

    /**
     * Importe del pago.
     */
	private final double importe;

    /**
     * Objeto de la solicitud que se está pagando.
     */
	private final Solicitud objetoPagado;

    /**
     * Constructor de la clase Pago.
     * 
     * @param fechaPago Fecha en que se realiza el pago.
     * @param importe Importe del pago.
     * @param objetoPagado Solicitud asociada al pago.
     */
	public Pago(DateTimeSimulado fechaPago, double importe, Solicitud objetoPagado) {
		super();
		this.fechaPago = fechaPago;
		this.importe = importe;
		this.objetoPagado = objetoPagado;
	}

    /**
     * Devuelve la fecha del pago.
     * 
     * @return Fecha de pago.
     */
	public DateTimeSimulado getFechaPago() {
		return fechaPago;
	}

    /**
     * Devuelve el importe del pago.
     * 
     * @return Importe pagado.
     */
	public double getImporte() {
		return importe;
	}

    /**
     * Devuelve la solicitud asociada al pago.
     * 
     * @return Objeto de la solicitud pagada.
     */
	public Solicitud getObjetoPagado() {
		return objetoPagado;
	}
}