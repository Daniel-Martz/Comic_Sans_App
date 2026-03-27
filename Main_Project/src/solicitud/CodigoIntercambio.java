package solicitud;

public class CodigoIntercambio {
	private String codigo;
	
	public CodigoIntercambio(String codigo) {
		this.codigo = codigo;
	}

	public boolean validarCodigo(String codigo) {
		return this.codigo == codigo;
	}

}	
