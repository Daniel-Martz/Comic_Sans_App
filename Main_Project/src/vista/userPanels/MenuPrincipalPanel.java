package vista.userPanels;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import modelo.producto.LineaProductoVenta;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel principal de usuario (Menu Principal).
*/
public class MenuPrincipalPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // --- Colores de la paleta del Mockup ---
    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 
    private final Color BANNER_SUB_COLOR = new Color(131, 117, 181); 
    private final Color BUY_NOW_COLOR = new Color(255, 204, 0);      

    // --- Componentes de la Cabecera ---
    private HeaderPanel headerPanel;

    // --- Contenedores Principales ---
    private JPanel recommendedPanel;
    private JPanel categoriesPanel;

    // --- Listas para gestión eficiente de Listeners ---
    private List<JButton> buyNowButtons;
    private List<JButton> categoryButtons;
    private ActionListener buyNowListener;

    public MenuPrincipalPanel() {
        buyNowButtons = new ArrayList<>();
        categoryButtons = new ArrayList<>();
        
        initComponents();
        initLayout();
    }
    

    private void initComponents() {
        headerPanel = new HeaderPanel();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // ==========================================
        // 1. HEADER (NORTE)
        // ==========================================
        add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. BODY (CENTRO)
        // ==========================================
        JPanel bodyContent = new JPanel();
        bodyContent.setLayout(new BoxLayout(bodyContent, BoxLayout.Y_AXIS));
        bodyContent.setBackground(BG_COLOR);
        bodyContent.setBorder(new EmptyBorder(0, 20, 20, 20));

        bodyContent.add(createBanner("MAIN MENU", BANNER_MAIN_COLOR, 34));
        bodyContent.add(Box.createVerticalStrut(10));
        bodyContent.add(createBanner("¡¡¡YOU SHOULD BUY THESE PRODUCTS!!!", BANNER_SUB_COLOR, 25));
        bodyContent.add(Box.createVerticalStrut(15));

        recommendedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        recommendedPanel.setBackground(BANNER_MAIN_COLOR); 
        recommendedPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        
        // Los productos se añaden dinámicamente mediante actualizarRecomendados()

        JPanel recWrapper = new JPanel(new BorderLayout());
        recWrapper.setBackground(BANNER_MAIN_COLOR);
        recWrapper.add(recommendedPanel, BorderLayout.CENTER);

        JScrollPane scrollProducts = new JScrollPane(recWrapper);
        scrollProducts.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollProducts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollProducts.setBorder(null);
        scrollProducts.setPreferredSize(new Dimension(1000, 240));
        
        bodyContent.add(scrollProducts);
        bodyContent.add(Box.createVerticalStrut(20));

        bodyContent.add(createBanner("TYPE OF PRODUCTS", BANNER_SUB_COLOR, 25));
        bodyContent.add(Box.createVerticalStrut(15));

        categoriesPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        categoriesPanel.setOpaque(false);
        
        categoriesPanel.add(createCategoryCard("BOARD GAMES", "BOARD_GAMES", "src/assets/juegodemesa.png"));
        categoriesPanel.add(createCategoryCard("COMICS", "COMICS", "src/assets/comic.png"));
        categoriesPanel.add(createCategoryCard("FIGURES", "FIGURES", "src/assets/figuraGoku.png"));

        bodyContent.add(categoriesPanel);

        JScrollPane mainScroll = new JScrollPane(bodyContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE CREACIÓN DE UI
    // ==========================================

    private JPanel createBanner(String text, Color bgColor, int fontSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Comic Sans MS", Font.BOLD, fontSize));
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProductCard(LineaProductoVenta prod) {
        String name = prod.getNombre();
        String price = String.format("%.2f €", prod.getPrecio());
        String productId = String.valueOf(prod.getID());

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_COLOR);
        card.setPreferredSize(new Dimension(180, 230));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerProd = new JPanel(new BorderLayout(5, 0));
        headerProd.setOpaque(false);
        
        JLabel lblName = new JLabel(name, SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblName.setForeground(Color.DARK_GRAY);
        lblName.setBorder(new EmptyBorder(0, 0, 5, 0));
        headerProd.add(lblName, BorderLayout.CENTER);
        

        JLabel imagePlaceholder = new JLabel();
        imagePlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imagePlaceholder.setBackground(Color.WHITE);
        imagePlaceholder.setOpaque(true);
        try {
            if (prod.getFoto() != null && prod.getFoto().getPath() != null) {
                ImageIcon iconoOriginal = new ImageIcon(prod.getFoto().getPath()); 
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imagePlaceholder.setIcon(new ImageIcon(imgEscalada));
            } else {
                imagePlaceholder.setText("NO IMAGE");
            }
        } catch (Exception e) {
            imagePlaceholder.setText("NO IMAGE");
        }

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblPrice = new JLabel(price, SwingConstants.CENTER);
        lblPrice.setOpaque(true);
        lblPrice.setBackground(new Color(0, 204, 204));
        lblPrice.setBorder(new LineBorder(Color.BLACK, 1));
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
            btnDesc.setMargin(new Insets(2, 4, 2, 4));
            btnDesc.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDesc.setFocusPainted(false);
            btnDesc.setActionCommand("DESCINFO_" + productId);
            buyNowButtons.add(btnDesc);
            pricePanel.add(btnDesc);
        }
        
        JButton btnBuy = new JButton("BUY NOW") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
            }
        };
        btnBuy.setContentAreaFilled(false);
        btnBuy.setBackground(BUY_NOW_COLOR);
        btnBuy.setForeground(Color.RED);
        btnBuy.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
        btnBuy.setBorder(new EmptyBorder(5, 10, 5, 10));
        btnBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuy.setActionCommand("ADD_" + productId);
        
        buyNowButtons.add(btnBuy);

        JButton btnInfo = new JButton("INFO") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                super.paintComponent(g);
            }
        };
        btnInfo.setContentAreaFilled(false);
        btnInfo.setBackground(new Color(74, 118, 201));
        btnInfo.setForeground(Color.WHITE);
        btnInfo.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnInfo.setBorder(new EmptyBorder(5, 10, 5, 10));
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.setActionCommand("INFO_" + productId);

        buyNowButtons.add(btnInfo);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnBuy);
        buttonsPanel.add(btnInfo);

        bottom.add(pricePanel, BorderLayout.CENTER);
        bottom.add(buttonsPanel, BorderLayout.EAST);

        card.add(headerProd, BorderLayout.NORTH);
        card.add(imagePlaceholder, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCategoryCard(String title, String actionCommand, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(136, 212, 204)); 
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 2),
                new EmptyBorder(10, 10, 10, 10)));
        card.setPreferredSize(new Dimension(300, 200));

        JButton btnCat = new JButton(title);
        btnCat.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnCat.setBackground(Color.WHITE);
        btnCat.setFocusPainted(false);
        btnCat.setActionCommand(actionCommand);
        btnCat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        categoryButtons.add(btnCat);

        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(Color.WHITE);
        try {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Image img = javax.imageio.ImageIO.read(imgFile);
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(180, 140, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(scaledImg));
                } else {
                    imgLabel.setText("NO IMAGE");
                }
            } else {
                imgLabel.setText("NO IMAGE");
            }
        } catch (Exception e) {
            imgLabel.setText("NO IMAGE");
        }

        card.add(btnCat, BorderLayout.NORTH);
        card.add(imgLabel, BorderLayout.CENTER);

        return card;
    }

    // ==========================================
    // MÉTODOS PARA REGISTRAR LISTENERS
    // ==========================================
    
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }

    public void addBuyNowListener(ActionListener l) {
        this.buyNowListener = l;
        for (JButton btn : buyNowButtons) {
            btn.addActionListener(l);
        }
    }

    public void actualizarRecomendados(Set<LineaProductoVenta> recomendados) {
        recommendedPanel.removeAll();
        buyNowButtons.clear();

        if (recomendados == null || recomendados.isEmpty()) {
            JLabel lbl = new JLabel("No recommendations available right now.", SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
            lbl.setForeground(Color.WHITE);
            recommendedPanel.add(lbl);
        } else {
            for (LineaProductoVenta p : recomendados) {
                JPanel card = createProductCard(p);
                recommendedPanel.add(card);
            }
        }

        if (buyNowListener != null) {
            for (JButton btn : buyNowButtons) {
                btn.addActionListener(buyNowListener);
            }
        }

        recommendedPanel.revalidate();
        recommendedPanel.repaint();
    }

    public void addCategoryListener(ActionListener l) {
        for (JButton btn : categoryButtons) {
            btn.addActionListener(l);
        }
    }
    
}
