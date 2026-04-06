package solicitud;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import org.junit.jupiter.api.DisplayNameGenerator.Simple;

import aplicacion.Aplicacion;
import categoria.Categoria;
import descuento.*;
import producto.LineaProductoVenta;
import usuario.ClienteRegistrado;
import tiempo.DateTimeSimulado;

public class SolicitudPedido extends Solicitud implements Caducable{

	private Map<LineaProductoVenta, Integer> productosDiferentes = new HashMap<>();
	private Map<SimpleEntry<LineaProductoVenta, Integer>, Double> productosRecaudacion = new HashMap<>();
	private ClienteRegistrado cliente;
	private Pago pagoPedido;
	private EstadoPedido estado;
  private DateTimeSimulado fechaRealizacion;

	public SolicitudPedido(ClienteRegistrado cliente, Map<LineaProductoVenta, Integer> productos) {
		super();
		this.cliente = cliente;
		this.productosDiferentes.putAll(productos);
		calcularRecaudaciones(productos);
		this.estado = EstadoPedido.PENDIENTE_DE_PAGO;
		this.fechaRealizacion = new DateTimeSimulado();
	}

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
          //Si el descuento ha caducado, lo borramos e insertamos el producto en productowsRecaudacion con su precio normal
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

  public Map<SimpleEntry<LineaProductoVenta, Integer>, Double> getRecaudacionProductos(){
    return Collections.unmodifiableMap(productosRecaudacion);
  }

	public double getCostePedido() {
		double precioFinal= 0;
		for(Double precioPorProducto : productosRecaudacion.values()) {
			precioFinal += precioPorProducto;
		}
		return precioFinal;
	}

	public void actualizarPagoPedido(EstadoPedido estado) {
		this.estado = estado;
	}

	public void añadirPagoPedido(Pago pagoPedido) {
		this.pagoPedido = pagoPedido;
	}

	public boolean pagado() {
		if (this.pagoPedido == null) {
			return false;
		}
		return true;
	}

	public void actualizarEstado(EstadoPedido estado) {
		if (estado == null) {
			throw new IllegalArgumentException("El estado no puede ser null");
		}
    if(estado == EstadoPedido.PENDIENTE_DE_PAGO){
      throw new IllegalStateException("El pedido todavía no se ha pagado y no se puede procesar");
    }
		this.estado = estado;
	}

	public EstadoPedido getEstado() {
		return estado;
	}


	/**
	 * @return the pagoPedido
	 */
	public Pago getPagoPedido() {
		return pagoPedido;
	}

	public void setCliente(ClienteRegistrado cliente) {
		this.cliente = cliente;
	}

	public Map<LineaProductoVenta, Integer> getProductosDiferentes() {
		return productosDiferentes;
	}

	public ClienteRegistrado getCliente() {
		return cliente;
	}


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

}
