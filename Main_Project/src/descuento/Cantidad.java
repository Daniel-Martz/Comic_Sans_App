package descuento;

import java.time.LocalDateTime;

public class Cantidad extends Descuento {
	private int numeroComprados;
	private int numeroRecibidos;
	public Cantidad(LocalDateTime fechaInicio, LocalDateTime fechaFin, int numeroComprados, int numeroRecibidos) {
		super(fechaInicio, fechaFin);
		this.numeroComprados = numeroComprados;
		this.numeroRecibidos = numeroRecibidos;
	}

}
