package vista.empleadoWindow;

import controladores.ControladorValidationRequests;
import modelo.producto.EstadoConservacion;
import modelo.solicitud.SolicitudValidacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidarProductoWindow.
 */
public class ValidarProductoWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The txt precio validacion. */
    private JTextField txtPrecioValidacion;
    
    /** The txt precio estimado. */
    private JTextField txtPrecioEstimado;
    
    /** The cb estado. */
    private JComboBox<EstadoConservacion> cbEstado;

    /**
     * Instantiates a new validar producto window.
     *
     * @param parent the parent
     * @param solicitud the solicitud
     * @param ctrl the ctrl
     */
    public ValidarProductoWindow(JFrame parent, SolicitudValidacion solicitud, ControladorValidationRequests ctrl) {
        super(parent, "Validate Product", true);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(54, 119, 189), 3));

        // Título de la ventana
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(54, 119, 189));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Valuating: " + solicitud.getProductoAValidar().getNombre());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Formulario central
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Validation Fee
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        JLabel lblFee = new JLabel("Validation Fee (€):");
        lblFee.setFont(labelFont);
        lblFee.setForeground(new Color(50, 50, 50));
        formPanel.add(lblFee, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.6;
        txtPrecioValidacion = new JTextField();
        txtPrecioValidacion.setFont(fieldFont);
        txtPrecioValidacion.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(txtPrecioValidacion, gbc);

        // Estimated Value
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        JLabel lblValue = new JLabel("Estimated Value (€):");
        lblValue.setFont(labelFont);
        lblValue.setForeground(new Color(50, 50, 50));
        formPanel.add(lblValue, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.6;
        txtPrecioEstimado = new JTextField();
        txtPrecioEstimado.setFont(fieldFont);
        txtPrecioEstimado.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(txtPrecioEstimado, gbc);

        // Condition
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4;
        JLabel lblCondition = new JLabel("Condition:");
        lblCondition.setFont(labelFont);
        lblCondition.setForeground(new Color(50, 50, 50));
        formPanel.add(lblCondition, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.6;
        cbEstado = new JComboBox<>(EstadoConservacion.values());
        cbEstado.setFont(fieldFont);
        cbEstado.setBackground(Color.WHITE);
        formPanel.add(cbEstado, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(new Color(230, 235, 240));
        bottomPanel.setBorder(new LineBorder(new Color(210, 215, 220), 1));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCancel.setBackground(new Color(178, 34, 34));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnConfirm = new JButton("Confirm Validation");
        btnConfirm.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(e -> {
            try {
                double pVal = Double.parseDouble(txtPrecioValidacion.getText().replace(",", "."));
                double pProd = Double.parseDouble(txtPrecioEstimado.getText().replace(",", "."));
                EstadoConservacion estado = (EstadoConservacion) cbEstado.getSelectedItem();
                
                ctrl.confirmarValidacion(solicitud, pVal, pProd, estado);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric prices.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnConfirm);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}