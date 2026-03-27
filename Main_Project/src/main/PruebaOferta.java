package main;


import aplicacion.*;
import usuario.*;

public class PruebaOferta {
	public static void main(String[] args) {
		Gestor gestor;
		Cliente cliente;
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
		if( usuarioActual instanceof Cliente) {
			cliente = (Cliente)usuarioActual;
		}
		else {
			return;
		}
		
		
	}
}
