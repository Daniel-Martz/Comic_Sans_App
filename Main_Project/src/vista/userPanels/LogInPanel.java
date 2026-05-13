package vista.userPanels;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

// TODO: Auto-generated Javadoc
/**
 * The Class LogInPanel.
 */
public class LogInPanel extends JPanel {

  /** The Constant BG. */
  static final Color BG           = new Color(74, 144, 210);
  
  /** The Constant FIELD_LINE. */
  static final Color FIELD_LINE   = new Color(220, 235, 255);
  
  /** The Constant WHITE. */
  static final Color WHITE        = Color.WHITE;
  
  /** The Constant RED. */
  static final Color RED        =  new Color(220, 70, 70);
  
  /** The Constant GREEN. */
  static final Color GREEN        =  new Color(90, 210, 90);
  
  /** The Constant REQ_HEADER. */
  static final Color REQ_HEADER   = new Color(60, 100, 200);

  /** The title label. */
  private JLabel titleLabel;
  
  /** The username label. */
  private JLabel usernameLabel;
  
  /** The username field. */
  private JTextField usernameField;
  
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
  
  /** The button. */
  private JButton button;
  
  /** The create account label. */
  private JLabel createAccountLabel;
  
  /** The create account button. */
  private JButton createAccountButton;

  /**
   * Instantiates a new log in panel.
   */
  public LogInPanel() {
    initComponents();
    initLayout();
  }

    
  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername()          { return usernameField.getText().trim(); }
  
  /**
   * Gets the password.
   *
   * @return the password
   */
  public String getPassword()          { return String.valueOf(passwordField.getPassword()).trim(); }
  
  /**
   * Sets the status label text.
   *
   * @param text the new status label text
   */
  public void setStatusLabelText(String text)   {statusLabel.setText(text);}


  /**
   * Añadir listener boton log in.
   *
   * @param a the a
   */
  public void añadirListenerBotonLogIn(ActionListener a){
    button.addActionListener(a);
  }

  /**
   * Añadir listener boton crear usuario.
   *
   * @param a the a
   */
  public void añadirListenerBotonCrearUsuario(ActionListener a){
    createAccountButton.addActionListener(a);
  }

  /**
   * Crea una etiqueta con emoji + texto, blanca y en Comic Sans.
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
		titleLabel = new JLabel("Log In");
		titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		titleLabel.setForeground(WHITE);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);

		usernameField  = styledField();
		usernameField.setAlignmentX(CENTER_ALIGNMENT);
		passwordField  = styledPasswordField();
		passwordField.setAlignmentX(CENTER_ALIGNMENT); 
		showPasswordButton = new JButton("Show");
		usernameLabel  = iconLabel("\uD83D\uDC64", "Username");
		usernameLabel.setAlignmentX(CENTER_ALIGNMENT); 
		passwordLabel  = iconLabel("\uD83D\uDD12", "Password");
		passwordLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		statusLabel = new JLabel("");
		statusLabel.setForeground(RED);
		statusLabel.setAlignmentX(CENTER_ALIGNMENT); 
		statusLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    
		button = new JButton();
		button.setText("Log in");
		button.setAlignmentX(CENTER_ALIGNMENT); 
    
		createAccountLabel = new JLabel("Don't have an account?");
		createAccountLabel.setForeground(WHITE);
		createAccountLabel.setAlignmentX(CENTER_ALIGNMENT); 
		createAccountLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    
		createAccountButton = new JButton();
		createAccountButton.setText("Create account");
		createAccountButton.setAlignmentX(CENTER_ALIGNMENT); 
		
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
		
		usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(passwordLabel);
		add(Box.createVerticalStrut(4));

		passwordField.setAlignmentX(CENTER_ALIGNMENT);
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
		showPasswordButton.setAlignmentX(CENTER_ALIGNMENT);
		passwordRowPanel.add(showPasswordButton);
		passwordRowPanel.add(Box.createHorizontalGlue());
		add(passwordRowPanel);
		
		add(Box.createVerticalStrut(18));
		add(button);
		add(Box.createVerticalStrut(18));
		add(statusLabel);
		add(Box.createVerticalStrut(18));
		add(createAccountLabel);
		add(Box.createVerticalStrut(18));
		add(createAccountButton);
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
