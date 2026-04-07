package aplicacion;

import java.util.*;
import java.util.Map.Entry;

import descuento.*;
import categoria.*;
import producto.*;
import tiempo.DateTimeSimulado;
import usuario.ClienteRegistrado;
import usuario.NotificacionDeseada;
import filtro.*;
import notificacion.NotificacionProducto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class Catalogo.
 */
public class Catalogo {

	/** El instancia. */
	private static Catalogo instancia;

	/** El descuentos. */
	private Set<Descuento> descuentos = new HashSet<>();

	/** El categorias tienda. */
	private Set<Categoria> categoriasTienda = new HashSet<>();

	/** El productos nuevos. */
	private Set<LineaProductoVenta> productosNuevos = new HashSet<>();

	/** El productos segunda mano. */
	private Set<ProductoSegundaMano> productosSegundaMano = new HashSet<>();

	/** El filtro productos gestion. */
	// Parametros para busqueda
	private FiltroVenta filtroProductosGestion = new FiltroVenta(false);

	/** El filtro productos segunda mano. */
	private FiltroIntercambio filtroProductosSegundaMano = new FiltroIntercambio(false, 0, Double.MAX_VALUE);

	/** El filtro productos venta. */
	private FiltroVentaCliente filtroProductosVenta = new FiltroVentaCliente(false, 0, 0, 0, 0, false, false);

	/** El primer lanzamiento. */
	// Parametros para recomendacion por novedad
	private DateTimeSimulado primerLanzamiento = null;

	/** El ultimo lanzamiento. */
	private DateTimeSimulado ultimoLanzamiento = null;

	/**
	 * Instancia un nuevo catalogo.
	 */
	private Catalogo() {

	}

	/**
	 * Devuelve el instancia.
	 *
	 * @return el instancia
	 */
	public static Catalogo getInstancia() {
		if (instancia == null) {
			instancia = new Catalogo();
		}
		return instancia;
	}

	/**
	 * Añadir descuento.
	 *
	 * @param d el d
	 */
	// Métodos para los descuentos
	public void añadirDescuento(Descuento d) {
		if (d == null) {
			throw new IllegalArgumentException("El descuento introducido no es valido");
		}

		descuentos.add(d);
	}

	/**
	 * Eliminar descuento.
	 *
	 * @param d el d
	 */
	public void eliminarDescuento(Descuento d) {
		descuentos.remove(d);
	}

	/**
	 * Añadir producto.
	 *
	 * @param p el p
	 */
	// Métodos para los productos
	public void añadirProducto(LineaProductoVenta p) {
		if (p == null) {
			throw new IllegalArgumentException("El producto introducido no es valido");
		}

		productosNuevos.add(p);

		// Para el ranking de novedad
		if (primerLanzamiento == null) {
			primerLanzamiento = p.getFechaSubida();
		}
		ultimoLanzamiento = p.getFechaSubida();
		ConfiguracionRecomendacion config = Aplicacion.getInstancia().getConfiguracionRecomendacion();
		config.actualizarRankingNovedad(p);
		config.actualizarRankingValoracion(p);

		// Notificar a usuarios interesados y con permisos
		NotificacionProducto noti = new NotificacionProducto("¡Un nuevo producto que encaja con tus gustos!",
				new DateTimeSimulado());
		noti.addProducto(p);
		for (ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			u.getInteres().actualizarInteresNuevoVenta(p);

			// Comprobar si el usuario quiere recibir recomendaciones
			if (!u.getConfiguracionNotificacionClientees().contains(NotificacionDeseada.RECOMENDACIONES)) {
				continue;
			}

			double rankProd = 0;
			double rankUmbral = u.getInteres().getRankMaxCat();

			// Si el usuario no tiene intereses (umbral 0), evitamos notificar a ciegas
			if (rankUmbral <= 0)
				continue;

			// Iteramos sobre las categorías del PRODUCTO (es más eficiente que iterar sobre
			// el mapa del usuario)
			for (Categoria cat : p.getCategorias()) {
				Integer interesCat = u.getInteres().getRankingInteresCategoriaVenta().get(cat);

				if (interesCat != null) {
					rankProd += interesCat;

					if (rankProd >= rankUmbral) {
						Aplicacion.getInstancia().enviarNotificacion(u, noti);

						break;
					}
				}
			}
		}
	}

