package producto;

public class DatosValidacion {
	
	private final double precioEstimadoProducto;
	private final EstadoConservacion estadoConservacion;
	
	public DatosValidacion(double precioEstimadoProducto, EstadoConservacion estadoConservacion) {
		this.precioEstimadoProducto = precioEstimadoProducto;
		this.estadoConservacion = estadoConservacion;
	}

	public double getPrecioEstimadoProducto() {
		return precioEstimadoProducto;
	}

	public EstadoConservacion getEstadoConservacion() {
		return estadoConservacion;
	}

}