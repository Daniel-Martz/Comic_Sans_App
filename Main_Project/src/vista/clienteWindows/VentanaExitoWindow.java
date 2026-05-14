package vista.clienteWindows;

import javax.swing.*;
import java.awt.*;

// TODO: Auto-generated Javadoc
/**
 * Ventana genérica modal para mostrar mensajes de éxito.
 * Mantiene el estilo visual verde pastel de la aplicación.
 */
public class VentanaExitoWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new ventana exito window.
     *
     * @param owner         La ventana o frame padre (para centrarla y bloquearla)
     * @param tituloVentana El texto que aparece en la barra superior de la ventana (ej: "Success")
     * @param tituloMensaje El texto en negrita del interior (ej: "SUCCESSFUL PAYMENT!!")
     * @param descripcion   La descripción detallada del éxito
     */
    public VentanaExitoWindow(Window owner, String tituloVentana, String tituloMensaje, String descripcion) {
        super(owner, tituloVentana, ModalityType.APPLICATION_MODAL);
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(200, 240, 200)); // Verde pastel

        JLabel lblTitulo = new JLabel(tituloMensaje, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        panel.add(lblTitulo);
        
        String textoFormateado = "<html><div style='text-align: center;'>" + descripcion.replace("\n", "<br>") + "</div></html>";
        JLabel lblDesc = new JLabel(textoFormateado, SwingConstants.CENTER);
        panel.add(lblDesc);

        add(panel);
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    /**
     * Mostrar.
     */
    public void mostrar() {
        setVisible(true);
    }
}
