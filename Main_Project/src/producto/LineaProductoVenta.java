package producto;

<<<<<<< Updated upstream
public class LineaProductoVenta {

=======
import java.util.*;
import java.io.File;

public class LineaProductoVenta extends Producto {
	private int stock;
	private double precio;
	private int unidadesVendidas;
	private List<String> reseña = new ArrayList<String>();
	
	public LineaProductoVenta(String name, String descripcion, File foto, double precio, int unidadesVendidas)
	{
		super(name, descripcion, foto);
		this.precio = precio;
		this.unidadesVendidas = unidadesVendidas;
	}
	
	public void añadirCategoria()
	{
		
	}
>>>>>>> Stashed changes
}
