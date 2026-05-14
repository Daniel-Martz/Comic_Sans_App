package vista.empleadoPanel;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ModifyProductsPanel.
 */
public class ModifyProductsPanel extends JPanel {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);      
    
    /** The banner main color. */
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    /** The grid comics. */
    private JPanel gridComics;
    
    /** The grid board games. */
    private JPanel gridBoardGames;
    
    /** The grid figures. */
    private JPanel gridFigures;
    
    /** The btn back. */
    private JButton btnBack;
    
    /** The btn back manage. */
    private JButton btnBackManage;
    
    /** The txt search. */
    private JTextField txtSearch;
    
    /** The modify buttons. */
    private java.util.List<JButton> modifyButtons = new java.util.ArrayList<>();

    /**
     * Instantiates a new modify products panel.
     */
    public ModifyProductsPanel() {
        initLayout();
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Header custom (Products, Search)
        JPanel customHeader = new JPanel(new BorderLayout());
        customHeader.setBackground(BANNER_MAIN_COLOR);
        customHeader.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        btnBack = new JButton("HOME 🏠"); 
        btnBack.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeader.setOpaque(false);
        leftHeader.add(btnBack);
        // Botón 'BACK' adicional junto a HOME para volver a ManageProducts
        btnBackManage = new JButton("BACK");
        btnBackManage.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        btnBackManage.setForeground(Color.WHITE);
        btnBackManage.setContentAreaFilled(false);
        btnBackManage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftHeader.add(btnBackManage);

        JLabel lblTitle = new JLabel("PRODUCTS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JPanel searchBox = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                super.paintComponent(g);
                g2.dispose();
            }
        };
        searchBox.setOpaque(false);
        searchBox.setBorder(new EmptyBorder(6, 12, 6, 12));
        
        JButton btnSearchIcon = new JButton("🔍");
        btnSearchIcon.setContentAreaFilled(false);
        btnSearchIcon.setBorderPainted(false);
        btnSearchIcon.setFocusPainted(false);
        btnSearchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        txtSearch = new JTextField(15);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        txtSearch.setBorder(null);
        txtSearch.setText("SEARCH");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals("SEARCH")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("SEARCH");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        searchBox.add(btnSearchIcon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        customHeader.add(leftHeader, BorderLayout.WEST);
        customHeader.add(lblTitle, BorderLayout.CENTER);
        customHeader.add(searchBox, BorderLayout.EAST);

        add(customHeader, BorderLayout.NORTH);

        // Body
        JPanel bodyContent = new JPanel(new GridLayout(1, 3, 20, 0));
        bodyContent.setBackground(BG_COLOR);
        bodyContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create layout for categories
        JPanel colComics = new JPanel(new BorderLayout(0, 10)); colComics.setOpaque(false);
        JPanel colBoardGames = new JPanel(new BorderLayout(0, 10)); colBoardGames.setOpaque(false);
        JPanel colFigures = new JPanel(new BorderLayout(0, 10)); colFigures.setOpaque(false);

        colComics.add(createColumnTitle("COMICS"), BorderLayout.NORTH);
        colBoardGames.add(createColumnTitle("BOARD GAMES"), BorderLayout.NORTH);
        colFigures.add(createColumnTitle("FIGURES"), BorderLayout.NORTH);

        gridComics = new JPanel(new GridLayout(0, 1, 0, 15)); gridComics.setOpaque(false);
        gridBoardGames = new JPanel(new GridLayout(0, 1, 0, 15)); gridBoardGames.setOpaque(false);
        gridFigures = new JPanel(new GridLayout(0, 1, 0, 15)); gridFigures.setOpaque(false);

        colComics.add(createScroll(gridComics), BorderLayout.CENTER);
        colBoardGames.add(createScroll(gridBoardGames), BorderLayout.CENTER);
        colFigures.add(createScroll(gridFigures), BorderLayout.CENTER);

        bodyContent.add(colComics);
        bodyContent.add(colBoardGames);
        bodyContent.add(colFigures);

        add(bodyContent, BorderLayout.CENTER);
    }

    /**
     * Creates the column title.
     *
     * @param title the title
     * @return the j label
     */
    private JLabel createColumnTitle(String title) {
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setBackground(Color.WHITE);
        lblTitle.setOpaque(true);
        lblTitle.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1),
                new EmptyBorder(5, 0, 5, 0)
        ));
        return lblTitle;
    }

    /**
     * Creates the scroll.
     *
     * @param gridPanel the grid panel
     * @return the j scroll pane
     */
    private JScrollPane createScroll(JPanel gridPanel) {
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    /**
     * Actualizar productos.
     *
     * @param comics the comics
     * @param boardGames the board games
     * @param figures the figures
     * @param modifyListener the modify listener
     */
    public void actualizarProductos(List<LineaProductoVenta> comics, List<LineaProductoVenta> boardGames, List<LineaProductoVenta> figures, ActionListener modifyListener) {
        gridComics.removeAll();
        gridBoardGames.removeAll();
        gridFigures.removeAll();
        modifyButtons.clear();

        llenarGrid(gridComics, comics, modifyListener);
        llenarGrid(gridBoardGames, boardGames, modifyListener);
        llenarGrid(gridFigures, figures, modifyListener);

        gridComics.revalidate();
        gridComics.repaint();
        gridBoardGames.revalidate();
        gridBoardGames.repaint();
        gridFigures.revalidate();
        gridFigures.repaint();
    }

    /**
     * Llenar grid.
     *
     * @param grid the grid
     * @param productos the productos
     * @param modifyListener the modify listener
     */
    private void llenarGrid(JPanel grid, List<LineaProductoVenta> productos, ActionListener modifyListener) {
        if (productos != null) {
            for (LineaProductoVenta p : productos) {
                grid.add(createCard(p, modifyListener));
            }
        }
    }

    /**
     * Creates the card.
     *
     * @param p the p
     * @param modifyListener the modify listener
     * @return the j panel
     */
    private JPanel createCard(LineaProductoVenta p, ActionListener modifyListener) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        card.setPreferredSize(new Dimension(200, 250));
        
        JLabel lblName = new JLabel("<html><center>" + p.getNombre() + "</center></html>", SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setBorder(new EmptyBorder(5, 5, 5, 5));
        card.add(lblName, BorderLayout.NORTH);

        JLabel imgPlaceholder = new JLabel();
        imgPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imgPlaceholder.setBackground(Color.WHITE);
        imgPlaceholder.setOpaque(true);
        try {
            if (p.getFoto() != null && p.getFoto().getPath() != null) {
                ImageIcon iconoOriginal = new ImageIcon(p.getFoto().getPath()); 
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imgPlaceholder.setIcon(new ImageIcon(imgEscalada));
            } else {
                imgPlaceholder.setText("NO IMAGE");
            }
        } catch (Exception e) {
            imgPlaceholder.setText("X");
            imgPlaceholder.setFont(new Font("SansSerif", Font.BOLD, 48));
            imgPlaceholder.setForeground(Color.LIGHT_GRAY);
        }
        card.add(imgPlaceholder, BorderLayout.CENTER);

        JButton btnModify = new JButton("Modify");
        btnModify.setBackground(new Color(74, 144, 226));
        btnModify.setForeground(Color.WHITE);
        btnModify.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnModify.setFocusPainted(false);
        btnModify.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModify.setActionCommand("MODIFY_" + p.getID());
        if (modifyListener != null) {
            btnModify.addActionListener(modifyListener);
            modifyButtons.add(btnModify);
        }
        
        card.add(btnModify, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Gets the btn back.
     *
     * @return the btn back
     */
    public JButton getBtnBack() {
        return btnBack;
    }

    /**
     * Gets the btn back manage.
     *
     * @return the btn back manage
     */
    public JButton getBtnBackManage() {
        return btnBackManage;
    }
    
    /**
     * Gets the search text.
     *
     * @return the search text
     */
    public String getSearchText() {
        String text = txtSearch.getText().trim();
        if (text.equals("SEARCH")) return "";
        return text;
    }
    
    /**
     * Adds the search listener.
     *
     * @param l the l
     */
    public void addSearchListener(ActionListener l) {
        txtSearch.addActionListener(l);
    }
}
