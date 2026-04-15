package modelo.aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.aplicacion.GestorSolicitudes;
import modelo.solicitud.*;
import modelo.producto.*;
import modelo.usuario.ClienteRegistrado;
import modelo.tiempo.DateTimeSimulado;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

class GestorSolicitudesTest {

	@BeforeEach
	void resetearSingleton() throws Exception {
		Field instanciaGS = GestorSolicitudes.class.getDeclaredField("instancia");
		instanciaGS.setAccessible(true);
		instanciaGS.set(null, null);
	}

	private ClienteRegistrado crearCliente() {
		return new ClienteRegistrado("Pedro", "123456789A", "pass1234");
	}

	private SolicitudPedido crearPedido() {
		Map<LineaProductoVenta, Integer> productos = new HashMap<>();
		LineaProductoVenta productoPrueba = new LineaProductoVenta("Libro", "Un libro", new File("foto.jpg"), 10, 15.0);
		productos.put(productoPrueba, 1);
		ClienteRegistrado cliente = crearCliente();
		return new SolicitudPedido(cliente, productos);
	}

	private ProductoSegundaMano crearProducto() {
		return new ProductoSegundaMano("Pelota", "Una pelota", new File("pelota.jpg"), crearCliente());
	}

	private SolicitudIntercambio crearIntercambio() {
		ClienteRegistrado ofertante = new ClienteRegistrado("ofertante", "123456789A", "pass1234");
		ClienteRegistrado destinatario = new ClienteRegistrado("destinatario", "987654321B", "pass5678");
		Oferta oferta = new Oferta(new DateTimeSimulado(), destinatario, ofertante, new HashSet<>(), new HashSet<>());
		return new SolicitudIntercambio("codigo1", "codigo2", "Tienda", oferta);
	}

	/**
   * Se comprueba que al añadir un pedido nuevo a la aplicación, esto se hace correctamente
   */
  @Test
	void testAñadirPedidoValido() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		SolicitudPedido pedido = crearPedido();

		gs.añadirPedido(pedido);
		boolean bool = gs.getPedidos().contains(pedido);
		assertTrue(bool);
	}
 
	@Test
	void testAñadirPedidoNulo() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();

		assertThrows(IllegalArgumentException.class, () -> gs.añadirPedido(null));
	}

	@Test
	void testEliminarPedidoExistente() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		SolicitudPedido pedido = crearPedido();

		gs.añadirPedido(pedido);
		gs.eliminarPedido(pedido);

		assertFalse(gs.getPedidos().contains(pedido));
	} 
  
	@Test
	void testAñadirIntercambioValido() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		SolicitudIntercambio intercambio = crearIntercambio();

		gs.añadirSolicitudIntercambio(intercambio);

		assertTrue(gs.getIntercambios().contains(intercambio));
	}

	@Test
	void testAñadirIntercambioNulo() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();

		assertThrows(IllegalArgumentException.class, () -> gs.añadirSolicitudIntercambio(null));
	}

	@Test
	void testEliminarIntercambioExistente() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		SolicitudIntercambio intercambio = crearIntercambio();

		gs.añadirSolicitudIntercambio(intercambio);
		gs.eliminarSolicitudIntercambio(intercambio);

		assertFalse(gs.getIntercambios().contains(intercambio));
	}

	@Test
	void testAñadirValidacionNula() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();

		assertThrows(IllegalArgumentException.class, () -> gs.añadirSolicitudValidacion(null));
	}

	@Test
	void testEliminarValidacionExistente() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		ProductoSegundaMano producto = crearProducto();
		SolicitudValidacion validacion = producto.getSolicitudValidacion();
		
		gs.eliminarSolicitudValidacion(validacion);

		assertFalse(gs.getValidaciones().contains(validacion));
	}

	@Test
	void testGetIntercambiosPendientesSoloNoAprobados() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		SolicitudIntercambio intercambio = crearIntercambio();
		
		gs.añadirSolicitudIntercambio(intercambio);

		assertFalse(gs.getIntercambiosPendientes().isEmpty());
		assertTrue(gs.getIntercambiosPendientes().contains(intercambio));
	}

	@Test
	void testGetIntercambiosPendientesConAprobado() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();
		SolicitudIntercambio intercambio = crearIntercambio();
		gs.añadirSolicitudIntercambio(intercambio);

		intercambio.aprobarIntercambio("codigo1", "codigo2");

		assertFalse(gs.getIntercambiosPendientes().contains(intercambio));
	}

	@Test
	void testGetIntercambiosPendientesFiltroMixto() {
		GestorSolicitudes gs = GestorSolicitudes.getInstancia();

		SolicitudIntercambio aprobado = crearIntercambio();
		SolicitudIntercambio noAprobado = crearIntercambio();

		gs.añadirSolicitudIntercambio(aprobado);
		gs.añadirSolicitudIntercambio(noAprobado);

		aprobado.aprobarIntercambio("codigo1", "codigo2");

		List<SolicitudIntercambio> pendientes = gs.getIntercambiosPendientes();

		assertEquals(1, pendientes.size());
		assertTrue(pendientes.contains(noAprobado));
		assertFalse(pendientes.contains(aprobado));
	}
}
