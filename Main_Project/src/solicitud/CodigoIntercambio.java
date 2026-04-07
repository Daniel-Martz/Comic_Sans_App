package solicitud;

/**
 * Representa un código asociado a un intercambio para su validación o seguimiento.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class CodigoIntercambio {
	
	/** El texto que representa el código de intercambio. */
	private String codigo;
	
	/**
	 * Instancia un nuevo código de intercambio.
	 *
	 * @param codigo el texto que conforma el código
	 */
	public CodigoIntercambio(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Comprueba si un código proporcionado coincide con el código almacenado.
	 *
	 * @param codigo el código que se desea validar
	 * @return true si los códigos coinciden, false en caso contrario
	 */
	public boolean validarCodigo(String codigo) {
		return this.codigo.equals(codigo);
	}

	/**
	 * Obtiene el texto del código de intercambio.
	 *
	 * @return el código
	 */
	public String getCodigo() {
		return codigo;
	}
}