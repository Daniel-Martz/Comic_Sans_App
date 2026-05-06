package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudPedido;
import vista.empleadoPanel.ManageOrdersPanel;
import vista.empleadoPanel.SelectOrderStateWindow;
import vista.empleadoPanel.ViewOrderWindow;
import vista.main.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        SelectOrderStateWindow dialog = new SelectOrderStateWindow(mainFrame, pedido, this);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // En caso de necesitar manejar otros eventos
    }
}
