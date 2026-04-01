package solicitud;

import java.util.*;
import tiempo.DateTimeSimulado;

public class Pago {
	private DateTimeSimulado fechaPago;
	private final double importe;
	
	public Pago(DateTimeSimulado fechaPago, double importe) {
		super();
		this.fechaPago = fechaPago;
		this.importe = importe;
	}
	
}
