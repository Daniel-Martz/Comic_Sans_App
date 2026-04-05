package solicitud.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import producto.ProductoSegundaMano;
import producto.EstadoConservacion;
import solicitud.SolicitudValidacion;
import java.io.File;

class SolicitudValidacionTest {
  SolicitudValidacion solValidacion = new SolicitudValidacion(new ProductoSegundaMano("Pelota", "Una pelota muy chula", new File("pelota.jpg")));

	@Test
	void testValidarProductoError() {
    Exception e = assertThrowsExactly(IllegalArgumentException.class, ()->{solValidacion.validarProducto(5, -1.0, EstadoConservacion.MUY_BUENO);}); 
    assertEquals(e.getMessage(), "Los argumentos introducidos no son válidos");
	}

	@Test
	void testValidarProducto() {
    solValidacion.validarProducto(5, 10.0, EstadoConservacion.MUY_BUENO);
    assertTrue(solValidacion.validado());
	}
}
