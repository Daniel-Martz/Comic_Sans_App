package filtro.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import categoria.Categoria;
import filtro.FiltroIntercambio;
import filtro.FiltroVenta;
import filtro.FiltroVentaCliente;
import filtro.TipoDescuento;
import filtro.TipoProducto;
import producto.EstadoConservacion;

class FiltroTest {

    // =========================================================
    // FiltroVenta
    // =========================================================

    @Test
    void testFiltroVentaCreadoAscendente() {
        FiltroVenta filtro = new FiltroVenta(true);
        assertTrue(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroVentaCreadoDescendente() {
        FiltroVenta filtro = new FiltroVenta(false);
        assertFalse(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroVentaCategoriasFiltroVacio() {
        FiltroVenta filtro = new FiltroVenta(true);
        assertTrue(filtro.getCategoriasFiltradas().isEmpty());
    }

    @Test
    void testFiltroVentaTiposFiltroVacio() {
        FiltroVenta filtro = new FiltroVenta(true);
        assertTrue(filtro.getTipoFiltrado().isEmpty());
    }

    @Test
    void testFiltroVentaCambiarFiltroConCategorias() {
        FiltroVenta filtro = new FiltroVenta(true);
        Set<Categoria> categorias = new HashSet<>();
        categorias.add(new Categoria("Accion"));
        categorias.add(new Categoria("Romance"));
        filtro.cambiarFiltro(false, categorias, null);
        assertEquals(2, filtro.getCategoriasFiltradas().size());
        assertFalse(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroVentaCambiarFiltroConTipos() {
        FiltroVenta filtro = new FiltroVenta(false);
        Set<TipoProducto> tipos = new HashSet<>();
        tipos.add(TipoProducto.COMIC);
        tipos.add(TipoProducto.FIGURA);
        filtro.cambiarFiltro(true, null, tipos);
        assertEquals(2, filtro.getTipoFiltrado().size());
        assertTrue(filtro.getTipoFiltrado().contains(TipoProducto.COMIC));
        assertTrue(filtro.getTipoFiltrado().contains(TipoProducto.FIGURA));
    }

    @Test
    void testFiltroVentaCambiarFiltroNulos() {
        FiltroVenta filtro = new FiltroVenta(true);
        assertDoesNotThrow(() -> filtro.cambiarFiltro(false, null, null));
        assertTrue(filtro.getCategoriasFiltradas().isEmpty());
        assertTrue(filtro.getTipoFiltrado().isEmpty());
    }

    @Test
    void testFiltroVentaLimpiarFiltro() {
        FiltroVenta filtro = new FiltroVenta(true);
        Set<Categoria> cats = new HashSet<>();
        cats.add(new Categoria("Terror"));
        Set<TipoProducto> tipos = new HashSet<>();
        tipos.add(TipoProducto.JUEGO_DE_MESA);
        filtro.cambiarFiltro(true, cats, tipos);
        filtro.limpiarFiltro();
        assertTrue(filtro.getCategoriasFiltradas().isEmpty());
        assertTrue(filtro.getTipoFiltrado().isEmpty());
        assertFalse(filtro.isOrdenAscendente());
    }

    // =========================================================
    // FiltroVentaCliente
    // =========================================================

    @Test
    void testFiltroVentaClienteCreacion() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        assertEquals(0.0, filtro.getPuntuacionMin());
        assertEquals(5.0, filtro.getPuntuacionMax());
        assertEquals(0.0, filtro.getPrecioMin());
        assertEquals(100.0, filtro.getPrecioMax());
        assertFalse(filtro.isOrdenarPorPrecio());
        assertFalse(filtro.isOrdenarPorPuntuacion());
    }

    @Test
    void testFiltroVentaClienteCambiarFiltroValido() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        filtro.cambiarFiltro(false, null, null, 2.0, 4.5, 10.0, 50.0, true, false, null);
        assertEquals(2.0, filtro.getPuntuacionMin());
        assertEquals(4.5, filtro.getPuntuacionMax());
        assertEquals(10.0, filtro.getPrecioMin());
        assertEquals(50.0, filtro.getPrecioMax());
        assertTrue(filtro.isOrdenarPorPrecio());
        assertFalse(filtro.isOrdenarPorPuntuacion());
        assertFalse(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroVentaClientePuntuacionMinNegativaLanzaExcepcion() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, null, null, -1.0, 5.0, 0.0, 100.0, false, false, null));
    }

    @Test
    void testFiltroVentaClientePuntuacionMaxSuperior5LanzaExcepcion() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, null, null, 0.0, 6.0, 0.0, 100.0, false, false, null));
    }

