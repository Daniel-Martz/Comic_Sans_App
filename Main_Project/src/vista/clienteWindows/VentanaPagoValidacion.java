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

// TODO: Auto-generated Javadoc
/**
 * Ventana de pago para la validación de un producto de segunda mano.
 * Actúa puramente como VISTA en el patrón MVC.
 */
public class VentanaPagoValidacion extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The label total. */
    // ── Componentes de la vista
    private JLabel labelTotal;
    
    /** The campo numero tarjeta. */
    private JTextField campoNumeroTarjeta;
    
    /** The campo caducidad. */
    private JTextField campoCaducidad;
    
    /** The campo CVV. */
    private JPasswordField campoCVV;
    
    /** The boton confirmar. */
    private JButton botonConfirmar;

    /**
     * Instantiates a new ventana pago validacion.
     *
     * @param padre the padre
     * @param solicitud the solicitud
     */
    public VentanaPagoValidacion(Window padre, SolicitudValidacion solicitud) {
        super(padre, "Payment Window", ModalityType.APPLICATION_MODAL);
        
        inicializarComponentes(solicitud.getPrecioValidacion());

        pack();
        setLocationRelativeTo(padre);
        setResizable(false);
    }

    /**
     * Inicializar componentes.
     *
     * @param precioValidacion the precio validacion
     */
    private void inicializarComponentes(double precioValidacion) {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 10, 10)); 
        
        JLabel labelCheckout = new JLabel("CHECKOUT", SwingConstants.CENTER);
        labelCheckout.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        labelCheckout.setOpaque(true);
        labelCheckout.setBackground(new Color(100, 130, 200));
        labelCheckout.setForeground(Color.WHITE);
        labelCheckout.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        panelNorte.add(labelCheckout);

        labelTotal = new JLabel(
                String.format("TOTAL TO PAY FOR VALIDATION: %.2f €", precioValidacion),
                SwingConstants.CENTER);
        labelTotal.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        labelTotal.setOpaque(true);
        labelTotal.setBackground(new Color(220, 220, 220));
        labelTotal.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panelNorte.add(labelTotal);
        
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(5, 1, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panelCampos.add(new JLabel("Card Number", SwingConstants.CENTER));
        campoNumeroTarjeta = new JTextField(16);
        campoNumeroTarjeta.setHorizontalAlignment(JTextField.CENTER);
        panelCampos.add(campoNumeroTarjeta);

        JPanel panelFechaYCVV = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFechaYCVV.add(new JLabel("Expiry Date", SwingConstants.CENTER));
        panelFechaYCVV.add(new JLabel("CVV", SwingConstants.CENTER));
        
        campoCaducidad = new JTextField("MM/AA");
        campoCaducidad.setHorizontalAlignment(JTextField.CENTER);
        panelFechaYCVV.add(campoCaducidad);
        
        campoCVV = new JPasswordField(3);
        panelFechaYCVV.add(campoCVV);

        panelCampos.add(panelFechaYCVV);

        botonConfirmar = new JButton("CONFIRM");
        botonConfirmar.setBackground(new Color(50, 200, 80));
        botonConfirmar.setForeground(Color.WHITE);
        botonConfirmar.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        panelCampos.add(botonConfirmar);

        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        add(panelPrincipal);
    }
    
    /**
     * Sets the controlador.
     *
     * @param c the new controlador
     */
    public void setControlador(ActionListener c) {
        botonConfirmar.addActionListener(c);
        botonConfirmar.setActionCommand("CONFIRMAR_PAGO");
    }

    /**
     * Gets the numero tarjeta.
     *
     * @return the numero tarjeta
     */
    public String getNumeroTarjeta() {
        return campoNumeroTarjeta.getText().trim();
    }

    /**
     * Gets the cvv.
     *
     * @return the cvv
     */
    public String getCVV() {
        return new String(campoCVV.getPassword()).trim();
    }

    /**
     * Gets the caducidad.
     *
     * @return the caducidad
     */
    public String getCaducidad() {
        return campoCaducidad.getText().trim();
    }


    /**
     * Mostrar ventana exito.
     */
    public void mostrarVentanaExito() {
        VentanaExitoWindow exito = new VentanaExitoWindow(
            getOwner(), 
            "Successful Payment", 
            "SUCCESSFUL PAYMENT!!", 
            "Your product can now be interchanged"
        );
        exito.mostrar();
    }

    /**
     * Mostrar ventana error.
     *
     * @param motivo the motivo
     */
    public void mostrarVentanaError(String motivo) {
        VentanaErrorWindow error = new VentanaErrorWindow(
            getOwner(), 
            "Error", 
            "THERE WAS AN ERROR DURING THE PAYMENT", 
            motivo + "\nYou will get paid back."
        );
        error.mostrar();
    }
    
    /**
     * Mostrar aviso incompleto.
     *
     * @param mensaje the mensaje
     * @param titulo the titulo
     */
    public void mostrarAvisoIncompleto(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }
}
