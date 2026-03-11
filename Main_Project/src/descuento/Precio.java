package descuento;

import java.time.LocalDateTime;

public class Precio extends Descuento {
	private int porcentaje;

	public Precio(LocalDateTime fechaInicio, LocalDateTime fechaFin, int porcentaje) {
		super(fechaInicio, fechaFin);
		this.porcentaje = porcentaje;
	}
}
