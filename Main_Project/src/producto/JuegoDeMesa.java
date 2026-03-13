package producto;

import java.util.*;
import java.io.File;

public class JuegoDeMesa extends LineaProductoVenta {
	private int numeroJugadores;
	private int edadMinima;
	private int edadMaxima;
	
	public JuegoDeMesa(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
					   int numeroJugadores, int edadMinima, int edadMaxima, Tipo tipo) {
		
		super(nombre, descripcion, foto, stock, precio, unidadesVendidas);
		this.numeroJugadores = numeroJugadores;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
	}
}