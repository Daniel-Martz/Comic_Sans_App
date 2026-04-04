package descuento;

import tiempo.*;

public class RebajaUmbral extends UmbralGasto implements DePorcentaje{
	private int procentajeRebaja;

	public RebajaUmbral(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral, int procentajeRebaja) {
		super(fechaInicio, fechaFin, umbral);
		this.procentajeRebaja = procentajeRebaja;
	}

	public int getPorcentajeRebaja() {
		return procentajeRebaja;
	}

	
}
