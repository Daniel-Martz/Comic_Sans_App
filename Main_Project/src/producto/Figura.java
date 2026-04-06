package producto;

import java.io.File;
import java.util.*;

public class Figura extends LineaProductoVenta {
	private final String marca;
	private final String material;
	private final double dimensionX;
	private final double dimensionY;
	private final double dimensionZ;

	public Figura(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
			String marca, String material, double dimensionX, double dimensionY, double dimensionZ) {

		super(nombre, descripcion, foto, stock, precio);
		this.marca = marca;
		this.material = material;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.dimensionZ = dimensionZ;
	}

	public String getMarca() {
		return marca;
	}

	public String getMaterial() {
		return material;
	}

	public double getDimensionX() {
		return dimensionX;
	}

	public double getDimensionY() {
		return dimensionY;
	}

	public double getDimensionZ() {
		return dimensionZ;
	}
}
