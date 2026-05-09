package vista.userPanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import controladores.NewPasswordController;

public class CrearUsuarioPanel extends JPanel {

  // ── Paleta de colores ────────────────────────────────────────────
  static final Color BG           = new Color(74, 144, 210);
  static final Color FIELD_LINE   = new Color(220, 235, 255);
  static final Color WHITE        = Color.WHITE;
  static final Color RED        =  new Color(220, 70, 70);
  static final Color GREEN        =  new Color(90, 210, 90);
  static final Color REQ_HEADER   = new Color(60, 100, 200);

  private JLabel titleLabel;
  private PanelInferior panelInferior;
  private PanelIntermedio panelIntermedio;

  public CrearUsuarioPanel() {
    initComponents();
    initLayout();
  }

  private void initComponents() {
    titleLabel = new JLabel("Create Account");
    titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
    titleLabel.setForeground(WHITE);
    panelIntermedio = new PanelIntermedio(this);
    panelInferior   = new PanelInferior();
  }

  private void initLayout() {
    setBackground(BG);
    setLayout(new BorderLayout());

    add(titleLabel, BorderLayout.NORTH);
    titleLabel.setPreferredSize(new Dimension(0, 90));
    titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

    panelIntermedio.setAlignmentX(CENTER_ALIGNMENT);
    add(panelIntermedio, BorderLayout.CENTER);

    panelInferior.setAlignmentX(CENTER_ALIGNMENT);
    panelInferior.setPreferredSize(new Dimension(0, 120));
    add(panelInferior, BorderLayout.SOUTH);
  }

  public String getUsername()          { return panelIntermedio.fieldsPanel.usernameField.getText().trim(); }
  public String getDni()               { return panelIntermedio.fieldsPanel.dniField.getText().trim(); }
  public String getPassword()          { return panelIntermedio.fieldsPanel.passwordField.getText().trim(); }
  public String getConfirmedPassword() { return panelIntermedio.fieldsPanel.confirmPasswordField.getText().trim(); }

  public void setStatusLabelText(String text)     { 
	  panelInferior.statusLabel.setText(text); 
  }
  public void setTenCharactersBox(boolean status) { 
	  panelIntermedio.tenCharacters.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.tenCharacters, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }
  public void setUpperAndLowerBox(boolean status) { 
	  panelIntermedio.upperAndLowerCase.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.upperAndLowerCase, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }
  public void setSymbolBox(boolean status)        { 
	  panelIntermedio.symbol.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.symbol, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }
  public void setNumberBox(boolean status)        { 
	  panelIntermedio.number.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.number, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }

  public void añadirListenerBotonCrear(ActionListener a){
    panelInferior.botonCrear.addActionListener(a);
  }

  // ════════════════════════════════════════════════════════════════
  //  PANEL INTERMEDIO
  // ════════════════════════════════════════════════════════════════
  public class PanelIntermedio extends JPanel {

    FieldsPanel      fieldsPanel;
    RequirementsPanel requirementsPanel;

    // Checkboxes (mismos nombres que en el original para que los setters funcionen)
    JCheckBox tenCharacters   = new JCheckBox("10 characters");
    JCheckBox upperAndLowerCase = new JCheckBox("Uppercase and lowercase letters.");
    JCheckBox number          = new JCheckBox("Number");
    JCheckBox symbol          = new JCheckBox("Symbol");
    CrearUsuarioPanel c;

    public PanelIntermedio(CrearUsuarioPanel c) {
      this.c = c;
      initComponents();
      initLayout();
    }

    void initComponents() {
      fieldsPanel      = new FieldsPanel(c);
      requirementsPanel = new RequirementsPanel();

      // Icono verde (aprobado) y colores de advertencia por requisito
      applyCheckboxStyle(tenCharacters, RED);
      applyCheckboxStyle(upperAndLowerCase, RED);
      applyCheckboxStyle(number,     RED);
      applyCheckboxStyle(symbol,     RED);
    }

