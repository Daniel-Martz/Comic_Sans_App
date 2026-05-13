package vista.userPanels;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * Panel for editing the user profile information.
 */
public class EditProfilePanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant BG. */
    static final Color BG           = new Color(74, 144, 210);
    
    /** The Constant FIELD_LINE. */
    static final Color FIELD_LINE   = new Color(220, 235, 255);
    
    /** The Constant WHITE. */
    static final Color WHITE        = Color.WHITE;
    
    /** The Constant RED. */
    static final Color RED          = new Color(220, 70, 70);

    /** The title label. */
    private JLabel titleLabel;
    
    /** The username label. */
    private JLabel usernameLabel;
    
    /** The username field. */
    private JTextField usernameField;
    
    /** The dni label. */
    private JLabel dniLabel;
    
    /** The dni field. */
    private JTextField dniField;
    
    /** The old password label. */
    private JLabel oldPasswordLabel;
    
    /** The old password field. */
    private JPasswordField oldPasswordField;
    
    /** The show old password button. */
    private JButton showOldPasswordButton;
    
    /** The is old password visible. */
    private boolean isOldPasswordVisible = false;
    
    /** The password label. */
    private JLabel passwordLabel;
    
    /** The password field. */
    private JPasswordField passwordField;
    
    /** The show password button. */
    private JButton showPasswordButton;
    
    /** The is password visible. */
    private boolean isPasswordVisible = false;
    
    /** The status label. */
    private JLabel statusLabel;
    
    /** The button change data. */
    private JButton buttonChangeData;

    /**
     * Constructs the panel and initializes its components.
     */
    public EditProfilePanel() {
        initComponents();
        initLayout();
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername()    { return usernameField.getText().trim(); }
    
    /**
     * Gets the dni.
     *
     * @return the dni
     */
    public String getDni()         { return dniField.getText().trim(); }
    
    /**
     * Gets the old password.
     *
     * @return the old password
     */
    public String getOldPassword() { return String.valueOf(oldPasswordField.getPassword()).trim(); }
    
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword()    { return String.valueOf(passwordField.getPassword()).trim(); }
    
    /**
     * Sets the status label text.
     *
     * @param text the new status label text
     */
    public void setStatusLabelText(String text) { statusLabel.setText(text); }

    /**
     * Registers a listener for the data modification button.
     *
     * @param a the a
     */
    public void añadirListenerBotonChangeData(ActionListener a) {
        buttonChangeData.addActionListener(a);
    }
    
    /**
     * Fills the fields with current user information.
     *
     * @param username the username
     * @param dni the dni
     */
    public void inicializarCampos(String username, String dni) {
        usernameField.setText(username);
        dniField.setText(dni);
    }

    /**
     * Icon label.
     *
     * @param icon the icon
     * @param text the text
     * @return the j label
     */
    private JLabel iconLabel(String icon, String text) {
        JLabel lbl = new JLabel(icon + "  " + text);
        lbl.setForeground(WHITE);
        lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        return lbl;
    }

    /**
     * Adds the row.
     *
     * @param label the label
     * @param field the field
     */
    private void addRow(JLabel label, JTextField field) {
        label.setAlignmentX(CENTER_ALIGNMENT);
        field.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(4));
        add(field);
    }
    
    /**
     * Inits the components.
     */
    private void initComponents() {
        titleLabel = new JLabel("User Information");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        usernameField = styledField();
        dniField = styledField();
        passwordField = styledPasswordField();
        oldPasswordField = styledPasswordField();
        
        showOldPasswordButton = new JButton("Show");
        showPasswordButton = new JButton("Show");

        usernameLabel = iconLabel("\uD83D\uDC64", "Username");
        dniLabel = iconLabel("\uD83D\uDC64", "DNI");
        oldPasswordLabel = iconLabel("\uD83D\uDD12", "Old Password");
        passwordLabel = iconLabel("\uD83D\uDD12", "New Password");
        
        statusLabel = new JLabel("");
        statusLabel.setForeground(RED);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    
        buttonChangeData = new JButton("Modify account");
        buttonChangeData.setAlignmentX(CENTER_ALIGNMENT);
        
        showOldPasswordButton.addActionListener(e -> {
            if (!isOldPasswordVisible) {
                oldPasswordField.setEchoChar((char) 0);
                showOldPasswordButton.setText("Hide");
                isOldPasswordVisible = true;
            } else {
                oldPasswordField.setEchoChar('\u25CF');
                showOldPasswordButton.setText("Show");
                isOldPasswordVisible = false;
            }
        });
        
        showPasswordButton.addActionListener(e -> {
            if (!isPasswordVisible) {
                passwordField.setEchoChar((char) 0);
                showPasswordButton.setText("Hide");
                isPasswordVisible = true;
            } else {
                passwordField.setEchoChar('\u25CF');
                showPasswordButton.setText("Show");
                isPasswordVisible = false;
            }
        });
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setBackground(BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(titleLabel);
        add(Box.createVerticalStrut(25));

        addRow(usernameLabel, usernameField);
        add(Box.createVerticalStrut(18));
        addRow(dniLabel, dniField);
        add(Box.createVerticalStrut(18));
        
        add(oldPasswordLabel);
        add(Box.createVerticalStrut(4));
        JPanel oldPasswordRowPanel = new JPanel();
        oldPasswordRowPanel.setOpaque(false);
        oldPasswordRowPanel.setLayout(new BoxLayout(oldPasswordRowPanel, BoxLayout.X_AXIS));
        oldPasswordRowPanel.setAlignmentX(CENTER_ALIGNMENT);
        oldPasswordRowPanel.setMaximumSize(new Dimension(440, 36));
        oldPasswordRowPanel.add(Box.createHorizontalGlue());
        oldPasswordRowPanel.add(oldPasswordField);
        oldPasswordRowPanel.add(Box.createHorizontalStrut(8));
        showOldPasswordButton.setPreferredSize(new Dimension(100, 28));
        showOldPasswordButton.setMaximumSize(new Dimension(100, 28));
        oldPasswordRowPanel.add(showOldPasswordButton);
        oldPasswordRowPanel.add(Box.createHorizontalGlue());
        add(oldPasswordRowPanel);
        
        add(Box.createVerticalStrut(18));
        
        add(passwordLabel);
        add(Box.createVerticalStrut(4));
        JPanel passwordRowPanel = new JPanel();
        passwordRowPanel.setOpaque(false);
        passwordRowPanel.setLayout(new BoxLayout(passwordRowPanel, BoxLayout.X_AXIS));
        passwordRowPanel.setAlignmentX(CENTER_ALIGNMENT);
        passwordRowPanel.setMaximumSize(new Dimension(440, 36));
        passwordRowPanel.add(Box.createHorizontalGlue());
        passwordRowPanel.add(passwordField);
        passwordRowPanel.add(Box.createHorizontalStrut(8));
        showPasswordButton.setPreferredSize(new Dimension(100, 28));
        showPasswordButton.setMaximumSize(new Dimension(100, 28));
        passwordRowPanel.add(showPasswordButton);
        passwordRowPanel.add(Box.createHorizontalGlue());
        add(passwordRowPanel);
        
        add(Box.createVerticalStrut(18));
        add(buttonChangeData);
        add(Box.createVerticalStrut(18));
        add(statusLabel);
    }

    /**
     * Styled field.
     *
     * @return the j text field
     */
    private JTextField styledField() {
        JTextField f = new JTextField(25);
        f.setOpaque(false);
        f.setForeground(WHITE);
        f.setCaretColor(WHITE);
        f.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        f.setPreferredSize(new Dimension(320, 36));
        f.setMaximumSize(new Dimension(320, 36));
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_LINE));
        return f;
    }

    /**
     * Styled password field.
     *
     * @return the j password field
     */
    private JPasswordField styledPasswordField() {
        JPasswordField f = new JPasswordField(25);
        f.setOpaque(false);
        f.setForeground(WHITE);
        f.setCaretColor(WHITE);
        f.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        f.setPreferredSize(new Dimension(320, 36));
        f.setMaximumSize(new Dimension(320, 36));
        f.setEchoChar('\u25CF');
        f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_LINE));
        return f;
    }
}