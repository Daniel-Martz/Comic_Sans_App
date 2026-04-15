/**
 * 
 */
package modelo.producto.test;

import modelo.producto.LineaProductoVenta;
import modelo.producto.Pack;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
class PackTest {

	Pack p = new Pack("Comics de accion", "Llevate varios comics rebajados", new File("/fotocomic.png"), 5, 19.99);

	/**
	 * Test method for
	 * {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 */
	@Test
	void testAñadirProductoValidoAPack() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
		LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12,
				5.99);

		p.añadirProductoAPack(p1, 2);
		p.añadirProductoAPack(p2, 1);
		p.añadirProductoAPack(p3, 1);

		assertTrue(p.getProductosPack().containsKey(p1) && p.getProductosPack().containsKey(p2)
				&& p.getProductosPack().containsKey(p3));
	}

	/**
	 * Test method for
	 * {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 */
	@Test
	void testAñadirProductoValidoYaExistenteAPack() {
	    LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto1.png"), 100, 5.99);
	    p.añadirProductoAPack(p1, 2);
	    p.añadirProductoAPack(p1, 1);
	    Map<LineaProductoVenta, Integer> productos = p.getProductosPack();
	    assertNotNull(productos.get(p1), "El producto p1 debería existir en el pack");
	    assertEquals(3, productos.get(p1).intValue());
	}
	/**
	 * Test method for
	 * {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 * 
	 */
	@Test
	void testAñadirProductoInvalidoPorStockAPack() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		// Este pack tiene stock 5 y estamos indicando que cada pack incluirá 5
		// unidades,
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			p.añadirProductoAPack(p1, 5);
		});
		assertEquals(e.getMessage(), "El stock necesario para el paquete supera el stock actual del producto");
	}

	/**
	 * Test method for
	 * {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 *
	 */
	@Test
	void testAñadirProductoInvalidoPorArgs1() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			p.añadirProductoAPack(p1, -1);
		});
		assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

	/**
	 * Test method for
	 * {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 *
	 */
	@Test
	void testAñadirProductoInvalidoPorArgs2() {
		LineaProductoVenta p1 = null;
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			p.añadirProductoAPack(p1, 2);
		});
		assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

	/**
	 * Test method for {@link producto.Pack#añadirProductosAPack(java.util.Map)}.
	 */
	@Test
	void testAñadirProductosAPack() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
	  LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
	  LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12, 5.99);
    p.añadirProductoAPack(p1, 1);
    p.añadirProductoAPack(p2, 1);
    p.añadirProductoAPack(p3, 1);
    Map<LineaProductoVenta, Integer> listaProds = new HashMap<>();
    listaProds.put(p1, 1);
    listaProds.put(p2, 1);
    listaProds.put(p3, 1);
    Map<LineaProductoVenta, Integer> listaProdsDevuelta = p.getProductosPack();
    //Comprobamos que ambos mapas tienen el mismo tamaño y que los elementos de una están en otra (igualdad conjuntista)
    assertEquals(listaProdsDevuelta.size(), listaProds.size());
    for(LineaProductoVenta l : this.p.getProductosPack().keySet()){
      assertTrue(listaProds.containsKey(l));
    }
   
	}

	/**
	 * Test method for {@link producto.Pack#añadirProductosAPack(java.util.Map)}.
	 */
	@Test
	void testAñadirProductosAPackConListaNula() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			p.añadirProductosAPack(null);
		});
		assertEquals(e.getMessage(), "La lista de productos no puede ser nula");
	}

	/**
	 * Test method for {@link producto.Pack#getProductosPack()}.
	 */
	@Test
	void testGetProductosPack() {
	    LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto1.png"), 10, 5.99);
	    LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic", new File("Foto2.png"), 10, 5.99);
	    LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic", new File("Foto3.png"), 10, 5.99);
	    
	    p.añadirProductoAPack(p1, 1);
	    p.añadirProductoAPack(p2, 1);
	    p.añadirProductoAPack(p3, 1);
	    
	    Map<LineaProductoVenta, Integer> esperado = new HashMap<>();
	    esperado.put(p1, 1);
	    esperado.put(p2, 1);
	    esperado.put(p3, 1);
	    
	    Map<LineaProductoVenta, Integer> obtenido = p.getProductosPack();
	    
	    assertEquals(esperado, obtenido);
	    assertEquals(3, obtenido.size());
	}

	/**
	 * Test method for
	 * {@link producto.Pack#eliminarProductoDePack(LineaProductoVenta, Integer)}
	 */
	@Test
	void testEliminarProductoDePack() {
	    LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto1.png"), 100, 5.99);
	    this.p.añadirProductoAPack(p1, 10);
	    this.p.eliminarProductoDePack(p1, 1);
	    assertEquals(9, this.p.getProductosPack().get(p1));
	    this.p.eliminarProductoDePack(p1, 9);
	    assertNotNull(this.p.getProductosPack());
	    assertTrue(this.p.getProductosPack().isEmpty());
	}

	/**
	 * Test method for
	 * {@link producto.Pack#eliminarProductoDePack(LineaProductoVenta, Integer)}
	 */
	@Test
	void testEliminarProductoDePackInvalidoArgs() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 100, 5.99);
		this.p.añadirProductoAPack(p1, 10);

		// Producto null
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			p.eliminarProductoDePack(null, 1);
		});
		assertEquals("Los argumentos introducidos no son validos", e.getMessage());

		// Cantidad negativa
		e = assertThrows(IllegalArgumentException.class, () -> {
			p.eliminarProductoDePack(p1, -1);
		});
		assertEquals("Los argumentos introducidos no son validos", e.getMessage());

		// Producto que no existe en el pack
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 10, 5.99);
		e = assertThrows(IllegalArgumentException.class, () -> {
			p.eliminarProductoDePack(p2, 1);
		});
		assertEquals("El producto no existe en el pack", e.getMessage());
	}
}
