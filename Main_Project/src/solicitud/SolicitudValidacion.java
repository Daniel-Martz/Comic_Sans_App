
package solicitud;

import producto.EstadoConservacion;
import producto.ProductoSegundaMano;
import usuario.ClienteRegistrado;

/**
 * @author Dani
 * @version 1.0
 * @date 13/03
 */
public class SolicitudValidacion extends Solicitud {

  /** Pago asociado a la validacion. */
  private Pago pagoValidacion;

  /** Producto asociado a la solicitud */
  private ProductoSegundaMano productoAValidar;
  
  private ClienteRegistrado cliente;

  /**
   * Instancia un objeto del tipo SolicitudValidacion, con el producto asociado.
   *
   * @param productoAValidar el producto a validar
   */
  public SolicitudValidacion(ProductoSegundaMano productoAValidar) {
    super();
    this.productoAValidar = productoAValidar;
  }

  /**
   * Metodo que hace referencia a la accion de validar el producto de una
   * solicitud,
   * que llamara al metodo que validara al producto.
   *
   * @param precio el precio
   * @param estado el estado
   */
  public void validarProducto(double precio, EstadoConservacion estado) {
    if (precio < 0) {
      throw new IllegalArgumentException("El precio no puede tener un valor negativo");
    }
    productoAValidar.validarProducto(precio, estado);
  }


  public boolean validado(){
    if (this.productoAValidar.getDatosValidacion() != null){
      return true;
    }
    return false;
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

  @Override
  public String toString() {
    return "SolicitudValidacion [pagoValidacion=" + pagoValidacion + ", productoAValidar=" + productoAValidar + "]";
  }

  public ProductoSegundaMano getProductoAValidar() {
    return productoAValidar;
  }

  public Pago getPagoValidacion() {
    return pagoValidacion;
  }

  public ClienteRegistrado getCliente() {
	return cliente;
  }
  
}
