package filtro;
import producto.EstadoConservacion;
import java.util.*;

public class FiltroIntercambio extends Filtro {
	private double valorMin;
	private double valorMax;
	private Set<EstadoConservacion> estadosFiltrados = new HashSet<EstadoConservacion>();
	
	public FiltroIntercambio(boolean ordenAscendente, double valorMin, double valorMax) {
		super(ordenAscendente);
		this.valorMin = valorMin;
		this.valorMax = valorMax;
	}
	
	
	public void añadirEstadoConservacion(EstadoConservacion estado) {
		this.estadosFiltrados.add(estado);
	}

	public void eliminarEstadoConservacion(EstadoConservacion estado) {
		this.estadosFiltrados.remove(estado);
	}
}
