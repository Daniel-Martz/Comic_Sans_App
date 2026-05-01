package vista.userPanels;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel que muestra los productos destacados.
 */
public class OutstandingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color COLOR_FONDO = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private JPanel panelScrollProductos;

    public OutstandingPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Titulo
        JLabel lblTitulo = new JLabel("PRODUCTOS DESTACADOS ⭐", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitulo.setForeground(Color.DARK_GRAY);
        lblTitulo.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentWrapper.add(lblTitulo, BorderLayout.NORTH);

        panelScrollProductos = new JPanel(new GridLayout(0, 4, 15, 15));
        panelScrollProductos.setBackground(COLOR_FONDO);
        panelScrollProductos.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel contenedorGrid = new JPanel(new BorderLayout());
        contenedorGrid.setBackground(COLOR_FONDO);
        contenedorGrid.add(panelScrollProductos, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(contenedorGrid);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(COLOR_FONDO);
        contentWrapper.add(scroll, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    public void actualizarProductos(List<LineaProductoVenta> productos, ActionListener controlador) {
        panelScrollProductos.removeAll();

        if (productos == null || productos.isEmpty()) {
            JLabel vacio = new JLabel("No hay productos destacados en este momento.");
            vacio.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            panelScrollProductos.add(vacio);
        } else {
            for (LineaProductoVenta p : productos) {
                JPanel tarjeta = crearTarjeta(p, controlador);
                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                wrapper.setBackground(COLOR_FONDO);
                wrapper.add(tarjeta);
                panelScrollProductos.add(wrapper);
            }
        }

        panelScrollProductos.revalidate();
        panelScrollProductos.repaint();
    }

    private JPanel crearTarjeta(LineaProductoVenta prod, ActionListener controlador) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(10,10,10,10)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(220, 290));

        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblNombre);

        tarjeta.add(Box.createVerticalStrut(5));

        JLabel lblRating = new JLabel(String.format("⭐ %.1f / 5", prod.obtenerPuntuacionMedia()));
        lblRating.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        lblRating.setForeground(new Color(255, 140, 0));
        lblRating.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblRating);

        tarjeta.add(Box.createVerticalStrut(10));

        JLabel img = new JLabel("IMAGE", SwingConstants.CENTER);
        img.setOpaque(true);
        img.setBackground(new Color(220,220,220));
        img.setPreferredSize(new Dimension(160, 90));
        img.setMaximumSize(new Dimension(160,90));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(img);

        tarjeta.add(Box.createVerticalStrut(10));

        JLabel lblPrecio = new JLabel(String.format("%.2f €", prod.getPrecio()));
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblPrecio);

        tarjeta.add(Box.createVerticalStrut(8));

        JButton btnAdd = new JButton("ADD TO CART");
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.setActionCommand("ADD_" + prod.getID());
        btnAdd.addActionListener(controlador);
        tarjeta.add(btnAdd);

        tarjeta.add(Box.createVerticalStrut(5));

        JButton btnInfo = new JButton("More Information");
        btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInfo.setActionCommand("INFO_" + prod.getID());
        btnInfo.addActionListener(controlador);
        tarjeta.add(btnInfo);

        return tarjeta;
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }

    public void mostrarMensaje(String msg, String titulo) {
        JOptionPane.showMessageDialog(this, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
