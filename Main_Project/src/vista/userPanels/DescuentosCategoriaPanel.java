package vista.userPanels;

import modelo.categoria.Categoria;
import modelo.descuento.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class DescuentosCategoriaPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 
    private final Color CARD_BG = new Color(245, 247, 250);

    private HeaderPanel headerPanel;
    private ColumnaDescuentos colCategorias;
    private JButton btnBack;
 
    public DescuentosCategoriaPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();
        btnBack = headerPanel.addSecondaryTopButton("BACK");
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout(0, 20));
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.setBorder(new EmptyBorder(20, 80, 40, 80)); 

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_MAIN_COLOR);
        bannerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        JLabel lblTitle = new JLabel("MANAGE CATEGORY DISCOUNTS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);
        contentWrapper.add(bannerPanel, BorderLayout.NORTH);

        colCategorias = new ColumnaDescuentos("CATEGORIES", "CAT");

        contentWrapper.add(colCategorias, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public ColumnaDescuentos getColCategorias() { return colCategorias; }
    public JButton getBtnBack() { return btnBack; }

    public class ColumnaDescuentos extends JPanel {
        private static final long serialVersionUID = 1L;
        private JTextField txtSearch;
        private JButton btnSearch;
        private JPanel gridCategorias;
        private String commandPrefix;

        public ColumnaDescuentos(String title, String commandPrefix) {
            this.commandPrefix = commandPrefix;
            setLayout(new BorderLayout(0, 10));
            setBackground(BG_COLOR);
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BANNER_MAIN_COLOR, 3, true),
                    new EmptyBorder(15, 15, 15, 15)
            ));

            JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblTitle.setOpaque(true);
            lblTitle.setBackground(BANNER_MAIN_COLOR);
            lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));

            JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
            searchPanel.setOpaque(false);
            searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            
            txtSearch = new JTextField("Search...");
            txtSearch.setForeground(Color.GRAY);
            txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
            txtSearch.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
            txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtSearch.getText().equals("Search...")) {
                        txtSearch.setText("");
                        txtSearch.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtSearch.getText().isEmpty()) {
                        txtSearch.setText("Search...");
                        txtSearch.setForeground(Color.GRAY);
                    }
                }
            });
            
            btnSearch = new JButton("🔍");
            btnSearch.setActionCommand("SEARCH_" + commandPrefix);
            btnSearch.setBackground(Color.WHITE);
            btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            searchPanel.add(txtSearch, BorderLayout.CENTER);
            searchPanel.add(btnSearch, BorderLayout.WEST);

            JPanel topContainer = new JPanel(new BorderLayout());
            topContainer.setOpaque(false);
            topContainer.add(lblTitle, BorderLayout.NORTH);
            topContainer.add(searchPanel, BorderLayout.SOUTH);
            add(topContainer, BorderLayout.NORTH);

            gridCategorias = new JPanel();
            gridCategorias.setLayout(new BoxLayout(gridCategorias, BoxLayout.Y_AXIS));
            gridCategorias.setBackground(BG_COLOR);
            gridCategorias.setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel gridWrapper = new JPanel(new BorderLayout());
            gridWrapper.setBackground(BG_COLOR);
            gridWrapper.add(gridCategorias, BorderLayout.NORTH);
            
            JScrollPane scroll = new JScrollPane(gridWrapper);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            add(scroll, BorderLayout.CENTER);
        }

        public void actualizarCategorias(List<Categoria> categorias, ActionListener actionCtrl) {
            gridCategorias.removeAll();
            if (categorias.isEmpty()) {
                JLabel vacio = new JLabel("No categories found.");
                vacio.setForeground(Color.WHITE);
                vacio.setFont(new Font("SansSerif", Font.BOLD, 14));
                gridCategorias.add(vacio);
            } else {
                for (Categoria c : categorias) {
                    gridCategorias.add(crearTarjetaDescuento(c, actionCtrl));
                    gridCategorias.add(Box.createVerticalStrut(15));
                }
            }
            gridCategorias.revalidate();
            gridCategorias.repaint();
        }

        public void addSearchListener(ActionListener l) {
            btnSearch.addActionListener(l);
            txtSearch.addActionListener(e -> btnSearch.doClick());
        }
        
        public String getSearchText() { 
            String t = txtSearch.getText().trim();
            return t.equals("Search...") ? "" : t;
        }

        private JPanel crearTarjetaDescuento(Categoria cat, ActionListener actionCtrl) {
            JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
            tarjeta.setBackground(CARD_BG);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 2, true),
                    new EmptyBorder(10, 10, 10, 10)
            ));
            tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            tarjeta.setPreferredSize(new Dimension(0, 100));

            // Información
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            JLabel lblNombre = new JLabel(cat.getNombre().toUpperCase());
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblNombre.setForeground(BANNER_MAIN_COLOR);
            
            int numProds = cat.obtenerProductosCategoria() != null ? cat.obtenerProductosCategoria().size() : 0;
            JLabel lblProds = new JLabel("Products in this category: " + numProds);
            lblProds.setFont(new Font("SansSerif", Font.ITALIC, 14));
            lblProds.setForeground(Color.DARK_GRAY);
            
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(lblNombre);
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(lblProds);
            
            tarjeta.add(infoPanel, BorderLayout.CENTER);

            // Círculo de Descuento
            Descuento d = cat.getDescuento();
            
            JButton btnAdd = new JButton("ADD");
            btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnAdd.setBackground(new Color(46, 204, 113));
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setFocusPainted(false);
            btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAdd.setActionCommand("ADD_" + cat.getNombre());
            btnAdd.addActionListener(actionCtrl);
             
            if (d != null && !d.haCaducado()) {
                btnAdd.setText("REPLACE");
                btnAdd.setBackground(new Color(243, 156, 18));
                
                String descText = "%";
                if (d instanceof Precio) descText = "-" + ((Precio)d).getPorcentajeRebaja() + "%";
                else if (d instanceof RebajaUmbral) descText = "-" + ((RebajaUmbral)d).getPorcentajeRebaja() + "%";
                else if (d instanceof Cantidad) descText = ((Cantidad)d).getNumeroComprados() + "x" + ((Cantidad)d).getNumeroRecibidos();
                else if (d instanceof Regalo) descText = "GIFT";
                
                vista.elements.DiscountBadge badge = new vista.elements.DiscountBadge(descText, new Color(240, 192, 32));
                tarjeta.add(badge, BorderLayout.WEST);
            } else {
                JPanel badgePlaceholder = new JPanel();
                badgePlaceholder.setOpaque(false);
                badgePlaceholder.setPreferredSize(new Dimension(60, 60));
                tarjeta.add(badgePlaceholder, BorderLayout.WEST);
            }

            JPanel btnPanel = new JPanel(new GridBagLayout());
            btnPanel.setOpaque(false);
            btnPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
            btnPanel.add(btnAdd);

            tarjeta.add(btnPanel, BorderLayout.EAST);

            return tarjeta;
        }
    }
}