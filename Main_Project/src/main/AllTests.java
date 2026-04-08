package main;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite Completa de Pruebas del Sistema")
@SelectPackages({
    "aplicacion.test", 
    "producto.test", 
    "usuario.test",
    "filtro.test",
    "notificacion.test",
    "descuento.test",
    "solicitud.test"
    
})
/*
 * Ejecuta todos los tests
 */
public class AllTests {

}