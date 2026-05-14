package vista.empleadoWindow;

import modelo.aplicacion.Aplicacion;
import modelo.categoria.Categoria;
import modelo.producto.Comic;
import modelo.producto.Figura;
import modelo.producto.JuegoDeMesa;
import modelo.producto.TipoJuegoMesa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AddProductManuallyWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    private final Color BG_COLOR = new Color(162, 187, 210);      
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    private JButton btnBack;
    private JButton btnConfirmChanges;
    private JButton btnChangePhoto;

    private JLabel lblPhoto;
    
    // Selector de tipo
    private JComboBox<String> comboProductType;

    // Campos comunes
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtDescription;
    private JTextField txtStock;
    private JButton btnCategories;
    
    private Set<Categoria> categoriasSeleccionadas = new HashSet<>();
    
    // Contenedores dinámicos
    private JPanel formPanel;
    private JPanel specificFieldsPanel;

    // Campos específicos Comic
    private JTextField txtNumPaginas;
    private JTextField txtAutor;
    private JTextField txtEditorial;
    private JTextField txtAño;

    // Campos específicos Figura
    private JTextField txtMarca;
    private JTextField txtMaterial;
    private JTextField txtDimX;
    private JTextField txtDimY;
    private JTextField txtDimZ;

    // Campos específicos JuegoDeMesa
    private JTextField txtNumJugadores;
    private JTextField txtEdadMin;
    private JTextField txtEdadMax;
    private JComboBox<TipoJuegoMesa> comboTipoJuego;
    
    private File selectedPhotoFile = null;

    public AddProductManuallyWindow(JFrame parent) {
        super(parent, "Add Product Manually", true); // Ventana modal
        setSize(1050, 750);
        setLocationRelativeTo(parent);
        initLayout();
        actualizarCamposEspecificos();
    }

    private void initLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // Header
        JPanel customHeader = new JPanel(new BorderLayout());
        customHeader.setBackground(BANNER_MAIN_COLOR);
        customHeader.setBorder(new EmptyBorder(10, 15, 10, 15));

        btnBack = new JButton("BACK 🔙"); 
        btnBack.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        btnBack.setForeground(Color.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel("Add Product", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        customHeader.add(btnBack, BorderLayout.WEST);
        customHeader.add(lblTitle, BorderLayout.CENTER);
        
        JLabel placeholder = new JLabel("         ");
        customHeader.add(placeholder, BorderLayout.EAST);

        mainPanel.add(customHeader, BorderLayout.NORTH);

        // Body
        JPanel bodyContent = new JPanel(new BorderLayout(20, 20));
        bodyContent.setBackground(BG_COLOR);
        bodyContent.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Area Izquierda: Foto
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(350, 0));
        
        JPanel photoPlaceholder = new JPanel(new BorderLayout());
        photoPlaceholder.setBackground(Color.WHITE);
        photoPlaceholder.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        
        lblPhoto = new JLabel("<html><center>X<br>ADD PHOTO</center></html>", SwingConstants.CENTER);
        lblPhoto.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblPhoto.setForeground(Color.LIGHT_GRAY);
        photoPlaceholder.add(lblPhoto, BorderLayout.CENTER);

        leftPanel.add(photoPlaceholder, BorderLayout.CENTER);
        
        btnChangePhoto = new JButton("ADD / CHANGE PHOTO");
        btnChangePhoto.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnChangePhoto.setBackground(BANNER_MAIN_COLOR);
        btnChangePhoto.setForeground(Color.WHITE);
        btnChangePhoto.setFocusPainted(false);
        btnChangePhoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePhoto.addActionListener(e -> seleccionarFoto());
        leftPanel.add(btnChangePhoto, BorderLayout.SOUTH);

        bodyContent.add(leftPanel, BorderLayout.WEST);

        // Area Derecha: Formulario (Scrollable)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setOpaque(false);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        comboProductType = new JComboBox<>(new String[]{"Comic", "Figure", "Board Game"});
        comboProductType.addActionListener(e -> actualizarCamposEspecificos());

        txtId = new JTextField();
        txtName = new JTextField();
        txtPrice = new JTextField();
        txtDescription = new JTextField();
        txtStock = new JTextField();
        
        btnCategories = new JButton("Select Categories");
        btnCategories.addActionListener(e -> abrirSelectorCategorias());

        formPanel.add(createFormRow("PRODUCT TYPE:", comboProductType, true));
        formPanel.add(createFormRow("ID:", txtId, true));
        formPanel.add(createFormRow("NAME:", txtName, true));
        formPanel.add(createFormRow("PRICE:", txtPrice, true));
        formPanel.add(createFormRow("DESCRIPTION:", txtDescription, true));
        formPanel.add(createFormRow("NUMBER STOCK:", txtStock, true));
        formPanel.add(createFormRow("CATEGORIES:", btnCategories, true));

        // Panel de campos específicos
        specificFieldsPanel = new JPanel();
        specificFieldsPanel.setLayout(new BoxLayout(specificFieldsPanel, BoxLayout.Y_AXIS));
        specificFieldsPanel.setOpaque(false);

        // Boton de accion
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        btnConfirmChanges = new JButton("Confirm Changes");
        btnConfirmChanges.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnConfirmChanges.setBackground(new Color(46, 204, 113));
        btnConfirmChanges.setForeground(Color.WHITE);
        btnConfirmChanges.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmChanges.setBorder(new EmptyBorder(10, 20, 10, 20));
        bottomPanel.add(btnConfirmChanges);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(formPanel, BorderLayout.NORTH);
        contentPanel.add(specificFieldsPanel, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        bodyContent.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(bodyContent, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createFormRow(String labelText, JComponent component, boolean editable) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setPreferredSize(new Dimension(150, 30));
        row.add(lbl, BorderLayout.WEST);

        if (component instanceof JTextField txtField) {
            txtField.setEditable(editable);
            txtField.setBackground(editable ? Color.WHITE : new Color(240, 240, 240));
            txtField.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.GRAY, 1),
                    new EmptyBorder(0, 5, 0, 5)));
        }
        
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    private void actualizarCamposEspecificos() {
        specificFieldsPanel.removeAll();
        String type = (String) comboProductType.getSelectedItem();

        if ("Comic".equals(type)) {
            txtNumPaginas = new JTextField();
            txtAutor = new JTextField();
            txtEditorial = new JTextField();
            txtAño = new JTextField();
            
            specificFieldsPanel.add(createFormRow("PAGES:", txtNumPaginas, true));
            specificFieldsPanel.add(createFormRow("AUTHOR:", txtAutor, true));
            specificFieldsPanel.add(createFormRow("EDITORIAL:", txtEditorial, true));
            specificFieldsPanel.add(createFormRow("YEAR:", txtAño, true));
            
        } else if ("Figure".equals(type)) {
            txtMarca = new JTextField();
            txtMaterial = new JTextField();
            txtDimX = new JTextField();
            txtDimY = new JTextField();
            txtDimZ = new JTextField();
            
            specificFieldsPanel.add(createFormRow("BRAND:", txtMarca, true));
            specificFieldsPanel.add(createFormRow("MATERIAL:", txtMaterial, true));
            specificFieldsPanel.add(createFormRow("DIM X (cm):", txtDimX, true));
            specificFieldsPanel.add(createFormRow("DIM Y (cm):", txtDimY, true));
            specificFieldsPanel.add(createFormRow("DIM Z (cm):", txtDimZ, true));
            
        } else if ("Board Game".equals(type)) {
            txtNumJugadores = new JTextField();
            txtEdadMin = new JTextField();
            txtEdadMax = new JTextField();
            
            comboTipoJuego = new JComboBox<>(TipoJuegoMesa.values());
            
            specificFieldsPanel.add(createFormRow("PLAYERS:", txtNumJugadores, true));
            specificFieldsPanel.add(createFormRow("MIN AGE:", txtEdadMin, true));
            specificFieldsPanel.add(createFormRow("MAX AGE:", txtEdadMax, true));
            specificFieldsPanel.add(createFormRow("GAME TYPE:", comboTipoJuego, true));
        }

        specificFieldsPanel.revalidate();
        specificFieldsPanel.repaint();
    }
    
    private void actualizarTextoBotonCategorias() {
        if (categoriasSeleccionadas.isEmpty()) {
            btnCategories.setText("Select Categories");
        } else {
            btnCategories.setText(categoriasSeleccionadas.size() + " Selected");
        }
    }

    private void abrirSelectorCategorias() {
        Set<Categoria> todas = Aplicacion.getInstancia().getCatalogo().getCategoriasTienda();
        
        JDialog dialog = new JDialog(this, "Select Categories", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
        
        JPanel pnlChecks = new JPanel();
        pnlChecks.setLayout(new BoxLayout(pnlChecks, BoxLayout.Y_AXIS));
        pnlChecks.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        java.util.List<JCheckBox> checks = new java.util.ArrayList<>();
        
        for (Categoria c : todas) {
            JCheckBox chk = new JCheckBox(c.getNombre());
            if (categoriasSeleccionadas.contains(c)) {
                chk.setSelected(true);
            }
            checks.add(chk);
            pnlChecks.add(chk);
        }
        
        JScrollPane scroll = new JScrollPane(pnlChecks);
        scroll.setPreferredSize(new Dimension(300, 300));
        dialog.add(scroll, BorderLayout.CENTER);
        
        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(e -> {
            categoriasSeleccionadas.clear();
            for (int i = 0; i < checks.size(); i++) {
                if (checks.get(i).isSelected()) {
                    int idx = 0;
                    for (Categoria cat : todas) {
                        if (idx == i) {
                            categoriasSeleccionadas.add(cat);
                            break;
                        }
                        idx++;
                    }
                }
            }
            actualizarTextoBotonCategorias();
            dialog.dispose();
        });
        
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.add(btnOk);
        dialog.add(pnlBotones, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void cargarImagen(String path) {
        if (path != null && !path.isEmpty()) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(path); 
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(imgEscalada));
                lblPhoto.setText(""); // Ocultar el texto de placeholder
            } catch (Exception ex) {
                mostrarPlaceholderFoto();
            }
        } else {
            mostrarPlaceholderFoto();
        }
    }

    private void mostrarPlaceholderFoto() {
        lblPhoto.setIcon(null);
        lblPhoto.setText("<html><center>X<br>ADD PHOTO</center></html>");
    }
    
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png, gif)", "jpg", "png", "gif", "jpeg"));
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = fileChooser.getSelectedFile();
            cargarImagen(selectedPhotoFile.getAbsolutePath());
        }
    }

    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnConfirmChanges() { return btnConfirmChanges; }
    
    public String getProductType() { return (String) comboProductType.getSelectedItem(); }
    
    // Getters comunes
    public String getNewId() { return txtId.getText(); }
    public String getNewName() { return txtName.getText(); }
    public String getNewPrice() { return txtPrice.getText(); }
    public String getNewDescription() { return txtDescription.getText(); }
    public String getNewStock() { return txtStock.getText(); }
    public Set<Categoria> getCategoriasSeleccionadas() { return categoriasSeleccionadas; }
    public File getSelectedPhotoFile() { return selectedPhotoFile; }
    
    // Getters específicos
    public String getNewNumPaginas() { return txtNumPaginas != null ? txtNumPaginas.getText() : null; }
    public String getNewAutor() { return txtAutor != null ? txtAutor.getText() : null; }
    public String getNewEditorial() { return txtEditorial != null ? txtEditorial.getText() : null; }
    public String getNewAño() { return txtAño != null ? txtAño.getText() : null; }
    
    public String getNewMarca() { return txtMarca != null ? txtMarca.getText() : null; }
    public String getNewMaterial() { return txtMaterial != null ? txtMaterial.getText() : null; }
    public String getNewDimX() { return txtDimX != null ? txtDimX.getText() : null; }
    public String getNewDimY() { return txtDimY != null ? txtDimY.getText() : null; }
    public String getNewDimZ() { return txtDimZ != null ? txtDimZ.getText() : null; }
    
    public String getNewNumJugadores() { return txtNumJugadores != null ? txtNumJugadores.getText() : null; }
    public String getNewEdadMin() { return txtEdadMin != null ? txtEdadMin.getText() : null; }
    public String getNewEdadMax() { return txtEdadMax != null ? txtEdadMax.getText() : null; }
    public TipoJuegoMesa getNewTipoJuego() { return comboTipoJuego != null ? (TipoJuegoMesa) comboTipoJuego.getSelectedItem() : null; }
}