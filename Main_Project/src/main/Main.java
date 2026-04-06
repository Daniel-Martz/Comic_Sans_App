package main;

import usuario.*;
import aplicacion.*;
import notificacion.*;
import solicitud.*;
import producto.*;
import descuento.*;
import filtro.*;
import tiempo.*;
import categoria.*;

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		ClienteRegistrado matteo, rodrigo;
		Empleado federico = null;
		Usuario usuarioActual;
		Gestor gestor;
		Aplicacion app = Aplicacion.getInstancia();

		// *********************************************************************************************************
		// PRUEBA DE LA GESTION DE CUENTAS

		app.iniciarSesion("gestor", "123456");

		app.cambiarContraseña("gestor", "123456", "@soyElGestor1234!");
		app.cerrarSesion();
		app.iniciarSesion("gestor", "@soyElGestor1234!");

		usuarioActual = app.getUsuarioActual();
		if (usuarioActual instanceof Gestor) {
			gestor = (Gestor) usuarioActual;
		} else {
			return;
		}

		federico = gestor.crearEmpleado("Federico", "132435468B");
		gestor.añadirPermiso(federico, Permiso.VALIDACIONES);
		gestor.añadirPermiso(federico, Permiso.INTERCAMBIOS);

		app.cerrarSesion();

		matteo = app.crearCuenta("Matteo", "123456789B", "1111");
		rodrigo = app.crearCuenta("Rodrigo", "123456789C", "2222");

		// *********************************************************************************************************
		// PRUEBA DE SOLICITUD VALIDACION Y SOLICITUD INTETRCAMBIO

		app.iniciarSesion("Matteo", "1111");
		matteo.añadirProductoACarteraDeIntercambio("Peluche de perro", "Es un peluche muy bonito y suavecito", null);
		app.cerrarSesion();

		app.iniciarSesion("Rodrigo", "2222");
		rodrigo.añadirProductoACarteraDeIntercambio("Camion de bomberos", "Un camion con 4 ruedas, es increible!",
				null);
		app.cerrarSesion();

		app.iniciarSesion("Federico", "123456");
		List<SolicitudValidacion> solicitudes = Aplicacion.getInstancia().getGestorSolicitud().getValidaciones();
		federico.validarProducto(solicitudes.get(0), 2.4, 10, EstadoConservacion.MUY_BUENO);
		federico.validarProducto(solicitudes.get(1), 3, 10, EstadoConservacion.MUY_USADO);
		app.cerrarSesion();

		app.iniciarSesion("Rodrigo", "2222");
		List<ProductoSegundaMano> productosRodrigo = new ArrayList<>(rodrigo.getCartera().getProductos());
		rodrigo.pagarValidacion(productosRodrigo.get(0).getSolicitudValidacion(), "1234567890123456", "123",
				new DateTimeSimulado());
		app.cerrarSesion();

		// Matteo inicia sesion para realizar una oferta a Rodrigo
		app.iniciarSesion("Matteo", "1111");
		List<ProductoSegundaMano> productosMatteo = new ArrayList<>(matteo.getCartera().getProductos());
		matteo.pagarValidacion(productosMatteo.get(0).getSolicitudValidacion(), "1234567890123456", "123",
				new DateTimeSimulado());

		// Sets que usaremos para realizar la oferta
		Set<ProductoSegundaMano> sMatteo = new HashSet<>(productosMatteo);
		Set<ProductoSegundaMano> sRodrigo = new HashSet<>(productosRodrigo);
		matteo.realizarOferta(sMatteo, sRodrigo, rodrigo);
		app.cerrarSesion();

		// Rodrigo inicia sesion para aceptar la oferta
		app.iniciarSesion("Rodrigo", "2222");
		List<Oferta> ofertasRecibidasRodrigo = rodrigo.getOfertasRecibidas();
		rodrigo.aceptarOferta(ofertasRecibidasRodrigo.get(0));
		app.cerrarSesion();

		// Matteo inicia sesion para acceder a sus notificaciones
		app.iniciarSesion("Matteo", "1111");
		List<NotificacionCliente> notifsMatteo = matteo.getNotificaciones();
		NotificacionIntercambio notifIntercambioMatteo = null;
		for (NotificacionCliente notif : notifsMatteo) {
			if (notif instanceof NotificacionIntercambio) {
				notifIntercambioMatteo = (NotificacionIntercambio) notif;
				break;
			}
		}
		app.cerrarSesion();

		// Rodrigo inicia sesión para acceder a sus notificaciones
		app.iniciarSesion("Rodrigo", "2222");
		List<NotificacionCliente> notifsRodrigo = rodrigo.getNotificaciones();
		NotificacionIntercambio notifIntercambioRodrigo = null;
		for (NotificacionCliente notif : notifsRodrigo) {
			if (notif instanceof NotificacionIntercambio) {
				notifIntercambioRodrigo = (NotificacionIntercambio) notif;
				break;
			}
		}
		app.cerrarSesion();

		String codigoMatteo = notifIntercambioMatteo.getCodigoIntercambio();
		String codigoRodrigo = notifIntercambioRodrigo.getCodigoIntercambio();
		// Federico inicia sesion para aprobar el intercambio
		app.iniciarSesion("Federico", "123456");
		List<SolicitudIntercambio> listaIntercambios = GestorSolicitudes.getInstancia().getIntercambios();
		SolicitudIntercambio sol = listaIntercambios.get(0);
		federico.aprobarIntercambio(sol, codigoMatteo, codigoRodrigo);
		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE SOLICITUD PEDIDO

		// Introducimos productos nuevos en la aplicación
		Catalogo cat = app.getCatalogo();
		app.iniciarSesion("gestor", "@soyElGestor1234!");
		gestor.añadirProducto("Comic de Spiderman Adventures, Volumen 3",
				"Un cómic de auténtico colleccionista, preservado en el envoltorio original. Parte de la colección Spiderman Adventures tan valorada por los fans del personaje.",
				new File("/images/SpidermanAdventures2.jpg"), 20, 19.99);
		gestor.añadirProducto("Comic de Spiderman Chronicles, Volumen 5",
				"Un cómic de auténtico colleccionista, preservado en el envoltorio original. Parte de la colección Spiderman Adventures tan valorada por los fans del personaje.",
				new File("/images/SpidermanChronicles5.jpg"), 20, 19.99);

		// Añadimos algunas categorías adicionales
		cat.añadirCategoria(new Categoria("Fantasía"));
		cat.añadirCategoria(new Categoria("Thriller"));
		cat.añadirCategoria(new Categoria("Anime"));
		cat.añadirCategoria(new Categoria("Estrategia"));

		// Añadimos productos a través de un fichero, así como sus categorías
		try {
			gestor.añadirProductosDesdeFichero(new File("txt/cargarDesdeFichero1.txt"));
		} catch (IOException excepcionFichero) {
			System.out.println(excepcionFichero);
		}

		System.out.println("Categorías de la tienda");
		System.out.println(cat.getCategoriasTienda());

		// Modificamos una categoria
		Categoria catThriller = cat.buscarCategoriaPorNombre("Thriller");
		cat.modificarCategoria(catThriller, "Suspense");

		System.out.println("Categorías (con modificación de la tienda");
		System.out.println(cat.getCategoriasTienda());

		// Modificamos los datos de un producto
		List<LineaProductoVenta> prod = cat.obtenerProductosNuevosGestion("Comic de Spiderman Adventures");
		System.out.println(
				"Lista de productos obtenidos como resultado al buscar con el prompt: Comic de Spiderman Adventures");
		// Modificamos el producto que más se ajuste al resultado, aumentando su stock
		cat.modificarProducto(prod.get(0), "Comic de Spiderman Adventures, Volumen 3",
				"Un cómic de auténtico colleccionista, preservado en el envoltorio original. Parte de la colección Spiderman Adventures tan valorada por los fans del personaje.",
				new File("/images/SpidermanAdventures2.jpg"), 30, 19.99);

		// Creamos un pack de comics de Spiderman
		List<LineaProductoVenta> comicsSpidermanProds = cat.obtenerProductosNuevosGestion("Comic Spiderman");
		Pack comicsSpidermanPack = new Pack("Pack de comics de Spiderman",
				"Dos comics sumamente valiosos y valorados dentro de la comunidad de amantes de Spiderman",
				new File("/dosComicsSpiderman.jpg"), 5, 34.99);
		// Añadimos primero 10 comics a cada pack, lo que producirá un error por
		// insuficiencia de stock
		try {
			comicsSpidermanPack.añadirProductoAPack(prod.get(0), 10);
		} catch (IllegalArgumentException errorStock) {
			System.out.println(errorStock);
		}
		for (LineaProductoVenta p : prod) {
			comicsSpidermanPack.añadirProductoAPack(p, 1);
		}

		System.out.println("Los productos de la tienda son:");
		System.out.println(cat.getProductosNuevos());

		cat.cambiarFiltroVenta(new FiltroVentaCliente(true, 0, 5, 0, 20, true, false));
		System.out.println("Los productos de la tienda filtrados con puntuación entre 0 y 5, y precio entre 0 y 20 son:");
		System.out.println(cat.obtenerProductosNuevosFiltrados("Comic"));
		
		//Vamos a ajustar el sistema de recomendacion a un único producto y modificamos importancia para darle prioridad a la busqueda
		gestor.configurarUnidadesRecomendadas(1);
		gestor.configurarImportancia(1, 0, 0);
		
		//Cerramos sesion y iniciamos con un cliente. Vamos a hacer busquedas y compras para ver que funciona el sistema de recomendacion
		app.cerrarSesion();
		app.iniciarSesion("Rodrigo", "2222");
		//app.buscarProductosNuevos()
		
		
	}
}
