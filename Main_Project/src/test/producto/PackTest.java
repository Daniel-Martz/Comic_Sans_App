/**
 * 
 */
package test.producto;
import producto.LineaProductoVenta;
import producto.Pack;
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
	 * Test method for {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 */
	@Test
	void testAñadirProductoValidoAPack() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
	  LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
	  LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12, 5.99);
    
    p.añadirProductoAPack(p1, 2);
    p.añadirProductoAPack(p2, 1);
    p.añadirProductoAPack(p3, 1);

    assertTrue(p.getProductosPack().containsKey(p1) && p.getProductosPack().containsKey(p2) && p.getProductosPack().containsKey(p3));
	}

	/**
	 * Test method for {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 */
	@Test
	void testAñadirProductoValidoYaExistenteAPack() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
    
    p.añadirProductoAPack(p1, 2);
    p.añadirProductoAPack(p1, 1);

    assertTrue(p.getProductosPack().get(p1) == 3);
	}

	/**
	 * Test method for {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
	 * 
	 */
	@Test
	void testAñadirProductoInvalidoPorStockAPack() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
    //Este pack tiene stock 5 y estamos indicando que cada pack incluirá 5 unidades,
    Exception e = assertThrows(IllegalArgumentException.class,()-> {p.añadirProductoAPack(p1, 5);});
    assertEquals(e.getMessage(), "El stock necesario para el paquete supera el stock actual del producto");
	}

  /**
	 * Test method for {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
   *
  */
	void testAñadirProductoInvalidoPorArgs1() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
    Exception e = assertThrows(IllegalArgumentException.class,()-> {p.añadirProductoAPack(p1, -1);});
    assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

  /**
	 * Test method for {@link producto.Pack#añadirProductoAPack(producto.LineaProductoVenta, java.lang.Integer)}.
   *
  */
	void testAñadirProductoInvalidoPorArgs2() {
	  LineaProductoVenta p1 = null;
    Exception e = assertThrows(IllegalArgumentException.class,()-> {p.añadirProductoAPack(p1, 2);});
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
void testAñadirProductosAPackConListaNula() {
  Exception e = assertThrows(IllegalArgumentException.class, ()->{p.añadirProductosAPack(null);}) ;
    assertEquals(e.getMessage(), "La lista de productos no puede ser nula");
	}

	/**
	 * Test method for {@link producto.Pack#getProductosPack()}.
	 */
	@Test
	void testGetProductosPack() {
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
    Map <LineaProductoVenta, Integer> listaProdsDevuelta = this.p.getProductosPack();
    listaProdsDevuelta.put(p1, 1);
    listaProdsDevuelta.put(p2, 1);
    listaProdsDevuelta.put(p3, 1);
    assertEquals(listaProds.entrySet(), listaProdsDevuelta.entrySet());
	}

  /**
   * Test method for {@link producto.Pack#eliminarProductoDePack(LineaProductoVenta, Integer)}
   */
  @Test
  void testEliminarProductoDePack(){
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 100, 5.99);
    this.p.añadirProductoAPack(p1, 10);
    this.p.eliminarProductoDePack(p1, 1);
    assertEquals(this.p.getProductosPack().get(p1), (Integer)9);
    this.p.eliminarProductoDePack(p1, 9);
    Exception e = assertThrows(IllegalStateException.class,()-> {p.getProductosPack();});
    assertEquals(e.getMessage(), "Este pack no contiene productos");

  }
}
