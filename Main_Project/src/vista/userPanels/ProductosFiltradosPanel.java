package vista.userPanels;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel that displays products based on search results or filters.
 */
public class ProductosFiltradosPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final Color BACKGROUND_COLOR = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private JPanel productScrollPanel;

    /**
     * Constructs the panel and initializes the UI components.
     */
    public ProductosFiltradosPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(BACKGROUND_COLOR);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));

        productScrollPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        productScrollPanel.setBackground(BACKGROUND_COLOR);
        productScrollPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setBackground(BACKGROUND_COLOR);
        gridContainer.add(productScrollPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(gridContainer);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(BACKGROUND_COLOR);
        
        contentWrapper.add(scroll, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Updates the product list displayed in the grid.
     */
    public void actualizarProductos(List<LineaProductoVenta> productos, ActionListener controlador) {
        productScrollPanel.removeAll();

        if (productos == null || productos.isEmpty()) {
            JLabel emptyLabel = new JLabel("No products found.");
            emptyLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            productScrollPanel.add(emptyLabel);
        } else {
            for (LineaProductoVenta p : productos) {
                JPanel card = createCard(p, controlador);
                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                wrapper.setBackground(BACKGROUND_COLOR);
                wrapper.add(card);
                productScrollPanel.add(wrapper);
            }
        }

        productScrollPanel.revalidate();
        productScrollPanel.repaint();
    }

    private JPanel createCard(LineaProductoVenta prod, ActionListener controlador) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(10,10,10,10)));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(220, 290));

        JPanel headerProd = new JPanel(new BorderLayout(5, 0));
        headerProd.setOpaque(false);
        headerProd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        headerProd.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblName = new JLabel(prod.getNombre());
        lblName.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        headerProd.add(lblName, BorderLayout.CENTER);
        
        card.add(headerProd);

        JLabel lblDesc = new JLabel(prod.getDescripcion() != null ? prod.getDescripcion() : "");
        lblDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblDesc);

        card.add(Box.createVerticalStrut(10));

        ImageIcon originalIcon = new ImageIcon(prod.getFoto().getPath()); 
        Image scaledImg = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JLabel img = new JLabel(scaledIcon, SwingConstants.CENTER);
        img.setOpaque(true);
        img.setBackground(new Color(220,220,220));
        img.setPreferredSize(new Dimension(160, 90));
        img.setMaximumSize(new Dimension(160,90));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(img);

        card.add(Box.createVerticalStrut(10));

        JLabel lblPrice = new JLabel(String.format("%.2f €", prod.getPrecio()));
        lblPrice.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblPrice.setForeground(Color.BLACK);
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pricePanel.setOpaque(false);
        pricePanel.add(lblPrice);
        
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
            lblPrice.setForeground(Color.RED);
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
        card.add(pricePanel);

        card.add(Box.createVerticalStrut(8));

        JButton btnAdd = new JButton("ADD TO CART");
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.setActionCommand("ADD_" + prod.getID());
        btnAdd.addActionListener(controlador);
        card.add(btnAdd);

        card.add(Box.createVerticalStrut(5));

        JButton btnInfo = new JButton("More Information");
        btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInfo.setActionCommand("INFO_" + prod.getID());
        btnInfo.addActionListener(controlador);
        card.add(btnInfo);

        return card;
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }

    /**
     * Displays an information message dialog.
     */
    public void mostrarMensaje(String msg, String titulo) {
        JOptionPane.showMessageDialog(this, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}