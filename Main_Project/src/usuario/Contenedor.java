package usuario;

public interface Contenedor<T> {
	public void añadirProducto(T obj);
	public void eliminarProducto(T obj);
}
