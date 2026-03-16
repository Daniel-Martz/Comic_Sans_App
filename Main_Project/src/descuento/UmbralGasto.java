package descuento;

import java.time.LocalDateTime;

public abstract class UmbralGasto extends Descuento {
	private double umbral;
	public UmbralGasto(LocalDateTime fechaInicio, LocalDateTime fechaFin, double umbral) {
		super(fechaInicio, fechaFin);
		this.umbral = umbral;
	}
	public double getUmbral() {
		return umbral;
	}

}
