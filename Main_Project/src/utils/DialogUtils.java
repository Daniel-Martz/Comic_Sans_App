package utils;

import javax.swing.*;
import java.awt.*;

/**
 * Pequeña utilidad para mostrar diálogos JOptionPane con tamaño personalizado.
 */
public class DialogUtils {

    /**
     * Muestra un mensaje en un JOptionPane dentro de un JDialog y fuerza un ancho mayor.
     *
     * @param parent componente padre para la localización del diálogo
     * @param message texto o componente a mostrar
     * @param title título del diálogo
     * @param messageType tipo de mensaje (JOptionPane.MESSAGE_TYPE)
     */
    public static void showWideMessage(Component parent, Object message, String title, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType);
        JDialog dialog = pane.createDialog(parent, title);

        // Aumentamos ligeramente el ancho por defecto para mensajes de acceso denegado
        Dimension pref = dialog.getPreferredSize();
        int newWidth = Math.max(pref.width + 160, 480); // agrandar ancho
        int newHeight = pref.height + 20;
        dialog.setPreferredSize(new Dimension(newWidth, newHeight));
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
