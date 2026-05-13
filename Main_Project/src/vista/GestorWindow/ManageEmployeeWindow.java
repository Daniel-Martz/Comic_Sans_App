package vista.GestorWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageEmployeeWindow.
 */
public class ManageEmployeeWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The btn modify permissions. */
    private JButton btnModifyPermissions;
    
    /** The btn delete user. */
    private JButton btnDeleteUser;
    
    /** The user dni. */
    private String userDni;

    /** The bg color. */
    private final Color BG_COLOR = new Color(245, 247, 250);
    
    /** The btn blue. */
    private final Color BTN_BLUE = new Color(74, 144, 226);
    
    /** The btn red. */
    private final Color BTN_RED = new Color(231, 76, 60);

    /**
     * Instantiates a new manage employee window.
     *
     * @param parent the parent
     * @param userDni the user dni
     */
    public ManageEmployeeWindow(Frame parent, String userDni) {
        super(parent, "Manage employee", true);
        this.userDni = userDni;
        initComponents();
        initLayout();
        setSize(300, 200);
        setLocationRelativeTo(parent);
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        btnModifyPermissions = new JButton("Modify permissions");
        styleButton(btnModifyPermissions, BTN_BLUE);

        btnDeleteUser = new JButton("Delete employee");
        styleButton(btnDeleteUser, BTN_RED);
    }

    /**
     * Style button.
     *
     * @param btn the btn
     * @param bgColor the bg color
     */
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

    /**
     * Inits the layout.
     */
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

    /**
     * Adds the modify permissions listener.
     *
     * @param l the l
     */
    public void addModifyPermissionsListener(ActionListener l) {
        btnModifyPermissions.addActionListener(l);
    }

    /**
     * Adds the delete user listener.
     *
     * @param l the l
     */
    public void addDeleteUserListener(ActionListener l) {
        btnDeleteUser.addActionListener(l);
    }

    /**
     * Gets the user dni.
     *
     * @return the user dni
     */
    public String getUserDni() {
        return userDni;
    }
}
