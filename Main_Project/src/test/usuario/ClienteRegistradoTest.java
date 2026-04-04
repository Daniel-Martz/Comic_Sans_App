package test.usuario;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import producto.*;
import tiempo.DateTimeSimulado;
import usuario.*;
import solicitud.*;
import tiempo.*;
class ClienteRegistradoTest {
  ClienteRegistrado cliente = new ClienteRegistrado("Juan", "01122233A", "123456");
	@Test
	void testGetGastoTotalPedidos() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 6);
	  LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comid de DC", new File("Foto2.png"), 10, 6);
	  Pack pack1 = new Pack("DC", "Comics varios", new File("Foto2.png"), 10, 11);
    pack1.añadirProductoAPack(p1, 2);
    pack1.añadirProductoAPack(p2, 1);
    cliente.añadirProductoACarrito(p1, 3);
    cliente.añadirProductoACarrito(p2, 2);
    cliente.añadirProductoACarrito(pack1, 1);
    cliente.realizarPedido(); 
    LineaProductoVenta p3 = new LineaProductoVenta("Star Wars", "Comic de Star Wars", new File("Foto1.png"), 10, 6);
	  LineaProductoVenta p4 = new LineaProductoVenta("Star Trek", "Comid de Star Trek", new File("Foto2.png"), 10, 6);
	  Pack pack2 = new Pack("DC", "Comics varios del espacio", new File("Foto2.png"), 10, 11);
    pack1.añadirProductoAPack(p3, 2);
    pack1.añadirProductoAPack(p4, 1);
    cliente.añadirProductoACarrito(p3, 3);
    cliente.añadirProductoACarrito(p4, 2);
    cliente.añadirProductoACarrito(pack2, 1);
    cliente.realizarPedido(); 
    for(SolicitudPedido s : cliente.getPedidos()){
      cliente.pagarPedido(s, "0123456789012345", "123", new DateTimeSimulado());
    }
    assertTrue(cliente.getGastoTotalPedidos() == 3*6 + 2*6 + 1*11 + 3*6 + 2*6 + 1*11);
	}

	@Test
	void testRealizarOferta() {
    ClienteRegistrado ofertante = cliente;
    ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");

    ProductoSegundaMano pseg1 = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Pelota firmada", new File("pelota.jpg"));
    ProductoSegundaMano pseg2 = cliente.añadirProductoACarteraDeIntercambio("Mesa", "Mesa de mármol", new File("mesa.jpg"));
    Set<ProductoSegundaMano> productosOfertante = new HashSet<>();
    Set<ProductoSegundaMano> productosDestinatario = new HashSet<>();
    //Nos aseguramos de validar los productos para que se puedan intercambiar 
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
    
    //Nos aseguramos de validar los productos para que se puedan intercambiar 
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
      
    assertTrue(true);
	}

	@Test
	void testRechazarOferta() {
    assertTrue(true);
	}

	@Test
	void testEliminarOferta() {
    assertTrue(true);
	}

	@Test
	void testEscribirReseñaNoValido() {
	  LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 10, 6);
    Exception e = assertThrowsExactly(IllegalArgumentException.class, ()->{cliente.escribirReseña(p1, "Un producto estupendo", 6, new DateTimeSimulado());});
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
	  for(SolicitudPedido s : cliente.getPedidos()){
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
    e.añadirPermiso(Permiso.INTERCAMBIOS);
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
    ProductoSegundaMano pseg3 = cliente.añadirProductoACarteraDeIntercambio("Botas de montaña", "Botas bonitas, poco usadas, perfectas para caminos por la montaña", new File("botas.jpg"));
    //Nos aseguramos de validar los productos para que se puedan intercambiar 
    Empleado e = new Empleado("federico", "01122233A", "123456");
    e.añadirPermiso(Permiso.VALIDACIONES);
    e.validarProducto(pseg1.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
    e.validarProducto(pseg2.getSolicitudValidacion(), 9.0, 20.0, EstadoConservacion.USO_LIGERO);
    e.validarProducto(pseg3.getSolicitudValidacion(), 7.0, 20.0, EstadoConservacion.USO_LIGERO);

    Set<ProductoSegundaMano> productosOfertante = new HashSet<>();
    Set<ProductoSegundaMano> productosDestinatario = new HashSet<>();
    productosOfertante.add(pseg1);
    productosDestinatario.add(pseg2);
    productosOfertante.add(pseg3);
    assertTrue(ofertante.getProductosSegundaManoDisponibles().get(0) == pseg3);
	}

}