    @Test
    void testFiltroVentaClientePuntuacionMinMayorQueMaxLanzaExcepcion() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, null, null, 4.0, 2.0, 0.0, 100.0, false, false, null));
    }

    @Test
    void testFiltroVentaClientePrecioMinNegativoLanzaExcepcion() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, null, null, 0.0, 5.0, -5.0, 100.0, false, false, null));
    }

    @Test
    void testFiltroVentaClientePrecioMinMayorQueMaxLanzaExcepcion() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, null, null, 0.0, 5.0, 200.0, 100.0, false, false, null));
    }

    @Test
    void testFiltroVentaClienteCambiarFiltroConDescuentos() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        Set<TipoDescuento> descuentos = new HashSet<>();
        descuentos.add(TipoDescuento.REBAJA);
        descuentos.add(TipoDescuento.REGALO);
        filtro.cambiarFiltro(true, null, null, 0.0, 5.0, 0.0, 100.0, false, false, descuentos);
        assertTrue(filtro.getDescuentoFiltrado().contains(TipoDescuento.REBAJA));
        assertTrue(filtro.getDescuentoFiltrado().contains(TipoDescuento.REGALO));
    }

    @Test
    void testFiltroVentaClienteLimpiarFiltro() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 2.0, 4.0, 5.0, 50.0, true, true);
        filtro.limpiarFiltro();
        assertEquals(0.0, filtro.getPuntuacionMin());
        assertEquals(5.0, filtro.getPuntuacionMax());
        assertEquals(0.0, filtro.getPrecioMin());
        assertEquals(Double.MAX_VALUE, filtro.getPrecioMax());
        assertFalse(filtro.isOrdenarPorPrecio());
        assertFalse(filtro.isOrdenarPorPuntuacion());
        assertTrue(filtro.getDescuentoFiltrado().isEmpty());
        assertTrue(filtro.getCategoriasFiltradas().isEmpty());
        assertTrue(filtro.getTipoFiltrado().isEmpty());
        assertFalse(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroVentaClienteSetters() {
        FiltroVentaCliente filtro = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
        filtro.setPuntuacionMin(1.0);
        filtro.setPuntuacionMax(4.0);
        filtro.setPrecioMin(5.0);
        filtro.setPrecioMax(200.0);
        filtro.setOrdenarPorPrecio(true);
        filtro.setOrdenarPorPuntuacion(true);
        assertEquals(1.0, filtro.getPuntuacionMin());
        assertEquals(4.0, filtro.getPuntuacionMax());
        assertEquals(5.0, filtro.getPrecioMin());
        assertEquals(200.0, filtro.getPrecioMax());
        assertTrue(filtro.isOrdenarPorPrecio());
        assertTrue(filtro.isOrdenarPorPuntuacion());
    }

    // =========================================================
    // FiltroIntercambio
    // =========================================================

    @Test
    void testFiltroIntercambioCreacionValida() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 0.0, 50.0);
        assertEquals(0.0, filtro.getValorMin());
        assertEquals(50.0, filtro.getValorMax());
        assertTrue(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroIntercambioValorMinNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new FiltroIntercambio(true, -1.0, 50.0));
    }

    @Test
    void testFiltroIntercambioValorMaxMenorQueMinLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new FiltroIntercambio(true, 30.0, 10.0));
    }

    @Test
    void testFiltroIntercambioEstadosVaciosInicialmente() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 0.0, 100.0);
        assertTrue(filtro.getEstadosFiltrados().isEmpty());
    }

    @Test
    void testFiltroIntercambioCambiarFiltroConEstados() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 0.0, 100.0);
        Set<EstadoConservacion> estados = new HashSet<>();
        estados.add(EstadoConservacion.PERFECTO);
        estados.add(EstadoConservacion.MUY_BUENO);
        filtro.cambiarFiltro(false, 5.0, 80.0, estados);
        assertEquals(5.0, filtro.getValorMin());
        assertEquals(80.0, filtro.getValorMax());
        assertTrue(filtro.getEstadosFiltrados().contains(EstadoConservacion.PERFECTO));
        assertTrue(filtro.getEstadosFiltrados().contains(EstadoConservacion.MUY_BUENO));
        assertFalse(filtro.isOrdenAscendente());
    }

    @Test
    void testFiltroIntercambioCambiarFiltroValorMinNegativoLanzaExcepcion() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 0.0, 100.0);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, -5.0, 100.0, null));
    }

    @Test
    void testFiltroIntercambioCambiarFiltroValorMaxMenorQueMinLanzaExcepcion() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 0.0, 100.0);
        assertThrows(IllegalArgumentException.class,
            () -> filtro.cambiarFiltro(true, 60.0, 10.0, null));
    }

    @Test
    void testFiltroIntercambioCambiarFiltroConEstadosNulos() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 0.0, 100.0);
        assertDoesNotThrow(() -> filtro.cambiarFiltro(true, 0.0, 50.0, null));
        assertTrue(filtro.getEstadosFiltrados().isEmpty());
    }

    @Test
    void testFiltroIntercambioLimpiarFiltro() {
        FiltroIntercambio filtro = new FiltroIntercambio(true, 10.0, 80.0);
        Set<EstadoConservacion> estados = new HashSet<>();
        estados.add(EstadoConservacion.USO_EVIDENTE);
        filtro.cambiarFiltro(true, 10.0, 80.0, estados);
        filtro.limpiarFiltro();
        assertEquals(0.0, filtro.getValorMin());
        assertEquals(Double.MAX_VALUE, filtro.getValorMax());
        assertTrue(filtro.getEstadosFiltrados().isEmpty());
        assertFalse(filtro.isOrdenAscendente());
    }
}