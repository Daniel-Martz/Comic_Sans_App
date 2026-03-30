package producto;

import java.util.*;
import java.io.File;

public class JuegoDeMesa extends LineaProductoVenta {
	private int numeroJugadores;
	private int edadMinima;
	private int edadMaxima;
  private TipoJuegoMesa tipo;	
	public JuegoDeMesa(String nombre, String descripcion, File foto, int stock, double precio,
					   int numeroJugadores, int edadMinima, int edadMaxima, TipoJuegoMesa tipo) {
		
		super(nombre, descripcion, foto, stock, precio);
		this.numeroJugadores = numeroJugadores;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
    this.tipo = tipo;
	}

	public int getNumeroJugadores() {
		return numeroJugadores;
	}

	public void setNumeroJugadores(int numeroJugadores) {
		this.numeroJugadores = numeroJugadores;
	}

	public int getEdadMinima() {
		return edadMinima;
	}

	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}

	public int getEdadMaxima() {
		return edadMaxima;
	}

	public void setEdadMaxima(int edadMaxima) {
		this.edadMaxima = edadMaxima;
	}

  public TipoJuegoMesa getTipoJuegoDeMesa(){
    return this.tipo;
  }

  public void setTipoJuegoDeMesa(TipoJuegoMesa tipo){
    this.tipo = tipo;
  }
}
