package vista.empleadoPanel;

import modelo.producto.LineaProductoVenta;
import modelo.solicitud.SolicitudPedido;
import modelo.aplicacion.Catalogo;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

public class ViewOrderWindow extends JDialog {

    public ViewOrderWindow(JFrame parent, SolicitudPedido pedido) {
        super(parent, "Order Details", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        double precioTotal = pedido.getCostePedido(); 
        String fecha = pedido.getFechaRealizacion().toString();
        Map<SimpleEntry<LineaProductoVenta, Integer>, Double> mapaProductos = pedido.getRecaudacionProductos();

        JLabel lblInfo = new JLabel("Order from: " + fecha + " | Total: " + String.format("%.2f", precioTotal) + " €");
        lblInfo.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel panelTop = new JPanel(new FlowLayout());
        panelTop.add(lblInfo);
        add(panelTop, BorderLayout.NORTH);

        JPanel panelProductos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        for (SimpleEntry<LineaProductoVenta, Integer> entrada : mapaProductos.keySet()) {
            JPanel panelProductoConcreto = new JPanel();
            panelProductoConcreto.setLayout(new BoxLayout(panelProductoConcreto, BoxLayout.Y_AXIS));
            panelProductoConcreto.setPreferredSize(new Dimension(300, 200));
            panelProductoConcreto.setBackground(new Color(240, 248, 255));
            panelProductoConcreto.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(176, 196, 222)), 
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));

            JLabel nameLabel = new JLabel(entrada.getKey().getNombre());
            JLabel unitsLabel = new JLabel("Units bought: " + entrada.getValue());
            JLabel priceLabel = new JLabel("Total price: " + String.format("%.2f", mapaProductos.get(entrada)) + " €");
            JButton infoButton = new JButton("Product Information");
            
            infoButton.addActionListener(e -> {
                vista.clienteWindows.VentanaDetallesProducto dialog = new vista.clienteWindows.VentanaDetallesProducto(
                        SwingUtilities.getWindowAncestor(this), 
                        Catalogo.getInstancia().buscarProductoNuevo(entrada.getKey().getID())
                );
                dialog.setVisible(true);
            });

            ImageIcon iconoOriginal = new ImageIcon(entrada.getKey().getFoto().getPath()); 
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon iconoEscalado = new ImageIcon(imgEscalada);
            JLabel imagenLabel = new JLabel(iconoEscalado);

            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            unitsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            panelProductoConcreto.add(nameLabel);
            panelProductoConcreto.add(Box.createVerticalGlue()); 
            panelProductoConcreto.add(imagenLabel);
            panelProductoConcreto.add(Box.createVerticalGlue()); 
            panelProductoConcreto.add(unitsLabel);
            panelProductoConcreto.add(priceLabel);
            panelProductoConcreto.add(Box.createRigidArea(new Dimension(0, 10))); 
            panelProductoConcreto.add(infoButton);

            panelProductos.add(panelProductoConcreto);
        }

        JScrollPane scrollHorizontal = new JScrollPane(panelProductos);
        scrollHorizontal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollHorizontal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollHorizontal.setViewportView(panelProductos); 
        scrollHorizontal.getHorizontalScrollBar().setUnitIncrement(16);
        scrollHorizontal.setBorder(BorderFactory.createEmptyBorder()); 

        add(scrollHorizontal, BorderLayout.CENTER);
        
        JPanel panelSur = new JPanel();
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        panelSur.add(btnClose);
        add(panelSur, BorderLayout.SOUTH);
    }
}
