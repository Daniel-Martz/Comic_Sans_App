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
    Exception e = assertThrowsExactly(IllegalArgumentException.class, ()->{solValidacion.validarProducto(-1, EstadoConservacion.MUY_BUENO);}); 
    assertEquals(e.getMessage(), "El precio no puede tener un valor negativo");
	}

	@Test
	void testValidarProducto() {
    solValidacion.validarProducto(10, EstadoConservacion.MUY_BUENO);
    assertTrue(solValidacion.validado());
	}
}
