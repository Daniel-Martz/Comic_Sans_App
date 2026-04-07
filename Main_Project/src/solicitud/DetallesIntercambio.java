package solicitud;
import java.util.*;
import tiempo.DateTimeSimulado;

import java.io.Serializable;
/**
 * Representa los detalles especificos de un intercambio, 
 * incluyendo la fecha y el lugar donde se llevara a cabo.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class DetallesIntercambio implements Serializable{
	
	/** Fecha en la que se realizara o se acordo el intercambio. */
	private DateTimeSimulado fechaIntercambio;
	
	/** Lugar fisico donde se llevara a cabo el intercambio. */
	private String lugarIntercambio;
	
	/**
	 * Instancia unos nuevos detalles de intercambio.
	 *
	 * @param fechaIntercambio la fecha acordada para el intercambio
	 * @param lugarIntercambio el lugar acordado para el intercambio
	 */
	public DetallesIntercambio(DateTimeSimulado fechaIntercambio, String lugarIntercambio) {
		this.fechaIntercambio = fechaIntercambio;
		this.lugarIntercambio = lugarIntercambio;
	}

}