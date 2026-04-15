package usuario.test;
import usuario.*;
import aplicacion.*;
import producto.*;
import java.util.*;
import solicitud.*;
import tiempo.DateTimeSimulado;
import notificacion.*;
import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import aplicacion.GestorSolicitudes;
import notificacion.NotificacionIntercambio;


class EmpleadoTest {
  Empleado e = new Empleado("federico", "01122233A", "12345");

  @BeforeAll
  public static void resetearSingletons() throws Exception {
      resetField(ConfiguracionRecomendacion.class, "instancia");
      resetField(Aplicacion.class,                "instancia");
      resetField(Catalogo.class,                  "instancia");
      resetField(GestorSolicitudes.class,         "instancia");
      resetField(SistemaEstadisticas.class,        "instancia");
      resetField(SistemaPago.class,                "instancia");
  }

  private static void resetField(Class<?> clazz, String fieldName) throws Exception {
      Field f = clazz.getDeclaredField(fieldName);
      f.setAccessible(true);
      f.set(null, null);
  }
	@Test
	void testAprobarIntercambio() {
    e.añadirPermiso(Permiso.INTERCAMBIOS);
    e.añadirPermiso(Permiso.VALIDACIONES);
	  ClienteRegistrado ofertante = new ClienteRegistrado("Juan", "01122233A", "123456");
    ClienteRegistrado destinatario = new ClienteRegistrado("Marcos", "91122233A", "123456");

    ProductoSegundaMano pseg1 = ofertante.añadirProductoACarteraDeIntercambio("Pelota", "Pelota firmada", new File("pelota.jpg"));
    ProductoSegundaMano pseg2 = destinatario.añadirProductoACarteraDeIntercambio("Mesa", "Mesa de mármol", new File("mesa.jpg"));
    
    //Nos aseguramos de validar los productos para que se puedan intercambiar 
    e.validarProducto(pseg1.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);
    e.validarProducto(pseg2.getSolicitudValidacion(), 9.0, 20.0, EstadoConservacion.USO_LIGERO);

    Set<ProductoSegundaMano> productosOfertante = new HashSet<>();
    Set<ProductoSegundaMano> productosDestinatario = new HashSet<>();
    productosOfertante.add(pseg1);
    productosDestinatario.add(pseg2);
    Oferta o = ofertante.realizarOferta(productosOfertante, productosDestinatario, destinatario);
    destinatario.aceptarOferta(o);
    String codigoOfertante = null; 
    String codigoDestinatario = null; 
    for(NotificacionCliente n : ofertante.getNotificaciones()){
      if(n instanceof NotificacionIntercambio){
        codigoOfertante = ((NotificacionIntercambio)n).getCodigoIntercambio();
      }
    }

    for(NotificacionCliente n : destinatario.getNotificaciones()){
      if(n instanceof NotificacionIntercambio){
        codigoDestinatario = ((NotificacionIntercambio)n).getCodigoIntercambio();
      }
    }
    SolicitudIntercambio s = GestorSolicitudes.getInstancia().getIntercambiosPendientes().get(0);
    e.aprobarIntercambio(s, codigoOfertante, codigoDestinatario);
    assertTrue(s.esAprobado());
	}

	@Test
	void testValidarProducto() {
    e.añadirPermiso(Permiso.VALIDACIONES);
    ClienteRegistrado cliente = new ClienteRegistrado("Juan", "01122233A", "123456");

    ProductoSegundaMano pseg1 = cliente.añadirProductoACarteraDeIntercambio("Pelota", "Pelota firmada", new File("pelota.jpg"));
    
    //Nos aseguramos de validar los productos para que se puedan intercambiar 
    e.validarProducto(pseg1.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.USO_EVIDENTE);

    assertTrue(pseg1.isValidado());
 
	}

	@Test
	void testActualizarEstadoPedido() {
    e.añadirPermiso(Permiso.PEDIDOS);
    ClienteRegistrado cliente = new ClienteRegistrado("Juan", "01122233A", "123456");
    LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "Comic de Marvel", new File("Foto1.png"), 1000, 6);
	  LineaProductoVenta p2 = new LineaProductoVenta("DC", "Comid de DC", new File("Foto2.png"), 1000, 6);
	  Pack pack1 = new Pack("DC", "Comics varios", new File("Foto2.png"), 10, 11);
    pack1.añadirProductoAPack(p1, 2);
    pack1.añadirProductoAPack(p2, 1);
    cliente.añadirProductoACarrito(p1, 3);
    cliente.añadirProductoACarrito(p2, 2);
    cliente.añadirProductoACarrito(pack1, 1);
    SolicitudPedido pedido = cliente.realizarPedido(); 
    cliente.pagarPedido(pedido, "0123456789012345", "123", new DateTimeSimulado());
    e.actualizarEstadoPedido(pedido, EstadoPedido.LISTO_PARA_RECOGER);
    assertTrue(pedido.getEstado() == EstadoPedido.LISTO_PARA_RECOGER);
    }
	

}
