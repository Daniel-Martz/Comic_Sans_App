package descuento;

import java.time.DateTimeSimulado;

public class Rebaja extends UmbralGasto {
	private int procentajeRebaja;

	public Rebaja(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral, int procentajeRebaja) {
		super(fechaInicio, fechaFin, umbral);
		this.procentajeRebaja = procentajeRebaja;
	}

	public int getProcentajeRebaja() {
		return procentajeRebaja;
	}

	
}
