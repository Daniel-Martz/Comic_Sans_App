package filtro;
import java.util.*;

public class FiltroVentaCliente extends FiltroVenta {
	private double puntuacionMin;
	private double puntuacionMax;
	private double precioMin;
	private double precioMax;
	private boolean ordenarPorPrecio;
	private boolean ordenarPorPuntuacion;
	private Set<TipoDescuento> descuentoFiltrado = new HashSet<TipoDescuento>();

	public FiltroVentaCliente(boolean ordenAscendente, double puntuacionMin, double puntuacionMax, double precioMin, double precioMax,
		boolean ordenarPorPrecio, boolean ordenarPorPuntuacion) {
		super(ordenAscendente);
		this.puntuacionMin = puntuacionMin;
		this.puntuacionMax = puntuacionMax;
		this.precioMin = precioMin;
		this.precioMax = precioMax;
		this.ordenarPorPrecio = ordenarPorPrecio;
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;
	}

	public double getPuntuacionMin() {
		return puntuacionMin;
	}

	public void setPuntuacionMin(double puntuacionMin) {
		this.puntuacionMin = puntuacionMin;
	}

	public double getPuntuacionMax() {
		return puntuacionMax;
	}

	public void setPuntuacionMax(double puntuacionMax) {
		this.puntuacionMax = puntuacionMax;
	}

	public double getPrecioMin() {
		return precioMin;
	}

	public void setPrecioMin(double precioMin) {
		this.precioMin = precioMin;
	}

	public double getPrecioMax() {
		return precioMax;
	}

	public void setPrecioMax(double precioMax) {
		this.precioMax = precioMax;
	}

	public boolean isOrdenarPorPrecio() {
		return ordenarPorPrecio;
	}

	public void setOrdenarPorPrecio(boolean ordenarPorPrecio) {
		this.ordenarPorPrecio = ordenarPorPrecio;
	}
	
	public void añadirTipodescuento(TipoDescuento tipo) {
		this.descuentoFiltrado.add(tipo);
	}

	public void eliminarTipodescuento(TipoDescuento tipo) {
		this.descuentoFiltrado.remove(tipo);
	}

    public Set<TipoDescuento> getDescuentoFiltrado() { 
    	return descuentoFiltrado; 
    	}

	public boolean isOrdenarPorPuntuacion() {
		return ordenarPorPuntuacion;
	}

	public void setOrdenarPorPuntuacion(boolean ordenarPorPuntuacion) {
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;
	}
    
}
