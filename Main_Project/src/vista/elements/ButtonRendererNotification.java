package vista.elements;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import modelo.notificacion.Notificacion;

// TODO: Auto-generated Javadoc
/**
 * The Class ButtonRendererNotification.
 */
public class ButtonRendererNotification extends JButton implements TableCellRenderer {
    
    /**
     * Instantiates a new button renderer notification.
     */
    public ButtonRendererNotification() {
        setOpaque(true);
    }

    /**
     * Gets the table cell renderer component.
     *
     * @param table the table
     * @param value the value
     * @param isSelected the is selected
     * @param hasFocus the has focus
     * @param row the row
     * @param column the column
     * @return the table cell renderer component
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value == null || !(value instanceof Notificacion)) setText("");
        Notificacion n = (Notificacion) value;
        if(n.getRead() == true) setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
        else setFont(new Font("Comic Sans MS", Font.BOLD, 10));
        setText(n.getMensaje());
        return this;
    }
}
