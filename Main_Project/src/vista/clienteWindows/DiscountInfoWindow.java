package vista.clienteWindows;

import modelo.producto.LineaProductoVenta;
import modelo.descuento.*;
import modelo.categoria.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class DiscountInfoWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    public DiscountInfoWindow(Window parent, LineaProductoVenta p) {
        super(parent, "Discount Information", ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Descuento d = p.getDescuento();
        if (d == null) {
            for (Categoria c : p.getCategorias()) {
                if (c.getDescuento() != null && !c.getDescuento().haCaducado()) {
                    d = c.getDescuento();
                    break;
                }
            }
        }

        if (d == null) {
            mainPanel.add(new JLabel("This product doesn't have an active discount."));
            add(mainPanel);
            return;
        }

        JLabel lblTitle = new JLabel("DISCOUNT DETAILS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        lblTitle.setForeground(new Color(178, 34, 34));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(createRow("Valid From:", d.getFechaInicio().toStringFecha()));
        mainPanel.add(createRow("Valid Until:", d.getFechaFin().toStringFecha()));
        mainPanel.add(Box.createVerticalStrut(10));

        if (d instanceof Precio) {
            mainPanel.add(createRow("Type:", "Percentage Discount"));
            mainPanel.add(createRow("Discount:", ((Precio) d).getPorcentajeRebaja() + "% off"));
        } else if (d instanceof RebajaUmbral ru) {
            mainPanel.add(createRow("Type:", "Threshold Discount"));
            mainPanel.add(createRow("Minimum Spent:", String.format("%.2f €", ru.getUmbral())));
            mainPanel.add(createRow("Discount:", ru.getPorcentajeRebaja() + "% off"));
        } else if (d instanceof Cantidad c) {
            mainPanel.add(createRow("Type:", "Quantity Discount (BxGy)"));
            mainPanel.add(createRow("Buy:", String.valueOf(c.getNumeroComprados())));
            mainPanel.add(createRow("Pay only for:", String.valueOf(c.getNumeroRecibidos())));
        } else if (d instanceof Regalo r) {
            mainPanel.add(createRow("Type:", "Gift Discount"));
            mainPanel.add(createRow("Minimum Spent:", String.format("%.2f €", r.getUmbral())));
            mainPanel.add(Box.createVerticalStrut(10));
            JLabel giftsLabel = new JLabel("Gifts included:");
            giftsLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
            mainPanel.add(giftsLabel);
            for (Map.Entry<LineaProductoVenta, Integer> entry : r.getProductosRegalo().entrySet()) {
                mainPanel.add(createRow(" - " + entry.getKey().getNombre(), "x" + entry.getValue()));
            }
        }

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(245, 247, 250));
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        bottom.add(btnClose);
        add(bottom, BorderLayout.SOUTH);
    }

    private JPanel createRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        row.add(lblLabel);
        row.add(lblValue);
        return row;
    }
}
