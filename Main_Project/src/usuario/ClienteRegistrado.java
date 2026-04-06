package usuario;

import java.io.File;
import java.util.*;
import notificacion.*;
import producto.*;
import solicitud.*;
import tiempo.*;
import aplicacion.*;

/**
 * Implementa la clase ClienteRegistrado. Representa a un usuario de la tienda
 * con carrito, cartera de intercambios, notificaciones y un registro de sus
 * compras e intereses.
 *
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 * @date 06-04-2026
 */
public class ClienteRegistrado extends Usuario {
	private Set<NotificacionDeseada> configuracionNotificacionClientees = new HashSet<NotificacionDeseada>();
	private Cartera cartera = new Cartera();
	private Carrito carrito = new Carrito();
	private List<NotificacionCliente> notificaciones = new ArrayList<>();
	private List<SolicitudPedido> pedidos = new ArrayList<>();
	private List<Reseña> reseñas = new ArrayList<>();
	private List<Oferta> ofertasRealizadas = new ArrayList<>();
	private List<Oferta> ofertasRecibidas = new ArrayList<>();
	private Interes interes = new Interes();

	/**
	 * Instancia un nuevo cliente registrado.
	 *
	 * @param username el nombre de usuario
	 * @param DNI      el DNI
	 * @param password la contraseña
	 */
	public ClienteRegistrado(String username, String DNI, String password) {
		super(username, DNI, password);
		System.out.println("Nuevo cliente registrado: " + username);
	}

	/**
	 * Obtiene el gasto total de los pedidos pagados por el cliente.
	 *
	 * @return el gasto total en pedidos
	 */
	public double getGastoTotalPedidos() {
		double total = 0;
		for (SolicitudPedido sP : this.pedidos) {
			if (sP.pagado() == true) {
				total += sP.getCostePedido();
			}
		}
		return total;
	}

	/**
	 * Obtiene el gasto total en validaciones de productos de segunda mano.
	 *
	 * @return el gasto total en validaciones
	 */
	public double getGastoTotalValidaciones() {
		double total = 0;
		for (ProductoSegundaMano p : this.cartera.getProductos()) {
			if (p.getDatosValidacion() != null) {
				total += p.getSolicitudValidacion().getPrecioValidacion();
			}
		}
		return total;
	}

	/**
	 * Añade un producto al carrito del cliente y actualiza su interés.
	 *
	 * @param p        el producto a añadir
	 * @param cantidad la cantidad del producto
	 */
	public void añadirProductoACarrito(LineaProductoVenta p, Integer cantidad) {
		this.getInteres().actualizarInteresCarritoCategorias(p);
		this.carrito.añadirProducto(p, cantidad);
		System.out.println("Producto añadido al carrito.");
	}

	/**
	 * Elimina una cantidad específica de un producto del carrito.
	 *
	 * @param p        el producto a eliminar
	 * @param cantidad la cantidad a retirar
	 */
	public void eliminarProductoDelCarrito(LineaProductoVenta p, Integer cantidad) {
		this.carrito.eliminarProducto(p, cantidad);
	}

	/**
	 * Crea y añade un producto de segunda mano a la cartera de intercambio.
	 *
	 * @param nombre      el nombre del producto
	 * @param descripcion la descripción del producto
	 * @param imagen      el archivo de imagen
	 * @return el producto de segunda mano creado
	 */
	public ProductoSegundaMano añadirProductoACarteraDeIntercambio(String nombre, String descripcion, File imagen) {
		ProductoSegundaMano p = new ProductoSegundaMano(nombre, descripcion, imagen, this);
		this.cartera.añadirProducto(p);
		System.out.println("Producto añadido a la cartera de intercambio.");
		return p;
	}

	/**
	 * Elimina un producto de la cartera de intercambio.
	 *
	 * @param p el producto a eliminar
	 */
	public void eliminarProductoDeCarteraDeIntercambio(ProductoSegundaMano p) {
		this.cartera.eliminarProducto(p);
	}

