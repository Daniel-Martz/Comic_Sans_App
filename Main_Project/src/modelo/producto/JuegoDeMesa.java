package modelo.producto;

import java.io.File;

/**
 * Representa un juego de mesa disponible para venta.
 * 
 * Incluye información específica como:
 * - número de jugadores
 * - rango de edad recomendado
 * - tipo de juego
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class JuegoDeMesa extends LineaProductoVenta {

	/** Identificador de versión para serialización. */
	private static final long serialVersionUID = 1L;

	/** Número de jugadores permitidos. */
	private int numeroJugadores;

	/** Edad mínima recomendada. */
	private int edadMinima;

	/** Edad máxima recomendada. */
	private int edadMaxima;

	/** Tipo de juego de mesa. */
	private TipoJuegoMesa tipo;

	/**
	 * Construye un juego de mesa.
	 *
	 * @param nombre nombre del producto
	 * @param descripcion descripción
	 * @param foto imagen asociada
	 * @param stock unidades disponibles
	 * @param precio precio unitario
	 * @param numeroJugadores número recomendado de jugadores
	 * @param edadMinima edad mínima recomendada
	 * @param edadMaxima edad máxima recomendada
	 * @param tipo tipo de juego de mesa
	 */
	public JuegoDeMesa(String nombre, String descripcion, File foto, int stock, double precio,
			int numeroJugadores, int edadMinima, int edadMaxima, TipoJuegoMesa tipo) {

		super(nombre, descripcion, foto, stock, precio);

		this.numeroJugadores = numeroJugadores;
		this.edadMinima = edadMinima;
		this.edadMaxima = edadMaxima;
		this.tipo = tipo;
	}

	/**
	 * Devuelve el número recomendado de jugadores.
	 *
	 * @return número de jugadores
	 */
	public int getNumeroJugadores() {
		return numeroJugadores;
	}

	/**
	 * Devuelve la edad mínima recomendada.
	 *
	 * @return edad mínima
	 */
	public int getEdadMinima() {
		return edadMinima;
	}

	/**
	 * Devuelve la edad máxima recomendada.
	 *
	 * @return edad máxima
	 */
	public int getEdadMaxima() {
		return edadMaxima;
	}

	/**
	 * Devuelve el tipo de juego de mesa.
	 *
	 * @return tipo de juego
	 */
	public TipoJuegoMesa getTipoJuegoDeMesa() {
		return this.tipo;
	}

	public void setNumeroJugadores(int numeroJugadores) {
		this.numeroJugadores = numeroJugadores;
	}

	public void setEdadMinima(int edadMinima) {
		this.edadMinima = edadMinima;
	}

	public void setEdadMaxima(int edadMaxima) {
		this.edadMaxima = edadMaxima;
	}

	public void setTipoJuegoDeMesa(TipoJuegoMesa tipo) {
		this.tipo = tipo;
	}
}
