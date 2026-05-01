package vista.userWindows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.stream.Collectors;
import modelo.aplicacion.Catalogo;
import modelo.producto.Comic;
import modelo.producto.Figura;
import modelo.producto.LineaProductoVenta;

/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class FiltrosDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    // --- Colores de la paleta ---
    private final Color BG_COLOR = new Color(162, 187, 210);      // Azul claro
    private final Color HEADER_COLOR = new Color(92, 117, 181);   // Azul oscuro/morado
    private final Color SECTION_BG = new Color(114, 158, 206);    // Azul medio

    // --- Componentes Interactivos (Izquierda) ---
    private JCheckBox chkSoloDisponibles;
    private JCheckBox chkBoardGames;
    private JCheckBox chkComics;
    private JCheckBox chkFigures;

    // --- Componentes para Layout Dinámico ---
    private JPanel rightPanel;
    private CardLayout cardLayout;
    private JTextField txtPrecioMin;
    private JTextField txtPrecioMax;
    private JComboBox<String> cbCalifications;

    public FiltrosDialog(JFrame parent) {
        super(parent, "Interchange No Category Filters", true); // true = Modal
        setSize(800, 750);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setBackground(BG_COLOR);
        mainContent.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. COLUMNA IZQUIERDA (Filtros Generales)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BG_COLOR);

        JLabel lblGeneral = createMainHeader("GENERAL FILTERS");
        leftPanel.add(lblGeneral);
        leftPanel.add(Box.createVerticalStrut(15));

        chkSoloDisponibles = new JCheckBox("Show only available products");
        chkSoloDisponibles.setOpaque(false);
        leftPanel.add(chkSoloDisponibles);
        leftPanel.add(Box.createVerticalStrut(15));

        // Secciones Generales
     // Secciones Generales con TOPES de tamaño para evitar espacios en blanco
        JPanel pnlCalificaciones = createSection("CALIFICATIONS", createCalificationsContent());
        // Integer.MAX_VALUE mantiene el ancho intacto. 55 es la altura (bájalo si quieres menos espacio)
        pnlCalificaciones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        leftPanel.add(pnlCalificaciones);
        leftPanel.add(Box.createVerticalStrut(10));
        
        JPanel pnlPrecios = createSection("PRICES", createPricesContent());
        // Integer.MAX_VALUE mantiene el ancho intacto. 85 es la altura para encuadrar las dos filas
        pnlPrecios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
        leftPanel.add(pnlPrecios);
        leftPanel.add(Box.createVerticalStrut(10));
        
        // Categorías (agrupadas para que solo se seleccione una)
        JPanel catContent = createCategoryContent();
        leftPanel.add(createSection("CATEGORY", catContent));
        leftPanel.add(Box.createVerticalStrut(10));
        
        leftPanel.add(createSection("DISCOUNTS", createDiscountsContent()));

        // 2. COLUMNA DERECHA (Filtros Específicos con CardLayout)
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(BG_COLOR);

        // Creamos un contenedor vertical para el header y el panel derecho
        JPanel headerRightPanel = new JPanel();
        headerRightPanel.setLayout(new BoxLayout(headerRightPanel, BoxLayout.Y_AXIS));
        headerRightPanel.setBackground(BG_COLOR);
        headerRightPanel.add(createMainHeader("CATEGORY FILTERS"));
        headerRightPanel.add(Box.createVerticalStrut(12));
        // Encapsulamos rightPanel en un wrapper para que no pegue a los bordes
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.add(rightPanel, BorderLayout.CENTER);
        headerRightPanel.add(rightWrapper);

        // Añadir las "cartas" al panel derecho
        rightPanel.add(createNoCategoryCard(), "NO_CATEGORY");
        rightPanel.add(createComicsCard(), "COMICS");
        rightPanel.add(createFiguresCard(), "FIGURES");
        rightPanel.add(createBoardGamesCard(), "BOARD_GAMES");

        // Mostrar por defecto la carta sin categoría seleccionada
        cardLayout.show(rightPanel, "NO_CATEGORY");

        mainContent.add(leftPanel);
        mainContent.add(headerRightPanel);

        // Botón aplicar abajo
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BG_COLOR);
        
        JButton btnReset = new JButton("Reset Filters");
        btnReset.addActionListener(e -> resetFiltros());
        bottomPanel.add(btnReset);

        JButton btnAplicar = new JButton("Apply Filters");
        btnAplicar.addActionListener(e -> dispose()); // Cierra la ventana
        bottomPanel.add(btnAplicar);

        setLayout(new BorderLayout());
        add(mainContent, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ==========================================
    // CREADORES DE CARTAS DINÁMICAS (DERECHA)
    // ==========================================

    private JPanel createNoCategoryCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(200, 180, 210)); // Color lila del mockup
        p.setBorder(new LineBorder(Color.DARK_GRAY));
        p.setPreferredSize(new Dimension(300, 50));
        JLabel lbl = new JLabel("NO CATEGORY SELECTED", SwingConstants.CENTER);
        lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        p.add(lbl, BorderLayout.CENTER);
        
        // Lo envolvemos para que no ocupe todo el espacio
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(BG_COLOR);
        wrapper.add(p);
        return wrapper;
    }

    private JPanel pnlAutores;
    private JPanel pnlEditoriales;
    private JPanel pnlMarcas;
    private JPanel pnlMateriales;

    // ... inside createComicsCard ...
    private JPanel createComicsCard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_COLOR);
        p.add(createSection("EXTENSION", createCheckList(new String[]{"Single Issue (<48 pages)", "Standard Volume (48-150)", "Trade Paperback (150-300)", "Omnibus (>300)"})));
        p.add(Box.createVerticalStrut(10));
        pnlEditoriales = createSearchableCheckList(getEditoriales());
        p.add(createSection("PUBLISHING HOUSE", pnlEditoriales));
        p.add(Box.createVerticalStrut(10));
        pnlAutores = createSearchableCheckList(getAutores());
        p.add(createSection("AUTHOR", pnlAutores));
        p.add(Box.createVerticalStrut(10));
        p.add(createSection("AGE", createCheckList(new String[]{"Golden Age (1938-1956)", "Silver Age (1956-1970)", "Bronze Age", "Modern Age"})));
        return p;
    }

    private JPanel createFiguresCard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_COLOR);
        p.add(createSection("SIZE", createCheckList(new String[]{"Mini / Micro (< 5 cm)", "Small / Handheld (5 - 15 cm)", "Medium (15 - 40 cm)", "Large (> 40 cm)"})));
        p.add(Box.createVerticalStrut(10));
        pnlMateriales = createSearchableCheckList(getMateriales());
        p.add(createSection("MATERIAL", pnlMateriales));
        p.add(Box.createVerticalStrut(10));
        pnlMarcas = createSearchableCheckList(getMarcas());
        p.add(createSection("BRAND", pnlMarcas));
        return p;
    }

    private java.util.List<String> getSelectedItems(JPanel searchableCheckListPanel) {
        java.util.List<String> selected = new java.util.ArrayList<>();
        if (searchableCheckListPanel == null) return selected;
        // El JPanel devuelto por createSearchableCheckList es BorderLayout con JTextField al norte y JScrollPane al centro
        for (Component c : searchableCheckListPanel.getComponents()) {
            if (c instanceof JPanel) { // The wrapper
                for (Component innerC : ((JPanel) c).getComponents()) {
                    if (innerC instanceof JScrollPane) {
                        JViewport viewport = ((JScrollPane) innerC).getViewport();
                        Component view = viewport.getView();
                        if (view instanceof JPanel) {
                            for (Component chk : ((JPanel) view).getComponents()) {
                                if (chk instanceof JCheckBox && ((JCheckBox) chk).isSelected()) {
                                    selected.add(((JCheckBox) chk).getText());
                                }
                            }
                        }
                    }
                }
            }
        }
        return selected;
    }

    public boolean cumpleFiltrosAvanzados(LineaProductoVenta p) {
        // Filtro de solo disponibles
        if (chkSoloDisponibles.isSelected() && p.getStock() <= 0) return false;

        // Categorías generales
        if (chkComics.isSelected() && !(p instanceof Comic)) return false;
        if (chkFigures.isSelected() && !(p instanceof Figura)) return false;
        if (chkBoardGames.isSelected() && !(p instanceof modelo.producto.JuegoDeMesa)) return false;

        // Filtro de precio
        if (txtPrecioMin != null && !txtPrecioMin.getText().trim().isEmpty()) {
            try {
                double min = Double.parseDouble(txtPrecioMin.getText().trim());
                if (p.getPrecio() < min) return false;
            } catch (NumberFormatException e) {
                // ignorar
            }
        }
        if (txtPrecioMax != null && !txtPrecioMax.getText().trim().isEmpty()) {
            try {
                double max = Double.parseDouble(txtPrecioMax.getText().trim());
                if (p.getPrecio() > max) return false;
            } catch (NumberFormatException e) {
                // ignorar
            }
        }

        // Filtro de Calificaciones (Reseñas)
        if (cbCalifications != null && cbCalifications.getSelectedIndex() > 0) {
            int minRating = cbCalifications.getSelectedIndex(); // "1 Star" -> index 1
            if (p.obtenerPuntuacionMedia() < minRating) {
                return false;
            }
        }

        if (p instanceof Comic) {
            Comic c = (Comic) p;
            java.util.List<String> selAutores = getSelectedItems(pnlAutores);
            if (!selAutores.isEmpty() && !selAutores.contains(c.getAutor())) return false;
            
            java.util.List<String> selEditoriales = getSelectedItems(pnlEditoriales);
            if (!selEditoriales.isEmpty() && !selEditoriales.contains(c.getEditorial())) return false;
        } else if (p instanceof Figura) {
            Figura f = (Figura) p;
            java.util.List<String> selMarcas = getSelectedItems(pnlMarcas);
            if (!selMarcas.isEmpty() && !selMarcas.contains(f.getMarca())) return false;
            
            java.util.List<String> selMateriales = getSelectedItems(pnlMateriales);
            if (!selMateriales.isEmpty() && !selMateriales.contains(f.getMaterial())) return false;
        }
        return true;
    }

    public void resetFiltros() {
        resetAllComponents(this.getContentPane());
        cambiarVistaDerecha("NO_CATEGORY");
        chkComics.setSelected(false);
        chkFigures.setSelected(false);
        chkBoardGames.setSelected(false);
        chkSoloDisponibles.setSelected(false);
    }

    private void resetAllComponents(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JCheckBox) {
                ((JCheckBox) c).setSelected(false);
            } else if (c instanceof JComboBox) {
                ((JComboBox<?>) c).setSelectedIndex(0);
            } else if (c instanceof JTextField) {
                if (c == txtPrecioMin) {
                    ((JTextField) c).setText("0.00");
                } else if (c == txtPrecioMax) {
                    ((JTextField) c).setText("");
                } else {
                    if (((JTextField) c).getText().isEmpty() || !((JTextField) c).getText().equals("Search...")) {
                        ((JTextField) c).setText("Search...");
                        c.setForeground(Color.GRAY);
                    }
                }
            } else if (c instanceof Container) {
                resetAllComponents((Container) c);
            }
        }
    }

    private String[] getAutores() {
        return Catalogo.getInstancia().getProductosNuevos().stream()
                .filter(p -> p instanceof Comic)
                .map(p -> ((Comic) p).getAutor())
                .filter(a -> a != null && !a.trim().isEmpty())
                .distinct()
                .sorted()
                .toArray(String[]::new);
    }

    private String[] getEditoriales() {
        return Catalogo.getInstancia().getProductosNuevos().stream()
                .filter(p -> p instanceof Comic)
                .map(p -> ((Comic) p).getEditorial())
                .filter(e -> e != null && !e.trim().isEmpty())
                .distinct()
                .sorted()
                .toArray(String[]::new);
    }

    private String[] getMarcas() {
        return Catalogo.getInstancia().getProductosNuevos().stream()
                .filter(p -> p instanceof Figura)
                .map(p -> ((Figura) p).getMarca())
                .filter(m -> m != null && !m.trim().isEmpty())
                .distinct()
                .sorted()
                .toArray(String[]::new);
    }

    private String[] getMateriales() {
        return Catalogo.getInstancia().getProductosNuevos().stream()
                .filter(p -> p instanceof Figura)
                .map(p -> ((Figura) p).getMaterial())
                .filter(m -> m != null && !m.trim().isEmpty())
                .distinct()
                .sorted()
                .toArray(String[]::new);
    }

    private JPanel createBoardGamesCard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_COLOR);
        p.add(createSection("NUMBER OF PLAYERS", createCheckList(new String[]{"Solo (1 Player)", "Duo (2 Players)", "Small Group (3-4)", "Party (5+)"})));
        p.add(Box.createVerticalStrut(10));
        p.add(createSection("AGE RANGE", createCheckList(new String[]{"Kids (3-7)", "Family (8-12)", "Teen (13-17)", "Adult (18+)"})));
        p.add(Box.createVerticalStrut(10));
        p.add(createSection("GAME TYPE", createCheckList(new String[]{"Strategy", "Party Games", "Card Games", "Cooperative"})));
        p.add(Box.createVerticalStrut(10));
        p.add(createSection("ESTIMATED PLAYTIME", createCheckList(new String[]{"Quick (Under 30 min)", "Standard (30-90 min)", "Long (90+ min)"})));
        return p;
    }

    // ==========================================
    // COMPONENTES REUTILIZABLES
    // ==========================================

    private JLabel createMainHeader(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(HEADER_COLOR);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(6, 10, 6, 10)));
        lbl.setPreferredSize(new Dimension(300, 46));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JPanel createSection(String title, JPanel content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setOpaque(true);
        lblTitle.setBackground(SECTION_BG);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        lblTitle.setBorder(new EmptyBorder(5, 0, 5, 0));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoryContent() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        
        chkBoardGames = new JCheckBox("Board Games");
        chkComics = new JCheckBox("Comics");
        chkFigures = new JCheckBox("Figures");

        // Estilizado ligero para que se integren en el panel blanco
        JCheckBox[] catChecks = new JCheckBox[]{chkBoardGames, chkComics, chkFigures};
        for (JCheckBox c : catChecks) {
            c.setBackground(Color.WHITE);
            c.setFocusPainted(false);
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
            c.setBorder(new EmptyBorder(4,4,4,4));
        }

        p.add(chkBoardGames);
        p.add(chkComics);
        p.add(chkFigures);
        return p;
    }

    private JPanel createCheckList(String[] items) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        for (String item : items) {
            JCheckBox chk = new JCheckBox(item);
            chk.setBackground(Color.WHITE);
            chk.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(chk);
        }
        return wrapInScroll(p);
    }

    private JPanel createSearchableCheckList(String[] items) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        JTextField txtSearch = new JTextField("Search...");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(4,6,4,6)));
        txtSearch.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals("Search...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("Search...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        container.add(txtSearch, BorderLayout.NORTH);
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        for (String item : items) {
            JCheckBox chk = new JCheckBox(item);
            chk.setBackground(Color.WHITE);
            p.add(chk);
        }
        container.add(wrapInScroll(p), BorderLayout.CENTER);
        return container;
    }

    private JPanel wrapInScroll(JPanel content) {
        JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(250, 90));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // Más suave al moverse
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // Mockups para las secciones estáticas de la izquierda
    private JPanel createCalificationsContent() {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(Color.WHITE);
        cbCalifications = new JComboBox<>(new String[]{"No preference", "1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"});
        p.add(cbCalifications);
        return p;
    }

    private JPanel createPricesContent() {
        JPanel p = new JPanel(new GridLayout(3, 1));
        p.setBackground(Color.WHITE);
        p.add(new JComboBox<>(new String[]{"No preference"}));
        
        JPanel range = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        range.setBackground(Color.WHITE);
        txtPrecioMin = new JTextField("0.00", 5);
        range.add(txtPrecioMin);
        range.add(new JLabel("-"));
        txtPrecioMax = new JTextField("", 5);
        range.add(txtPrecioMax);
        p.add(range);
        return p;
    }

    private JPanel createDiscountsContent() {
        return createCheckList(new String[]{"Quantity Discount", "Price Reduction", "Spending Volume", "Threshold Gift"});
    }

    // ==========================================
    // GETTERS PARA EL CONTROLADOR
    // ==========================================
    public JCheckBox getChkBoardGames() { return chkBoardGames; }
    public JCheckBox getChkComics() { return chkComics; }
    public JCheckBox getChkFigures() { return chkFigures; }
    
    // Método para cambiar la vista derecha
    public void cambiarVistaDerecha(String vistaNombre) {
        cardLayout.show(rightPanel, vistaNombre);
    }
}
