package modelo.solicitud.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.*;

import modelo.usuario.*;
import modelo.aplicacion.*;
import modelo.producto.*;
import modelo.solicitud.*;
import modelo.notificacion.*;
import modelo.tiempo.DateTimeSimulado;

class SolicitudIntercambioTest {
  
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
		ClienteRegistrado matteo, rodrigo;
		Empleado federico=null;
		Usuario usuarioActual;
    Gestor gestor;
		Aplicacion app = Aplicacion.getInstancia();
		
		app.iniciarSesion("gestor", "123456");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof Gestor) {
			gestor = (Gestor)usuarioActual;
		}
		else {
			return;
		}
		
		federico = gestor.crearEmpleado("Federico", "132435468B");
		gestor.añadirPermiso(federico, Permiso.VALIDACIONES);
		gestor.añadirPermiso(federico, Permiso.INTERCAMBIOS);
		
		app.cerrarSesion();
		
		matteo = app.crearCuenta("Matteo", "123456789B", "Matteo@123", "Matteo@123");
		rodrigo = app.crearCuenta("Rodrigo", "123456789C", "Rodrigo@456", "Rodrigo@456");
		
		app.iniciarSesion("Matteo", "Matteo@123");
		matteo.añadirProductoACarteraDeIntercambio("Peluche de perro", "Es un peluche muy bonito y suavecito", null);
		app.cerrarSesion();
		
		app.iniciarSesion("Rodrigo", "Rodrigo@456");
		rodrigo.añadirProductoACarteraDeIntercambio("Camion de bomberos", "Un camion con 4 ruedas, es increible!", null);
		app.cerrarSesion();
		
		app.iniciarSesion("Federico", "123456");
		List<SolicitudValidacion> solicitudes = GestorSolicitudes.getInstancia().getValidaciones();
		federico.validarProducto(solicitudes.get(0), 2.4, 10, EstadoConservacion.MUY_BUENO);
		federico.validarProducto(solicitudes.get(1), 3, 10, EstadoConservacion.MUY_USADO);
		app.cerrarSesion();
		
		app.iniciarSesion("Rodrigo", "Rodrigo@456");
		List<ProductoSegundaMano> productosRodrigo = new ArrayList<>(rodrigo.getCartera().getProductos());
		rodrigo.pagarValidacion(productosRodrigo.get(0).getSolicitudValidacion(), "1234567890123456", "123", new DateTimeSimulado());
		app.cerrarSesion();

    //Matteo inicia sesion para realizar una oferta a Rodrigo
		app.iniciarSesion("Matteo", "Matteo@123");
		List<ProductoSegundaMano> productosMatteo = new ArrayList<>(matteo.getCartera().getProductos());
		matteo.pagarValidacion(productosMatteo.get(0).getSolicitudValidacion(), "1234567890123456", "123", new DateTimeSimulado());
	  
    //Sets que usaremos para realizar la oferta
		Set<ProductoSegundaMano> sMatteo = new HashSet<>(productosMatteo);
		Set<ProductoSegundaMano> sRodrigo = new HashSet<>(productosRodrigo);
    matteo.realizarOferta(sMatteo, sRodrigo, rodrigo);
		app.cerrarSesion();

    //Rodrigo inicia sesion para aceptar la oferta
		app.iniciarSesion("Rodrigo", "Rodrigo@456");
		List<Oferta> ofertasRecibidasRodrigo = rodrigo.getOfertasRecibidas();
		rodrigo.aceptarOferta(ofertasRecibidasRodrigo.get(0));
		app.cerrarSesion();

    //Matteo inicia sesion para acceder a sus notificaciones
		app.iniciarSesion("Matteo", "Matteo@123");
		List<NotificacionCliente> notifsMatteo = matteo.getNotificaciones();
    NotificacionIntercambio notifIntercambioMatteo = null;
    for(NotificacionCliente notif : notifsMatteo ){
      if(notif instanceof NotificacionIntercambio notifIntercambio){
        notifIntercambioMatteo = notifIntercambio;
        break;
      }
    }
    assertNotNull(notifIntercambioMatteo, "No se encontró NotificacionIntercambio para Matteo");
		app.cerrarSesion();

    //Rodrigo inicia sesión para acceder a sus notificaciones
		app.iniciarSesion("Rodrigo", "Rodrigo@456");
		List<NotificacionCliente> notifsRodrigo = rodrigo.getNotificaciones();
    NotificacionIntercambio notifIntercambioRodrigo = null;
		for(NotificacionCliente notif : notifsRodrigo ){
      if(notif instanceof NotificacionIntercambio notifIntercambio){
        notifIntercambioRodrigo = notifIntercambio;
        break;
      }
    }
    assertNotNull(notifIntercambioRodrigo, "No se encontró NotificacionIntercambio para Rodrigo");
		app.cerrarSesion();

		String codigoMatteo = notifIntercambioMatteo.getCodigoIntercambio();
		String codigoRodrigo = notifIntercambioRodrigo.getCodigoIntercambio();
    //Federico inicia sesion para aprobar el intercambio		
		app.iniciarSesion("Federico", "123456");
		List<SolicitudIntercambio> listaIntercambios = GestorSolicitudes.getInstancia().getIntercambios();
		SolicitudIntercambio sol = listaIntercambios.get(0);
		federico.aprobarIntercambio(sol, codigoMatteo, codigoRodrigo);
    assertTrue(sol.esAprobado());

	}

}
