package vista.empleadoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import modelo.solicitud.SolicitudIntercambio;
import vista.userPanels.HeaderPanel;
import vista.userPanels.InterchangeCardPanel;
import controladores.ControladorManageInterchanges;
import modelo.producto.ProductoSegundaMano;

public class ManageInterchangesPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Color COLOR_FONDO = new Color(153, 180, 209);
    
    private HeaderPanel headerPanel;
    private JPanel mainColumn;
    
    private ControladorManageInterchanges controlador;

    public ManageInterchangesPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        
        // Encabezado global
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(10, 80, 20, 80));
        
        // 1. Encabezado principal (Header)
        JLabel lblTitulo = new JLabel("MANAGE INTERCHANGES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(54, 119, 189));
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        // 2. Panel de Estado
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel lblStatus = new JLabel("PENDING INTERCHANGES", SwingConstants.CENTER);
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

    public void setControlador(ControladorManageInterchanges controlador) {
        this.controlador = controlador;
    }
    
    public ControladorManageInterchanges getControlador() {
        return this.controlador;
    }

    public void actualizarIntercambios(List<SolicitudIntercambio> intercambios, ActionListener actionListener) {
        mainColumn.removeAll();
        
        if (intercambios != null && !intercambios.isEmpty()) {
            for (int i = 0; i < intercambios.size(); i++) {
                SolicitudIntercambio s = intercambios.get(i);
                
                String headerLabel = "Interchange: " + s.getOferta().getOfertante().getNombreUsuario() + " ⇄ " + s.getOferta().getDestinatario().getNombreUsuario();
                
                ProductoSegundaMano[] pOfertados = s.getOferta().productosOfertados().toArray(new ProductoSegundaMano[0]);
                ProductoSegundaMano[] pSolicitados = s.getOferta().productosSolicitados().toArray(new ProductoSegundaMano[0]);
                
                String givenTitle = s.getOferta().getOfertante().getNombreUsuario() + "'s PRODUCTS ▼";
                String receivedTitle = s.getOferta().getDestinatario().getNombreUsuario() + "'s PRODUCTS ▼";

                InterchangeCardPanel card = new InterchangeCardPanel(
                        headerLabel, 0.0, pOfertados, pSolicitados, InterchangeCardPanel.Modo.EMPLOYEE, givenTitle, receivedTitle
                );
                
                // Le pasamos el action command con el indice de la solicitud
                final int index = i;
                card.addAcceptListener(e -> {
                    actionListener.actionPerformed(new java.awt.event.ActionEvent(card, java.awt.event.ActionEvent.ACTION_PERFORMED, "APPROVE_" + index));
                });
                
                // Escuchamos el botón de + Info
                card.setInfoListener(e -> {
                    actionListener.actionPerformed(e);
                });
                
                mainColumn.add(card);
                mainColumn.add(Box.createVerticalStrut(15));
            }
        } else {
            JLabel lblEmpty = new JLabel("No pending interchanges", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("SansSerif", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.DARK_GRAY);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainColumn.add(Box.createVerticalStrut(30));
            mainColumn.add(lblEmpty);
        }
        
        mainColumn.revalidate();
        mainColumn.repaint();
    }

    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}
