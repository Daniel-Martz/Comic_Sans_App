package vista.GestorWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ModifyPermissionsWindow.
 */
public class ModifyPermissionsWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The chk validaciones. */
    private JCheckBox chkValidaciones;
    
    /** The chk intercambios. */
    private JCheckBox chkIntercambios;
    
    /** The chk pedidos. */
    private JCheckBox chkPedidos;
    
    /** The btn accept. */
    private JButton btnAccept;
    
    /** The user dni. */
    private String userDni;

    /** The bg color. */
    private final Color BG_COLOR = new Color(245, 247, 250);
    
    /** The btn blue. */
    private final Color BTN_BLUE = new Color(74, 144, 226);

    /**
     * Instantiates a new modify permissions window.
     *
     * @param parent the parent
     * @param userDni the user dni
     * @param hasValidaciones the has validaciones
     * @param hasIntercambios the has intercambios
     * @param hasPedidos the has pedidos
     */
    public ModifyPermissionsWindow(Frame parent, String userDni, boolean hasValidaciones, boolean hasIntercambios, boolean hasPedidos) {
        super(parent, "Modify permissions", true);
        this.userDni = userDni;
        initComponents(hasValidaciones, hasIntercambios, hasPedidos);
        initLayout();
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    /**
     * Inits the components.
     *
     * @param hasValidaciones the has validaciones
     * @param hasIntercambios the has intercambios
     * @param hasPedidos the has pedidos
     */
    private void initComponents(boolean hasValidaciones, boolean hasIntercambios, boolean hasPedidos) {
        chkValidaciones = new JCheckBox("Validations management");
        chkValidaciones.setSelected(hasValidaciones);
        chkValidaciones.setBackground(BG_COLOR);
        
        chkIntercambios = new JCheckBox("Interchange management");
        chkIntercambios.setSelected(hasIntercambios);
        chkIntercambios.setBackground(BG_COLOR);
        
        chkPedidos = new JCheckBox("Orders management");
        chkPedidos.setSelected(hasPedidos);
        chkPedidos.setBackground(BG_COLOR);
        
        btnAccept = new JButton("Accept");
        btnAccept.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAccept.setBackground(BTN_BLUE);
        btnAccept.setForeground(Color.WHITE);
        btnAccept.setFocusPainted(false);
        btnAccept.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccept.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        chkValidaciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkIntercambios.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkPedidos.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(chkValidaciones);
        panel.add(Box.createVerticalStrut(10));
        panel.add(chkIntercambios);
        panel.add(Box.createVerticalStrut(10));
        panel.add(chkPedidos);
        panel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(BG_COLOR);
        btnPanel.add(btnAccept);

        panel.add(btnPanel);

        setContentPane(panel);
    }

    /**
     * Adds the accept listener.
     *
     * @param l the l
     */
    public void addAcceptListener(ActionListener l) {
        btnAccept.addActionListener(l);
    }

    /**
     * Gets the user dni.
     *
     * @return the user dni
     */
    public String getUserDni() {
        return userDni;
    }

    /**
     * Checks for validaciones.
     *
     * @return true, if successful
     */
    public boolean hasValidaciones() {
        return chkValidaciones.isSelected();
    }

    /**
     * Checks for intercambios.
     *
     * @return true, if successful
     */
    public boolean hasIntercambios() {
        return chkIntercambios.isSelected();
    }

    /**
     * Checks for pedidos.
     *
     * @return true, if successful
     */
    public boolean hasPedidos() {
        return chkPedidos.isSelected();
    }
}
