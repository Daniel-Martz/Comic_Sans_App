package vista.clienteWindows;

import controladores.ControladorPagoValidacion;
import modelo.aplicacion.Aplicacion;
import modelo.solicitud.SolicitudValidacion;
import modelo.usuario.ClienteRegistrado;
import modelo.producto.ProductoSegundaMano;
import modelo.producto.EstadoConservacion;

import java.awt.BorderLayout;
import java.awt.Color;
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
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 10, 10)); // Aumentado el gap para que respire
        
        // Estilo igual al de la otra ventana (Fondo Azul, Letras Blancas)
        JLabel labelCheckout = new JLabel("CHECKOUT", SwingConstants.CENTER);
        labelCheckout.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        labelCheckout.setOpaque(true);
        labelCheckout.setBackground(new Color(100, 130, 200));
        labelCheckout.setForeground(Color.WHITE);
        labelCheckout.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        panelNorte.add(labelCheckout);

        // Estilo igual al de la otra ventana (Fondo Gris clarito)
        labelTotal = new JLabel(
                String.format("TOTAL TO PAY FOR VALIDATION: %.2f €", precioValidacion),
                SwingConstants.CENTER);
        labelTotal.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        labelTotal.setOpaque(true);
        labelTotal.setBackground(new Color(220, 220, 220));
        labelTotal.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panelNorte.add(labelTotal);
        
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // ── Panel Central: Formulario de tarjeta 
        JPanel panelCampos = new JPanel(new GridLayout(5, 1, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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

        // Botón CONFIRM (Verde, igual que en la ventana de pedido)
        botonConfirmar = new JButton("CONFIRM");
        botonConfirmar.setBackground(new Color(50, 200, 80));
        botonConfirmar.setForeground(Color.WHITE);
        botonConfirmar.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        panelCampos.add(botonConfirmar);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    // ── Métodos para el MVC (Conexión y Getters)

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
        VentanaExito exito = new VentanaExito(
            getOwner(), 
            "Successful Payment", 
            "SUCCESSFUL PAYMENT!!", 
            "Your product will be validated and priced soon"
        );
        exito.mostrar();
    }

    public void mostrarVentanaError(String motivo) {
        // Al usar \n en la descripción, la clase lo convertirá en dos líneas automáticamente
        VentanaError error = new VentanaError(
            getOwner(), 
            "Error", 
            "THERE WAS AN ERROR DURING THE PAYMENT", 
            motivo + "\nYou will get payed back."
        );
        error.mostrar();
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
        
        // Configuramos la vista con el controlador
        vista.setControlador(controlador);
        
        vista.setVisible(true);
    }
}
