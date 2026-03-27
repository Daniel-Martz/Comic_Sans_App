package producto;
import java.io.File;
import solicitud.*;
import java.util.*;

public class ProductoSegundaMano extends Producto{
	private boolean validado;
	private Oferta ofertaRecibida;
	private Oferta ofertaEnviada;
	private final SolicitudValidacion solicitudValidacion;
	private DatosValidacion datosValidacion;
	
	public ProductoSegundaMano(String nombre, String descripcion, File foto)
	{
		super(nombre, descripcion, foto);
		this.validado = false;
		this.solicitudValidacion = new SolicitudValidacion(this);
	}

	public boolean isValidado() {
		return validado;
	}

	public Oferta getOfertaRecibida() {
		return ofertaRecibida;
	}

	public void addOfertaRecibida(Oferta ofertaRecibida) {
		if (this.estaBloqueado()) return;
		this.ofertaRecibida = ofertaRecibida;
	}

	public void eliminarOfertaRecibida() {
		this.ofertaRecibida = null;
	}

	public Oferta getOfertaEnviada() {
		return ofertaEnviada;
	}

	public void addOfertaEnviada(Oferta ofertaEnviada) {
		if (this.estaBloqueado()) return;
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
		return (ofertaRecibida != null || ofertaEnviada != null);
	}
	
	
	// public boolean pasaFiltro(Filtro filtroIntercambio, String prompt)
	
}
