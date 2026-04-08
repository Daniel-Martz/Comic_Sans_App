package producto;

import java.io.File;
import java.util.*;

/**
 * Implementa la clase Pack.
 * Representa un conjunto de productos que se venden juntos como una única línea de producto.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class Pack extends LineaProductoVenta {
	
	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;
	
	/** Mapa que asocia cada producto incluido en el pack con su cantidad correspondiente. */
	private Map<LineaProductoVenta, Integer> pack = new HashMap<>();
	/**
	 * Instancia un nuevo pack.
	 *
	 * @param nombre el nombre del pack
	 * @param descripcion la descripción
	 * @param foto el archivo de imagen
	 * @param stock el número de packs disponibles
	 * @param precio el precio del pack completo
	 */
	public Pack(String nombre, String descripcion, File foto, int stock, double precio){
		super(nombre, descripcion, foto, stock, precio);
	}

	/**
	 * Añade una cantidad de un producto al pack.
	 * Verifica que haya stock suficiente del producto individual para cubrir
	 * la cantidad requerida por todos los packs en stock.
	 *
	 * @param l el producto a añadir
	 * @param i la cantidad del producto
	 * @throws IllegalArgumentException si los argumentos son inválidos o no hay stock
	 */
	public void añadirProductoAPack(LineaProductoVenta l, Integer i){
		if(l == null || i < 0){
			throw new IllegalArgumentException("Los argumentos introducidos no son validos");
		}
		
		int cantidadTotalDeseada = i + this.pack.getOrDefault(l, 0);
		
		if(cantidadTotalDeseada * this.stock > l.getStock()){
			throw new IllegalArgumentException("El stock necesario para el paquete supera el stock actual del producto");
		}
		
		this.pack.merge(l, i, Integer::sum);
	}

	/**
	 * Elimina una cantidad específica de un producto del pack.
	 *
	 * @param l el producto a eliminar
	 * @param i la cantidad a retirar
	 * @throws IllegalArgumentException si los argumentos no son válidos o el producto no existe
	 */
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
	
	/**
	 * Añade múltiples productos al pack desde un mapa.
	 *
	 * @param productos el mapa con los productos y sus cantidades
	 * @throws IllegalArgumentException si la lista de productos es nula
	 */
	public void añadirProductosAPack(Map<LineaProductoVenta, Integer> productos) {
		if (productos == null) {
			throw new IllegalArgumentException("La lista de productos no puede ser nula");
		}
		
		for (Map.Entry<LineaProductoVenta, Integer> entry : productos.entrySet()) {
			this.añadirProductoAPack(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Obtiene los productos que componen el pack.
	 * Se devuelve un mapa inmodificable para proteger la estructura interna.
	 *
	 * @return el mapa de productos y sus cantidades
	 */
	public Map<LineaProductoVenta, Integer> getProductosPack() {
		return Collections.unmodifiableMap(pack);
	}
	

	/**
	 * Devuelve una representación en formato texto del pack y su contenido.
	 *
	 * @return la cadena de texto con la información
	 */
	@Override
	public String toString() {
		/* 1. Usamos StringBuilder para construir la cadena de forma eficiente */
		StringBuilder sb = new StringBuilder();
		
		/* 2. Información básica del Pack (heredada de LineaProductoVenta y Producto) */
		sb.append("=== DETALLES DEL PACK ===\n");
		sb.append(super.toString()); /* Aprovechamos el toString() de la clase padre */
		sb.append("\nCONTENIDO DEL PACK:");
		
		/* 3. Verificamos si el pack tiene productos antes de iterar */
		if (pack.isEmpty()) {
			sb.append("\n  (Este pack está vacío)");
		} else {
			/* 4. Recorremos el mapa para mostrar nombre y cantidad */
			for (Map.Entry<LineaProductoVenta, Integer> entry : pack.entrySet()) {
				sb.append("\n  - ")
				  .append(entry.getKey().getNombre()) /* Solo el nombre del producto */
				  .append(" (x")
				  .append(entry.getValue())
				  .append(")");
			}
		}
		
		return sb.toString();
	}

}
