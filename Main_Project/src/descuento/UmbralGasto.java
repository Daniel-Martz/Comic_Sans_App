package descuento;

import tiempo.*;

public abstract class UmbralGasto extends Descuento {
	private double umbral;
	public UmbralGasto(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral) {
		super(fechaInicio, fechaFin);
		this.umbral = umbral;
	}
	public double getUmbral() {
		return umbral;
	}

}
