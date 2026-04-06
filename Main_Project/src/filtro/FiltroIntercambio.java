package filtro;
import producto.EstadoConservacion;
import java.util.*;


public class FiltroIntercambio extends Filtro {
	private double valorMin;
	private double valorMax;
	private Set<EstadoConservacion> estadosFiltrados = new HashSet<EstadoConservacion>();
	
	public FiltroIntercambio(boolean ordenAscendente, double valorMin, double valorMax) {
		if(valorMin < 0 || valorMax < valorMin) {
			throw new IllegalArgumentException("Los valores minimo y máximo deben de ser positivos y el máximo mayor que el mínimo");
		}
		super(ordenAscendente);
		this.valorMin = valorMin;
		this.valorMax = valorMax;
	}

	public double getValorMin() {
		return valorMin;
	}


	public double getValorMax() {
		return valorMax;
	}


	public Set<EstadoConservacion> getEstadosFiltrados() {
		return estadosFiltrados;
	}
	
	@Override
	public void limpiarFiltro() {
		valorMin = 0;
		valorMax = Double.MAX_VALUE;
		estadosFiltrados.clear();
		super.limpiarFiltro();
	}

	public void cambiarFiltro(boolean ordenAscendente, double valorMin, double valorMax, Set<EstadoConservacion> estados) {
		if (valorMin < 0) {
			throw new IllegalArgumentException("El valor mínimo no puede ser negativo");
		}
		if (valorMax < valorMin) {
			throw new IllegalArgumentException("El valor máximo no puede ser menor que el mínimo");
		}

		this.ordenAscendente = ordenAscendente;

		this.valorMin = valorMin;
		this.valorMax = valorMax;

		this.estadosFiltrados.clear();
		if (estados != null) {
			this.estadosFiltrados.addAll(estados);
		}
	}
	
}
