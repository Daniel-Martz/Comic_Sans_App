package solicitud.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ SolicitudIntercambioTest.class, SolicitudPedidoTest.class, SolicitudValidacionTest.class })
/*
 * Ejecuta todos los tests de solicitud
 */
public class AllTestsSolicitud {

}
