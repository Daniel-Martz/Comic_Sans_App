package solicitud.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import solicitud.Oferta;
import tiempo.TiempoSimulado;
import tiempo.DateTimeSimulado;
import usuario.*;
import producto.*;

class OfertaTest {
	Oferta o;

  @BeforeEach
	void setUp() {
		DateTimeSimulado inicio = new DateTimeSimulado();
		ClienteRegistrado cliente1 = new ClienteRegistrado("Rigoberto", "01122233A", "123456");
		ClienteRegistrado cliente2 = new ClienteRegistrado("Bonifacio", "01122233X", "123456");
		Set<ProductoSegundaMano> prodsOfertados = new HashSet<>();
		Set<ProductoSegundaMano> prodsSolicitados = new HashSet<>();
		o = new Oferta(new DateTimeSimulado(), cliente1, cliente2, prodsOfertados, prodsSolicitados);
	}

	@Test
	void testHaCaducado1() {
		assertFalse(o.haCaducado());
	}


	@Test
	void testHaCaducado2() {
		TiempoSimulado.getInstance().avanzarDias(10);
		assertTrue(o.haCaducado());
	}

}
