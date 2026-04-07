package filtro;

import java.io.Serializable;

/**
 * Clase base abstracta para la gestión de filtros en la aplicación.
 * Proporciona la funcionalidad básica de ordenación y permite la persistencia
 * mediante la interfaz Serializable.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public abstract class Filtro implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** Define si el orden de los resultados es ascendente (true) o descendente (false). */
	protected boolean ordenAscendente;

	/**
	 * Constructor base para los filtros.
	 * * @param ordenAscendente valor inicial para el sentido de la ordenación.
	 */
	public Filtro(boolean ordenAscendente) {
		super();
		this.ordenAscendente = ordenAscendente;
	}
	
	/**
	 * Indica el sentido actual de la ordenación.
	 * * @return true si es ascendente, false si es descendente.
	 */
	public boolean isOrdenAscendente() {
	    return ordenAscendente;
	}
	
	/**
	 * Restablece el filtro a su configuración por defecto.
	 * En esta clase base, establece el orden a descendente (false).
	 */
	public void limpiarFiltro() {
		ordenAscendente = false;
	}
	
}