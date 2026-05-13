package vista.userPanels;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Panel de inicio de sesión.
 *
 * Muestra los campos de usuario/contraseña y botones para iniciar sesión o
 * crear cuenta. Comentario en estilo natural: es la UI que aparece cuando el
 * usuario quiere autenticarse.
 */
public class LogInPanel extends JPanel {

  // ── Paleta de colores ────────────────────────────────────────────
  static final Color BG           = new Color(74, 144, 210);
  static final Color FIELD_LINE   = new Color(220, 235, 255);
  static final Color WHITE        = Color.WHITE;
  static final Color RED        =  new Color(220, 70, 70);
  static final Color GREEN        =  new Color(90, 210, 90);
  static final Color REQ_HEADER   = new Color(60, 100, 200);

  private JLabel titleLabel;
  private JLabel usernameLabel;
  private JTextField usernameField;
  private JLabel passwordLabel;
  private JPasswordField passwordField;
  private JButton showPasswordButton;
  private boolean isPasswordVisible = false;
  private JLabel statusLabel;
  private JButton button;
  private JLabel createAccountLabel;
  private JButton createAccountButton;

  public LogInPanel() {
    initComponents();
    initLayout();
  }

    
  public String getUsername()          { return usernameField.getText().trim(); }
  public String getPassword()          { return String.valueOf(passwordField.getPassword()).trim(); }
  public void setStatusLabelText(String text)   {statusLabel.setText(text);}


  public void añadirListenerBotonLogIn(ActionListener a){
    button.addActionListener(a);
  }

  public void añadirListenerBotonCrearUsuario(ActionListener a){
    createAccountButton.addActionListener(a);
  }

  /** Crea una etiqueta con emoji + texto, blanca y en Comic Sans. */
  private JLabel iconLabel(String icon, String text) {
    JLabel lbl = new JLabel(icon + "  " + text);
    lbl.setForeground(WHITE);
    lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    lbl.setAlignmentX(CENTER_ALIGNMENT);
    return lbl;
  }

  private void addRow(JLabel label, JTextField field) {
    label.setAlignmentX(CENTER_ALIGNMENT);
    field.setAlignmentX(CENTER_ALIGNMENT);
    add(label);
    add(Box.createVerticalStrut(4));
    add(field);
  }
  
	  private void initComponents() {
		titleLabel = new JLabel("Log In");
		titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		titleLabel.setForeground(WHITE);
		titleLabel.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout

		usernameField  = styledField();
		usernameField.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		passwordField  = styledPasswordField();
		passwordField.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		showPasswordButton = new JButton("Mostrar");
		usernameLabel  = iconLabel("\uD83D\uDC64", "Username");
		usernameLabel.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		passwordLabel  = iconLabel("\uD83D\uDD12", "Password");
		passwordLabel.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		
		statusLabel = new JLabel("");
		statusLabel.setForeground(RED);
		statusLabel.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		statusLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    
		button = new JButton();
		button.setText("Log in");
		button.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
    
		createAccountLabel = new JLabel("Don't have an account?");
		createAccountLabel.setForeground(WHITE);
		createAccountLabel.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		createAccountLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
    
		createAccountButton = new JButton();
		createAccountButton.setText("Create account");
		createAccountButton.setAlignmentX(CENTER_ALIGNMENT); // Importante para BoxLayout
		
		showPasswordButton.addActionListener(e -> {
            if (!isPasswordVisible) {
                passwordField.setEchoChar((char) 0);
                showPasswordButton.setText("Ocultar");
                isPasswordVisible = true;
            } else {
                passwordField.setEchoChar('\u25CF');
                showPasswordButton.setText("Mostrar");
                isPasswordVisible = false;
            }
        });
 
	}

	private void initLayout() {
		setBackground(BG);
		// Añadimos un margen interno al panel para que no esté pegado al borde
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// 1. Añadir el título solo
		add(titleLabel);
		add(Box.createVerticalStrut(25));

		// 2. Añadir filas correctamente
		addRow(usernameLabel, usernameField);
		add(Box.createVerticalStrut(18));
		
		// Fila con contraseña y botón mostrar/ocultar
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

	private JTextField styledField() {
		JTextField f = new JTextField(25);
		f.setOpaque(false);
		f.setForeground(WHITE);
		f.setCaretColor(WHITE);
		f.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		f.setPreferredSize(new Dimension(320, 36));
		f.setMaximumSize(new Dimension(320, 36));
		
		// ESTO es lo que crea el efecto de subrayado:
		f.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_LINE));
		
		return f;
	}

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
