package vista.userPanels;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel para listar los productos más top de la tienda.
 */
public class OutstandingPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color COLOR_FONDO = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private JPanel panelScrollProductos;

    /**
     * Monta el panel con el título y el grid.
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
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        contentWrapper.add(scroll, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Refresca el grid con los productos destacados.
     * @param productos los productos pata negra
     * @param controlador listener pa los botones
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

        // Rating: show stars (filled/half/empty). If no ratings, show a distinct badge.
        double ratingVal = prod.obtenerPuntuacionMedia();
        if (ratingVal <= 0.0) {
            JLabel noRating = new JLabel("No ratings yet");
            noRating.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
            noRating.setForeground(Color.GRAY);
            noRating.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(noRating);
        } else {
            JComponent stars = createRatingStars(ratingVal);
            stars.setAlignmentX(Component.CENTER_ALIGNMENT);
            tarjeta.add(stars);
        }

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

    /**
     * @return la barra de arriba
     */
    public HeaderPanel getHeaderPanel() { return headerPanel; }

    /**
     * Tira un pop-up por pantalla.
     * @param msg mensaje
     * @param titulo título de la ventana
     */
    public void mostrarMensaje(String msg, String titulo) {
        JOptionPane.showMessageDialog(this, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== Rating stars rendering ====================
    private JComponent createRatingStars(double rating) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        p.setOpaque(false);
        int starSize = 14;
        for (int i = 1; i <= 5; i++) {
            double starValue = rating - (i - 1);
            double fill;
            if (starValue >= 0.75) fill = 1.0;
            else if (starValue >= 0.25) fill = 0.5;
            else fill = 0.0;
            JLabel starLbl = new JLabel(new StarIcon(starSize, fill));
            starLbl.setPreferredSize(new Dimension(starSize + 2, starSize + 2));
            p.add(starLbl);
        }
        return p;
    }

    /** Simple Icon that draws a star glyph and fills it partially according to fillPercent (0..1). */
    private static class StarIcon implements Icon {
        private final int size;
        private final double fillPercent;
        private final Color fullColor = new Color(255, 140, 0);
        private final Color emptyColor = new Color(200, 200, 200);

        StarIcon(int size, double fillPercent) {
            this.size = size;
            this.fillPercent = Math.max(0.0, Math.min(1.0, fillPercent));
        }

        @Override
        public int getIconWidth() { return size; }

        @Override
        public int getIconHeight() { return size; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                String star = "\u2605"; // solid star glyph
                Font font = new Font("Dialog", Font.PLAIN, size);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics();
                int strW = fm.stringWidth(star);
                int strH = fm.getAscent();
                int drawX = x + (getIconWidth() - strW) / 2;
                int drawY = y + (getIconHeight() + strH - fm.getDescent()) / 2;

                // Draw empty (background) star first
                g2.setColor(emptyColor);
                g2.drawString(star, drawX, drawY);

                // Overlay filled portion by clipping
                int clipW = (int) Math.round(getIconWidth() * fillPercent);
                if (clipW > 0) {
                    Shape oldClip = g2.getClip();
                    g2.setClip(drawX, y, clipW, getIconHeight());
                    g2.setColor(fullColor);
                    g2.drawString(star, drawX, drawY);
                    g2.setClip(oldClip);
                }
            } finally {
                g2.dispose();
            }
        }
    }
}