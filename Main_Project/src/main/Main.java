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

		System.out.println("Categorías (con modificación) de la tienda");
		System.out.println(cat.getCategoriasTienda());

		// Modificamos los datos de un producto
		List<LineaProductoVenta> prod = cat.obtenerProductosNuevosGestion("Comic de Spiderman Adventures");
		System.out.println(
				"Lista de productos obtenidos como resultado al buscar con el prompt: Comic de Spiderman Adventures");

		for (LineaProductoVenta p : prod) {
			System.out.println(p);
			System.out.println("----------------------------------------------------");
		}
		// Modificamos el producto que más se ajuste al resultado, aumentando su stock
		LineaProductoVenta productoAux = prod.get(0);
		cat.modificarProducto(prod.get(0), productoAux.getNombre(), productoAux.getDescripcion(), productoAux.getFoto(),
				30, 19.99);

		// Creamos un pack de comics de Spiderman
		List<LineaProductoVenta> comicsSpidermanProds = cat.obtenerProductosNuevosGestion("Comic Spiderman");
		
		Pack comicsSpidermanPack = gestor.añadirPack("Pack de comics de Spiderman",
					"Dos comics sumamente valiosos y valorados dentro de la comunidad de amantes de Spiderman",
					new File("/dosComicsSpiderman.jpg"), 5, 34.99, new HashMap<>());

		
		// Añadimos primero 10 comics a cada pack, lo que producirá un error por
		// insuficiencia de stock
		System.out.println(comicsSpidermanPack);
		try {
			comicsSpidermanPack.añadirProductoAPack(prod.get(0), 10);
		} catch (IllegalArgumentException errorStock) {
			System.out.println(errorStock);
		}
		for (LineaProductoVenta p : prod) {
			comicsSpidermanPack.añadirProductoAPack(p, 1);
		}
		System.out.println(comicsSpidermanPack);

		System.out.println("\n====================================================");
		System.out.println("       PRODUCTOS DISPONIBLES EN LA TIENDA");
		System.out.println("====================================================");
		for (LineaProductoVenta p : cat.getProductosNuevos()) {
			System.out.println(p);
			System.out.println("----------------------------------------------------");
		}
		cat.cambiarFiltroVenta(true, null, null, 0, 5, 0, 20, true, false, null);

		List<LineaProductoVenta> filtrados = cat.obtenerProductosNuevosFiltrados("Comic");
		System.out.println("\n>>> PRODUCTOS FILTRADOS (Puntuación: 0-5 | Precio: 0-20€ | Texto: 'Comic')");
		if (filtrados.isEmpty()) {
			System.out.println("No se han encontrado productos que coincidan con los criterios.");
		} else {
			for (LineaProductoVenta p : filtrados) {
				System.out.println(p);
				System.out.println("----------------------------------------------------");
			}
		}

		// Veamos una primera demostración del sistema de recomendación, si un cliente
		// busca un producto, aumenta el interés en este
		System.out.println("\n[Configurando sistema de recomendación: 1 unidad, prioridad Interés]");
		gestor.configurarUnidadesRecomendadas(1);
		gestor.configurarImportancia(1, 0, 0);

		System.out.println("\n[Cerrando sesión de Gestor...]");
		app.cerrarSesion();

		// Comprobemos que el interés de un cliente funciona correctamente tras las
		// búsquedas de productos
		System.out.println("[Iniciando sesión como Rodrigo...]");
		app.iniciarSesion("Rodrigo", "2222");

		System.out.println("\nRodrigo busca: 'Comic Spiderman'");
		app.buscarProductosNuevos("Comic de Spiderman Chronicles, Volumen 5");

		System.out.println("\nProducto recomendado a Rodrigo: ");
		for (LineaProductoVenta p : app.getConfiguracionRecomendacion().getRecomendacion()) {
			System.out.println(p);
		}
		
		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE SOLICITUD PEDIDO

		// Rodrigo inicia sesión para hacer un pedido
		System.out.println("\n====================================================");
		System.out.println("           PRUEBA DE PEDIDO Y PAGO");
		System.out.println("====================================================");

		app.iniciarSesion("Rodrigo", "2222");

		// Rodrigo añade productos al carrito
		List<LineaProductoVenta> productosDisponibles = new ArrayList<>(cat.getProductosNuevos());
		LineaProductoVenta productoParaPedido1 = productosDisponibles.get(0);
		LineaProductoVenta productoParaPedido2 = productosDisponibles.get(1);

		rodrigo.añadirProductoACarrito(productoParaPedido1, 1);
		rodrigo.añadirProductoACarrito(productoParaPedido2, 2);

		System.out.println("\nCarrito de Rodrigo:");
		System.out.println(rodrigo.getCarrito());

		// Rodrigo crea el pedido a partir del carrito
		SolicitudPedido pedidoRodrigo = rodrigo.realizarPedido();
		System.out.println("\nPedido creado:");
		System.out.println(pedidoRodrigo);
		System.out.println("Estado del pedido: " + pedidoRodrigo.getEstado());

		// Rodrigo paga el pedido
		rodrigo.pagarPedido(pedidoRodrigo, "1234567890123456", "123", new DateTimeSimulado());
		System.out.println("\nEstado del pedido tras el pago: " + pedidoRodrigo.getEstado());

		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE VALIDACIÓN DE PEDIDO POR UN EMPLEADO CON PERMISOS

		System.out.println("\n====================================================");
		System.out.println("       PRUEBA DE VALIDACIÓN DE PEDIDO");
		System.out.println("====================================================");

		// Federico inicia sesión para validar el pedido
		app.iniciarSesion("Federico", "123456");

		List<SolicitudPedido> pedidosPendientes = GestorSolicitudes.getInstancia().getPedidos();
		System.out.println("\nPedidos pendientes de validar: " + pedidosPendientes.size());

		if (!pedidosPendientes.isEmpty()) {
			SolicitudPedido pedidoAValidar = pedidosPendientes.get(0);
			System.out.println("\nPedido a validar:");
			System.out.println(pedidoAValidar);
			System.out.println("Estado antes de validar: " + pedidoAValidar.getEstado());

			// Federico marca el pedido como listo para recoger
			federico.actualizarEstadoPedido(pedidoAValidar, EstadoPedido.LISTO_PARA_RECOGER);
			System.out.println("Estado tras validación: " + pedidoAValidar.getEstado());
		} else {
			System.out.println("No hay pedidos pendientes de validar.");
		}

		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE RECOGIDA DEL PEDIDO

		System.out.println("\n====================================================");
		System.out.println("          PRUEBA DE RECOGIDA DEL PEDIDO");
		System.out.println("====================================================");

		// Rodrigo inicia sesión para recoger el pedido
		app.iniciarSesion("Rodrigo", "2222");

		List<NotificacionCliente> notifsRodrigoPedido = rodrigo.getNotificaciones();
		System.out.println("\nNotificaciones de Rodrigo tras la validación:");
		for (NotificacionCliente notif : notifsRodrigoPedido) {
			System.out.println(notif);
		}

		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE ESTADÍSTICAS TRAS EL PEDIDO

		System.out.println("\n====================================================");
		System.out.println("         PRUEBA DE ESTADÍSTICAS");
		System.out.println("====================================================");

		app.iniciarSesion("gestor", "@soyElGestor1234!");

		File ficheroEstadisticas = new File("txt/estadisticas.txt");
		DateTimeSimulado inicio = new DateTimeSimulado(1, 1, 1, 0, 0, 0);
		DateTimeSimulado fin    = new DateTimeSimulado(1, 12, 30, 23, 59, 59);

		try {
			app.getSistemaEstadisticas().obtenerRecaudacionMensual(inicio, fin, ficheroEstadisticas);
			System.out.println("\nInforme de recaudación mensual generado en: " + ficheroEstadisticas.getAbsolutePath());

			File ficheroAmbito = new File("txt/estadisticas_ambito.txt");
			app.getSistemaEstadisticas().obtenerRecaudacionAmbito(inicio, fin, ficheroAmbito);
			System.out.println("Informe de recaudación por ámbito generado en: " + ficheroAmbito.getAbsolutePath());

			File ficheroProductos = new File("txt/estadisticas_productos.txt");
			app.getSistemaEstadisticas().obtenerVentasProductos(inicio, fin, false, ficheroProductos);
			System.out.println("Informe de ventas por producto generado en: " + ficheroProductos.getAbsolutePath());

			File ficheroClientes = new File("txt/estadisticas_clientes.txt");
			app.getSistemaEstadisticas().obtenerGastoClientes(inicio, fin, ficheroClientes);
			System.out.println("Informe de gasto por cliente generado en: " + ficheroClientes.getAbsolutePath());

		} catch (IOException e) {
			System.out.println("Error al generar estadísticas: " + e.getMessage());
		}

		app.cerrarSesion();

		System.out.println("\n====================================================");
		System.out.println("           FIN DE LA DEMOSTRACIÓN");
		System.out.println("====================================================");
	}

}
