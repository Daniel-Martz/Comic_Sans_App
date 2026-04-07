package solicitud;

/**
 * Interfaz que define el comportamiento de los objetos que tienen un periodo de validez.
 * Cualquier clase que implemente esta interfaz debe especificar como y cuando caduca.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public interface Caducable{
  
  /**
   * Comprueba si el objeto ha superado su periodo de validez o ha caducado.
   * * @return true si el objeto ha caducado, false en caso contrario
   */
  public boolean haCaducado();
}