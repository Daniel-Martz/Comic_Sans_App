package notificacion.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import notificacion.*;
import producto.*;
import solicitud.*;
import tiempo.DateTimeSimulado;
import usuario.*;
import aplicacion.Aplicacion;
import aplicacion.GestorSolicitudes;

/**
 * Tests del sistema de notificaciones.
 *
 * Cubre:
 *  - NotificacionPedido
 *  - NotificacionProducto
 *  - NotificacionIntercambio
 *  - NotificacionOferta
 *  - NotificacionValidacion
 *  - NotificacionEmpleado
 *  - Gestión de notificaciones en ClienteRegistrado (añadir, eliminar por objeto, eliminar por id)
 *  - Configuración de notificaciones deseadas
 */
class NotificacionTest {

    // ---------------------------------------------------------------
    // Helpers compartidos
    // ---------------------------------------------------------------

    private DateTimeSimulado ahora() {
        return new DateTimeSimulado();
    }

    private ClienteRegistrado nuevoCliente(String user, String dni, String pass) {
        return new ClienteRegistrado(user, dni, pass);
    }

    // ===============================================================
    // 1. NotificacionPedido
    // ===============================================================

    @Test
    void testNotificacionPedido_getMensaje() {
        SolicitudPedido pedido = new SolicitudPedido(
                nuevoCliente("alice", "00000000A0", "pass1234"),
                new java.util.HashMap<>());

        NotificacionPedido n = new NotificacionPedido("Pedido listo", ahora(), pedido);

        assertEquals("Pedido listo", n.getMensaje());
    }

    @Test
    void testNotificacionPedido_getPedido() {
        ClienteRegistrado c = nuevoCliente("bob", "11111111B0", "pass1234");
        SolicitudPedido pedido = new SolicitudPedido(c, new java.util.HashMap<>());

        NotificacionPedido n = new NotificacionPedido("Pedido creado", ahora(), pedido);

        assertSame(pedido, n.getPedido());
    }

    @Test
    void testNotificacionPedido_setPedido() {
        ClienteRegistrado c = nuevoCliente("carol", "22222222C0", "pass1234");
        SolicitudPedido p1 = new SolicitudPedido(c, new java.util.HashMap<>());
        SolicitudPedido p2 = new SolicitudPedido(c, new java.util.HashMap<>());

        NotificacionPedido n = new NotificacionPedido("msg", ahora(), p1);
        n.setPedido(p2);

        assertSame(p2, n.getPedido());
    }

    @Test
    void testNotificacionPedido_idAutoincremento() {
        NotificacionPedido n1 = new NotificacionPedido("a", ahora(),
                new SolicitudPedido(nuevoCliente("u1", "33333333D0", "pass1234"), new java.util.HashMap<>()));
        NotificacionPedido n2 = new NotificacionPedido("b", ahora(),
                new SolicitudPedido(nuevoCliente("u2", "44444444E0", "pass1234"), new java.util.HashMap<>()));

        assertTrue(n2.getId() > n1.getId(),
                "El id de la segunda notificación debe ser mayor que el de la primera");
    }

    // ===============================================================
    // 2. NotificacionProducto
    // ===============================================================

    @Test
    void testNotificacionProducto_addYGetProductos() {
        LineaProductoVenta prod = new LineaProductoVenta(
                "SpidermanVol1", "Desc", new File("/img.png"), 10, 9.99);

        NotificacionProducto n = new NotificacionProducto("Nuevo descuento", ahora());
        n.addProducto(prod);

        assertTrue(n.getProductos().contains(prod));
    }

    @Test
    void testNotificacionProducto_removeProducto() {
        LineaProductoVenta prod = new LineaProductoVenta(
                "Batman", "Desc", new File("/img.png"), 5, 7.99);

        NotificacionProducto n = new NotificacionProducto("Oferta", ahora());
        n.addProducto(prod);
        n.removeProducto(prod);

        assertFalse(n.getProductos().contains(prod));
    }

    @Test
    void testNotificacionProducto_setProductos() {
        LineaProductoVenta p1 = new LineaProductoVenta("A", "d", new File("/a.png"), 1, 1.0);
        LineaProductoVenta p2 = new LineaProductoVenta("B", "d", new File("/b.png"), 1, 2.0);

        Set<LineaProductoVenta> nuevosProductos = new HashSet<>();
        nuevosProductos.add(p1);
        nuevosProductos.add(p2);

        NotificacionProducto n = new NotificacionProducto("msg", ahora());
        n.setProductos(nuevosProductos);

        assertEquals(2, n.getProductos().size());
        assertTrue(n.getProductos().contains(p1));
        assertTrue(n.getProductos().contains(p2));
    }

