package vista.userPanels;

import controladores.ControladorHistorialPedidos;
import modelo.producto.LineaProductoVenta;
import modelo.solicitud.SolicitudPedido;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.List;

/**
 * Main container that displays the user's order history with vertical scrolling.
 */
public class HistorialPedidosPanel extends JPanel {
    private static final long serialVersionUID = 1L;
	private JPanel contenedorPedidos;
    private HeaderPanel headerPanel = new HeaderPanel();
    private List<JButton> botonesInformation = new ArrayList<>();

    private ControladorHistorialPedidos controladorHistorialPedidos;

    /**
     * @return
     */
    public HeaderPanel getHeaderPanel() { return headerPanel; }

    /**
     * Constructs the panel and initializes the scrollable container.
     */
    public HistorialPedidosPanel() {
        setLayout(new BorderLayout());

        contenedorPedidos = new JPanel();
        contenedorPedidos.setLayout(new BoxLayout(contenedorPedidos, BoxLayout.Y_AXIS));
        contenedorPedidos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollVertical = new JScrollPane(contenedorPedidos);
        scrollVertical.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollVertical.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollVertical.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollVertical, BorderLayout.CENTER);
    }

    /**
     * Adds a list of orders to the container.
     */
    public void agregarPedido(List<SolicitudPedido> pedido) {
        contenedorPedidos.removeAll();
        botonesInformation.clear();
        pedido.forEach(p -> agregarPedido(p));
        contenedorPedidos.revalidate();
        contenedorPedidos.repaint();
    }

    /**
     * Dynamically builds and adds a specific order to the interface.
     */
    public void agregarPedido(SolicitudPedido pedido) {
        double precioTotal = pedido.getCostePedido(); 
        String fecha = pedido.getFechaRealizacion().toString();
        Map<SimpleEntry<LineaProductoVenta, Integer>, Double> mapaProductos = pedido.getRecaudacionProductos();

        JPanel panelPedido = new JPanel();
        panelPedido.setLayout(new BorderLayout(5, 5));
        
        panelPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Order placed on: " + fecha),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        
        panelPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel lblPrecio = new JLabel("Total paid: " + String.format("%.2f", precioTotal) + " €");
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblPrecio.setForeground(new Color(34, 139, 34)); 
        panelPedido.add(lblPrecio, BorderLayout.NORTH);

        JPanel panelProductos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        for (SimpleEntry<LineaProductoVenta, Integer> entrada : mapaProductos.keySet()) {
            JPanel panelProductoConcreto = new JPanel();
            panelProductoConcreto.setLayout(new BoxLayout(panelProductoConcreto, BoxLayout.Y_AXIS));
            
            panelProductoConcreto.setPreferredSize(new Dimension(320, 240));
            panelProductoConcreto.setBackground(new Color(240, 248, 255));
            panelProductoConcreto.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(176, 196, 222)), 
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));

            JLabel nameLabel = new JLabel(entrada.getKey().getNombre());
            JLabel unitsLabel = new JLabel("Units bought: " + entrada.getValue());
            JLabel priceLabel = new JLabel("Total price: " + String.format("%.2f", mapaProductos.get(entrada)) + " €");
            
            ImageIcon iconoOriginal = new ImageIcon(entrada.getKey().getFoto().getPath()); 
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon iconoEscalado = new ImageIcon(imgEscalada);
            JLabel imagenLabel = new JLabel(iconoEscalado);

            JButton infoButton = new JButton("Product Information");
            if (controladorHistorialPedidos != null) {
                infoButton.addActionListener(controladorHistorialPedidos);
            }
            infoButton.setActionCommand("INFO_" + entrada.getKey().getID());
            this.botonesInformation.add(infoButton);

            JButton reviewButton = new JButton("Add Review");
            reviewButton.setBackground(new Color(255, 140, 0));
            reviewButton.setForeground(Color.WHITE);
            if (controladorHistorialPedidos != null) {
                reviewButton.addActionListener(controladorHistorialPedidos);
            }
            reviewButton.setActionCommand("REVIEW_" + entrada.getKey().getID());

            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            unitsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            panelProductoConcreto.add(nameLabel);
            panelProductoConcreto.add(Box.createVerticalGlue()); 
            panelProductoConcreto.add(imagenLabel);
            panelProductoConcreto.add(Box.createVerticalGlue()); 
            panelProductoConcreto.add(unitsLabel);
            panelProductoConcreto.add(priceLabel);
            panelProductoConcreto.add(Box.createRigidArea(new Dimension(0, 10))); 
            
            JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            botonesPanel.setOpaque(false);
            botonesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            botonesPanel.add(infoButton);
            botonesPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            botonesPanel.add(reviewButton);
            
            panelProductoConcreto.add(botonesPanel);

            panelProductos.add(panelProductoConcreto);
        }

        JScrollPane scrollHorizontal = new JScrollPane(panelProductos);
        scrollHorizontal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollHorizontal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        scrollHorizontal.setViewportView(panelProductos); 
        scrollHorizontal.getHorizontalScrollBar().setUnitIncrement(16);
        scrollHorizontal.setBorder(BorderFactory.createEmptyBorder()); 

        scrollHorizontal.setPreferredSize(new Dimension(400, 260));

        panelPedido.add(scrollHorizontal, BorderLayout.CENTER);

        contenedorPedidos.add(panelPedido);
        contenedorPedidos.add(Box.createRigidArea(new Dimension(0, 15)));

        contenedorPedidos.revalidate();
        contenedorPedidos.repaint();
    }

    /**
     * Assigns the controller to handle interactions.
     */
    public void addListenerForButtons(ControladorHistorialPedidos c) {
        this.controladorHistorialPedidos = c;
    }
}