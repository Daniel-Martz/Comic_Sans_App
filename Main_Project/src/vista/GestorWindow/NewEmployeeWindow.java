package vista.GestorWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * The Class NewEmployeeWindow.
 */
public class NewEmployeeWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The txt name. */
    private JTextField txtName;
    
    /** The txt dni. */
    private JTextField txtDni;
    
    /** The chk validaciones. */
    private JCheckBox chkValidaciones;
    
    /** The chk intercambios. */
    private JCheckBox chkIntercambios;
    
    /** The chk pedidos. */
    private JCheckBox chkPedidos;
    
    /** The btn accept. */
    private JButton btnAccept;

    /** The bg color. */
    private final Color BG_COLOR = new Color(245, 247, 250);
    
    /** The btn blue. */
    private final Color BTN_BLUE = new Color(74, 144, 226);

    /**
     * Instantiates a new new employee window.
     *
     * @param parent the parent
     */
    public NewEmployeeWindow(Frame parent) {
        super(parent, "New Employee", true);
        initComponents();
        initLayout();
        setSize(400, 350);
        setLocationRelativeTo(parent);
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        txtName = new JTextField(15);
        txtDni = new JTextField(15);

        chkValidaciones = new JCheckBox("Validations management");
        chkValidaciones.setBackground(BG_COLOR);
        
        chkIntercambios = new JCheckBox("Interchange management");
        chkIntercambios.setBackground(BG_COLOR);
        
        chkPedidos = new JCheckBox("Orders management");
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

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(BG_COLOR);
        namePanel.add(new JLabel("Name:"));
        namePanel.add(txtName);

        JPanel dniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dniPanel.setBackground(BG_COLOR);
        dniPanel.add(new JLabel("DNI:  "));
        dniPanel.add(txtDni);

        chkValidaciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkIntercambios.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkPedidos.setAlignmentX(Component.LEFT_ALIGNMENT);

        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dniPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(namePanel);
        panel.add(dniPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Permissions:"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(chkValidaciones);
        panel.add(Box.createVerticalStrut(5));
        panel.add(chkIntercambios);
        panel.add(Box.createVerticalStrut(5));
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
     * Gets the employee name.
     *
     * @return the employee name
     */
    public String getEmployeeName() {
        return txtName.getText().trim();
    }

    /**
     * Gets the employee dni.
     *
     * @return the employee dni
     */
    public String getEmployeeDni() {
        return txtDni.getText().trim();
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
