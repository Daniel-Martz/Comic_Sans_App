package producto;

public class DatosValidacion {
	
	private double precioEstimadoProducto;
	private EstadoConservacion estadoConservacion;
	
	public DatosValidacion(double precioEstimadoProducto, EstadoConservacion estadoConservacion) {
		this.precioEstimadoProducto = precioEstimadoProducto;
		this.estadoConservacion = estadoConservacion;
	}

	public double getPrecioEstimadoProducto() {
		return precioEstimadoProducto;
	}

	public void setPrecioEstimadoProducto(double precioEstimadoProducto) {
		this.precioEstimadoProducto = precioEstimadoProducto;
	}

	public EstadoConservacion getEstadoConservacion() {
		return estadoConservacion;
	}

	public void setEstadoConservacion(EstadoConservacion estadoConservacion) {
		this.estadoConservacion = estadoConservacion;
	}
	
}