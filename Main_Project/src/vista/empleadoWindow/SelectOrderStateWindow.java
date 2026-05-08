package vista.empleadoWindow;

import controladores.ControladorManageOrders;
import modelo.solicitud.EstadoPedido;
import modelo.solicitud.SolicitudPedido;
import vista.clienteWindows.VentanaExitoWindow;

import javax.swing.*;
import java.awt.*;

public class SelectOrderStateWindow extends JDialog {

    private SolicitudPedido pedido;
    private ControladorManageOrders controlador;
    private JComboBox<String> comboEstados;

    public SelectOrderStateWindow(JFrame parent, SolicitudPedido pedido, ControladorManageOrders controlador) {
        super(parent, "Select Order State", true);
        this.pedido = pedido;
        this.controlador = controlador;
        
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblInfo = new JLabel("Select the new state for the order:");
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentro.add(lblInfo);

        String[] estados = {"Payment pending", "Paid", "Ready for pickup", "Picked Up"};
        comboEstados = new JComboBox<>(estados);
        
        // Seleccionar el estado actual
        EstadoPedido estadoActual = pedido.getEstado();
        if (estadoActual == EstadoPedido.PENDIENTE_DE_PAGO) {
            comboEstados.setSelectedIndex(0);
        } else if (estadoActual == EstadoPedido.PAGADO) {
            comboEstados.setSelectedIndex(1);
        } else if (estadoActual == EstadoPedido.LISTO_PARA_RECOGER) {
            comboEstados.setSelectedIndex(2);
        } else if (estadoActual == EstadoPedido.RECOGIDO) {
            comboEstados.setSelectedIndex(3);
        }
        
        panelCentro.add(comboEstados);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        JButton btnConfirm = new JButton("Confirm Changes");
        btnConfirm.addActionListener(e -> confirmarCambios());
        panelSur.add(btnConfirm);
        add(panelSur, BorderLayout.SOUTH);
    }

    private void confirmarCambios() {
        int index = comboEstados.getSelectedIndex();
        EstadoPedido nuevoEstado = null;
        switch (index) {
            case 0: nuevoEstado = EstadoPedido.PENDIENTE_DE_PAGO; break;
            case 1: nuevoEstado = EstadoPedido.PAGADO; break;
            case 2: nuevoEstado = EstadoPedido.LISTO_PARA_RECOGER; break;
            case 3: nuevoEstado = EstadoPedido.RECOGIDO; break;
        }
        
        if (nuevoEstado != null) {
            pedido.actualizarEstadoPedidoEmpleado(nuevoEstado);
            controlador.actualizarPedidos(); // Refrescar la vista
            dispose();
            VentanaExitoWindow exito = new VentanaExitoWindow(null, "Success", "State Updated", "Order state updated successfully");
            exito.setVisible(true);
        }
    }
}
