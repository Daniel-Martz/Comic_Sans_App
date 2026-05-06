package vista.elements;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import modelo.notificacion.Notificacion;

public class DeleteButtonRendererNotifications extends JButton implements TableCellRenderer {
    public DeleteButtonRendererNotifications() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Delete");
        return this;
    }
}
