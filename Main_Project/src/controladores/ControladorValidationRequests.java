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

public class ControladorValidationRequests implements ActionListener {

    private ValidationRequestsPanel panel;
    private MainFrame mainFrame;
    private MainController mainController;

    public ControladorValidationRequests(ValidationRequestsPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.mainController = mainController;
        
        // Enlazar controlador con la vista
        this.panel.setControlador(this);
        
        // Configurar navegación de vuelta al HOME desde el header
        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
    }

    public void actualizarSolicitudes() {
        // Obtener directamente las solicitudes pendientes de validar del modelo
        List<SolicitudValidacion> pendientes = Aplicacion.getInstancia().getGestorSolicitud().getValidacionesPendientes();
        
        panel.actualizarSolicitudes(pendientes, this);
    }

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