package vista.clienteWindows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * Reusable modal dialog to inform the user they lack a required permission.
 */
public class PermissionRequiredWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new permission required window.
     *
     * @param parent the parent
     * @param permisoNombre the permiso nombre
     */
    public PermissionRequiredWindow(Frame parent, String permisoNombre) {
        super(parent, "Access Denied", true);
        initComponents(permisoNombre);
        setSize(520, 160);
        setLocationRelativeTo(parent);
    }

    /**
     * Inits the components.
     *
     * @param permisoNombre the permiso nombre
     */
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
                PermissionRequiredWindow.this.dispose();
            }
        });
        btnPanel.add(ok);
        content.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(content);
    }

    /**
     * Mostrar.
     */
    public void mostrar() {
        setVisible(true);
    }
}
