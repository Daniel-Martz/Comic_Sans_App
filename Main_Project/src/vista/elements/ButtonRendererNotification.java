package vista.elements;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import modelo.notificacion.Notificacion;

public class ButtonRendererNotification extends JButton implements TableCellRenderer {
    public ButtonRendererNotification() {
        setOpaque(true);
    }

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
