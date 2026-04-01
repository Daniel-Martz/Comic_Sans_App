package aplicacion;

import java.util.*;
import descuento.*;
import categoria.*;
import producto.*;
import solicitud.*;
import filtro.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Catalogo {

	private static Catalogo instancia;

	private Set<Descuento> descuentos = new HashSet<>();
	private Set<Categoria> categoriasTienda = new HashSet<>();
	private Set<LineaProductoVenta> productosNuevos = new HashSet<>();
	private Set<ProductoSegundaMano> productosSegundaMano = new HashSet<>();

	private FiltroVenta filtroProductosGestion;
	private FiltroIntercambio filtroProductosSegundaMano;
	private FiltroVentaCliente filtroProductosVenta;

	private Catalogo() {

	}

	public static Catalogo getInstancia() {
		if (instancia == null) {
			instancia = new Catalogo();
		}
		return instancia;
	}

	// Métodos para los descuentos
	public void añadirDescuento(Descuento d) {
		if (d == null) {
			throw new IllegalArgumentException("El descuento introducido no es valido");
		}

		descuentos.add(d);
	}

	public void eliminarDescuento(Descuento d) {
		descuentos.remove(d);
	}

	// Métodos para los productos
	public void añadirProducto(LineaProductoVenta p) {
		if (p == null) {
			throw new IllegalArgumentException("El producto introducido no es valido");
		}

		productosNuevos.add(p);
	}

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

	public void eliminarProducto(Producto p) {
		productosNuevos.remove(p);
	}

	public void añadirProductosDesdeFichero(File f) throws IOException {
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
				this.productosNuevos.add(nuevo);
			}
		}
	}

	// Métodos para las categorías
	public void añadirCategoria(Categoria c) {
		if (c == null) {
			return;
		}
		categoriasTienda.add(c);
	}

	public void eliminarCategoria(Categoria c) {
		if (c == null) {
			return;
		}
		categoriasTienda.remove(c);
	}

	public void modificarCategoria(Categoria c, String nombreNuevo) {
		if (c == null || nombreNuevo == null || nombreNuevo.trim().isEmpty()) {
			return;
		}
		if (categoriasTienda.contains(c)) {
			c.setNombre(nombreNuevo);
		}
	}

	// Busca una categoria por nombre exacto
	public Categoria buscarCategoriaPorNombre(String nombreCategoria) {
		for (Categoria c : categoriasTienda) {
			if (c.getNombre().equals(nombreCategoria)) {
				return c;
			}
		}
		return null;
	}

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
			throw new IllegalStateException("El producto '" + p.getNombre() + "' ya tiene un descuento activo.");
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
	}

	public void eliminarDescuento(LineaProductoVenta p, Descuento d) {
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

	public void aplicarDescuento(Descuento d, Categoria c) {
		if (d == null || c == null) {
			throw new IllegalArgumentException("El descuento y la categoría no pueden ser nulos.");
		}

		if (!categoriasTienda.contains(c) || !descuentos.contains(d)) {
			throw new IllegalStateException("El descuento y la categoria debe perternecer al catalogo");
		}

		if (c.getDescuento() != null) {
			throw new IllegalStateException("La categoría '" + c.getNombre() + "' ya tiene un descuento activo.");
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
	}

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

	// Métodos filtros
	public void cambiarFiltroVenta(FiltroVentaCliente filtro) {
		this.filtroProductosVenta = filtro;
	}

	public void cambiarFiltroGestion(FiltroVenta filtro) {
		this.filtroProductosGestion = filtro;
	}

	public void cambiarFiltroIntercambio(FiltroIntercambio filtro) {
		this.filtroProductosSegundaMano = filtro;
	}

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
							|| (filtroDescuentos.contains(TipoDescuento.REBAJA) && descuentoP instanceof Rebaja)
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

	public String getNombresCategorias() {
		String total = "";
		for (Categoria cat : this.categoriasTienda) {
			total += cat.getNombre() + "\n";
		}
		return total;
	}

	public LineaProductoVenta buscarProductoNuevo(int promptId) {
		for (LineaProductoVenta p : productosNuevos) {
			if (p.getID() == promptId) {
				return p;
			}
		}
		return null;
	}

	public ProductoSegundaMano buscarProductoIntercambio(int promptId) {
		for (ProductoSegundaMano p : productosSegundaMano) {
			if (p.getID() == promptId) {
				return p;
			}
		}
		return null;
	}

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
