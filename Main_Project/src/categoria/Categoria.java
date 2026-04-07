package categoria;
import java.util.*;
import descuento.Descuento;
import producto.LineaProductoVenta;

/**
  * Esta clase implementa las categorías de la aplicación.
  *
  * @author Matteo Artuñedo González
  * @version 1.0
  * @date 13-03-2026
  */
public class Categoria {
    
    /** El nombre de la categoría. */
    private String nombre;
    
    /** El descuento de la categoria. */
    private Descuento descuentoCategoria;
    
    /** Los productos pertenecientes a la categoria. */
    private Set<LineaProductoVenta> productosCategoria = new HashSet<LineaProductoVenta>();

    /**
     * Instancia una nueva categoria.
     *
     * @param name nombre de la categoría
     */
    public Categoria(String name) {
        this.nombre = name;
    }

    /**
     * Obtiene el nombre.
     *
     * @return el nombre de la categoría
     */
    public String getNombre() { 
        return nombre; 
    }
    
    /**
     *  Añade un descuento asociado a la categoría.
     *
     * @param desc el descuento que tendrá la categoría 
     * @return false si la categoría ya tiene un descuento, true en el resto de casos.
     */
    public boolean añadirDescuento(Descuento desc) {
    	if (this.descuentoCategoria != null) {
    		return false;
    	}
    	
    	this.descuentoCategoria = desc;
    	return true;
    }
    
    /**
     * Elimina el descuento que pudiese tener asociado la categoría.
     *
     * @param desc the desc
     */
    public void eliminarDescuento(Descuento desc) {
    	this.descuentoCategoria = null;
    }
    
    /**
     * Obtiene el descuento asociado a la categoría
     *
     * @return el descuento asociado a la categoría
     */
    public Descuento getDescuento() {
    	return this.descuentoCategoria;
    }
    
    /**
     * Añade un producto a la categoria.
     *
     * @param prod el producto que queremos que pertenezca a la categoría
     */
    public void añadirProductoACategoria(LineaProductoVenta prod) {
        if (this.productosCategoria.contains(prod)) {
            return; 
        }
        this.productosCategoria.add(prod);
        prod.añadirCategoria(this);
    }

    /**
     * Elimina un producto de la categoria.
     *
     * @param prod el producto que ya no queremos que pertenezca a la categoría
     */
    public void eliminarProductoACategoria(LineaProductoVenta prod) {
    	this.productosCategoria.remove(prod);
    }
    
    /**
     * Obtener los productos de la categoria.
     *
     * @return el conjunto de los productos que pertenecen a la categoría 
     */
    public Set<LineaProductoVenta> obtenerProductosCategoria() {
    	return this.productosCategoria;
    }

    /**
     * Establecer el nombre de la categoria. 
     */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	@Override
	public String toString() {
	    return this.nombre; // Para que en las listas solo salga el nombre
	}
    
}