package vista.elements;
import java.awt.*;

import javax.swing.*;


import controladores.ControladorNotificaciones;

import modelo.notificacion.Notificacion; 

public class DeleteButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private Notificacion n;

    public DeleteButtonEditor(JCheckBox checkBox, ControladorNotificaciones c) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();
            if (n != null) {
            	System.out.println("The notificaction is not null");
                c.borrarNotificacion(n); 
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    	if(value instanceof Notificacion n) {
    		this.n = n;
    	}
        button.setText("Delete");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return n;
    }
}
