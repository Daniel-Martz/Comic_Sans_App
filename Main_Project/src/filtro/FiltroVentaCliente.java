package filtro;

import java.util.*;

import categoria.Categoria;

public class FiltroVentaCliente extends FiltroVenta {
	private double puntuacionMin;
	private double puntuacionMax;
	private double precioMin;
	private double precioMax;
	private boolean ordenarPorPrecio;
	private boolean ordenarPorPuntuacion;
	private Set<TipoDescuento> descuentoFiltrado = new HashSet<TipoDescuento>();

	public FiltroVentaCliente(boolean ordenAscendente, double puntuacionMin, double puntuacionMax, double precioMin,
			double precioMax, boolean ordenarPorPrecio, boolean ordenarPorPuntuacion) {
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

	public Set<TipoDescuento> getDescuentoFiltrado() {
		return descuentoFiltrado;
	}

	public boolean isOrdenarPorPuntuacion() {
		return ordenarPorPuntuacion;
	}

	public void setOrdenarPorPuntuacion(boolean ordenarPorPuntuacion) {
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;
	}

	@Override
	public void limpiarFiltro() {
		super.limpiarFiltro();
		puntuacionMin = 0.0;
		puntuacionMax = 5.0;
		precioMin = 0.0;
		precioMax = Double.MAX_VALUE;
		ordenarPorPrecio = false;
		ordenarPorPuntuacion = false;
		descuentoFiltrado.clear();
	}

	public void cambiarFiltro(boolean ordenAscendente, Set<Categoria> categorias, Set<TipoProducto> tipos,
			double puntuacionMin, double puntuacionMax, double precioMin, double precioMax, boolean ordenarPorPrecio,
			boolean ordenarPorPuntuacion, Set<TipoDescuento> descuentoFiltrado) {

		super.cambiarFiltro(ordenAscendente, categorias, tipos);
		
		if (puntuacionMin < 0 || puntuacionMax > 5.0) {
			throw new IllegalArgumentException("La puntuación debe estar entre 0 y 5");
		}
		if (puntuacionMin > puntuacionMax) {
			throw new IllegalArgumentException("La puntuación mínima no puede ser mayor que la máxima");
		}

		if (precioMin < 0) {
			throw new IllegalArgumentException("El precio mínimo no puede ser negativo");
		}
		if (precioMin > precioMax) {
			throw new IllegalArgumentException("El precio mínimo no puede ser superior al máximo");
		}

		this.puntuacionMin = puntuacionMin;
		this.puntuacionMax = puntuacionMax;
		this.precioMin = precioMin;
		this.precioMax = precioMax;
		this.ordenarPorPrecio = ordenarPorPrecio;
		this.ordenarPorPuntuacion = ordenarPorPuntuacion;

		this.descuentoFiltrado.clear();
		if (descuentoFiltrado != null) {
			this.descuentoFiltrado.addAll(descuentoFiltrado);
		}
	}
		
}
