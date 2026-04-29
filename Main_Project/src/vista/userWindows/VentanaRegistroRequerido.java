package vista.userWindows;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroRequerido extends JDialog {
    
    // Constantes para saber qué botón pulsó el usuario
    public static final int INICIAR_SESION = 0;
    public static final int REGISTRARSE = 1;
    public static final int CANCELAR = 2;

    private int resultado = CANCELAR; // Por defecto es cancelar si cierra la ventana

    public VentanaRegistroRequerido(JFrame parent) {
        // Llamamos al constructor de JDialog (ventana padre, título, modal)
        super(parent, "Registro Requerido", true);
        initUI();
    }

    private void initUI() {
        // --- COLORES PASTEL Y FUENTE COMIC SANS ---
        Color fondoPastel = new Color(214, 234, 248);      // Azul pastel claro para el fondo
        Color botonPastel = new Color(174, 214, 241);      // Azul pastel un poco más oscuro para botones
        Color azulOscuroTexto = new Color(33, 97, 140);    // Azul oscuro para contrastar el texto
        
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
        
        // Para añadir un icono
        // lblMensaje.setIcon(UIManager.getIcon("OptionPane.informationIcon")); 

        panelPrincipal.add(lblMensaje, BorderLayout.CENTER);

        // --- PANEL DE BOTONES ---
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(fondoPastel);

        JButton btnIniciarSesion = crearBotonPersonalizado("Iniciar Sesión", botonPastel, azulOscuroTexto, fuenteComicSans);
        JButton btnRegistrarse = crearBotonPersonalizado("Registrarme", botonPastel, azulOscuroTexto, fuenteComicSans);
        JButton btnCancelar = crearBotonPersonalizado("Cancelar", Color.WHITE, azulOscuroTexto, fuenteComicSans);

        // Acciones de los botones
        btnIniciarSesion.addActionListener(e -> {
            resultado = INICIAR_SESION;
            dispose(); // Cierra la ventana
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

        // --- CONFIGURACIÓN FINAL DE LA VENTANA ---
        this.add(panelPrincipal);
        this.pack(); // Ajusta el tamaño automáticamente
        this.setLocationRelativeTo(getParent()); // Centra la ventana sobre el JFrame padre
        this.setResizable(false);
    }

    // Método auxiliar para que los botones queden estéticos y uniformes
    private JButton crearBotonPersonalizado(String texto, Color bg, Color fg, Font fuente) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        boton.setBackground(bg);
        boton.setForeground(fg);
        boton.setFocusPainted(false); // Quita el recuadro de selección al hacer clic
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manita al pasar por encima
        
        // Borde redondeado sutil
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fg, 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        return boton;
    }

    // Método para mostrar la ventana y devolver qué se ha pulsado
    public int mostrarVentana() {
        this.setVisible(true); // Al ser modal, el código se pausa aquí hasta que se cierra
        return resultado;
    }
}