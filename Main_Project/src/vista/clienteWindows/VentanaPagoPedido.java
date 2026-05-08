package vista.clienteWindows;

import modelo.solicitud.SolicitudPedido;
import modelo.producto.LineaProductoVenta;
import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import java.io.File;
import controladores.ControladorPagoPedido;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana de pago para la compra de productos del carrito.
 * Actúa puramente como VISTA en el patrón MVC.
 */
public class VentanaPagoPedido extends JDialog {

    private static final long serialVersionUID = 1L;

    // ── Componentes de la vista
    private JTable tablaProductos;
    private JLabel labelTotal;
    private JTextField campoNumeroTarjeta;
    private JTextField campoCaducidad;
    private JPasswordField campoCVV;
    private JButton botonConfirmar;
    private modelo.solicitud.SolicitudPedido pedido;

    // ── Constructor
    public VentanaPagoPedido(Window padre, SolicitudPedido pedido) {
        super(padre, "Payment Window", ModalityType.APPLICATION_MODAL);
        this.pedido = pedido;
        inicializarComponentes(pedido);
        pack();
        setLocationRelativeTo(padre);
        setResizable(false);
    }

    // ── Construcción de la interfaz
    private void inicializarComponentes(SolicitudPedido pedido) {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── Panel Norte: ORDER SUMMARY
        JLabel labelTitulo = new JLabel("ORDER SUMMARY", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        labelTitulo.setOpaque(true);
        labelTitulo.setBackground(new Color(100, 130, 200));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        panelPrincipal.add(labelTitulo, BorderLayout.NORTH);

        // ── Panel Centro: Tabla de productos + total + formulario
        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));

        // Tabla de productos
        String[] columnas = {"", "Product", "Units", "Total Price"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        int fila = 1;
        for (Map.Entry<SimpleEntry<LineaProductoVenta, Integer>, Double> entry :
                pedido.getRecaudacionProductos().entrySet()) {
            LineaProductoVenta prod = entry.getKey().getKey();
            int cantidad = entry.getKey().getValue();
            double precioFila = entry.getValue();
            modeloTabla.addRow(new Object[]{
                fila++,
                prod.getNombre(),
                cantidad,
                String.format("(%.2f €)", precioFila)
            });
        }

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(24);
        tablaProductos.getTableHeader().setBackground(new Color(240, 150, 150));
        tablaProductos.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(35);
        tablaProductos.getColumnModel().getColumn(2).setMaxWidth(55);
        tablaProductos.getColumnModel().getColumn(3).setMinWidth(100);

        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        scrollTabla.setPreferredSize(new Dimension(420, Math.min(200, fila * 26 + 30)));
        panelCentro.add(scrollTabla, BorderLayout.NORTH);

        // Fila de total
        labelTotal = new JLabel(
                String.format("TOTAL TO PAY: %.2f €", pedido.getCostePedido()),
                SwingConstants.LEFT);
        labelTotal.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        labelTotal.setOpaque(true);
        labelTotal.setBackground(new Color(220, 220, 220));
        labelTotal.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panelCentro.add(labelTotal, BorderLayout.CENTER);

        // ── Panel formulario de pago
        JPanel panelFormulario = new JPanel(new GridLayout(5, 1, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Título CHECKOUT
        JLabel labelCheckout = new JLabel("CHECKOUT", SwingConstants.CENTER);
        labelCheckout.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        labelCheckout.setOpaque(true);
        labelCheckout.setBackground(new Color(100, 130, 200));
        labelCheckout.setForeground(Color.WHITE);
        labelCheckout.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        panelFormulario.add(labelCheckout);

        // Campo número de tarjeta
        JLabel labelTarjeta = new JLabel("Card Number", SwingConstants.CENTER);
        panelFormulario.add(labelTarjeta);

        campoNumeroTarjeta = new JTextField(16);
        campoNumeroTarjeta.setHorizontalAlignment(JTextField.CENTER);
        panelFormulario.add(campoNumeroTarjeta);

        // Fecha y CVV en la misma fila
        JPanel panelFechaYCVV = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFechaYCVV.add(new JLabel("Expiry Date", SwingConstants.CENTER));
        panelFechaYCVV.add(new JLabel("CVV", SwingConstants.CENTER));

        campoCaducidad = new JTextField("MM/AA");
        campoCaducidad.setHorizontalAlignment(JTextField.CENTER);
        panelFechaYCVV.add(campoCaducidad);

        campoCVV = new JPasswordField(3);
        panelFechaYCVV.add(campoCVV);
        panelFormulario.add(panelFechaYCVV);

        // Botón CONFIRM
        botonConfirmar = new JButton("CONFIRM");
        botonConfirmar.setBackground(new Color(50, 200, 80));
        botonConfirmar.setForeground(Color.WHITE);
        botonConfirmar.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        panelFormulario.add(botonConfirmar);

        panelCentro.add(panelFormulario, BorderLayout.SOUTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    // ── Métodos para el MVC

    /** Asigna el controlador a los botones de la vista. */
    public void setControlador(ActionListener c) {
        botonConfirmar.addActionListener(c);
        botonConfirmar.setActionCommand("CONFIRMAR_PAGO_PEDIDO");
    }

    /** Devuelve el pedido asociado a esta ventana (para que el controlador lo procese). */
    public SolicitudPedido getPedido() {
        return pedido;
    }

    public String getNumeroTarjeta() {
        return campoNumeroTarjeta.getText().trim();
    }

    public String getCVV() {
        return new String(campoCVV.getPassword()).trim();
    }

    public String getCaducidad() {
        return campoCaducidad.getText().trim();
    }

    // ── Ventanas de resultado

    public void mostrarVentanaExito() {
        JDialog ventanaExito = new JDialog(getOwner(), "Successful Payment", ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(200, 240, 200));

        JLabel titulo = new JLabel("SUCCESSFUL PAYMENT!!", SwingConstants.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        panel.add(titulo);
        panel.add(new JLabel("Your order is getting prepared you will be notified when it is ready", SwingConstants.CENTER));

        ventanaExito.add(panel);
        ventanaExito.pack();
        ventanaExito.setLocationRelativeTo(getOwner());
        ventanaExito.setVisible(true);
    }

    public void mostrarVentanaError(String motivo) {
        JDialog ventanaError = new JDialog(getOwner(), "Error", ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 200, 200));

        JLabel titulo = new JLabel("THERE WAS AN ERROR DURING THE PAYMENT", SwingConstants.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        panel.add(titulo);
        panel.add(new JLabel(motivo != null ? motivo : "Unknown error.", SwingConstants.CENTER));
        panel.add(new JLabel("You will get payed back.", SwingConstants.CENTER));

        ventanaError.add(panel);
        ventanaError.pack();
        ventanaError.setLocationRelativeTo(getOwner());
        ventanaError.setVisible(true);
    }

    public void mostrarAvisoIncompleto(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }
}
