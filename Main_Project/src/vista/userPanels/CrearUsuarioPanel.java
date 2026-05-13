package vista.userPanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import controladores.NewPasswordController;

// TODO: Auto-generated Javadoc
/**
 * The Class CrearUsuarioPanel.
 */
public class CrearUsuarioPanel extends JPanel {

  /** The Constant BG. */
  // ── Paleta de colores ────────────────────────────────────────────
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
  
  /** The panel inferior. */
  private PanelInferior panelInferior;
  
  /** The panel intermedio. */
  private PanelIntermedio panelIntermedio;

  /**
   * Instantiates a new crear usuario panel.
   */
  public CrearUsuarioPanel() {
    initComponents();
    initLayout();
  }

  /**
   * Inits the components.
   */
  private void initComponents() {
    titleLabel = new JLabel("Create Account");
    titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
    titleLabel.setForeground(WHITE);
    panelIntermedio = new PanelIntermedio(this);
    panelInferior   = new PanelInferior();
  }

  /**
   * Inits the layout.
   */
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

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername()          { return panelIntermedio.fieldsPanel.usernameField.getText().trim(); }
  
  /**
   * Gets the dni.
   *
   * @return the dni
   */
  public String getDni()               { return panelIntermedio.fieldsPanel.dniField.getText().trim(); }
  
  /**
   * Gets the password.
   *
   * @return the password
   */
  public String getPassword()          { return panelIntermedio.fieldsPanel.passwordField.getText().trim(); }
  
  /**
   * Gets the confirmed password.
   *
   * @return the confirmed password
   */
  public String getConfirmedPassword() { return panelIntermedio.fieldsPanel.confirmPasswordField.getText().trim(); }

  /**
   * Sets the status label text.
   *
   * @param text the new status label text
   */
  public void setStatusLabelText(String text)     { 
	  panelInferior.statusLabel.setText(text); 
  }
  
  /**
   * Sets the ten characters box.
   *
   * @param status the new ten characters box
   */
  public void setTenCharactersBox(boolean status) { 
	  panelIntermedio.tenCharacters.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.tenCharacters, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }
  
  /**
   * Sets the upper and lower box.
   *
   * @param status the new upper and lower box
   */
  public void setUpperAndLowerBox(boolean status) { 
	  panelIntermedio.upperAndLowerCase.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.upperAndLowerCase, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }
  
  /**
   * Sets the symbol box.
   *
   * @param status the new symbol box
   */
  public void setSymbolBox(boolean status)        { 
	  panelIntermedio.symbol.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.symbol, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }
  
  /**
   * Sets the number box.
   *
   * @param status the new number box
   */
  public void setNumberBox(boolean status)        { 
	  panelIntermedio.number.setSelected(status); 
	  panelIntermedio.applyCheckboxStyle(panelIntermedio.number, status ? new Color(90, 210, 90) : new Color(220, 70, 70));
  }

  /**
   * Añadir listener boton crear.
   *
   * @param a the a
   */
  public void añadirListenerBotonCrear(ActionListener a){
    panelInferior.botonCrear.addActionListener(a);
  }

  // ════════════════════════════════════════════════════════════════
  //  PANEL INTERMEDIO
  /**
   * The Class PanelIntermedio.
   */
  // ════════════════════════════════════════════════════════════════
  public class PanelIntermedio extends JPanel {

    /** The fields panel. */
    FieldsPanel      fieldsPanel;
    
    /** The requirements panel. */
    RequirementsPanel requirementsPanel;

    /** The ten characters. */
    // Checkboxes (mismos nombres que en el original para que los setters funcionen)
    JCheckBox tenCharacters   = new JCheckBox("10 characters");
    
    /** The upper and lower case. */
    JCheckBox upperAndLowerCase = new JCheckBox("Uppercase and lowercase letters.");
    
    /** The number. */
    JCheckBox number          = new JCheckBox("Number");
    
    /** The symbol. */
    JCheckBox symbol          = new JCheckBox("Symbol");
    
    /** The c. */
    CrearUsuarioPanel c;

    /**
     * Instantiates a new panel intermedio.
     *
     * @param c the c
     */
    public PanelIntermedio(CrearUsuarioPanel c) {
      this.c = c;
      initComponents();
      initLayout();
    }

    /**
     * Inits the components.
     */
    void initComponents() {
      fieldsPanel      = new FieldsPanel(c);
      requirementsPanel = new RequirementsPanel();

      // Icono verde (aprobado) y colores de advertencia por requisito
      applyCheckboxStyle(tenCharacters, RED);
      applyCheckboxStyle(upperAndLowerCase, RED);
      applyCheckboxStyle(number,     RED);
      applyCheckboxStyle(symbol,     RED);
    }

