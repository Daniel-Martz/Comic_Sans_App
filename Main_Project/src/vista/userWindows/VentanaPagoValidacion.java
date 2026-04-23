package vista.userWindows;

import controladores.ControladorPagoValidacion;
import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudValidacion;
import modelo.usuario.ClienteRegistrado;
import modelo.producto.ProductoSegundaMano;
import modelo.producto.EstadoConservacion;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Ventana de pago para la validación de un producto de segunda mano.
 * Actúa puramente como VISTA en el patrón MVC.
 */
public class VentanaPagoValidacion extends JDialog {

    private static final long serialVersionUID = 1L;

    // ── Componentes de la vista
    private JLabel labelTotal;
    private JTextField campoNumeroTarjeta;
    private JTextField campoCaducidad;
    private JPasswordField campoCVV;
    private JButton botonConfirmar;

    // ── Constructor
    public VentanaPagoValidacion(Window padre, SolicitudValidacion solicitud) {
        super(padre, "Payment Window", ModalityType.APPLICATION_MODAL);
        
        inicializarComponentes(solicitud.getPrecioValidacion());

        pack();
        setLocationRelativeTo(padre);
        setResizable(false);
    }

    // ── Construcción de la interfaz 
    private void inicializarComponentes(double precioValidacion) {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ── Panel Norte: Título CHECKOUT y Total (no interactivo)
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JLabel labelCheckout = new JLabel("CHECKOUT", SwingConstants.CENTER);
        labelCheckout.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelNorte.add(labelCheckout);

        labelTotal = new JLabel(
                String.format("TOTAL TO PAY FOR VALIDATION: %.2f €", precioValidacion),
                SwingConstants.CENTER);
        panelNorte.add(labelTotal);
        
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // ── Panel Central: Formulario de tarjeta 
        JPanel panelCampos = new JPanel(new GridLayout(5, 1, 5, 5));

        panelCampos.add(new JLabel("Card Number", SwingConstants.CENTER));
        campoNumeroTarjeta = new JTextField(16);
        campoNumeroTarjeta.setHorizontalAlignment(JTextField.CENTER);
        panelCampos.add(campoNumeroTarjeta);

        // Fila con fecha de caducidad y CVV lado a lado
        JPanel panelFechaYCVV = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFechaYCVV.add(new JLabel("Expiry Date", SwingConstants.CENTER));
        panelFechaYCVV.add(new JLabel("CVV", SwingConstants.CENTER));
        
        campoCaducidad = new JTextField("MM/AA");
        campoCaducidad.setHorizontalAlignment(JTextField.CENTER);
        panelFechaYCVV.add(campoCaducidad);
        
        campoCVV = new JPasswordField(3);
        panelFechaYCVV.add(campoCVV);

        panelCampos.add(panelFechaYCVV);

        // Botón CONFIRM
        botonConfirmar = new JButton("CONFIRM");
        panelCampos.add(botonConfirmar);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    // ── Métodos para el MVC (Conexión y Getters)

    /**
     * Asigna el controlador a los botones de la vista.
     */
    public void setControlador(ActionListener c) {
        botonConfirmar.addActionListener(c);
        botonConfirmar.setActionCommand("CONFIRMAR_PAGO");
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

    // ── Ventanas de resultado (Llamadas por el controlador) ──────────────────

    public void mostrarVentanaExito() {
        JDialog ventanaExito = new JDialog(getOwner(), "Successful Payment", ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("SUCCESSFUL PAYMENT!!", SwingConstants.CENTER));
        panel.add(new JLabel("Your product will be validated and priced soon", SwingConstants.CENTER));

        ventanaExito.add(panel);
        ventanaExito.pack();
        ventanaExito.setLocationRelativeTo(getOwner());
        ventanaExito.setVisible(true);
    }

    public void mostrarVentanaError(String motivo) {
        JDialog ventanaError = new JDialog(getOwner(), "Error", ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("THERE WAS AN ERROR DURING THE PAYMENT", SwingConstants.CENTER));
        panel.add(new JLabel(motivo, SwingConstants.CENTER)); // Muestra el motivo real
        panel.add(new JLabel("You will get payed back.", SwingConstants.CENTER));

        ventanaError.add(panel);
        ventanaError.pack();
        ventanaError.setLocationRelativeTo(getOwner());
        ventanaError.setVisible(true);
    }
    
    public void mostrarAvisoIncompleto(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    // ── Main para testear
    public static void main(String[] args) {
        Aplicacion app = Aplicacion.getInstancia();
        ClienteRegistrado cliente = new ClienteRegistrado("demoUser", "123456789A", "Passw0rd!");
        app.setUsuarioActual(cliente);

        ProductoSegundaMano producto = new ProductoSegundaMano("Demo Product", "Descripción demo", (File) null, cliente);
        SolicitudValidacion solicitud = producto.getSolicitudValidacion();
        solicitud.validarProducto(5.0, 50.0, EstadoConservacion.USO_LIGERO);

        // Instanciamos la Vista
        VentanaPagoValidacion vista = new VentanaPagoValidacion(null, solicitud);
        
        // Instanciamos el Controlador y le pasamos la vista y el modelo (solicitud)
        ControladorPagoValidacion controlador = new ControladorPagoValidacion(vista, solicitud);
        
        // Configuramos la vista con el controlador [cite: 126, 127]
        vista.setControlador(controlador);
        
        vista.setVisible(true);
    }
}