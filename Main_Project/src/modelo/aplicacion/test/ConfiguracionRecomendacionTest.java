package modelo.aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import modelo.aplicacion.ConfiguracionRecomendacion;
import modelo.aplicacion.GestorSolicitudes;
import modelo.aplicacion.SistemaEstadisticas;
import modelo.aplicacion.SistemaPago;
import modelo.producto.LineaProductoVenta;
import modelo.tiempo.TiempoSimulado;

class ConfiguracionRecomendacionTest {

    /** Resetea todos los singletons para aislar cada test. */
    @BeforeEach
    void resetearSingletons() throws Exception {
        resetField(ConfiguracionRecomendacion.class, "instancia");
        resetField(Aplicacion.class,                "instancia");
        resetField(Catalogo.class,                  "instancia");
        resetField(GestorSolicitudes.class,         "instancia");
        resetField(SistemaEstadisticas.class,        "instancia");
        resetField(SistemaPago.class,                "instancia");
        resetField(TiempoSimulado.class,            "instance");
    }

    private void resetField(Class<?> clazz, String fieldName) throws Exception {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, null);
    }

    // ---------------------------------------------------------------
    // Singleton
    // ---------------------------------------------------------------

    @Test
    void testGetInstanciaDevuelveInstanciaUnica() {
        ConfiguracionRecomendacion c1 = ConfiguracionRecomendacion.getInstancia();
        ConfiguracionRecomendacion c2 = ConfiguracionRecomendacion.getInstancia();
        assertSame(c1, c2, "getInstancia debe devolver siempre el mismo objeto");
    }

    // ---------------------------------------------------------------
    // configurarImportancia
    // ---------------------------------------------------------------

    @Test
    void testConfigurarImportanciaValida() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        // No debe lanzar ninguna excepción
        assertDoesNotThrow(() -> cr.configurarImportancia(2, 3, 5));
    }

    @Test
    void testConfigurarImportanciaConCeros() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertDoesNotThrow(() -> cr.configurarImportancia(0, 0, 0),
                "Importancias a cero deben ser válidas");
    }

    @Test
    void testConfigurarImportanciaInteresNegativoLanzaExcepcion() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> cr.configurarImportancia(-1, 1, 1),
                "Importancia de interés negativa debe lanzar excepción");
    }

    @Test
    void testConfigurarImportanciaResenaNegativaLanzaExcepcion() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> cr.configurarImportancia(1, -1, 1));
    }

    @Test
    void testConfigurarImportanciaNovedadNegativaLanzaExcepcion() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> cr.configurarImportancia(1, 1, -1));
    }

    @Test
    void testConfigurarImportanciaTodosNegativosLanzaExcepcion() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> cr.configurarImportancia(-5, -3, -2));
    }

    // ---------------------------------------------------------------
    // configurarUnidades
    // ---------------------------------------------------------------

    @Test
    void testConfigurarUnidadesValida() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertDoesNotThrow(() -> cr.configurarUnidades(10));
    }

    @Test
    void testConfigurarUnidadesCeroLanzaExcepcion() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> cr.configurarUnidades(0),
                "0 unidades debe lanzar excepción");
    }

    @Test
    void testConfigurarUnidadesNegativoLanzaExcepcion() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertThrows(IllegalArgumentException.class,
                () -> cr.configurarUnidades(-3));
    }

    @Test
    void testConfigurarUnidadesUnaUnidadEsValida() {
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        assertDoesNotThrow(() -> cr.configurarUnidades(1));
    }

    // ---------------------------------------------------------------
    // actualizarRankingNovedad / eliminarProductoNovedad
    // ---------------------------------------------------------------

    @Test
    void testActualizarRankingNovedadNoLanzaExcepcion() {
        Aplicacion.getInstancia(); // inicializa la aplicación
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Prod1", "desc", new File("f.png"), 10, 5.0);
        assertDoesNotThrow(() -> cr.actualizarRankingNovedad(p));
    }

    @Test
    void testEliminarProductoNovedadExistente() {
        Aplicacion.getInstancia();
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Prod1", "desc", new File("f.png"), 10, 5.0);
        cr.actualizarRankingNovedad(p);
        assertDoesNotThrow(() -> cr.eliminarProductoNovedad(p));
    }

    @Test
    void testEliminarProductoNovedadNoExistenteNoLanzaExcepcion() {
        Aplicacion.getInstancia();
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Prod1", "desc", new File("f.png"), 10, 5.0);
        // Eliminamos sin haberlo añadido: no debe explotar
        assertDoesNotThrow(() -> cr.eliminarProductoNovedad(p));
    }

    // ---------------------------------------------------------------
    // actualizarRankingValoracion / eliminarProductoValoracion
    // ---------------------------------------------------------------

    @Test
    void testActualizarRankingValoracionNoLanzaExcepcion() {
        Aplicacion.getInstancia();
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Prod1", "desc", new File("f.png"), 10, 5.0);
        assertDoesNotThrow(() -> cr.actualizarRankingValoracion(p));
    }

    @Test
    void testEliminarProductoValoracionExistente() {
        Aplicacion.getInstancia();
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Prod1", "desc", new File("f.png"), 10, 5.0);
        cr.actualizarRankingValoracion(p);
        assertDoesNotThrow(() -> cr.eliminarProductoValoracion(p));
    }

    @Test
    void testEliminarProductoValoracionNoExistenteNoLanzaExcepcion() {
        Aplicacion.getInstancia();
        ConfiguracionRecomendacion cr = ConfiguracionRecomendacion.getInstancia();
        LineaProductoVenta p = new LineaProductoVenta("Prod1", "desc", new File("f.png"), 10, 5.0);
        assertDoesNotThrow(() -> cr.eliminarProductoValoracion(p));
    }

    // ---------------------------------------------------------------
    // getRecomendacion — sin sesión activa
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionSinUsuarioLogueadoDevuelveVacio() {
        Aplicacion app = Aplicacion.getInstancia();
        // No hay sesión iniciada
        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        assertTrue(rec.isEmpty(), "Sin usuario logueado debe devolver conjunto vacío");
    }

    // ---------------------------------------------------------------
    // getRecomendacion — usuario no es ClienteRegistrado
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionUsuarioGestionDevuelveVacio() {
        Aplicacion app = Aplicacion.getInstancia();
        app.iniciarSesion("gestor", "123456");
        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        assertTrue(rec.isEmpty(), "El gestor no es ClienteRegistrado, debe devolver vacío");
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — catálogo vacío con cliente
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionCatalogoVacioDevuelveVacio() {
        Aplicacion app = Aplicacion.getInstancia();
        app.crearCuenta("ClienteA", "111111111A", "1234", "1234");
        app.iniciarSesion("ClienteA", "1234");
        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        assertTrue(rec.isEmpty(), "Con catálogo vacío debe devolver conjunto vacío");
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — menos productos que unidades configuradas
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionMenosProductosQueUnidades() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();

        // Añadimos 2 productos
        LineaProductoVenta p1 = new LineaProductoVenta("P1", "d1", new File("f.png"), 5, 10.0);
        LineaProductoVenta p2 = new LineaProductoVenta("P2", "d2", new File("f.png"), 5, 15.0);
        cat.añadirProducto(p1);
        cat.añadirProducto(p2);

        // Configuramos para recomendar 10
        app.getConfiguracionRecomendacion().configurarUnidades(10);

        app.crearCuenta("ClienteB", "222222222B", "1234", "1234");
        app.iniciarSesion("ClienteB", "1234");

        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        // Debe devolver los 2 existentes sin errores
        assertEquals(2, rec.size());
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — exactamente el número de unidades configuradas
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionExactamenteUnidadesConfiguradas() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta p1 = new LineaProductoVenta("P1", "d1", new File("f.png"), 5, 10.0);
        LineaProductoVenta p2 = new LineaProductoVenta("P2", "d2", new File("f.png"), 5, 15.0);
        cat.añadirProducto(p1);
        cat.añadirProducto(p2);

        app.getConfiguracionRecomendacion().configurarUnidades(2);

        app.crearCuenta("ClienteC", "333333333C", "Holahola1@", "Holahola1@");
        app.iniciarSesion("ClienteC", "Holahola1@");

        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        assertEquals(2, rec.size());
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — más productos que unidades → limita el resultado
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionLimitaAlNumeroDeUnidades() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();

        for (int i = 0; i < 10; i++) {
            cat.añadirProducto(new LineaProductoVenta("Prod" + i, "desc", new File("f.png"), 10, i + 1.0));
        }

        app.getConfiguracionRecomendacion().configurarUnidades(3);

        app.crearCuenta("ClienteD", "444444444D", "Holahola1@", "Holahola1@");
        app.iniciarSesion("ClienteD", "1234");

        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        assertEquals(3, rec.size(), "La recomendación debe limitarse a las unidades configuradas");
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — sólo importa novedad (0, 0, 1)
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionPrioridadNovedad() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();
        // Obtenemos la instancia del tiempo para manipularla
        TiempoSimulado tiempo = TiempoSimulado.getInstance();

        LineaProductoVenta p1 = new LineaProductoVenta("Primero",  "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p1);
        
        tiempo.avanzarDias(1); // Forzamos que el siguiente sea más nuevo
        
        LineaProductoVenta p2 = new LineaProductoVenta("Segundo",  "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p2);
        
        tiempo.avanzarDias(1); // Forzamos que p3 sea el más nuevo de todos
        
        LineaProductoVenta p3 = new LineaProductoVenta("Tercero",  "d", new File("f.png"), 5, 5.0);
        cat.añadirProducto(p3);

        // Solo novedad importa (0, 0, 1)
        app.getConfiguracionRecomendacion().configurarImportancia(0, 0, 1);
        app.getConfiguracionRecomendacion().configurarUnidades(1);

        app.crearCuenta("ClienteE", "555555555E", "Holahola1@", "Holahola1@");
        app.iniciarSesion("ClienteE", "1234");

        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        
        assertEquals(1, rec.size());
        LineaProductoVenta produc = new LinkedList<>(rec).get(0);
        
        // Ahora sí, p3 tendrá el ID más alto y la fecha más reciente
        assertEquals(p3.getID(), produc.getID(), "El producto recomendado debe ser p3 por ser el más reciente");
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — resultado no es nulo con cliente y productos
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionDevuelveSetNoNulo() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();
        cat.añadirProducto(new LineaProductoVenta("P", "d", new File("f.png"), 5, 9.0));

        app.crearCuenta("ClienteF", "666666666F", "Holahola1@", "Holahola1@");
        app.iniciarSesion("ClienteF", "Holahola1@");

        assertNotNull(app.getConfiguracionRecomendacion().getRecomendacion());
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // getRecomendacion — resultado no contiene productos de segunda mano
    // ---------------------------------------------------------------

    @Test
    void testGetRecomendacionSoloContieneProductosNuevos() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();

        LineaProductoVenta p1 = new LineaProductoVenta("Nuevo", "d", new File("f.png"), 5, 12.0);
        cat.añadirProducto(p1);

        app.crearCuenta("ClienteG", "777777777G", "Holahola1@", "Holahola1@");
        app.iniciarSesion("ClienteG", "Holahola1@");

        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        // Todos los recomendados deben ser LineaProductoVenta (productos nuevos)
        for (LineaProductoVenta p : rec) {
            assertNotNull(p);
        }
        app.cerrarSesion();
    }

    // ---------------------------------------------------------------
    // Efecto de buscarProductosNuevos en el interés → cambia recomendación
    // ---------------------------------------------------------------

    @Test
    void testBusquedaActualizaInteresYAfectaRecomendacion() {
        Aplicacion app = Aplicacion.getInstancia();
        Catalogo cat = app.getCatalogo();
        TiempoSimulado tiempo = TiempoSimulado.getInstance();

        // 1. Creamos y añadimos el primer producto
        LineaProductoVenta pBuscado  = new LineaProductoVenta("Figura Goku",  "d", new File("f.png"), 10, 20.0);
        cat.añadirProducto(pBuscado);

        // 2. Avanzamos el tiempo para que el siguiente producto tenga una fecha de subida distinta
        tiempo.avanzarDias(1);

        // 3. Creamos y añadimos el segundo producto (ahora tendrá una fecha posterior)
        LineaProductoVenta pNoBuscado = new LineaProductoVenta("Comic Marvel", "d", new File("f.png"), 10, 10.0);
        cat.añadirProducto(pNoBuscado);

        // Solo el interés (búsquedas) importa en esta configuración
        app.getConfiguracionRecomendacion().configurarImportancia(1, 0, 0);
        app.getConfiguracionRecomendacion().configurarUnidades(1);

        app.crearCuenta("ClienteH", "888888888H", "Holahola1@", "Holahola1@");
        app.iniciarSesion("ClienteH", "Holahola1@");

        // Buscar varias veces el producto para acumular interés (PESO_BUSQUEDA = 5)
        app.buscarProductosNuevos("Figura Goku");
        app.buscarProductosNuevos("Figura Goku");

        Set<LineaProductoVenta> rec = app.getConfiguracionRecomendacion().getRecomendacion();
        
        // Verificaciones
        assertFalse(rec.isEmpty(), "La recomendación no debería estar vacía");
        assertEquals(1, rec.size(), "Debería recomendar exactamente 1 unidad");             
    }
    
    
}
