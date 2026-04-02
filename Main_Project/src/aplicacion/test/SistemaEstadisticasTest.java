package aplicacion.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import aplicacion.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import producto.*;
import solicitud.*;
import usuario.*;
import tiempo.DateTimeSimulado;
import tiempo.TiempoSimulado;
import categoria.Categoria;

class SistemaEstadisticasTest {

	private SistemaEstadisticas sistema;
	private DateTimeSimulado periodoInicio;
	private DateTimeSimulado periodoFin;
	private DateTimeSimulado periodoFuera;
	private File fichero;

	// Productos
	private LineaProductoVenta producto1;
	private LineaProductoVenta producto2;
	private LineaProductoVenta producto3;

	// Clientes
	private ClienteRegistrado cliente1;
	private ClienteRegistrado cliente2;

	// Pedidos
	private SolicitudPedido pedido1;
	private SolicitudPedido pedido2;
	private SolicitudPedido pedido3;

	// Validaciones
	private SolicitudValidacion validacion1;
	private SolicitudValidacion validacion2;

	// Pagos
	private Pago pagoPedido1;
	private Pago pagoPedido2;
	private Pago pagoPedido3;
	private Pago pagoValidacion1;
	private Pago pagoValidacion2;

	@BeforeEach
	void setUp() throws Exception {
		// Reseteamos todos los Singletons para que no se duppliquen cosas
		resetSingleton(Aplicacion.class, "instancia");
		resetSingleton(Catalogo.class, "instancia");
		resetSingleton(SistemaEstadisticas.class, "instancia");
		resetSingleton(GestorSolicitudes.class, "instancia");
		resetSingleton(ConfiguracionRecomendacion.class, "instancia");
		resetSingleton(SistemaPago.class, "instancia");
		resetSingleton(TiempoSimulado.class, "instance");

		// Ahora sí inicializamos
		TiempoSimulado.getInstance().iniciar(1, 30, 12);

		// Obtenemos la instancia de la aplicación (Singleton)
		Aplicacion app = Aplicacion.getInstancia();
		sistema = app.getSistemaEstadisticas();

		// Fechas del período de prueba
		periodoInicio = new DateTimeSimulado(1, 1, 1, 0, 0, 0);
		periodoFin = new DateTimeSimulado(1, 6, 30, 0, 0, 0);
		periodoFuera = new DateTimeSimulado(1, 8, 1, 0, 0, 0); // fuera del período

		// Fichero temporal para los informes
		fichero = File.createTempFile("informe_test", ".txt");
		fichero.deleteOnExit();

		// ===== CATEGORÍAS — se registran en el catálogo =====
		Categoria catComics = new Categoria("Comics");
		Categoria catFiguras = new Categoria("Figuras");
		app.getCatalogo().añadirCategoria(catComics);
		app.getCatalogo().añadirCategoria(catFiguras);

		// ===== PRODUCTOS — se registran en el catálogo =====
		producto1 = new LineaProductoVenta("Winny", "Foquita de mar", new File("foquita.jpg"), 5, 12.99);
		producto2 = new LineaProductoVenta("Spiderman Comic #1", "Primer número", new File("spiderman.jpg"), 10, 8.99);
		producto3 = new LineaProductoVenta("Figura Goku", "Figura Dragon Ball", new File("goku.jpg"), 3, 24.99);

		producto1.añadirCategoria(catFiguras);
		producto2.añadirCategoria(catComics);
		producto3.añadirCategoria(catFiguras);

		catFiguras.añadirProductoACategoria(producto1);
		catComics.añadirProductoACategoria(producto2);
		catFiguras.añadirProductoACategoria(producto3);

		// añadirProducto actualiza rankings de novedad, valoración e interés
		app.getCatalogo().añadirProducto(producto1);
		app.getCatalogo().añadirProducto(producto2);
		app.getCatalogo().añadirProducto(producto3);

		// ===== CLIENTES — se registran en la aplicación =====
		app.crearCuenta("juanito", "12345678AB", "pass1234");
		app.crearCuenta("marieta", "87654321BC", "pass5678");

		// Obtenemos los clientes ya registrados
		List<ClienteRegistrado> clientes = app.getClientesRegistrados();
		cliente1 = clientes.get(0); // juanito
		cliente2 = clientes.get(1); // marieta

		// ===== PEDIDOS =====

		// Pedido 1 — cliente1, dentro del período (mes 1)
		Map<LineaProductoVenta, Integer> prods1 = new HashMap<>();
		prods1.put(producto1, 2); // 2 x 12.99 = 25.98
		prods1.put(producto2, 1); // 1 x 8.99 = 8.99
		pedido1 = new SolicitudPedido(cliente1, prods1);
		pagoPedido1 = new Pago(new DateTimeSimulado(1, 1, 15, 10, 0, 0), 34.97, pedido1);
		pedido1.añadirPagoPedido(pagoPedido1);
		GestorSolicitudes.getInstancia().añadirPedido(pedido1);

		// Pedido 2 — cliente2, dentro del período (mes 3)
		Map<LineaProductoVenta, Integer> prods2 = new HashMap<>();
		prods2.put(producto3, 1); // 1 x 24.99 = 24.99
		pedido2 = new SolicitudPedido(cliente2, prods2);
		pagoPedido2 = new Pago(new DateTimeSimulado(1, 3, 1, 10, 0, 0), 24.99, pedido2);
		pedido2.añadirPagoPedido(pagoPedido2);
		GestorSolicitudes.getInstancia().añadirPedido(pedido2);

		// Pedido 3 — cliente1, FUERA del período (mes 8)
		Map<LineaProductoVenta, Integer> prods3 = new HashMap<>();
		prods3.put(producto2, 3); // 3 x 8.99 = 26.97
		pedido3 = new SolicitudPedido(cliente1, prods3);
		pagoPedido3 = new Pago(periodoFuera, 26.97, pedido3);
		pedido3.añadirPagoPedido(pagoPedido3);
		GestorSolicitudes.getInstancia().añadirPedido(pedido3);

		// ===== VALIDACIONES =====

		// ProductoSegundaMano — su constructor ya crea la SolicitudValidacion
		// y la registra en GestorSolicitudes automáticamente
		ProductoSegundaMano psm1 = new ProductoSegundaMano("Catan usado", "Buen estado", new File("catan.jpg"));
		ProductoSegundaMano psm2 = new ProductoSegundaMano("Comic usado", "Páginas amarillas", new File("comic.jpg"));

		validacion1 = psm1.getSolicitudValidacion();
		validacion2 = psm2.getSolicitudValidacion();

		pagoValidacion1 = new Pago(new DateTimeSimulado(1, 2, 1, 10, 0, 0), 5.00, validacion1);
		pagoValidacion2 = new Pago(new DateTimeSimulado(1, 4, 1, 10, 0, 0), 5.00, validacion2);

		validacion1.añadirPagoValidacion(pagoValidacion1);
		validacion2.añadirPagoValidacion(pagoValidacion2);

		// ===== AÑADIMOS LOS PAGOS AL SISTEMA DE ESTADÍSTICAS =====
		sistema.añadirPago(pagoPedido1);
		sistema.añadirPago(pagoPedido2);
		sistema.añadirPago(pagoPedido3);
		sistema.añadirPago(pagoValidacion1);
		sistema.añadirPago(pagoValidacion2);
	}

