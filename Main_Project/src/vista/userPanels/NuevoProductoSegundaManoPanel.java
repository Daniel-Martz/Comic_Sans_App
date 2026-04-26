package vista.userPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel para el formulario de añadir un nuevo producto de segunda mano.
 * Actúa como VISTA. No contiene lógica de negocio.
 * * Estructura modular:
 * - Titulo (JLabel)
 * - FormPanel (GridBagLayout con los inputs)
 * - ActionPanel (FlowLayout con los botones)
 */
public class NuevoProductoSegundaManoPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------
    // Subpaneles internos
    // -------------------------------------------------------
    private FormPanel formPanel;
    private ActionPanel actionPanel;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public NuevoProductoSegundaManoPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        formPanel = new FormPanel();
        actionPanel = new ActionPanel();
    }

    private void initLayout() {
        setBackground(new Color(153, 180, 209));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ── Título ──────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("NEW SECOND-HAND PRODUCT", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(74, 118, 201));
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));

        // ── Ensamblaje ──────────────────────────────────────────
        add(lblTitulo, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    // -------------------------------------------------------
    // Métodos públicos para el CONTROLADOR (Delegación)
    // -------------------------------------------------------
    public String getNombre() { return formPanel.getNombre(); }
    public String getDescripcion() { return formPanel.getDescripcion(); }
    public void setNombreImagen(String nombre) { formPanel.setNombreImagen(nombre); }

    public void setControlador(ActionListener c) {
        formPanel.addImagenListener(c);
        actionPanel.addConfirmarListener(c);
        actionPanel.addCancelarListener(c);
    }

    // =======================================================
    // CLASES INTERNAS (SUBPANELES)
    // =======================================================

    /**
     * Subpanel central: Contiene el formulario (Nombre, Descripción, Imagen).
     */
    private class FormPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JTextField txtNombre;
        private JTextArea txtDescripcion;
        private JButton btnSubirImagen;
        private JLabel lblNombreImagen;

        public FormPanel() {
            setLayout(new GridBagLayout());
            setBackground(new Color(153, 180, 209));
            setBorder(new EmptyBorder(30, 50, 30, 50));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            // Fila 0: Nombre
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
            JLabel lblNombre = new JLabel("Product Name:");
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
            add(lblNombre, gbc);

            gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
            txtNombre = new JTextField(20);
            txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
            add(txtNombre, gbc);

            // Fila 1: Descripción
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2; gbc.anchor = GridBagConstraints.NORTH;
            JLabel lblDesc = new JLabel("Description:");
            lblDesc.setFont(new Font("SansSerif", Font.BOLD, 14));
            add(lblDesc, gbc);

            gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
            txtDescripcion = new JTextArea(4, 20);
            txtDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);
            JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
            add(scrollDesc, gbc);

            // Fila 2: Imagen
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2; gbc.anchor = GridBagConstraints.CENTER;
            JLabel lblImagen = new JLabel("Product Image:");
            lblImagen.setFont(new Font("SansSerif", Font.BOLD, 14));
            add(lblImagen, gbc);

            gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.8;
            JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panelImagen.setOpaque(false);
            
            btnSubirImagen = new JButton("Upload Image...");
            lblNombreImagen = new JLabel(" No file selected");
            lblNombreImagen.setFont(new Font("SansSerif", Font.ITALIC, 12));
            
            panelImagen.add(btnSubirImagen);
            panelImagen.add(lblNombreImagen);
            add(panelImagen, gbc);
        }

        public String getNombre() { return txtNombre.getText().trim(); }
        public String getDescripcion() { return txtDescripcion.getText().trim(); }
        public void setNombreImagen(String nombre) { lblNombreImagen.setText(" " + nombre); }

        public void addImagenListener(ActionListener l) {
            btnSubirImagen.addActionListener(l);
            btnSubirImagen.setActionCommand("SUBIR_IMAGEN");
        }
    }

    /**
     * Subpanel sur: Contiene los botones de acción.
     */
    private class ActionPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JButton btnConfirmar;
        private JButton btnCancelar;

        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
            setBackground(new Color(153, 180, 209));

            btnConfirmar = new JButton("CONFIRM");
            btnConfirmar.setBackground(new Color(50, 205, 50));
            btnConfirmar.setForeground(Color.WHITE);
            btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnConfirmar.setPreferredSize(new Dimension(120, 35));

            btnCancelar = new JButton("CANCEL");
            btnCancelar.setBackground(new Color(178, 34, 34));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnCancelar.setPreferredSize(new Dimension(120, 35));

            add(btnConfirmar);
            add(btnCancelar);
        }

        public void addConfirmarListener(ActionListener l) {
            btnConfirmar.addActionListener(l);
            btnConfirmar.setActionCommand("CONFIRMAR");
        }

        public void addCancelarListener(ActionListener l) {
            btnCancelar.addActionListener(l);
            btnCancelar.setActionCommand("CANCELAR");
        }
    }
}