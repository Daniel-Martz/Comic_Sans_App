package filtro.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import categoria.Categoria;
import filtro.*;
import producto.EstadoConservacion;

class FiltroTest {

	private FiltroVentaCliente filtroVentaCliente;
	private FiltroIntercambio filtroIntercambio;
	private FiltroVenta filtroVenta;
	private Categoria cat1;
	private Categoria cat2;

	@BeforeEach
	void setUp() {
		filtroVentaCliente = new FiltroVentaCliente(true, 0.0, 5.0, 0.0, 100.0, false, false);
		filtroIntercambio = new FiltroIntercambio(true, 0.0, 50.0);
		filtroVenta = new FiltroVenta(true);
		cat1 = new Categoria("Comics");
		cat2 = new Categoria("Figuras");
	}

	// ===================================================
	// Tests de Filtro (base)
	// ===================================================

	@Test
	void testOrdenAscendente() {
		assertTrue(filtroVenta.isOrdenAscendente());
	}

	@Test
	void testOrdenDescendente() {
		FiltroVenta filtroDesc = new FiltroVenta(false);
		assertFalse(filtroDesc.isOrdenAscendente());
	}

	// ===================================================
	// Tests de FiltroVenta
	// ===================================================

	@Test
	void testAñadirCategoria() {
		filtroVenta.añadirCategoria(cat1);
		assertTrue(filtroVenta.getCategoriasFiltradas().contains(cat1));
	}

	@Test
	void testEliminarCategoria() {
		filtroVenta.añadirCategoria(cat1);
		filtroVenta.eliminarCategoria(cat1);
		assertFalse(filtroVenta.getCategoriasFiltradas().contains(cat1));
	}

	@Test
	void testAñadirVariasCategorias() {
		filtroVenta.añadirCategoria(cat1);
		filtroVenta.añadirCategoria(cat2);
		assertEquals(2, filtroVenta.getCategoriasFiltradas().size());
	}

	@Test
	void testAñadirCategoriaDuplicada() {
		filtroVenta.añadirCategoria(cat1);
		filtroVenta.añadirCategoria(cat1); // duplicado
		assertEquals(1, filtroVenta.getCategoriasFiltradas().size()); // set no permite duplicados
	}

	@Test
	void testAñadirTipoProducto() {
		filtroVenta.añadirTipoProducto(TipoProducto.COMIC);
		assertTrue(filtroVenta.getTipoFiltrado().contains(TipoProducto.COMIC));
	}

	@Test
	void testEliminarTipoProducto() {
		filtroVenta.añadirTipoProducto(TipoProducto.COMIC);
		filtroVenta.eliminarTipoProducto(TipoProducto.COMIC);
		assertFalse(filtroVenta.getTipoFiltrado().contains(TipoProducto.COMIC));
	}

	@Test
	void testAñadirTodosLosTiposProducto() {
		filtroVenta.añadirTipoProducto(TipoProducto.COMIC);
		filtroVenta.añadirTipoProducto(TipoProducto.FIGURA);
		filtroVenta.añadirTipoProducto(TipoProducto.JUEGO_DE_MESA);
		assertEquals(3, filtroVenta.getTipoFiltrado().size());
	}

	// ===================================================
	// Tests de FiltroVentaCliente
	// ===================================================

	@Test
	void testGetPuntuacionMin() {
		assertEquals(0.0, filtroVentaCliente.getPuntuacionMin());
	}

	@Test
	void testGetPuntuacionMax() {
		assertEquals(5.0, filtroVentaCliente.getPuntuacionMax());
	}

	@Test
	void testGetPrecioMin() {
		assertEquals(0.0, filtroVentaCliente.getPrecioMin());
	}

	@Test
	void testGetPrecioMax() {
		assertEquals(100.0, filtroVentaCliente.getPrecioMax());
	}

	@Test
	void testSetPuntuacionMin() {
		filtroVentaCliente.setPuntuacionMin(1.0);
		assertEquals(1.0, filtroVentaCliente.getPuntuacionMin());
	}

	@Test
	void testSetPuntuacionMax() {
		filtroVentaCliente.setPuntuacionMax(4.5);
		assertEquals(4.5, filtroVentaCliente.getPuntuacionMax());
	}

