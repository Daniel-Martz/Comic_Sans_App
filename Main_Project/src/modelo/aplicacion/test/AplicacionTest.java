package modelo.aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import modelo.aplicacion.*;
import modelo.notificacion.*;
import modelo.producto.*;
import modelo.solicitud.*;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.*;

/**
 * Tests de integración y unidad para Aplicacion.java.
 *
 * Como Aplicacion es un Singleton compartido, los tests se ordenan
 * explícitamente con @Order para controlar el estado acumulado entre ellos.
 * Cada test deja la sesión cerrada al terminar para no interferir con el siguiente.
 */
@TestMethodOrder(OrderAnnotation.class)
class AplicacionTest {

    static Aplicacion app;

    static Empleado empleadoGlobal;
    static ClienteRegistrado clienteGlobal1;
    static ClienteRegistrado clienteGlobal2;

    @BeforeEach
    void cerrarSesionSiHay() {
        if (app == null) {
            app = Aplicacion.getInstancia();
        }
        try {
            if (app.getUsuarioActual() != null) {
                app.cerrarSesion();
            }
        } catch (Exception ignored) { }
    }

    // =========================================================================
    // BLOQUE 1 – SINGLETON
    // =========================================================================

    @Test @Order(1)
    void testGetInstanciaNoEsNull() {
        assertNotNull(Aplicacion.getInstancia());
    }

    @Test @Order(2)
    void testGetInstanciaMismaReferencia() {
        assertSame(Aplicacion.getInstancia(), Aplicacion.getInstancia());
    }

    @Test @Order(3)
    void testNombreNoEsNull() {
        assertNotNull(app.getNombre());
    }

    // =========================================================================
    // BLOQUE 2 – SUBSISTEMAS (getters)
    // =========================================================================

    @Test @Order(4)
    void testGetCatalogoNoEsNull() {
        assertNotNull(app.getCatalogo());
    }

    @Test @Order(5)
    void testGetSistemaPagoNoEsNull() {
        assertNotNull(app.getSistemaPago());
    }

    @Test @Order(6)
    void testGetSistemaEstadisticasNoEsNull() {
        assertNotNull(app.getSistemaEstadisticas());
    }

    @Test @Order(7)
    void testGetGestorSolicitudNoEsNull() {
        assertNotNull(app.getGestorSolicitud());
    }

    @Test @Order(8)
    void testGetConfiguracionRecomendacionNoEsNull() {
        assertNotNull(app.getConfiguracionRecomendacion());
    }

    // =========================================================================
    // BLOQUE 3 – INICIAR / CERRAR SESIÓN
    // =========================================================================

    @Test @Order(10)
    void testIniciarSesionGestorCorrecto() {
        app.iniciarSesion("gestor", "123456");
        assertNotNull(app.getUsuarioActual());
        assertInstanceOf(Gestor.class, app.getUsuarioActual());
        app.cerrarSesion();
    }

    @Test @Order(11)
    void testIniciarSesionContraseñaIncorrecta() {
        assertThrows(IllegalArgumentException.class,
            () -> app.iniciarSesion("gestor", "mal"));
    }

    @Test @Order(12)
    void testIniciarSesionUsuarioInexistente() {
        assertThrows(IllegalArgumentException.class,
            () -> app.iniciarSesion("nadie", "123456"));
    }

    @Test @Order(13)
    void testIniciarSesionNombreNulo() {
        assertThrows(IllegalArgumentException.class,
            () -> app.iniciarSesion(null, "123456"));
    }

    @Test @Order(14)
    void testIniciarSesionContraseñaNula() {
        assertThrows(IllegalArgumentException.class,
            () -> app.iniciarSesion("gestor", null));
    }

