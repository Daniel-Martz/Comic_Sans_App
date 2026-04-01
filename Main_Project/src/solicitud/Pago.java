package solicitud;

import java.util.*;
import tiempo.DateTimeSimulado;

public class Pago {
	private final DateTimeSimulado fechaPago;
	private final double importe;
	private final Solicitud objetoPagado;
	
	public Pago(DateTimeSimulado fechaPago, double importe, Solicitud objetoPagado) {
		super();
		this.fechaPago = fechaPago;
		this.importe = importe;
		this.objetoPagado = objetoPagado;
	}

	/**
	 * @return the fechaPago
	 */
	public DateTimeSimulado getFechaPago() {
		return fechaPago;
	}

	/**
	 * @return the importe
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * @return the objetoPagado
	 */
	public Solicitud getObjetoPagado() {
		return objetoPagado;
	}
	
	
}