	private void resetSingleton(Class<?> clazz, String nombreCampo) throws Exception {
		java.lang.reflect.Field field = clazz.getDeclaredField(nombreCampo);
		field.setAccessible(true);
		field.set(null, null);
	}

	@AfterEach
	void tearDown() {
		// Borramos el fichero temporal si existe
		if (fichero != null && fichero.exists()) {
			fichero.delete();
		}
	}

	// ===================================================
	// Tests de añadirPago
	// ===================================================

	@Test
	void testAñadirPagoNull() {
		assertThrows(IllegalArgumentException.class, () -> sistema.añadirPago(null));
	}

	@Test
	void testAñadirPagoValido() {
		Pago nuevoPago = new Pago(periodoInicio, 10.0, pedido1);
		assertDoesNotThrow(() -> sistema.añadirPago(nuevoPago));
	}

	// ===================================================
	// Tests de obtenerRecaudacionMensual
	// ===================================================

	@Test
	void testRecaudacionMensualPeriodoInicioNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionMensual(null, periodoFin, fichero));
	}

	@Test
	void testRecaudacionMensualPeriodoFinNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionMensual(periodoInicio, null, fichero));
	}

	@Test
	void testRecaudacionMensualFicheroNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionMensual(periodoInicio, periodoFin, null));
	}

	@Test
	void testRecaudacionMensualPeriodoInvalido() {
		// fin antes que inicio
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionMensual(periodoFin, periodoInicio, fichero));
	}

	@Test
	void testRecaudacionMensualCreaFichero() throws IOException {
		sistema.obtenerRecaudacionMensual(periodoInicio, periodoFin, fichero);
		assertTrue(fichero.exists());
		assertTrue(fichero.length() > 0);
	}

	@Test
	void testRecaudacionMensualContenidoCabecera() throws IOException {
		sistema.obtenerRecaudacionMensual(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("INFORME DE RECAUDACION MENSUAL"));
		assertTrue(contenido.contains("MES"));
		assertTrue(contenido.contains("RECAUDACION"));
		assertTrue(contenido.contains("TOTAL"));
	}

	@Test
	void testRecaudacionMensualMesesSinPagosAparecen() throws IOException {
		// El mes 5 no tiene pagos — debe aparecer con 0.0
		sistema.obtenerRecaudacionMensual(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("5/1")); // mes 5 año 1
		assertTrue(contenido.contains("0,00 €") || contenido.contains("0.00 €"));
	}

	@Test
	void testRecaudacionMensualNoCuentaPagosForaPeriodo() throws IOException {
		// El pedido3 es del mes 8, fuera del período 1-6
		// La recaudación total debe ser 34.97 + 24.99 + 5.00 + 5.00 = 69.96
		sistema.obtenerRecaudacionMensual(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("69,96 €") || contenido.contains("69.96 €"));
	}

	// ===================================================
	// Tests de obtenerRecaudacionAmbito
	// ===================================================

	@Test
	void testRecaudacionAmbitoPeriodoInicioNull() {
		assertThrows(IllegalArgumentException.class, () -> sistema.obtenerRecaudacionAmbito(null, periodoFin, fichero));
	}

	@Test
	void testRecaudacionAmbitoPeriodoFinNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionAmbito(periodoInicio, null, fichero));
	}

	@Test
	void testRecaudacionAmbitoFicheroNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionAmbito(periodoInicio, periodoFin, null));
	}

	@Test
	void testRecaudacionAmbitoPeriodoInvalido() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerRecaudacionAmbito(periodoFin, periodoInicio, fichero));
	}

	@Test
	void testRecaudacionAmbitoContieneAmbos() throws IOException {
		sistema.obtenerRecaudacionAmbito(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("Venta de productos"));
		assertTrue(contenido.contains("Validaciones"));
		assertTrue(contenido.contains("TOTAL"));
	}

	@Test
	void testRecaudacionAmbitoSeparaCorrectamente() throws IOException {
		// Ventas: 34.97 + 24.99 = 59.96
		// Validaciones: 5.00 + 5.00 = 10.00
		sistema.obtenerRecaudacionAmbito(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("59,96 €") || contenido.contains("59.96 €"));
		assertTrue(contenido.contains("10,00 €") || contenido.contains("10.00 €"));
	}

	// ===================================================
	// Tests de obtenerVentasProductos
	// ===================================================

	@Test
	void testVentasProductosPeriodoInicioNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerVentasProductos(null, periodoFin, false, fichero));
	}

	@Test
	void testVentasProductosPeriodoFinNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerVentasProductos(periodoInicio, null, false, fichero));
	}

	@Test
	void testVentasProductosFicheroNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerVentasProductos(periodoInicio, periodoFin, false, null));
	}

	@Test
	void testVentasProductosPeriodoInvalido() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerVentasProductos(periodoFin, periodoInicio, false, fichero));
	}

	@Test
	void testVentasProductosOrdenadoPorRecaudacion() throws IOException {
		sistema.obtenerVentasProductos(periodoInicio, periodoFin, false, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		// producto1 recaudó 25.98, producto3 recaudó 24.99, producto2 recaudó 8.99
		// Winny debe aparecer antes que Spiderman
		int posWinny = contenido.indexOf("Winny");
		int posSpiderman = contenido.indexOf("Spiderman");
		assertTrue(posWinny < posSpiderman);
	}

	@Test
	void testVentasProductosOrdenadoPorId() throws IOException {
		sistema.obtenerVentasProductos(periodoInicio, periodoFin, true, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		// Ordenado por ID — producto1 tiene ID menor que producto2
		int posProducto1 = contenido.indexOf(String.valueOf(producto1.getID()));
		int posProducto2 = contenido.indexOf(String.valueOf(producto2.getID()));
		assertTrue(posProducto1 < posProducto2);
	}

	@Test
	void testVentasProductosNoCuentaFueraPeriodo() throws IOException {
		// pedido3 (producto2 x3 = 26.97) está fuera del período
		// producto2 solo debería tener 8.99 (del pedido1)
		sistema.obtenerVentasProductos(periodoInicio, periodoFin, true, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("8,99 €") || contenido.contains("8.99 €"));
		assertFalse(contenido.contains("26,97 €") || contenido.contains("26.97 €"));
	}

	// ===================================================
	// Tests de obtenerGastoClientes
	// ===================================================

	@Test
	void testGastoClientesPeriodoInicioNull() {
		assertThrows(IllegalArgumentException.class, () -> sistema.obtenerGastoClientes(null, periodoFin, fichero));
	}

	@Test
	void testGastoClientesPeriodoFinNull() {
		assertThrows(IllegalArgumentException.class, () -> sistema.obtenerGastoClientes(periodoInicio, null, fichero));
	}

	@Test
	void testGastoClientesFicheroNull() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerGastoClientes(periodoInicio, periodoFin, null));
	}

	@Test
	void testGastoClientesPeriodoInvalido() {
		assertThrows(IllegalArgumentException.class,
				() -> sistema.obtenerGastoClientes(periodoFin, periodoInicio, fichero));
	}

	@Test
	void testGastoClientesContenidoCabecera() throws IOException {
		sistema.obtenerGastoClientes(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("INFORME DE RECAUDACION POR CLIENTES"));
		assertTrue(contenido.contains("DNI"));
		assertTrue(contenido.contains("NOMBRE DE USUARIO"));
		assertTrue(contenido.contains("TOTAL RECAUDADO EN EL PERIODO:"));
	}

	@Test
	void testGastoClientesAmbosClientes() throws IOException {
		sistema.obtenerGastoClientes(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertTrue(contenido.contains("juanito"));
		assertTrue(contenido.contains("marieta"));
	}

	@Test
	void testGastoClientesOrdenadoMayorMenor() throws IOException {
		// juanito gastó 34.97 (pedido1), marieta gastó 24.99 (pedido2)
		// juanito debe aparecer antes
		sistema.obtenerGastoClientes(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		int posJuanito = contenido.indexOf("juanito");
		int posMarieta = contenido.indexOf("marieta");
		assertTrue(posJuanito < posMarieta);
	}

	@Test
	void testGastoClientesNoCuentaPedidosFueraPeriodo() throws IOException {
		// pedido3 de juanito (26.97) está en mes 8, fuera del período
		// juanito solo debería aparecer con 34.97
		sistema.obtenerGastoClientes(periodoInicio, periodoFin, fichero);
		String contenido = new String(Files.readAllBytes(fichero.toPath()));
		assertFalse(contenido.contains("61,94 €") || contenido.contains("61.94 €")); // 34.97 + 26.97
		assertTrue(contenido.contains("34,97 €") || contenido.contains("34.97 €"));
	}
}
