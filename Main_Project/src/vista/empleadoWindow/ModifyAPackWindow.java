package vista.empleadoWindow;

import modelo.aplicacion.Catalogo;
import modelo.producto.LineaProductoVenta;
import modelo.producto.Pack;
import controladores.ControladorModifyPacks;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class ModifyAPackWindow.
 */
public class ModifyAPackWindow extends JDialog {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The pack. */
    private Pack pack;
    
    /** The controlador. */
    private ControladorModifyPacks controlador;
    
    /** The temp pack products. */
    private Map<LineaProductoVenta, Integer> tempPackProducts;

    /** The txt id. */
    private JTextField txtId;
    
    /** The txt name. */
    private JTextField txtName;
    
    /** The txt price. */
    private JTextField txtPrice;
    
    /** The txt description. */
    private JTextField txtDescription;
    
    /** The txt stock. */
    private JTextField txtStock;
    
    /** The lbl photo. */
    private JLabel lblPhoto;
    
    /** The selected photo file. */
    private File selectedPhotoFile;

    /** The panel in pack. */
    private JPanel panelInPack;
    
    /** The panel available. */
    private JPanel panelAvailable;
    
    /** The txt search available. */
    private JTextField txtSearchAvailable;

    /** The checks in pack. */
    private List<JCheckBox> checksInPack = new ArrayList<>();
    
    /** The checks available. */
    private List<JCheckBox> checksAvailable = new ArrayList<>();
    
    /** The spinners in pack. */
    private List<JSpinner> spinnersInPack = new ArrayList<>();
    
    /** The spinners available. */
    private List<JSpinner> spinnersAvailable = new ArrayList<>();
    
    /** The list in pack. */
    private List<LineaProductoVenta> listInPack = new ArrayList<>();
    
    /** The list available. */
    private List<LineaProductoVenta> listAvailable = new ArrayList<>();

    /**
     * Instantiates a new modify A pack window.
     *
     * @param parent the parent
     * @param pack the pack
     * @param ctrl the ctrl
     */
    public ModifyAPackWindow(JFrame parent, Pack pack, ControladorModifyPacks ctrl) {
        super(parent, "Modify Pack", true);
        this.pack = pack;
        this.controlador = ctrl;
        this.tempPackProducts = new HashMap<>(pack.getProductosPack());
        
        setSize(900, 750);
        setLocationRelativeTo(parent);
        initLayout();
        cargarDatosBasicos();
        actualizarListas();
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(54, 119, 189));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Modify Pack");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Body Content
        JPanel bodyContent = new JPanel(new BorderLayout(10, 10));
        bodyContent.setOpaque(false);
        bodyContent.setBorder(new EmptyBorder(15, 15, 15, 15));

        // TOP: Basic Info
        JPanel topInfoPanel = new JPanel(new BorderLayout(15, 0));
        topInfoPanel.setOpaque(false);

        // Photo
        JPanel photoPanel = new JPanel(new BorderLayout(0, 5));
        photoPanel.setOpaque(false);
        photoPanel.setPreferredSize(new Dimension(180, 0));
        lblPhoto = new JLabel("NO PHOTO", SwingConstants.CENTER);
        lblPhoto.setBorder(new LineBorder(Color.GRAY));
        lblPhoto.setBackground(Color.WHITE);
        lblPhoto.setOpaque(true);
        JButton btnChangePhoto = new JButton("Change Photo");
        btnChangePhoto.addActionListener(e -> seleccionarFoto());
        photoPanel.add(lblPhoto, BorderLayout.CENTER);
        photoPanel.add(btnChangePhoto, BorderLayout.SOUTH);
        topInfoPanel.add(photoPanel, BorderLayout.WEST);

        // Fields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        txtId = new JTextField(); txtId.setEditable(false);
        txtName = new JTextField();
        txtPrice = new JTextField();
        txtDescription = new JTextField();
        txtStock = new JTextField();

        fieldsPanel.add(createFormRow("ID:", txtId));
        fieldsPanel.add(createFormRow("NAME:", txtName));
        fieldsPanel.add(createFormRow("DESCRIPTION:", txtDescription));
        fieldsPanel.add(createFormRow("PRICE (€):", txtPrice));
        fieldsPanel.add(createFormRow("STOCK:", txtStock));
        
        topInfoPanel.add(fieldsPanel, BorderLayout.CENTER);
        bodyContent.add(topInfoPanel, BorderLayout.NORTH);

        // CENTER: Two Columns for Products
        JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        columnsPanel.setOpaque(false);

