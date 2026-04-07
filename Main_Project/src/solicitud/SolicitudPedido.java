package solicitud;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import aplicacion.Aplicacion;
import categoria.Categoria;
import descuento.*;
import producto.LineaProductoVenta;
import usuario.ClienteRegistrado;
import tiempo.DateTimeSimulado;

/**
 * Clase que representa una solicitud de pedido realizada por un cliente registrado.
 * @author Matteo Artuñedo, Rodrigo Diaz y Daniel Martinez
 * @version 1.0
 */
public class SolicitudPedido extends Solicitud implements Caducable {

    private static final long serialVersionUID = 1L;

	/**
     * Mapa que almacena los productos distintos del pedido y la cantidad de cada uno.
     */
	private Map<LineaProductoVenta, Integer> productosDiferentes = new HashMap<>();

    /**
     * Mapa que almacena las entradas de productos con su cantidad y la recaudación calculada
     * después de aplicar los descuentos correspondientes.
     */
	private Map<SimpleEntry<LineaProductoVenta, Integer>, Double> productosRecaudacion = new HashMap<>();

    /**
     * Cliente que realiza el pedido.
     */
	private ClienteRegistrado cliente;

    /**
     * Objeto que representa el pago asociado al pedido.
     */
	private Pago pagoPedido;

    /**
     * Estado actual del pedido.
     */
	private EstadoPedido estado;

    /**
     * Fecha de realización del pedido.
     */
    private DateTimeSimulado fechaRealizacion;

    /**
     * Constructor de la clase.
     * Inicializa el pedido con un cliente, los productos seleccionados,
     * calcula las recaudaciones y establece el estado inicial.
     * 
     * @param cliente Cliente que realiza el pedido.
     * @param productos Mapa de productos y sus cantidades solicitadas.
     */
	public SolicitudPedido(ClienteRegistrado cliente, Map<LineaProductoVenta, Integer> productos) {
		super();
		this.cliente = cliente;
		this.productosDiferentes.putAll(productos);
		calcularRecaudaciones(productos);
		this.estado = EstadoPedido.PENDIENTE_DE_PAGO;
		this.fechaRealizacion = new DateTimeSimulado();
	}

