package vista.empleadoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.solicitud.SolicitudValidacion;
import modelo.producto.ProductoSegundaMano;
import vista.userPanels.HeaderPanel;
import controladores.ControladorValidationRequests;

// TODO: Auto-generated Javadoc
/**
 * Panel de gestión de validaciones para el empleado.
 */
public class ValidationRequestsPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The color fondo. */
    private final Color COLOR_FONDO = new Color(153, 180, 209);
    
    /** The header panel. */
    private HeaderPanel headerPanel;
    
    /** The main column. */
    private JPanel mainColumn;
    
    /** The controlador. */
    private ControladorValidationRequests controlador;

    /**
     * Instantiates a new validation requests panel.
     */
    public ValidationRequestsPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        
        // Encabezado global
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(10, 80, 20, 80)); 
        
        // 1. Encabezado principal (Header)
        JLabel lblTitulo = new JLabel("VALIDATION REQUESTS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(54, 119, 189)); // Azul medio oscuro
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        // 2. Panel de Estado
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel lblStatus = new JLabel("AWAITING VALIDATION", SwingConstants.CENTER);
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblStatus.setForeground(Color.DARK_GRAY);
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(245, 247, 250));
        lblStatus.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 2, true),
                new EmptyBorder(8, 25, 8, 25)
        ));
        statusPanel.add(lblStatus);
        
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(lblTitulo, BorderLayout.NORTH);
        topContainer.add(statusPanel, BorderLayout.CENTER);
        
        contentWrapper.add(topContainer, BorderLayout.NORTH);
        
        // 3. Área de Contenido Principal (1 columna vertical)
        mainColumn = new JPanel();
        mainColumn.setLayout(new BoxLayout(mainColumn, BoxLayout.Y_AXIS));
        mainColumn.setBackground(COLOR_FONDO);
        mainColumn.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(mainColumn);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(COLOR_FONDO);
        scroll.setBorder(new LineBorder(new Color(100, 130, 175), 2, true)); 
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        contentWrapper.add(scroll, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }
    
    /**
     * Sets the controlador.
     *
     * @param controlador the new controlador
     */
    public void setControlador(ControladorValidationRequests controlador) {
        this.controlador = controlador;
    }
    
    /**
     * Gets the controlador.
     *
     * @return the controlador
     */
    public ControladorValidationRequests getControlador() {
        return this.controlador;
    }

    /**
     * Actualizar solicitudes.
     *
     * @param solicitudes the solicitudes
     * @param actionListener the action listener
     */
    public void actualizarSolicitudes(List<SolicitudValidacion> solicitudes, ActionListener actionListener) {
        mainColumn.removeAll();
        
        if (solicitudes != null && !solicitudes.isEmpty()) {
            for (SolicitudValidacion s : solicitudes) {
                JPanel card = createCard(s, actionListener);
                mainColumn.add(card);
                mainColumn.add(Box.createVerticalStrut(15));
            }
        } else {
            JLabel lblEmpty = new JLabel("No pending validation requests", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("SansSerif", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.DARK_GRAY);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainColumn.add(Box.createVerticalStrut(30));
            mainColumn.add(lblEmpty);
        }
        
        mainColumn.revalidate();
        mainColumn.repaint();
    }
    
    /**
     * Creates the card.
     *
     * @param solicitud the solicitud
     * @param actionListener the action listener
     * @return the j panel
     */
    private JPanel createCard(SolicitudValidacion solicitud, ActionListener actionListener) {
        ProductoSegundaMano prod = solicitud.getProductoAValidar();
        
        boolean hasImage = (prod.getFoto() != null && prod.getFoto().exists());
        return buildCardBase(
                prod.getNombre(), 
                "Pending", 
                prod.getDescripcion(), 
                hasImage ? prod.getFoto().getAbsolutePath() : null, 
                "EDIT_" + prod.getID(), 
                actionListener
        );
    }

    /**
     * Builds the card base.
     *
     * @param productName the product name
     * @param condition the condition
     * @param description the description
     * @param imagePath the image path
     * @param actionCommand the action command
     * @param actionListener the action listener
     * @return the j panel
     */
    private JPanel buildCardBase(String productName, String condition, String description, String imagePath, String actionCommand, ActionListener actionListener) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(new Color(245, 247, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Izquierda de la Tarjeta (Imagen)
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagePath == null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220, 220, 220));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    
                    g2.setColor(Color.DARK_GRAY);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(20, 20, getWidth() - 20, getHeight() - 20);
                    g2.drawLine(getWidth() - 20, 20, 20, getHeight() - 20);
                }
            }
        };
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(130, 130));
        
        if (imagePath != null) {
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img));
                imagePanel.add(imgLabel, BorderLayout.CENTER);
            } catch (Exception e) {
                // Ignorado, pintará el componente por defecto (X)
            }
        }
        
        card.add(imagePanel, BorderLayout.WEST);
        
        // Derecha de la Tarjeta (Detalles)
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setOpaque(false);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        Font fontLabel = new Font("SansSerif", Font.BOLD, 13);
        Font fontValue = new Font("SansSerif", Font.PLAIN, 13);
        
        // Fila 1: PRODUCT NAME y Botón EDIT
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel lblName = new JLabel("PRODUCT NAME: ");
        lblName.setFont(fontLabel);
        lblName.setForeground(new Color(50, 50, 50));
        
        JLabel lblNameVal = new JLabel(productName);
        lblNameVal.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNameVal.setForeground(new Color(54, 119, 189));
        
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        nameRow.setOpaque(false);
        nameRow.add(lblName);
        nameRow.add(lblNameVal);
        
        topPanel.add(nameRow, BorderLayout.CENTER);
        
        JButton btnEdit = new JButton("EDIT / VALIDATE");
        btnEdit.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnEdit.setBackground(new Color(46, 204, 113));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(39, 174, 96), 1, true),
                new EmptyBorder(6, 15, 6, 15)
        ));
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setActionCommand(actionCommand);
        if (actionListener != null) {
            btnEdit.addActionListener(actionListener);
        }
        topPanel.add(btnEdit, BorderLayout.EAST);
        
        textPanel.add(topPanel);
        textPanel.add(Box.createVerticalStrut(5));
        
        // Resto de información
        textPanel.add(createDetailRow("CONDITION: ", condition, fontLabel, fontValue));
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(createDetailRow("DESCRIPTION: ", description, fontLabel, fontValue));
        
        detailsPanel.add(textPanel, BorderLayout.CENTER);
        card.add(detailsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Creates the detail row.
     *
     * @param label the label
     * @param value the value
     * @param labelFont the label font
     * @param valueFont the value font
     * @return the j panel
     */
    private JPanel createDetailRow(String label, String value, Font labelFont, Font valueFont) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        row.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(labelFont);
        lbl.setForeground(new Color(50, 50, 50));
        
        JLabel val = new JLabel(value);
        val.setFont(valueFont);
        val.setForeground(new Color(100, 100, 100));
        
        row.add(lbl);
        row.add(val);
        return row;
    }
    
    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}