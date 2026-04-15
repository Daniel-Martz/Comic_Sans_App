package aplicacion;

import solicitud.*;
import tiempo.*;
import java.io.*;

/**
 * Sistema encargado de simular el procesamiento de pagos en la aplicación.
 * Implementa el patrón Singleton para garantizar una única instancia y 
 * soporta serialización para persistir su estado.
 * * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class SistemaPago implements Serializable{

	/** Identificador único para la serialización de la clase. */
	private static final long serialVersionUID = 1L;
	
	/** Instancia única de la clase (Singleton). */
	static SistemaPago instancia;

	/**
	 * Constructor privado para evitar la instanciación externa.
	 */
	private SistemaPago() {
	}

	/**
	 * Obtiene la instancia única de SistemaPago.
	 *
	 * @return La instancia global del sistema de pago.
	 */
	public static SistemaPago getInstancia() {
		if (instancia == null) {
			instancia = new SistemaPago();
		}
		return instancia;
	}

	/**
	 * Procesa un pago simulado validando previamente el importe y el formato de la tarjeta.
	 *
	 * @param importe Cantidad a cobrar.
	 * @param numTarjeta Número de la tarjeta de crédito (debe tener 16 caracteres).
	 * @param cvv Código de seguridad de la tarjeta (debe tener 3 caracteres).
	 * @param fechaCaducidad Fecha de caducidad de la tarjeta.
	 * @param objetoPagado Solicitud a la que se asocia el pago.
	 * @return Un nuevo objeto Pago que representa la transacción completada.
	 * @throws IllegalArgumentException Si el importe, tarjeta, CVV o fecha son inválidos.
	 */
	public Pago procesarPago(double importe, String numTarjeta, String cvv, DateTimeSimulado fechaCaducidad, Solicitud objetoPagado) {
		
		if (importe <= 0) {
			throw new IllegalArgumentException("El importe debe ser mayor que 0.");
		}

		// Comprobamos si es null O si el tamaño no es 16
		if (numTarjeta == null || numTarjeta.length() != 16) {
			throw new IllegalArgumentException("El número de tarjeta debe tener exactamente 16 caracteres.");
		}

		//Comprobamos si es null O si el tamaño no es 3
		if (cvv == null || cvv.length() != 3) {
			throw new IllegalArgumentException("El CVV debe tener exactamente 3 caracteres.");
		}
		
		if (fechaCaducidad == null) {
			throw new IllegalArgumentException("La fecha de caducidad no puede ser nula.");
		}


		int mesCaducidad = fechaCaducidad.getMes();

		// Comprobamos meses menores a 1 y mayores a 12
		if (mesCaducidad < 1 || mesCaducidad > 12) {
			throw new IllegalArgumentException("El mes de caducidad no es válido.");
		}
		
		System.out.println("¡Pago de " + importe + "€ procesado con éxito!");
		
		Pago p = new Pago(new DateTimeSimulado(), importe, objetoPagado);
		return p;
	}

	/**
	 * Personaliza el proceso de serialización.
	 * @param oos Flujo de salida de objetos.
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException {
	    oos.defaultWriteObject();
	}

	/**
	 * Personaliza el proceso de deserialización para restaurar el Singleton correctamente.
	 * @param ois Flujo de entrada de objetos.
	 * @throws IOException Si ocurre un error de lectura.
	 * @throws ClassNotFoundException Si no se encuentra la clase.
	 */
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
	    ois.defaultReadObject();
	    instancia = this;
	}
}
