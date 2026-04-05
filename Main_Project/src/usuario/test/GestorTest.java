package usuario.test;
import usuario.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import aplicacion.Aplicacion;

class GestorTest {
	@Test
	void testCrearEmpleado() {
    Aplicacion app = Aplicacion.getInstancia();
    app.iniciarSesion("gestor", "123456");
    Gestor g = (Gestor)app.getUsuarioActual();
    g.crearEmpleado("Juan", "011222233A");
    app.cerrarSesion();
    app.iniciarSesion("Juan", "123456");
    assertTrue(app.getUsuarioActual() instanceof Empleado);
    app.cerrarSesion();
	}

	@Test
	void testEliminarEmpleado() {
		Aplicacion app = Aplicacion.getInstancia();
    app.iniciarSesion("gestor", "123456");
    Gestor g = (Gestor)app.getUsuarioActual();
    Empleado e = g.crearEmpleado("Paco", "911222233A");
    app.cerrarSesion();
    app.iniciarSesion("Paco", "123456");
    g.eliminarEmpleado(e);
    assertTrue(app.getUsuariosRegistrados().size() == 1);

	}

}
