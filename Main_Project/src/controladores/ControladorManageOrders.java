package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.aplicacion.Aplicacion;
import modelo.solicitud.EstadoPedido;
import modelo.solicitud.SolicitudPedido;
import vista.empleadoPanel.ManageOrdersPanel;
import vista.empleadoWindow.SelectOrderStateWindow;
import vista.empleadoWindow.ViewOrderWindow;
import vista.main.MainFrame;

// TODO: Auto-generated Javadoc
/**
 * Controlador para la gestión de pedidos desde la perspectiva del empleado.
 *
 * Permite listar pedidos, ver detalles y cambiar su estado (cuando procede).
 */
public class ControladorManageOrders implements ActionListener {

    /** The panel. */
    private ManageOrdersPanel panel;
    
    /** The main frame. */
    private MainFrame mainFrame;

    /**
     * Inicializa el controlador y asocia la vista.
     *
     * @param panel panel de gestión de pedidos
     * @param mainFrame ventana principal
     */
    public ControladorManageOrders(ManageOrdersPanel panel, MainFrame mainFrame) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.panel.setControlador(this);
        
        // Add listener to home button
        this.panel.getHeaderPanel().addHomeListener(e -> mainFrame.mostrarPanel(MainFrame.PANEL_MENU_EMPLEADO));
    }

    /**
     * Recupera los pedidos del gestor de solicitudes y actualiza la vista.
     */
    public void actualizarPedidos() {
        List<SolicitudPedido> pedidos = Aplicacion.getInstancia().getGestorSolicitud().getPedidos();
        panel.actualizarPedidos(pedidos);
    }

    /**
     * Muestra una ventana con la información detallada del pedido.
     *
     * @param pedido pedido a mostrar
     */
    public void mostrarInformacionPedido(SolicitudPedido pedido) {
        ViewOrderWindow dialog = new ViewOrderWindow(mainFrame, pedido);
        dialog.setVisible(true);
    }

    /**
     * Abre el diálogo para cambiar el estado del pedido si está gestionable.
     *
     * @param pedido pedido cuyo estado se quiere cambiar
     */
    public void cambiarEstadoPedido(SolicitudPedido pedido) {
        if(pedido.getEstado() == EstadoPedido.PENDIENTE_DE_PAGO){
            JOptionPane.showMessageDialog(
                mainFrame, 
                "The order cannot be managed until it is paid", 
                "Payment required from user", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        SelectOrderStateWindow dialog = new SelectOrderStateWindow(mainFrame, pedido, this);
        dialog.setVisible(true);
    }

    /**
     * Action performed.
     *
     * @param e the e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Implementado solo por compatibilidad; aquí se podrían manejar
        // eventos adicionales si la vista los emite.
    }
}
