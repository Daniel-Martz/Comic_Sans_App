package vista.userPanels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controladores.ControladorNotificaciones;
import modelo.notificacion.Notificacion;
import vista.elements.*;
import java.awt.*;
import java.util.*;
import java.util.List;
/**
 * Pantalla que simula una bandeja de entrada para notificaciones.
 */
public class NotificacionesPanel extends JPanel{

    private JTable tablaNotificaciones;
    private DefaultTableModel modeloTabla;
    private HeaderPanel headerPanel = new HeaderPanel();
    private ControladorNotificaciones controlador;
    private ButtonRendererNotification rendererRead;
    private ButtonEditor editorRead;
    private DeleteButtonRendererNotifications rendererDelete;
    private DeleteButtonEditor editorDelete;
    private Set<Notificacion> notifsEnLaTabla = new HashSet<>();

    /** @return el header modificado pa que no haya tanto lio visual */
    public HeaderPanel getHeaderPanel()	{return headerPanel;}

    /**
     * Monta la tabla y sus movidas.
     */
    public NotificacionesPanel() {
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        headerPanel.setPreferredSize(new Dimension(headerPanel.getPreferredSize().width, headerPanel.getPreferredSize().height)); 
        headerPanel.configurarMenuNotificaciones();
        add(headerPanel, BorderLayout.NORTH);

        JPanel centroPanel = new JPanel();
        centroPanel.setLayout(new BorderLayout(0, 10));
        centroPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Notification Inbox");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        centroPanel.add(lblTitulo, BorderLayout.NORTH);

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
        tablaNotificaciones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
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
    /**
     * Enchufa una notificación nueva al final de la tabla.
     * @param n la notificacion a meter
     */
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
     * Adds a new notification to the table if it's not already present.
     */
    public void agregarNotificacion(Notificacion n) {
        if(!notifsEnLaTabla.contains(n)) {
            Object[] fila = {n, n.getHoraEnvio(), n};
            modeloTabla.addRow(fila);
            notifsEnLaTabla.add(n);
        }
    }
    
    /**
     * Borra todo de la tabla.
     */
    public void clearNotificaciones() {
        modeloTabla.setRowCount(0);
        notifsEnLaTabla.clear();
    }

    /**
     * Guarda el controlador para configurar la tabla.
     * @param c controlador
     */
    public void addListenerForElements(ControladorNotificaciones c){
        this.controlador = c;
        configurarColumnasInteractivas(c);
    }

    /**
     * @return el controlador
     */
    public ControladorNotificaciones getControladorPrincipal(){return controlador;}

}
