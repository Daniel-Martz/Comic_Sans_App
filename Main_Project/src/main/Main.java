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
		System.out.println("\n====================================================");
		System.out.println("           PRUEBA DE GESTIÓN DE CUENTAS");
		System.out.println("====================================================");
		System.out.println("[->] Gestor inicia sesión y cambia su contraseña...");

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

		System.out.println("\n[->] Creando empleado Federico y asignando permisos...");
		federico = gestor.crearEmpleado("Federico", "132435468B");
		gestor.añadirPermiso(federico, Permiso.VALIDACIONES);
		gestor.añadirPermiso(federico, Permiso.INTERCAMBIOS);

		app.cerrarSesion();

		System.out.println("\n[->] Registrando nuevos clientes: Matteo y Rodrigo...");
		matteo = app.crearCuenta("Matteo", "123456789B", "1111");
		rodrigo = app.crearCuenta("Rodrigo", "123456789C", "2222");

		// *********************************************************************************************************
		// CONFIGURACIÓN DE NOTIFICACIONES DESEADAS POR EL CLIENTE
		System.out.println("\n====================================================");
		System.out.println("      CONFIGURACIÓN DE NOTIFICACIONES DEL CLIENTE");
		System.out.println("====================================================");
		System.out.println("[->] Matteo activa notificaciones de DESCUENTOS y RECOMENDACIONES...");
		app.iniciarSesion("Matteo", "1111");
		matteo.getConfiguracionNotificacionClientees().add(NotificacionDeseada.DESCUENTOS);
		matteo.getConfiguracionNotificacionClientees().add(NotificacionDeseada.RECOMENDACIONES);
		System.out.println("     Notificaciones activas de Matteo: " + matteo.getConfiguracionNotificacionClientees());
		app.cerrarSesion();

		System.out.println("[->] Rodrigo activa únicamente notificaciones de RECOMENDACIONES...");
		app.iniciarSesion("Rodrigo", "2222");
		rodrigo.getConfiguracionNotificacionClientees().add(NotificacionDeseada.RECOMENDACIONES);
		System.out
				.println("     Notificaciones activas de Rodrigo: " + rodrigo.getConfiguracionNotificacionClientees());
		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE SOLICITUD VALIDACION Y SOLICITUD INTERCAMBIO
		System.out.println("\n====================================================");
		System.out.println("    PRUEBA DE VALIDACIÓN E INTERCAMBIOS DE SEGUNDA MANO");
		System.out.println("====================================================");
		System.out.println("[->] Matteo y Rodrigo añaden productos a sus carteras de intercambio...");

		app.iniciarSesion("Matteo", "1111");
		matteo.añadirProductoACarteraDeIntercambio("Peluche de perro", "Es un peluche muy bonito y suavecito", null);
		app.cerrarSesion();

		app.iniciarSesion("Rodrigo", "2222");
		rodrigo.añadirProductoACarteraDeIntercambio("Camion de bomberos", "Un camion con 4 ruedas, es increible!",
				null);
		app.cerrarSesion();

		System.out.println("\n[->] Federico (Empleado) revisa y valida los productos de segunda mano...");
		app.iniciarSesion("Federico", "123456");
		List<SolicitudValidacion> solicitudes = Aplicacion.getInstancia().getGestorSolicitud().getValidaciones();
		federico.validarProducto(solicitudes.get(0), 2.4, 10, EstadoConservacion.MUY_BUENO);
		federico.validarProducto(solicitudes.get(1), 3, 10, EstadoConservacion.MUY_USADO);
		app.cerrarSesion();

		System.out.println("\n[->] Rodrigo y Matteo pagan las tasas de validación de sus productos...");
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

		System.out.println("\n[->] Matteo realiza una oferta de intercambio a Rodrigo...");
		// Sets que usaremos para realizar la oferta
		Set<ProductoSegundaMano> sMatteo = new HashSet<>(productosMatteo);
		Set<ProductoSegundaMano> sRodrigo = new HashSet<>(productosRodrigo);
		matteo.realizarOferta(sMatteo, sRodrigo, rodrigo);
		app.cerrarSesion();

		System.out.println("\n[->] Rodrigo revisa sus ofertas y ACEPTA la oferta de Matteo...");
		// Rodrigo inicia sesion para aceptar la oferta
		app.iniciarSesion("Rodrigo", "2222");
		List<Oferta> ofertasRecibidasRodrigo = rodrigo.getOfertasRecibidas();
		Oferta ofertaAceptar = ofertasRecibidasRodrigo.get(0);
		rodrigo.aceptarOferta(ofertaAceptar);
		app.cerrarSesion();

		System.out.println("\n[->] Extrayendo códigos de intercambio de las notificaciones...");
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

		System.out.println("\n[->] Federico aprueba el intercambio final usando los códigos de seguridad...");
		// Federico inicia sesion para aprobar el intercambio
		app.iniciarSesion("Federico", "123456");
		List<SolicitudIntercambio> listaIntercambios = GestorSolicitudes.getInstancia().getIntercambios();
		SolicitudIntercambio sol = listaIntercambios.get(0);
		System.out.println("Notificaciones de Federico antes de aprobar: " + federico.getNotificaciones());
		federico.aprobarIntercambio(sol, codigoMatteo, codigoRodrigo);
		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE GESTIÓN DE CATÁLOGO (PRODUCTOS NUEVOS)
		System.out.println("\n====================================================");
		System.out.println("           PRUEBA DE GESTIÓN DE CATÁLOGO");
		System.out.println("====================================================");
		System.out.println("[->] Gestor añade productos nuevos y configura categorías...");

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
			System.out.println("Error al cargar fichero: " + excepcionFichero);
		}

		System.out.println("\nCategorías iniciales de la tienda:");
		for (Categoria c : cat.getCategoriasTienda()) {
			System.out.println(" - " + c.getNombre());
		}

		// Modificamos una categoria
		Categoria catThriller = cat.buscarCategoriaPorNombre("Thriller");
		cat.modificarCategoria(catThriller, "Suspense");

		System.out.println("\nCategorías (con modificación de Thriller a Suspense):");
		for (Categoria c : cat.getCategoriasTienda()) {
			System.out.println(" - " + c.getNombre());
		}

		System.out.println("\n[->] Gestor busca y modifica el stock de un producto...");
		// Modificamos los datos de un producto
		List<LineaProductoVenta> prod = cat.obtenerProductosNuevosGestion("Comic de Spiderman Adventures");
		System.out.println("Lista de productos obtenidos al buscar 'Comic de Spiderman Adventures':");

		for (LineaProductoVenta p : prod) {
			System.out.println(p);
			System.out.println("----------------------------------------------------");
		}
		// Modificamos el producto que más se ajuste al resultado, aumentando su stock
		LineaProductoVenta productoAux = prod.get(0);
		cat.modificarProducto(prod.get(0), productoAux.getNombre(), productoAux.getDescripcion(), productoAux.getFoto(),
				30, 19.99);

		System.out.println("\n[->] Gestor crea un Pack de productos...");
		// Creamos un pack de comics de Spiderman
		List<LineaProductoVenta> comicsSpidermanProds = cat.obtenerProductosNuevosGestion("Comic Spiderman");

		Pack comicsSpidermanPack = gestor.añadirPack("Pack de comics de Spiderman",
				"Dos comics sumamente valiosos y valorados dentro de la comunidad de amantes de Spiderman",
				new File("/dosComicsSpiderman.jpg"), 5, 34.99, new HashMap<>());

		// Añadimos primero 10 comics a cada pack, lo que producirá un error por
		// insuficiencia de stock
		System.out.println("\nIntentando añadir 10 unidades al pack (debe saltar error de stock):");
		System.out.println(comicsSpidermanPack);
		try {
			comicsSpidermanPack.añadirProductoAPack(prod.get(0), 10);
		} catch (IllegalArgumentException errorStock) {
			System.out.println("Error capturado correctamente: " + errorStock.getMessage());
		}

		System.out.println("\nAñadiendo cantidades correctas al pack...");
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

		System.out.println("\nProducto recomendado a Rodrigo tras su búsqueda: ");
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
		SolicitudPedido pedidoRodrigo = Aplicacion.getInstancia().crearPedidoAPartirDeCarrito();
		System.out.println("\nPedido creado:");
		System.out.println(pedidoRodrigo);
		System.out.println("Estado del pedido: " + pedidoRodrigo.getEstado());

		// Rodrigo paga el pedido
		System.out.println("\nProcesando pago del pedido...");
		rodrigo.pagarPedido(pedidoRodrigo, "1234567890123456", "123", new DateTimeSimulado());
		System.out.println("Estado del pedido tras el pago: " + pedidoRodrigo.getEstado());

		System.out.println("\nVemos que a Federico le aparece una notificación correspondiente al pedido:\n"
				+ federico.getNotificaciones());

		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE VALIDACIÓN DE PEDIDO POR UN EMPLEADO CON PERMISOS

		System.out.println("\n====================================================");
		System.out.println("       PRUEBA DE VALIDACIÓN DE PEDIDO");
		System.out.println("====================================================");

		// Federico inicia sesión para validar el pedido
		app.iniciarSesion("Federico", "123456");

		List<SolicitudPedido> pedidosPendientes = GestorSolicitudes.getInstancia().getPedidos();
		System.out.println("\nPedidos pendientes de preparar: " + pedidosPendientes.size());

		if (!pedidosPendientes.isEmpty()) {
			SolicitudPedido pedidoAValidar = pedidosPendientes.get(0);
			System.out.println("\nPedido a preparar por el empleado:");
			System.out.println(pedidoAValidar);
			System.out.println("Estado antes de validar: " + pedidoAValidar.getEstado());

			System.out.println("\nFederico intenta actualizar el estado pero NO tiene permiso de pedidos:");
			// Federico marca el pedido como listo para recoger pero no tiene el permiso
			try {
				federico.actualizarEstadoPedido(pedidoAValidar, EstadoPedido.LISTO_PARA_RECOGER);
			} catch (IllegalStateException e) {
				System.out.println("Error capturado: " + e.getMessage());
			}
			app.cerrarSesion();

			System.out.println("\nGestor inicia sesión y otorga el permiso de Pedidos a Federico...");
			// El gestor le da el permiso
			app.iniciarSesion("gestor", "@soyElGestor1234!");
			gestor.añadirPermiso(federico, Permiso.PEDIDOS);
			app.cerrarSesion();

			System.out.println("Federico vuelve a iniciar sesión y ahora SÍ puede actualizar el pedido...");
			// Federico inicia sesión para validar el pedido
			app.iniciarSesion("Federico", "123456");
			federico.actualizarEstadoPedido(pedidoAValidar, EstadoPedido.LISTO_PARA_RECOGER);

			System.out.println("Estado tras validación: " + pedidoAValidar.getEstado());
		} else {
			System.out.println("No hay pedidos pendientes de validar.");
		}

		app.cerrarSesion();

		// *********************************************************************************************************
		// PRUEBA DE RECOGIDA DEL PEDIDO

		System.out.println("\n====================================================");
		System.out.println("          PRUEBA PEDIDO A RECOGER");
		System.out.println("====================================================");

		// Rodrigo inicia sesión para recoger el pedido
		app.iniciarSesion("Rodrigo", "2222");

		List<NotificacionCliente> notifsRodrigoPedido = rodrigo.getNotificaciones();
		System.out.println("\nNotificaciones de Rodrigo tras la validación (Debería tener aviso de recogida):");
		for (NotificacionCliente notif : notifsRodrigoPedido) {
			System.out.println(notif);
		}

		app.cerrarSesion();

		// *********************************************************************************************************
		// DEMOSTRACIÓN: NUEVO PRODUCTO CON DESCUENTO Y NOTIFICACIÓN

		System.out.println("\n====================================================");
		System.out.println("   DEMOSTRACIÓN: NUEVO PRODUCTO CON DESCUENTO");
		System.out.println("====================================================");

		// Federico (Empleado) sube un producto nuevo
		app.iniciarSesion("Federico", "123456");
		System.out.println("[->] Federico sube un nuevo producto: 'Figura de Iron Man'...");
		// original:
		// 100€
		LineaProductoVenta ironMan = federico.añadirProducto("Figura de Iron Man Limited Edition",
				"Figura de alta calidad, escala 1/6 con luces LED.", new File("/images/ironman.jpg"), 10, 100.0);

		System.out.println("[->] Federico crea un descuento del 20% y lo aplica a la Figura de Iron Man...");
		Descuento desc20 = new Precio(new DateTimeSimulado(), DateTimeSimulado.DateTimeDiasDespues(7), 20);
		cat.añadirDescuento(desc20);
		cat.aplicarDescuento(ironMan, desc20);
		app.cerrarSesion();

		// Rodrigo entra y revisa sus notificaciones
		app.iniciarSesion("Rodrigo", "2222");
		System.out.println("\n[->] Rodrigo entra en la aplicación y revisa sus notificaciones...");

		// Rodrigo tenía activas las notificaciones de Recomendaciones y Descuentos
		System.out.println(rodrigo.getNotificaciones());

		// 4. Rodrigo añade el producto al carrito y paga
		System.out.println("\n[->] Rodrigo añade la figura al carrito y procede al pago...");
		rodrigo.añadirProductoACarrito(ironMan, 1);

		SolicitudPedido pedidoDescuento = app.crearPedidoAPartirDeCarrito();

		double precioAntesDePagar = pedidoDescuento.getCostePedido();
		System.out.println("Importe total del pedido (con descuento aplicado): " + precioAntesDePagar + "€");

		rodrigo.pagarPedido(pedidoDescuento, "1234567890123456", "123", new DateTimeSimulado());

		// 5. Verificación final del ahorro
		System.out.println("Verificación: El precio pagado es " + precioAntesDePagar 
				+ "€ frente al precio original de 100.0€");

		if (pedidoDescuento.getCostePedido() < 100.0) {
			System.out
					.println(">>> ÉXITO: El cliente ha ahorrado " + (100 - pedidoDescuento.getCostePedido()) + "€");
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
		DateTimeSimulado fin = new DateTimeSimulado(1, 12, 30, 23, 59, 59);

		try {
			app.getSistemaEstadisticas().obtenerRecaudacionMensual(inicio, fin, ficheroEstadisticas);
			System.out
					.println("\nInforme de recaudación mensual generado en: " + ficheroEstadisticas.getAbsolutePath());

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

		// *********************************************************************************************************
		// DEMOSTRACIÓN DETALLADA DEL SISTEMA DE RECOMENDACIÓN
		// *********************************************************************************************************
		System.out.println("\n====================================================");
		System.out.println("      DEMOSTRACIÓN SISTEMA DE RECOMENDACIÓN");
		System.out.println("====================================================");

		// 1. ESCENARIO A: PRIORIDAD NOVEDAD (Configuración por defecto del Gestor)
		// El gestor configura que lo más importante sea la Novedad (factor 3)
		app.iniciarSesion("gestor", "@soyElGestor1234!");
		gestor.configurarImportancia(0, 0, 1); // Solo importa la novedad
		gestor.configurarUnidadesRecomendadas(1);
		app.cerrarSesion();

		app.iniciarSesion("Rodrigo", "2222");
		System.out.println("\n[Escenario A] Prioridad: NOVEDAD");
		System.out.println("Producto recomendado (debe ser el último añadido al catálogo):");
		for (LineaProductoVenta p : app.getConfiguracionRecomendacion().getRecomendacion()) {
			System.out.println("-> " + p.getNombre() + " (Fecha: " + p.getFechaSubida().toStringFecha() + ")");
		}
		app.cerrarSesion();

		// 2. ESCENARIO B: PRIORIDAD RESEÑAS (Valoración)
		// Vamos a añadir una reseña excelente a un producto antiguo para que destaque
		// sobre los nuevos
		app.iniciarSesion("Matteo", "1111");
		List<LineaProductoVenta> todos = new ArrayList<>(cat.getProductosNuevos());
		LineaProductoVenta productoAntiguo = todos.get(0); // El primer comic de Spiderman
		matteo.escribirReseña(productoAntiguo, "Increíble. Es el mejor cómic que he leído", 5, new DateTimeSimulado());
		app.cerrarSesion();

		app.iniciarSesion("gestor", "@soyElGestor1234!");
		gestor.configurarImportancia(0, 1, 0); // Solo importan las reseñas
		app.cerrarSesion();

		app.iniciarSesion("Rodrigo", "2222");
		System.out.println("\n[Escenario B] Prioridad: RESEÑAS (Valoración)");
		System.out.println("Producto recomendado (debe ser el que tiene la mejor puntuación media):");
		for (LineaProductoVenta p : app.getConfiguracionRecomendacion().getRecomendacion()) {
			System.out.println("-> " + p.getNombre() + " (Puntuación: " + p.obtenerPuntuacionMedia() + ")");
		}
		app.cerrarSesion();

		// 3. ESCENARIO C: PRIORIDAD INTERÉS (Comportamiento del usuario)
		// Rodrigo busca mucho un tipo de producto, lo que debe cambiar su recomendación
		// personal
		app.iniciarSesion("gestor", "@soyElGestor1234!");
		gestor.configurarImportancia(1, 0, 0); // Solo importa el interés generado por el usuario
		app.cerrarSesion();

		app.iniciarSesion("Rodrigo", "2222");
		System.out.println("\n[Escenario C] Prioridad: INTERÉS");
		System.out.println("Rodrigo va a buscar 'Figuras' repetidamente...");
		app.buscarProductosNuevos("Figura");
		app.buscarProductosNuevos("Figura");

		System.out.println("Producto recomendado para Rodrigo (debe ser una Figura por su interés de búsqueda):");
		for (LineaProductoVenta p : app.getConfiguracionRecomendacion().getRecomendacion()) {
			System.out.println("-> " + p.getNombre());
		}

		// 4. ESCENARIO D: INTERÉS POR CATEGORÍA (Añadir al carrito)
		// Al añadir al carrito, el interés por la categoría aumenta drásticamente
		// (PESO_CARRITO = 10)
		System.out.println("\nRodrigo añade un 'Pack' al carrito...");
		List<LineaProductoVenta> prodsPack = cat.obtenerProductosNuevosGestion("Pack");
		if (!prodsPack.isEmpty()) {
			rodrigo.añadirProductoACarrito(prodsPack.get(0), 1);
		}

		System.out.println("Nueva recomendación tras añadir al carrito (el interés ha evolucionado):");
		for (LineaProductoVenta p : app.getConfiguracionRecomendacion().getRecomendacion()) {
			System.out.println("-> " + p.getNombre());
		}
		app.cerrarSesion();

		System.out.println("\n====================================================");
		System.out.println("      PRUEBA DE PERSISTENCIA (GUARDAR Y CARGAR DATOS)");
		System.out.println("====================================================");
		String archivo = "test_tienda.dat";

		System.out.println("Guardando el estado actual de la aplicación en '" + archivo + "'...");
		try {
			app.guardarEstadoAplicacion(archivo);
			System.out.println("Estado guardado con éxito.");
		} catch (IOException e) {
			System.err.println("Error guardando: " + e.getMessage());
			return;
		}

		Aplicacion appCargada = null;
		System.out.println("\nCargando el estado de la aplicación desde el fichero...");
		try {
			Aplicacion.cargarEstadoAplicacion(archivo);
			appCargada = Aplicacion.getInstancia();
			System.out.println("Aplicación cargada con éxito.");
		} catch (Exception e) {
			System.err.println("Error cargando: " + e.getMessage());
			return;
		}

		System.out.println("\nImprimiendo un fragmento del catálogo cargado para verificar persistencia:");
		System.out.println("Categorías restauradas: " + appCargada.getCatalogo().getCategoriasTienda());

		// 5. LIMPIAR
		new java.io.File(archivo).delete();

		System.out.println("\n====================================================");
		System.out.println("           GUARDADO DE APLICACIÓN");
		System.out.println("====================================================");

		System.out.println("Estado de la aplicacion antes de su guardado :");
		System.out.println("====================================================");
		System.out.println(app);
		System.out.println(
				"============================================================================================================================================================");
		System.out.println(
				"============================================================================================================================================================");
		System.out.println(
				"============================================================================================================================================================");
		System.out.println("\nEstado de la aplicacion al cargarla del guardado:");
		System.out.println("====================================================");
		System.out.println(appCargada);

		System.out.println("\n====================================================");
		System.out.println("           FIN DE LA DEMOSTRACIÓN");
		System.out.println("====================================================");

	}
}