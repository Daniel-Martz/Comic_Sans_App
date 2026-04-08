package producto.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ LineaProductoVentaTest.class, PackTest.class, ProductoSegundaManoTest.class })
/*
 * ejecuta todos los tests de Producto
 */
public class AllTestsProducto {

}
