package aplicacion;

import java.util.*;
import descuento.*;
import categoria.*;
import producto.*;
import solicitud.*;
import usuario.ClienteRegistrado;
import usuario.Usuario;
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
		
		//Para añadir el producto al ranking de todos los usuarios
		for(ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			u.getInteres().actualizarInteresNuevoVenta(p);
		}
	}

	public void añadirPack(LineaProductoVenta pack, Map<LineaProductoVenta, Integer> prods) {
		if (pack == null) {
			throw new IllegalArgumentException("El pack introducido no es valido");
		}
		if (prods == null) {
			throw new IllegalArgumentException("La lista de productos introducidos no es válida");
		}

		pack.añadirProductosPack(prods);

		productosNuevos.add(pack);
	}

	public void eliminarProducto(Producto p) {
		productosNuevos.remove(p);
	}

	public void modificarProducto(LineaProductoVenta p, String nuevoNombre, String nuevaDescripcion, Integer nuevoStock,
	Double nuevoPrecio, File nuevaFoto) {
		if(nuevoNombre != null) {
			p.setNombre(nuevoNombre);
		}
		if(nuevaDescripcion != null) {
			p.setDescripcion(nuevaDescripcion);
		}
		if(nuevoStock != null) {
			p.setStock(nuevoStock);
		}
		if(nuevoPrecio != null) {
			p.setPrecio(nuevoPrecio);
		}
		if(nuevaFoto != null) {
			p.setFoto(nuevaFoto);
		}
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
		
		//Añadir la categoria al ranking de todos los clientes
		for(ClienteRegistrado u : Aplicacion.getInstancia().getClientesRegistrados()) {
			u.getInteres().actualizarInteresCategoriaNueva(c);
		}
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
		return new ArrayList<>();
	}

	public List<ProductoSegundaMano> obtenerProductosIntercambioFiltrados(String prompt) {
		return new ArrayList<>();
	}

	public List<LineaProductoVenta> obtenerProductosAModificarFiltrados(String prompt) {
		return new ArrayList<>();
	}

  public String getNombresCategorias(){
    String total = "";
    for(Categoria cat : this.categoriasTienda){
      total += cat.getNombre() + "\n";
    }
    return total;
  }

  /**
   * @return the categoriasTienda
   */
  public Set<Categoria> getCategoriasTienda() {
	return categoriasTienda;
  }

  /**
   * @return the productosNuevos
   */
  public Set<LineaProductoVenta> getProductosNuevos() {
	return productosNuevos;
  }
  
  

}
