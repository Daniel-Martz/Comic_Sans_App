package vista.userPanels;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controladores.ControladorNotificaciones;
import modelo.notificacion.Notificacion;

import java.awt.*;

public class NotificacionesPanel extends JPanel{

    private JTable tablaNotificaciones;
    private DefaultTableModel modeloTabla;
    private HeaderPanel headerPanel = new HeaderPanel();
    private ControladorNotificaciones controlador;

    public HeaderPanel getHeaderPanel()	{return headerPanel;}

    public NotificacionesPanel() {
        // Configuramos el layout principal del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //Ponemos el panel de cabecera de la aplicación
        add(headerPanel, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Bandeja de Notificaciones");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.CENTER);

        String[] columnas = {"Content", "Date"};

        // 3. Crear el modelo de la tabla
        // Sobrescribimos isCellEditable para que el usuario no pueda modificar el texto directamente
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        // 4. Configurar el JTable
        tablaNotificaciones = new JTable(modeloTabla);
        
        // Ajustes visuales para que parezca una lista tipo Gmail (limpia y sin cuadrículas marcadas)
        tablaNotificaciones.setRowHeight(35); // Filas más altas y cómodas de leer
        tablaNotificaciones.setShowGrid(false); // Ocultar líneas de la cuadrícula
        tablaNotificaciones.setIntercellSpacing(new Dimension(0, 0));
        tablaNotificaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Seleccionar solo una a la vez
        tablaNotificaciones.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        
        // Ajustar el ancho de las columnas
        tablaNotificaciones.getColumnModel().getColumn(0).setPreferredWidth(400); // Título
        tablaNotificaciones.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha

        // 5. Añadir la tabla a un ScrollPane (imprescindible para listas largas)
        JScrollPane scrollPane = new JScrollPane(tablaNotificaciones);
        scrollPane.getViewport().setBackground(Color.WHITE); // Fondo blanco para la zona sin filas
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        add(scrollPane, BorderLayout.CENTER);

    }

    /**
     * Método público para añadir una nueva notificación desde otras partes de tu app
     */
    public void agregarNotificacion(Notificacion n) {
        Object[] fila = {n.getMensaje(), n.getHoraEnvio()};
        modeloTabla.addRow(fila);
    }
    
    public void clearNotificaciones() {
    	tablaNotificaciones.removeAll();
    }

    public void addListenerForElements(ControladorNotificaciones c){
      this.controlador = c;
    }

}
