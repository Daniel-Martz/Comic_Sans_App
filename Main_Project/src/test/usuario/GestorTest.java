package test.usuario;
import usuario.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import aplicacion.Aplicacion;

class GestorTest {
	@Test
	void testCrearEmpleado() {
    Aplicacion app = Aplicacion.getInstancia();
    Gestor g = (Gestor)app.getUsuarioActual();
    g.crearEmpleado("Juan", "011222233A");
    app.iniciarSesion("Juan", "123456");
    assertTrue(app.getUsuarioActual() instanceof Empleado);
	}

	@Test
	void testEliminarEmpleado() {
		Aplicacion app = Aplicacion.getInstancia();
    Gestor g = (Gestor)app.getUsuarioActual();
    g.crearEmpleado("Juan", "011222233A");
    app.iniciarSesion("Juan", "123456");
    g.eliminarEmpleado(e);
    assertTrue(app.getUsuariosRegistrados().size() == 1);

	}

}
