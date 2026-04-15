package modelo.aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.aplicacion.GestorSolicitudes;
import modelo.aplicacion.SistemaEstadisticas;
import modelo.aplicacion.SistemaPago;
import modelo.producto.*;
import modelo.solicitud.*;
import modelo.tiempo.DateTimeSimulado;
import modelo.tiempo.TiempoSimulado;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Empleado;
import modelo.usuario.Permiso;

/**
 * Tests exhaustivos para la clase SistemaEstadisticas.
 *
 * Cubre:
 *  - Patrón singleton.
 *  - añadirPago: pago nulo lanza excepción, pagos válidos se acumulan.
 *  - obtenerRecaudacionMensual: parámetros nulos, periodo invertido,
 *    sin pagos, un pago en rango, varios pagos en rango, pago fuera de rango,
 *    contenido del fichero.
 *  - obtenerRecaudacionAmbito: mismas condiciones de límite + distinción
 *    venta vs validación.
 *  - obtenerVentasProductos: parámetros nulos, periodo invertido,
 *    sin pedidos pagados, con pedidos pagados ordenado por id y por recaudación.
 *  - obtenerGastoClientes: parámetros nulos, periodo invertido,
 *    sin clientes, con clientes y gastos mixtos.
 */
class SistemaEstadisticasTest {

    @TempDir
    File tempDir;

    // ---------------------------------------------------------------
    // Helpers de reseteo de singletons
    // ---------------------------------------------------------------

    @BeforeEach
    void resetearSingletons() throws Exception {
        resetField(SistemaEstadisticas.class, "instancia");
        resetField(Aplicacion.class,          "instancia");
        resetField(Catalogo.class,            "instancia");
        resetField(GestorSolicitudes.class,   "instancia");
        resetField(SistemaPago.class,         "instancia");
    }

