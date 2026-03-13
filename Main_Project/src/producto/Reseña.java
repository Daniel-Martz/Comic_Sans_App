package producto;

import java.util.*;

public class Reseña {
	
	private String descripcion;
	private double puntuacion;
	private Date fecha;
	private LineaProductoVenta producto;
	
	public Reseña(String descripcion, double puntuacion, Date fecha, LineaProductoVenta producto) {
		this.descripcion = descripcion;
		this.puntuacion = puntuacion;
		this.fecha = fecha;
		this.producto = producto;
	}
}