    @Test
    void testNotificacionProducto_sinProductosInicialmente() {
        NotificacionProducto n = new NotificacionProducto("msg", ahora());
        assertTrue(n.getProductos().isEmpty());
    }

    // ===============================================================
    // 3. NotificacionIntercambio
    // ===============================================================

    @Test
    void testNotificacionIntercambio_getCodigo() {
        DetallesIntercambio detalles = new DetallesIntercambio(ahora(), "Tienda principal");
        NotificacionIntercambio n = new NotificacionIntercambio(
                "Intercambio aprobado", ahora(), "ABC123", detalles);

        assertEquals("ABC123", n.getCodigoIntercambio());
    }

    @Test
    void testNotificacionIntercambio_getDetalles() {
        DetallesIntercambio detalles = new DetallesIntercambio(ahora(), "Tienda principal");
        NotificacionIntercambio n = new NotificacionIntercambio(
                "Intercambio aprobado", ahora(), "XYZ999", detalles);

        assertSame(detalles, n.getDetallesIntercambio());
    }

    @Test
    void testNotificacionIntercambio_mensajeYHora() {
        DetallesIntercambio detalles = new DetallesIntercambio(ahora(), "Tienda Norte");
        DateTimeSimulado hora = ahora();
        NotificacionIntercambio n = new NotificacionIntercambio("msg intercambio", hora, "COD01", detalles);

        assertEquals("msg intercambio", n.getMensaje());
        assertSame(hora, n.getHoraEnvio());
    }

    // ===============================================================
    // 4. NotificacionOferta
    // ===============================================================

    @Test
    void testNotificacionOferta_getOferta() {
        ClienteRegistrado ofertante   = nuevoCliente("ofertante",   "55555555F0", "pass1234");
        ClienteRegistrado destinatario = nuevoCliente("destinatario", "66666666G0", "pass1234");

        Oferta oferta = new Oferta(ahora(), destinatario, ofertante,
                new HashSet<>(), new HashSet<>());

        NotificacionOferta n = new NotificacionOferta("Nueva oferta recibida", ahora(), oferta);

        assertSame(oferta, n.getOferta());
    }

    @Test
    void testNotificacionOferta_mensaje() {
        ClienteRegistrado ofertante    = nuevoCliente("of2", "77777777H0", "pass1234");
        ClienteRegistrado destinatario = nuevoCliente("dest2", "88888888I0", "pass1234");

        Oferta oferta = new Oferta(ahora(), destinatario, ofertante, new HashSet<>(), new HashSet<>());
        NotificacionOferta n = new NotificacionOferta("Oferta de of2", ahora(), oferta);

        assertEquals("Oferta de of2", n.getMensaje());
    }

    // ===============================================================
    // 5. NotificacionValidacion
    // ===============================================================

    @Test
    void testNotificacionValidacion_getSolicitud() {
    	ClienteRegistrado cliente    = nuevoCliente("of2", "77777777H0", "pass1234");
        ProductoSegundaMano prod = new ProductoSegundaMano(
                "Figura Goku", "Figura de coleccionista", new File("/goku.jpg"), cliente);
        SolicitudValidacion sol = new SolicitudValidacion(prod, cliente);

        NotificacionValidacion n = new NotificacionValidacion(
                "Validación lista", ahora(), sol);

        assertSame(sol, n.getSolicitudProductoSegundaMano());
    }

    @Test
    void testNotificacionValidacion_setSolicitud() {
    	ClienteRegistrado cliente    = nuevoCliente("of2", "77777777H0", "pass1234");
        ProductoSegundaMano p1 = new ProductoSegundaMano("P1", "d1", new File("/p1.jpg"), cliente);
        ProductoSegundaMano p2 = new ProductoSegundaMano("P2", "d2", new File("/p2.jpg"),cliente);
        SolicitudValidacion s1 = new SolicitudValidacion(p1, cliente);
        SolicitudValidacion s2 = new SolicitudValidacion(p2,cliente);

        NotificacionValidacion n = new NotificacionValidacion("msg", ahora(), s1);
        n.setSolicitudProductoSegundaMano(s2);

        assertSame(s2, n.getSolicitudProductoSegundaMano());
    }

