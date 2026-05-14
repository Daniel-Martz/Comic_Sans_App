package vista.clienteWindows;

import javax.swing.*;
import java.awt.*;

// TODO: Auto-generated Javadoc
/**
 * The Class VentanaRegistroRequeridoWindow.
 */
public class VentanaRegistroRequeridoWindow extends JDialog {
    
    /** The Constant INICIAR_SESION. */
    public static final int INICIAR_SESION = 0;
    
    /** The Constant REGISTRARSE. */
    public static final int REGISTRARSE = 1;
    
    /** The Constant CANCELAR. */
    public static final int CANCELAR = 2;

    /** The resultado. */
    private int resultado = CANCELAR;

    /**
     * Instantiates a new ventana registro requerido window.
     *
     * @param parent the parent
     */
    public VentanaRegistroRequeridoWindow(JFrame parent) {
        super(parent, "Registro Requerido", true);
        initUI();
    }

    /**
     * Inits the UI.
     */
    private void initUI() {

        Color fondoPastel = new Color(214, 234, 248);      
        Color botonPastel = new Color(174, 214, 241);      
        Color azulOscuroTexto = new Color(33, 97, 140);    
        
        Font fuenteComicSans = new Font("Comic Sans MS", Font.PLAIN, 15);
        Font fuenteTitulo = new Font("Comic Sans MS", Font.BOLD, 17);

        // --- PANEL PRINCIPAL ---
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(15, 20));
        panelPrincipal.setBackground(fondoPastel);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // --- ETIQUETA DE MENSAJE ---
        String textoMensaje = "<html><center>Para usar esta funcionalidad<br>"
                            + "<b>debes estar registrado.</b></center></html>";
        JLabel lblMensaje = new JLabel(textoMensaje);
        lblMensaje.setFont(fuenteTitulo);
        lblMensaje.setForeground(azulOscuroTexto);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
       
        panelPrincipal.add(lblMensaje, BorderLayout.CENTER);

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(fondoPastel);

        JButton btnIniciarSesion = crearBotonPersonalizado("Log in", botonPastel, azulOscuroTexto, fuenteComicSans);
        JButton btnRegistrarse = crearBotonPersonalizado("Register", botonPastel, azulOscuroTexto, fuenteComicSans);
        JButton btnCancelar = crearBotonPersonalizado("Cancel", Color.WHITE, azulOscuroTexto, fuenteComicSans);

        // Acciones de los botones
        btnIniciarSesion.addActionListener(e -> {
            resultado = INICIAR_SESION;
            dispose();
        });

        btnRegistrarse.addActionListener(e -> {
            resultado = REGISTRARSE;
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            resultado = CANCELAR;
            dispose();
        });

        panelBotones.add(btnIniciarSesion);
        panelBotones.add(btnRegistrarse);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

    
        this.add(panelPrincipal);
        this.pack(); 
        this.setLocationRelativeTo(getParent()); 
        this.setResizable(false);
    }

    /**
     * Crear boton personalizado.
     *
     * @param texto the texto
     * @param bg the bg
     * @param fg the fg
     * @param fuente the fuente
     * @return the j button
     */
    // Método auxiliar para que los botones queden estéticos y uniformes
    private JButton crearBotonPersonalizado(String texto, Color bg, Color fg, Font fuente) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setBackground(bg);
        boton.setForeground(fg);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fg, 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        return boton;
    }


    /**
     * Mostrar ventana.
     *
     * @return the int
     */
    public int mostrarVentana() {
        this.setVisible(true);
        return resultado;
    }
}