    /**
     * Calcula la recaudación de los productos del pedido aplicando los descuentos vigentes.
     * Diferencia entre descuentos individuales y grupales, y gestiona los productos con descuentos caducados.
     * 
     * @param productos Mapa de productos y sus cantidades.
     */
  private void calcularRecaudaciones(Map<LineaProductoVenta, Integer> productos){
    Map<Descuento, LinkedList<LineaProductoVenta>> descuentosPendientes = new HashMap<>();
    //Recorremos los descuentos de cada producto, tratando los que sean individuales
    //y guardando los que sean en conjunto para tratarlos posteriormente
    for(LineaProductoVenta l : productos.keySet()){
      Categoria categoriaDescontada = null;
      Descuento descuentoProducto = null;
      for(Categoria c : l.getCategorias()){
        if(c.getDescuento() != null){
          //Nos aseguramos de que los descuentos no hayan caducado a la hora de valorarlos
          if(c.getDescuento().haCaducado() == true){
            Aplicacion.getInstancia().getCatalogo().eliminarDescuento(c.getDescuento(), c);
          }else{
            categoriaDescontada = c;
          }
        }
      } 
      //Indentificamos si el descuento es del producto o de la categoría. 
      //Si la categoría tiene algún descuento, lo tomamos directamente como el descuento del producto
      if(categoriaDescontada != null){
        descuentoProducto = categoriaDescontada.getDescuento(); 
      //Si la cateogría no tiene descuento, pasamos a analizar el descuento del producto
      //Nos aseguramos de que los descuentos no hayan caducado a la hora de valorarlos
      }else if(l.getDescuento() != null && l.getDescuento().haCaducado() == true){
          //Si el descuento ha caducado, lo borramos e insertamos el producto en productosRecaudacion con su precio normal
          Aplicacion.getInstancia().getCatalogo().eliminarDescuento(l.getDescuento(), l);
          productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l));
          continue;
      }else if (l.getDescuento() != null && l.getDescuento().haCaducado() == false){
          descuentoProducto = l.getDescuento();
      //Si el producto no tiene ningún descuento, calculamos su recaudacion como su precio por las unidades
      }else{
        productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l));
        continue;
      }

      if(descuentoProducto instanceof Precio){
        productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l) * ((Precio)descuentoProducto).getPorcentajeRebaja() / 100);
      }else if(descuentoProducto instanceof Cantidad){
    	int unidadesRegaladas = (productosDiferentes.get(l)  / ((Cantidad)descuentoProducto).getNumeroComprados()) * ((Cantidad)descuentoProducto).getNumeroRecibidos();
        productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, ((productosDiferentes.get(l)%2) + unidadesRegaladas)), 
        		l.getPrecio() * productosDiferentes.get(l)); 
      } else if(descuentoProducto instanceof Regalo || descuentoProducto instanceof UmbralGasto){
        //Nos guardamos en un mapa el descuento asociado a los productos que lo tienen para tratar el descuento posteriormente
        LinkedList<LineaProductoVenta> productosConDescuento = descuentosPendientes.computeIfAbsent(descuentoProducto, k -> new LinkedList<>());
        productosConDescuento.add(l);
      }
    } 
    //Vamos a tratar aquellos descuentos que no se han aplicado todavía por ser grupales
    for(Descuento d : descuentosPendientes.keySet()){
      if(d instanceof RebajaUmbral){
    	  RebajaUmbral r = (RebajaUmbral)d;
        //Obtenemos qué umbral tienen que sobrepasar los productos para que se aplique el descuento
        double umbral = r.getUmbral();
        double total = 0;
        //Calculamos cuanto suman los productos que tienen el descuento para ver si alcanzan el umbral
        for(LineaProductoVenta l : descuentosPendientes.get(d)){
          total += l.getPrecio() * productosDiferentes.get(l);
        }
        //Si hemos superado el umbral, aplicamos la rebaja a los productos
        if(total >= umbral){
          for(LineaProductoVenta l : descuentosPendientes.get(d)){
            productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l) * ((RebajaUmbral)d).getPorcentajeRebaja() / 100);
          }
        }else 
        //Si no hemos superado el umbral, guardamos los productos con su precio normal
        {
        	for(LineaProductoVenta l : descuentosPendientes.get(d)){
            productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l));
        	}
        }
      }else if(d instanceof Regalo){
        Regalo r = (Regalo)d;
        //Obtenemos qué umbral tienen que sobrepasar los productos para que se aplique el descuento
        double umbral = r.getUmbral();
        double total = 0;
       
        //Añadimos los productos independientemente de que superen el umbral para el regalo o no
        for(LineaProductoVenta l : descuentosPendientes.get(d)){
            productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l));
          }
        
        //Calculamos cuanto suman los productos que tienen el descuento para ver si alcanzan el umbral
        for(LineaProductoVenta l : descuentosPendientes.get(d)){
          total += l.getPrecio() * productosDiferentes.get(l);
        }
        //Si hemos superado el umbral, aplicamos la rebaja a los productos
        if(total >= umbral){
          
          //Añadimos los regalos
          for(LineaProductoVenta l : r.getProductosRegalo().keySet()){
            //r.getProductosRegalo devuelve un mapa con cada LineaproductoVenta y la cantidad de productos regalados 
            //Ponemos que la recaudación de esos productos sea cero
            productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, r.getProductosRegalo().get(l)), 0.0);

          }

        }
      }
    }
  }

    /**
     * Devuelve un mapa inmutable con la recaudación calculada de cada producto del pedido.
     * 
     * @return Mapa de productos con cantidad y recaudación aplicada.
     */
  public Map<SimpleEntry<LineaProductoVenta, Integer>, Double> getRecaudacionProductos(){
    return Collections.unmodifiableMap(productosRecaudacion);
  }

    /**
     * Calcula el coste total del pedido sumando la recaudación de todos los productos.
     * 
     * @return Precio total del pedido.
     */
	public double getCostePedido() {
		double precioFinal= 0;
		for(Double precioPorProducto : productosRecaudacion.values()) {
			precioFinal += precioPorProducto;
		}
		return precioFinal;
	}

    /**
     * Actualiza el estado del pago del pedido.
     * 
     * @param estado Nuevo estado del pedido.
     */
	public void actualizarPagoPedido(EstadoPedido estado) {
		this.estado = estado;
	}

    /**
     * Añade un pago al pedido.
     * 
     * @param pagoPedido Objeto Pago asociado al pedido.
     */
	public void añadirPagoPedido(Pago pagoPedido) {
		this.pagoPedido = pagoPedido;

	}

    /**
     * Comprueba si el pedido ya ha sido pagado.
     * 
     * @return true si el pedido tiene un pago asociado, false en caso contrario.
     */
	public boolean pagado() {
		if (this.pagoPedido == null) {
			return false;
		}
		return true;
	}

    /**
     * Actualiza el estado del pedido, validando que no sea nulo ni
     * que se intente procesar un pedido pendiente de pago.
     * 
     * @param estado Nuevo estado a asignar.
     * @throws IllegalArgumentException si el estado es null.
     * @throws IllegalStateException si el pedido está pendiente de pago.
     */
	public void actualizarEstado(EstadoPedido estado) {
		if (estado == null) {
			throw new IllegalArgumentException("El estado no puede ser null");
		}
    if(this.estado == EstadoPedido.PENDIENTE_DE_PAGO){
      throw new IllegalStateException("El pedido todavía no se ha pagado y no se puede procesar");
    }
		this.estado = estado;
	}

    /**
     * Devuelve el estado actual del pedido.
     * 
     * @return Estado del pedido.
     */
	public EstadoPedido getEstado() {
		return estado;
	}

    /**
     * Devuelve el pago asociado al pedido.
     * 
     * @return Objeto Pago del pedido.
     */
	public Pago getPagoPedido() {
		return pagoPedido;
	}

    /**
     * Asigna un nuevo cliente al pedido.
     * 
     * @param cliente Cliente registrado que realiza el pedido.
     */
	public void setCliente(ClienteRegistrado cliente) {
		this.cliente = cliente;
	}

    /**
     * Devuelve los productos distintos del pedido junto con su cantidad.
     * 
     * @return Mapa de productos y cantidades.
     */
	public Map<LineaProductoVenta, Integer> getProductosDiferentes() {
		return productosDiferentes;
	}

    /**
     * Devuelve el cliente que realizó el pedido.
     * 
     * @return Cliente registrado del pedido.
     */
	public ClienteRegistrado getCliente() {
		return cliente;
	}

    /**
     * Comprueba si el pedido ha caducado.
     * Un pedido caduca si han pasado 7 días desde su realización
     * y todavía no se ha efectuado el pago.
     * 
     * @return true si el pedido ha caducado, false en caso contrario.
     */
  public boolean haCaducado(){
    //Si la fecha de realización de la oferta + 7 días es anterior a la fecha actual, la oferta ha caducado. 
    //Se tiene que cumplir también que no haya ya un intercambio confirmado para el producto
	fechaRealizacion.diasDespuesDeFecha(7);
    if(fechaRealizacion.diasDespuesDeFecha(7).compareTo(new DateTimeSimulado()) < 0 && this.estado == EstadoPedido.PENDIENTE_DE_PAGO){
      return true;
    }else{
      return false;
    }
  }
  
  @Override
  public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("----------------------------------------------------\n");
      sb.append(String.format("CLIENTE: %-15s | ESTADO: %s\n", 
                this.cliente.getNombreUsuario(), this.estado));
      sb.append("Fecha realización: ").append(this.fechaRealizacion.toStringFecha()).append("\n");
      sb.append("Resumen de productos:\n");
      
      for (Map.Entry<LineaProductoVenta, Integer> entry : productosDiferentes.entrySet()) {
          sb.append(String.format("  > %-30s [x%d]\n", 
                    entry.getKey().getNombre(), entry.getValue()));
      }
      
      sb.append(String.format("COSTE TOTAL DEL PEDIDO: %.2f €\n", getCostePedido()));
      sb.append("----------------------------------------------------");
      return sb.toString();
  }
}
