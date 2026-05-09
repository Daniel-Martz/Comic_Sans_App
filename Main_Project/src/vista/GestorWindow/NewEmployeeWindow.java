package vista.GestorWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class NewEmployeeWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private JTextField txtDni;
    private JCheckBox chkValidaciones;
    private JCheckBox chkIntercambios;
    private JCheckBox chkPedidos;
    private JButton btnAccept;

    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color BTN_BLUE = new Color(74, 144, 226);

    public NewEmployeeWindow(Frame parent) {
        super(parent, "New Employee", true);
        initComponents();
        initLayout();
        setSize(400, 350);
        setLocationRelativeTo(parent);
    }

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

    public void addAcceptListener(ActionListener l) {
        btnAccept.addActionListener(l);
    }

    public String getEmployeeName() {
        return txtName.getText().trim();
    }

    public String getEmployeeDni() {
        return txtDni.getText().trim();
    }

    public boolean hasValidaciones() {
        return chkValidaciones.isSelected();
    }

    public boolean hasIntercambios() {
        return chkIntercambios.isSelected();
    }

    public boolean hasPedidos() {
        return chkPedidos.isSelected();
    }
}
