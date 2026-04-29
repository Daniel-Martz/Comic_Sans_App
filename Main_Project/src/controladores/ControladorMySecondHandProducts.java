package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.producto.ProductoSegundaMano;
import modelo.solicitud.SolicitudValidacion;
import modelo.usuario.ClienteRegistrado;
import vista.main.MainFrame;
import vista.userPanels.MySecondHandProductsPanel;
import vista.userWindows.VentanaNuevoProductoSegundaMano;
import vista.userWindows.VentanaPagoValidacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import controladores.ControladorNuevoProductoSegundaMano;

/**
 * Controlador para `MySecondHandProductsPanel`.
 *
 * Responsabilidades:
 *  - Registrar listeners en la vista (ADD NEW PRODUCT, PAY VALIDATION).
 *  - Ejecutar la lógica necesaria en el modelo (añadir producto, abrir ventana de pago).
 *  - Recargar la vista cuando cambian los productos.
 */
public class ControladorMySecondHandProducts implements ActionListener {

    private final MySecondHandProductsPanel vista;
    private final MainFrame parent; 

    public ControladorMySecondHandProducts(MySecondHandProductsPanel vista, MainFrame parent) {
        this.vista = vista;
        this.parent = parent;

        // Registrar listeners
        this.vista.addAddProductListener(e -> onAddNewProduct());
        // Registramos este controlador como listener global para los botones PAY_VALIDATION
        // (la vista guardará el listener y lo añadirá a los botones cuando los cree)
        this.vista.addPayValidationListener(this);

        // Cargar inicialmente los productos desde el modelo y poblar la vista
        cargarProductosDesdeModelo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        if (cmd.startsWith("PAY_VALIDATION")) {
            // Formato esperado: PAY_VALIDATION:<id>
            int sep = cmd.indexOf(":");
            if (sep > 0 && sep + 1 < cmd.length()) {
                try {
                    int id = Integer.parseInt(cmd.substring(sep + 1));
                    onPayValidation(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(parent, "Invalid product id for payment: " + cmd,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Abre la ventana para crear un nuevo producto de segunda mano.
     * La ventana es modal, y al cerrarse, este método recarga la lista de productos.
     */
    private void onAddNewProduct() {
        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            JOptionPane.showMessageDialog(parent, "Only registered clients can add products.", "Access denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Crear la ventana
        VentanaNuevoProductoSegundaMano ventanaNuevo = new VentanaNuevoProductoSegundaMano(parent);
        
        // 2. Crear su controlador, que se encargará de la lógica interna de la ventana
        new ControladorNuevoProductoSegundaMano(ventanaNuevo);
        
        // 3. Mostrar la ventana (es modal, el código se detiene aquí hasta que se cierre)
        ventanaNuevo.mostrar();

        // 4. Cuando la ventana se cierra, recargamos la vista para reflejar el nuevo producto
        cargarProductosDesdeModelo();
    }

    /** Abre la ventana de pago para la solicitud asociada al producto indicado por id. */
    private void onPayValidation(int productoId) {
        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            JOptionPane.showMessageDialog(parent, "Only registered clients can pay validations.", "Access denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();

        ProductoSegundaMano target = null;
        for (ProductoSegundaMano p : cliente.getCartera().getProductos()) {
            if (p.getID() == productoId) {
                target = p;
                break;
            }
        }

        if (target == null) {
            JOptionPane.showMessageDialog(parent, "Product not found (id=" + productoId + ")", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SolicitudValidacion solicitud = target.getSolicitudValidacion();

        VentanaPagoValidacion ventanaPago = new VentanaPagoValidacion(parent, solicitud);
        ControladorPagoValidacion controladorPago = new ControladorPagoValidacion(ventanaPago, solicitud);
        ventanaPago.setControlador(controladorPago);

        // ventanaPago es modal; tras cerrarla recargamos la vista para reflejar cambios
        ventanaPago.setVisible(true);
        cargarProductosDesdeModelo();
    }

    /** Carga los productos del usuario actual desde el modelo y los añade a la vista.
     *  Toda la lógica de negocio queda en el controlador; la vista solo expone
     *  métodos para añadir tarjetas y mostrar placeholders. */
    private void cargarProductosDesdeModelo() {
        vista.clearProducts();

        Aplicacion app = Aplicacion.getInstancia();
        if (!(app.getUsuarioActual() instanceof ClienteRegistrado)) {
            // Usuario no registrado: dejamos que la vista muestre los placeholders vacíos
            vista.ensurePlaceholdersIfEmpty();
            return;
        }

        ClienteRegistrado cliente = (ClienteRegistrado) app.getUsuarioActual();

        for (ProductoSegundaMano p : cliente.getCartera().getProductos()) {
            boolean validado  = p.isValidado();
            boolean pagado    = p.getSolicitudValidacion() != null && p.getSolicitudValidacion().getPagoValidacion() != null;
            boolean bloqueado = p.estaBloqueado();

            if (validado && !bloqueado) {
                vista.addReadyProduct(p);
            } else {
                vista.addValidationProduct(p, pagado);
            }
        }

        vista.ensurePlaceholdersIfEmpty();
    }
}
