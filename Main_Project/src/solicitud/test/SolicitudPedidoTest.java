package solicitud.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.*;

import producto.*;
import usuario.*;
import solicitud.Pago;
import solicitud.SolicitudPedido;
import tiempo.DateTimeSimulado;

class SolicitudPedidoTest {
  private static SolicitudPedido solicitudPedido = null;
  @BeforeAll
  static void prepararTest(){
    LineaProductoVenta prod1 = new LineaProductoVenta("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Pack pack1 = new Pack("Cometa", "Una cometa muy chula", new File("cometa.jpg"), 5, 10);
    Map<LineaProductoVenta, Integer> prods = new HashMap<>();
    prods.put(prod1, 5);
    prods.put(pack1, 3);
    SolicitudPedidoTest.solicitudPedido = new SolicitudPedido(new ClienteRegistrado("Rigoberto", "01122233A", "123456" ), prods);
  }

	@Test
	void testGetCostePedido() {
    assertTrue(SolicitudPedidoTest.solicitudPedido.getCostePedido() == 80);
	}

	@Test
	void testPagado() {
    SolicitudPedidoTest.solicitudPedido.añadirPagoPedido(new Pago(new DateTimeSimulado(), 10));
    assertTrue(SolicitudPedidoTest.solicitudPedido.pagado());
	}

}
