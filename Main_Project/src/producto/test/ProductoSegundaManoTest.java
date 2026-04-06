package producto.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import producto.DatosValidacion;
import producto.EstadoConservacion;
import producto.ProductoSegundaMano;
import solicitud.Oferta;
import tiempo.DateTimeSimulado;
import usuario.ClienteRegistrado;

class ProductoSegundaManoTest {

	@Test
	void testValidarProducto() {
		ProductoSegundaMano p = new ProductoSegundaMano("Pelota", "Una maravillosa pelota", new File("/pelota.jpg"));
		p.validarProducto(9.99, EstadoConservacion.MUY_BUENO);
		DatosValidacion d1 = new DatosValidacion(9.99, EstadoConservacion.MUY_BUENO);
		DatosValidacion d2 = p.getDatosValidacion();
		assertTrue(d1.getEstadoConservacion() == d2.getEstadoConservacion()
				&& d1.getPrecioEstimadoProducto() == d2.getPrecioEstimadoProducto());
	}

	@Test
	void testAddOfertaRecibida() {
		ProductoSegundaMano p = new ProductoSegundaMano("Pelota", "Una maravillosa pelota", new File("/pelota.jpg"));
		Set<ProductoSegundaMano> productosOfertados = new HashSet<>();
		Set<ProductoSegundaMano> productosSolicitados = new HashSet<>();
		p.validarProducto(9.99, EstadoConservacion.MUY_BUENO);
		Oferta o = new Oferta(new DateTimeSimulado(), new ClienteRegistrado("Matt", "00000000A", "Molamola"),
				new ClienteRegistrado("Dani", "11111111B", "Holahola"), productosOfertados, productosSolicitados);
		p.addOfertaRecibida(o);
		Oferta o2 = new Oferta(new DateTimeSimulado(), new ClienteRegistrado("Matt", "00000000A", "Molamola"),
				new ClienteRegistrado("Dani", "11111111B", "Holahola"), productosOfertados, productosSolicitados);
		assertThrows(IllegalStateException.class, () -> {p.addOfertaRecibida(o2);});

	}

	@Test
	void testAddOfertaEnviada() {
		ProductoSegundaMano p = new ProductoSegundaMano("Pelota", "Una maravillosa pelota", new File("/pelota.jpg"));
		Set<ProductoSegundaMano> productosOfertados = new HashSet<>();
		Set<ProductoSegundaMano> productosSolicitados = new HashSet<>();
		p.validarProducto(9.99, EstadoConservacion.MUY_BUENO);

		Oferta o = new Oferta(new DateTimeSimulado(), new ClienteRegistrado("Matt", "00000000A", "Molamola"),
				new ClienteRegistrado("Dani", "11111111B", "Holahola"), productosOfertados, productosSolicitados);
		p.addOfertaEnviada(o);
		Oferta o2 = new Oferta(new DateTimeSimulado(), new ClienteRegistrado("Matt", "00000000A", "Molamola"),
				new ClienteRegistrado("Dani", "11111111B", "Holahola"), productosOfertados, productosSolicitados);
		assertThrows(IllegalStateException.class, () -> {p.addOfertaEnviada(o2);});
	}
}
