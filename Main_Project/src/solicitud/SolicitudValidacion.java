
package solicitud;

import producto.EstadoConservacion;
import producto.ProductoSegundaMano;

/**
 * @author Dani
 * @version 1.0
 * @date 13/03
 */
public class SolicitudValidacion extends Solicitud{
	
	/** Pago asociado a la validacion. */
	private Pago pagoValidacion;
	
	/** Producto asociado a la solicitud */
	private ProductoSegundaMano productoAValidar;
	
	/**
	 * Instancia un objeto del tipo SolicitudValidacion, con el producto asociado.
	 *
	 * @param productoAValidar el producto a validar
	 */
	public SolicitudValidacion(ProductoSegundaMano productoAValidar){
		super();
		this.productoAValidar = productoAValidar;
	}
	
	/**
	 * Metodo que hace referencia a la accion de validar el producto de una solicitud, 
	 * que llamara al metodo que validara al producto.
	 *
	 * @param precio el precio
	 * @param estado el estado
	 */
	public boolean validarProducto(int precio, EstadoConservacion estado) {
		if (productoAValidar.validarProducto(precio, estado) == true) {
			return true;
		}
		else return false;
	}
	
	
	/**
	 * Metodo que asociara a la solicitud de validacion su pago, 
	 * una vez el cliente haya pagado por la validacion de su producto.
	 *
	 * @param pagoValidacion el pago correspodiente a la validacion del producto
	 */
	public void añadirPagoValidacion(Pago pagoValidacion) {
		this.pagoValidacion = pagoValidacion;
	}
}
