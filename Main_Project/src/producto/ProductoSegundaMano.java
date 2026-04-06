package producto;
import java.io.File;
import solicitud.*;
import java.util.*;
import usuario.ClienteRegistrado;
import aplicacion.Aplicacion;
import aplicacion.GestorSolicitudes;

public class ProductoSegundaMano extends Producto{
	private boolean validado;
	private Oferta ofertaRecibida;
	private Oferta ofertaEnviada;
	private final SolicitudValidacion solicitudValidacion;
	private DatosValidacion datosValidacion;
	private ClienteRegistrado clienteProducto;
	
	public ProductoSegundaMano(String nombre, String descripcion, File foto, ClienteRegistrado cliente)
	{
		super(nombre, descripcion, foto);
		this.validado = false;
		this.clienteProducto = cliente;
		this.solicitudValidacion = new SolicitudValidacion(this, cliente);
		Aplicacion.getInstancia().getGestorSolicitud().añadirSolicitudValidacion(solicitudValidacion);
	}

	public ClienteRegistrado getClienteProducto() {
		return clienteProducto;
	}

	public boolean isValidado() {
		return validado;
	}

	public Oferta getOfertaRecibida() {
		return ofertaRecibida;
	}

	public void addOfertaRecibida(Oferta ofertaRecibida) {
		if (this.estaBloqueado()) throw new IllegalStateException("El producto se encuentra bloqueado");
		this.ofertaRecibida = ofertaRecibida;
	}

	public void eliminarOfertaRecibida() {
		this.ofertaRecibida = null;
	}

	public Oferta getOfertaEnviada() {
		return ofertaEnviada;
	}

	public void addOfertaEnviada(Oferta ofertaEnviada) {
		if (this.estaBloqueado()) throw new IllegalStateException("El producto se encuentra bloqueado");
		this.ofertaEnviada = ofertaEnviada;
	}

	public void eliminarOfertaEnviada() {
		this.ofertaEnviada = null;
	}

	public SolicitudValidacion getSolicitudValidacion() {
		return solicitudValidacion;
	}

	public DatosValidacion getDatosValidacion() {
		return datosValidacion;
	}

	public void setDatosValidacion(DatosValidacion datosValidacion) {
		this.datosValidacion = datosValidacion;
	}
		
	public void validarProducto(double precio, EstadoConservacion estado)
	{
		this.validado = true;
		this.datosValidacion = new DatosValidacion(precio, estado);
	}
	
	public boolean estaBloqueado() {
		return (ofertaRecibida != null || ofertaEnviada != null || this.datosValidacion == null);
	}

	@Override
	public String toString() {
		return "ProductoSegundaMano [validado=" + validado + ", ofertaRecibida=" + ofertaRecibida + ", ofertaEnviada="
				+ ofertaEnviada + ", datosValidacion=" 
				+ datosValidacion + "]";
	}
	
	
	// public boolean pasaFiltro(Filtro filtroIntercambio, String prompt)
	
}
