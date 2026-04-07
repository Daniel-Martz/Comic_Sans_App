package usuario.test;

import usuario.*;
import producto.*;
import aplicacion.*;
import categoria.Categoria;
import descuento.*;
import tiempo.DateTimeSimulado;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


// ============================================================
//  USUARIO GESTION TEST
//  (se prueba a través de Gestor, que es la subclase concreta
//   más accesible; los métodos de UsuarioGestion son heredados)
// ============================================================
class UsuarioGestionTest {

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
        Aplicacion.getInstancia().iniciarSesion("gestor", "123456");
        return (Gestor) Aplicacion.getInstancia().getUsuarioActual();
    }

    // ---------------------------------------------------------------
    // añadirProducto
    // ---------------------------------------------------------------

    @Test
    void testAñadirProductoApareceEnCatalogo() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("Cómic X", "desc", new File("f.png"), 10, 9.99);
        assertTrue(Catalogo.getInstancia().getProductosNuevos().contains(p));
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testAñadirProductoDevuelveProductoNoNulo() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("Cómic Y", "desc", new File("f.png"), 5, 4.99);
        assertNotNull(p);
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testAñadirVariosProductosAparecentodos() {
        Gestor g = loginGestor();
        LineaProductoVenta p1 = g.añadirProducto("P1", "d", new File("f.png"), 10, 1.0);
        LineaProductoVenta p2 = g.añadirProducto("P2", "d", new File("f.png"), 10, 2.0);
        Set<LineaProductoVenta> prods = Catalogo.getInstancia().getProductosNuevos();
        assertTrue(prods.contains(p1) && prods.contains(p2));
        Aplicacion.getInstancia().cerrarSesion();
    }

    // ---------------------------------------------------------------
    // añadirPack
    // ---------------------------------------------------------------

    @Test
    void testAñadirPackVacioApareceEnCatalogo() {
        Gestor g = loginGestor();
        Pack pack = g.añadirPack("Pack test", "desc", new File("f.png"), 5, 19.99, new HashMap<>());
        assertTrue(Catalogo.getInstancia().getProductosNuevos().contains(pack));
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testAñadirPackConProductos() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("ItemPack", "d", new File("f.png"), 100, 5.0);
        Map<LineaProductoVenta, Integer> prods = new HashMap<>();
        prods.put(p, 1);
        Pack pack = g.añadirPack("Pack con items", "d", new File("f.png"), 10, 14.99, prods);
        assertTrue(pack.getProductosPack().containsKey(p));
        Aplicacion.getInstancia().cerrarSesion();
    }

    // ---------------------------------------------------------------
    // eliminarProducto
    // ---------------------------------------------------------------

    @Test
    void testEliminarProductoDesaparceDelCatalogo() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("Borrable", "d", new File("f.png"), 5, 3.0);
        g.eliminarProducto(p);
        assertFalse(Catalogo.getInstancia().getProductosNuevos().contains(p));
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testEliminarProductoNuloNoLanzaExcepcion() {
        Gestor g = loginGestor();
        assertDoesNotThrow(() -> g.eliminarProducto(null));
        Aplicacion.getInstancia().cerrarSesion();
    }

    // ---------------------------------------------------------------
    // modificarProducto
    // ---------------------------------------------------------------

    @Test
    void testModificarNombreProducto() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("Viejo", "d", new File("f.png"), 5, 3.0);
        g.modificarProducto(p, "Nuevo", null, null, null, null);
        assertEquals("Nuevo", p.getNombre());
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testModificarStockProducto() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("StockTest", "d", new File("f.png"), 5, 3.0);
        g.modificarProducto(p, null, null, 99, null, null);
        assertEquals(99, p.getStock());
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testModificarPrecioProducto() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("PrecioTest", "d", new File("f.png"), 5, 3.0);
        g.modificarProducto(p, null, null, null, 99.99, null);
        assertEquals(99.99, p.getPrecio(), 0.001);
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testModificarTodosLosCamposProducto() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("Original", "desc original", new File("old.png"), 5, 3.0);
        File nuevaFoto = new File("new.png");
        g.modificarProducto(p, "Modificado", "nueva desc", 20, 7.50, nuevaFoto);
        assertAll(
            () -> assertEquals("Modificado", p.getNombre()),
            () -> assertEquals("nueva desc", p.getDescripcion()),
            () -> assertEquals(20, p.getStock()),
            () -> assertEquals(7.50, p.getPrecio(), 0.001),
            () -> assertEquals(nuevaFoto, p.getFoto())
        );
        Aplicacion.getInstancia().cerrarSesion();
    }

    // ---------------------------------------------------------------
    // añadirProductosDesdeFichero
    // ---------------------------------------------------------------

    @Test
    void testAñadirProductosDesdeFicheroInexistenteLanzaExcepcion() {
        Gestor g = loginGestor();
        File noExiste = new File("no_existe_jamas.txt");
        assertThrows(Exception.class,
                () -> g.añadirProductosDesdeFichero(noExiste));
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testAñadirProductosDesdeFicheroNuloLanzaExcepcion() {
        Gestor g = loginGestor();
        assertThrows(Exception.class,
                () -> g.añadirProductosDesdeFichero(null));
        Aplicacion.getInstancia().cerrarSesion();
    }

    // ---------------------------------------------------------------
    // Categorías
    // ---------------------------------------------------------------

    @Test
    void testAñadirCategoriaApareceEnCatalogo() {
        Gestor g = loginGestor();
        g.añadirCategoria("Terror");
        boolean encontrada = Catalogo.getInstancia().getCategoriasTienda()
                .stream().anyMatch(c -> c.getNombre().equals("Terror"));
        assertTrue(encontrada);
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testEliminarCategoriaDesaparceDelCatalogo() {
        Gestor g = loginGestor();
        g.añadirCategoria("Suspenso");
        Categoria cat = Catalogo.getInstancia().buscarCategoriaPorNombre("Suspenso");
        g.eliminarCategoria(cat);
        assertNull(Catalogo.getInstancia().buscarCategoriaPorNombre("Suspenso"));
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testModificarCategoriaCambiaNombre() {
        Gestor g = loginGestor();
        g.añadirCategoria("Romance");
        Categoria cat = Catalogo.getInstancia().buscarCategoriaPorNombre("Romance");
        g.modificarCategoria(cat, "Drama");
        assertEquals("Drama", cat.getNombre());
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testEliminarCategoriaNulaNoLanzaExcepcion() {
        Gestor g = loginGestor();
        assertDoesNotThrow(() -> g.eliminarCategoria(null));
        Aplicacion.getInstancia().cerrarSesion();
    }

    // ---------------------------------------------------------------
    // Descuentos
    // ---------------------------------------------------------------

    @Test
    void testAplicarDescuentoPorcentajeAProducto() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("DescTest", "d", new File("f.png"), 10, 20.0);
        Descuento d = new Precio(new DateTimeSimulado(),
                DateTimeSimulado.DateTimeDiasDespues(30), 50);
        Catalogo.getInstancia().añadirDescuento(d);
        g.aplicarDescuento(d, Set.of(p), null);
        assertEquals(d, p.getDescuento());
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testEliminarDescuentoDeProducto() {
        Gestor g = loginGestor();
        LineaProductoVenta p = g.añadirProducto("DescElim", "d", new File("f.png"), 10, 20.0);
        Descuento d = new Precio(new DateTimeSimulado(),
                DateTimeSimulado.DateTimeDiasDespues(30), 10);
        Catalogo.getInstancia().añadirDescuento(d);
        g.aplicarDescuento(d, Set.of(p), null);
        g.eliminarDescuentoProducto(d, p);
        assertNull(p.getDescuento());
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testAplicarDescuentoACategoria() {
        Gestor g = loginGestor();
        g.añadirCategoria("Anime");
        Categoria cat = Catalogo.getInstancia().buscarCategoriaPorNombre("Anime");
        Descuento d = new Precio(new DateTimeSimulado(),
                DateTimeSimulado.DateTimeDiasDespues(30), 20);
        Catalogo.getInstancia().añadirDescuento(d);
        g.aplicarDescuento(d, null, Set.of(cat));
        assertEquals(d, cat.getDescuento());
        Aplicacion.getInstancia().cerrarSesion();
    }

    @Test
    void testEliminarDescuentoDeCategoria() {
        Gestor g = loginGestor();
        g.añadirCategoria("Sci-Fi");
        Categoria cat = Catalogo.getInstancia().buscarCategoriaPorNombre("Sci-Fi");
        Descuento d = new Precio(new DateTimeSimulado(),
                DateTimeSimulado.DateTimeDiasDespues(30), 15);
        Catalogo.getInstancia().añadirDescuento(d);
        g.aplicarDescuento(d, null, Set.of(cat));
        g.eliminarDescuentoCategoria(d, cat);
        assertNull(cat.getDescuento());
        Aplicacion.getInstancia().cerrarSesion();
    }
}