	/**
	 * Añadir pack.
	 *
	 * @param pack  el pack
	 * @param prods el prods
	 */
	public void añadirPack(Pack pack, Map<LineaProductoVenta, Integer> prods) {
		if (pack == null) {
			throw new IllegalArgumentException("El pack introducido no es valido");
		}
		if (prods == null) {
			throw new IllegalArgumentException("La lista de productos introducidos no es válida");
		}

		pack.añadirProductosAPack(prods);

		productosNuevos.add(pack);
	}

	/**
	 * Eliminar producto.
	 *
	 * @param p el p
	 */
	public void eliminarProducto(Producto p) {
		if (productosNuevos.isEmpty() || p == null) {
			return;
		}
		productosNuevos.remove(p);

		// Para eliminar de los rankings el producto
		if (p instanceof LineaProductoVenta) {
			for (ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
				u.getInteres().eliminarProductoInteres((LineaProductoVenta) p);
			}
			Aplicacion.getInstancia().getConfiguracionRecomendacion().eliminarProductoNovedad((LineaProductoVenta) p);
			Aplicacion.getInstancia().getConfiguracionRecomendacion()
					.eliminarProductoValoracion((LineaProductoVenta) p);

			// Para actualizar la el primer lanzamiento y ultimo lanzamiento
			if (p.getFechaSubida().dateTimeEnSegundos() == ultimoLanzamiento.dateTimeEnSegundos()) {
				ultimoLanzamiento = null;
				long maxSegundos = -1;
				for (LineaProductoVenta prod : productosNuevos) {
					long segundos = prod.getFechaSubida().dateTimeEnSegundos();
					if (segundos > maxSegundos) {
						maxSegundos = segundos;
						ultimoLanzamiento = prod.getFechaSubida();
					}
				}
			}
			if (p.getFechaSubida().dateTimeEnSegundos() == primerLanzamiento.dateTimeEnSegundos()) {
				primerLanzamiento = null;
				long minSegundos = Long.MAX_VALUE;
				for (LineaProductoVenta prod : productosNuevos) {
					long segundos = prod.getFechaSubida().dateTimeEnSegundos();
					if (segundos < minSegundos) {
						minSegundos = segundos;
						primerLanzamiento = prod.getFechaSubida();
					}
				}
			}
		}
	}

	/**
	 * Modificar producto.
	 *
	 * @param p                el p
	 * @param nuevoNombre      el nuevo nombre
	 * @param nuevaDescripcion el nueva descripcion
	 * @param nuevaFoto        el nueva foto
	 * @param nuevoStock       el nuevo stock
	 * @param nuevoPrecio      el nuevo precio
	 */
	public void modificarProducto(LineaProductoVenta p, String nuevoNombre, String nuevaDescripcion, File nuevaFoto,
			Integer nuevoStock, Double nuevoPrecio) {
		if (nuevoNombre != null) {
			p.setNombre(nuevoNombre);
		}
		if (nuevaDescripcion != null) {
			p.setDescripcion(nuevaDescripcion);
		}
		if (nuevoStock != null) {
			p.setStock(nuevoStock);
		}
		if (nuevoPrecio != null) {
			p.setPrecio(nuevoPrecio);
		}
		if (nuevaFoto != null) {
			p.setFoto(nuevaFoto);
		}
	}

