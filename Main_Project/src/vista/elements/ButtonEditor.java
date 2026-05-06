package vista.elements;
import java.awt.*;

import javax.swing.*;


import controladores.ControladorNotificaciones;

import modelo.notificacion.Notificacion; 

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private Notificacion n;

    public ButtonEditor(JCheckBox checkBox, ControladorNotificaciones c) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        
        // El listener se añade UNA SOLA VEZ en el constructor
        button.addActionListener(e -> {
            fireEditingStopped();
            if(n != null){
              c.leerNotificacion(n);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Notificacion) {
            this.n = (Notificacion) value;
            label = n.getMensaje();
            if(n.getRead() == true) button.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
            else button.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
        } else {
            label = (value == null) ? "" : value.toString();
        }
        
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        // Devolvemos el objeto Notificacion original para no perder datos en el modelo
        return n;
    }
}
