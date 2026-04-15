package modelo.usuario.test;

import modelo.usuario.*;
import modelo.producto.*;
import modelo.aplicacion.*;
import modelo.categoria.Categoria;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


// ============================================================
//  INTERES TEST
// ============================================================
class InteresTest {

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

    /** Crea un Interes limpio con la aplicación inicializada. */
    private Interes nuevoInteres() {
        Aplicacion.getInstancia(); // inicializa singletons
        return new Interes();
    }

    // ---------------------------------------------------------------
    // Estado inicial
    // ---------------------------------------------------------------

    @Test
    void testObtenerRankingDeInteresInicialDevuelveMapa() {
        Interes interes = nuevoInteres();
        assertNotNull(interes.obtenerRankingDeInteres());
    }

    @Test
    void testObtenerRankingDeInteresInicialSinProductoEsVacio() {
        Interes interes = nuevoInteres();
        // Con catálogo vacío y sin búsquedas el ranking debe estar vacío
        assertTrue(interes.obtenerRankingDeInteres().isEmpty());
    }

    // ---------------------------------------------------------------
    // actualizarInteresBusquedaVenta
    // ---------------------------------------------------------------

    @Test
    void testActualizarInteresBusquedaListaVaciaNoFalla() {
        Interes interes = nuevoInteres();
        assertDoesNotThrow(() ->
                interes.actualizarInteresBusquedaVenta(Collections.emptyList()));
    }

    @Test
    void testActualizarInteresBusquedaUnProductoApareceEnRanking() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("BuscadoA", "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresBusquedaVenta(List.of(p));

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        assertTrue(ranking.containsKey(p), "El producto buscado debe aparecer en el ranking");
        assertTrue(ranking.get(p) > 0.0, "La puntuación debe ser positiva tras una búsqueda");
    }

    @Test
    void testActualizarInteresBusquedaVariasBusquedasAcumulan() {
        Aplicacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("BuscadoB", "d", new File("f.png"), 5, 5.0);
        Catalogo.getInstancia().añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresBusquedaVenta(List.of(p));
        double tras1 = interes.obtenerRankingDeInteres().getOrDefault(p, 0.0);

        interes.actualizarInteresBusquedaVenta(List.of(p));
        double tras2 = interes.obtenerRankingDeInteres().getOrDefault(p, 0.0);

        assertTrue(tras2 >= tras1,
                "Más búsquedas no deben reducir el interés del producto");
    }

    @Test
    void testActualizarInteresBusquedaVariosProductosIndependientes() {
        Aplicacion.getInstancia();
        LineaProductoVenta p1 = new LineaProductoVenta("BuscadoC", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("BuscadoD", "d", new File("f.png"), 5, 5.0);
        Catalogo.getInstancia().añadirProducto(p1);
        Catalogo.getInstancia().añadirProducto(p2);

        Interes interes = new Interes();
        // Buscamos p1 dos veces y p2 una vez
        interes.actualizarInteresBusquedaVenta(List.of(p1));
        interes.actualizarInteresBusquedaVenta(List.of(p1));
        interes.actualizarInteresBusquedaVenta(List.of(p2));

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        assertTrue(ranking.get(p1) >= ranking.get(p2),
                "El producto más buscado debe tener puntuación >= al menos buscado");
    }

    // ---------------------------------------------------------------
    // actualizarInteresCarritoCategorias
    // ---------------------------------------------------------------

    @Test
    void testActualizarInteresCarritoProductoSinCategoriasNoFalla() {
        Aplicacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("SinCat", "d", new File("f.png"), 5, 5.0);
        Interes interes = new Interes();
        assertDoesNotThrow(() -> interes.actualizarInteresCarritoCategorias(p));
    }

    @Test
    void testActualizarInteresCarritoConCategoriaAumentaInteresCat() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        Categoria categoria = new Categoria("Accion");
        cat.añadirCategoria(categoria);

        LineaProductoVenta p = new LineaProductoVenta("ConCat", "d", new File("f.png"), 5, 5.0);
        p.añadirCategoria(categoria);
        cat.añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresCarritoCategorias(p);

        // El producto pertenece a la categoría, su interés debe ser > 0
        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        assertTrue(ranking.containsKey(p));
        assertTrue(ranking.get(p) > 0.0);
    }