	/**
	 * Añadir productos desde fichero.
	 *
	 * @param f el f
	 * @throws IOException Señala que la I/O exception ha ocurrido
	 */
	public List<LineaProductoVenta> añadirProductosDesdeFichero(File f) throws IOException {
		List<LineaProductoVenta> productos = new LinkedList<>();
		if (f == null || !f.exists()) {
			throw new IllegalArgumentException("El fichero no existe o no es valido.");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {

			// Numero total de productos
			String lineaNumProductos = br.readLine();
			if (lineaNumProductos == null) {
				throw new IllegalArgumentException("Fichero vacio.");
			}
			int numProductos;
			try {
				numProductos = Integer.parseInt(lineaNumProductos.trim());
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("El número de productos no es válido");
			}

			for (int i = 0; i < numProductos; i++) {

				// Nombre
				String nombre = br.readLine();
				if (nombre == null)
					throw new IllegalArgumentException("Falta nombre en producto " + (i + 1));
				nombre = nombre.trim();

				// Descripcion
				String descripcion = br.readLine();
				if (descripcion == null)
					throw new IllegalArgumentException("Falta descripción en producto " + (i + 1));
				descripcion = descripcion.trim();

				// Precio
				String lineaPrecio = br.readLine();
				if (lineaPrecio == null)
					throw new IllegalArgumentException("Falta precio en producto " + (i + 1));
				double precio;
				try {
					precio = Double.parseDouble(lineaPrecio.trim());
					if (precio < 0)
						throw new IllegalArgumentException("El precio no puede ser negativo en producto " + (i + 1));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Precio inválido en producto " + (i + 1));
				}

				// Foto
				String rutaFoto = br.readLine();
				if (rutaFoto == null)
					throw new IllegalArgumentException("Falta la foto en producto " + (i + 1));
				File foto = new File(rutaFoto.trim());

				// Stock
				String lineaStock = br.readLine();
				if (lineaStock == null)
					throw new IllegalArgumentException("Falta stock en producto " + (i + 1));
				int stock;
				try {
					stock = Integer.parseInt(lineaStock.trim());
					if (stock < 0)
						throw new IllegalArgumentException("El stock no puede ser negativo en producto " + (i + 1));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Stock inválido en producto " + (i + 1));
				}

				// Categoria
				String nombreCategoria = br.readLine();
				if (nombreCategoria == null)
					throw new IllegalArgumentException("Falta categoría en producto " + (i + 1));
				nombreCategoria = nombreCategoria.trim();
				Categoria categoria = buscarCategoriaPorNombre(nombreCategoria);

				// Tipo de producto
				String tipo = br.readLine();
				if (tipo == null)
					throw new IllegalArgumentException("Falta tipo en producto " + (i + 1));
				tipo = tipo.trim();

				// Atributos especificos
				String lineaAtributos = br.readLine();
				if (lineaAtributos == null)
					throw new IllegalArgumentException("Faltan atributos en producto " + (i + 1));
				String[] atributos = lineaAtributos.trim().split(";");

				LineaProductoVenta nuevo;
				switch (tipo) {
				case "Comic": {
					if (atributos.length < 4)
						throw new IllegalArgumentException("Atributos insuficientes para Comic en producto " + (i + 1));
					int numeroPaginas;
					try {
						numeroPaginas = Integer.parseInt(atributos[0].trim());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Número de páginas inválido en producto " + (i + 1));
					}
					String autor = atributos[1].trim();
					String editorial = atributos[2].trim();
					int añoPublicacion;
					try {
						añoPublicacion = Integer.parseInt(atributos[3].trim());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Año de publicación inválido en producto " + (i + 1));
					}
					nuevo = new Comic(nombre, descripcion, foto, stock, precio, 0, numeroPaginas, autor, editorial,
							añoPublicacion);
					break;
				}
				case "JuegoDeMesa": {
					if (atributos.length < 4)
						throw new IllegalArgumentException(
								"Atributos insuficientes para JuegoDeMesa en producto " + (i + 1));
					int numeroJugadores, edadMinima, edadMaxima;
					try {
						numeroJugadores = Integer.parseInt(atributos[0].trim());
						edadMinima = Integer.parseInt(atributos[1].trim());
						edadMaxima = Integer.parseInt(atributos[2].trim());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException(
								"Atributos numéricos inválidos en JuegoDeMesa producto " + (i + 1));
					}
					TipoJuegoMesa tipoJuego;
					try {
						tipoJuego = TipoJuegoMesa.valueOf(atributos[3].trim());
					} catch (IllegalArgumentException e) {
						throw new IllegalArgumentException("Tipo de juego inválido en producto " + (i + 1));
					}
					nuevo = new JuegoDeMesa(nombre, descripcion, foto, stock, precio, numeroJugadores, edadMinima,
							edadMaxima, tipoJuego);
					break;
				}
				case "Figura": {
					if (atributos.length < 5)
						throw new IllegalArgumentException(
								"Atributos insuficientes para Figura en producto " + (i + 1));
					String marca = atributos[0].trim();
					String material = atributos[1].trim();
					double dX, dY, dZ;
					try {
						dX = Double.parseDouble(atributos[2].trim());
						dY = Double.parseDouble(atributos[3].trim());
						dZ = Double.parseDouble(atributos[4].trim());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Dimensiones inválidas en Figura producto " + (i + 1));
					}
					nuevo = new Figura(nombre, descripcion, foto, stock, precio, 0, marca, material, dX, dY, dZ);
					break;
				}
				default:
					throw new IllegalArgumentException(
							"Tipo de producto desconocido: '" + tipo + "' en producto " + (i + 1));
				}

				nuevo.añadirCategoria(categoria);
				categoria.añadirProductoACategoria(nuevo);
				this.añadirProducto(nuevo);
				productos.add(nuevo);
			}
		}
		return productos;
	}

	/**
	 * Añadir categoria.
	 *
	 * @param c el c
	 */
	// Métodos para las categorías
	public void añadirCategoria(Categoria c) {
		if (c == null) {
			return;
		}
		categoriasTienda.add(c);

		// Añadir la categoria al ranking de todos los clientes
		for (ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			u.getInteres().actualizarInteresCategoriaNueva(c);
		}
	}

	/**
	 * Eliminar categoria.
	 *
	 * @param c el c
	 */
	public void eliminarCategoria(Categoria c) {
		if (c == null) {
			return;
		}
		categoriasTienda.remove(c);
		// Añadir la categoria al ranking de todos los clientes
		for (ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			u.getInteres().eliminarCategoriaInteres(c);
		}
	}

	/**
	 * Modificar categoria.
	 *
	 * @param c           el c
	 * @param nombreNuevo el nombre nuevo
	 */
	public void modificarCategoria(Categoria c, String nombreNuevo) {
		if (c == null || nombreNuevo == null || nombreNuevo.trim().isEmpty()) {
			return;
		}
		if (categoriasTienda.contains(c)) {
			c.setNombre(nombreNuevo);
		}
	}

	/**
	 * Buscar categoria por nombre.
	 *
	 * @param nombreCategoria el nombre categoria
	 * @return el categoria
	 */
	// Busca una categoria por nombre exacto
	public Categoria buscarCategoriaPorNombre(String nombreCategoria) {
		for (Categoria c : categoriasTienda) {
			if (c.getNombre().equals(nombreCategoria)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Aplicar descuento.
	 *
	 * @param p el p
	 * @param d el d
	 */
	// Métodos para los descuentos
	public void aplicarDescuento(LineaProductoVenta p, Descuento d) {
		if (p == null || d == null) {
			throw new IllegalArgumentException("El producto y el descuento no pueden ser nulos.");
		}

		if (!productosNuevos.contains(p) || !descuentos.contains(d)) {
			throw new IllegalStateException("El producto y el descuento debe perternecer al catalogo");
		}

		// Un producto no puede tener más de un descuento activo
		if (p.getDescuento() != null) {
			if (p.getDescuento().haCaducado() == true) {
				eliminarDescuento(d, p);
			} else {
				throw new IllegalStateException("El producto '" + p.getNombre() + "' ya tiene un descuento activo.");
			}
		}

		// Si alguna categoría del producto ya tiene descuento, hay conflicto
		for (Categoria cat : p.getCategorias()) {
			if (cat.getDescuento() != null) {
				throw new IllegalStateException("El producto '" + p.getNombre() + "' pertenece a la categoría '"
						+ cat.getNombre() + "' que ya tiene un descuento. No se pueden acumular descuentos.");
			}
		}
		p.setDescuento(d);
		d.añadirProductoRebajado(p);
		NotificacionProducto noti = new NotificacionProducto(
				"Aprovecha el descuento en el producto " + p.getNombre() + "!!", new DateTimeSimulado());
		noti.addProducto(p);
		for (ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			if (u.getConfiguracionNotificacionClientees().contains(NotificacionDeseada.DESCUENTOS)) {
				Aplicacion.getInstancia().enviarNotificacion(u, noti);
			}
		}
	}

	/**
	 * Eliminar descuento.
	 *
	 * @param d el d
	 * @param p el p
	 */
	public void eliminarDescuento(Descuento d, LineaProductoVenta p) {
		if (p == null || d == null) {
			throw new IllegalArgumentException("El producto y el descuento no pueden ser nulos.");
		}

		if (!productosNuevos.contains(p) || !descuentos.contains(d)) {
			throw new IllegalStateException("El producto y el descuento debe perternecer al catalogo");
		}

		if (p.getDescuento() == null) {
			throw new IllegalStateException("El producto '" + p.getNombre() + "' no tiene ningún descuento activo.");
		}

		if (!p.getDescuento().equals(d)) {
			throw new IllegalStateException(
					"El descuento indicado no coincide con el descuento activo del producto '" + p.getNombre() + "'.");
		}

		p.setDescuento(null);
		d.eliminarProductoRebajado(p);
	}

	/**
	 * Aplicar descuento.
	 *
	 * @param d el d
	 * @param c el c
	 */
	public void aplicarDescuento(Descuento d, Categoria c) {
		if (d == null || c == null) {
			throw new IllegalArgumentException("El descuento y la categoría no pueden ser nulos.");
		}

		if (!categoriasTienda.contains(c) || !descuentos.contains(d)) {
			throw new IllegalStateException("El descuento y la categoria debe perternecer al catalogo");
		}

		// Vemos si el producto ya tiene un descuento vigente
		if (c.getDescuento() != null) {
			if (c.getDescuento().haCaducado() == true) {
				eliminarDescuento(d, c);
			} else {
				throw new IllegalStateException("La categoría '" + c.getNombre() + "' ya tiene un descuento activo.");
			}
		}

		// Comprobamos que ningún producto de la categoría tenga ya un descuento
		// individual
		for (LineaProductoVenta p : c.obtenerProductosCategoria()) {
			if (p.getDescuento() != null) {
				throw new IllegalStateException("El producto '" + p.getNombre() + "' de la categoría '" + c.getNombre()
						+ "' ya tiene un descuento individual. No se pueden acumular descuentos.");
			}
		}
		c.añadirDescuento(d);
		d.añadirCategoria(c);
		NotificacionProducto noti = new NotificacionProducto(
				"Aprovecha el descuento en todos los productos de la categoria " + c.getNombre() + "!!",
				new DateTimeSimulado());
		for (LineaProductoVenta p : c.obtenerProductosCategoria()) {
			noti.addProducto(p);
		}
		for (ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			if (u.getConfiguracionNotificacionClientees().contains(NotificacionDeseada.DESCUENTOS)) {
				Aplicacion.getInstancia().enviarNotificacion(u, noti);
			}
		}
	}

	/**
	 * Eliminar descuento.
	 *
	 * @param d el d
	 * @param c el c
	 */
	public void eliminarDescuento(Descuento d, Categoria c) {
		if (d == null || c == null) {
			throw new IllegalArgumentException("El descuento y la categoría no pueden ser nulos.");
		}

		if (!categoriasTienda.contains(c) || !descuentos.contains(d)) {
			throw new IllegalStateException("El descuento y la categoria debe perternecer al catalogo");
		}

		if (c.getDescuento() == null) {
			throw new IllegalStateException("La categoría '" + c.getNombre() + "' no tiene ningún descuento activo.");
		}

		if (!c.getDescuento().equals(d)) {
			throw new IllegalStateException(
					"El descuento indicado no coincide con el descuento activo de la categoría '" + c.getNombre()
							+ "'.");
		}
		c.eliminarDescuento(d);
		d.eliminarCategoria(c);
	}

	/**
	 * Cambiar filtro gestion.
	 *
	 * @param ordenAscendente el orden ascendente
	 * @param categorias      el categorias
	 * @param tipos           el tipos
	 */
	// Métodos filtros
	public void cambiarFiltroGestion(boolean ordenAscendente, Set<Categoria> categorias, Set<TipoProducto> tipos) {
		this.filtroProductosGestion.cambiarFiltro(ordenAscendente, categorias, tipos);
	}

	/**
	 * Cambiar filtro venta.
	 *
	 * @param ordenAscendente      el orden ascendente
	 * @param categorias           el categorias
	 * @param tipos                el tipos
	 * @param puntuacionMin        el puntuacion min
	 * @param puntuacionMax        el puntuacion max
	 * @param precioMin            el precio min
	 * @param precioMax            el precio max
	 * @param ordenarPorPrecio     el ordenar por precio
	 * @param ordenarPorPuntuacion el ordenar por puntuacion
	 * @param descuentoFiltrado    el descuento filtrado
	 */
	public void cambiarFiltroVenta(boolean ordenAscendente, Set<Categoria> categorias, Set<TipoProducto> tipos,
			double puntuacionMin, double puntuacionMax, double precioMin, double precioMax, boolean ordenarPorPrecio,
			boolean ordenarPorPuntuacion, Set<TipoDescuento> descuentoFiltrado) {
		this.filtroProductosVenta.cambiarFiltro(ordenAscendente, categorias, tipos, puntuacionMin, puntuacionMax,
				precioMin, precioMax, ordenarPorPrecio, ordenarPorPuntuacion, descuentoFiltrado);
	}

	/**
	 * Cambiar filtro intercambio.
	 *
	 * @param ordenAscendente el orden ascendente
	 * @param valorMin        el valor min
	 * @param valorMax        el valor max
	 * @param estados         el estados
	 */
	public void cambiarFiltroIntercambio(boolean ordenAscendente, double valorMin, double valorMax,
			Set<EstadoConservacion> estados) {
		this.filtroProductosSegundaMano.cambiarFiltro(ordenAscendente, valorMin, valorMax, estados);
	}

	/**
	 * Limpiar filtros.
	 */
	public void limpiarFiltros() {
		filtroProductosGestion.limpiarFiltro();
		filtroProductosSegundaMano.limpiarFiltro();
		filtroProductosVenta.limpiarFiltro();
	}

	/**
	 * Obtener productos nuevos filtrados.
	 *
	 * @param prompt el prompt
	 * @return el list
	 */
	// Métodos de búsqueda
	public List<LineaProductoVenta> obtenerProductosNuevosFiltrados(String prompt) {
		List<LineaProductoVenta> resultado = new ArrayList<>();

		for (LineaProductoVenta p : productosNuevos) {
			// Filtramos por el prompt: comprobamos el nombre y la descripcion
			if (prompt != null && !prompt.trim().isEmpty()) {
				// ponemos en minusculas todo
				String palabra = prompt.toLowerCase();
				boolean boolNombre = p.getNombre().toLowerCase().contains(palabra);
				boolean boolDescripcion = p.getDescripcion().toLowerCase().contains(palabra);
				// si no hay ninguna relacion entre el prompt con el nombre ni la descripcion
				// pasamos al siguiente producto
				if (!boolNombre && !boolDescripcion)
					continue;
			}
			// Filtramos por el filtro que tenga el cliente: categorias, precio, etc
			if (filtroProductosVenta != null) {

				// Filtramos por tipo de producto
				Set<TipoProducto> tiposFiltro = filtroProductosVenta.getTipoFiltrado();
				if (!tiposFiltro.isEmpty()) {
					boolean coincideTipo = (tiposFiltro.contains(TipoProducto.COMIC) && p instanceof Comic)
							|| (tiposFiltro.contains(TipoProducto.JUEGO_DE_MESA) && p instanceof JuegoDeMesa)
							|| (tiposFiltro.contains(TipoProducto.FIGURA) && p instanceof Figura);
					if (!coincideTipo)
						continue;
				}
				// Filtramos por categoria
				Set<Categoria> categoriasFiltro = filtroProductosVenta.getCategoriasFiltradas();
				if (!categoriasFiltro.isEmpty()) {
					boolean pertenceAAlguna = false;
					for (Categoria c : p.getCategorias()) {
						if (categoriasFiltro.contains(c)) {
							pertenceAAlguna = true;
							break;
						}
					}
					if (!pertenceAAlguna)
						continue;
				}

				// Filtramos por precio
				if (p.getPrecio() < filtroProductosVenta.getPrecioMin()
						|| p.getPrecio() > filtroProductosVenta.getPrecioMax())
					continue;

				// Filtramos por puntacion
				double puntuacion = p.obtenerPuntuacionMedia();
				if (puntuacion < filtroProductosVenta.getPuntuacionMin()
						|| puntuacion > filtroProductosVenta.getPuntuacionMax())
					continue;

				// Filtramos por descuentos
				Set<TipoDescuento> filtroDescuentos = filtroProductosVenta.getDescuentoFiltrado();
				if (!filtroDescuentos.isEmpty()) {
					Descuento descuentoP = p.getDescuento();
					if (descuentoP == null)
						continue;
					boolean coincideDescuento = (filtroDescuentos.contains(TipoDescuento.CANTIDAD)
							&& descuentoP instanceof Cantidad)
							|| (filtroDescuentos.contains(TipoDescuento.PRECIO) && descuentoP instanceof Precio)
							|| (filtroDescuentos.contains(TipoDescuento.REBAJA) && descuentoP instanceof RebajaUmbral)
							|| (filtroDescuentos.contains(TipoDescuento.REGALO) && descuentoP instanceof Regalo);
					if (!coincideDescuento)
						continue;
				}
			}
			resultado.add(p);
		}

		// Ordenamos segun el filtro que tenga el cliente
		if (filtroProductosVenta != null) {
			boolean asc = filtroProductosVenta.isOrdenAscendente();
			boolean porPrecio = filtroProductosVenta.isOrdenarPorPrecio();
			boolean porPuntuacion = filtroProductosVenta.isOrdenarPorPuntuacion();

			if (porPrecio && porPuntuacion) {
				// Ordena primero por precio, luego por puntuación como desempate
				resultado.sort((a, b) -> {
					int cmpPrecio = Double.compare(a.getPrecio(), b.getPrecio());
					if (cmpPrecio != 0)
						return asc ? cmpPrecio : -cmpPrecio;
					int cmpPunt = Double.compare(a.obtenerPuntuacionMedia(), b.obtenerPuntuacionMedia());
					return asc ? cmpPunt : -cmpPunt;
				});
			} else if (porPrecio) {
				resultado.sort((a, b) -> {
					int cmp = Double.compare(a.getPrecio(), b.getPrecio());
					return asc ? cmp : -cmp;
				});
			} else if (porPuntuacion) {
				resultado.sort((a, b) -> {
					int cmp = Double.compare(a.obtenerPuntuacionMedia(), b.obtenerPuntuacionMedia());
					return asc ? cmp : -cmp;
				});
			}
			// Si ninguno está activo, se devuelve en el orden natural del Set
		}
		return resultado;
	}

	/**
	 * Obtener productos intercambio filtrados.
	 *
	 * @param prompt el prompt
	 * @return el list
	 */
	public List<ProductoSegundaMano> obtenerProductosIntercambioFiltrados(String prompt) {
		List<ProductoSegundaMano> resultado = new ArrayList<>();

		for (ProductoSegundaMano p : productosSegundaMano) {

			// Solo mostramos productos validados
			if (!p.isValidado())
				continue;

			// Solo mostramos productos disponibles
			if (p.estaBloqueado())
				continue;

			// Filtro por prompt (nombre o descripción)
			if (prompt != null && !prompt.trim().isEmpty()) {
				String palabra = prompt.toLowerCase();
				boolean boolNombre = p.getNombre().toLowerCase().contains(palabra);
				boolean boolDescripcion = p.getDescripcion() != null
						&& p.getDescripcion().toLowerCase().contains(palabra);
				if (!boolNombre && !boolDescripcion)
					continue;
			}

			if (filtroProductosSegundaMano != null) {
				// Filtro por valor estimado
				double precio = p.getDatosValidacion().getPrecioEstimadoProducto();
				if (precio < filtroProductosSegundaMano.getValorMin())
					continue;
				if (precio > filtroProductosSegundaMano.getValorMax())
					continue;

				// Filtro por estado de conservación
				Set<EstadoConservacion> estadosFiltro = filtroProductosSegundaMano.getEstadosFiltrados();
				if (!estadosFiltro.isEmpty()) {
					EstadoConservacion estadoProducto = p.getDatosValidacion().getEstadoConservacion();
					if (!estadosFiltro.contains(estadoProducto))
						continue;
				}
			}
			resultado.add(p);
		}

		// Ordenación por valor estimado
		if (filtroProductosSegundaMano != null) {
			boolean asc = filtroProductosSegundaMano.isOrdenAscendente();
			resultado.sort((a, b) -> {
				double precioA = a.getDatosValidacion().getPrecioEstimadoProducto();
				double precioB = b.getDatosValidacion().getPrecioEstimadoProducto();
				int cmp = Double.compare(precioA, precioB);
				return asc ? cmp : -cmp;
			});
		}
		return resultado;
	}

	/**
	 * Devuelve el nombres categorias.
	 *
	 * @return el nombres categorias
	 */
	public String getNombresCategorias() {
		String total = "";
		for (Categoria cat : this.categoriasTienda) {
			total += cat.getNombre() + "\n";
		}
		return total;
	}

	/**
	 * Devuelve el categorias tienda.
	 *
	 * @return the categoriasTienda
	 */
	public Set<Categoria> getCategoriasTienda() {
		return categoriasTienda;
	}

	/**
	 * Devuelve el productos nuevos.
	 *
	 * @return the productosNuevos
	 */
	public Set<LineaProductoVenta> getProductosNuevos() {
		return productosNuevos;
	}

	/**
	 * Devuelve el primer lanzamiento.
	 *
	 * @return the primerLanzamiento
	 */
	public DateTimeSimulado getPrimerLanzamiento() {
		return primerLanzamiento;
	}

	/**
	 * Devuelve el ultimo lanzamiento.
	 *
	 * @return the ultimoLanzamiento
	 */
	public DateTimeSimulado getUltimoLanzamiento() {
		return ultimoLanzamiento;
	}

	/**
	 * Buscar producto nuevo.
	 *
	 * @param promptId el prompt id
	 * @return el linea producto venta
	 */
	public LineaProductoVenta buscarProductoNuevo(int promptId) {
		for (LineaProductoVenta p : productosNuevos) {
			if (p.getID() == promptId) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Buscar producto intercambio.
	 *
	 * @param promptId el prompt id
	 * @return el producto segunda mano
	 */
	public ProductoSegundaMano buscarProductoIntercambio(int promptId) {
		for (ProductoSegundaMano p : productosSegundaMano) {
			if (p.getID() == promptId) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Obtener productos nuevos gestion.
	 *
	 * @param prompt el prompt
	 * @return el list
	 */
	public List<LineaProductoVenta> obtenerProductosNuevosGestion(String prompt) {
		List<LineaProductoVenta> resultado = new ArrayList<>();

		for (LineaProductoVenta p : productosNuevos) {
			// Filtramos por el prompt (nombre y descripcion)
			if (prompt != null && !prompt.trim().isEmpty()) {
				String palabra = prompt.toLowerCase();
				boolean boolNombre = p.getNombre().toLowerCase().contains(palabra);
				boolean boolDescripcion = p.getDescripcion() != null
						&& p.getDescripcion().toLowerCase().contains(palabra);
				if (!boolNombre && !boolDescripcion)
					continue;
			}

			if (filtroProductosGestion != null) {
				// Filtramos por tipo de producto
				Set<TipoProducto> tiposFiltro = filtroProductosGestion.getTipoFiltrado();
				if (!tiposFiltro.isEmpty()) {
					boolean coincideTipo = (tiposFiltro.contains(TipoProducto.COMIC) && p instanceof Comic)
							|| (tiposFiltro.contains(TipoProducto.FIGURA) && p instanceof Figura)
							|| (tiposFiltro.contains(TipoProducto.JUEGO_DE_MESA) && p instanceof JuegoDeMesa);
					if (!coincideTipo)
						continue;
				}

				// Filtramos por categoria
				Set<Categoria> categoriasFiltro = filtroProductosGestion.getCategoriasFiltradas();
				if (!categoriasFiltro.isEmpty()) {
					boolean perteneceAAlguna = false;
					for (Categoria c : p.getCategorias()) {
						if (categoriasFiltro.contains(c)) {
							perteneceAAlguna = true;
							break;
						}
					}
					if (!perteneceAAlguna)
						continue;
				}
			}
			resultado.add(p);
		}
		// Ordenacion
		if (filtroProductosGestion != null) {
			boolean asc = filtroProductosGestion.isOrdenAscendente();
			resultado.sort((a, b) -> {
				int cmp = a.getNombre().compareToIgnoreCase(b.getNombre());
				return asc ? cmp : -cmp;
			});
		}
		return resultado;
	}

}
