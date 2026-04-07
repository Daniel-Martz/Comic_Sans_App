package descuento;

import tiempo.*;

/**
 * Descuento que aplica una rebaja porcentual al superar un umbral de gasto mínimo.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class RebajaUmbral extends UmbralGasto implements DePorcentaje{
	
	/** El porcentaje de rebaja a aplicar (ej. 15 para un 15%). */
	private int procentajeRebaja;

	/**
	 * Crea un descuento de rebaja por umbral.
	 *
	 * @param fechaInicio      la fecha en la que empieza a ser válido
	 * @param fechaFin         la fecha en la que caduca
	 * @param umbral           el gasto mínimo requerido
	 * @param procentajeRebaja el porcentaje a descontar
	 */
	public RebajaUmbral(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral, int procentajeRebaja) {
		super(fechaInicio, fechaFin, umbral);
		this.procentajeRebaja = procentajeRebaja;
	}

	/**
	 * Obtiene el porcentaje de la rebaja.
	 *
	 * @return el porcentaje
	 */
	public int getPorcentajeRebaja() {
		return procentajeRebaja;
	}

	
}