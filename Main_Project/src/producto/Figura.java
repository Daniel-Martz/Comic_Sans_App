package producto;

import java.io.File;
import java.util.*;

public class Figura extends LineaProductoVenta {
	private String marca;
	private String material;
	private double dimensionX;
	private double dimensionY;
	private double dimensionZ;
	private TipoJuegoMesa tipo;


	public Figura(String nombre, String descripcion, File foto, int stock, double precio, int unidadesVendidas,
			String marca, String material, double dimesionX, double dimesionY, double dimesionZ, TipoJuegoMesa tipo) {
		
		super(nombre, descripcion, foto, stock, precio, unidadesVendidas);
		this.marca = marca;
		this.material = material;
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		this.dimensionZ = dimensionZ;
		this.tipo = tipo;
	}
}
