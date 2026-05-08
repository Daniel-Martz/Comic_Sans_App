package vista.userWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Reusable modal dialog to inform the user they lack a required permission.
 */
public class PermissionRequiredDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public PermissionRequiredDialog(Frame parent, String permisoNombre) {
        super(parent, "Access Denied", true);
        initComponents(permisoNombre);
        setSize(420, 160);
        setLocationRelativeTo(parent);
    }

    private void initComponents(String permisoNombre) {
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel("You do not have the required permission: " + permisoNombre);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        content.add(lbl, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PermissionRequiredDialog.this.dispose();
            }
        });
        btnPanel.add(ok);
        content.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(content);
    }

    public void mostrar() {
        setVisible(true);
    }
}
