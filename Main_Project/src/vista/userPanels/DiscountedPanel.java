package vista.userPanels;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel that displays discounted products.
 */
public class DiscountedPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color BG_COLOR = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private JPanel productsScrollPanel;

    /**
     * Instantiates a new discounted products panel.
     */
    public DiscountedPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));

        JLabel titleLabel = new JLabel("DISCOUNTED PRODUCTS ⭐", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentWrapper.add(titleLabel, BorderLayout.NORTH);

        productsScrollPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        productsScrollPanel.setBackground(BG_COLOR);
        productsScrollPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setBackground(BG_COLOR);
        gridContainer.add(productsScrollPanel, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(gridContainer);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(BG_COLOR);
        contentWrapper.add(scroll, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Updates the displayed products in the scroll panel.
     *
     * @param products the list of products to display
     * @param controller the action listener handling interactions
     */
    public void updateProducts(List<LineaProductoVenta> products, ActionListener controller) {
        productsScrollPanel.removeAll();

        if (products == null || products.isEmpty()) {
            JLabel emptyLabel = new JLabel("No discounted products at the moment.");
            emptyLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            productsScrollPanel.add(emptyLabel);
        } else {
            for (LineaProductoVenta p : products) {
                JPanel card = createCard(p, controller);
                JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                wrapper.setBackground(BG_COLOR);
                wrapper.add(card);
                productsScrollPanel.add(wrapper);
            }
        }

        productsScrollPanel.revalidate();
        productsScrollPanel.repaint();
    }

    private JPanel createCard(LineaProductoVenta prod, ActionListener controller) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY, 2), new EmptyBorder(10, 10, 10, 10)));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(220, 290));

        JPanel prodHeader = new JPanel(new BorderLayout(5, 0));
        prodHeader.setOpaque(false);
        prodHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        prodHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel(prod.getNombre());
        nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        prodHeader.add(nameLabel, BorderLayout.CENTER);
        
        card.add(prodHeader);
        card.add(Box.createVerticalStrut(5));

        JLabel ratingLabel = new JLabel(String.format("⭐ %.1f / 5", prod.obtenerPuntuacionMedia()));
        ratingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        ratingLabel.setForeground(new Color(255, 140, 0));
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(ratingLabel);

        card.add(Box.createVerticalStrut(10));

        JLabel img = new JLabel("IMAGE", SwingConstants.CENTER);
        img.setOpaque(true);
        img.setBackground(new Color(220, 220, 220));
        img.setPreferredSize(new Dimension(160, 90));
        img.setMaximumSize(new Dimension(160, 90));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(img);

        card.add(Box.createVerticalStrut(10));

        JLabel priceLabel = new JLabel(String.format("%.2f €", prod.getPrecio()));
        priceLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        priceLabel.setForeground(Color.BLACK);
        
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pricePanel.setOpaque(false);
        pricePanel.add(priceLabel);
        
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
            priceLabel.setForeground(Color.RED);
            String descText = "%";
            if (d instanceof modelo.descuento.Precio) descText = "-" + ((modelo.descuento.Precio)d).getPorcentajeRebaja() + "%";
            else if (d instanceof modelo.descuento.RebajaUmbral) descText = "-" + ((modelo.descuento.RebajaUmbral)d).getPorcentajeRebaja() + "%";
            else if (d instanceof modelo.descuento.Cantidad) descText = ((modelo.descuento.Cantidad)d).getNumeroComprados() + "x" + ((modelo.descuento.Cantidad)d).getNumeroRecibidos();
            else if (d instanceof modelo.descuento.Regalo) descText = "GIFT";
            
            JButton descBtn = new JButton(descText);
            descBtn.setBackground(Color.RED);
            descBtn.setForeground(Color.YELLOW);
            descBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            descBtn.setMargin(new java.awt.Insets(2, 4, 2, 4));
            descBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            descBtn.setFocusPainted(false);
            descBtn.setActionCommand("DESCINFO_" + prod.getID());
            descBtn.addActionListener(controller);
            pricePanel.add(descBtn);
        }
        pricePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pricePanel);

        card.add(Box.createVerticalStrut(8));

        JButton btnAdd = new JButton("ADD TO CART");
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.setActionCommand("ADD_" + prod.getID());
        btnAdd.addActionListener(controller);
        card.add(btnAdd);

        card.add(Box.createVerticalStrut(5));

        JButton btnInfo = new JButton("More Information");
        btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInfo.setActionCommand("INFO_" + prod.getID());
        btnInfo.addActionListener(controller);
        card.add(btnInfo);

        return card;
    }

    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() { 
        return headerPanel; 
    }

    /**
     * Displays a message dialog.
     *
     * @param msg the message to display
     * @param title the title of the dialog
     */
    public void showMessage(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
}