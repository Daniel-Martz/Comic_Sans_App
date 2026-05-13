package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudIntercambio;
import modelo.usuario.Empleado;
import vista.main.MainFrame;
import vista.empleadoPanel.ManageInterchangesPanel;
import vista.empleadoWindow.ApproveInterchangeWindow;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la gestión de solicitudes de intercambio (empleados).
 *
 * Muestra las solicitudes pendientes y permite aprobar o ver detalles.
 */
public class ControladorManageInterchanges implements ActionListener {

    private ManageInterchangesPanel panel;
    private MainFrame mainFrame;
    private List<SolicitudIntercambio> pendientes;

    /**
     * Crea el controlador y enlaza la vista y el controlador principal.
     *
     * @param panel panel de gestión de intercambios
     * @param mainFrame ventana principal
     * @param mainController controlador principal
     */
    public ControladorManageInterchanges(ManageInterchangesPanel panel, MainFrame mainFrame, MainController mainController) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.panel.setControlador(this);
        
        this.panel.getHeaderPanel().addHomeListener(e -> mainController.mostrarMenuPrincipal());
    }

    /**
     * Obtiene las solicitudes pendientes del modelo y actualiza la vista.
     */
    public void actualizarSolicitudes() {
        pendientes = Aplicacion.getInstancia().getGestorSolicitud().getIntercambiosPendientes();
        panel.actualizarIntercambios(pendientes, this);
    }

    /**
     * Maneja acciones de la vista: aprobar una solicitud o ver información del
     * producto asociado.
     *
     * @param e evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd != null && cmd.startsWith("APPROVE_")) {
            try {
                int index = Integer.parseInt(cmd.substring(8));
                if (pendientes != null && index >= 0 && index < pendientes.size()) {
                    abrirVentanaAprobacion(pendientes.get(index));
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } else if (cmd != null && cmd.startsWith("INFO_")) {
            try {
                int id = Integer.parseInt(cmd.substring(5));
                modelo.producto.ProductoSegundaMano p = modelo.aplicacion.Catalogo.getInstancia().buscarProductoIntercambio(id);
                if (p != null) {
                    vista.clienteWindows.VentanaDetallesProductoSegundaManoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoSegundaManoWindow(mainFrame, p);
                    dialog.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Abre la ventana para aprobar una solicitud concreta.
     *
     * @param solicitud solicitud a aprobar
     */
    private void abrirVentanaAprobacion(SolicitudIntercambio solicitud) {
        ApproveInterchangeWindow dialog = new ApproveInterchangeWindow(mainFrame, solicitud, this);
        dialog.setVisible(true);
    }

    /**
     * Confirma la aprobación de la solicitud usando los códigos proporcionados.
     *
     * @param s solicitud a aprobar
     * @param code1 código del primer producto
     * @param code2 código del segundo producto
     */
    public void confirmarAprobacion(SolicitudIntercambio s, String code1, String code2) {
        try {
            Empleado emp = (Empleado) Aplicacion.getInstancia().getUsuarioActual();
            emp.aprobarIntercambio(s, code1, code2); // Aquí el modelo valida los códigos y realiza el cambio de dueño
            
            actualizarSolicitudes(); // Recarga la vista para quitar la tarjeta
            JOptionPane.showMessageDialog(mainFrame, "Interchange approved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, "Error approving interchange: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