    // ===============================================================
    // 6. NotificacionEmpleado
    // ===============================================================

    @Test
    void testNotificacionEmpleado_addSolicitud() {
        SolicitudPedido pedido = new SolicitudPedido(
                nuevoCliente("emp1", "99999999J0", "pass1234"), new java.util.HashMap<>());

        NotificacionEmpleado n = new NotificacionEmpleado("Nuevo pedido", ahora());
        n.addSolicitud(pedido);

        assertTrue(n.getSolicitudes().contains(pedido));
    }

    @Test
    void testNotificacionEmpleado_removeSolicitud() {
        SolicitudPedido pedido = new SolicitudPedido(
                nuevoCliente("emp2", "10101010K0", "pass1234"), new java.util.HashMap<>());

        NotificacionEmpleado n = new NotificacionEmpleado("Nuevo pedido", ahora());
        n.addSolicitud(pedido);
        n.removeSolicitud(pedido);

        assertFalse(n.getSolicitudes().contains(pedido));
    }

    @Test
    void testNotificacionEmpleado_sinSolicitudesInicialmente() {
        NotificacionEmpleado n = new NotificacionEmpleado("msg", ahora());
        assertTrue(n.getSolicitudes().isEmpty());
    }

    @Test
    void testNotificacionEmpleado_variasSolicitudes() {
        SolicitudPedido p1 = new SolicitudPedido(
                nuevoCliente("e3", "20202020L0", "pass1234"), new java.util.HashMap<>());
        SolicitudPedido p2 = new SolicitudPedido(
                nuevoCliente("e4", "30303030M0", "pass1234"), new java.util.HashMap<>());

        NotificacionEmpleado n = new NotificacionEmpleado("msg", ahora());
        n.addSolicitud(p1);
        n.addSolicitud(p2);

        assertEquals(2, n.getSolicitudes().size());
    }

    // ===============================================================
    // 7. Gestión de notificaciones en ClienteRegistrado
    // ===============================================================

    @Test
    void testClienteRegistrado_añadirNotificacion() {
        ClienteRegistrado cliente = nuevoCliente("cli1", "40404040N0", "pass1234");
        NotificacionPedido n = new NotificacionPedido("msg", ahora(),
                new SolicitudPedido(cliente, new java.util.HashMap<>()));

        cliente.anadirNotificacion(n);

        assertTrue(cliente.getNotificaciones().contains(n));
    }

    @Test
    void testClienteRegistrado_eliminarNotificacion() {
        ClienteRegistrado cliente = nuevoCliente("cli2", "50505050O0", "pass1234");
        NotificacionPedido n = new NotificacionPedido("msg", ahora(),
                new SolicitudPedido(cliente, new java.util.HashMap<>()));

        cliente.anadirNotificacion(n);
        cliente.eliminarNotificacion(n);

        assertFalse(cliente.getNotificaciones().contains(n));
    }

    @Test
    void testClienteRegistrado_eliminarNotificacionPorId() {
        ClienteRegistrado cliente = nuevoCliente("cli3", "60606060P0", "pass1234");
        NotificacionPedido n = new NotificacionPedido("msg", ahora(),
                new SolicitudPedido(cliente, new java.util.HashMap<>()));

        cliente.anadirNotificacion(n);
        int id = n.getId();
        cliente.eliminarNotificacionPorId(id);

        assertFalse(cliente.getNotificaciones().contains(n));
    }

    @Test
    void testClienteRegistrado_eliminarNotificacionPorIdInexistente_sinError() {
        ClienteRegistrado cliente = nuevoCliente("cli4", "70707070Q0", "pass1234");
        // Eliminar un id que no existe no debe lanzar excepción
        assertDoesNotThrow(() -> cliente.eliminarNotificacionPorId(Integer.MAX_VALUE));
    }

    @Test
    void testClienteRegistrado_variosNotificaciones_ordenPreservado() {
        ClienteRegistrado cliente = nuevoCliente("cli5", "80808080R0", "pass1234");

        NotificacionPedido n1 = new NotificacionPedido("primera", ahora(),
                new SolicitudPedido(cliente, new java.util.HashMap<>()));
        NotificacionPedido n2 = new NotificacionPedido("segunda", ahora(),
                new SolicitudPedido(cliente, new java.util.HashMap<>()));

        cliente.anadirNotificacion(n1);
        cliente.anadirNotificacion(n2);

        List<NotificacionCliente> lista = cliente.getNotificaciones();
        assertEquals(2, lista.size());
        assertSame(n1, lista.get(0));
        assertSame(n2, lista.get(1));
    }