    @Test @Order(15)
    void testIniciarSesionConSesionYaActiva() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalStateException.class,
            () -> app.iniciarSesion("gestor", "123456"));
        app.cerrarSesion();
    }

    @Test @Order(16)
    void testCerrarSesionSinSesionActiva() {
        assertThrows(IllegalStateException.class, () -> app.cerrarSesion());
    }

    @Test @Order(17)
    void testCerrarSesionDejaUsuarioActualNull() {
        app.iniciarSesion("gestor", "123456");
        app.cerrarSesion();
        assertNull(app.getUsuarioActual());
    }

    @Test @Order(18)
    void testGetUsuarioActualNullSinSesion() {
        assertNull(app.getUsuarioActual());
    }

    @Test @Order(19)
    void testSetUsuarioActual() {
        app.iniciarSesion("gestor", "123456");
        Usuario u = app.getUsuarioActual();
        assertNotNull(u);
        app.setUsuarioActual(null);
        assertNull(app.getUsuarioActual());
        // restauramos para no romper el estado
        app.setUsuarioActual(u);
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 4 – CREAR CUENTA (ClienteRegistrado)
    // =========================================================================

    @Test @Order(20)
    void testCrearCuentaCorrectamente() {
        clienteGlobal1 = app.crearCuenta("ClienteUno", "95112992B", "Passw0rd!", "Passw0rd!");
        assertNotNull(clienteGlobal1);
        assertEquals("ClienteUno", clienteGlobal1.getNombreUsuario());
    }

    @Test @Order(21)
    void testCrearCuentaSeAgregaAUsuariosRegistrados() {
        assertTrue(app.getUsuariosRegistrados()
            .stream().anyMatch(u -> u.getNombreUsuario().equals("ClienteUno")));
    }

    @Test @Order(22)
    void testCrearCuentaSeAgregaAClientesRegistrados() {
        assertTrue(app.getClientesRegistrados()
            .stream().anyMatch(c -> c.getNombreUsuario().equals("ClienteUno")));
    }

    @Test @Order(23)
    void testCrearCuentaNombreNulo() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearCuenta(null, "95112992B", "Passw0rd!", "Passw0rd!"));
    }

    @Test @Order(24)
    void testCrearCuentaNombreVacio() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearCuenta("   ", "95112992B", "Passw0rd!", "Passw0rd!"));
    }

    @Test @Order(25)
    void testCrearCuentaContraseñaCorta() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearCuenta("UserCorto", "95112992B", "ab", "ab"));
    }

    @Test @Order(26)
    void testCrearCuentaContraseñaNula() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearCuenta("UserNullPass", "95112992B", null, null));
    }

    @Test @Order(27)
    void testCrearCuentaDNIDemasiadoCorto() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearCuenta("UserDNI", "123", "Passw0rd!", "Passw0rd!"));
    }

    @Test @Order(28)
    void testCrearCuentaDNINulo() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearCuenta("UserDNINull", null, "Passw0rd!", "Passw0rd!"));
    }

    @Test @Order(29)
    void testCrearCuentaNombreDuplicado() {
        app.crearCuenta("UserDup", "95112992B", "Passw0rd!", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.crearCuenta("UserDup", "95112992B", "Passw0rd!", "Passw0rd!"));
    }

    @Test @Order(30)
    void testCrearCuentaPermiteLoginPostCreacion() {
        app.crearCuenta("ClienteLogin", "95112992B", "Passw0rd!", "Passw0rd!");
        app.iniciarSesion("ClienteLogin", "loginpass");
        assertInstanceOf(ClienteRegistrado.class, app.getUsuarioActual());
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 5 – AÑADIR / ELIMINAR EMPLEADO
    // =========================================================================

    @Test @Order(40)
    void testAñadirEmpleadoSinSesionActiva() {
        assertThrows(IllegalArgumentException.class,
            () -> app.añadirEmpleado("Emp1", "5555555550", "123456"));
    }

    @Test @Order(41)
    void testAñadirEmpleadoConClienteLogueado() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.añadirEmpleado("Emp2", "5555555551", "123456"));
        app.cerrarSesion();
    }

    @Test @Order(42)
    void testAñadirEmpleadoCorrectamente() {
        app.iniciarSesion("gestor", "123456");
        empleadoGlobal = app.añadirEmpleado("EmpleadoTest", "6666666660", "123456");
        assertNotNull(empleadoGlobal);
        assertEquals("EmpleadoTest", empleadoGlobal.getNombreUsuario());
        app.cerrarSesion();
    }

    @Test @Order(43)
    void testAñadirEmpleadoSeGuardaEnUsuarios() {
        assertTrue(app.getUsuariosRegistrados()
            .stream().anyMatch(u -> u.getNombreUsuario().equals("EmpleadoTest")));
    }

    @Test @Order(44)
    void testAñadirEmpleadoNombreDuplicado() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalStateException.class,
            () -> app.añadirEmpleado("EmpleadoTest", "7777777770", "123456"));
        app.cerrarSesion();
    }

    @Test @Order(45)
    void testAñadirEmpleadoDNICorto() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalArgumentException.class,
            () -> app.añadirEmpleado("EmpDNI", "123", "123456"));
        app.cerrarSesion();
    }

    @Test @Order(46)
    void testAñadirEmpleadoContraseñaCorta() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalArgumentException.class,
            () -> app.añadirEmpleado("EmpPass", "7777777771", "ab"));
        app.cerrarSesion();
    }

    @Test @Order(47)
    void testEliminarEmpleadoSinSesion() {
        assertThrows(IllegalArgumentException.class,
            () -> app.eliminarEmpleado(empleadoGlobal));
    }

    @Test @Order(48)
    void testEliminarEmpleadoConClienteLogueado() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.eliminarEmpleado(empleadoGlobal));
        app.cerrarSesion();
    }

    @Test @Order(49)
    void testEliminarEmpleadoNullNoLanzaExcepcion() {
        app.iniciarSesion("gestor", "123456");
        assertDoesNotThrow(() -> app.eliminarEmpleado(null));
        app.cerrarSesion();
    }

    @Test @Order(50)
    void testEliminarEmpleadoCorrectamente() {
        app.iniciarSesion("gestor", "123456");
        Empleado temporal = app.añadirEmpleado("EmpTemporal", "8888888880", "123456");
        app.eliminarEmpleado(temporal);
        assertFalse(app.getUsuariosRegistrados().contains(temporal));
        app.cerrarSesion();
    }

    @Test @Order(51)
    void testEliminarEmpleadoNoEliminaOtros() {
        app.iniciarSesion("gestor", "123456");
        Empleado temporal2 = app.añadirEmpleado("EmpTemporal2", "8888888881", "123456");
        app.eliminarEmpleado(temporal2);
        // El gestor y los demás usuarios siguen estando
        assertTrue(app.getUsuariosRegistrados().stream()
            .anyMatch(u -> u.getNombreUsuario().equals("gestor")));
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 6 – CAMBIAR CONTRASEÑA
    // =========================================================================

    @Test @Order(60)
    void testCambiarContraseñaCorrectamente() {
        app.crearCuenta("UserCambio", "95112992B", "Passw0rd!", "Passw0rd!");
        app.cambiarContraseña("UserCambio", "Passw0rd!", "Passw1rd!");
        // login con la nueva contraseña debe funcionar
        app.iniciarSesion("UserCambio", "Passw1rd!");
        assertNotNull(app.getUsuarioActual());
        app.cerrarSesion();
    }

    @Test @Order(61)
    void testCambiarContraseñaAnteriorIncorrecta() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cambiarContraseña("UserCambio", "wrongold", "otrapass"));
    }

    @Test @Order(62)
    void testCambiarContraseñaUsuarioInexistente() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cambiarContraseña("NoExiste", "pass", "newpass"));
    }

    @Test @Order(63)
    void testCambiarContraseñaNombreNulo() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cambiarContraseña(null, "viejapass", "nuevapass"));
    }

    @Test @Order(64)
    void testCambiarContraseñaAnteriorNula() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cambiarContraseña("UserCambio", null, "nuevapass"));
    }

    @Test @Order(65)
    void testCambiarContraseñaNuevaNula() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cambiarContraseña("UserCambio", "nuevapass", null));
    }

    @Test @Order(66)
    void testCambiarContraseñaNuevaDemasiadoCorta() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cambiarContraseña("UserCambio", "nuevapass", "ab"));
    }

    @Test @Order(67)
    void testLoginConContraseñaViejaFallaDespuesDeCambio() {
        assertThrows(IllegalArgumentException.class,
            () -> app.iniciarSesion("UserCambio", "viejapass"));
    }

    // =========================================================================
    // BLOQUE 7 – BUSCAR PRODUCTOS (venta)
    // =========================================================================

    @Test @Order(70)
    void testBuscarProductosNuevosDevuelveLista() {
        // Añadir producto como gestor
        app.iniciarSesion("gestor", "123456");
        app.getCatalogo().añadirProducto(
            new LineaProductoVenta("ComicBusqueda", "desc", null, 10, 5.0));
        app.cerrarSesion();

        // Buscar como cliente
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        List<LineaProductoVenta> resultados = app.buscarProductosNuevos(null);
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
        app.cerrarSesion();
    }

    @Test @Order(71)
    void testBuscarProductosNuevosConPromptCoincide() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        List<LineaProductoVenta> resultados = app.buscarProductosNuevos("ComicBusqueda");
        assertTrue(resultados.stream()
            .anyMatch(p -> p.getNombre().contains("ComicBusqueda")));
        app.cerrarSesion();
    }

    @Test @Order(72)
    void testBuscarProductosNuevosConPromptSinCoincidir() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        List<LineaProductoVenta> resultados = app.buscarProductosNuevos("xyzProductoQueNoExiste99");
        assertTrue(resultados.isEmpty());
        app.cerrarSesion();
    }

    @Test @Order(73)
    void testBuscarProductosNuevosPromptVacio() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        // Prompt vacío → devuelve todos
        List<LineaProductoVenta> todos   = app.buscarProductosNuevos(null);
        List<LineaProductoVenta> vacios  = app.buscarProductosNuevos("   ");
        assertEquals(todos.size(), vacios.size());
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 8 – BUSCAR PRODUCTOS (gestión)
    // =========================================================================

    @Test @Order(80)
    void testBuscarProductosGestionSinSesion() {
        assertThrows(IllegalArgumentException.class,
            () -> app.buscarProductosNuevosGestion("algo"));
    }

    @Test @Order(81)
    void testBuscarProductosGestionConClienteLogueado() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.buscarProductosNuevosGestion("algo"));
        app.cerrarSesion();
    }

    @Test @Order(82)
    void testBuscarProductosGestionConGestorCorrecto() {
        app.iniciarSesion("gestor", "123456");
        List<LineaProductoVenta> res = app.buscarProductosNuevosGestion("ComicBusqueda");
        assertNotNull(res);
        assertFalse(res.isEmpty());
        app.cerrarSesion();
    }

    @Test @Order(83)
    void testBuscarProductosGestionConEmpleado() {
        app.iniciarSesion("EmpleadoTest", "123456");
        assertDoesNotThrow(() -> app.buscarProductosNuevosGestion("Comic"));
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 9 – BUSCAR PRODUCTO POR ID
    // =========================================================================

    @Test @Order(90)
    void testBuscarProductoNuevoSinSesion() {
        assertThrows(IllegalArgumentException.class,
            () -> app.buscarProductoNuevo(1));
    }

    @Test @Order(91)
    void testBuscarProductoNuevoConClienteLogueado() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.buscarProductoNuevo(1));
        app.cerrarSesion();
    }

    @Test @Order(92)
    void testBuscarProductoNuevoIdNegativo() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalArgumentException.class,
            () -> app.buscarProductoNuevo(-1));
        app.cerrarSesion();
    }

    @Test @Order(93)
    void testBuscarProductoNuevoIdExistente() {
        app.iniciarSesion("gestor", "123456");
        // Tomamos el ID de un producto que sabemos que existe
        LineaProductoVenta cualquiera = app.getCatalogo()
            .getProductosNuevos().iterator().next();
        LineaProductoVenta encontrado = app.buscarProductoNuevo(cualquiera.getID());
        assertNotNull(encontrado);
        assertEquals(cualquiera.getID(), encontrado.getID());
        app.cerrarSesion();
    }

    @Test @Order(94)
    void testBuscarProductoNuevoIdInexistenteDevuelveNull() {
        app.iniciarSesion("gestor", "123456");
        LineaProductoVenta noExiste = app.buscarProductoNuevo(Integer.MAX_VALUE);
        assertNull(noExiste);
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 10 – BUSCAR PRODUCTO INTERCAMBIO (por ID y por String)
    // =========================================================================

    @Test @Order(100)
    void testBuscarProductoIntercambioStringDevuelveLista() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        List<ProductoSegundaMano> res = app.buscarProductoIntercambio("peluche");
        assertNotNull(res);
        app.cerrarSesion();
    }

    @Test @Order(101)
    void testBuscarProductoIntercambioIdSinSesion() {
        assertThrows(IllegalArgumentException.class,
            () -> app.buscarProductoIntercambio(1));
    }

    @Test @Order(102)
    void testBuscarProductoIntercambioIdConCliente() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.buscarProductoIntercambio(1));
        app.cerrarSesion();
    }

    @Test @Order(103)
    void testBuscarProductoIntercambioIdNegativo() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalArgumentException.class,
            () -> app.buscarProductoIntercambio(-5));
        app.cerrarSesion();
    }

    @Test @Order(104)
    void testBuscarProductoIntercambioIdInexistenteDevuelveNull() {
        app.iniciarSesion("gestor", "123456");
        ProductoSegundaMano noExiste = app.buscarProductoIntercambio(Integer.MAX_VALUE);
        assertNull(noExiste);
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 11 – ENVIAR NOTIFICACIÓN
    // =========================================================================

    @Test @Order(110)
    void testEnviarNotificacionParametrosNulos() {
        assertThrows(IllegalArgumentException.class,
            () -> app.enviarNotificacion(null, null));
    }

    @Test @Order(111)
    void testEnviarNotificacionUsuarioNulo() {
        assertThrows(IllegalArgumentException.class,
            () -> app.enviarNotificacion(null,
                new NotificacionProducto("msg", new DateTimeSimulado())));
    }

    @Test @Order(112)
    void testEnviarNotificacionNotificacionNula() {
        assertThrows(IllegalArgumentException.class,
            () -> app.enviarNotificacion(clienteGlobal1, null));
    }

    @Test @Order(113)
    void testEnviarNotificacionAClienteRegistrado() {
        NotificacionProducto notif =
            new NotificacionProducto("Descuento especial", new DateTimeSimulado());
        int antes = clienteGlobal1.getNotificaciones().size();
        app.enviarNotificacion(clienteGlobal1, notif);
        assertEquals(antes + 1, clienteGlobal1.getNotificaciones().size());
    }

    @Test @Order(114)
    void testEnviarNotificacionAEmpleado() {
        NotificacionEmpleado notif =
            new NotificacionEmpleado("Nuevo pedido", new DateTimeSimulado());
        // empleado.getNotificaciones() es un campo privado; usamos el método de Aplicacion
        app.enviarNotificacion(empleadoGlobal, notif);
        // Si llegamos aquí sin excepción, el envío fue correcto
        assertTrue(true);
    }

    // =========================================================================
    // BLOQUE 12 – CREAR PEDIDO A PARTIR DE CARRITO
    // =========================================================================

    @Test @Order(120)
    void testCrearPedidoSinSesion() {
        assertThrows(IllegalArgumentException.class,
            () -> app.crearPedidoAPartirDeCarrito());
    }

    @Test @Order(121)
    void testCrearPedidoConGestorLogueado() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalStateException.class,
            () -> app.crearPedidoAPartirDeCarrito());
        app.cerrarSesion();
    }

    @Test @Order(122)
    void testCrearPedidoCarritoVacio() {
        // ClienteUno no tiene nada en el carrito
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        // realizarPedido lanza IllegalStateException si el carrito está vacío
        assertThrows(IllegalStateException.class,
            () -> app.crearPedidoAPartirDeCarrito());
        app.cerrarSesion();
    }

    @Test @Order(123)
    void testCrearPedidoConProductoEnCarrito() {
        // Tomamos un producto del catálogo
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();

        app.iniciarSesion("ClienteUno", "Passw0rd!");
        clienteGlobal1.añadirProductoACarrito(prod, 1);
        assertDoesNotThrow(() -> app.crearPedidoAPartirDeCarrito());
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 13 – CANCELAR PEDIDO
    // =========================================================================

    @Test @Order(130)
    void testCancelarPedidoSinSesion() {
        assertThrows(IllegalArgumentException.class,
            () -> app.cancelarPedido(null));
    }

    @Test @Order(131)
    void testCancelarPedidoConGestorLogueado() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalStateException.class,
            () -> app.cancelarPedido(null));
        app.cerrarSesion();
    }

    @Test @Order(132)
    void testCancelarPedidoNullConClienteLogueado() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalArgumentException.class,
            () -> app.cancelarPedido(null));
        app.cerrarSesion();
    }

    @Test @Order(133)
    void testCancelarPedidoPendienteCorrectamente() {
        // Creamos un pedido nuevo para poder cancelarlo
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();

        app.iniciarSesion("ClienteUno", "Passw0rd!");
        clienteGlobal1.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedido = clienteGlobal1.realizarPedido();
        assertEquals(EstadoPedido.PENDIENTE_DE_PAGO, pedido.getEstado());

        // cancelar debe quitar el pedido de la lista del cliente
        app.cancelarPedido(pedido);
        assertFalse(clienteGlobal1.getPedidos().contains(pedido));
        app.cerrarSesion();
    }

    @Test @Order(134)
    void testCancelarPedidoRestaureaStock() {
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();
        int stockAntes = prod.getStock();

        app.iniciarSesion("ClienteUno", "Passw0rd!");
        clienteGlobal1.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedido = clienteGlobal1.realizarPedido();
        app.cancelarPedido(pedido);
        // tras cancelar debe volver al valor anterior
        assertEquals(stockAntes, prod.getStock());
        app.cerrarSesion();
    }

    @Test @Order(135)
    void testCancelarPedidoQueNoPerteneceAlCliente() {
        // Creamos un segundo cliente con su propio pedido
        clienteGlobal2 = app.crearCuenta("ClienteDos", "0000000002", "Passw0rd!", "Passw0rd!");
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();

        app.iniciarSesion("ClienteDos", "pass2222");
        clienteGlobal2.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedidoAjeno = clienteGlobal2.realizarPedido();
        app.cerrarSesion();

        // ClienteUno intenta cancelar el pedido de ClienteDos
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.cancelarPedido(pedidoAjeno));
        app.cerrarSesion();

        // Limpieza: cancelar el pedido real
        app.iniciarSesion("ClienteDos", "pass2222");
        app.cancelarPedido(pedidoAjeno);
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 14 – GESTIONAR PAGO PEDIDO
    // =========================================================================

    @Test @Order(140)
    void testGestionarPagoPedidoSinSesion() {
        assertThrows(IllegalStateException.class,
            () -> app.gestionarPagoPedido(null, "1234567890123456", "123",
                new DateTimeSimulado()));
    }

    @Test @Order(141)
    void testGestionarPagoPedidoConGestor() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalStateException.class,
            () -> app.gestionarPagoPedido(null, "1234567890123456", "123",
                new DateTimeSimulado()));
        app.cerrarSesion();
    }

    @Test @Order(142)
    void testGestionarPagoPedidoPedidoNulo() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalArgumentException.class,
            () -> app.gestionarPagoPedido(null, "1234567890123456", "123",
                new DateTimeSimulado()));
        app.cerrarSesion();
    }

    @Test @Order(143)
    void testGestionarPagoPedidoTarjetaNula() {
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();

        app.iniciarSesion("ClienteUno", "Passw0rd!");
        clienteGlobal1.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedido = clienteGlobal1.realizarPedido();

        assertThrows(IllegalArgumentException.class,
            () -> app.gestionarPagoPedido(pedido, null, "123",
                new DateTimeSimulado()));

        // limpieza
        app.cancelarPedido(pedido);
        app.cerrarSesion();
    }

    @Test @Order(144)
    void testGestionarPagoPedidoCorrecto() {
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();

        app.iniciarSesion("ClienteUno", "Passw0rd!");
        clienteGlobal1.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedido = clienteGlobal1.realizarPedido();

        // Tarjeta válida
        app.gestionarPagoPedido(pedido, "1234567890123456", "123",
            new DateTimeSimulado());

        assertEquals(EstadoPedido.PAGADO, pedido.getEstado());
        app.cerrarSesion();
    }

    @Test @Order(145)
    void testGestionarPagoPedidoPedidoAjenoFalla() {
        LineaProductoVenta prod = app.getCatalogo()
            .getProductosNuevos().iterator().next();

        app.iniciarSesion("ClienteDos", "pass2222");
        clienteGlobal2.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedidoDos = clienteGlobal2.realizarPedido();
        app.cerrarSesion();

        // ClienteUno intenta pagar el pedido de ClienteDos
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalStateException.class,
            () -> app.gestionarPagoPedido(pedidoDos, "1234567890123456", "123",
                new DateTimeSimulado()));
        app.cerrarSesion();

        // limpieza
        app.iniciarSesion("ClienteDos", "pass2222");
        app.cancelarPedido(pedidoDos);
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 15 – GESTIONAR PAGO VALIDACIÓN
    // =========================================================================

    @Test @Order(150)
    void testGestionarPagoValidacionSinSesion() {
        assertThrows(IllegalStateException.class,
            () -> app.gestionarPagoValidacion(null, "1234567890123456", "123",
                new DateTimeSimulado()));
    }

    @Test @Order(151)
    void testGestionarPagoValidacionConGestor() {
        app.iniciarSesion("gestor", "123456");
        assertThrows(IllegalStateException.class,
            () -> app.gestionarPagoValidacion(null, "1234567890123456", "123",
                new DateTimeSimulado()));
        app.cerrarSesion();
    }

    @Test @Order(152)
    void testGestionarPagoValidacionSolicitudNula() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertThrows(IllegalArgumentException.class,
            () -> app.gestionarPagoValidacion(null, "1234567890123456", "123",
                new DateTimeSimulado()));
        app.cerrarSesion();
    }

    // =========================================================================
    // BLOQUE 16 – GETTERS DE LISTAS DE USUARIOS
    // =========================================================================

    @Test @Order(160)
    void testGetUsuariosRegistradosContienGestor() {
        List<Usuario> lista = app.getUsuariosRegistrados();
        assertTrue(lista.stream()
            .anyMatch(u -> u.getNombreUsuario().equals("gestor")));
    }

    @Test @Order(161)
    void testGetUsuariosRegistradosEsInmodificable() {
        List<Usuario> lista = app.getUsuariosRegistrados();
        assertThrows(UnsupportedOperationException.class,
            () -> lista.add(null));
    }

    @Test @Order(162)
    void testGetClientesRegistradosSoloDevuelveClientes() {
        List<ClienteRegistrado> clientes = app.getClientesRegistrados();
        // El gestor y los empleados no deben aparecer
        assertTrue(clientes.stream()
            .noneMatch(c -> c.getNombreUsuario().equals("gestor")));
        assertTrue(clientes.stream()
            .noneMatch(c -> c.getNombreUsuario().equals("EmpleadoTest")));
    }

    @Test @Order(163)
    void testGetClientesRegistradosEsInmodificable() {
        List<ClienteRegistrado> lista = app.getClientesRegistrados();
        assertThrows(UnsupportedOperationException.class,
            () -> lista.add(null));
    }

    // =========================================================================
    // BLOQUE 17 – FLUJO COMPLETO DE INTERCAMBIO
    // =========================================================================

    @Test @Order(170)
    void testFlujoCompletoSolicitudIntercambio() {
        // Crear clientes de intercambio
        ClienteRegistrado alfa = app.crearCuenta("AlfaInt", "1010101010", "alfapass", "alfapass");
        ClienteRegistrado beta = app.crearCuenta("BetaInt",  "2020202020", "betapass", "betapass");

        // Alfa añade producto a su cartera
        app.iniciarSesion("AlfaInt", "alfapass");
        ProductoSegundaMano prodAlfa = alfa
            .añadirProductoACarteraDeIntercambio("Muñeco Alfa", "Muñeco de colección", null);
        app.cerrarSesion();

        // Beta añade producto a su cartera
        app.iniciarSesion("BetaInt", "betapass");
        ProductoSegundaMano prodBeta = beta
            .añadirProductoACarteraDeIntercambio("Figura Beta", "Figura edición limitada", null);
        app.cerrarSesion();

        // Empleado valida los productos
        app.iniciarSesion("gestor", "123456");
        Empleado emp = app.añadirEmpleado("EmpInterc", "3030303030", "123456");
        emp.añadirPermiso(Permiso.VALIDACIONES);
        emp.añadirPermiso(Permiso.INTERCAMBIOS);
        app.cerrarSesion();

        app.iniciarSesion("EmpInterc", "123456");
        emp.validarProducto(prodAlfa.getSolicitudValidacion(),2, 10.0, EstadoConservacion.MUY_BUENO);
        emp.validarProducto(prodBeta.getSolicitudValidacion(),1, 8.0,  EstadoConservacion.USO_LIGERO);
        app.cerrarSesion();

        // Alfa paga la validación
        app.iniciarSesion("AlfaInt", "alfapass");
        alfa.pagarValidacion(prodAlfa.getSolicitudValidacion(),
            "1234567890123456", "123", new DateTimeSimulado());
        app.cerrarSesion();

        // Beta paga la validación
        app.iniciarSesion("BetaInt", "betapass");
        beta.pagarValidacion(prodBeta.getSolicitudValidacion(),
            "1234567890123456", "123", new DateTimeSimulado());
        app.cerrarSesion();

        // Alfa propone oferta a Beta
        app.iniciarSesion("AlfaInt", "alfapass");
        Set<ProductoSegundaMano> ofertados   = new HashSet<>(List.of(prodAlfa));
        Set<ProductoSegundaMano> solicitados = new HashSet<>(List.of(prodBeta));
        alfa.realizarOferta(ofertados, solicitados, beta);
        app.cerrarSesion();

        // Beta acepta la oferta
        app.iniciarSesion("BetaInt", "betapass");
        List<Oferta> ofertasBeta = beta.getOfertasRecibidas();
        assertFalse(ofertasBeta.isEmpty(), "Beta debe haber recibido la oferta");
        beta.aceptarOferta(ofertasBeta.get(0));
        app.cerrarSesion();

        // Verificamos que se creó la SolicitudIntercambio
        assertFalse(GestorSolicitudes.getInstancia().getIntercambios().isEmpty(),
            "Debe existir al menos un intercambio registrado");
    }

    // =========================================================================
    // BLOQUE 18 – CRÉDITOS DE RECOMENDACIÓN 
    // =========================================================================

    @Test @Order(180)
    void testRecomendacionDevuelveSetNoNull() {
        app.iniciarSesion("ClienteUno", "Passw0rd!");
        assertNotNull(app.getConfiguracionRecomendacion().getRecomendacion());
        app.cerrarSesion();
    }

    @Test @Order(181)
    void testRecomendacionVaciaParaGestorLogueado() {
        // El sistema solo recomienda a ClienteRegistrado
        app.iniciarSesion("gestor", "123456");
        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        assertTrue(rec.isEmpty(),
            "El gestor no es un cliente; no debe recibir recomendaciones");
        app.cerrarSesion();
    }
}