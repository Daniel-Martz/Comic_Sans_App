package vista.userPanels;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controladores.ControladorNotificaciones;
import modelo.notificacion.Notificacion;
import vista.elements.*;
import java.awt.*;

public class NotificacionesPanel extends JPanel{

    private JTable tablaNotificaciones;
    private DefaultTableModel modeloTabla;
    private HeaderPanel headerPanel = new HeaderPanel();
    private ControladorNotificaciones controlador;
    private ButtonRendererNotification rendererRead;
    private ButtonEditor editorRead;
    private DeleteButtonRendererNotifications rendererDelete;
    private DeleteButtonEditor editorDelete;

    public HeaderPanel getHeaderPanel()	{return headerPanel;}

    public NotificacionesPanel() {
    // 1. Cambiamos el layout principal a BorderLayout
    setLayout(new BorderLayout(0, 15)); // 15px de separación vertical entre zonas
    setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    setBackground(Color.WHITE); // O el color que prefieras

    // 2. EL HEADER: Va en la parte superior (NORTH)
    // Nota: BorderLayout respetará la altura preferida del HeaderPanel
    headerPanel.setPreferredSize(new Dimension(headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height)); 
    add(headerPanel, BorderLayout.NORTH);

    // 3. EL CONTENIDO CENTRAL: Título + Tabla
    // Creamos un panel intermedio para meter el título y la tabla juntos
    JPanel centroPanel = new JPanel();
    centroPanel.setLayout(new BorderLayout(0, 10)); // Espacio entre título y tabla
    centroPanel.setOpaque(false);

    // Título
    JLabel lblTitulo = new JLabel("Bandeja de Notificaciones");
    lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
    centroPanel.add(lblTitulo, BorderLayout.NORTH); // El título arriba del panel central

    // --- Configuración de la Tabla (Tu código original) ---
    String[] columnas = {"Content", "Date", "Delete"};
    modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 2; 
        }
    };

    tablaNotificaciones = new JTable(modeloTabla);
    tablaNotificaciones.setRowHeight(35);
    tablaNotificaciones.setShowGrid(false);
    tablaNotificaciones.setIntercellSpacing(new Dimension(0, 0));
    tablaNotificaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaNotificaciones.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Cambiado de Comic Sans por estética profesional
    
    tablaNotificaciones.getColumnModel().getColumn(0).setPreferredWidth(400);
    tablaNotificaciones.getColumnModel().getColumn(1).setPreferredWidth(100);
    tablaNotificaciones.getColumnModel().getColumn(2).setPreferredWidth(100);

    JScrollPane scrollPane = new JScrollPane(tablaNotificaciones);
    scrollPane.getViewport().setBackground(Color.WHITE);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

    // Añadimos el scrollPane al CENTRO del panel intermedio
    centroPanel.add(scrollPane, BorderLayout.CENTER);

    // 4. Añadimos el panel intermedio al CENTRO del panel principal
    add(centroPanel, BorderLayout.CENTER);
}

    public void configurarColumnasInteractivas(ControladorNotificaciones c) { 
      rendererRead = new ButtonRendererNotification();
      editorRead = new ButtonEditor(new JCheckBox(), c);
      rendererDelete = new DeleteButtonRendererNotifications();
      editorDelete = new DeleteButtonEditor(new JCheckBox(), c);
      tablaNotificaciones.getColumnModel().getColumn(0).setCellRenderer(this.rendererRead);
      tablaNotificaciones.getColumnModel().getColumn(0).setCellEditor(this.editorRead);    
      tablaNotificaciones.getColumnModel().getColumn(2).setCellRenderer(this.rendererDelete);
      tablaNotificaciones.getColumnModel().getColumn(2).setCellEditor(this.editorDelete);    

    }

    /**
     * Método público para añadir una nueva notificación desde otras partes de tu app
     */
    public void agregarNotificacion(Notificacion n) {
        Object[] fila = {n, n.getHoraEnvio(), n};
        modeloTabla.addRow(fila);
    }
    
    public void clearNotificaciones() {
      modeloTabla.setRowCount(0);
    }

    public void addListenerForElements(ControladorNotificaciones c){
      this.controlador = c;
      //Teniendo ya el listener, podemos construir las columnas interactivas
      configurarColumnasInteractivas(c);
    }

}
