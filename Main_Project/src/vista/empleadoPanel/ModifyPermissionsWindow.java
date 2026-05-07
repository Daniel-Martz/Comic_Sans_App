package vista.empleadoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ModifyPermissionsWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private JCheckBox chkValidaciones;
    private JCheckBox chkIntercambios;
    private JCheckBox chkPedidos;
    private JButton btnAccept;
    private String userDni;

    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color BTN_BLUE = new Color(74, 144, 226);

    public ModifyPermissionsWindow(Frame parent, String userDni, boolean hasValidaciones, boolean hasIntercambios, boolean hasPedidos) {
        super(parent, "Modify permissions", true);
        this.userDni = userDni;
        initComponents(hasValidaciones, hasIntercambios, hasPedidos);
        initLayout();
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private void initComponents(boolean hasValidaciones, boolean hasIntercambios, boolean hasPedidos) {
        chkValidaciones = new JCheckBox("Modifications and changes on the available products and its stock");
        chkValidaciones.setSelected(hasValidaciones);
        chkValidaciones.setBackground(BG_COLOR);
        
        chkIntercambios = new JCheckBox("Interchange management");
        chkIntercambios.setSelected(hasIntercambios);
        chkIntercambios.setBackground(BG_COLOR);
        
        chkPedidos = new JCheckBox("Sales management");
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

    public void addAcceptListener(ActionListener l) {
        btnAccept.addActionListener(l);
    }

    public String getUserDni() {
        return userDni;
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