	/**
	 * Realiza una oferta de intercambio a otro cliente.
	 *
	 * @param productosOfertados   los productos que ofrece este cliente
	 * @param productosSolicitados los productos que solicita del destinatario
	 * @param destinatario         el cliente al que va dirigida la oferta
	 * @return la oferta realizada
	 */
	public Oferta realizarOferta(Set<ProductoSegundaMano> productosOfertados,
			Set<ProductoSegundaMano> productosSolicitados, ClienteRegistrado destinatario) {
		Oferta ofertaRealizada = new Oferta(new DateTimeSimulado(), destinatario, this, productosOfertados,
				productosSolicitados);
		this.ofertasRealizadas.add(ofertaRealizada);
		destinatario.recibirOferta(ofertaRealizada);
		NotificacionOferta notif = new NotificacionOferta("Nueva oferta recibida de " + this.nombreUsuario,
				new DateTimeSimulado(), ofertaRealizada);
		destinatario.anadirNotificacion(notif);
		for (ProductoSegundaMano prod : productosSolicitados) {
			prod.addOfertaRecibida(ofertaRealizada);
		}
		for (ProductoSegundaMano prod : productosOfertados) {
			prod.addOfertaEnviada(ofertaRealizada);
		}
		System.out.println("Oferta enviada correctamente a: " + destinatario.getNombreUsuario());
		return ofertaRealizada;
	}

	/**
	 * Recibe una oferta y la añade a la lista de ofertas recibidas.
	 *
	 * @param o la oferta recibida
	 */
	public void recibirOferta(Oferta o) {
		this.ofertasRecibidas.add(o);
	}

	/**
	 * Acepta una oferta y crea la solicitud de intercambio correspondiente.
	 *
	 * @param o la oferta a aceptar
	 */
	public void aceptarOferta(Oferta o) {
		Aplicacion app = Aplicacion.getInstancia();
		app.crearSolicitudIntercambio(o);
		System.out.println("Oferta aceptada.");
	}

	/**
	 * Obtiene la configuración de notificaciones del cliente.
	 *
	 * @return la configuración de notificaciones
	 */
	public Set<NotificacionDeseada> getConfiguracionNotificacionClientees() {
		return configuracionNotificacionClientees;
	}

	/**
	 * Obtiene la cartera de intercambio del cliente.
	 *
	 * @return la cartera
	 */
	public Cartera getCartera() {
		return cartera;
	}

	/**
	 * Obtiene el carrito del cliente.
	 *
	 * @return el carrito
	 */
	public Carrito getCarrito() {
		return carrito;
	}

	/**
	 * Obtiene la lista de notificaciones del cliente.
	 *
	 * @return las notificaciones
	 */
	public List<NotificacionCliente> getNotificaciones() {
		return notificaciones;
	}

	/**
	 * Obtiene el historial de pedidos del cliente.
	 *
	 * @return los pedidos
	 */
	public List<SolicitudPedido> getPedidos() {
		return pedidos;
	}

	/**
	 * Obtiene las reseñas escritas por el cliente.
	 *
	 * @return las reseñas
	 */
	public List<Reseña> getReseñas() {
		return reseñas;
	}

	/**
	 * Obtiene las ofertas realizadas por el cliente.
	 *
	 * @return las ofertas realizadas
	 */
	public List<Oferta> getOfertasRealizadas() {
		return ofertasRealizadas;
	}

	/**
	 * Obtiene el registro de interés del cliente.
	 *
	 * @return el interés
	 */
	public Interes getInteres() {
		return interes;
	}

	/**
	 * Obtiene las ofertas recibidas por el cliente.
	 *
	 * @return las ofertas recibidas
	 */
	public List<Oferta> getOfertasRecibidas() {
		return ofertasRecibidas;
	}

	/**
	 * Rechaza una oferta recibida.
	 *
	 * @param o la oferta a rechazar
	 */
	public void rechazarOferta(Oferta o) {
		this.ofertasRecibidas.remove(o);
		o.getOfertante().eliminarOferta(o);
		System.out.println("Oferta rechazada.");
	}

	/**
	 * Elimina una oferta de la lista de ofertas realizadas.
	 *
	 * @param o la oferta a eliminar
	 */
	public void eliminarOferta(Oferta o) {
		this.ofertasRealizadas.remove(o);
	}