    // ---------------------------------------------------------------
    // actualizarInteresCompraCategorias
    // ---------------------------------------------------------------

    @Test
    void testActualizarInteresCompraConCategoriaIncrementaRanking() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        Categoria cat1 = new Categoria("Fantasia");
        cat.añadirCategoria(cat1);

        LineaProductoVenta p = new LineaProductoVenta("Comprado", "d", new File("f.png"), 5, 5.0);
        p.añadirCategoria(cat1);
        cat.añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresCompraCategorias(p);

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        assertTrue(ranking.getOrDefault(p, 0.0) > 0.0,
                "Comprar un producto debe aumentar el interés de su categoría");
    }

    @Test
    void testActualizarInteresCompraEsMayorQueBusqueda() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        Categoria cat1 = new Categoria("Thriller2");
        cat.añadirCategoria(cat1);

        LineaProductoVenta p = new LineaProductoVenta("Thriller2Prod", "d", new File("f.png"), 5, 5.0);
        p.añadirCategoria(cat1);
        cat.añadirProducto(p);

        Interes interesCompra  = new Interes();
        Interes interesBusqueda = new Interes();

        interesCompra.actualizarInteresCompraCategorias(p);
        interesBusqueda.actualizarInteresBusquedaVenta(List.of(p));

        // PESO_COMPRA (20) > PESO_BUSQUEDA (5), así que la compra tiene más peso
        double valorCompra   = interesCompra.obtenerRankingDeInteres().getOrDefault(p, 0.0);
        double valorBusqueda = interesBusqueda.obtenerRankingDeInteres().getOrDefault(p, 0.0);
        assertTrue(valorCompra >= valorBusqueda,
                "El interés generado por comprar debe ser >= al de buscar");
    }

    // ---------------------------------------------------------------
    // actualizarInteresPedidoCategorias
    // ---------------------------------------------------------------

    @Test
    void testActualizarInteresPedidoConCategoriaIncrementaRanking() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        Categoria catPedido = new Categoria("Pedido");
        cat.añadirCategoria(catPedido);

        LineaProductoVenta p = new LineaProductoVenta("Pedido1", "d", new File("f.png"), 5, 5.0);
        p.añadirCategoria(catPedido);
        cat.añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresPedidoCategorias(p);

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        assertTrue(ranking.getOrDefault(p, 0.0) > 0.0);
    }

    // ---------------------------------------------------------------
    // actualizarInteresNuevoVenta
    // ---------------------------------------------------------------

    @Test
    void testActualizarInteresNuevoVentaRegistraProducto() {
        Aplicacion.getInstancia();
        Interes interes = new Interes();
        LineaProductoVenta p = new LineaProductoVenta("Nuevo1", "d", new File("f.png"), 5, 5.0);
        interes.actualizarInteresNuevoVenta(p);
        // Tras añadir el nuevo producto al interés, el ranking puede calcularse
        assertDoesNotThrow(() -> interes.obtenerRankingDeInteres());
    }

    @Test
    void testActualizarInteresNuevoVentaElProductoTienePuntuacionMinimaOMenorQueOtros() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();

        // Producto "viejo" muy buscado
        LineaProductoVenta viejo = new LineaProductoVenta("Viejo", "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(viejo);

        Interes interes = new Interes();
        // Le damos mucho interés al producto viejo
        for (int i = 0; i < 5; i++) {
            interes.actualizarInteresBusquedaVenta(List.of(viejo));
        }

        // Añadimos un producto nuevo
        LineaProductoVenta nuevo = new LineaProductoVenta("Nuevo2", "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(nuevo);
        interes.actualizarInteresNuevoVenta(nuevo);

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        // El nuevo producto no debe superar en interés al viejo muy buscado
        double puntuacionViejo = ranking.getOrDefault(viejo, 0.0);
        double puntuacionNuevo = ranking.getOrDefault(nuevo, 0.0);
        assertTrue(puntuacionNuevo <= puntuacionViejo,
                "Un producto nuevo no debe superar el interés acumulado de uno muy buscado");
    }

    // ---------------------------------------------------------------
    // eliminarProductoInteres
    // ---------------------------------------------------------------

    @Test
    void testEliminarProductoInteresNoApareceEnRanking() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Eliminable", "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresBusquedaVenta(List.of(p));
        interes.eliminarProductoInteres(p);

        assertFalse(interes.obtenerRankingDeInteres().containsKey(p),
                "El producto eliminado no debe aparecer en el ranking");
    }

    @Test
    void testEliminarProductoNoExistenteNoLanzaExcepcion() {
        Interes interes = nuevoInteres();
        LineaProductoVenta p = new LineaProductoVenta("Fantasma", "d", new File("f.png"), 5, 5.0);
        assertDoesNotThrow(() -> interes.eliminarProductoInteres(p));
    }

    // ---------------------------------------------------------------
    // actualizarInteresCategoriaNueva / eliminarCategoriaInteres
    // ---------------------------------------------------------------

    @Test
    void testActualizarInteresCategoriaNuevaNoLanzaExcepcion() {
        Interes interes = nuevoInteres();
        Categoria c = new Categoria("Nueva");
        assertDoesNotThrow(() -> interes.actualizarInteresCategoriaNueva(c));
    }

    @Test
    void testEliminarCategoriaInteresNoLanzaExcepcion() {
        Interes interes = nuevoInteres();
        Categoria c = new Categoria("Borrable");
        interes.actualizarInteresCategoriaNueva(c);
        assertDoesNotThrow(() -> interes.eliminarCategoriaInteres(c));
    }

    @Test
    void testEliminarCategoriaNoExistenteNoLanzaExcepcion() {
        Interes interes = nuevoInteres();
        assertDoesNotThrow(() -> interes.eliminarCategoriaInteres(new Categoria("Inexistente")));
    }

    // ---------------------------------------------------------------
    // obtenerRankingDeInteres — valores normalizados entre 0 y 1
    // ---------------------------------------------------------------

    @Test
    void testRankingNormalizadoEntresCeroYUno() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        LineaProductoVenta p1 = new LineaProductoVenta("N1", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("N2", "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p1);
        cat.añadirProducto(p2);

        Interes interes = new Interes();
        interes.actualizarInteresBusquedaVenta(List.of(p1, p2));
        interes.actualizarInteresBusquedaVenta(List.of(p1));

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        for (double valor : ranking.values()) {
            assertTrue(valor >= 0.0 && valor <= 1.0,
                    "Todos los valores del ranking deben estar en [0, 1], pero fue: " + valor);
        }
    }

    @Test
    void testRankingConMaximoBusquedoTieneValorUno() {
        Aplicacion.getInstancia();
        Catalogo cat = Catalogo.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("ElMejor", "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p);

        Interes interes = new Interes();
        interes.actualizarInteresBusquedaVenta(List.of(p));

        Map<LineaProductoVenta, Double> ranking = interes.obtenerRankingDeInteres();
        assertEquals(1.0, ranking.get(p), 0.001,
                "El único producto buscado debe tener valor normalizado de 1.0");
    }

    // ---------------------------------------------------------------
    // Constantes de pesos
    // ---------------------------------------------------------------

    @Test
    void testConstantePesoBusquedaEsCinco() {
        assertEquals(5, Interes.PESO_BUSQUEDA);
    }

    @Test
    void testConstantePesoCompraEsVeinte() {
        assertEquals(20, Interes.PESO_COMPRA);
    }

    @Test
    void testConstantePesoPedidoEsQuince() {
        assertEquals(15, Interes.PESO_PEDIDO);
    }

    @Test
    void testConstantePesoCarritoEsDiez() {
        assertEquals(10, Interes.PESO_CARRITO);
    }
}
