package producto;

import java.util.*;

public class Reseña {
	
	private String descripcion;
	private double puntuacion;
	private DateTimeSimulado fecha;
	private LineaProductoVenta producto;
	
	public Reseña(String descripcion, double puntuacion, DateTimeSimulado fecha, LineaProductoVenta producto) {
		this.descripcion = descripcion;
		this.puntuacion = puntuacion;
		this.fecha = fecha;
		this.producto = producto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}

	public DateTimeSimulado getFecha() {
		return fecha;
	}

	public void setFecha(DateTimeSimulado fecha) {
		this.fecha = fecha;
	}

	public LineaProductoVenta getProducto() {
		return producto;
	}

	public void setProducto(LineaProductoVenta producto) {
		this.producto = producto;
	}
	
}
