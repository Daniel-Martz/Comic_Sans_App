package test.solicitud;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import descuento.*;
import producto.*;
import usuario.*;
import solicitud.Pago;
import solicitud.SolicitudPedido;
import tiempo.DateTimeSimulado;

class SolicitudPedidoTest {
  private SolicitudPedido solicitudPedido = null;
  @BeforeAll
  static void prepararTest(){
      }

	@Test
	void testGetCostePedido() {
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 5);
    prods.put(pack1, 3);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);

    assertTrue(solicitudPedido.getCostePedido() == 80);
	}

  @Test
	void testGetCostePedidoDescuentoRebaja() {
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    
    Precio d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 50);
    d.añadirProductoRebajado(prod1);
    prod1.setDescuento(d);
    prods.put(prod1, 1);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);

    assertTrue(solicitudPedido.getCostePedido() == 15);
	}

  /**
   * En este test probaremos que, al comprar 1 unidad de un producto y haber un 2x3, nos seguimos llevando 1
   */
  @Test
	void testGetCostePedidoDescuentoCantidadSinCambios() {
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    
    Cantidad d = new Cantidad(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 2, 3);
    d.añadirProductoRebajado(prod1);
    prod1.setDescuento(d);
    prods.put(prod1, 1);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    double val1 = solicitudPedido.getCostePedido();
    boolean cond2 = (val1 == 20.0);
    boolean cond1 = solicitudPedido.getRecaudacionProductos().get(new SimpleEntry<LineaProductoVenta, Integer>(prod1, 3)) != null;
    assertTrue(cond1 && cond2);
	}

  /**
   * En este test probaremos que, al comprar 2 unidades de un producto y haber un 2x3, realmente nos llevamos 3
   */
  @Test
	void testGetCostePedidoDescuentoCantidad() {
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    
    Cantidad d = new Cantidad(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 2, 3);
    d.añadirProductoRebajado(prod1);
    prod1.setDescuento(d);
    prods.put(prod1, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    //Confirmamos que se han pagado 2 productos y se han recibido 3
    boolean cond1 = solicitudPedido.getRecaudacionProductos().get(new SimpleEntry<LineaProductoVenta, Integer>(prod1, 3)) != null;
    boolean cond2 = solicitudPedido.getCostePedido() == 30;
    assertTrue(cond1 && cond2);
	}

  /**
   * En este test probaremos que, al comprar productos que tienen un descuento de regalo asociado y superar el umbral, se añaden los productos regalados
   */
  @Test
	void testGetCostePedidoDescuentoRegalo() {
    //Creamos los productos
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);

    LineaProductoVenta prod2 = new LineaProductoVenta("Botas", "Botas de montaña", new File("botas.jpg")
      , 5, 10);
    
    //Regalaremos dos unidades de prod3
    LineaProductoVenta prod3 = new LineaProductoVenta("Figura", "Figura Pokemon", new File("figura.jpg")
      , 5, 2);
    
    //Creamos el descuento con el producto de regalo y lo asociamos a los productos deseados
    Map<LineaProductoVenta, Integer> prodsRegalo = new HashMap<>();
    prodsRegalo.put(prod3, 2);
    Regalo d = new Regalo(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 25, prodsRegalo);
    d.añadirProductoRebajado(prod1);
    d.añadirProductoRebajado(prod2);
    prod1.setDescuento(d);
    prod2.setDescuento(d);
    
    //Creamos la solicitudPedido
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 2);
    prods.put(prod2, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    //Confirmamos que se han pagado 2 productos y se han recibido 3
    assertTrue(solicitudPedido.getCostePedido() == 50 && solicitudPedido.getRecaudacionProductos().get(new SimpleEntry<LineaProductoVenta, Integer>(prod3, 2)) != null);
	}

  /**
   * En este test probaremos que, al comprar productos que tienen un descuento de regalo asociado y NO superar el umbral, NO se añaden los productos regalados
   */
  @Test
	void testGetCostePedidoDescuentoRebajaRegaloAplicado() {
    //Creamos los productos
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);

    LineaProductoVenta prod2 = new LineaProductoVenta("Botas", "Botas de montaña", new File("botas.jpg")
      , 5, 10);
    
    //Regalaremos dos unidades de prod3
    LineaProductoVenta prod3 = new LineaProductoVenta("Figura", "Figura Pokemon", new File("figura.jpg")
      , 5, 2);
    
    //Creamos el descuento con el producto de regalo y lo asociamos a los productos deseados
    Map<LineaProductoVenta, Integer> prodsRegalo = new HashMap<>();
    prodsRegalo.put(prod3, 2);
    Regalo d = new Regalo(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 100, prodsRegalo);
    d.añadirProductoRebajado(prod1);
    d.añadirProductoRebajado(prod2);
    prod1.setDescuento(d);
    prod2.setDescuento(d);
    
    //Creamos la solicitudPedido
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 2);
    prods.put(prod2, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    //Confirmamos que se han pagado 2 productos y se han recibido 3
    assertTrue(solicitudPedido.getCostePedido() == 50 && solicitudPedido.getRecaudacionProductos().get(new SimpleEntry<LineaProductoVenta, Integer>(prod3, 2)) == null);
	}

  /**
   * En este test probaremos que, al comprar productos que tienen un descuento de regalo asociado y NO superar el umbral, NO se añaden los productos regalados
   */
  @Test
	void testGetCostePedidoDescuentoRegaloNoAplicado() {
    //Creamos los productos
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);

    LineaProductoVenta prod2 = new LineaProductoVenta("Botas", "Botas de montaña", new File("botas.jpg")
      , 5, 10);
    
    //Regalaremos dos unidades de prod3
    LineaProductoVenta prod3 = new LineaProductoVenta("Figura", "Figura Pokemon", new File("figura.jpg")
      , 5, 2);
    
    //Creamos el descuento con el producto de regalo y lo asociamos a los productos deseados
    Map<LineaProductoVenta, Integer> prodsRegalo = new HashMap<>();
    prodsRegalo.put(prod3, 2);
    Regalo d = new Regalo(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 100, prodsRegalo);
    d.añadirProductoRebajado(prod1);
    d.añadirProductoRebajado(prod2);
    prod1.setDescuento(d);
    prod2.setDescuento(d);
    
    //Creamos la solicitudPedido
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 2);
    prods.put(prod2, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    //Confirmamos que se han pagado 2 productos y se han recibido 3
    assertTrue(solicitudPedido.getCostePedido() == 50 && solicitudPedido.getRecaudacionProductos().get(new SimpleEntry<LineaProductoVenta, Integer>(prod3, 2)) == null);
	}


  /**
   * En este test probaremos que, al comprar productos que tienen un descuento de regalo asociado y NO superar el umbral, NO se añaden los productos regalados
   */
  @Test
	void testGetCostePedidoDescuentoRebajaPorUmbralNoAplicado() {
    //Creamos los productos
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);

    LineaProductoVenta prod2 = new LineaProductoVenta("Botas", "Botas de montaña", new File("botas.jpg")
      , 5, 10);
    
    //Creamos el descuento con el producto de regalo y lo asociamos a los productos deseados
    RebajaUmbral d = new RebajaUmbral(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 100, 50);
    d.añadirProductoRebajado(prod1);
    d.añadirProductoRebajado(prod2);
    prod1.setDescuento(d);
    prod2.setDescuento(d);
    
    //Creamos la solicitudPedido
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 2);
    prods.put(prod2, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    //Confirmamos que se han pagado 2 productos y se han recibido 3
    assertTrue(solicitudPedido.getCostePedido() == 50);
	}

  /**
   * En este test probaremos que, al comprar productos que tienen un descuento de regalo asociado y NO superar el umbral, NO se añaden los productos regalados
   */
  @Test
	void testGetCostePedidoDescuentoRebajaPorUmbralAplicado() {
    //Creamos los productos
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);

    LineaProductoVenta prod2 = new LineaProductoVenta("Botas", "Botas de montaña", new File("botas.jpg")
      , 5, 10);
    
    //Creamos el descuento con el producto de regalo y lo asociamos a los productos deseados
    RebajaUmbral d = new RebajaUmbral(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 20, 50);
    d.añadirProductoRebajado(prod1);
    d.añadirProductoRebajado(prod2);
    prod1.setDescuento(d);
    prod2.setDescuento(d);
    
    //Creamos la solicitudPedido
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 2);
    prods.put(prod2, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    //Confirmamos que se han pagado 2 productos y se han recibido 3
    assertTrue(solicitudPedido.getCostePedido() == 30);
	}

	@Test
	void testPagado() {
    //Creamos los productos
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg")
      , 5, 10);

    LineaProductoVenta prod2 = new LineaProductoVenta("Botas", "Botas de montaña", new File("botas.jpg")
      , 5, 10);
    
    //Creamos la solicitudPedido
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 2);
    prods.put(prod2, 2);
    prods.put(pack1, 1);
    solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
    solicitudPedido.añadirPagoPedido(new Pago(new DateTimeSimulado(), 10));
    assertTrue(solicitudPedido.pagado());
	}

}
