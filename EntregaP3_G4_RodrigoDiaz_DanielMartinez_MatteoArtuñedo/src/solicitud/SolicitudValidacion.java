package solicitud;

import producto.EstadoConservacion;
import producto.ProductoSegundaMano;
import usuario.ClienteRegistrado;

/**
 * Representa una solicitud para que un empleado valide un producto de segunda mano.
 * Contiene la información del producto, el cliente solicitante y el pago asociado
 * al proceso de validación.
 * @author Dani, Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class SolicitudValidacion extends Solicitud {

  private static final long serialVersionUID = 1L;

  /** Pago asociado a la validacion. */
  private Pago pagoValidacion;
  
  /** Coste del servicio de validación. */
  private double precioValidacion;

  /** Producto que debe ser revisado por el empleado. */
  private ProductoSegundaMano productoAValidar;
  
  /** Cliente que solicita la validación. */
  private ClienteRegistrado cliente;

  /**
   * Instancia un objeto del tipo SolicitudValidacion con el producto y cliente asociados.
   *
   * @param productoAValidar el producto que se pretende validar.
   * @param cliente el usuario dueño del producto.
   */
  public SolicitudValidacion(ProductoSegundaMano productoAValidar, ClienteRegistrado cliente) {
    super();
    this.productoAValidar = productoAValidar;
    this.cliente = cliente;
  }

  /**
   * Procesa la validación técnica del producto asociado a esta solicitud.
   * Actualiza el precio del servicio y delega la validación de estado y tasación al producto.
   *
   * @param precioValidacion el coste del servicio de validación.
   * @param precioProducto el precio de mercado asignado al producto tras la revisión.
   * @param estadoProducto el estado de conservación detectado por el empleado.
   * @throws IllegalArgumentException si los precios son negativos o el estado es nulo.
   */
  public void validarProducto(double precioValidacion, double precioProducto, EstadoConservacion estadoProducto) {
    if (precioProducto < 0 || precioValidacion < 0 || estadoProducto == null) {
      throw new IllegalArgumentException("Los argumentos introducidos no son válidos");
    }
    this.precioValidacion = precioValidacion;
    productoAValidar.validarProducto(precioProducto, estadoProducto);
  }

  /**
   * Comprueba si el proceso de validación ha finalizado correctamente.
   * @return true si el producto ya cuenta con datos de validación, false en caso contrario.
   */
  public boolean validado(){
    if (this.productoAValidar.getDatosValidacion() != null){
      return true;
    }
    return false;
  }

  /**
   * Asocia el comprobante de pago a la solicitud de validación.
   *
   * @param pagoValidacion el pago realizado por el cliente para procesar la validación.
   */
  public void añadirPagoValidacion(Pago pagoValidacion) {
    this.pagoValidacion = pagoValidacion;
  }

  /**
   * Devuelve una representación textual de la solicitud.
   * @return cadena con la información del pago y el producto.
   */
  @Override
  public String toString() {
      return "SolicitudValidacion [\n" +
             "  producto=" + productoAValidar.getNombre() + "\n" +
             "  validado=" + (productoAValidar.getDatosValidacion() != null) + "\n" +
             "  pago=" + (pagoValidacion != null ? pagoValidacion.toString() : "sin pago") + "\n" +
             "]";
  }
  /**
   * Obtiene el producto vinculado a esta solicitud.
   * @return el objeto ProductoSegundaMano.
   */
  public ProductoSegundaMano getProductoAValidar() {
    return productoAValidar;
  }

  /**
   * Obtiene el registro de pago de la validación.
   * @return el objeto Pago o null si aún no se ha pagado.
   */
  public Pago getPagoValidacion() {
    return pagoValidacion;
  }

  /**
   * Obtiene el cliente propietario de la solicitud.
   * @return el cliente registrado.
   */
  public ClienteRegistrado getCliente() {
	return cliente;
  }
  
  /**
   * Obtiene el precio estipulado por realizar la validación.
   * @return coste de la validación.
   */
  public double getPrecioValidacion(){
    return this.precioValidacion;
  }

}
