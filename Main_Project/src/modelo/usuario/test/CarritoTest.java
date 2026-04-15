package modelo.usuario.test;
import modelo.usuario.*;
import modelo.producto.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Implementa la clase de pruebas CarritoTest.
 * Verifica el correcto funcionamiento de las operaciones del carrito.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
class CarritoTest {
	Carrito c = new Carrito();

	@Test
	void testAñadirProductoValido() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		
		c.añadirProducto(p1, 2);
		c.añadirProducto(p1, 1);

		assertTrue(c.getProductos().get(p1) == 3);
	}

	@Test
	void testAñadirProductoInvalidoPorArgs1() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		Exception e = assertThrows(IllegalArgumentException.class,()-> {c.añadirProducto(p1, -1);});
		assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

	@Test
	void testAñadirProductoInvalidoPorArgs2() {
		LineaProductoVenta p1 = null;
		Exception e = assertThrows(IllegalArgumentException.class,()-> {c.añadirProducto(p1, 2);});
		assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

	@Test
	void testGetProductos() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
		LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12, 5.99);
		c.añadirProducto(p1, 1);
		c.añadirProducto(p2, 1);
		c.añadirProducto(p3, 1);
		Map<LineaProductoVenta, Integer> listaProds = new HashMap<>();
		listaProds.put(p1, 1);
		listaProds.put(p2, 1);
		listaProds.put(p3, 1);
		Map <LineaProductoVenta, Integer> listaProdsDevuelta = this.c.getProductos();
		listaProdsDevuelta.put(p1, 1);
		listaProdsDevuelta.put(p2, 1);
		listaProdsDevuelta.put(p3, 1);
		assertEquals(listaProds.entrySet(), listaProdsDevuelta.entrySet());
	}

	@Test
	void testEliminarProducto(){
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 100, 5.99);
		this.c.añadirProducto(p1, 10);
		this.c.eliminarProducto(p1, 1);
		assertEquals(this.c.getProductos().get(p1), (Integer)9);
		this.c.eliminarProducto(p1, 9);
		assertTrue(c.getProductos().isEmpty());
	}

	@Test
	void testGetPrecioProductos(){
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
		LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12, 5.99);
		c.añadirProducto(p1, 1);
		c.añadirProducto(p2, 1);
		c.añadirProducto(p3, 1);
		assertTrue(c.getPrecioProductos() == 17.97);
	}

	@Test
	void testVaciarCarrito(){
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
		LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12, 5.99);
		c.añadirProducto(p1, 1);
		c.añadirProducto(p2, 1);
		c.añadirProducto(p3, 1);
		c.vaciarCarrito();
		assertTrue(c.getProductos().isEmpty());
	}
	
	@Test
	void testAñadirProductoExcedeStock() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 5, 5.99);
		c.añadirProducto(p1, 3);
		
		Exception e = assertThrows(IllegalStateException.class, ()-> {c.añadirProducto(p1, 3);});
		assertEquals(e.getMessage(), "No hay suficiente stock de ese producto");
	}

	@Test
	void testEliminarProductoNulo() {
		Exception e = assertThrows(IllegalArgumentException.class, ()-> {c.eliminarProducto(null, 1);});
		assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

	@Test
	void testEliminarProductoCantidadNegativa() { 
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		
		Exception e = assertThrows(IllegalArgumentException.class, ()-> {c.eliminarProducto(p1, -5);});
		assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}
}
