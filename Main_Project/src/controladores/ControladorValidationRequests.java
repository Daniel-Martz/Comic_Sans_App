package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudValidacion;
import modelo.producto.EstadoConservacion;
import modelo.usuario.Empleado;
import vista.main.MainFrame;
import vista.empleadoPanel.ValidationRequestsPanel;
import vista.empleadoWindow.ValidarProductoWindow;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Controlador para gestionar las solicitudes de validación de productos
 * (panel de empleados).
 *
 * Lista las solicitudes pendientes y abre la ventana para validar cada
 * producto, delegando la validación al modelo.
 */
public class ControladorValidationRequests implements ActionListener {

    /** The panel. */
    private ValidationRequestsPanel panel;
    
    /** The main frame. */
    private MainFrame mainFrame;
    /**
     * Crea el controlador y enlaza la vista y navegación.
     *
     * @param panel panel de solicitudes de validación
     * @param mainFrame ventana principal
     * @param mainController controlador principal
     */
    public ControladorValidationRequests(ValidationRequestsPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        // Enlazar controlador con la vista
        this.panel.setControlador(this);

        // Configurar navegación de vuelta al HOME desde el header
        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
    }

    /**
     * Recupera las solicitudes pendientes del modelo y actualiza la vista.
     */
    public void actualizarSolicitudes() {
        List<SolicitudValidacion> pendientes = Aplicacion.getInstancia().getGestorSolicitud().getValidacionesPendientes();

        panel.actualizarSolicitudes(pendientes, this);
    }

    /**
     * Maneja acciones de la vista (editar/validar una solicitud concreta).
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd != null && cmd.startsWith("EDIT_")) {
            try {
                int id = Integer.parseInt(cmd.substring(5));
                abrirVentanaValidacion(id);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Abre la ventana modal para validar el producto con el id dado.
     *
     * @param idProducto id del producto a validar
     */
    private void abrirVentanaValidacion(int idProducto) {
        List<SolicitudValidacion> pendientes = Aplicacion.getInstancia().getGestorSolicitud().getValidacionesPendientes();
        SolicitudValidacion target = null;
        for (SolicitudValidacion s : pendientes) {
            if (s.getProductoAValidar().getID() == idProducto) {
                target = s;
                break;
            }
        }

        if (target != null) {
            ValidarProductoWindow dialog = new ValidarProductoWindow(mainFrame, target, this);
            dialog.setVisible(true);
        }
    }

    /**
     * Confirma la validación del producto con los datos proporcionados, delegando.
     *
     * @param s solicitud de validación a procesar
     * @param precioVal precio de validación (sugerido por el cliente)
     * @param precioProd precio final del producto (decisión del empleado)
     * @param estado estado de conservación del producto (decisión del empleado)
     */
    public void confirmarValidacion(SolicitudValidacion s, double precioVal, double precioProd, EstadoConservacion estado) {
        try {
            Empleado emp = (Empleado) Aplicacion.getInstancia().getUsuarioActual();
            emp.validarProducto(s, precioVal, precioProd, estado);
            actualizarSolicitudes(); // Recarga la vista
            JOptionPane.showMessageDialog(mainFrame, "Product validated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error validating product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}