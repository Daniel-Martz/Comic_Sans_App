package vista.elements;
import java.awt.*;

import javax.swing.*;


import controladores.ControladorNotificaciones;

import modelo.notificacion.Notificacion; 

// TODO: Auto-generated Javadoc
/**
 * The Class ButtonEditor.
 */
public class ButtonEditor extends DefaultCellEditor {
    
    /** The button. */
    protected JButton button;
    
    /** The label. */
    private String label;
    
    /** The n. */
    private Notificacion n;

    /**
     * Instantiates a new button editor.
     *
     * @param checkBox the check box
     * @param c the c
     */
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

    /**
     * Gets the table cell editor component.
     *
     * @param table the table
     * @param value the value
     * @param isSelected the is selected
     * @param row the row
     * @param column the column
     * @return the table cell editor component
     */
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

    /**
     * Gets the cell editor value.
     *
     * @return the cell editor value
     */
    @Override
    public Object getCellEditorValue() {
        // Devolvemos el objeto Notificacion original para no perder datos en el modelo
        return n;
    }
}
