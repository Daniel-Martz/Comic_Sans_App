package usuario;

public interface Contenedor<T> {
	public void añadirProducto(T obj, Integer cantidad);
	public void eliminarProducto(T obj, Integer cantidad);
}
