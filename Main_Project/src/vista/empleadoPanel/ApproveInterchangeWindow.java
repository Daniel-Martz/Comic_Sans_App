package vista.empleadoPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import modelo.solicitud.SolicitudIntercambio;
import controladores.ControladorManageInterchanges;

public class ApproveInterchangeWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField txtCode1;
    private JTextField txtCode2;

    public ApproveInterchangeWindow(JFrame parent, SolicitudIntercambio solicitud, ControladorManageInterchanges ctrl) {
        super(parent, "Approve Interchange", true);
        setSize(400, 250);
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
        
        JLabel lblTitulo = new JLabel("Security Codes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Formulario central
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        JLabel lblCode1 = new JLabel(solicitud.getOferta().getOfertante().getNombreUsuario() + "'s Code:");
        lblCode1.setFont(labelFont);
        lblCode1.setForeground(new Color(50, 50, 50));
        formPanel.add(lblCode1);

        txtCode1 = new JTextField();
        txtCode1.setFont(fieldFont);
        formPanel.add(txtCode1);

        JLabel lblCode2 = new JLabel(solicitud.getOferta().getDestinatario().getNombreUsuario() + "'s Code:");
        lblCode2.setFont(labelFont);
        lblCode2.setForeground(new Color(50, 50, 50));
        formPanel.add(lblCode2);

        txtCode2 = new JTextField();
        txtCode2.setFont(fieldFont);
        formPanel.add(txtCode2);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(new Color(230, 235, 240));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCancel.setBackground(new Color(178, 34, 34));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnConfirm = new JButton("Approve");
        btnConfirm.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(e -> {
            String code1 = txtCode1.getText().trim();
            String code2 = txtCode2.getText().trim();
            if (code1.isEmpty() || code2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both security codes.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ctrl.confirmarAprobacion(solicitud, code1, code2);
            dispose();
        });

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnConfirm);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
