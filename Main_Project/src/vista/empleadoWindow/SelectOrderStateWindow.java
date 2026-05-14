package vista.empleadoWindow;

import controladores.ControladorManageOrders;
import java.awt.*;
import javax.swing.*;

import modelo.aplicacion.Aplicacion;
import modelo.solicitud.EstadoPedido;
import modelo.solicitud.SolicitudPedido;
import modelo.usuario.Empleado;
import vista.clienteWindows.VentanaExitoWindow;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectOrderStateWindow.
 */
public class SelectOrderStateWindow extends JDialog {

    /** The pedido. */
    private SolicitudPedido pedido;
    
    /** The controlador. */
    private ControladorManageOrders controlador;
    
    /** The combo estados. */
    private JComboBox<String> comboEstados;
    
    /** The estado actual. */
    private EstadoPedido estadoActual;

    /**
     * Instantiates a new select order state window.
     *
     * @param parent the parent
     * @param pedido the pedido
     * @param controlador the controlador
     */
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

        // Determinar los estados disponibles según el estado actual
        String[] estados;
        this.estadoActual = pedido.getEstado();
        
        if (this.estadoActual == EstadoPedido.PENDIENTE_DE_PAGO) {
            // Si está pendiente de pago: mostrar Pagado, Listo para recoger, Recogido
            estados = new String[]{"Paid", "Ready for pickup", "Picked Up"};
        } else if (this.estadoActual == EstadoPedido.PAGADO) {
            // Si está pagado: mostrar Listo para recoger, Recogido (no mostrar Pagado)
            estados = new String[]{"Ready for pickup", "Picked Up"};
        } else if (this.estadoActual == EstadoPedido.LISTO_PARA_RECOGER) {
            // Si está listo para recoger: mostrar solo Recogido (no mostrar Pagado)
            estados = new String[]{"Picked Up"};
        } else {
            // Si está recogido: no hay más estados posibles
            estados = new String[]{"Picked Up"};
        }
        
        comboEstados = new JComboBox<>(estados);
        comboEstados.setSelectedIndex(0);
        
        panelCentro.add(comboEstados);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        JButton btnConfirm = new JButton("Confirm Changes");
        btnConfirm.addActionListener(e -> confirmarCambios());
        panelSur.add(btnConfirm);
        add(panelSur, BorderLayout.SOUTH);
    }

    /**
     * Confirmar cambios.
     */
    private void confirmarCambios() {
        int index = comboEstados.getSelectedIndex();
        EstadoPedido nuevoEstado = null;
        
        // Mapear el índice al estado según el estado actual
        if (estadoActual == EstadoPedido.PENDIENTE_DE_PAGO) {
            // Estados disponibles: Pagado, Listo para recoger, Recogido
            switch (index) {
                case 0: nuevoEstado = EstadoPedido.PAGADO; break;
                case 1: nuevoEstado = EstadoPedido.LISTO_PARA_RECOGER; break;
                case 2: nuevoEstado = EstadoPedido.RECOGIDO; break;
            }
        } else if (estadoActual == EstadoPedido.PAGADO) {
            // Estados disponibles: Listo para recoger, Recogido
            switch (index) {
                case 0: nuevoEstado = EstadoPedido.LISTO_PARA_RECOGER; break;
                case 1: nuevoEstado = EstadoPedido.RECOGIDO; break;
            }
        } else if (estadoActual == EstadoPedido.LISTO_PARA_RECOGER) {
            // Estados disponibles: Solo Recogido
            switch (index) {
                case 0: nuevoEstado = EstadoPedido.RECOGIDO; break;
            }
        } else if (estadoActual == EstadoPedido.RECOGIDO) {
            // No hay más estados disponibles
            nuevoEstado = EstadoPedido.RECOGIDO;
        }
        
        if (nuevoEstado != null) {
            ((Empleado)Aplicacion.getInstancia().getUsuarioActual()).actualizarEstadoPedido(pedido, nuevoEstado);
            controlador.actualizarPedidos(); // Refrescar la vista
            dispose();
            VentanaExitoWindow exito = new VentanaExitoWindow(null, "Success", "State Updated", "Order state updated successfully");
            exito.setVisible(true);
        }
    }
}
