package vista.userPanels;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel that displays outstanding products.
 */
public class OutstandingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color COLOR_FONDO = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private JPanel panelScrollProductos;

    /**
     * Initializes the panel and its components.
     */
    public OutstandingPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(COLOR_FONDO);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));

        JLabel lblTitulo = new JLabel("FEATURED PRODUCTS ⭐", SwingConstants.CENTER);
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

    /**
     * Refreshes the product list.
     */
    public void actualizarProductos(List<LineaProductoVenta> productos, ActionListener controlador) {
        panelScrollProductos.removeAll();

        if (productos == null || productos.isEmpty()) {
            JLabel vacio = new JLabel("No featured products available at this moment.");
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

        JPanel headerProd = new JPanel(new BorderLayout(5, 0));
        headerProd.setOpaque(false);
        headerProd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        headerProd.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        headerProd.add(lblNombre, BorderLayout.CENTER);
        
        tarjeta.add(headerProd);

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

        try {
            if (prod.getFoto() != null && prod.getFoto().exists()) {
                ImageIcon iconoOriginal = new ImageIcon(prod.getFoto().getPath());
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(160, 90, Image.SCALE_SMOOTH);
                img.setIcon(new ImageIcon(imgEscalada));
                img.setText("");
            }
        } catch (Exception ex) {
            img.setText("IMAGE");
        }
        tarjeta.add(img);

        tarjeta.add(Box.createVerticalStrut(10));

        JLabel lblPrecio = new JLabel(String.format("%.2f €", prod.getPrecio()));
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblPrecio.setForeground(Color.BLACK);
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pricePanel.setOpaque(false);
        pricePanel.add(lblPrecio);
        
        modelo.descuento.Descuento d = prod.getDescuento();
        if (d == null) {
            for (modelo.categoria.Categoria c : prod.getCategorias()) {
                if (c.getDescuento() != null && !c.getDescuento().haCaducado()) {
                    d = c.getDescuento();
                    break;
                }
            }
        }
        if (d != null && !d.haCaducado()) {
            lblPrecio.setForeground(Color.RED);
            String descText = "%";
            if (d instanceof modelo.descuento.Precio) descText = "-" + ((modelo.descuento.Precio)d).getPorcentajeRebaja() + "%";
            else if (d instanceof modelo.descuento.RebajaUmbral) descText = "-" + ((modelo.descuento.RebajaUmbral)d).getPorcentajeRebaja() + "%";
            else if (d instanceof modelo.descuento.Cantidad) descText = ((modelo.descuento.Cantidad)d).getNumeroComprados() + "x" + ((modelo.descuento.Cantidad)d).getNumeroRecibidos();
            else if (d instanceof modelo.descuento.Regalo) descText = "GIFT";
            
            JButton btnDesc = new JButton(descText);
            btnDesc.setBackground(Color.RED);
            btnDesc.setForeground(Color.YELLOW);
            btnDesc.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            btnDesc.setMargin(new java.awt.Insets(2, 4, 2, 4));
            btnDesc.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDesc.setFocusPainted(false);
            btnDesc.setActionCommand("DESCINFO_" + prod.getID());
            btnDesc.addActionListener(controlador);
            pricePanel.add(btnDesc);
        }
        pricePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(pricePanel);

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

    /**
     * Displays a message dialog.
     */
    public void mostrarMensaje(String msg, String titulo) {
        JOptionPane.showMessageDialog(this, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}