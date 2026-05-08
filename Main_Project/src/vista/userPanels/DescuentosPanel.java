package vista.userPanels;

import modelo.producto.LineaProductoVenta;
import modelo.descuento.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class DescuentosPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 
    private final Color CARD_BG = new Color(245, 247, 250);

    private HeaderPanel headerPanel;
    private ColumnaDescuentos colIndividuales;
    private ColumnaDescuentos colPacks;

    public DescuentosPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // --- Header ---
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuEmpleado();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout(0, 20));
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.setBorder(new EmptyBorder(20, 40, 40, 40)); // Padding generoso

        // --- Título Principal ---
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_MAIN_COLOR);
        bannerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        JLabel lblTitle = new JLabel("MANAGE DISCOUNTS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);
        contentWrapper.add(bannerPanel, BorderLayout.NORTH);

        // --- Cuerpo (Dos Columnas) ---
        JPanel columnsContainer = new JPanel(new GridLayout(1, 2, 40, 0)); // 40px de separación
        columnsContainer.setBackground(BG_COLOR);

        colIndividuales = new ColumnaDescuentos("INDIVIDUAL PRODUCTS", "INDIVIDUAL");
        colPacks = new ColumnaDescuentos("PACKS", "PACKS");

        columnsContainer.add(colIndividuales);
        columnsContainer.add(colPacks);

        contentWrapper.add(columnsContainer, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public ColumnaDescuentos getColIndividuales() { return colIndividuales; }
    public ColumnaDescuentos getColPacks() { return colPacks; }

    // =========================================================================
    // CLASE INTERNA: COLUMNA DE DESCUENTOS
    // =========================================================================
    public class ColumnaDescuentos extends JPanel {
        private static final long serialVersionUID = 1L;
        private JTextField txtSearch;
        private JButton btnSearch;
        private JPanel gridProductos;
        private String commandPrefix;

        public ColumnaDescuentos(String title, String commandPrefix) {
            this.commandPrefix = commandPrefix;
            setLayout(new BorderLayout(0, 10));
            setBackground(BG_COLOR);
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BANNER_MAIN_COLOR, 3, true),
                    new EmptyBorder(15, 15, 15, 15)
            ));

            // Cabecera
            JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblTitle.setOpaque(true);
            lblTitle.setBackground(BANNER_MAIN_COLOR);
            lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));

            // Buscador
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

            // Grid de Productos
            gridProductos = new JPanel();
            gridProductos.setLayout(new BoxLayout(gridProductos, BoxLayout.Y_AXIS));
            gridProductos.setBackground(BG_COLOR);
            gridProductos.setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel gridWrapper = new JPanel(new BorderLayout());
            gridWrapper.setBackground(BG_COLOR);
            gridWrapper.add(gridProductos, BorderLayout.NORTH);
            
            JScrollPane scroll = new JScrollPane(gridWrapper);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            add(scroll, BorderLayout.CENTER);
        }

        public void actualizarProductos(List<LineaProductoVenta> productos, ActionListener actionCtrl) {
            gridProductos.removeAll();
            if (productos.isEmpty()) {
                JLabel vacio = new JLabel("No active discounts found.");
                vacio.setForeground(Color.WHITE);
                vacio.setFont(new Font("SansSerif", Font.BOLD, 14));
                gridProductos.add(vacio);
            } else {
                for (LineaProductoVenta p : productos) {
                    gridProductos.add(crearTarjetaDescuento(p, actionCtrl));
                    gridProductos.add(Box.createVerticalStrut(15));
                }
            }
            gridProductos.revalidate();
            gridProductos.repaint();
        }

        public void addSearchListener(ActionListener l) {
            btnSearch.addActionListener(l);
            txtSearch.addActionListener(e -> btnSearch.doClick());
        }
        
        public String getSearchText() { 
            String t = txtSearch.getText().trim();
            return t.equals("Search...") ? "" : t;
        }

        private JPanel crearTarjetaDescuento(LineaProductoVenta prod, ActionListener actionCtrl) {
            JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
            tarjeta.setBackground(CARD_BG);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 2, true),
                    new EmptyBorder(10, 10, 10, 10)
            ));
            tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            tarjeta.setPreferredSize(new Dimension(0, 120));

            // 1. Imagen (Placeholder 100x100)
            JPanel imgPanel = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (prod.getFoto() == null) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.setStroke(new BasicStroke(2));
                        g2.drawLine(0, 0, getWidth(), getHeight());
                        g2.drawLine(getWidth(), 0, 0, getHeight());
                    }
                }
            };
            imgPanel.setBackground(Color.WHITE);
            imgPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            imgPanel.setPreferredSize(new Dimension(100, 100));
            if (prod.getFoto() != null && prod.getFoto().exists()) {
                ImageIcon icon = new ImageIcon(prod.getFoto().getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imgPanel.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
            }
            tarjeta.add(imgPanel, BorderLayout.WEST);

            // 2. Información
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            JLabel lblNombre = new JLabel(prod.getNombre().toUpperCase());
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblNombre.setForeground(BANNER_MAIN_COLOR);
            
            String catStr = prod.getCategorias().isEmpty() ? "" : "(" + prod.getCategorias().iterator().next().getNombre() + ")";
            JLabel lblCat = new JLabel(catStr);
            lblCat.setFont(new Font("SansSerif", Font.ITALIC, 12));
            lblCat.setForeground(Color.DARK_GRAY);
            
            JLabel lblPrecio = new JLabel(" Price: " + String.format("%.2f €", prod.getPrecio()) + " ");
            lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblPrecio.setOpaque(true);
            lblPrecio.setBackground(new Color(245, 247, 250));
            lblPrecio.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            
            JButton btnInfo = new JButton("VIEW INFO");
            btnInfo.setFont(new Font("SansSerif", Font.BOLD, 11));
            btnInfo.setBackground(new Color(74, 144, 210));
            btnInfo.setForeground(Color.WHITE);
            btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnInfo.setActionCommand("INFO_" + prod.getID());
            btnInfo.addActionListener(actionCtrl);

            infoPanel.add(lblNombre);
            infoPanel.add(lblCat);
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(lblPrecio);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(btnInfo);
            
            tarjeta.add(infoPanel, BorderLayout.CENTER);

            // 3. Círculo de Descuento
            Descuento d = prod.getDescuento();
            if (d == null) {
                for (modelo.categoria.Categoria c : prod.getCategorias()) {
                    if (c.getDescuento() != null && !c.getDescuento().haCaducado()) {
                        d = c.getDescuento();
                        break;
                    }
                }
            }
            
            JButton btnAdd = new JButton("ADD");
            btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnAdd.setBackground(new Color(46, 204, 113));
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setFocusPainted(false);
            btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAdd.setActionCommand("ADD_" + prod.getID());
            btnAdd.addActionListener(actionCtrl);
             
            if (d != null) {
                btnAdd.setText("REPLACE");
                btnAdd.setBackground(new Color(243, 156, 18));
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