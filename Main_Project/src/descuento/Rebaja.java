package descuento;

import java.time.LocalDateTime;

public class Rebaja extends UmbralGasto {
	private int procentajeRebaja;

	public Rebaja(LocalDateTime fechaInicio, LocalDateTime fechaFin, double umbral, int procentajeRebaja) {
		super(fechaInicio, fechaFin, umbral);
		this.procentajeRebaja = procentajeRebaja;
	}

	public int getProcentajeRebaja() {
		return procentajeRebaja;
	}

	
}