    private void resetField(Class<?> clazz, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, null);
    }

    /** Lee el contenido completo de un fichero como String. */
    private String leerFichero(File f) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        }
        return sb.toString();
    }

    // Fechas de conveniencia para los tests
    private DateTimeSimulado fechaInicio() {
        return new DateTimeSimulado(1, 1, 1, 0, 0, 0);
    }

    private DateTimeSimulado fechaFin() {
        return new DateTimeSimulado(1, 12, 30, 23, 59, 59);
    }

    // ---------------------------------------------------------------
    // Singleton
    // ---------------------------------------------------------------

    @Test
    void testGetInstanciaDevuelveInstanciaUnica() {
        SistemaEstadisticas s1 = SistemaEstadisticas.getInstancia();
        SistemaEstadisticas s2 = SistemaEstadisticas.getInstancia();
        assertSame(s1, s2);
    }

    // ---------------------------------------------------------------
    // añadirPago
    // ---------------------------------------------------------------

    @Test
    void testAñadirPagoNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        assertThrows(IllegalArgumentException.class, () -> se.añadirPago(null));
    }

    @Test
    void testAñadirPagoValidoNoLanzaExcepcion() {
        Aplicacion.getInstancia(); // inicializa
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        // Creamos un pago genérico con objetoPagado == null (permitido por el constructor)
        Pago pago = new Pago(new DateTimeSimulado(), 50.0, null);
        assertDoesNotThrow(() -> se.añadirPago(pago));
    }

    // ---------------------------------------------------------------
    // obtenerRecaudacionMensual — validación de parámetros
    // ---------------------------------------------------------------

    @Test
    void testRecaudacionMensualPeriodoInicioNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionMensual(null, fechaFin(), f));
    }

    @Test
    void testRecaudacionMensualPeriodoFinNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionMensual(fechaInicio(), null, f));
    }

    @Test
    void testRecaudacionMensualFicheroNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), null));
    }

    @Test
    void testRecaudacionMensualPeriodoInvertidoLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        // inicio posterior a fin
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionMensual(fechaFin(), fechaInicio(), f));
    }

    @Test
    void testRecaudacionMensualSinPagosGeneraFichero() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), f);
        assertTrue(f.exists(), "Se debe crear el fichero aunque no haya pagos");
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("TOTAL"),
                "El fichero debe contener la línea TOTAL");
    }

    @Test
    void testRecaudacionMensualTotalCeroSinPagos() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("0,00 €") || contenido.contains("0.00 €"),
                "Con cero pagos el total debe ser 0,00 €");
    }

    @Test
    void testRecaudacionMensualConUnPagoEnRango() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();

        Pago pago = new Pago(new DateTimeSimulado(), 75.50, null);
        se.añadirPago(pago);

        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("75,50 €") || contenido.contains("75.50 €"),
                "El informe debe reflejar el importe del pago");
    }

    @Test
    void testRecaudacionMensualPagoFueraDeRangoNoSeSuma() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();

        // Pago con fecha muy posterior (año 99)
        DateTimeSimulado fechaFuera = new DateTimeSimulado(99, 1, 1, 0, 0, 0);
        Pago pago = new Pago(fechaFuera, 999.99, null);
        se.añadirPago(pago);

        File f = new File(tempDir, "out.txt");
        // Rango limitado al año 1
        se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertFalse(contenido.contains("999,99 €") || contenido.contains("999.99 €"),
                "Un pago fuera de rango no debe aparecer en el total");
    }

    @Test
    void testRecaudacionMensualVariosPagosEnRango() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();

        se.añadirPago(new Pago(new DateTimeSimulado(), 10.0, null));
        se.añadirPago(new Pago(new DateTimeSimulado(), 20.0, null));
        se.añadirPago(new Pago(new DateTimeSimulado(), 30.0, null));

        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        // Total = 60
        assertTrue(contenido.contains("60,00 €") || contenido.contains("60.00 €"),
                "El total de tres pagos debe ser 60,00 €");
    }

    @Test
    void testRecaudacionMensualCabeceraTieneNombreInforme() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionMensual(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("RECAUDACION MENSUAL"),
                "La cabecera debe mencionar 'RECAUDACION MENSUAL'");
    }

    // ---------------------------------------------------------------
    // obtenerRecaudacionAmbito — validación de parámetros
    // ---------------------------------------------------------------

    @Test
    void testRecaudacionAmbitoPeriodoInicioNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionAmbito(null, fechaFin(), f));
    }

    @Test
    void testRecaudacionAmbitoFicheroNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionAmbito(fechaInicio(), fechaFin(), null));
    }

    @Test
    void testRecaudacionAmbitoPeriodoInvertidoLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerRecaudacionAmbito(fechaFin(), fechaInicio(), f));
    }

    @Test
    void testRecaudacionAmbitoSinPagosGeneraFicheroConCero() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionAmbito(fechaInicio(), fechaFin(), f);
        assertTrue(f.exists());
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("TOTAL"));
    }

    @Test
    void testRecaudacionAmbitoCabecera() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerRecaudacionAmbito(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("AMBITO"),
                "La cabecera debe contener la palabra AMBITO");
    }

    @Test
    void testRecaudacionAmbitoDistingueVentaYValidacion() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();

        // Pago de pedido
        ClienteRegistrado cliente = app.crearCuenta("AA", "111111111A", "1234");
        LineaProductoVenta p = new LineaProductoVenta("Prod", "d", new File("f.png"), 100, 20.0);
        cliente.añadirProductoACarrito(p, 1);
        SolicitudPedido pedido = cliente.realizarPedido();
        Pago pagoPedido = new Pago(new DateTimeSimulado(), 20.0, pedido);
        se.añadirPago(pagoPedido);

        // Pago de validación
        ProductoSegundaMano psm = cliente.añadirProductoACarteraDeIntercambio("Peluche", "d", null);
        Empleado emp = new Empleado("emp1", "222222222B", "123456");
        emp.añadirPermiso(Permiso.VALIDACIONES);
        emp.validarProducto(psm.getSolicitudValidacion(), 5.0, 30.0, EstadoConservacion.MUY_BUENO);
        SolicitudValidacion sv = psm.getSolicitudValidacion();
        Pago pagoVal = new Pago(new DateTimeSimulado(), 5.0, sv);
        se.añadirPago(pagoVal);

        File f = new File(tempDir, "ambito.txt");
        se.obtenerRecaudacionAmbito(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);

        assertTrue(contenido.contains("Venta de productos"), "Debe aparecer el ámbito 'Venta de productos'");
        assertTrue(contenido.contains("Validaciones"),       "Debe aparecer el ámbito 'Validaciones'");
    }

    // ---------------------------------------------------------------
    // obtenerVentasProductos — validación de parámetros
    // ---------------------------------------------------------------

    @Test
    void testVentasProductosPeriodoInicioNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerVentasProductos(null, fechaFin(), false, f));
    }

    @Test
    void testVentasProductosFicheroNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerVentasProductos(fechaInicio(), fechaFin(), false, null));
    }

    @Test
    void testVentasProductosPeriodoInvertidoLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerVentasProductos(fechaFin(), fechaInicio(), false, f));
    }

    @Test
    void testVentasProductosSinPagosGeneraFicheroConCero() throws IOException {
        Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerVentasProductos(fechaInicio(), fechaFin(), false, f);
        assertTrue(f.exists());
    }

    @Test
    void testVentasProductosCabecera() throws IOException {
        Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        se.obtenerVentasProductos(fechaInicio(), fechaFin(), false, f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("RECAUDACION POR PRODUCTOS"),
                "El informe debe tener la cabecera correcta");
    }

    @Test
    void testVentasProductosPorIdContieneProducto() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta p = new LineaProductoVenta("Gadget", "desc", new File("f.png"), 50, 15.0);
        cat.añadirProducto(p);

        ClienteRegistrado cliente = app.crearCuenta("CC", "333333333C", "1234");
        cliente.añadirProductoACarrito(p, 2);
        SolicitudPedido pedido = cliente.realizarPedido();
        Pago pago = new Pago(new DateTimeSimulado(), 30.0, pedido);
        se.añadirPago(pago);

        File f = new File(tempDir, "ventas.txt");
        se.obtenerVentasProductos(fechaInicio(), fechaFin(), true, f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("Gadget"),
                "El informe por ID debe contener el nombre del producto vendido");
    }

    @Test
    void testVentasProductosPorRecaudacionContieneProducto() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta p = new LineaProductoVenta("Widget", "desc", new File("f.png"), 50, 10.0);
        cat.añadirProducto(p);

        ClienteRegistrado cliente = app.crearCuenta("DD", "444444444D", "1234");
        cliente.añadirProductoACarrito(p, 3);
        SolicitudPedido pedido = cliente.realizarPedido();
        Pago pago = new Pago(new DateTimeSimulado(), 30.0, pedido);
        se.añadirPago(pago);

        File f = new File(tempDir, "ventas2.txt");
        se.obtenerVentasProductos(fechaInicio(), fechaFin(), false, f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("Widget"),
                "El informe por recaudación debe contener el nombre del producto vendido");
    }

    @Test
    void testVentasProductosPagoFueraDeRangoNoAfectaTotal() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta p = new LineaProductoVenta("Producto", "desc", new File("f.png"), 50, 100.0);
        cat.añadirProducto(p);

        ClienteRegistrado cliente = app.crearCuenta("EE", "555555555E", "1234");
        cliente.añadirProductoACarrito(p, 1);
        SolicitudPedido pedido = cliente.realizarPedido();

        // Pago con fecha fuera del rango consultado
        DateTimeSimulado fechaFuera = new DateTimeSimulado(99, 6, 1, 0, 0, 0);
        Pago pago = new Pago(fechaFuera, 100.0, pedido);
        se.añadirPago(pago);

        File f = new File(tempDir, "ventas3.txt");
        se.obtenerVentasProductos(fechaInicio(), fechaFin(), true, f);
        String contenido = leerFichero(f);

        // El total recaudado en el rango debe ser 0
        assertTrue(contenido.contains("0,00 €") || contenido.contains("0.00 €"),
                "Un pago fuera de rango no debe contabilizarse");
    }

    // ---------------------------------------------------------------
    // obtenerGastoClientes — validación de parámetros
    // ---------------------------------------------------------------

    @Test
    void testGastoClientesPeriodoInicioNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerGastoClientes(null, fechaFin(), f));
    }

    @Test
    void testGastoClientesPeriodoFinNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerGastoClientes(fechaInicio(), null, f));
    }

    @Test
    void testGastoClientesFicheroNuloLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerGastoClientes(fechaInicio(), fechaFin(), null));
    }

    @Test
    void testGastoClientesPeriodoInvertidoLanzaExcepcion() {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "out.txt");
        assertThrows(IllegalArgumentException.class,
                () -> se.obtenerGastoClientes(fechaFin(), fechaInicio(), f));
    }

    @Test
    void testGastoClientesSinClientesGeneraFichero() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "clientes.txt");
        se.obtenerGastoClientes(fechaInicio(), fechaFin(), f);
        assertTrue(f.exists(), "El fichero debe crearse aunque no haya clientes");
    }

    @Test
    void testGastoClientesCabecera() throws IOException {
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        File f = new File(tempDir, "clientes.txt");
        se.obtenerGastoClientes(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("RECAUDACION POR CLIENTES"),
                "La cabecera debe indicar que es un informe por clientes");
    }

    @Test
    void testGastoClientesConPedidoPagadoReflejaGasto() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta prod = new LineaProductoVenta("Libro", "desc", new File("f.png"), 100, 25.0);
        cat.añadirProducto(prod);

        ClienteRegistrado cliente = app.crearCuenta("FF", "666666666F", "1234");
        cliente.añadirProductoACarrito(prod, 2);
        SolicitudPedido pedido = cliente.realizarPedido();

        // Asociamos el pago tanto a estadísticas como al pedido
        Pago pago = app.getSistemaPago().procesarPago(50.0, "1234567890123456", "123",
                DateTimeSimulado.DateTimeDiasDespues(30), pedido);
        pedido.añadirPagoPedido(pago);
        pedido.actualizarEstadoPedido(EstadoPedido.PAGADO);
        se.añadirPago(pago);

        File f = new File(tempDir, "clientes2.txt");
        se.obtenerGastoClientes(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);

        // El nombre de usuario del cliente debe aparecer
        assertTrue(contenido.contains("FF"),
                "El informe debe mostrar el nombre del cliente con gasto");
    }

    @Test
    void testGastoClientesConValidacionPagadaReflejaGasto() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();

        ClienteRegistrado cliente = app.crearCuenta("GG", "777777777G", "1234");
        ProductoSegundaMano psm = cliente.añadirProductoACarteraDeIntercambio("Muñeca", "d", null);

        Empleado emp = new Empleado("emp2", "888888888H", "123456");
        emp.añadirPermiso(Permiso.VALIDACIONES);
        emp.validarProducto(psm.getSolicitudValidacion(), 8.0, 50.0, EstadoConservacion.PERFECTO);

        Pago pagoVal = app.getSistemaPago().procesarPago(8.0, "1234567890123456", "123",
                DateTimeSimulado.DateTimeDiasDespues(30), psm.getSolicitudValidacion());
        psm.getSolicitudValidacion().añadirPagoValidacion(pagoVal);
        se.añadirPago(pagoVal);

        File f = new File(tempDir, "clientes3.txt");
        se.obtenerGastoClientes(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("GG"),
                "El informe debe mostrar el cliente con gasto en validación");
    }

    @Test
    void testGastoClientesVariosClientesOrdenadosPorGasto() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta prod1 = new LineaProductoVenta("Caro",   "d", new File("f.png"), 100, 100.0);
        LineaProductoVenta prod2 = new LineaProductoVenta("Barato",  "d", new File("f.png"), 100, 5.0);
        cat.añadirProducto(prod1);
        cat.añadirProducto(prod2);

        ClienteRegistrado c1 = app.crearCuenta("Rico",   "101010101A", "1234");
        ClienteRegistrado c2 = app.crearCuenta("Pobre",  "202020202B", "1234");

        c1.añadirProductoACarrito(prod1, 1);
        SolicitudPedido pedido1 = c1.realizarPedido();
        Pago pago1 = app.getSistemaPago().procesarPago(100.0, "1234567890123456", "123",
                DateTimeSimulado.DateTimeDiasDespues(30), pedido1);
        pedido1.añadirPagoPedido(pago1);
        pedido1.actualizarEstadoPedido(EstadoPedido.PAGADO);
        se.añadirPago(pago1);

        c2.añadirProductoACarrito(prod2, 1);
        SolicitudPedido pedido2 = c2.realizarPedido();
        Pago pago2 = app.getSistemaPago().procesarPago(5.0, "1234567890123456", "123",
                DateTimeSimulado.DateTimeDiasDespues(30), pedido2);
        pedido2.añadirPagoPedido(pago2);
        pedido2.actualizarEstadoPedido(EstadoPedido.PAGADO);
        se.añadirPago(pago2);

        File f = new File(tempDir, "clientes4.txt");
        se.obtenerGastoClientes(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);

        // Verificamos que ambos clientes aparecen
        assertTrue(contenido.contains("Rico"),  "El cliente 'Rico' debe aparecer en el informe");
        assertTrue(contenido.contains("Pobre"), "El cliente 'Pobre' debe aparecer en el informe");

        // El de mayor gasto debe aparecer antes en el fichero
        int posRico  = contenido.indexOf("Rico");
        int posPobre = contenido.indexOf("Pobre");
        assertTrue(posRico < posPobre,
                "El informe debe ordenar de mayor a menor gasto (Rico antes que Pobre)");
    }

    @Test
    void testGastoClientesTotalAcumuladoCorrecto() throws IOException {
        Aplicacion app = Aplicacion.getInstancia();
        SistemaEstadisticas se = SistemaEstadisticas.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta prod = new LineaProductoVenta("Item", "d", new File("f.png"), 100, 40.0);
        cat.añadirProducto(prod);

        ClienteRegistrado c = app.crearCuenta("Medio", "303030303C", "1234");
        c.añadirProductoACarrito(prod, 1);
        SolicitudPedido pedido = c.realizarPedido();
        Pago pago = app.getSistemaPago().procesarPago(40.0, "1234567890123456", "123",
                DateTimeSimulado.DateTimeDiasDespues(30), pedido);
        pedido.añadirPagoPedido(pago);
        pedido.actualizarEstadoPedido(EstadoPedido.PAGADO);
        se.añadirPago(pago);

        File f = new File(tempDir, "clientes5.txt");
        se.obtenerGastoClientes(fechaInicio(), fechaFin(), f);
        String contenido = leerFichero(f);
        assertTrue(contenido.contains("40,00 €") || contenido.contains("40.00 €"),
                "El total acumulado debe ser 40,00 €");
    }
}
