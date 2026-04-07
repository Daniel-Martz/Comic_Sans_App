package descuento;

import tiempo.*;

/**
 * Descuento basado en cantidad de productos (promociones del tipo "Lleva X y paga Y").
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Cantidad extends Descuento {
	
	/** Número de unidades que el cliente paga para aplicar la promoción. */
	private int numeroComprados;
	
	/** Número total de unidades que el cliente recibe con la promoción. */
	private int numeroRecibidos;
	
	/**
	 * Crea un descuento por cantidad.
	 *
	 * @param fechaInicio     la fecha en la que empieza a ser válido
	 * @param fechaFin        la fecha en la que caduca
	 * @param numeroComprados las unidades que se cobran
	 * @param numeroRecibidos las unidades totales que se entregan al cliente
	 */
	public Cantidad(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, int numeroComprados, int numeroRecibidos) {
		super(fechaInicio, fechaFin);
		this.numeroComprados = numeroComprados;
		this.numeroRecibidos = numeroRecibidos;
	}
	
	/**
	 * Obtiene el número de unidades que el cliente debe pagar.
	 *
	 * @return la cantidad cobrada
	 */
	public int getNumeroComprados() {
		return numeroComprados;
	}
	
	/**
	 * Obtiene el número total de unidades que el cliente se lleva.
	 *
	 * @return la cantidad recibida
	 */
	public int getNumeroRecibidos() {
		return numeroRecibidos;
	}

}