package descuento;

import tiempo.*;

public class Precio extends Descuento{
	private int porcentaje;

	public int getPorcentajeRebaja() {
		return porcentaje;
	}

	public Precio(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, int porcentaje) {
		super(fechaInicio, fechaFin);
		this.porcentaje = porcentaje;
	}
}
