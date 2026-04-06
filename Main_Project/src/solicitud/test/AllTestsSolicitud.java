package solicitud.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ SolicitudIntercambioTest.class, SolicitudPedidoTest.class, SolicitudValidacionTest.class })
public class AllTestsSolicitud {

}