	/**
	 * Escribe una reseña para un producto.
	 *
	 * @param p           el producto a reseñar
	 * @param descripcion el texto de la reseña
	 * @param puntuacion  la puntuación otorgada
	 * @param fecha       la fecha de la reseña
	 */
	public void escribirReseña(LineaProductoVenta p, String descripcion, double puntuacion, DateTimeSimulado fecha) {
		Reseña r = new Reseña(descripcion, puntuacion, fecha, p);
		p.añadirReseña(r);
		this.reseñas.add(r);
		System.out.println("Reseña escrita correctamente.");
	}

	/**
	 * Elimina una reseña de un producto.
	 *
	 * @param p el producto
	 * @param r la reseña a eliminar
	 */
	public void eliminarReseña(LineaProductoVenta p, Reseña r) {
		p.eliminarReseña(r);
		this.reseñas.remove(r);
	}

	/**
	 * Añade una notificación a la bandeja del cliente.
	 *
	 * @param n la notificación
	 */
	public void anadirNotificacion(NotificacionCliente n) {
		this.notificaciones.add(n);
	}

	/**
	 * Elimina una notificación de la bandeja del cliente.
	 *
	 * @param n la notificación
	 */
	public void eliminarNotificacion(NotificacionCliente n) {
		this.notificaciones.remove(n);
	}

	/**
	 * Elimina una notificación buscando por su ID.
	 *
	 * @param id el identificador de la notificación
	 */
	public void eliminarNotificacionPorId(Integer id) {
		Iterator<NotificacionCliente> iterador = this.notificaciones.iterator();
		while (iterador.hasNext()) {
			NotificacionCliente notif = iterador.next();
			if (notif.getId() == id) {
				iterador.remove();
			}
		}
	}

	/**
	 * Cancela un pedido existente.
	 *
	 * @param pedido el pedido a cancelar
	 */
	public void cancelarPedido(SolicitudPedido pedido) {
		this.pedidos.remove(pedido);
		Aplicacion.getInstancia().getGestorSolicitud().eliminarPedido(pedido);
		System.out.println("Pedido cancelado.");
	}

	/**
	 * Procesa el pago de un pedido pendiente.
	 *
	 * @param pedido         el pedido a pagar
	 * @param numTarjeta     el número de tarjeta
	 * @param cvv            el código de seguridad
	 * @param fechaCaducidad la fecha de caducidad
	 * @throws IllegalArgumentException si los datos son nulos
	 * @throws IllegalStateException    si el pedido ya está pagado o hay un error
	 */
	public void pagarPedido(SolicitudPedido pedido, String numTarjeta, String cvv, DateTimeSimulado fechaCaducidad) {
		if (pedido == null || fechaCaducidad == null) {
			throw new IllegalArgumentException("El pedido y la fecha de caducidad no pueden ser nulos.");
		}

		if (pedido.pagado()) {
			throw new IllegalStateException("El pedido ya tiene un pago asociado.");
		}

		if (pedido.getEstado() != EstadoPedido.PENDIENTE_DE_PAGO) {
			throw new IllegalStateException("El pedido no está en estado pendiente de pago.");
		}

		Pago pago = Aplicacion.getInstancia().getSistemaPago().procesarPago(pedido.getCostePedido(), numTarjeta, cvv,
				fechaCaducidad, pedido);

		if (pago == null) {
			NotificacionPedido noti = new NotificacionPedido("¡Error en el pago del pedidio!", new DateTimeSimulado(),
					pedido);
			anadirNotificacion(noti);
			throw new IllegalStateException("El pago del pedido no se ha podido procesar.");
		}

		pedido.añadirPagoPedido(pago);
		Aplicacion.getInstancia().getSistemaEstadisticas().añadirPago(pago);

		/* Añadimos la notificacion */
		NotificacionPedido noti = new NotificacionPedido("¡Pago procesado con éxito!", new DateTimeSimulado(), pedido);
		anadirNotificacion(noti);
		pedido.actualizarPagoPedido(EstadoPedido.PAGADO);

		/* Actualizamos el interés */
		for (LineaProductoVenta p : pedido.getProductosDiferentes().keySet()) {
			this.getInteres().actualizarInteresCompraCategorias(p);
		}
		System.out.println("Pago del pedido realizado con éxito.");
	}

