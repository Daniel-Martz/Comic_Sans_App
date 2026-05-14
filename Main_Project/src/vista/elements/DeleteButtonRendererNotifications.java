package vista.elements;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import modelo.notificacion.Notificacion;

// TODO: Auto-generated Javadoc
/**
 * The Class DeleteButtonRendererNotifications.
 */
public class DeleteButtonRendererNotifications extends JButton implements TableCellRenderer {
    
    /**
     * Instantiates a new delete button renderer notifications.
     */
    public DeleteButtonRendererNotifications() {
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
        setText("Delete");
        return this;
    }
}
