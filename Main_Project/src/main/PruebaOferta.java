package main;


import java.io.File;
import java.util.*;

import aplicacion.*;
import notificacion.NotificacionCliente;
import producto.*;
import solicitud.Oferta;
import solicitud.SolicitudIntercambio;
import solicitud.SolicitudValidacion;
import tiempo.DateTimeSimulado;
import usuario.*;

public class PruebaOferta {
	public static void main(String[] args) {
		Gestor gestor;
		ClienteRegistrado cliente;
		Empleado empleado;
		Usuario usuarioActual;
		Aplicacion app = Aplicacion.getInstancia();
		
		app.iniciarSesion("gestor", "123456");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof Gestor) {
			gestor = (Gestor)usuarioActual;
		}
		else {
			return;
		}
		
		gestor.crearEmpleado("Federico", "132435468B");
		
		Empleado Federico=null;
		List<Usuario> usuariosAplicacion = app.getUsuariosRegistrados();
		for(Usuario u : usuariosAplicacion) {
			if(u.getNombreUsuario() == "Federico") {
				Federico = (Empleado)u;
			}
		}
		gestor.añadirPermiso(Federico, Permiso.VALIDACIONES);
		gestor.añadirPermiso(Federico, Permiso.INTERCAMBIOS);
		
		app.cerrarSesion();
		
		app.crearCuenta("Matteo", "123456789B", "Artunedo");
		app.crearCuenta("Rodrigo", "123456789C", "Diaz");
		
		app.iniciarSesion("Matteo", "Artunedo");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		
		cliente.añadirProductoACarteraDeIntercambio("Peluche de perro", "Es un peluche muy bonito y suavecito", null);
		
		app.cerrarSesion();
		
		app.iniciarSesion("Rodrigo", "Diaz");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		
		cliente.añadirProductoACarteraDeIntercambio("Camion de bomberos", "Un camion con 4 ruedas, es increible!", null);
		
		app.cerrarSesion();
		
		app.iniciarSesion("Federico", "123456");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof Empleado) {
			empleado = (Empleado)usuarioActual;
		}
		else {
			return;
		}
		
		List<SolicitudValidacion> solicitudes = GestorSolicitudes.getInstancia().getValidaciones();
		empleado.validarProducto(solicitudes.get(0), 2.4, 10.0, EstadoConservacion.MUY_BUENO);
		empleado.validarProducto(solicitudes.get(1), 1.0, 15.0, EstadoConservacion.MUY_USADO);
		
		
		app.cerrarSesion();
		
		app.iniciarSesion("Rodrigo", "Diaz");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		
		List<ProductoSegundaMano> productosRodrigo = new ArrayList<>(cliente.getCartera().getProductos());
		cliente.pagarValidacion(productosRodrigo.get(0).getSolicitudValidacion(), "1234567890123456", "123", new DateTimeSimulado());
		
		app.cerrarSesion();
		app.iniciarSesion("Matteo", "Artunedo");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		
		List<ProductoSegundaMano> productosMatteo = new ArrayList<>(cliente.getCartera().getProductos());
		cliente.pagarValidacion(productosMatteo.get(0).getSolicitudValidacion(), "1234567890123456", "123", new DateTimeSimulado());
		
		Set<ProductoSegundaMano> sMatteo = new HashSet<>(productosMatteo);
		Set<ProductoSegundaMano> sRodrigo = new HashSet<>(productosRodrigo);
		usuariosAplicacion = app.getUsuariosRegistrados();
		ClienteRegistrado Rodrigo=null;
		for(Usuario u : usuariosAplicacion) {
			if(u.getNombreUsuario() == "Rodrigo") {
				Rodrigo = (ClienteRegistrado)u;
			}
		}
		cliente.realizarOferta(sMatteo, sRodrigo, Rodrigo);
		
		app.cerrarSesion();
		app.iniciarSesion("Rodrigo", "Diaz");
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		List<Oferta> ofertasRecibidasRodrigo = cliente.getOfertasRecibidas();
		cliente.aceptarOferta(ofertasRecibidasRodrigo.get(0));
		
		
		app.cerrarSesion();
		app.iniciarSesion("Federico", "123456");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof Empleado) {
			empleado = (Empleado)usuarioActual;
		}
		else {
			return;
		}
		
		
		app.cerrarSesion();
		app.iniciarSesion("Matteo", "Artunedo");
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		List<NotificacionCliente> notifsMatteo = cliente.getNotificaciones();
		
		
		app.cerrarSesion();
		app.iniciarSesion("Rodrigo", "Diaz");
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof ClienteRegistrado) {
			cliente = (ClienteRegistrado)usuarioActual;
		}
		else {
			return;
		}
		List<NotificacionCliente> notifsRodrigo = cliente.getNotificaciones();
		
		System.out.println(notifsMatteo);
		System.out.println(notifsRodrigo);
		
		Scanner sc = new Scanner(System.in);
		
		String codigoMatteo = sc.next();
		String codigoRodrigo = sc.next();
		
		app.cerrarSesion();
		app.iniciarSesion("Federico", "123456");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof Empleado) {
			empleado = (Empleado)usuarioActual;
		}
		else {
			return;
		}
		
		List<SolicitudIntercambio> listaIntercambios = GestorSolicitudes.getInstancia().getIntercambios();
		SolicitudIntercambio sol = listaIntercambios.get(0);
		empleado.aprobarIntercambio(sol, codigoMatteo, codigoRodrigo);
	}
}
