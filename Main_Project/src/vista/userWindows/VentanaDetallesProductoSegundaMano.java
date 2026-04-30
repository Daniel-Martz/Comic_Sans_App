package vista.userWindows;

import modelo.producto.ProductoSegundaMano;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Ventana emergente (JDialog) que muestra la información detallada de un producto de segunda mano.
 */
public class VentanaDetallesProductoSegundaMano extends JDialog {

    private static final long serialVersionUID = 1L;
    private final Color COLOR_FONDO = new Color(245, 245, 250);
    private final Color COLOR_TITULO = new Color(50, 60, 100);

    public VentanaDetallesProductoSegundaMano(Window parent, ProductoSegundaMano p) {
        super(parent, "Product Details", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(550, 500);
        setLocationRelativeTo(parent);
        initComponents(p);
    }

    private void initComponents(ProductoSegundaMano p) {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header: Nombre
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_FONDO);

        JLabel lblNombre = new JLabel(p.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNombre.setForeground(COLOR_TITULO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblNombre);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center: Imagen y Detalles
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(COLOR_FONDO);

        // Imagen
        JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(200, 200));
        lblFoto.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        if (p.getFoto() != null && p.getFoto().exists()) {
            ImageIcon icon = new ImageIcon(p.getFoto().getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } else {
            lblFoto.setText("NO IMAGE");
        }
        centerPanel.add(lblFoto, BorderLayout.WEST);

        // Detalles Right Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        String condicion = p.getDatosValidacion() != null ? p.getDatosValidacion().getEstadoConservacion().toString() : "Pending";
        addDetailRow(detailsPanel, "Condition", condicion);

        String valor = p.getDatosValidacion() != null ? String.format("%.2f €", p.getDatosValidacion().getPrecioEstimadoProducto()) : "Pending";
        addDetailRow(detailsPanel, "Value", valor);

        String propietario = p.getClienteProducto() != null ? p.getClienteProducto().getNombreUsuario() : "Unknown";
        addDetailRow(detailsPanel, "Owner", propietario);

        centerPanel.add(detailsPanel, BorderLayout.CENTER);

        // South Center: Descripción
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(COLOR_FONDO);
        JLabel lblDescTitle = new JLabel("Description:");
        lblDescTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDescTitle.setBorder(new EmptyBorder(10, 0, 5, 0));
        descPanel.add(lblDescTitle, BorderLayout.NORTH);
        
        JTextArea txtDesc = new JTextArea(p.getDescripcion() != null && !p.getDescripcion().isEmpty() ? p.getDescripcion() : "No description.");
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descPanel.add(txtDesc, BorderLayout.CENTER);
        
        centerPanel.add(descPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom: Cerrar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(COLOR_FONDO);
        JButton btnCerrar = new JButton("Close");
        btnCerrar.addActionListener(e -> dispose());
        bottomPanel.add(btnCerrar);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label + ": ");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        JLabel val = new JLabel(value != null ? value : "N/A");
        val.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }
}