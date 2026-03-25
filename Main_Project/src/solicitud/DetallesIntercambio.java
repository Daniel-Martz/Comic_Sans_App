package solicitud;
import java.util.*;
import tiempo.DateTimeSimulado;

public class DetallesIntercambio {
	private DateTimeSimulado fechaIntercambio;
	private String lugarIntercambio;
	
	public DetallesIntercambio(DateTimeSimulado fechaIntercambio, String lugarIntercambio) {
		this.fechaIntercambio = fechaIntercambio;
		this.lugarIntercambio = lugarIntercambio;
	}
}
