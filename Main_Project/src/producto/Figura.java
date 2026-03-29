package producto;

import java.io.File;
import java.util.*;

public class Figura extends LineaProductoVenta {
	private String marca;
	private String material;
	private double dimensionX;
	private double dimensionY;
	private double dimensionZ;


	public Figura(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
			String marca, String material, double dimesionX, double dimesionY, double dimesionZ) {
		
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


	public void setMarca(String marca) {
		this.marca = marca;
	}


	public String getMaterial() {
		return material;
	}


	public void setMaterial(String material) {
		this.material = material;
	}


	public double getDimensionX() {
		return dimensionX;
	}


	public void setDimensionX(double dimensionX) {
		this.dimensionX = dimensionX;
	}


	public double getDimensionY() {
		return dimensionY;
	}


	public void setDimensionY(double dimensionY) {
		this.dimensionY = dimensionY;
	}


	public double getDimensionZ() {
		return dimensionZ;
	}


	public void setDimensionZ(double dimensionZ) {
		this.dimensionZ = dimensionZ;
	}	
}
