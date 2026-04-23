package vista.userWindows;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana genérica modal para mostrar mensajes de éxito.
 * Mantiene el estilo visual verde pastel de la aplicación.
 */
public class VentanaExito extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * @param owner         La ventana o frame padre (para centrarla y bloquearla)
     * @param tituloVentana El texto que aparece en la barra superior de la ventana (ej: "Success")
     * @param tituloMensaje El texto en negrita del interior (ej: "SUCCESSFUL PAYMENT!!")
     * @param descripcion   La descripción detallada del éxito
     */
    public VentanaExito(Window owner, String tituloVentana, String tituloMensaje, String descripcion) {
        super(owner, tituloVentana, ModalityType.APPLICATION_MODAL);
        
        // El 0 en filas permite que el panel crezca si metes más componentes
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(200, 240, 200)); // Verde pastel

        JLabel lblTitulo = new JLabel(tituloMensaje, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(lblTitulo);
        
        // Si quieres que la descripción soporte saltos de línea con \n, usamos HTML
        String textoFormateado = "<html><div style='text-align: center;'>" + descripcion.replace("\n", "<br>") + "</div></html>";
        JLabel lblDesc = new JLabel(textoFormateado, SwingConstants.CENTER);
        panel.add(lblDesc);

        add(panel);
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    public void mostrar() {
        setVisible(true);
    }
}