	/**
	 * Confirma los productos del carrito y genera un nuevo pedido.
	 *
	 * @return el pedido generado
	 * @throws IllegalStateException si el carrito está vacío
	 */
	public SolicitudPedido realizarPedido() {
		if (this.getCarrito().getProductos().isEmpty()) {
			throw new IllegalStateException("El carrito está vacío.");
		}

		List<LineaProductoVenta> productosSinStock = new ArrayList<>();

		for (LineaProductoVenta prod : carrito.getProductos().keySet()) {
			this.interes.actualizarInteresPedidoCategorias(prod);
			int cantidad = carrito.getProductos().get(prod);
			if (cantidad > prod.getStock()) {
				productosSinStock.add(prod);
			} else {
				prod.setStock(prod.getStock() - cantidad);
			}
		}

		/* Eliminamos los productos sin stock fuera del bucle de recorrido */
		for (LineaProductoVenta prod : productosSinStock) {
			carrito.eliminarProducto(prod, carrito.getProductos().get(prod));
			System.out.println("No se añadió el producto " + prod.getNombre() + " al pedido por falta de stock");
		}

		if (this.getCarrito().getProductos().isEmpty()) {
	        throw new IllegalStateException("El carrito está vacío.");
	    }
		
		SolicitudPedido pedido = new SolicitudPedido(this, carrito.getProductos());
		this.pedidos.add(pedido);
		Aplicacion.getInstancia().getGestorSolicitud().añadirPedido(pedido);
		carrito.vaciarCarrito();
		NotificacionPedido noti = new NotificacionPedido("¡Pedido realizado con éxito!", new DateTimeSimulado(),
				pedido);
		anadirNotificacion(noti);
		System.out.println("Pedido generado correctamente.");
		return pedido;
	}

	/**
	 * Procesa el pago para validar un producto de segunda mano.
	 *
	 * @param validacion     la solicitud de validación
	 * @param numTarjeta     el número de tarjeta
	 * @param cvv            el código de seguridad
	 * @param fechaCaducidad la fecha de caducidad
	 * @return el pago generado
	 * @throws IllegalArgumentException si los datos son inválidos
	 * @throws IllegalStateException    si el pago falla
	 */
	public Pago pagarValidacion(SolicitudValidacion validacion, String numTarjeta, String cvv,
			DateTimeSimulado fechaCaducidad) {
		if (validacion == null || fechaCaducidad == null) {
			throw new IllegalArgumentException("La solicitud de validación no puede ser nula.");
		}

		if (validacion.getPagoValidacion() != null) {
			throw new IllegalArgumentException("La solicitud de validación ya tiene un pago asociado.");
		}

		Pago pago = Aplicacion.getInstancia().getSistemaPago().procesarPago(validacion.getPrecioValidacion(),
				numTarjeta, cvv, fechaCaducidad, validacion);

		if (pago == null) {
			NotificacionValidacion noti = new NotificacionValidacion("!Error en el pago de la valoración!",
					new DateTimeSimulado(), validacion);
			anadirNotificacion(noti);

			throw new IllegalStateException("El pago de la validación no se ha podido procesar.");
		}

		validacion.añadirPagoValidacion(pago);
		Aplicacion.getInstancia().getSistemaEstadisticas().añadirPago(pago);

		NotificacionValidacion noti = new NotificacionValidacion("¡Valoración pagada con éxito!",
				new DateTimeSimulado(), validacion);
		anadirNotificacion(noti);

		System.out.println("Pago de validación realizado con éxito.");
		return pago;
	}

	/**
	 * Obtiene los productos de la cartera que no están bloqueados.
	 *
	 * @return la lista de productos disponibles
	 */
	public List<ProductoSegundaMano> getProductosSegundaManoDisponibles() {
		List<ProductoSegundaMano> prods = new LinkedList<ProductoSegundaMano>();
		for (ProductoSegundaMano p : this.cartera.getProductos()) {
			if (!p.estaBloqueado()) {
				prods.add(p);
			}
		}
		return prods;
	}

}