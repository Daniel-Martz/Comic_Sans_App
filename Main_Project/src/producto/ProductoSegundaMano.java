package producto;
import java.io.File;
import solicitud.*;
import java.util.*;

public class ProductoSegundaMano extends Producto{
	private boolean validado;
	private Oferta ofertaRecibida;
	private Oferta ofertaEnviada;
	private SolicitudValidacion solicitudValidacion;
	private DatosValidacion datosValidacion;
	
	public ProductoSegundaMano(String nombre, String descripcion, File foto)
	{
		super(nombre, descripcion, foto);
		this.validado = false;
	}

	public boolean isValidado() {
		return validado;
	}

	public Oferta getOfertaRecibida() {
		return ofertaRecibida;
	}

	public void setOfertaRecibida(Oferta ofertaRecibida) {
		this.ofertaRecibida = ofertaRecibida;
	}

	public Oferta getOfertaEnviada() {
		return ofertaEnviada;
	}

	public void setOfertaEnviada(Oferta ofertaEnviada) {
		this.ofertaEnviada = ofertaEnviada;
	}

	public SolicitudValidacion getSolicitudValidacion() {
		return solicitudValidacion;
	}

	public void setSolicitudValidacion(SolicitudValidacion solicitudValidacion) {
		this.solicitudValidacion = solicitudValidacion;
	}

	public DatosValidacion getDatosValidacion() {
		return datosValidacion;
	}

	public void setDatosValidacion(DatosValidacion datosValidacion) {
		this.datosValidacion = datosValidacion;
	}
		
	public boolean validarProducto(int precio, EstadoConservacion estado)
	{
		this.validado = true;
		this.datosValidacion = new DatosValidacion(precio, estado);
		return true;
	}
	
	// public boolean pasaFiltro(Filtro filtroIntercambio, String prompt)
	
}
