package vista.clienteWindows;

import controladores.ControladorHistorialPedidos;
import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddReviewWindow extends JDialog {
    private JSpinner ratingSpinner;
    private JTextArea descriptionArea;
    private JButton btnConfirmChanges;

    public AddReviewWindow(JFrame parent, LineaProductoVenta producto, ControladorHistorialPedidos controlador) {
        super(parent, "Add Review for " + producto.getNombre(), true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Write a Review");
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Rating
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.setOpaque(false);
        JLabel ratingLabel = new JLabel("Rating (0.0 - 5.0): ");
        SpinnerNumberModel model = new SpinnerNumberModel(5.0, 0.0, 5.0, 0.5);
        ratingSpinner = new JSpinner(model);
        
        // Aumentar el tamaño del spinner para que quepan bien los decimales
        JComponent editor = ratingSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(3);
        }
        ratingSpinner.setPreferredSize(new Dimension(50, 30));

        ratingPanel.add(ratingLabel);
        ratingPanel.add(ratingSpinner);
        ratingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(ratingPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Description
        JLabel descLabel = new JLabel("Comment:");
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(descLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnConfirmChanges = new JButton("Confirm Changes");
        btnConfirmChanges.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnConfirmChanges.setBackground(new Color(46, 204, 113));
        btnConfirmChanges.setForeground(Color.WHITE);
        btnConfirmChanges.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnConfirmChanges.addActionListener(e -> {
            controlador.confirmarReseña(producto, descriptionArea.getText(), (Double) ratingSpinner.getValue(), this);
        });

        bottomPanel.add(btnConfirmChanges);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
