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
    pedido.forEach(p -> agregarPedido(p));
  }

    /**
     * Método para construir y añadir dinámicamente un pedido a la interfaz.
     */
    public void agregarPedido(SolicitudPedido pedido) {
        double precioTotal = pedido.getCostePedido(); 
        String fecha = pedido.getFechaRealizacion().toString();
        Map<SimpleEntry<LineaProductoVenta, Integer>, Double> mapaProductos = pedido.getRecaudacionProductos();

        // Panel contenedor del pedido individual
        JPanel panelPedido = new JPanel();
        panelPedido.setLayout(new BorderLayout(5, 5));
        
        // Borde decorativo con el título de la fecha
        panelPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Pedido realizado el:  " + fecha),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        
        // Evitar que el BoxLayout lo estire verticalmente más de lo necesario
        panelPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // --- CABECERA DEL PEDIDO (Precio) ---
        JLabel lblPrecio = new JLabel("Total pagado: " + String.format("%.2f", precioTotal) + " €");
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblPrecio.setForeground(new Color(34, 139, 34)); // Verde oscuro
        panelPedido.add(lblPrecio, BorderLayout.NORTH);

        // --- LISTA DE PRODUCTOS (Scroll Horizontal) ---
        // Usamos FlowLayout configurado sin saltos de línea (ideal para scroll horizontal)
        JPanel panelProductos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        // Crear una "tarjeta" visual para cada producto
        for (SimpleEntry<LineaProductoVenta, Integer> entrada : mapaProductos.keySet()) {
            JPanel panelProductoConcreto = new JPanel();
            panelProductoConcreto.setLayout(new BoxLayout(panelProductoConcreto, BoxLayout.Y_AXIS));
            JLabel nameLabel = new JLabel(entrada.getKey().getNombre());
            JLabel unitsLabel = new JLabel("Units bought: " + entrada.getValue());
            JLabel priceLabel = new JLabel("Total price for the product: " + mapaProductos.get(entrada));
            JButton infoButton = new JButton("Product information");

            infoButton.setActionCommand("" + entrada.getKey().getID());

            formatLabel(nameLabel); formatLabel(unitsLabel); formatLabel(priceLabel);
            panelProductoConcreto.add(nameLabel);
            panelProductoConcreto.add(unitsLabel);
            panelProductoConcreto.add(priceLabel);
            panelProductoConcreto.add(infoButton);

            panelProductos.add(panelProductoConcreto);
        }

        // Envolver los productos en su propio JScrollPane horizontal
        JScrollPane scrollHorizontal = new JScrollPane(panelProductos);
        scrollHorizontal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollHorizontal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollHorizontal.getHorizontalScrollBar().setUnitIncrement(16);
        scrollHorizontal.setBorder(BorderFactory.createEmptyBorder()); // Quitar el borde extra del scroll

        panelPedido.add(scrollHorizontal, BorderLayout.CENTER);

        // --- AÑADIR AL CONTENEDOR PRINCIPAL ---
        contenedorPedidos.add(panelPedido);
        // Añadir un espacio rígido para separar visualmente los pedidos
        contenedorPedidos.add(Box.createRigidArea(new Dimension(0, 15)));

        // Refrescar la UI
        contenedorPedidos.revalidate();
        contenedorPedidos.repaint();
    }

  private void formatLabel(JLabel label){
    label.setOpaque(true);
    label.setBackground(new Color(240, 248, 255)); // Azul claro
    label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(176, 196, 222)), // Borde azul claro
            BorderFactory.createEmptyBorder(10, 15, 10, 15) // Padding interno
    ));
  }

  public void addListenerForButtons(ControladorHistorialPedidos c){
    this.botonesInformation.forEach(b -> b.addActionListener(c));
  }
}
