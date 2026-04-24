package modelo.usuario.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import modelo.producto.*;
import modelo.tiempo.DateTimeSimulado;
import modelo.tiempo.TiempoSimulado;
import modelo.usuario.*;
import modelo.solicitud.*;
import modelo.notificacion.*;
import modelo.aplicacion.Aplicacion;

/**
 * Implementa la clase de pruebas ClienteRegistradoTest.
 * Verifica el correcto funcionamiento de las operaciones de un cliente registrado.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
class ClienteRegistradoTest {
	
	ClienteRegistrado cliente = new ClienteRegistrado("Juan", "01122233A", "123456");

	@Test
	void testGetGastoTotalPedidos() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 1000, 6);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comid de DC", new File("Foto2.png"), 1000, 6);
		Pack pack1 = new Pack("DC", "Comics varios", new File("Foto2.png"), 10, 11);
		
		pack1.añadirProductoAPack(p1, 2);
		pack1.añadirProductoAPack(p2, 1);
		
		cliente.añadirProductoACarrito(p1, 3);
		cliente.añadirProductoACarrito(p2, 2);
		cliente.añadirProductoACarrito(pack1, 1);
		cliente.realizarPedido(); 
		
		LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto1.png"), 1000, 6);
		LineaProductoVenta p4 = new LineaProductoVenta("Star Trek", "Comid de Star Trek", new File("Foto2.png"), 1000, 6);
		Pack pack2 = new Pack("DC", "Comics varios del espacio", new File("Foto2.png"), 10, 11);
		
		pack2.añadirProductoAPack(p3, 2);
		pack2.añadirProductoAPack(p4, 1);
		
		cliente.añadirProductoACarrito(p3, 3);
		cliente.añadirProductoACarrito(p4, 2);
		cliente.añadirProductoACarrito(pack2, 1);
		cliente.realizarPedido(); 
		
		for(SolicitudPedido s : cliente.getPedidos()){
			cliente.pagarPedido(s, "0123456789012345", "123", new DateTimeSimulado());
		}
		
		double gastoEsperado = 3*6 + 2*6 + 1*11 + 3*6 + 2*6 + 1*11;
		assertEquals(gastoEsperado, cliente.getGastoTotalPedidos(), 0.01);
	}

	@Test
	void testRealizarOferta() {
		ClienteRegistrado ofertante = cliente;
		ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");

		ProductoSegundaMano pseg1 = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Pelota firmada", new File("pelota.jpg"));
		ProductoSegundaMano pseg2 = cliente.añadirProductoACarteraDeIntercambio("Mesa", "Mesa de mármol", new File("mesa.jpg"));
		Set<ProductoSegundaMano> productosOfertante = new HashSet<>();
		Set<ProductoSegundaMano> productosDestinatario = new HashSet<>();
		
		Empleado e = new Empleado("federico", "01122233A", "123456");
		e.añadirPermiso(Permiso.VALIDACIONES);
		e.validarProducto(pseg1.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
		e.validarProducto(pseg2.getSolicitudValidacion(), 9.0, 20.0, EstadoConservacion.USO_LIGERO);
		
		productosOfertante.add(pseg1);
		productosDestinatario.add(pseg2);
		Oferta o = ofertante.realizarOferta(productosOfertante, productosDestinatario, destinatario);
		
		assertTrue(ofertante.getOfertasRealizadas().get(0) == o);
	}

	@Test
	void testRecibirOferta() {
		ClienteRegistrado ofertante = cliente;
		ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");

		ProductoSegundaMano pseg1 = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Pelota firmada", new File("pelota.jpg"));
		ProductoSegundaMano pseg2 = cliente.añadirProductoACarteraDeIntercambio("Mesa", "Mesa de mármol", new File("mesa.jpg"));
		
		Empleado e = new Empleado("federico", "01122233A", "123456");
		e.añadirPermiso(Permiso.VALIDACIONES);
		e.validarProducto(pseg1.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
		e.validarProducto(pseg2.getSolicitudValidacion(), 9.0, 20.0, EstadoConservacion.USO_LIGERO);

		Set<ProductoSegundaMano> productosOfertante = new HashSet<>();
		Set<ProductoSegundaMano> productosDestinatario = new HashSet<>();
		productosOfertante.add(pseg1);
		productosDestinatario.add(pseg2);
		
		Oferta o = ofertante.realizarOferta(productosOfertante, productosDestinatario, destinatario);
		
		assertTrue(destinatario.getOfertasRecibidas().get(0) == o);
	}

	@Test
	void testAceptarOferta() {
		ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");
		Oferta o = cliente.realizarOferta(new HashSet<>(), new HashSet<>(), destinatario);
		destinatario.aceptarOferta(o);
		assertNotNull(Aplicacion.getInstancia().getGestorSolicitud());
	}

	@Test
	void testRechazarOferta() {
		ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");
		Oferta o = cliente.realizarOferta(new HashSet<>(), new HashSet<>(), destinatario);
		destinatario.rechazarOferta(o);
		assertFalse(destinatario.getOfertasRecibidas().contains(o));
		assertFalse(cliente.getOfertasRealizadas().contains(o));
	}

	@Test
	void testEliminarOferta() {
		ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");
		Oferta o = cliente.realizarOferta(new HashSet<>(), new HashSet<>(), destinatario);
		cliente.eliminarOfertaRealizada(o);
		assertFalse(cliente.getOfertasRealizadas().contains(o));
	}

	@Test
	void testEscribirReseñaNoValido() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 6);
		Exception e = assertThrowsExactly(IllegalArgumentException.class, ()->{
			cliente.escribirReseña(p1, "Un producto estupendo", 6, new DateTimeSimulado());
		});
		assertEquals(e.getMessage(), "La puntuación debe ser un real entre 0.00 y 5.00");
	}

	@Test
	void testEscribirReseñaValido() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 6);
		cliente.escribirReseña(p1, "Un producto estupendo", 5, new DateTimeSimulado());
		Reseña r = cliente.getReseñas().get(0);
		assertTrue(r.getProducto() == p1 && r.getPuntuacion() == 5 && r.getDescripcion() == "Un producto estupendo");
	} 

	@Test
	void testEliminarReseña() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 6);
		cliente.escribirReseña(p1, "Un producto estupendo", 5, new DateTimeSimulado());
		Reseña r = cliente.getReseñas().get(0);
		cliente.eliminarReseña(p1, r);
		assertTrue(cliente.getReseñas().isEmpty() && p1.getReseña().isEmpty());
	}

	@Test
	void testCancelarPedido() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 1000, 6);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comid de DC", new File("Foto2.png"), 1000, 6);
		Pack pack1 = new Pack("DC", "Comics varios", new File("Foto2.png"), 10, 11);
		pack1.añadirProductoAPack(p1, 2);
		pack1.añadirProductoAPack(p2, 1);
		cliente.añadirProductoACarrito(p1, 3);
		cliente.añadirProductoACarrito(p2, 2);
		cliente.añadirProductoACarrito(pack1, 1);
		cliente.realizarPedido(); 
		
		List<SolicitudPedido> pedidosACancelar = new ArrayList<>(cliente.getPedidos());
		for(SolicitudPedido s : pedidosACancelar){
			cliente.cancelarPedido(s);
		}
		assertTrue(cliente.getPedidos().isEmpty());
	}

	@Test
	void testPagarPedido() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 1000, 6);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comid de DC", new File("Foto2.png"), 1000, 6);
		Pack pack1 = new Pack("DC", "Comics varios", new File("Foto2.png"), 10, 11);
		pack1.añadirProductoAPack(p1, 2);
		pack1.añadirProductoAPack(p2, 1);
		cliente.añadirProductoACarrito(p1, 3);
		cliente.añadirProductoACarrito(p2, 2);
		cliente.añadirProductoACarrito(pack1, 1);
		cliente.realizarPedido(); 
		
		for(SolicitudPedido s : cliente.getPedidos()){
			cliente.pagarPedido(s, "0123456789012345", "123", new DateTimeSimulado());
			assertTrue(s.pagado() == true && s.getEstado() == EstadoPedido.PAGADO);
		}
	}

	@Test
	void testRealizarPedido() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 1000, 6);
		LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comid de DC", new File("Foto2.png"), 1000, 6);
		Pack pack1 = new Pack("DC", "Comics varios", new File("Foto2.png"), 10, 11);
		pack1.añadirProductoAPack(p1, 2);
		pack1.añadirProductoAPack(p2, 1);
		cliente.añadirProductoACarrito(p1, 3);
		cliente.añadirProductoACarrito(p2, 2);
		cliente.añadirProductoACarrito(pack1, 1);
		SolicitudPedido pedido = cliente.realizarPedido(); 
		
		for(SolicitudPedido s : cliente.getPedidos()){
			assertEquals(s, pedido);
		}
	}

	@Test
	void testPagarValidacion() {
		ProductoSegundaMano p = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Una pelota chulísima", new File("pelota.jpg"));
		Empleado e = new Empleado("federico", "01122233A", "123456");
		e.añadirPermiso(Permiso.VALIDACIONES);
		e.validarProducto(p.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
		
		Pago pago = cliente.pagarValidacion(p.getSolicitudValidacion(), "0123456789012345", "123", new DateTimeSimulado());
		
		assertTrue(p.getSolicitudValidacion().getPagoValidacion() == pago && p.getSolicitudValidacion().getPrecioValidacion() == 5.0);
		cliente.eliminarProductoDeCarteraDeIntercambio(p);
	}

	@Test
	void testGetProductosSegundaManoDisponibles() {
		ClienteRegistrado ofertante = cliente;
		ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");

		ProductoSegundaMano pseg1 = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Pelota firmada", new File("pelota.jpg"));
		ProductoSegundaMano pseg2 = destinatario.añadirProductoACarteraDeIntercambio("Mesa", "Mesa de mármol", new File("mesa.jpg"));
		ProductoSegundaMano pseg3 = cliente.añadirProductoACarteraDeIntercambio("Botas", "Botas", new File("botas.jpg"));
		
		Empleado e = new Empleado("federico", "01122233A", "123456");
		e.añadirPermiso(Permiso.VALIDACIONES);
		e.validarProducto(pseg1.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
		e.validarProducto(pseg2.getSolicitudValidacion(), 9.0, 20.0, EstadoConservacion.USO_LIGERO);
		e.validarProducto(pseg3.getSolicitudValidacion(), 7.0, 20.0, EstadoConservacion.USO_LIGERO);

		Set<ProductoSegundaMano> productosOfertante = new HashSet<>();
		Set<ProductoSegundaMano> productosDestinatario = new HashSet<>();
		productosOfertante.add(pseg1);
		productosDestinatario.add(pseg2);
		
		ofertante.realizarOferta(productosOfertante, productosDestinatario, destinatario);

		List<ProductoSegundaMano> disponibles = ofertante.getProductosSegundaManoDisponibles();
		assertTrue(disponibles.contains(pseg3));
		assertFalse(disponibles.contains(pseg1));
	}

	@Test
	void testGetGastoTotalValidaciones() {
		ProductoSegundaMano p = cliente.añadirProductoACarteraDeIntercambio("Bici", "Bici", new File("bici.jpg"));
		Empleado e = new Empleado("federico", "01122233A", "123456");
		e.añadirPermiso(Permiso.VALIDACIONES);
		e.validarProducto(p.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
		
		assertEquals(5.0, cliente.getGastoTotalValidaciones(), 0.01);
	}

	@Test
	void testPagarValidacionNulos() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			cliente.pagarValidacion(null, "123", "123", null);
		});
		assertEquals("La solicitud de validación no puede ser nula.", e.getMessage());
	}

	@Test
	void testPagarValidacionYaPagado() {
		ProductoSegundaMano p = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Pelota", new File("pelota.jpg"));
		Empleado e = new Empleado("federico", "01122233A", "123456");
		e.añadirPermiso(Permiso.VALIDACIONES);
		e.validarProducto(p.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
		SolicitudValidacion validacion = p.getSolicitudValidacion();
		
		cliente.pagarValidacion(validacion, "0123456789012345", "123", new DateTimeSimulado());
		
		Exception ex = assertThrows(IllegalArgumentException.class, () -> {
			cliente.pagarValidacion(validacion, "0123456789012345", "123", new DateTimeSimulado());
		});
		assertEquals("La solicitud de validación ya tiene un pago asociado.", ex.getMessage());
	}

	@Test
	void testEliminarProductoDelCarrito() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto.png"), 10, 6.0);
		cliente.añadirProductoACarrito(p1, 3);
		cliente.eliminarProductoDelCarrito(p1, 1);
		
		assertEquals(2, cliente.getCarrito().getProductos().get(p1));
	}

	@Test
	void testRealizarPedidoCarritoVacio() {
		Exception e = assertThrows(IllegalStateException.class, () -> {
			cliente.realizarPedido();
		});
		assertEquals("El carrito está vacío.", e.getMessage());
	}

	@Test
	void testRealizarPedidoSinStock() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto.png"), 10, 6.0);
		cliente.añadirProductoACarrito(p1, 5);
		p1.setStock(2); 
		
		Exception e = assertThrows(IllegalStateException.class, () -> {
			cliente.realizarPedido(); 
		});
		assertEquals("El carrito está vacío.", e.getMessage());
	}

	@Test
	void testPagarPedidoNulos() {
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			cliente.pagarPedido(null, "123", "123", null);
		});
		assertEquals("El pedido y la fecha de caducidad no pueden ser nulos.", e.getMessage());
	}

	@Test
	void testPagarPedidoYaPagado() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto.png"), 1000, 6.0);
		cliente.añadirProductoACarrito(p1, 1);
		SolicitudPedido pedido = cliente.realizarPedido();
		
		cliente.pagarPedido(pedido, "0123456789012345", "123", new DateTimeSimulado());
		
		Exception e = assertThrows(IllegalStateException.class, () -> {
			cliente.pagarPedido(pedido, "0123456789012345", "123", new DateTimeSimulado());
		});
		assertEquals("El pedido ya tiene un pago asociado.", e.getMessage());
	}

	@Test
	void testPagarPedidoCaducado() {
		LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic", new File("Foto.png"), 1000, 6.0);
		cliente.añadirProductoACarrito(p1, 1);
		SolicitudPedido pedido = cliente.realizarPedido();
		TiempoSimulado.getInstance().avanzarDias(100);
		Exception e = assertThrows(IllegalStateException.class, () -> {
			cliente.pagarPedido(pedido, "0123456789012345", "123", new DateTimeSimulado());
		});
		assertEquals("El pedido ya no existe, ha caducado", e.getMessage());
	}

	@Test
	void testEliminarNotificacionPorId() {
		NotificacionPedido noti1 = new NotificacionPedido("Noti 1", new DateTimeSimulado(), null);
		cliente.anadirNotificacion(noti1);
		cliente.eliminarNotificacionPorId(noti1.getId());
		assertTrue(cliente.getNotificaciones().isEmpty());
	}
}
