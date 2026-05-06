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
 * Clase que representa el contenedor principal con el scroll vertical.
 */
public class HistorialPedidosPanel extends JPanel {
    private JPanel contenedorPedidos;
    private HeaderPanel headerPanel = new HeaderPanel();
    private List<JButton> botonesInformation = new ArrayList<>();

    private ControladorHistorialPedidos controladorHistorialPedidos;

    public HeaderPanel getHeaderPanel()	{return headerPanel;}

    public HistorialPedidosPanel() {
        setLayout(new BorderLayout());

        // Contenedor donde se apilarán los pedidos verticalmente
        contenedorPedidos = new JPanel();
        contenedorPedidos.setLayout(new BoxLayout(contenedorPedidos, BoxLayout.Y_AXIS));
        // Un poco de margen interior general
        contenedorPedidos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll vertical general
        JScrollPane scrollVertical = new JScrollPane(contenedorPedidos);
        scrollVertical.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollVertical.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollVertical.getVerticalScrollBar().setUnitIncrement(16); // Scroll de rueda del ratón más fluido

        add(headerPanel, BorderLayout.NORTH);
        add(scrollVertical, BorderLayout.CENTER);
    }

  public void agregarPedido(List<SolicitudPedido> pedido){
    contenedorPedidos.removeAll();
    botonesInformation.clear();
    pedido.forEach(p -> agregarPedido(p));
    contenedorPedidos.revalidate();
    contenedorPedidos.repaint();
  }
  /**
    * Método para construir y añadir dinámicamente un pedido a la interfaz.
    */
  public void agregarPedido(SolicitudPedido pedido) {
    double precioTotal = pedido.getCostePedido(); 
    String fecha = pedido.getFechaRealizacion().toString();
    Map<SimpleEntry<LineaProductoVenta, Integer>, Double> mapaProductos = pedido.getRecaudacionProductos();

    JPanel panelPedido = new JPanel();
    panelPedido.setLayout(new BorderLayout(5, 5));
    
    panelPedido.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Pedido realizado el:  " + fecha),
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
    ));
    
    // Ajustamos la altura máxima del bloque del pedido
    panelPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

    JLabel lblPrecio = new JLabel("Total pagado: " + String.format("%.2f", precioTotal) + " €");
    lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
    lblPrecio.setForeground(new Color(34, 139, 34)); 
    panelPedido.add(lblPrecio, BorderLayout.NORTH);

    // --- CAMBIO CRÍTICO 1: Usamos un JPanel con FlowLayout para que actúe como "ancla" ---
    // FlowLayout permite que el contenido mantenga su tamaño preferido y se desborde a lo ancho.
    JPanel panelProductos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    
    for (SimpleEntry<LineaProductoVenta, Integer> entrada : mapaProductos.keySet()) {
        JPanel panelProductoConcreto = new JPanel();
        panelProductoConcreto.setLayout(new BoxLayout(panelProductoConcreto, BoxLayout.Y_AXIS));
        
        // --- CAMBIO CRÍTICO 2: Forzar un tamaño preferido a la tarjeta ---
        // Si no fijamos el ancho, el BoxLayout lo colapsará al mínimo posible.
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
        // Si el controlador todavía no está asignado, lo añadimos de todas formas,
        // o nos aseguramos de que cuando se llame, sea correcto.
        if (controladorHistorialPedidos != null) {
            infoButton.addActionListener(controladorHistorialPedidos);
        }
        infoButton.setActionCommand("INFO_" + entrada.getKey().getID());
        this.botonesInformation.add(infoButton);

        JButton reviewButton = new JButton("Add Review");
        reviewButton.setBackground(new Color(255, 140, 0)); // Naranja
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

    // --- CAMBIO CRÍTICO 3: Configuración del JScrollPane ---
    JScrollPane scrollHorizontal = new JScrollPane(panelProductos);
    scrollHorizontal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scrollHorizontal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    // Esto es vital: obliga al JScrollPane a reconocer el tamaño del panelProductos
    scrollHorizontal.setViewportView(panelProductos); 
    scrollHorizontal.getHorizontalScrollBar().setUnitIncrement(16);
    scrollHorizontal.setBorder(BorderFactory.createEmptyBorder()); 

    // Ajustamos el tamaño preferido del scroll para que el BorderLayout sepa cuánto espacio ocupar
    scrollHorizontal.setPreferredSize(new Dimension(400, 260));

    panelPedido.add(scrollHorizontal, BorderLayout.CENTER);

    contenedorPedidos.add(panelPedido);
    contenedorPedidos.add(Box.createRigidArea(new Dimension(0, 15)));

    contenedorPedidos.revalidate();
    contenedorPedidos.repaint();
  }

  public void addListenerForButtons(ControladorHistorialPedidos c){
    this.controladorHistorialPedidos = c;
  }
}
