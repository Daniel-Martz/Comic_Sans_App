package descuento;

import tiempo.*;

/**
 * Descuento directo que aplica una rebaja porcentual sobre el precio.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Precio extends Descuento implements DePorcentaje{
	
	/** El porcentaje de rebaja a aplicar. */
	private int porcentaje;

	/**
	 * Obtiene el porcentaje de la rebaja.
	 *
	 * @return el porcentaje
	 */
	public int getPorcentajeRebaja() {
		return porcentaje;
	}

	/**
	 * Crea un descuento directo por precio.
	 *
	 * @param fechaInicio la fecha en la que empieza a ser válido
	 * @param fechaFin    la fecha en la que caduca
	 * @param porcentaje  el porcentaje a descontar
	 */
	public Precio(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, int porcentaje) {
		super(fechaInicio, fechaFin);
		this.porcentaje = porcentaje;
	}
}