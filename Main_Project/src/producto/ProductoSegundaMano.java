package producto;

import java.util.*;

public class ProductoSegundaMano extends Producto{
	private boolean validado;
	private Oferta oferta;
	private Solicitudvalidacion solicitudValidacion;
	private DatosValidacion datosValidacion;
	
	public ProductoSegundaMano(String nombre, String descripcion, File foto)
	{
		super(nombre, descripcion, foto);
		this.validado = false;
	}
}
