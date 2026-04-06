package producto;
import java.io.File;
import java.util.*;


public class Pack extends LineaProductoVenta{
	private Map<LineaProductoVenta, Integer> pack = new HashMap<>();

  public Pack(String nombre, String descripcion, File foto, int stock, double precio){
    super(nombre, descripcion, foto, stock, precio);
  }

  public void añadirProductoAPack(LineaProductoVenta l, Integer i){
    if(l == null || i < 0){
      throw new IllegalArgumentException("Los argumentos introducidos no son validos");
    }
    else if(i * this.stock > l.getStock()){
      throw new IllegalArgumentException("El stock necesario para el paquete supera el stock actual del producto");
    }
	  this.pack.merge(l, i, Integer::sum);
  }

  public void eliminarProductoDePack(LineaProductoVenta l, Integer i) {
	    if (l == null || i < 0) {
	        throw new IllegalArgumentException("Los argumentos introducidos no son validos");
	    }
	    if (!pack.containsKey(l)) {
	        throw new IllegalArgumentException("El producto no existe en el pack");
	    }
	    if (pack.get(l) > i) {
	        pack.put(l, pack.get(l) - i);
	    } else {
	        pack.remove(l);
	    }
	}
  
  public void añadirProductosAPack(Map<LineaProductoVenta, Integer> productos) {
	    if (productos == null) {
	        throw new IllegalArgumentException("La lista de productos no puede ser nula");
	    }
	    
	    for (Map.Entry<LineaProductoVenta, Integer> entry : productos.entrySet()) {
	    	this.añadirProductoAPack(entry.getKey(), entry.getValue());
	    }
	}

	public Map<LineaProductoVenta, Integer> getProductosPack() {
		if(pack.isEmpty()) {
	        throw new IllegalStateException("Este pack no contiene productos");
		}
	    return pack;
	}
	

	@Override
	public String toString() {
	    // 1. Usamos StringBuilder para construir la cadena de forma eficiente
	    StringBuilder sb = new StringBuilder();
	    
	    // 2. Información básica del Pack (heredada de LineaProductoVenta y Producto)
	    sb.append("=== DETALLES DEL PACK ===\n");
	    sb.append(super.toString()); // Aprovechamos el toString() de la clase padre
	    sb.append("\nCONTENIDO DEL PACK:");
	    
	    // 3. Verificamos si el pack tiene productos antes de iterar
	    if (pack.isEmpty()) {
	        sb.append("\n  (Este pack está vacío)");
	    } else {
	        // 4. Recorremos el mapa para mostrar nombre y cantidad
	        for (Map.Entry<LineaProductoVenta, Integer> entry : pack.entrySet()) {
	            sb.append("\n  - ")
	              .append(entry.getKey().getNombre()) // Solo el nombre del producto
	              .append(" (x")
	              .append(entry.getValue())
	              .append(")");
	        }
	    }
	    
	    return sb.toString();
	}


}
