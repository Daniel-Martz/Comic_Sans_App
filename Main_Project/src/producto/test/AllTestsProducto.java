package producto.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ LineaProductoVentaTest.class, PackTest.class, ProductoSegundaManoTest.class })
public class AllTestsProducto {

}
