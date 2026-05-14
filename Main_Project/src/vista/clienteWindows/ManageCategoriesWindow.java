package vista.clienteWindows;

import modelo.categoria.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageCategoriesWindow.
 */
public class ManageCategoriesWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bg color. */
    // --- Colores de la paleta (Consistentes con FiltrosDialog) ---
    private final Color BG_COLOR = new Color(162, 187, 210);      
    
    /** The header color. */
    private final Color HEADER_COLOR = new Color(92, 117, 181);   
    
    /** The row bg color. */
    private final Color ROW_BG_COLOR = new Color(114, 158, 206);  
    
    /** The btn red. */
    private final Color BTN_RED = new Color(231, 76, 60);         
    
    /** The btn green. */
    private final Color BTN_GREEN = new Color(46, 204, 113);      

    /** The list panel. */
    private JPanel listPanel;
    
    /** The delete listener. */
    // Listeners que inyectará el controlador
    private ActionListener deleteListener;
    
    /** The create listener. */
    private ActionListener createListener;

    /**
     * Instantiates a new manage categories window.
     *
     * @param parent the parent
     */
    public ManageCategoriesWindow(JFrame parent) {
        super(parent, "Manage Categories", true); // Modal
        setSize(400, 500);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_COLOR);
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Banner superior
        JLabel lblHeader = new JLabel("ALL CATEGORIES", SwingConstants.CENTER);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(HEADER_COLOR);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblHeader.setBorder(new EmptyBorder(15, 10, 15, 10));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        listPanel.add(lblHeader);
        listPanel.add(Box.createVerticalStrut(15));

        // JScrollPane envuelve la lista
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    /**
     * Método para popular dinámicamente la lista de categorías.
     *
     * @param categorias the new categorias
     */
    public void setCategorias(List<Categoria> categorias) {
        listPanel.removeAll();
        
        JLabel lblHeader = new JLabel("ALL CATEGORIES", SwingConstants.CENTER);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(HEADER_COLOR);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblHeader.setBorder(new EmptyBorder(15, 10, 15, 10));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        listPanel.add(lblHeader);
        listPanel.add(Box.createVerticalStrut(15));

        if (categorias != null) {
            for (Categoria c : categorias) {
                listPanel.add(createCategoryRow(c.getNombre()));
                listPanel.add(Box.createVerticalStrut(5));
            }
        }

        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(createAddRow());

        listPanel.revalidate();
        listPanel.repaint();
    }

    /**
     * Creates the category row.
     *
     * @param catName the cat name
     * @return the j panel
     */
    private JPanel createCategoryRow(String catName) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(ROW_BG_COLOR);
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1),
                new EmptyBorder(10, 15, 10, 15)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblName = new JLabel(catName);
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        row.add(lblName, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlButtons.setOpaque(false);

        JButton btnDelete = new JButton("X");
        btnDelete.setBackground(BTN_RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setActionCommand("DELETE_" + catName);
        if (deleteListener != null) btnDelete.addActionListener(deleteListener);

        pnlButtons.add(btnDelete);
        row.add(pnlButtons, BorderLayout.EAST);

        return row;
    }

    /**
     * Creates the add row.
     *
     * @return the j panel
     */
    private JPanel createAddRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(ROW_BG_COLOR);
        row.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1),
                new EmptyBorder(10, 15, 10, 15)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lblName = new JLabel("Create New category");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        row.add(lblName, BorderLayout.WEST);

        JButton btnAdd = new JButton("+");
        btnAdd.setBackground(BTN_GREEN);
        btnAdd.setForeground(Color.WHITE);
        if (createListener != null) btnAdd.addActionListener(createListener);
        row.add(btnAdd, BorderLayout.EAST);

        return row;
    }

    /**
     * Adds the delete listener.
     *
     * @param l the l
     */
    public void addDeleteListener(ActionListener l) { this.deleteListener = l; }
    
    /**
     * Adds the create listener.
     *
     * @param l the l
     */
    public void addCreateListener(ActionListener l) { this.createListener = l; }
}