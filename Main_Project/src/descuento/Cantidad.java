package descuento;

import java.time.DateTimeSimulado;

public class Cantidad extends Descuento {
	private int numeroComprados;
	private int numeroRecibidos;
	public Cantidad(DateTimeSimulado fechaInicio, DateTimeSimulado fechaFin, int numeroComprados, int numeroRecibidos) {
		super(fechaInicio, fechaFin);
		this.numeroComprados = numeroComprados;
		this.numeroRecibidos = numeroRecibidos;
	}
	public int getNumeroComprados() {
		return numeroComprados;
	}
	public int getNumeroRecibidos() {
		return numeroRecibidos;
	}

}
