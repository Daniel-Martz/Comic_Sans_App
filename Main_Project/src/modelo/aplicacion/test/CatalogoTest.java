package modelo.aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import modelo.aplicacion.*;
import modelo.categoria.Categoria;
import modelo.descuento.*;
import modelo.filtro.*;
import modelo.producto.*;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.*;

class CatalogoTest {

    @TempDir
    File tempDir;

    // ---------------------------------------------------------------
    // Reseteo de singletons
    // ---------------------------------------------------------------

    @BeforeEach
    void resetearSingletons() throws Exception {
        resetField(Aplicacion.class,                "instancia");
        resetField(Catalogo.class,                  "instancia");
        resetField(GestorSolicitudes.class,         "instancia");
        resetField(SistemaEstadisticas.class,       "instancia");
        resetField(SistemaPago.class,               "instancia");
        resetField(ConfiguracionRecomendacion.class,"instancia");
    }

    private void resetField(Class<?> clazz, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, null);
    }

    /** Devuelve un Catalogo con la aplicación ya inicializada. */
    private Catalogo cat() {
        Aplicacion.getInstancia();
        return Catalogo.getInstancia();
    }

    /** Crea un fichero temporal con contenido de texto. */
    private File crearFichero(String contenido) throws IOException {
        File f = new File(tempDir, "productos_" + System.nanoTime() + ".txt");
        Files.writeString(f.toPath(), contenido);
        return f;
    }

    // ================================================================
    //  SINGLETON
    // ================================================================

    @Test
    void testGetInstanciaDevuelveInstanciaUnica() {
        Catalogo c1 = cat();
        Catalogo c2 = Catalogo.getInstancia();
        assertSame(c1, c2);
    }

    // ================================================================
    //  DESCUENTOS — añadir / eliminar
    // ================================================================

    @Test
    void testAñadirDescuentoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> cat().añadirDescuento(null));
    }

    @Test
    void testAñadirDescuentoValidoNoLanzaExcepcion() {
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        assertDoesNotThrow(() -> cat().añadirDescuento(d));
    }

    @Test
    void testEliminarDescuentoNoExistenteNoLanzaExcepcion() {
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        assertDoesNotThrow(() -> cat().eliminarDescuento(d));
    }

    // ================================================================
    //  PRODUCTOS NUEVOS — añadir
    // ================================================================

    @Test
    void testAñadirProductoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> cat().añadirProducto(null));
    }

    @Test
    void testAñadirProductoApareceEnCatalogo() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P1", "d", new File("f.png"), 10, 5.0);
        c.añadirProducto(p);
        assertTrue(c.getProductosNuevos().contains(p));
    }

    @Test
    void testAñadirVariosProductosTodasAparecentodos() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("A", "d", new File("f.png"), 5, 1.0);
        LineaProductoVenta p2 = new LineaProductoVenta("B", "d", new File("f.png"), 5, 2.0);
        c.añadirProducto(p1);
        c.añadirProducto(p2);
        assertTrue(c.getProductosNuevos().containsAll(List.of(p1, p2)));
    }

    @Test
    void testAñadirPrimerProductoEstablecePrimerLanzamiento() {
        Catalogo c = cat();
        assertNull(c.getPrimerLanzamiento());
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        assertNotNull(c.getPrimerLanzamiento());
    }

    @Test
    void testAñadirProductoActualizaUltimoLanzamiento() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("P1", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("P2", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p1);
        DateTimeSimulado tras1 = c.getUltimoLanzamiento();
        c.añadirProducto(p2);
        assertNotNull(c.getUltimoLanzamiento());
        // El último lanzamiento no puede ser null
        assertNotNull(tras1);
    }

    // ================================================================
    //  PRODUCTOS NUEVOS — eliminar
    // ================================================================

    @Test
    void testEliminarProductoNuloNoLanzaExcepcion() {
        assertDoesNotThrow(() -> cat().eliminarProducto(null));
    }

    @Test
    void testEliminarProductoDesaparceDelCatalogo() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("Del", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.eliminarProducto(p);
        assertFalse(c.getProductosNuevos().contains(p));
    }

    @Test
    void testEliminarProductoEnCatalogoVacioNoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("X", "d", new File("f.png"), 5, 5.0);
        // No añadimos el producto; eliminar no debe fallar
        assertDoesNotThrow(() -> c.eliminarProducto(p));
    }

    @Test
    void testEliminarUnicoProductoDejaUltimoLanzamientoNulo() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("Solo", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.eliminarProducto(p);
        assertNull(c.getUltimoLanzamiento());
    }

    @Test
    void testEliminarUnicoProductoDejaePrimerLanzamientoNulo() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("Solo2", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.eliminarProducto(p);
        assertNull(c.getPrimerLanzamiento());
    }

    @Test
    void testEliminarProductoEliminaDeRankingsCliente() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo c = Catalogo.getInstancia();
        ClienteRegistrado cliente = app.crearCuenta("cli1", "111111111A", "1234", "1234");
        LineaProductoVenta p = new LineaProductoVenta("Rank", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.eliminarProducto(p);
        // Tras eliminar, el ranking del cliente no debe contener el producto
        assertFalse(cliente.getInteres().obtenerRankingDeInteres().containsKey(p));
    }

    // ================================================================
    //  PRODUCTOS NUEVOS — modificar
    // ================================================================

    @Test
    void testModificarNombreProducto() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("Viejo", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.modificarProducto(p, "Nuevo", null, null, null, null);
        assertEquals("Nuevo", p.getNombre());
    }

    @Test
    void testModificarDescripcionProducto() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "original", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.modificarProducto(p, null, "nueva desc", null, null, null);
        assertEquals("nueva desc", p.getDescripcion());
    }

    @Test
    void testModificarStockProducto() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.modificarProducto(p, null, null, null, 99, null);
        assertEquals(99, p.getStock());
    }

    @Test
    void testModificarPrecioProducto() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        c.modificarProducto(p, null, null, null, null, 77.77);
        assertEquals(77.77, p.getPrecio(), 0.001);
    }

    @Test
    void testModificarFotoProducto() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("old.png"), 5, 5.0);
        c.añadirProducto(p);
        File nueva = new File("new.png");
        c.modificarProducto(p, null, null, nueva, null, null);
        assertEquals(nueva, p.getFoto());
    }

    @Test
    void testModificarConTodosNulosNoAlteraNada() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("Orig", "desc orig", new File("f.png"), 5, 9.0);
        c.añadirProducto(p);
        c.modificarProducto(p, null, null, null, null, null);
        assertAll(
            () -> assertEquals("Orig",      p.getNombre()),
            () -> assertEquals("desc orig", p.getDescripcion()),
            () -> assertEquals(5,           p.getStock()),
            () -> assertEquals(9.0,         p.getPrecio(), 0.001)
        );
    }

    // ================================================================
    //  BUSCAR PRODUCTO NUEVO / INTERCAMBIO POR ID
    // ================================================================

    @Test
    void testBuscarProductoNuevoPorIdExistente() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("BuscarID", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        assertEquals(p, c.buscarProductoNuevo(p.getID()));
    }

    @Test
    void testBuscarProductoNuevoPorIdInexistenteDevuelveNull() {
        assertNull(cat().buscarProductoNuevo(Integer.MAX_VALUE));
    }

    @Test
    void testBuscarProductoIntercambioPorIdInexistenteDevuelveNull() {
        assertNull(cat().buscarProductoIntercambio(Integer.MAX_VALUE));
    }

    // ================================================================
    //  PACKS
    // ================================================================

    @Test
    void testAñadirPackNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirPack(null, new HashMap<>()));
    }

    @Test
    void testAñadirPackConListaNulaLanzaExcepcion() {
        Pack pack = new Pack("Pack", "d", new File("f.png"), 5, 19.99);
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirPack(pack, null));
    }

    @Test
    void testAñadirPackVacioApareceEnProductosNuevos() {
        Catalogo c = cat();
        Pack pack = new Pack("PackVacio", "d", new File("f.png"), 5, 19.99);
        c.añadirPack(pack, new HashMap<>());
        assertTrue(c.getProductosNuevos().contains(pack));
    }

    @Test
    void testAñadirPackConProductosLosTieneAsociados() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("ItemPack", "d", new File("f.png"), 100, 5.0);
        Pack pack = new Pack("PackItems", "d", new File("f.png"), 5, 14.99);
        Map<LineaProductoVenta, Integer> prods = Map.of(p, 1);
        c.añadirPack(pack, prods);
        assertTrue(pack.getProductosPack().containsKey(p));
    }

    // ================================================================
    //  CARGA DESDE FICHERO
    // ================================================================

    private String ficheroComicValido() {
        return "1\n" +
               "Spider-Man\n" +
               "Un gran cómic\n" +
               "9.99\n" +
               "spiderman.jpg\n" +
               "10\n" +
               "Ninguna\n" +        // categoría nula (buscarCategoriaPorNombre devuelve null → no añade cat)
               "Comic\n" +
               "100;Stan Lee;Marvel;1963\n";
    }

    @Test
    void testAñadirProductosDesdeFicheroNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(null));
    }

    @Test
    void testAñadirProductosDesdeFicheroInexistenteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(new File("no_existe.txt")));
    }

    @Test
    void testAñadirProductosDesdeFicheroVacioLanzaExcepcion() throws IOException {
        File f = crearFichero("");
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroNumeroProductosNoNumericoLanzaExcepcion() throws IOException {
        File f = crearFichero("abc\n");
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroTipoDesconocidoLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n5.0\nfoto.jpg\n10\nNinguna\nTipoRaro\natrib1\n"
        );
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroPrecioNegativoLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n-1.0\nfoto.jpg\n10\nNinguna\nComic\n100;Autor;Ed;2000\n"
        );
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroStockNegativoLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n5.0\nfoto.jpg\n-1\nNinguna\nComic\n100;Autor;Ed;2000\n"
        );
        assertThrows(IllegalArgumentException.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroAtributosInsuficientesComicLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n5.0\nfoto.jpg\n10\nNinguna\nComic\n100\n"
        );
        assertThrows(Exception.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroAtributosInsuficientesFiguraLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n5.0\nfoto.jpg\n10\nNinguna\nFigura\nMarca;Plastico\n"
        );
        assertThrows(Exception.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroAtributosInsuficientesJuegoDeMesaLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n5.0\nfoto.jpg\n10\nNinguna\nJuegoDeMesa\n4;8\n"
        );
        assertThrows(Exception.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroJuegoDeMesaTipoInvalidoLanzaExcepcion() throws IOException {
        File f = crearFichero(
            "1\nNombre\nDesc\n5.0\nfoto.jpg\n10\nNinguna\nJuegoDeMesa\n4;6;99;TipoInvalido\n"
        );
        assertThrows(Exception.class,
                () -> cat().añadirProductosDesdeFichero(f));
    }

    @Test
    void testAñadirProductosDesdeFicheroComicValidoDevuelveListaNoVacia() throws IOException {
        Catalogo c = cat();
        // Añadimos la categoría "Ninguna" para que no sea null
        c.añadirCategoria(new Categoria("Ninguna"));

        File f = crearFichero(ficheroComicValido());
        List<LineaProductoVenta> resultado = c.añadirProductosDesdeFichero(f);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testAñadirProductosDesdeFicheroFiguraValida() throws IOException {
        Catalogo c = cat();
        c.añadirCategoria(new Categoria("Cat"));
        File f = crearFichero(
            "1\nFigura1\nFigura coleccionable\n15.0\nfigura.jpg\n20\nCat\nFigura\n" +
            "Bandai;PVC;10.5;5.0;12.3\n"
        );
        List<LineaProductoVenta> resultado = c.añadirProductosDesdeFichero(f);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0) instanceof Figura);
    }

    @Test
    void testAñadirProductosDesdeFicheroJuegoDeMesaValido() throws IOException {
        Catalogo c = cat();
        c.añadirCategoria(new Categoria("Cat2"));
        File f = crearFichero(
            "1\nCatan\nEl clásico\n30.0\ncatan.jpg\n15\nCat2\nJuegoDeMesa\n" +
            "4;8;99;CARTAS\n"
        );
        List<LineaProductoVenta> resultado = c.añadirProductosDesdeFichero(f);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0) instanceof JuegoDeMesa);
    }

    // ================================================================
    //  CATEGORÍAS — añadir / eliminar / modificar / buscar
    // ================================================================

    @Test
    void testAñadirCategoriaNulaNoLanzaExcepcion() {
        assertDoesNotThrow(() -> cat().añadirCategoria(null));
    }

    @Test
    void testAñadirCategoriaApareceEnTienda() {
        Catalogo c = cat();
        Categoria cat = new Categoria("Terror");
        c.añadirCategoria(cat);
        assertTrue(c.getCategoriasTienda().contains(cat));
    }

    @Test
    void testAñadirMismaCategoriaDosVecesSoloApareceUnaVez() {
        Catalogo c = cat();
        Categoria cat = new Categoria("Dup");
        c.añadirCategoria(cat);
        c.añadirCategoria(cat);
        assertEquals(1, c.getCategoriasTienda().stream()
                .filter(x -> x.getNombre().equals("Dup")).count());
    }

    @Test
    void testEliminarCategoriaNulaNoLanzaExcepcion() {
        assertDoesNotThrow(() -> cat().eliminarCategoria(null));
    }

    @Test
    void testEliminarCategoriaDesaparceDelCatalogo() {
        Catalogo c = cat();
        Categoria cat = new Categoria("Borra");
        c.añadirCategoria(cat);
        c.eliminarCategoria(cat);
        assertFalse(c.getCategoriasTienda().contains(cat));
    }

    @Test
    void testEliminarCategoriaNoExistenteNoLanzaExcepcion() {
        Catalogo c = cat();
        assertDoesNotThrow(() -> c.eliminarCategoria(new Categoria("Fantasma")));
    }

    @Test
    void testModificarCategoriaCambiaNombre() {
        Catalogo c = cat();
        Categoria cat = new Categoria("Vieja");
        c.añadirCategoria(cat);
        c.modificarCategoria(cat, "Nueva");
        assertEquals("Nueva", cat.getNombre());
    }

    @Test
    void testModificarCategoriaNulaNoLanzaExcepcion() {
        assertDoesNotThrow(() -> cat().modificarCategoria(null, "Nombre"));
    }

    @Test
    void testModificarCategoriaConNombreVacioNoAlteraNada() {
        Catalogo c = cat();
        Categoria cat = new Categoria("Original");
        c.añadirCategoria(cat);
        c.modificarCategoria(cat, "   ");
        assertEquals("Original", cat.getNombre());
    }

    @Test
    void testBuscarCategoriaPorNombreExistente() {
        Catalogo c = cat();
        Categoria cat = new Categoria("BuscarCat");
        c.añadirCategoria(cat);
        assertEquals(cat, c.buscarCategoriaPorNombre("BuscarCat"));
    }

    @Test
    void testBuscarCategoriaPorNombreInexistenteDevuelveNull() {
        assertNull(cat().buscarCategoriaPorNombre("NoExiste"));
    }

    @Test
    void testBuscarCategoriaPorNombreEsCaseSensitive() {
        Catalogo c = cat();
        c.añadirCategoria(new Categoria("Terror"));
        assertNull(c.buscarCategoriaPorNombre("terror"));
    }

    @Test
    void testGetNombresCategorias() {
        Catalogo c = cat();
        c.añadirCategoria(new Categoria("A"));
        c.añadirCategoria(new Categoria("B"));
        String nombres = c.getNombresCategorias();
        assertTrue(nombres.contains("A") && nombres.contains("B"));
    }

    // ================================================================
    //  DESCUENTOS SOBRE PRODUCTOS
    // ================================================================

    @Test
    void testAplicarDescuentoProductoNuloLanzaExcepcion() {
        Catalogo c = cat();
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        assertThrows(IllegalArgumentException.class, () -> c.aplicarDescuento(null, d));
    }

    @Test
    void testAplicarDescuentoNuloAProductoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        assertThrows(IllegalArgumentException.class, () -> c.aplicarDescuento(p, null));
    }

    @Test
    void testAplicarDescuentoProductoNoEnCatalogoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        // No añadimos 'p' al catálogo
        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(p, d));
    }

    @Test
    void testAplicarDescuentoNoRegistradoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        // No añadimos 'd' al catálogo
        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(p, d));
    }

    @Test
    void testAplicarDescuentoValidoSeAsignaAlProducto() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        c.aplicarDescuento(p, d);
        assertEquals(d, p.getDescuento());
    }

    @Test
    void testAplicarSegundoDescuentoAProductoConDescuentoActivoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        Descuento d1 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        Descuento d2 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 20);
        c.añadirDescuento(d1);
        c.añadirDescuento(d2);
        c.aplicarDescuento(p, d1);
        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(p, d2));
    }

    @Test
    void testAplicarDescuentoAProductoConCategoriConDescuentoLanzaExcepcion() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatDesc");
        c.añadirCategoria(cat);
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        p.añadirCategoria(cat);
        c.añadirProducto(p);

        Descuento dCat = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 5);
        Descuento dProd = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 15);
        c.añadirDescuento(dCat);
        c.añadirDescuento(dProd);
        c.aplicarDescuento(dCat, cat);

        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(p, dProd));
    }

    // ================================================================
    //  ELIMINAR DESCUENTO DE PRODUCTO
    // ================================================================

    @Test
    void testEliminarDescuentoDeProductoNulosLanzaExcepcion() {
        Catalogo c = cat();
        assertThrows(IllegalArgumentException.class, () -> c.eliminarDescuento(null, (LineaProductoVenta) null));
    }

    @Test
    void testEliminarDescuentoDeProductoSinDescuentoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        assertThrows(IllegalStateException.class, () -> c.eliminarDescuento(d, p));
    }

    @Test
    void testEliminarDescuentoDeProductoCoincidenteLoQuita() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        c.aplicarDescuento(p, d);
        c.eliminarDescuento(d, p);
        assertNull(p.getDescuento());
    }

    @Test
    void testEliminarDescuentoNoCoincidenteConElActivoLanzaExcepcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("P", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        Descuento d1 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        Descuento d2 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 20);
        c.añadirDescuento(d1);
        c.añadirDescuento(d2);
        c.aplicarDescuento(p, d1);
        assertThrows(IllegalStateException.class, () -> c.eliminarDescuento(d2, p));
    }

    // ================================================================
    //  DESCUENTOS SOBRE CATEGORÍAS
    // ================================================================

    @Test
    void testAplicarDescuentoCategoriaNulaLanzaExcepcion() {
        Catalogo c = cat();
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        assertThrows(IllegalArgumentException.class, () -> c.aplicarDescuento(d, (Categoria) null));
    }

    @Test
    void testAplicarDescuentoNuloACategoriaLanzaExcepcion() {
        Catalogo c = cat();
        Categoria cat = new Categoria("Cat");
        c.añadirCategoria(cat);
        assertThrows(IllegalArgumentException.class, () -> c.aplicarDescuento(null, cat));
    }

    @Test
    void testAplicarDescuentoCategoriaNoEnCatalogoLanzaExcepcion() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatNoReg");
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(d, cat));
    }

    @Test
    void testAplicarDescuentoValidoACategoriaSeAsigna() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatOK");
        c.añadirCategoria(cat);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        c.aplicarDescuento(d, cat);
        assertEquals(d, cat.getDescuento());
    }

    @Test
    void testAplicarSegundoDescuentoACategoriaConDescuentoActivoLanzaExcepcion() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatDos");
        c.añadirCategoria(cat);
        Descuento d1 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        Descuento d2 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 20);
        c.añadirDescuento(d1);
        c.añadirDescuento(d2);
        c.aplicarDescuento(d1, cat);
        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(d2, cat));
    }

    @Test
    void testAplicarDescuentoACategoriaConProductoConDescuentoIndividualLanzaExcepcion() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatConProd");
        c.añadirCategoria(cat);
        LineaProductoVenta p = new LineaProductoVenta("PP", "d", new File("f.png"), 5, 5.0);
        p.añadirCategoria(cat);
        cat.añadirProductoACategoria(p);
        c.añadirProducto(p);

        Descuento dProd = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 5);
        Descuento dCat  = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 15);
        c.añadirDescuento(dProd);
        c.añadirDescuento(dCat);
        c.aplicarDescuento(p, dProd);

        assertThrows(IllegalStateException.class, () -> c.aplicarDescuento(dCat, cat));
    }

    // ================================================================
    //  ELIMINAR DESCUENTO DE CATEGORÍA
    // ================================================================

    @Test
    void testEliminarDescuentoDeCategoriaNulosLanzaExcepcion() {
        Catalogo c = cat();
        assertThrows(IllegalArgumentException.class, () -> c.eliminarDescuento(null, (Categoria) null));
    }

    @Test
    void testEliminarDescuentoDeCategoriaCoincidenteLoQuita() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatElim");
        c.añadirCategoria(cat);
        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        c.aplicarDescuento(d, cat);
        c.eliminarDescuento(d, cat);
        assertNull(cat.getDescuento());
    }

    @Test
    void testEliminarDescuentoDeCategoriaNoCoincidenteLanzaExcepcion() {
        Catalogo c = cat();
        Categoria cat = new Categoria("CatNoC");
        c.añadirCategoria(cat);
        Descuento d1 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        Descuento d2 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 20);
        c.añadirDescuento(d1);
        c.añadirDescuento(d2);
        c.aplicarDescuento(d1, cat);
        assertThrows(IllegalStateException.class, () -> c.eliminarDescuento(d2, cat));
    }

    // ================================================================
    //  FILTROS — cambiar y limpiar
    // ================================================================

    @Test
    void testCambiarFiltroGestionNoLanzaExcepcion() {
        assertDoesNotThrow(() ->
                cat().cambiarFiltroGestion(true, null, null));
    }

    @Test
    void testCambiarFiltroVentaNoLanzaExcepcion() {
        assertDoesNotThrow(() ->
                cat().cambiarFiltroVenta(true, null, null, 0, 5, 0, 100, false, false, null));
    }

    @Test
    void testCambiarFiltroIntercambioNoLanzaExcepcion() {
        assertDoesNotThrow(() ->
                cat().cambiarFiltroIntercambio(true, 0, Double.MAX_VALUE, null));
    }

    @Test
    void testLimpiarFiltrosNoLanzaExcepcion() {
        Catalogo c = cat();
        c.cambiarFiltroVenta(true, null, null, 1, 4, 5, 50, true, true, null);
        assertDoesNotThrow(c::limpiarFiltros);
    }

    // ================================================================
    //  BÚSQUEDA — obtenerProductosNuevosFiltrados
    // ================================================================

    @Test
    void testBusquedaPromptNullDevuelveTodos() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("Marvel", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("DC",     "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p1);
        c.añadirProducto(p2);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.containsAll(List.of(p1, p2)));
    }

    @Test
    void testBusquedaPromptVacioDevuelveTodos() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("Algo", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        assertFalse(c.obtenerProductosNuevosFiltrados("   ").isEmpty());
    }

    @Test
    void testBusquedaPromptCoincideConNombre() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("Spider-Man", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("Batman",     "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p1);
        c.añadirProducto(p2);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados("Spider");
        assertTrue(res.contains(p1));
        assertFalse(res.contains(p2));
    }

    @Test
    void testBusquedaPromptCoincideConDescripcion() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("X", "descripcion especial", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        assertTrue(c.obtenerProductosNuevosFiltrados("especial").contains(p));
    }

    @Test
    void testBusquedaFiltroPrecoioMinimoExcluyeProductosMasBaratos() {
        Catalogo c = cat();
        LineaProductoVenta barato = new LineaProductoVenta("Barato", "d", new File("f.png"), 5, 1.0);
        LineaProductoVenta caro   = new LineaProductoVenta("Caro",   "d", new File("f.png"), 5, 50.0);
        c.añadirProducto(barato);
        c.añadirProducto(caro);
        c.cambiarFiltroVenta(false, null, null, 0, 5, 10.0, 100.0, false, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertFalse(res.contains(barato));
        assertTrue(res.contains(caro));
    }

    @Test
    void testBusquedaFiltroPrecoioMaximoExcluyeProductosMasCaros() {
        Catalogo c = cat();
        LineaProductoVenta barato = new LineaProductoVenta("Barato2", "d", new File("f.png"), 5, 1.0);
        LineaProductoVenta caro   = new LineaProductoVenta("Caro2",   "d", new File("f.png"), 5, 200.0);
        c.añadirProducto(barato);
        c.añadirProducto(caro);
        c.cambiarFiltroVenta(false, null, null, 0, 5, 0.0, 10.0, false, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.contains(barato));
        assertFalse(res.contains(caro));
    }

    @Test
    void testBusquedaFiltroPorTipoComicSoloDevuelveComics() {
        Catalogo c = cat();
        LineaProductoVenta comic  = new Comic("X-Men", "d", new File("f.png"), 5, 5.0, 0, 100, "Stan", "Marvel", 1963);
        LineaProductoVenta figura = new Figura("Hulk", "d", new File("f.png"), 5, 9.0, 0, "Bandai", "PVC", 10, 5, 12);
        c.añadirProducto(comic);
        c.añadirProducto(figura);
        c.cambiarFiltroVenta(false, null, Set.of(TipoProducto.COMIC), 0, 5, 0, Double.MAX_VALUE, false, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.contains(comic));
        assertFalse(res.contains(figura));
    }

    @Test
    void testBusquedaFiltroPorCategoria() {
        Catalogo c = cat();
        Categoria catA = new Categoria("CatA");
        Categoria catB = new Categoria("CatB");
        c.añadirCategoria(catA);
        c.añadirCategoria(catB);

        LineaProductoVenta pA = new LineaProductoVenta("ProdA", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta pB = new LineaProductoVenta("ProdB", "d", new File("f.png"), 5, 5.0);
        pA.añadirCategoria(catA);
        pB.añadirCategoria(catB);
        c.añadirProducto(pA);
        c.añadirProducto(pB);

        c.cambiarFiltroVenta(false, Set.of(catA), null, 0, 5, 0, Double.MAX_VALUE, false, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.contains(pA));
        assertFalse(res.contains(pB));
    }

    @Test
    void testBusquedaFiltroPorPuntuacion() {
        Catalogo c = cat();
        LineaProductoVenta bueno = new LineaProductoVenta("Bueno", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta malo  = new LineaProductoVenta("Malo",  "d", new File("f.png"), 5, 5.0);
        bueno.añadirReseña(new Reseña("Genial", 5, new DateTimeSimulado(), bueno));
        c.añadirProducto(bueno);
        c.añadirProducto(malo);
        // Solo queremos productos con puntuación >= 4
        c.cambiarFiltroVenta(false, null, null, 4.0, 5.0, 0, Double.MAX_VALUE, false, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.contains(bueno));
        assertFalse(res.contains(malo));
    }

    @Test
    void testBusquedaFiltroPorDescuentoTipoPrecio() {
        Catalogo c = cat();
        LineaProductoVenta conDesc = new LineaProductoVenta("ConDesc", "d", new File("f.png"), 5, 10.0);
        LineaProductoVenta sinDesc = new LineaProductoVenta("SinDesc", "d", new File("f.png"), 5, 10.0);
        c.añadirProducto(conDesc);
        c.añadirProducto(sinDesc);

        Descuento d = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(30), 10);
        c.añadirDescuento(d);
        c.aplicarDescuento(conDesc, d);

        c.cambiarFiltroVenta(false, null, null, 0, 5, 0, Double.MAX_VALUE, false, false, Set.of(TipoDescuento.PRECIO));
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.contains(conDesc));
        assertFalse(res.contains(sinDesc));
    }

    @Test
    void testBusquedaOrdenacionAscendentePorPrecio() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("Z", "d", new File("f.png"), 5, 30.0);
        LineaProductoVenta p2 = new LineaProductoVenta("A", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p1);
        c.añadirProducto(p2);
        c.cambiarFiltroVenta(true, null, null, 0, 5, 0, Double.MAX_VALUE, true, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.indexOf(p2) < res.indexOf(p1),
                "El más barato debe aparecer primero en orden ascendente de precio");
    }

    @Test
    void testBusquedaOrdenacionDescendentePorPrecio() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("Z", "d", new File("f.png"), 5, 30.0);
        LineaProductoVenta p2 = new LineaProductoVenta("A", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p1);
        c.añadirProducto(p2);
        c.cambiarFiltroVenta(false, null, null, 0, 5, 0, Double.MAX_VALUE, true, false, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosFiltrados(null);
        assertTrue(res.indexOf(p1) < res.indexOf(p2),
                "El más caro debe aparecer primero en orden descendente de precio");
    }

    @Test
    void testBusquedaCatalogoVacioDevuelveListaVacia() {
        assertTrue(cat().obtenerProductosNuevosFiltrados(null).isEmpty());
    }

    // ================================================================
    //  BÚSQUEDA — obtenerProductosNuevosGestion
    // ================================================================

    @Test
    void testBusquedaGestionPromptNullDevuelveTodos() {
        Catalogo c = cat();
        LineaProductoVenta p = new LineaProductoVenta("GestProd", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p);
        assertTrue(c.obtenerProductosNuevosGestion(null).contains(p));
    }

    @Test
    void testBusquedaGestionPromptCoincide() {
        Catalogo c = cat();
        LineaProductoVenta p1 = new LineaProductoVenta("Figura Goku",  "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("Comic Batman", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(p1);
        c.añadirProducto(p2);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosGestion("Figura");
        assertTrue(res.contains(p1));
        assertFalse(res.contains(p2));
    }

    @Test
    void testBusquedaGestionFiltroPorTipo() {
        Catalogo c = cat();
        LineaProductoVenta fig = new Figura("Fig", "d", new File("f.png"), 5, 5.0, 0, "B", "PVC", 1, 1, 1);
        LineaProductoVenta com = new Comic("Com", "d", new File("f.png"), 5, 5.0, 0, 50, "A", "Ed", 2000);
        c.añadirProducto(fig);
        c.añadirProducto(com);
        c.cambiarFiltroGestion(false, null, Set.of(TipoProducto.FIGURA));
        List<LineaProductoVenta> res = c.obtenerProductosNuevosGestion(null);
        assertTrue(res.contains(fig));
        assertFalse(res.contains(com));
    }

    @Test
    void testBusquedaGestionFiltroPorCategoria() {
        Catalogo c = cat();
        Categoria catG = new Categoria("CatGest");
        c.añadirCategoria(catG);

        LineaProductoVenta p1 = new LineaProductoVenta("ConCat", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta p2 = new LineaProductoVenta("SinCat", "d", new File("f.png"), 5, 5.0);
        p1.añadirCategoria(catG);
        c.añadirProducto(p1);
        c.añadirProducto(p2);

        c.cambiarFiltroGestion(false, Set.of(catG), null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosGestion(null);
        assertTrue(res.contains(p1));
        assertFalse(res.contains(p2));
    }

    @Test
    void testBusquedaGestionOrdenacionAscendente() {
        Catalogo c = cat();
        LineaProductoVenta pZ = new LineaProductoVenta("Z-prod", "d", new File("f.png"), 5, 5.0);
        LineaProductoVenta pA = new LineaProductoVenta("A-prod", "d", new File("f.png"), 5, 5.0);
        c.añadirProducto(pZ);
        c.añadirProducto(pA);
        c.cambiarFiltroGestion(true, null, null);
        List<LineaProductoVenta> res = c.obtenerProductosNuevosGestion(null);
        assertTrue(res.indexOf(pA) < res.indexOf(pZ),
                "En orden ascendente 'A-prod' debe ir antes que 'Z-prod'");
    }
}