    // ===============================================================
    // 8. Configuración de notificaciones deseadas
    // ===============================================================

    @Test
    void testConfiguracionNotificaciones_activarDescuentos() {
        ClienteRegistrado cliente = nuevoCliente("cfg1", "90909090S0", "pass1234");
        cliente.getConfiguracionNotificacionClientees().add(NotificacionDeseada.DESCUENTOS);

        assertTrue(cliente.getConfiguracionNotificacionClientees()
                .contains(NotificacionDeseada.DESCUENTOS));
    }

    @Test
    void testConfiguracionNotificaciones_desactivarDescuentos() {
        ClienteRegistrado cliente = nuevoCliente("cfg2", "01010101T0", "pass1234");
        cliente.getConfiguracionNotificacionClientees().add(NotificacionDeseada.DESCUENTOS);
        cliente.getConfiguracionNotificacionClientees().remove(NotificacionDeseada.DESCUENTOS);

        assertFalse(cliente.getConfiguracionNotificacionClientees()
                .contains(NotificacionDeseada.DESCUENTOS));
    }

    @Test
    void testConfiguracionNotificaciones_activarRecomendaciones() {
        ClienteRegistrado cliente = nuevoCliente("cfg3", "12121212U0", "pass1234");
        cliente.getConfiguracionNotificacionClientees().add(NotificacionDeseada.RECOMENDACIONES);

        assertTrue(cliente.getConfiguracionNotificacionClientees()
                .contains(NotificacionDeseada.RECOMENDACIONES));
    }

    @Test
    void testConfiguracionNotificaciones_inicialmenteVacia() {
        ClienteRegistrado cliente = nuevoCliente("cfg4", "23232323V0", "pass1234");
        assertTrue(cliente.getConfiguracionNotificacionClientees().isEmpty());
    }

    // ===============================================================
    // 9. setMensaje / setHoraEnvio (herencia de Notificacion)
    // ===============================================================

    @Test
    void testSetMensaje() {
        NotificacionProducto n = new NotificacionProducto("original", ahora());
        n.setMensaje("modificado");
        assertEquals("modificado", n.getMensaje());
    }

    @Test
    void testSetHoraEnvio() {
        NotificacionProducto n = new NotificacionProducto("msg", ahora());
        DateTimeSimulado nuevaHora = ahora();
        n.setHoraEnvio(nuevaHora);
        assertSame(nuevaHora, n.getHoraEnvio());
    }

    // ===============================================================
    // 10. toString no lanza excepción
    // ===============================================================

    @Test
    void testToString_notificacionPedido() {
        SolicitudPedido pedido = new SolicitudPedido(
                nuevoCliente("ts1", "34343434W0", "pass1234"), new java.util.HashMap<>());
        NotificacionPedido n = new NotificacionPedido("msg", ahora(), pedido);
        assertDoesNotThrow(n::toString);
    }

    @Test
    void testToString_notificacionEmpleado() {
        NotificacionEmpleado n = new NotificacionEmpleado("msg", ahora());
        assertDoesNotThrow(n::toString);
    }

    @Test
    void testToString_notificacionIntercambio() {
        DetallesIntercambio d = new DetallesIntercambio(ahora(), "Lugar");
        NotificacionIntercambio n = new NotificacionIntercambio("msg", ahora(), "cod", d);
        assertDoesNotThrow(n::toString);
    }

    @Test
    void testToString_notificacionOferta() {
        ClienteRegistrado of  = nuevoCliente("of3", "45454545X0", "pass1234");
        ClienteRegistrado dest = nuevoCliente("ds3", "56565656Y0", "pass1234");
        Oferta o = new Oferta(ahora(), dest, of, new HashSet<>(), new HashSet<>());
        NotificacionOferta n = new NotificacionOferta("msg", ahora(), o);
        assertDoesNotThrow(n::toString);
    }

    @Test
    void testToString_notificacionValidacion() {
    	ClienteRegistrado cliente    = nuevoCliente("of2", "77777777H0", "pass1234");
        ProductoSegundaMano p = new ProductoSegundaMano("Prod", "desc", new File("/f.jpg"), cliente);
        SolicitudValidacion sv = new SolicitudValidacion(p,cliente);
        NotificacionValidacion n = new NotificacionValidacion("msg", ahora(), sv);
        assertDoesNotThrow(n::toString);
    }
}