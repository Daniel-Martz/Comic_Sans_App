package aplicacion;

import solicitud.*;
import java.time.LocalDate;

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
	public Pago procesarPago(double importe, String numTarjeta, String cvv, int mesCaducidad, int anioCaducidad) {

		if (importe <= 0) {
			return null;
		}
		if (numTarjeta == null || numTarjeta.length() != 16) {

		}

		if (cvv == null || cvv.length() != 3) {

		}

		LocalDate hoy = LocalDate.now();
		int mesActual = hoy.getMonthValue();
		int anioActual = hoy.getYear();

		if (mesCaducidad < 1 || mesCaducidad > 12) {
		}

		if (anioCaducidad < anioActual || anioCaducidad == anioActual && mesCaducidad < mesActual) {

		}
		System.out.println("¡Pago de " + importe + "€ procesado con éxito!");
		return null;
		
	}
}