package producto;

import java.io.File;

/**
 * Representa una figura coleccionable disponible para venta.
 * 
 * Incluye marca, material y dimensiones físicas.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class Figura extends LineaProductoVenta {

	private final String marca;
	private final String material;
	private final double dimensionX;
	private final double dimensionY;
	private final double dimensionZ;

	/**
	 * Construye una figura coleccionable.
	 *
	 * @param nombre nombre del producto
	 * @param descripcion descripción
	 * @param foto imagen asociada
	 * @param stock unidades disponibles
	 * @param precio precio unitario
	 * @param unidadesVendidas unidades vendidas (actualmente no utilizado)
	 * @param marca marca de la figura
	 * @param material material principal
	 * @param dimensionX dimensión en eje X
	 * @param dimensionY dimensión en eje Y
	 * @param dimensionZ dimensión en eje Z
	 */
	public Figura(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
			String marca, String material, double dimensionX, double dimensionY, double dimensionZ) {

		super(nombre, descripcion, foto, stock, precio);

		this.marca = marca;
		this.material = material;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.dimensionZ = dimensionZ;
	}

	/**
	 * Devuelve la marca de la figura.
	 *
	 * @return marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * Devuelve el material de la figura.
	 *
	 * @return material
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * Devuelve dimensión X.
	 *
	 * @return dimensión X
	 */
	public double getDimensionX() {
		return dimensionX;
	}

	/**
	 * Devuelve dimensión Y.
	 *
	 * @return dimensión Y
	 */
	public double getDimensionY() {
		return dimensionY;
	}

	/**
	 * Devuelve dimensión Z.
	 *
	 * @return dimensión Z
	 */
	public double getDimensionZ() {
		return dimensionZ;
	}
}