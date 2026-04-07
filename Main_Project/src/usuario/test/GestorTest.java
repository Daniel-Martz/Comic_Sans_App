package usuario.test;

import usuario.*;
import producto.*;
import aplicacion.*;
import solicitud.*;
import tiempo.DateTimeSimulado;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// ============================================================
//  GESTOR TEST
// ============================================================
class GestorTest {

    // ---------------------------------------------------------------
    // Reseteo de singletons para aislar cada test
    // ---------------------------------------------------------------
    @BeforeEach
    void resetearSingletons() throws Exception {
        resetField(Aplicacion.class,        "instancia");
        resetField(Catalogo.class,          "instancia");
        resetField(GestorSolicitudes.class, "instancia");
        resetField(SistemaEstadisticas.class,"instancia");
        resetField(SistemaPago.class,       "instancia");
        resetField(ConfiguracionRecomendacion.class, "instancia");
    }

    private void resetField(Class<?> clazz, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, null);
    }

    private Gestor loginGestor() {
        Aplicacion app = Aplicacion.getInstancia();
        app.iniciarSesion("gestor", "123456");
        return (Gestor) app.getUsuarioActual();
    }

    // ---------------------------------------------------------------
    // crearEmpleado
    // ---------------------------------------------------------------

    @Test
    void testCrearEmpleado() {
        Aplicacion app = Aplicacion.getInstancia();
        app.iniciarSesion("gestor", "123456");
        Gestor g = (Gestor) app.getUsuarioActual();
        g.crearEmpleado("Juan", "011222233A");
        app.cerrarSesion();
        app.iniciarSesion("Juan", "123456");
        assertTrue(app.getUsuarioActual() instanceof Empleado);
        app.cerrarSesion();
    }

    @Test
    void testCrearEmpleadoAparecenEnListaDeUsuarios() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        int antes = app.getUsuariosRegistrados().size();
        g.crearEmpleado("Maria", "123456789A");
        app.cerrarSesion();
        assertEquals(antes + 1, app.getUsuariosRegistrados().size());
    }

    @Test
    void testCrearDosEmpleadosDistintos() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        Empleado e1 = g.crearEmpleado("Emp1", "111111111A");
        Empleado e2 = g.crearEmpleado("Emp2", "222222222B");
        app.cerrarSesion();
        assertNotSame(e1, e2);
        assertTrue(app.getUsuariosRegistrados().contains(e1));
        assertTrue(app.getUsuariosRegistrados().contains(e2));
    }

    @Test
    void testCrearEmpleadoConNombreDuplicadoLanzaExcepcion() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        g.crearEmpleado("Dup", "333333333C");
        assertThrows(IllegalStateException.class,
                () -> g.crearEmpleado("Dup", "444444444D"),
                "Crear un empleado con nombre duplicado debe lanzar excepción");
        app.cerrarSesion();
    }

    @Test
    void testCrearEmpleadoTieneContraseñaPorDefecto() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        g.crearEmpleado("Pedro", "555555555E");
        app.cerrarSesion();
        // La contraseña por defecto que usa Gestor.crearEmpleado es "123456"
        assertDoesNotThrow(() -> app.iniciarSesion("Pedro", "123456"));
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // eliminarEmpleado
    // ---------------------------------------------------------------

    @Test
    void testEliminarEmpleado() {
        Aplicacion app = Aplicacion.getInstancia();
        app.iniciarSesion("gestor", "123456");
        Gestor g = (Gestor) app.getUsuarioActual();
        Empleado e = g.crearEmpleado("Paco", "911222233A");
        int usuariosAntes = app.getUsuariosRegistrados().size();
        g.eliminarEmpleado(e);
        assertEquals(usuariosAntes - 1, app.getUsuariosRegistrados().size());
        assertFalse(app.getUsuariosRegistrados().contains(e));
        app.cerrarSesion();
    }

    @Test
    void testEliminarEmpleadoNuloNoLanzaExcepcion() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        assertDoesNotThrow(() -> g.eliminarEmpleado(null));
        app.cerrarSesion();
    }

    @Test
    void testEliminarEmpleadoNoRegistradoNoModificaLista() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        int antes = app.getUsuariosRegistrados().size();
        Empleado fantasma = new Empleado("Fantasma", "000000000Z", "abcd");
        g.eliminarEmpleado(fantasma);
        assertEquals(antes, app.getUsuariosRegistrados().size());
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // añadirPermiso / eliminarPermiso
    // ---------------------------------------------------------------

    @Test
    void testAñadirPermisoValidaciones() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        Empleado e = g.crearEmpleado("Emp3", "666666666F");
        g.añadirPermiso(e, Permiso.VALIDACIONES);
        app.cerrarSesion();
        // El empleado ya puede validar (no lanzará IllegalStateException)
        ProductoSegundaMano psm = new ProductoSegundaMano("P", "d", null,
                new ClienteRegistrado("cli1", "100000000A", "1234"));
        assertDoesNotThrow(() ->
                e.validarProducto(psm.getSolicitudValidacion(), 5.0, 20.0, EstadoConservacion.MUY_BUENO));
    }

    @Test
    void testAñadirPermisoIntercambios() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        Empleado e = g.crearEmpleado("Emp4", "777777777G");
        g.añadirPermiso(e, Permiso.INTERCAMBIOS);
        app.cerrarSesion();
        // Con permiso no lanza excepción al aprobar (aunque falle por códigos)
        SolicitudIntercambio si = new SolicitudIntercambio("a", "b", "Tienda",
                new Oferta(new DateTimeSimulado(),
                        new ClienteRegistrado("x", "200000000B", "1234"),
                        new ClienteRegistrado("y", "300000000C", "1234"),
                        new HashSet<>(), new HashSet<>()));
        assertDoesNotThrow(() -> e.aprobarIntercambio(si, "a", "b"));
    }

    @Test
    void testAñadirPermisoPedidos() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        Empleado e = g.crearEmpleado("Emp5", "888888888H");
        g.añadirPermiso(e, Permiso.PEDIDOS);
        app.cerrarSesion();
        ClienteRegistrado c = new ClienteRegistrado("c2", "400000000D", "1234");
        LineaProductoVenta p = new LineaProductoVenta("Prod", "d", new File("f.png"), 100, 10.0);
        c.añadirProductoACarrito(p, 1);
        SolicitudPedido pedido = c.realizarPedido();
        c.pagarPedido(pedido, "0123456789012345", "123", new DateTimeSimulado());
        assertDoesNotThrow(() ->
                e.actualizarEstadoPedido(pedido, EstadoPedido.LISTO_PARA_RECOGER));
    }

    @Test
    void testEliminarPermisoImpideAccion() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        Empleado e = g.crearEmpleado("Emp6", "999999999I");
        g.añadirPermiso(e, Permiso.VALIDACIONES);
        g.eliminarPermiso(e, Permiso.VALIDACIONES);
        app.cerrarSesion();
        ProductoSegundaMano psm = new ProductoSegundaMano("P2", "d", null,
                new ClienteRegistrado("cli2", "500000000E", "1234"));
        assertThrows(IllegalStateException.class,
                () -> e.validarProducto(psm.getSolicitudValidacion(), 5.0, 20.0, EstadoConservacion.PERFECTO),
                "Sin permiso debe lanzar IllegalStateException");
    }

    // ---------------------------------------------------------------
    // configurarImportancia / configurarUnidades
    // ---------------------------------------------------------------

    @Test
    void testConfigurarImportanciaValida() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        assertDoesNotThrow(() -> g.configurarImportancia(1, 2, 3));
        app.cerrarSesion();
    }

    @Test
    void testConfigurarImportanciaValoresNegativosLanzaExcepcion() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        assertThrows(IllegalArgumentException.class,
                () -> g.configurarImportancia(-1, 0, 0));
        app.cerrarSesion();
    }

    @Test
    void testConfigurarUnidadesValida() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        assertDoesNotThrow(() -> g.configurarUnidadesRecomendadas(5));
        app.cerrarSesion();
    }

    @Test
    void testConfigurarUnidadesCeroLanzaExcepcion() {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        assertThrows(IllegalArgumentException.class,
                () -> g.configurarUnidadesRecomendadas(0));
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // estadísticas (delegan en SistemaEstadisticas — sólo comprobamos
    // que el fichero se crea y no lanza excepción)
    // ---------------------------------------------------------------

    @Test
    void testEstadisticasRecaudacionMensualGeneraFichero() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        File f = File.createTempFile("recMensual", ".txt");
        f.deleteOnExit();
        g.estadisticasRecaudacion(
                new DateTimeSimulado(1, 1, 1, 0, 0, 0),
                new DateTimeSimulado(1, 12, 30, 23, 59, 59),
                true, f);
        assertTrue(f.exists() && f.length() > 0);
        app.cerrarSesion();
    }

    @Test
    void testEstadisticasRecaudacionAmbitoGeneraFichero() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        File f = File.createTempFile("recAmbito", ".txt");
        f.deleteOnExit();
        g.estadisticasRecaudacion(
                new DateTimeSimulado(1, 1, 1, 0, 0, 0),
                new DateTimeSimulado(1, 12, 30, 23, 59, 59),
                false, f);
        assertTrue(f.exists() && f.length() > 0);
        app.cerrarSesion();
    }

    @Test
    void testEstadisticasVentasProductosGeneraFichero() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        File f = File.createTempFile("ventas", ".txt");
        f.deleteOnExit();
        g.estadisticasVentasProductos(
                new DateTimeSimulado(1, 1, 1, 0, 0, 0),
                new DateTimeSimulado(1, 12, 30, 23, 59, 59),
                true, f);
        assertTrue(f.exists() && f.length() > 0);
        app.cerrarSesion();
    }

    @Test
    void testEstadisticasGastoClientesGeneraFichero() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        Gestor g = loginGestor();
        File f = File.createTempFile("clientes", ".txt");
        f.deleteOnExit();
        g.estadisticasGastoClientes(
                new DateTimeSimulado(1, 1, 1, 0, 0, 0),
                new DateTimeSimulado(1, 12, 30, 23, 59, 59),
                f);
        assertTrue(f.exists() && f.length() > 0);
        app.cerrarSesion();
    }
}