	@Test
	void testSetPrecioMin() {
		filtroVentaCliente.setPrecioMin(10.0);
		assertEquals(10.0, filtroVentaCliente.getPrecioMin());
	}

	@Test
	void testSetPrecioMax() {
		filtroVentaCliente.setPrecioMax(200.0);
		assertEquals(200.0, filtroVentaCliente.getPrecioMax());
	}

	@Test
	void testOrdenarPorPrecioFalseInicial() {
		assertFalse(filtroVentaCliente.isOrdenarPorPrecio());
	}

	@Test
	void testSetOrdenarPorPrecio() {
		filtroVentaCliente.setOrdenarPorPrecio(true);
		assertTrue(filtroVentaCliente.isOrdenarPorPrecio());
	}

	@Test
	void testOrdenarPorPuntuacionFalseInicial() {
		assertFalse(filtroVentaCliente.isOrdenarPorPuntuacion());
	}

	@Test
	void testSetOrdenarPorPuntuacion() {
		filtroVentaCliente.setOrdenarPorPuntuacion(true);
		assertTrue(filtroVentaCliente.isOrdenarPorPuntuacion());
	}

	@Test
	void testAñadirTipoDescuento() {
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.PRECIO);
		assertTrue(filtroVentaCliente.getDescuentoFiltrado().contains(TipoDescuento.PRECIO));
	}

	@Test
	void testEliminarTipoDescuento() {
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.PRECIO);
		filtroVentaCliente.eliminarTipodescuento(TipoDescuento.PRECIO);
		assertFalse(filtroVentaCliente.getDescuentoFiltrado().contains(TipoDescuento.PRECIO));
	}

	@Test
	void testAñadirTodosLosTiposDescuento() {
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.CANTIDAD);
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.PRECIO);
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.REBAJA);
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.REGALO);
		assertEquals(4, filtroVentaCliente.getDescuentoFiltrado().size());
	}

	@Test
	void testDescuentoDuplicadoNoSeAñade() {
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.PRECIO);
		filtroVentaCliente.añadirTipodescuento(TipoDescuento.PRECIO);
		assertEquals(1, filtroVentaCliente.getDescuentoFiltrado().size());
	}

	// ===================================================
	// Tests de FiltroIntercambio
	// ===================================================

	@Test
	void testGetValorMin() {
		assertEquals(0.0, filtroIntercambio.getValorMin());
	}

	@Test
	void testGetValorMax() {
		assertEquals(50.0, filtroIntercambio.getValorMax());
	}

	@Test
	void testAñadirEstadoConservacion() {
		filtroIntercambio.añadirEstadoConservacion(EstadoConservacion.MUY_BUENO);
		assertTrue(filtroIntercambio.getEstadosFiltrados().contains(EstadoConservacion.MUY_BUENO));
	}

	@Test
	void testEliminarEstadoConservacion() {
		filtroIntercambio.añadirEstadoConservacion(EstadoConservacion.MUY_BUENO);
		filtroIntercambio.eliminarEstadoConservacion(EstadoConservacion.MUY_BUENO);
		assertFalse(filtroIntercambio.getEstadosFiltrados().contains(EstadoConservacion.MUY_BUENO));
	}

	@Test
	void testAñadirTodosLosEstados() {
		for (EstadoConservacion estado : EstadoConservacion.values()) {
			filtroIntercambio.añadirEstadoConservacion(estado);
		}
		assertEquals(EstadoConservacion.values().length, filtroIntercambio.getEstadosFiltrados().size());
	}

	@Test
	void testEstadoDuplicadoNoSeAñade() {
		filtroIntercambio.añadirEstadoConservacion(EstadoConservacion.MUY_BUENO);
		filtroIntercambio.añadirEstadoConservacion(EstadoConservacion.MUY_BUENO);
		assertEquals(1, filtroIntercambio.getEstadosFiltrados().size());
	}

	@Test
	void testFiltroIntercambioOrdenAscendente() {
		assertTrue(filtroIntercambio.isOrdenAscendente());
	}

	@Test
	void testFiltroIntercambioOrdenDescendente() {
		FiltroIntercambio filtroDesc = new FiltroIntercambio(false, 0.0, 50.0);
		assertFalse(filtroDesc.isOrdenAscendente());
	}
}