        // Left Column (In Pack)
        JPanel colLeft = new JPanel(new BorderLayout(0, 5));
        colLeft.setBorder(BorderFactory.createTitledBorder("Products in Pack"));
        panelInPack = new JPanel();
        panelInPack.setLayout(new BoxLayout(panelInPack, BoxLayout.Y_AXIS));
        JScrollPane scrollInPack = new JScrollPane(panelInPack);
        colLeft.add(scrollInPack, BorderLayout.CENTER);
        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(178, 34, 34));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> eliminarSeleccionados());
        colLeft.add(btnDelete, BorderLayout.SOUTH);

        // Right Column (Available)
        JPanel colRight = new JPanel(new BorderLayout(0, 5));
        colRight.setBorder(BorderFactory.createTitledBorder("Available Products"));
        
        JPanel searchPanel = new JPanel(new BorderLayout());
        txtSearchAvailable = new JTextField();
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> actualizarListaDisponibles());
        searchPanel.add(txtSearchAvailable, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        colRight.add(searchPanel, BorderLayout.NORTH);

        panelAvailable = new JPanel();
        panelAvailable.setLayout(new BoxLayout(panelAvailable, BoxLayout.Y_AXIS));
        JScrollPane scrollAvail = new JScrollPane(panelAvailable);
        colRight.add(scrollAvail, BorderLayout.CENTER);
        JButton btnAdd = new JButton("Add Selected");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> añadirSeleccionados());
        colRight.add(btnAdd, BorderLayout.SOUTH);

        columnsPanel.add(colLeft);
        columnsPanel.add(colRight);
        bodyContent.add(columnsPanel, BorderLayout.CENTER);

        mainPanel.add(bodyContent, BorderLayout.CENTER);

        // BOTTOM: Confirm / Cancel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        JButton btnConfirm = new JButton("Confirm Changes");
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> confirmar());
        bottomPanel.add(btnCancel);
        bottomPanel.add(btnConfirm);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * Creates the form row.
     *
     * @param labelText the label text
     * @param component the component
     * @return the j panel
     */
    private JPanel createFormRow(String labelText, JTextField component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setPreferredSize(new Dimension(100, 30));
        row.add(lbl, BorderLayout.WEST);
        component.setBorder(new LineBorder(Color.GRAY));
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    /**
     * Cargar datos basicos.
     */
    private void cargarDatosBasicos() {
        txtId.setText(String.valueOf(pack.getID()));
        txtName.setText(pack.getNombre());
        txtDescription.setText(pack.getDescripcion());
        txtPrice.setText(String.valueOf(pack.getPrecio()));
        txtStock.setText(String.valueOf(pack.getStock()));
        if (pack.getFoto() != null && pack.getFoto().exists()) {
            ImageIcon icon = new ImageIcon(pack.getFoto().getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(img));
            lblPhoto.setText("");
        }
    }

    /**
     * Seleccionar foto.
     */
    private void seleccionarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(selectedPhotoFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(img));
            lblPhoto.setText("");
        }
    }

    /**
     * Actualizar listas.
     */
    private void actualizarListas() {
        actualizarListaPack();
        actualizarListaDisponibles();
    }

    /**
     * Actualizar lista pack.
     */
    private void actualizarListaPack() {
        panelInPack.removeAll();
        checksInPack.clear();
        spinnersInPack.clear();
        listInPack.clear();
        
        for (Map.Entry<LineaProductoVenta, Integer> entry : tempPackProducts.entrySet()) {
            LineaProductoVenta p = entry.getKey();
            int qty = entry.getValue();
            
            JPanel row = new JPanel(new BorderLayout());
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            JCheckBox chk = new JCheckBox(p.getNombre() + " (x" + qty + ")");
            JSpinner spn = new JSpinner(new SpinnerNumberModel(1, 1, qty, 1));
            checksInPack.add(chk);
            spinnersInPack.add(spn);
            listInPack.add(p);
            row.add(chk, BorderLayout.CENTER);
            row.add(spn, BorderLayout.EAST);
            panelInPack.add(row);
        }
        panelInPack.revalidate();
        panelInPack.repaint();
    }

    /**
     * Actualizar lista disponibles.
     */
    private void actualizarListaDisponibles() {
        panelAvailable.removeAll();
        checksAvailable.clear();
        spinnersAvailable.clear();
        listAvailable.clear();

        String prompt = txtSearchAvailable.getText().toLowerCase();
        for (LineaProductoVenta p : Catalogo.getInstancia().getProductosNuevos()) {
            if (!(p instanceof Pack)) {
                if (prompt.isEmpty() || p.getNombre().toLowerCase().contains(prompt)) {
                    JPanel row = new JPanel(new BorderLayout());
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                    JCheckBox chk = new JCheckBox(p.getNombre());
                    JSpinner spn = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
                    checksAvailable.add(chk);
                    spinnersAvailable.add(spn);
                    listAvailable.add(p);
                    row.add(chk, BorderLayout.CENTER);
                    row.add(spn, BorderLayout.EAST);
                    panelAvailable.add(row);
                }
            }
        }
        panelAvailable.revalidate();
        panelAvailable.repaint();
    }

    /**
     * Añadir seleccionados.
     */
    private void añadirSeleccionados() {
        for (int i = 0; i < checksAvailable.size(); i++) {
            if (checksAvailable.get(i).isSelected()) {
                LineaProductoVenta p = listAvailable.get(i);
                int qtyToAdd = (Integer) spinnersAvailable.get(i).getValue();
                tempPackProducts.put(p, tempPackProducts.getOrDefault(p, 0) + qtyToAdd);
                checksAvailable.get(i).setSelected(false);
                spinnersAvailable.get(i).setValue(1);
            }
        }
        actualizarListaPack();
    }

    /**
     * Eliminar seleccionados.
     */
    private void eliminarSeleccionados() {
        for (int i = 0; i < checksInPack.size(); i++) {
            if (checksInPack.get(i).isSelected()) {
                LineaProductoVenta p = listInPack.get(i);
                int qty = tempPackProducts.get(p);
                int qtyToRemove = (Integer) spinnersInPack.get(i).getValue();
                if (qty > qtyToRemove) {
                    tempPackProducts.put(p, qty - qtyToRemove);
                } else {
                    tempPackProducts.remove(p);
                }
            }
        }
        actualizarListaPack();
    }

    /**
     * Confirmar.
     */
    private void confirmar() {
        try {
            String name = txtName.getText();
            String desc = txtDescription.getText();
            double price = Double.parseDouble(txtPrice.getText().replace(",", "."));
            int stock = Integer.parseInt(txtStock.getText());
            
            if (tempPackProducts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "The pack must have at least one product.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            controlador.confirmarModificacion(pack, name, desc, price, stock, selectedPhotoFile, tempPackProducts);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price or stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}