package test.usuario;
import usuario.*;
import producto.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CarritoTest {
	Carrito c = new Carrito();

	/**
	 */
	@Test
	void testAñadirProductoValido() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
    
    c.añadirProducto(p1, 2);
    c.añadirProducto(p1, 1);

    assertTrue(c.getProductos().get(p1) == 3);
	}

  /**
	 * Test method for {@link usuario.Carrito#añadirProducto(LineaProductoVenta, Integer), java.lang.Integer)}.
   *
  */
	void testAñadirProductoInvalidoPorArgs1() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
    Exception e = assertThrows(IllegalArgumentException.class,()-> {c.añadirProducto(p1, -1);});
    assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

  /**
	 * Test method for {@link usuario.Carrito#añadirProducto(LineaProductoVenta, Integer), java.lang.Integer)}.
   *
  */
	void testAñadirProductoInvalidoPorArgs2() {
	  LineaProductoVenta p1 = null;
    Exception e = assertThrows(IllegalArgumentException.class,()-> {c.añadirProducto(p1, 2);});
    assertEquals(e.getMessage(), "Los argumentos introducidos no son validos");
	}

	/**
	 * Test method for {@link usuario.Carrito#getProductos(), java.lang.Integer)}.
	 */
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

  /**
	 * Test method for {@link usuario.Carrito#eliminarProducto(LineaProductoVenta, Integer), java.lang.Integer)}.
   */
  @Test
  void testEliminarProducto(){
	LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 100, 5.99);
    this.c.añadirProducto(p1, 10);
    this.c.eliminarProducto(p1, 1);
    assertEquals(this.c.getProductos().get(p1), (Integer)9);
    this.c.eliminarProducto(p1, 9);
    Exception e = assertThrows(IllegalStateException.class,()-> {c.getProductos();});
    assertEquals(e.getMessage(), "Este carrito no contiene productos");
  }

  /**
	 * Test method for {@link usuario.Carrito#getPrecioProductos(), java.lang.Integer)}.
   */
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

   /**
	 * Test method for {@link usuario.Carrito#vaciarCarrito(), java.lang.Integer)}.
   */
  @Test
  void testVaciarCarrito(){
    LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 5.99);
	  LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comic de DC", new File("Foto2.png"), 8, 5.99);
	  LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto3.png"), 12, 5.99);
    c.añadirProducto(p1, 1);
    c.añadirProducto(p2, 1);
    c.añadirProducto(p3, 1);
    c.vaciarCarrito();
    Exception e = assertThrows(IllegalStateException.class,()-> {c.getProductos();});
    assertEquals(e.getMessage(), "Este carrito no contiene productos");
	}
}
