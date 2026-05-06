package vista.empleadoPanel;

import controladores.ControladorManageOrders;
import modelo.solicitud.SolicitudPedido;
import vista.userPanels.HeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageOrdersPanel extends JPanel {
    private HeaderPanel headerPanel;
    private JPanel contenedorPedidos;
    private ControladorManageOrders controlador;

    public ManageOrdersPanel() {
        setLayout(new BorderLayout());

        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();
        add(headerPanel, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("Processed Orders", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panelCentral.add(lblTitle, BorderLayout.NORTH);

        contenedorPedidos = new JPanel();
        contenedorPedidos.setLayout(new BoxLayout(contenedorPedidos, BoxLayout.Y_AXIS));
        contenedorPedidos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollVertical = new JScrollPane(contenedorPedidos);
        scrollVertical.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollVertical.getVerticalScrollBar().setUnitIncrement(16);
        panelCentral.add(scrollVertical, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    public void setControlador(ControladorManageOrders controlador) {
        this.controlador = controlador;
    }

    public ControladorManageOrders getControlador() {
        return controlador;
    }

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    public void actualizarPedidos(List<SolicitudPedido> pedidos) {
        contenedorPedidos.removeAll();
        for (SolicitudPedido pedido : pedidos) {
            agregarPedido(pedido);
        }
        contenedorPedidos.revalidate();
        contenedorPedidos.repaint();
    }

    private void agregarPedido(SolicitudPedido pedido) {
        JPanel panelPedido = new JPanel(new BorderLayout(10, 10));
        panelPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Info (Izquierda)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        JLabel lblFecha = new JLabel("Date: " + pedido.getFechaRealizacion().toString());
        lblFecha.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel lblCoste = new JLabel(String.format("Total cost: %.2f €", pedido.getCostePedido()));
        lblCoste.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JLabel lblEstado = new JLabel("State: " + pedido.getEstado().toString());
        lblEstado.setFont(new Font("SansSerif", Font.ITALIC, 14));

        panelInfo.add(lblFecha);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblCoste);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblEstado);

        // Botones (Derecha)
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        
        JButton btnViewOrder = new JButton("View order");
        btnViewOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnViewOrder.addActionListener(e -> controlador.mostrarInformacionPedido(pedido));

        JButton btnSelectState = new JButton("Select State");
        btnSelectState.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSelectState.addActionListener(e -> controlador.cambiarEstadoPedido(pedido));

        panelBotones.add(btnViewOrder);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnSelectState);

        panelPedido.add(panelInfo, BorderLayout.CENTER);
        panelPedido.add(panelBotones, BorderLayout.EAST);

        contenedorPedidos.add(panelPedido);
        contenedorPedidos.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}
