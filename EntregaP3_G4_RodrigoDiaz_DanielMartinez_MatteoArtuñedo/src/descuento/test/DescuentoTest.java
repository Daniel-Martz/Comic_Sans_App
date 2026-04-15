package descuento.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import descuento.Cantidad;
import descuento.Descuento;
import categoria.Categoria;
import producto.LineaProductoVenta;
import tiempo.DateTimeSimulado;
import tiempo.TiempoSimulado;

import java.io.File;

class DescuentoTest {

	private Descuento descuento;
	private DateTimeSimulado fechaInicio;
	private DateTimeSimulado fechaFin;

	@BeforeEach
	void setUp() {
		fechaInicio = new DateTimeSimulado();
		fechaFin = DateTimeSimulado.DateTimeDiasDespues(30);
		descuento = new Cantidad(fechaInicio, fechaFin, 3, 2);
	}

	@Test
	void testAñadirCategoriaValida() {
		Categoria c = new Categoria("Terror");
		descuento.añadirCategoria(c);
		assertTrue(descuento.getCategoriasRebajadas().contains(c));
	}

	@Test
	void testAñadirVariasCategorias() {
		Categoria c1 = new Categoria("Terror");
		Categoria c2 = new Categoria("Romance");

		descuento.añadirCategoria(c1);
		descuento.añadirCategoria(c2);

		assertTrue(descuento.getCategoriasRebajadas().contains(c1));
		assertTrue(descuento.getCategoriasRebajadas().contains(c2));
		assertEquals(2, descuento.getCategoriasRebajadas().size());
	}

	@Test
	void testAñadirCategoriaDuplicadaNoSeRepite() {
		Categoria c = new Categoria("Terror");
		descuento.añadirCategoria(c);
		descuento.añadirCategoria(c);
		assertEquals(1, descuento.getCategoriasRebajadas().size());
	}

	@Test
	void testEliminarCategoriaExistente() {
		Categoria c = new Categoria("Terror");
		descuento.añadirCategoria(c);
		descuento.eliminarCategoria(c);
		assertFalse(descuento.getCategoriasRebajadas().contains(c));
	}

	@Test
	void testEliminarCategoriaNoExistente() {
		Categoria c = new Categoria("Terror");
		assertDoesNotThrow(() -> descuento.eliminarCategoria(c));
	}

	@Test
	void testAñadirProductoRebajadoValido() {
		LineaProductoVenta prod = new LineaProductoVenta("Marvel", "Comic", new File("f.png"), 10, 9.99);
		descuento.añadirProductoRebajado(prod);
		assertTrue(descuento.getProductosRebajados().contains(prod));
	}

	@Test
	void testAñadirVariosProductosRebajados() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("f.png"), 10, 9.99);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic", new File("f.png"), 5, 7.99);

		descuento.añadirProductoRebajado(p1);
		descuento.añadirProductoRebajado(p2);

		assertEquals(2, descuento.getProductosRebajados().size());
		assertTrue(descuento.getProductosRebajados().contains(p1));
		assertTrue(descuento.getProductosRebajados().contains(p2));
	}

	@Test
	void testAñadirProductoRebajadoDuplicadoNoSeRepite() {
		LineaProductoVenta prod = new LineaProductoVenta("Marvel", "Comic", new File("f.png"), 10, 9.99);

		descuento.añadirProductoRebajado(prod);
		descuento.añadirProductoRebajado(prod);

		assertEquals(1, descuento.getProductosRebajados().size());
	}

	@Test
	void testEliminarProductoRebajadoExistente() {
		LineaProductoVenta prod = new LineaProductoVenta("Marvel", "Comic", new File("f.png"), 10, 9.99);
		descuento.añadirProductoRebajado(prod);
		descuento.eliminarProductoRebajado(prod);
		assertFalse(descuento.getProductosRebajados().contains(prod));
	}

	@Test
	void testEliminarProductoRebajadoNoExistente() {
		LineaProductoVenta prod = new LineaProductoVenta("Marvel", "Comic", new File("f.png"), 10, 9.99);
		assertDoesNotThrow(() -> descuento.eliminarProductoRebajado(prod));
	}
	
	@Test 
	public void testHaCaducado1() {
		TiempoSimulado.getInstance().avanzarDias(35);
		assertTrue(descuento.haCaducado());
	}
	
	@Test 
	public void testHaCaducado2() {
		TiempoSimulado.getInstance().avanzarDias(5);
		assertFalse(descuento.haCaducado());
	}
}
