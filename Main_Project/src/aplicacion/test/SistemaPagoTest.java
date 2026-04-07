package aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import solicitud.*;
import tiempo.*;
import aplicacion.*;

class SistemaPagoTest {

	@Test
	void testProcesarPagoCorrecto() {
		SistemaPago pagos = SistemaPago.getInstancia();
		DateTimeSimulado fechaFutura = DateTimeSimulado.DateTimeDiasDespues(365);
		
		Pago resultado = pagos.procesarPago(50.0, "1234567890123456", "123", fechaFutura, null);
		assertNotNull(resultado, "El pago debería procesarse correctamente.");
	}

	@Test
	void testPagoImporteInvalido() {
		SistemaPago pagos = SistemaPago.getInstancia();
		DateTimeSimulado fechaFutura = DateTimeSimulado.DateTimeDiasDespues(365);

		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(0, "1234567890123456", "123", fechaFutura, null); });
		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(-10.5, "1234567890123456", "123", fechaFutura, null); });
	}

	@Test
	void testPagoTarjetaInvalida() {
		SistemaPago pagos = SistemaPago.getInstancia();
		DateTimeSimulado fechaFutura = DateTimeSimulado.DateTimeDiasDespues(365);

		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, null, "123", fechaFutura, null); });
		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, "12345", "123", fechaFutura, null); });
	}

	@Test
	void testPagoCvvInvalido() {
		SistemaPago pagos = SistemaPago.getInstancia();
		DateTimeSimulado fechaFutura = DateTimeSimulado.DateTimeDiasDespues(365);

		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, "1234567890123456", null, fechaFutura, null); });
		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, "1234567890123456", "1234", fechaFutura, null); });
	}

	@Test
	void testPagoFechaNula() {
		SistemaPago pagos = SistemaPago.getInstancia();
		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, "1234567890123456", "123", null, null); });
	}

	@Test
	void testPagoMesInvalido() {
		SistemaPago pagos = SistemaPago.getInstancia();
		
		DateTimeSimulado fechaMesCero = new DateTimeSimulado(2050, 0, 1, 12, 0, 0);
		DateTimeSimulado fechaMesTrece = new DateTimeSimulado(2050, 13, 1, 12, 0, 0);

		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, "1234567890123456", "123", fechaMesCero, null); });
		assertThrows(IllegalArgumentException.class, () -> { pagos.procesarPago(50.0, "1234567890123456", "123", fechaMesTrece, null); });
	}
}