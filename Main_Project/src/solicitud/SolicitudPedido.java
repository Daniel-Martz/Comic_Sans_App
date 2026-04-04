package solicitud;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

import org.junit.jupiter.api.DisplayNameGenerator.Simple;

import categoria.Categoria;
import descuento.*;
import producto.LineaProductoVenta;
import usuario.ClienteRegistrado;

public class SolicitudPedido extends Solicitud{

	private Map<LineaProductoVenta, Integer> productosDiferentes = new HashMap<>();
	private Map<SimpleEntry<LineaProductoVenta, Integer>, Double> productosRecaudacion = new HashMap<>();
	private ClienteRegistrado cliente;
	private Pago pagoPedido;
	private EstadoPedido estado;
	

	public SolicitudPedido(ClienteRegistrado cliente, Map<LineaProductoVenta, Integer> productos) {
		super();
		this.cliente = cliente;
		this.productosDiferentes.putAll(productos);
    calcularRecaudaciones(productos);
		this.estado = EstadoPedido.PENDIENTE_DE_PAGO;
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
          categoriaDescontada = c;
        }
      } 
      //Indentificamos si el descuento es del producto o de la categoría. 
      //Si no hay descuento, insertamos el producto directamente en el mapa de la recaudacion de los productos
      if(categoriaDescontada != null){
        descuentoProducto = categoriaDescontada.getDescuento(); 
      }else if(l.getDescuento() != null){
        descuentoProducto = l.getDescuento();
      }else{
        //Si el producto no tiene ningún descuento, calculamos su recaudacion como su precio por las unidades
        productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l));
        continue;
      }

      if(descuentoProducto instanceof Precio){
        productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l) * ((Precio)descuentoProducto).getPorcentajeRebaja() / 100);
      }else if(descuentoProducto instanceof Cantidad){
    	int unidadesRegaladas = (productosDiferentes.get(l)  / ((Cantidad)descuentoProducto).getNumeroComprados()) * ((Cantidad)descuentoProducto).getNumeroRecibidos();
        productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, ((productosDiferentes.get(l)%2) + unidadesRegaladas)), 
        		l.getPrecio() * productosDiferentes.get(l)); 
      } else if(descuentoProducto instanceof Regalo || descuentoProducto instanceof RebajaUmbral){
        //Nos guardamos en un mapa el descuento asociado a los productos que lo tienen para tratar el descuento posteriormente
        LinkedList<LineaProductoVenta> productosConDescuento = descuentosPendientes.computeIfAbsent(descuentoProducto, k -> new LinkedList<>());
        productosConDescuento.add(l);
      }
    } 
    //Vamos a tratar aquellos descuentos que no se han aplicado todavía por ser grupales
    for(Descuento d : descuentosPendientes.keySet()){
      if(d instanceof Regalo){
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
        }
      }else if(d instanceof RebajaUmbral){
        Regalo r = (Regalo)d;
        //Obtenemos qué umbral tienen que sobrepasar los productos para que se aplique el descuento
        double umbral = r.getUmbral();
        double total = 0;
        //Calculamos cuanto suman los productos que tienen el descuento para ver si alcanzan el umbral
        for(LineaProductoVenta l : descuentosPendientes.get(d)){
          total += l.getPrecio() * productosDiferentes.get(l);
        }
        //Si hemos superado el umbral, aplicamos la rebaja a los productos
        if(total >= umbral){
          
          //Si hemos superado el umbral, añadimos los productos al mapa de recaudación con su precio normal 
          for(LineaProductoVenta l : descuentosPendientes.get(d)){
            productosRecaudacion.put(new SimpleEntry<LineaProductoVenta, Integer>(l, productosDiferentes.get(l)), l.getPrecio() * productosDiferentes.get(l));
          }
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
		for (SimpleEntry<LineaProductoVenta, Integer> e: productosRecaudacion.keySet()){
			precioFinal += productosRecaudacion.get(e);
		}
		return precioFinal;
	}
	
	public void añadirPagoPedido(Pago pagoPedido) {
		this.pagoPedido = pagoPedido;
	}
	
	public boolean pagado()	{
		if(this.pagoPedido == null) {
			return false;
		}
		return true;
	}
	
	public void actualizarEstado(EstadoPedido estado) {
	    if (estado == null) {
	        throw new IllegalArgumentException("El estado no puede ser null");
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
	
	
}