    /**
     * Aplica estilos visuales al JCheckBox usando iconos de colores.
     *
     * @param cb the cb
     * @param uncheckedColor the unchecked color
     */
    public void applyCheckboxStyle(JCheckBox cb, Color uncheckedColor) {
      cb.setBackground(new Color(50, 100, 180));
      cb.setForeground(WHITE);
      cb.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
      cb.setFocusPainted(false);
      cb.setIcon(new SquareIcon(uncheckedColor, false));
      cb.setSelectedIcon(new SquareIcon(new Color(80, 210, 80), true));
      cb.setOpaque(false);
    }

    /**
     * Inits the layout.
     */
    void initLayout() {
      setBackground(BG);
      setLayout(new BorderLayout(30, 0));
      setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
      add(fieldsPanel,      BorderLayout.WEST);
      add(requirementsPanel, BorderLayout.EAST);
    }

    /**
     * The Class FieldsPanel.
     */
    // ── FieldsPanel ─────────────────────────────────────────────
    public class FieldsPanel extends JPanel {

      private static final long serialVersionUID = 1L;

	  /** The username label. */
      private JLabel usernameLabel;
      
      /** The dni label. */
      private JLabel dniLabel;
      
      /** The password label. */
      private JLabel passwordLabel;
      
      /** The confirm password label. */
      private JLabel confirmPasswordLabel;

      /** The username field. */
      JTextField usernameField;
      
      /** The dni field. */
      JTextField dniField;
      
      /** The password field. */
      JPasswordField passwordField;
      
      /** The confirm password field. */
      JPasswordField confirmPasswordField;

      /** The show password button. */
      JButton showPasswordButton;
      
      /** The is password visible. */
      boolean isPasswordVisible = false;

      /** The c. */
      CrearUsuarioPanel c;

      /**
       * Instantiates a new fields panel.
       *
       * @param c the c
       */
      public FieldsPanel(CrearUsuarioPanel c) {
        this.c = c;
        initComponents();
        initLayout();
      }

      /**
       * Inits the components.
       */
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

      /**
       * Crea un JTextField con estilo subrayado (sin borde exterior).
       *
       * @return the j text field
       */
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
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
      }

      /**
       * Inits the layout.
       */
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

      /**
       * Adds the row.
       *
       * @param label the label
       * @param field the field
       */
      private void addRow(JLabel label, JTextField field) {
        label.setAlignmentX(LEFT_ALIGNMENT);
        field.setAlignmentX(LEFT_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(4));
        add(field);
      }

      /**
       * Reset.
       */
      void reset() {
        usernameField.setText("");
        dniField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
      }
    }

      /**
       * Styled password field.
       *
       * @return the j password field
       */
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

    /**
     * The Class RequirementsPanel.
     */
    public class RequirementsPanel extends JPanel {

      private static final long serialVersionUID = 1L;

	  /**
       * Instantiates a new requirements panel.
       */
      public RequirementsPanel() {
        initComponents();
        initLayout();
      }

      /**
       * Inits the components.
       */
      void initComponents() {
        tenCharacters.setEnabled(false);
        upperAndLowerCase.setEnabled(false);
        number.setEnabled(false);
        symbol.setEnabled(false);
      }

      /**
       * Inits the layout.
       */
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
  /**
   * The Class PanelInferior.
   */
  // ════════════════════════════════════════════════════════════════
  public class PanelInferior extends JPanel {

    private static final long serialVersionUID = 1L;

	/** The boton crear. */
    private JButton botonCrear;
    
    /** The status label. */
    private JLabel statusLabel = new JLabel();

    /**
     * Instantiates a new panel inferior.
     */
    public PanelInferior() {
      initComponents();
      initLayout();
    }

    /**
     * Inits the components.
     */
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

    /**
     * Inits the layout.
     */
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
    
    /** The color. */
    private final Color   color;
    
    /** The checked. */
    private final boolean checked;
    
    /** The Constant SIZE. */
    private static final int SIZE = 20;

    /**
     * Instantiates a new square icon.
     *
     * @param color the color
     * @param checked the checked
     */
    SquareIcon(Color color, boolean checked) {
      this.color   = color;
      this.checked = checked;
    }

    /**
     * Paint icon.
     *
     * @param c the c
     * @param g the g
     * @param x the x
     * @param y the y
     */
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

    /**
     * Gets the icon width.
     *
     * @return the icon width
     */
    @Override public int getIconWidth()  { return SIZE; }
    
    /**
     * Gets the icon height.
     *
     * @return the icon height
     */
    @Override public int getIconHeight() { return SIZE; }
  }
}
