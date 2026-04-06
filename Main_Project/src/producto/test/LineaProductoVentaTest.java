package producto.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import categoria.Categoria;
import producto.*;
import tiempo.DateTimeSimulado;

import java.io.File;
import java.util.*;

class LineaProductoVentaTest {
	LineaProductoVenta l = new LineaProductoVenta("ProductoPrueba", "Un producto muy bonito",
			new File("/path/image.png"), 5, 10.50);

	@Test
	void testAñadirCategoriasDiferentes() {
		Categoria c1 = new Categoria("Romance");
		Categoria c2 = new Categoria("Accion");
		Categoria c3 = new Categoria("Aventuras");
		l.añadirCategoria(c1);
		l.añadirCategoria(c2);
		l.añadirCategoria(c3);
		Set<Categoria> cats = l.getCategorias();
		assertTrue(cats.contains(c1) && cats.contains(c2) && cats.contains(c3));
	}

	@Test
	void testAñadirMismaCateogira() {
		Categoria c1 = new Categoria("Romance");
		l.añadirCategoria(c1);
		l.añadirCategoria(c1);
		Set<Categoria> cats = l.getCategorias();
		assertTrue(cats.size() == 1);
	}

	@Test
	void testEliminarCategoria() {
		Categoria c1 = new Categoria("Romance");
		Categoria c2 = new Categoria("Accion");
		Categoria c3 = new Categoria("Aventuras");
		l.añadirCategoria(c1);
		l.añadirCategoria(c2);
		l.añadirCategoria(c3);
		Set<Categoria> cats = l.getCategorias();
		l.eliminarCategoria(c2);
		assertTrue(cats.contains(c1) && !cats.contains(c2) && cats.contains(c3));

	}

	@Test
	void testAñadirReseña() {
		Reseña r = new Reseña("Muy buen producto", 5, new DateTimeSimulado(), l);
		l.añadirReseña(r);
		assertTrue(l.getReseña().contains(r));
	}

	@Test
	void testEliminarReseña() {
		Reseña r = new Reseña("Muy buen producto", 5, new DateTimeSimulado(), l);
		l.añadirReseña(r);
		l.eliminarReseña(r);
		assertFalse(l.getReseña().contains(r));
	}

	@Test
	void testObtenerPuntuacionMedia() {
		Reseña r1 = new Reseña("Muy buen producto", 5, new DateTimeSimulado(), l);
		l.añadirReseña(r1);
		Reseña r2 = new Reseña("Muy mal producto", 0, new DateTimeSimulado(), l);
		l.añadirReseña(r2);
		System.out.print(l.obtenerPuntuacionMedia());
		assertTrue(l.obtenerPuntuacionMedia() == 2.5);
		l.eliminarReseña(r1);
		l.eliminarReseña(r2);
		System.out.print(l.obtenerPuntuacionMedia());
		assertTrue(l.obtenerPuntuacionMedia() == 0.0);
	}

}