    /** Aplica estilos visuales al JCheckBox usando iconos de colores. */
    public void applyCheckboxStyle(JCheckBox cb, Color uncheckedColor) {
      cb.setBackground(new Color(50, 100, 180));
      cb.setForeground(WHITE);
      cb.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
      cb.setFocusPainted(false);
      cb.setIcon(new SquareIcon(uncheckedColor, false));
      cb.setSelectedIcon(new SquareIcon(new Color(80, 210, 80), true));
      cb.setOpaque(false);
    }

    void initLayout() {
      setBackground(BG);
      setLayout(new BorderLayout(30, 0));
      setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
      add(fieldsPanel,      BorderLayout.WEST);
      add(requirementsPanel, BorderLayout.EAST);
    }

    // ── FieldsPanel ─────────────────────────────────────────────
    public class FieldsPanel extends JPanel {

      private JLabel usernameLabel;
      private JLabel dniLabel;
      private JLabel passwordLabel;
      private JLabel confirmPasswordLabel;

      JTextField usernameField;
      JTextField dniField;
      JPasswordField passwordField;
      JPasswordField confirmPasswordField;

      JButton showPasswordButton;
      boolean isPasswordVisible = false;

      CrearUsuarioPanel c;

      public FieldsPanel(CrearUsuarioPanel c) {
        this.c = c;
        initComponents();
        initLayout();
      }

