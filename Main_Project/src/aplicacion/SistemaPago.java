package aplicacion;

import solicitud.*;
import tiempo.*;

public class SistemaPago {

	private static SistemaPago instancia;

	private SistemaPago() {
	}

	public static SistemaPago getInstancia() {
		if (instancia == null) {
			instancia = new SistemaPago();
		}
		return instancia;
	}

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

		DateTimeSimulado hoy = new DateTimeSimulado();
		int mesActual = hoy.getMes();
		int anioActual = hoy.getAño(); 
		
		int mesCaducidad = fechaCaducidad.getMes();
		int anioCaducidad = fechaCaducidad.getAño();

		// Comprobamos meses menores a 1 y mayores a 12
		if (mesCaducidad < 1 || mesCaducidad > 12) {
			throw new IllegalArgumentException("El mes de caducidad no es válido.");
		}
		
		System.out.println("¡Pago de " + importe + "€ procesado con éxito!");
		
		Pago p = new Pago(new DateTimeSimulado(), importe, objetoPagado);
		return p;
	}
}