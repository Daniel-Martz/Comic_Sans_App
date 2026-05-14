package vista.clienteWindows;

import javax.swing.*;
import java.awt.*;

// TODO: Auto-generated Javadoc
/**
 * Ventana genérica modal para mostrar mensajes de error.
 * Mantiene el estilo visual rojo pastel de la aplicación.
 */
public class VentanaErrorWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new ventana error window.
     *
     * @param owner         La ventana o frame padre
     * @param tituloVentana El texto de la barra superior (ej: "Error")
     * @param tituloMensaje El texto en negrita del interior (ej: "THERE WAS AN ERROR...")
     * @param descripcion   La descripción detallada del error (soporta saltos de línea con \n)
     */
    public VentanaErrorWindow(Window owner, String tituloVentana, String tituloMensaje, String descripcion) {
        super(owner, tituloVentana, ModalityType.APPLICATION_MODAL);
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 200, 200)); // Rojo pastel

        JLabel lblTitulo = new JLabel(tituloMensaje, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
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