      void initComponents() {
        usernameField        = styledField();
        dniField             = styledField();
        passwordField        = styledPasswordField();
        confirmPasswordField = styledPasswordField();

        usernameLabel        = iconLabel("\uD83D\uDC64", "Username");          // 👤
        dniLabel             = iconLabel("\uD83C\uDD94", "DNI");               // 🆔
        passwordLabel        = iconLabel("\uD83D\uDD12", "New Password");      // 🔒
        confirmPasswordLabel = iconLabel("\uD83D\uDD12", "Repeat New Password"); // 🔒
        showPasswordButton = new JButton("Mostrar");

        passwordField.getDocument().addDocumentListener(new NewPasswordController(c));
        showPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPasswordVisible) {
                    // Mostrar la contraseña pasando el char 0 (desactiva la máscara)
                    passwordField.setEchoChar((char) 0);
                    confirmPasswordField.setEchoChar((char) 0);
                    showPasswordButton.setText("Ocultar");
                    isPasswordVisible = true;
                } else {
                    // Ocultar la contraseña devolviendo el punto negro
                    passwordField.setEchoChar('\u25CF');
                    confirmPasswordField.setEchoChar('\u25CF');
                    showPasswordButton.setText("Mostrar");
                    isPasswordVisible = false;
                }
            }
        });
      }

      /** Crea un JTextField con estilo subrayado (sin borde exterior). */
      private JTextField styledField() {
        JTextField f = new JTextField(25);
        f.setBackground(new Color(74, 144, 210, 0));   // mismo color que fondo
        f.setOpaque(false);
        f.setForeground(WHITE);
        f.setCaretColor(WHITE);
        f.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        f.setPreferredSize(new Dimension(320, 36));
        f.setMaximumSize(new Dimension(320, 36));
        return f;
      }

      /** Crea una etiqueta con emoji + texto, blanca y en Comic Sans. */
      private JLabel iconLabel(String icon, String text) {
        JLabel lbl = new JLabel(icon + "  " + text);
        lbl.setForeground(WHITE);
        lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
      }

      void initLayout() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addRow(usernameLabel,        usernameField);
        add(Box.createVerticalStrut(18));
        addRow(dniLabel,             dniField);
        add(Box.createVerticalStrut(18));
        addRow(passwordLabel,        passwordField);
        add(Box.createVerticalStrut(18));
        addRow(confirmPasswordLabel, confirmPasswordField);
        add(Box.createVerticalStrut(18));
        add(showPasswordButton);
      }

      private void addRow(JLabel label, JTextField field) {
        label.setAlignmentX(LEFT_ALIGNMENT);
        field.setAlignmentX(LEFT_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(4));
        add(field);
      }

      void reset() {
        usernameField.setText("");
        dniField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
      }
    }

      private JPasswordField styledPasswordField(){
        JPasswordField f = new JPasswordField(25);
        f.setBackground(new Color(74, 144, 210, 0));   // mismo color que fondo
        f.setOpaque(false);
        f.setForeground(WHITE);
        f.setCaretColor(WHITE);
        f.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        f.setPreferredSize(new Dimension(320, 36));
        f.setMaximumSize(new Dimension(320, 36));
        f.setEchoChar('\u25CF'); 
        return f;
      }

    // ── RequirementsPanel ────────────────────────────────────────
    public class RequirementsPanel extends JPanel {

      public RequirementsPanel() {
        initComponents();
        initLayout();
      }

      void initComponents() {
        tenCharacters.setEnabled(false);
        upperAndLowerCase.setEnabled(false);
        number.setEnabled(false);
        symbol.setEnabled(false);
      }

      void initLayout() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabecera "REQUIREMENTS"
        JLabel header = new JLabel("REQUIREMENTS");
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        header.setForeground(WHITE);
        header.setOpaque(true);
        header.setBackground(REQ_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        header.setAlignmentX(LEFT_ALIGNMENT);

        // Panel contenedor con fondo semitransparente
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(50, 100, 180));
        box.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        for (JCheckBox cb : new JCheckBox[]{tenCharacters, upperAndLowerCase, number, symbol}) {
          cb.setAlignmentX(LEFT_ALIGNMENT);
          box.add(cb);
          box.add(Box.createVerticalStrut(6));
        }

        add(header);
        add(box);
      }
    }
  }

  // ════════════════════════════════════════════════════════════════
  //  PANEL INFERIOR
  // ════════════════════════════════════════════════════════════════
  public class PanelInferior extends JPanel {

    private JButton botonCrear;
    private JLabel statusLabel = new JLabel();

    public PanelInferior() {
      initComponents();
      initLayout();
    }

    void initComponents() {
      botonCrear = new JButton("Crear");
      botonCrear.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
      botonCrear.setBackground(WHITE);
      botonCrear.setForeground(new Color(60, 60, 60));
      botonCrear.setFocusPainted(false);
      botonCrear.setBorderPainted(false);
      botonCrear.setCursor(new Cursor(Cursor.HAND_CURSOR));
      botonCrear.setPreferredSize(new Dimension(180, 44));
      botonCrear.setMaximumSize(new Dimension(180, 44));
      botonCrear.setOpaque(true);

      statusLabel.setForeground(new Color(255, 220, 80));
      statusLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
    }

    void initLayout() {
      setBackground(BG);
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      botonCrear.setAlignmentX(CENTER_ALIGNMENT);
      statusLabel.setAlignmentX(CENTER_ALIGNMENT);
      add(botonCrear);
      add(Box.createVerticalStrut(8));
      add(statusLabel);
    }
  }

  // ════════════════════════════════════════════════════════════════
  //  UTILIDADES VISUALES
  // ════════════════════════════════════════════════════════════════
  /**
   * Icono cuadrado de color con o sin tilde de verificación.
   */
  private static class SquareIcon implements Icon {
    private final Color   color;
    private final boolean checked;
    private static final int SIZE = 20;

    SquareIcon(Color color, boolean checked) {
      this.color   = color;
      this.checked = checked;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Relleno
      g2.setColor(color);
      g2.fillRoundRect(x, y, SIZE, SIZE, 5, 5);

      // Borde
      g2.setColor(color.darker());
      g2.setStroke(new BasicStroke(1.5f));
      g2.drawRoundRect(x, y, SIZE, SIZE, 5, 5);

      // Tilde
      if (checked) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + 4,  y + 10, x + 8,  y + 15);
        g2.drawLine(x + 8,  y + 15, x + 16, y + 5);
      }

      g2.dispose();
    }

    @Override public int getIconWidth()  { return SIZE; }
    @Override public int getIconHeight() { return SIZE; }
  }
}
