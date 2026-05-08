package vista.empleadoWindow;

import controladores.ControladorDescuentos;
import modelo.producto.LineaProductoVenta;
import modelo.tiempo.DateTimeSimulado;
import modelo.descuento.*;
import modelo.aplicacion.Catalogo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AddDiscountWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboType;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    
    private JPanel dynamicPanel;
    
    private JTextField txtPercentage;
    private JTextField txtThreshold;
    private JTextField txtThreshPerc;
    private JTextField txtBuyQty;
    private JTextField txtReceiveQty;
    private JTextField txtGiftId;
    private JTextField txtGiftQty;
    private JTextField txtGiftThresh;

    public AddDiscountWindow(JFrame parent, LineaProductoVenta p, ControladorDescuentos ctrl) {
        super(parent, "Add Discount", true);
        setSize(500, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(54, 119, 189), 3));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(54, 119, 189));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Discount for: " + p.getNombre());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Central
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(245, 247, 250));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel commonPanel = new JPanel();
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        commonPanel.setOpaque(false);
        comboType = new JComboBox<>(new String[]{"Percentage (%)", "Threshold (€)", "Quantity (BxGy)", "Gift"});
        comboType.addActionListener(e -> updateDynamicFields());
        commonPanel.add(createFormRow("Discount Type:", comboType));
        
        DateTimeSimulado ahora = new DateTimeSimulado();
        txtStartDate = new JTextField();
        txtStartDate.setText(ahora.getDia() + "/" + ahora.getMes() + "/" + ahora.getAño());
        commonPanel.add(createFormRow("Start Date (DD/MM/YYYY):", txtStartDate));

        DateTimeSimulado futuro = new DateTimeSimulado();
        futuro.avanzarDias(7);
        txtEndDate = new JTextField();
        txtEndDate.setText(futuro.getDia() + "/" + futuro.getMes() + "/" + futuro.getAño());
        commonPanel.add(createFormRow("End Date (DD/MM/YYYY):", txtEndDate));

        formPanel.add(commonPanel);
        formPanel.add(Box.createVerticalStrut(10));

        dynamicPanel = new JPanel(new CardLayout());
        dynamicPanel.setOpaque(false);
        
        // Panel Percentage
        JPanel pnlPerc = new JPanel(); pnlPerc.setLayout(new BoxLayout(pnlPerc, BoxLayout.Y_AXIS)); pnlPerc.setOpaque(false);
        txtPercentage = new JTextField();
        pnlPerc.add(createFormRow("Percentage (%):", txtPercentage));
        dynamicPanel.add(pnlPerc, "Percentage (%)");

        // Panel Threshold
        JPanel pnlThresh = new JPanel(); pnlThresh.setLayout(new BoxLayout(pnlThresh, BoxLayout.Y_AXIS)); pnlThresh.setOpaque(false);
        txtThreshold = new JTextField();
        pnlThresh.add(createFormRow("Threshold (€):", txtThreshold));
        txtThreshPerc = new JTextField();
        pnlThresh.add(createFormRow("Percentage (%):", txtThreshPerc));
        dynamicPanel.add(pnlThresh, "Threshold (€)");

        // Panel Quantity
        JPanel pnlQty = new JPanel(); pnlQty.setLayout(new BoxLayout(pnlQty, BoxLayout.Y_AXIS)); pnlQty.setOpaque(false);
        txtBuyQty = new JTextField();
        pnlQty.add(createFormRow("Buy Qty (X):", txtBuyQty));
        txtReceiveQty = new JTextField();
        pnlQty.add(createFormRow("Pay Qty (Y):", txtReceiveQty));
        dynamicPanel.add(pnlQty, "Quantity (BxGy)");

        // Panel Gift
        JPanel pnlGift = new JPanel(); pnlGift.setLayout(new BoxLayout(pnlGift, BoxLayout.Y_AXIS)); pnlGift.setOpaque(false);
        txtGiftThresh = new JTextField();
        pnlGift.add(createFormRow("Threshold (€):", txtGiftThresh));
        txtGiftId = new JTextField();
        pnlGift.add(createFormRow("Gift Product ID:", txtGiftId));
        txtGiftQty = new JTextField("1");
        pnlGift.add(createFormRow("Gift Qty:", txtGiftQty));
        dynamicPanel.add(pnlGift, "Gift");

        formPanel.add(dynamicPanel);
        formPanel.add(Box.createVerticalGlue()); // Push content to the top

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bottom Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(new Color(230, 235, 240));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(178, 34, 34));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> {
            try {
                String[] startParts = txtStartDate.getText().trim().split("/");
                if (startParts.length != 3) throw new IllegalArgumentException("Invalid Start Date format");
                DateTimeSimulado inicio = new DateTimeSimulado(
                        Integer.parseInt(startParts[2].trim()), 
                        Integer.parseInt(startParts[1].trim()), 
                        Integer.parseInt(startParts[0].trim()), 
                        0, 0, 0);
                
                String[] endParts = txtEndDate.getText().trim().split("/");
                if (endParts.length != 3) throw new IllegalArgumentException("Invalid End Date format");
                DateTimeSimulado fin = new DateTimeSimulado(
                        Integer.parseInt(endParts[2].trim()), 
                        Integer.parseInt(endParts[1].trim()), 
                        Integer.parseInt(endParts[0].trim()), 
                        23, 59, 59);
                        
                if (inicio.dateTimeEnSegundos() > fin.dateTimeEnSegundos()) {
                    throw new IllegalArgumentException("Start Date cannot be after End Date.");
                }

                String type = (String) comboType.getSelectedItem();
                Descuento d = null;
                if (type.contains("Percentage")) {
                    int perc = Integer.parseInt(txtPercentage.getText().trim());
                    d = new Precio(inicio, fin, perc);
                } else if (type.contains("Threshold")) {
                    double thresh = Double.parseDouble(txtThreshold.getText().trim());
                    int perc = Integer.parseInt(txtThreshPerc.getText().trim());
                    d = new RebajaUmbral(inicio, fin, thresh, perc);
                } else if (type.contains("Quantity")) {
                    int buy = Integer.parseInt(txtBuyQty.getText().trim());
                    int recv = Integer.parseInt(txtReceiveQty.getText().trim());
                    d = new Cantidad(inicio, fin, buy, recv);
                } else if (type.contains("Gift")) {
                    double thresh = Double.parseDouble(txtGiftThresh.getText().trim());
                    int giftId = Integer.parseInt(txtGiftId.getText().trim());
                    int giftQty = Integer.parseInt(txtGiftQty.getText().trim());
                    LineaProductoVenta giftProd = Catalogo.getInstancia().buscarProductoNuevo(giftId);
                    if (giftProd == null) throw new IllegalArgumentException("Gift Product ID not found in Catalog");
                    Map<LineaProductoVenta, Integer> gifts = new HashMap<>();
                    gifts.put(giftProd, giftQty);
                    d = new Regalo(inicio, fin, thresh, gifts);
                }

                ctrl.confirmarAñadirDescuento(p, d);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnConfirm);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        
        prefillData(p);
        updateDynamicFields();
    }

    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setPreferredSize(new Dimension(190, 30));
        lbl.setForeground(new Color(50, 50, 50));
        row.add(lbl, BorderLayout.WEST);

        if (component instanceof JTextField) {
            ((JTextField) component).setFont(new Font("SansSerif", Font.PLAIN, 14));
            ((JTextField) component).setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(200, 200, 200), 1),
                    new EmptyBorder(5, 5, 5, 5)));
        }
        
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    private void updateDynamicFields() {
        CardLayout cl = (CardLayout) dynamicPanel.getLayout();
        cl.show(dynamicPanel, (String) comboType.getSelectedItem());
    }

    private void prefillData(LineaProductoVenta p) {
        Descuento d = p.getDescuento();
        if (d == null) {
            for (modelo.categoria.Categoria c : p.getCategorias()) {
                if (c.getDescuento() != null && !c.getDescuento().haCaducado()) {
                    d = c.getDescuento();
                    break;
                }
            }
        }
        
        if (d == null) return;
        
        // Rellenar las fechas del descuento existente
        txtStartDate.setText(d.getFechaInicio().getDia() + "/" + d.getFechaInicio().getMes() + "/" + d.getFechaInicio().getAño());
        txtEndDate.setText(d.getFechaFin().getDia() + "/" + d.getFechaFin().getMes() + "/" + d.getFechaFin().getAño());

        if (d instanceof Precio) {
            comboType.setSelectedItem("Percentage (%)");
            txtPercentage.setText(String.valueOf(((Precio) d).getPorcentajeRebaja()));
        } else if (d instanceof RebajaUmbral) {
            comboType.setSelectedItem("Threshold (€)");
            RebajaUmbral ru = (RebajaUmbral) d;
            txtThreshold.setText(String.valueOf(ru.getUmbral()));
            txtThreshPerc.setText(String.valueOf(ru.getPorcentajeRebaja()));
        } else if (d instanceof Cantidad) {
            comboType.setSelectedItem("Quantity (BxGy)");
            Cantidad c = (Cantidad) d;
            txtBuyQty.setText(String.valueOf(c.getNumeroComprados()));
            txtReceiveQty.setText(String.valueOf(c.getNumeroRecibidos()));
        } else if (d instanceof Regalo) {
            comboType.setSelectedItem("Gift");
            Regalo r = (Regalo) d;
            txtGiftThresh.setText(String.valueOf(r.getUmbral()));
            if (r.getProductosRegalo() != null && !r.getProductosRegalo().isEmpty()) {
                Map.Entry<LineaProductoVenta, Integer> entry = r.getProductosRegalo().entrySet().iterator().next();
                txtGiftId.setText(String.valueOf(entry.getKey().getID()));
                txtGiftQty.setText(String.valueOf(entry.getValue()));
            }
        }
    }
}