package vista.GestorWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ManageEmployeeWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private JButton btnModifyPermissions;
    private JButton btnDeleteUser;
    private String userDni;

    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color BTN_BLUE = new Color(74, 144, 226);
    private final Color BTN_RED = new Color(231, 76, 60);

    public ManageEmployeeWindow(Frame parent, String userDni) {
        super(parent, "Manage employee", true);
        this.userDni = userDni;
        initComponents();
        initLayout();
        setSize(300, 200);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        btnModifyPermissions = new JButton("Modify permissions");
        styleButton(btnModifyPermissions, BTN_BLUE);

        btnDeleteUser = new JButton("Delete employee");
        styleButton(btnDeleteUser, BTN_RED);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));
    }

    private void initLayout() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        btnModifyPermissions.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDeleteUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(btnModifyPermissions);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnDeleteUser);

        setContentPane(panel);
    }

    public void addModifyPermissionsListener(ActionListener l) {
        btnModifyPermissions.addActionListener(l);
    }

    public void addDeleteUserListener(ActionListener l) {
        btnDeleteUser.addActionListener(l);
    }

    public String getUserDni() {
        return userDni;
    }
}
