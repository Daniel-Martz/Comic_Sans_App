package vista.clienteWindows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * The Class AddCategoryWindow.
 */
public class AddCategoryWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bg color. */
    // --- Colores de la paleta ---
    private final Color BG_COLOR = new Color(162, 187, 210);       // Azul claro
    
    /** The header color. */
    private final Color HEADER_COLOR = new Color(92, 117, 181);    // Azul oscuro/morado
    
    /** The row bg color. */
    private final Color ROW_BG_COLOR = new Color(114, 158, 206);   // Azul intermedio
    
    /** The btn green. */
    private final Color BTN_GREEN = new Color(46, 204, 113);       // Verde brillante

    /** The txt name. */
    private JTextField txtName;
    
    /** The btn confirm. */
    private JButton btnConfirm;

    /**
     * Instantiates a new adds the category window.
     *
     * @param parent the parent
     */
    public AddCategoryWindow(JDialog parent) {
        super(parent, "Create New Category", true); // Modal over ManageCategoriesWindow
        setSize(300, 240);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JLabel lblMainHeader = new JLabel("New Category", SwingConstants.CENTER);
        lblMainHeader.setOpaque(true);
        lblMainHeader.setBackground(HEADER_COLOR);
        lblMainHeader.setForeground(Color.WHITE);
        lblMainHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        lblMainHeader.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(lblMainHeader, BorderLayout.NORTH);

        // Center
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);

        JLabel lblName = new JLabel("Category Name:");
        lblName.setForeground(Color.DARK_GRAY);
        lblName.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        
        txtName = new JTextField();
        txtName.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

        centerPanel.add(lblName);
        centerPanel.add(txtName);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        btnConfirm = new JButton("Confirm");
        btnConfirm.setBackground(BTN_GREEN);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        bottomPanel.add(btnConfirm);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * Gets the category name.
     *
     * @return the category name
     */
    public String getCategoryName() {
        return txtName.getText();
    }

    /**
     * Adds the confirm listener.
     *
     * @param l the l
     */
    public void addConfirmListener(ActionListener l) {
        btnConfirm.addActionListener(l);
    }
}