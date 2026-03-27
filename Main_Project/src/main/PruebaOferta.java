package main;


import java.io.File;
import java.util.*;

import aplicacion.*;
import producto.*;
import solicitud.SolicitudValidacion;
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
		
		cliente.añadirProductoACarteraDeIntercambio("Peluche de perro", "Es un peluche muy bonito y savecito", null);
		
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
		empleado.validarProducto(solicitudes.get(0), 2.4, EstadoConservacion.MUY_BUENO);
		empleado.validarProducto(solicitudes.get(1), 1, EstadoConservacion.MUY_USADO);
		
		
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
		cliente.pagarValidacion(productosRodrigo.get(0).getSolicitudValidacion(), 0, 0, null);
		
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
		cliente.pagarValidacion(productosMatteo.get(0).getSolicitudValidacion(), 0, 0, null);
		
		
		cliente.realizarOferta(cliente.getCartera().getProductos(), app.getCatalogo().get, cliente);
		
		app.cerrarSesion();
		app.iniciarSesion("Federico", "Artunedo");
		
		usuarioActual = app.getUsuarioActual();
		if( usuarioActual instanceof Empleado) {
			empleado = (Empleado)usuarioActual;
		}
		else {
			return;
		}
		
		
	}
}
