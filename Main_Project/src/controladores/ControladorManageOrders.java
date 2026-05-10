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

public class ControladorManageOrders implements ActionListener {

    private ManageOrdersPanel panel;
    private MainFrame mainFrame;

    public ControladorManageOrders(ManageOrdersPanel panel, MainFrame mainFrame) {
        this.panel = panel;
        this.mainFrame = mainFrame;
        this.panel.setControlador(this);
        
        // Add listener to home button
        this.panel.getHeaderPanel().addHomeListener(e -> mainFrame.mostrarPanel(MainFrame.PANEL_MENU_EMPLEADO));
    }

    public void actualizarPedidos() {
        List<SolicitudPedido> pedidos = Aplicacion.getInstancia().getGestorSolicitud().getPedidos();
        panel.actualizarPedidos(pedidos);
    }

    public void mostrarInformacionPedido(SolicitudPedido pedido) {
        ViewOrderWindow dialog = new ViewOrderWindow(mainFrame, pedido);
        dialog.setVisible(true);
    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        // En caso de necesitar manejar otros eventos
    }
}
