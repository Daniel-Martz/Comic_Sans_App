package descuento.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import descuento.Regalo;
import producto.LineaProductoVenta;
import tiempo.DateTimeSimulado;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

class RegaloTest {

	private Regalo regalo;
	private LineaProductoVenta productoRegalo;

	@BeforeEach
	void setUp() {
		DateTimeSimulado inicio = new DateTimeSimulado();
		DateTimeSimulado fin = DateTimeSimulado.DateTimeDiasDespues(30);
		productoRegalo = new LineaProductoVenta("Figura Dragon", "Figura de coleccion", new File("f.png"), 20, 15.0);

		Map<LineaProductoVenta, Integer> productosIniciales = new HashMap<>();
		regalo = new Regalo(inicio, fin, 50.0, productosIniciales);
	}

	@Test
	void testAñadirProductoRegaloNuevo() {
		regalo.añadirProductoRegalo(productoRegalo, 2);
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 2));
	}

	@Test
	void testAñadirProductoRegaloSumaUnidadesSiYaExiste() {
		regalo.añadirProductoRegalo(productoRegalo, 3);
		regalo.añadirProductoRegalo(productoRegalo, 2);
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 4));
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 1));
	}

	@Test
	void testAñadirVariosProductosRegalo() {
		LineaProductoVenta p2 = new LineaProductoVenta("Poster", "Poster exclusivo", new File("p.png"), 10, 5.0);
		regalo.añadirProductoRegalo(productoRegalo, 1);
		regalo.añadirProductoRegalo(p2, 1);
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 1));
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(p2, 1));
	}

	@Test
	void testEliminarProductoRegaloTotalCuandoUnidadesIguales() {
		regalo.añadirProductoRegalo(productoRegalo, 3);
		regalo.eliminarProductoRegalo(productoRegalo, 3);
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 1));
	}

	@Test
	void testEliminarProductoRegaloTotalCuandoUnidadesMayores() {
		regalo.añadirProductoRegalo(productoRegalo, 2);
		regalo.eliminarProductoRegalo(productoRegalo, 10);
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 1));
	}

	@Test
	void testEliminarProductoRegaloParcial() {
		regalo.añadirProductoRegalo(productoRegalo, 5);
		regalo.eliminarProductoRegalo(productoRegalo, 3);
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 2));
	}

	@Test
	void testEliminarProductoRegaloNoExistente() {
		assertDoesNotThrow(() -> regalo.eliminarProductoRegalo(productoRegalo, 1));
	}
}