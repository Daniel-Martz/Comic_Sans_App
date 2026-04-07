package descuento;

import tiempo.*;

/**
 * Clase abstracta base para los descuentos basados en un umbral de gasto mínimo.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class UmbralGasto extends Descuento {
	
	private static final long serialVersionUID = 1L;
	/** Cantidad mínima de dinero que se debe gastar para que el descuento sea aplicable. */
	private double umbral;
	
	/**
	 * Crea un descuento por umbral de gasto.
	 *
	 * @param fechaInicio la fecha en la que empieza a ser válido el descuento
	 * @param fechaFin    la fecha en la que caduca el descuento
	 * @param umbral      el gasto mínimo requerido
	 */
	public UmbralGasto(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, double umbral) {
		super(fechaInicio, fechaFin);
		this.umbral = umbral;
	}
	
	/**
	 * Obtiene el umbral de gasto necesario para el descuento.
	 *
	 * @return la cantidad del umbral
	 */
	public double getUmbral() {
		return umbral;
	}

}