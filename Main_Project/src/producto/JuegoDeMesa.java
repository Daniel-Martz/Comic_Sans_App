package producto;

import java.util.*;
import java.io.File;

public class JuegoDeMesa extends LineaProductoVenta {
	private final int numeroJugadores;
	private final int edadMinima;
	private final int edadMaxima;
	private final TipoJuegoMesa tipo;

	public JuegoDeMesa(String nombre, String descripcion, File foto, int stock, double precio, int numeroJugadores,
			int edadMinima, int edadMaxima, TipoJuegoMesa tipo) {

		super(nombre, descripcion, foto, stock, precio);
		this.numeroJugadores = numeroJugadores;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
		this.tipo = tipo;
	}

	public int getNumeroJugadores() {
		return numeroJugadores;
	}

	public int getEdadMinima() {
		return edadMinima;
	}

	public int getEdadMaxima() {
		return edadMaxima;
	}

	public TipoJuegoMesa getTipoJuegoDeMesa() {
		return this.tipo;
	}

}
