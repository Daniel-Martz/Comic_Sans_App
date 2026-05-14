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
import java.util.List;
 
// TODO: Auto-generated Javadoc
/**
 * The Class AddDiscountWindow.
 */
public class AddDiscountWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The combo type. */
    private JComboBox<String> comboType;
    
    /** The txt start date. */
    private JTextField txtStartDate;
    
    /** The txt end date. */
    private JTextField txtEndDate;
    
    /** The cb start hour. */
    private JComboBox<Integer> cbStartHour;
    
    /** The cb start minute. */
    private JComboBox<Integer> cbStartMinute;
    
    /** The cb end hour. */
    private JComboBox<Integer> cbEndHour;
    
    /** The cb end minute. */
    private JComboBox<Integer> cbEndMinute;
    
    /** The dynamic panel. */
    private JPanel dynamicPanel;
    
    /** The txt percentage. */
    private JTextField txtPercentage;
    
    /** The txt threshold. */
    private JTextField txtThreshold;
    
    /** The txt thresh perc. */
    private JTextField txtThreshPerc;
    
    /** The txt buy qty. */
    private JTextField txtBuyQty;
    
    /** The txt receive qty. */
    private JTextField txtReceiveQty;
    
    /** The txt gift id. */
    private JTextField txtGiftId;
    
    /** The txt gift qty. */
    private JTextField txtGiftQty;
    
    /** The txt gift thresh. */
    private JTextField txtGiftThresh;
    
    /** The old discount. */
    private Descuento oldDiscount;
    
    /** The productos. */
    private List<LineaProductoVenta> productos;

    /**
     * Instantiates a new adds the discount window.
     *
     * @param parent the parent
     * @param productos the productos
     * @param oldDiscount the old discount
     * @param ctrl the ctrl
     */
    public AddDiscountWindow(JFrame parent, List<LineaProductoVenta> productos, Descuento oldDiscount, ControladorDescuentos ctrl) {
        super(parent, oldDiscount == null ? "Add Discount" : "Edit Discount", true);
        this.productos = productos;
        this.oldDiscount = oldDiscount;
        setSize(680, 560);
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
        
        String titleTxt = oldDiscount == null ? "Create Discount for " + productos.size() + " product(s)" : "Edit Existing Discount";
        JLabel lblTitulo = new JLabel(titleTxt);
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

        JPanel affectedPanel = new JPanel(new BorderLayout());
        affectedPanel.setOpaque(false);
        affectedPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JLabel lblAffected = new JLabel("Affected Products (" + productos.size() + "):");
        lblAffected.setFont(new Font("SansSerif", Font.BOLD, 12));
        affectedPanel.add(lblAffected, BorderLayout.NORTH);
        
        StringBuilder sb = new StringBuilder();
        for (LineaProductoVenta prod : productos) {
            sb.append("- ").append(prod.getNombre()).append("\n");
        }
        JTextArea txtAffected = new JTextArea(sb.toString());
        txtAffected.setEditable(false);
        txtAffected.setOpaque(false);
        txtAffected.setFont(new Font("SansSerif", Font.ITALIC, 12));
        JScrollPane scrollAffected = new JScrollPane(txtAffected);
        scrollAffected.setPreferredSize(new Dimension(600, 60));
        scrollAffected.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        affectedPanel.add(scrollAffected, BorderLayout.CENTER);
        
        formPanel.add(affectedPanel);

        JPanel commonPanel = new JPanel();
        commonPanel.setLayout(new BoxLayout(commonPanel, BoxLayout.Y_AXIS));
        commonPanel.setOpaque(false);
        comboType = new JComboBox<>(new String[]{"Percentage (%)", "Threshold (€)", "Quantity (BxGy)", "Gift"});
        comboType.addActionListener(e -> updateDynamicFields());
        commonPanel.add(createFormRow("Discount Type:", comboType));
        
        DateTimeSimulado ahora = new DateTimeSimulado();
        txtStartDate = new JTextField();
        txtStartDate.setText(ahora.getDia() + "/" + ahora.getMes() + "/" + ahora.getAño());
        txtStartDate.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtStartDate.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 5, 5, 5)));
        txtStartDate.setPreferredSize(new Dimension(110, 30));

        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) hours[i] = i;
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 60; i++) minutes[i] = i;

        cbStartHour = new JComboBox<>(hours);
        cbStartMinute = new JComboBox<>(minutes);
        cbStartHour.setSelectedItem(ahora.getHora());
        cbStartMinute.setSelectedItem(ahora.getMinuto());
        
        JPanel pnlStartDateTime = new JPanel();
        pnlStartDateTime.setLayout(new BoxLayout(pnlStartDateTime, BoxLayout.X_AXIS));
        pnlStartDateTime.setOpaque(false);
        pnlStartDateTime.add(txtStartDate);
        pnlStartDateTime.add(Box.createHorizontalStrut(10));
        pnlStartDateTime.add(new JLabel("H:"));
        pnlStartDateTime.add(Box.createHorizontalStrut(5));
        pnlStartDateTime.add(cbStartHour);
        pnlStartDateTime.add(Box.createHorizontalStrut(10));
        pnlStartDateTime.add(new JLabel("M:"));
        pnlStartDateTime.add(Box.createHorizontalStrut(5));
        pnlStartDateTime.add(cbStartMinute);

        commonPanel.add(createFormRow("Start Date:", pnlStartDateTime));

        DateTimeSimulado futuro = new DateTimeSimulado();
        futuro.avanzarDias(7);
        txtEndDate = new JTextField();
        txtEndDate.setText(futuro.getDia() + "/" + futuro.getMes() + "/" + futuro.getAño());
        txtEndDate.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEndDate.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 5, 5, 5)));
        txtEndDate.setPreferredSize(new Dimension(110, 30));

        cbEndHour = new JComboBox<>(hours);
        cbEndMinute = new JComboBox<>(minutes);
        cbEndHour.setSelectedItem(23);
        cbEndMinute.setSelectedItem(59);
        
        JPanel pnlEndDateTime = new JPanel();
        pnlEndDateTime.setLayout(new BoxLayout(pnlEndDateTime, BoxLayout.X_AXIS));
        pnlEndDateTime.setOpaque(false);
        pnlEndDateTime.add(txtEndDate);
        pnlEndDateTime.add(Box.createHorizontalStrut(10));
        pnlEndDateTime.add(new JLabel("H:"));
        pnlEndDateTime.add(Box.createHorizontalStrut(5));
        pnlEndDateTime.add(cbEndHour);
        pnlEndDateTime.add(Box.createHorizontalStrut(10));
        pnlEndDateTime.add(new JLabel("M:"));
        pnlEndDateTime.add(Box.createHorizontalStrut(5));
        pnlEndDateTime.add(cbEndMinute);

        commonPanel.add(createFormRow("End Date:", pnlEndDateTime));

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
                int startHour = (Integer) cbStartHour.getSelectedItem();
                int startMinute = (Integer) cbStartMinute.getSelectedItem();
                DateTimeSimulado inicio = new DateTimeSimulado(
                        Integer.parseInt(startParts[2].trim()), 
                        Integer.parseInt(startParts[1].trim()), 
                        Integer.parseInt(startParts[0].trim()), 
                        startHour, startMinute, 0);
                
                String[] endParts = txtEndDate.getText().trim().split("/");
                if (endParts.length != 3) throw new IllegalArgumentException("Invalid End Date format");
                int endHour = (Integer) cbEndHour.getSelectedItem();
                int endMinute = (Integer) cbEndMinute.getSelectedItem();
                DateTimeSimulado fin = new DateTimeSimulado(
                        Integer.parseInt(endParts[2].trim()), 
                        Integer.parseInt(endParts[1].trim()), 
                        Integer.parseInt(endParts[0].trim()), 
                        endHour, endMinute, 59);
                        
                if (inicio.dateTimeEnSegundos() > fin.dateTimeEnSegundos()) {
                    throw new IllegalArgumentException("Start Date cannot be after End Date.");
                }

                String type = (String) comboType.getSelectedItem();
                int perc = 0, threshPerc = 0, buyQty = 0, recvQty = 0, giftId = 0, giftQty = 0;
                double thresh = 0.0, giftThresh = 0.0;

                if (type.contains("Percentage")) {
                    perc = Integer.parseInt(txtPercentage.getText().trim());
                } else if (type.contains("Threshold")) {
                    thresh = Double.parseDouble(txtThreshold.getText().trim());
                    threshPerc = Integer.parseInt(txtThreshPerc.getText().trim());
                } else if (type.contains("Quantity")) {
                    buyQty = Integer.parseInt(txtBuyQty.getText().trim());
                    recvQty = Integer.parseInt(txtReceiveQty.getText().trim());
                } else if (type.contains("Gift")) {
                    giftThresh = Double.parseDouble(txtGiftThresh.getText().trim());
                    giftId = Integer.parseInt(txtGiftId.getText().trim());
                    giftQty = Integer.parseInt(txtGiftQty.getText().trim());
                }

                ctrl.confirmarDescuentosMulti(productos, oldDiscount, type, inicio, fin, perc, thresh, threshPerc, buyQty, recvQty, giftThresh, giftId, giftQty);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnConfirm);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        
        if (oldDiscount != null) {
            prefillData(oldDiscount);
        }
        updateDynamicFields();
    }

    /**
     * Creates the form row.
     *
     * @param labelText the label text
     * @param component the component
     * @return the j panel
     */
    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setPreferredSize(new Dimension(160, 30));
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

    /**
     * Update dynamic fields.
     */
    private void updateDynamicFields() {
        CardLayout cl = (CardLayout) dynamicPanel.getLayout();
        cl.show(dynamicPanel, (String) comboType.getSelectedItem());
    }

    /**
     * Prefill data.
     *
     * @param d the d
     */
    private void prefillData(Descuento d) {
        if (d == null) return;
        
        // Rellenar las fechas del descuento existente
        txtStartDate.setText(d.getFechaInicio().getDia() + "/" + d.getFechaInicio().getMes() + "/" + d.getFechaInicio().getAño());
        cbStartHour.setSelectedItem(d.getFechaInicio().getHora());
        cbStartMinute.setSelectedItem(d.getFechaInicio().getMinuto());

        txtEndDate.setText(d.getFechaFin().getDia() + "/" + d.getFechaFin().getMes() + "/" + d.getFechaFin().getAño());
        cbEndHour.setSelectedItem(d.getFechaFin().getHora());
        cbEndMinute.setSelectedItem(d.getFechaFin().getMinuto());

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