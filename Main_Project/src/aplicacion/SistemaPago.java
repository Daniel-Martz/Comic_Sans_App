package aplicacion; 
import solicitud.*;

public class SistemaPago {
	
	private static SistemaPago instancia;
	
	private SistemaPago() {
	}
	
	public static SistemaPago getInstancia() {
		if (instancia == null) {
			instancia = new SistemaPago();
		}
		return instancia;
	}
	
	public Pago procesarPago(int importe, int numTarjeta, int cvv, int mesCaducidad, int añoCaducidad) {
		return null; 
	}
}