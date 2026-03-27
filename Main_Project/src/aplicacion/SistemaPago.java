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

	//faltan excepciones
	public Pago procesarPago(double importe, String numTarjeta, String cvv, DateTimeSimulado fechaCaducidad) {
		if (importe <= 0) {
			return null;
		}

		if (numTarjeta == null || numTarjeta.length() != 16) {
			return null;
		}

		if (cvv == null || cvv.length() != 3) {
			return null;
		}
		
		if (fechaCaducidad == null) {
			return null;
		}

		DateTimeSimulado hoy = new DateTimeSimulado();
		int mesActual = hoy.getMes();
		int anioActual = hoy.getAño();
		
		int mesCaducidad = fechaCaducidad.getMes();
		int anioCaducidad = fechaCaducidad.getAño();

		if (mesCaducidad < 1 || mesCaducidad > 12) {
			return null;
		}

		if (anioCaducidad < anioActual || (anioCaducidad == anioActual && mesCaducidad < mesActual)) {
			return null;
		}
		
		System.out.println("¡Pago de " + importe + "€ procesado con éxito!");
		
		return null;
	}
}