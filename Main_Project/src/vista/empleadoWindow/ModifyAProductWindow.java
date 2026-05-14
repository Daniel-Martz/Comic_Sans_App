package vista.empleadoWindow;

import modelo.aplicacion.Aplicacion;
import modelo.categoria.Categoria;
import modelo.producto.Comic;
import modelo.producto.Figura;
import modelo.producto.JuegoDeMesa;
import modelo.producto.LineaProductoVenta;
import modelo.producto.TipoJuegoMesa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class ModifyAProductWindow.
 */
public class ModifyAProductWindow extends JDialog {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);      
    
    /** The banner main color. */
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189); 

    /** The btn back. */
    private JButton btnBack;
    
    /** The btn confirm changes. */
    private JButton btnConfirmChanges;
    
    /** The btn change photo. */
    private JButton btnChangePhoto;

    /** The lbl photo. */
    private JLabel lblPhoto;
    
    // Campos comunes
    /** The txt name. */
    private JTextField txtName;
    
    /** The txt price. */
    private JTextField txtPrice;
    
    /** The txt description. */
    private JTextField txtDescription;
    
    /** The txt stock. */
    private JTextField txtStock;
    
    /** The btn categories. */
    private JButton btnCategories;
    
    /** The categorias seleccionadas. */
    private Set<Categoria> categoriasSeleccionadas = new HashSet<>();
    
    /** The form panel. */
    // Contenedores dinámicos
    private JPanel formPanel;
    
    /** The specific fields panel. */
    private JPanel specificFieldsPanel;

    /** The txt num paginas. */
    // Campos específicos Comic
    private JTextField txtNumPaginas;
    
    /** The txt autor. */
    private JTextField txtAutor;
    
    /** The txt editorial. */
    private JTextField txtEditorial;
    
    /** The txt año. */
    private JTextField txtAño;

    /** The txt marca. */
    // Campos específicos Figura
    private JTextField txtMarca;
    
    /** The txt material. */
    private JTextField txtMaterial;
    
    /** The txt dim X. */
    private JTextField txtDimX;
    
    /** The txt dim Y. */
    private JTextField txtDimY;
    
    /** The txt dim Z. */
    private JTextField txtDimZ;

    /** The txt num jugadores. */
    // Campos específicos JuegoDeMesa
    private JTextField txtNumJugadores;
    
    /** The txt edad min. */
    private JTextField txtEdadMin;
    
    /** The txt edad max. */
    private JTextField txtEdadMax;
    
    /** The combo tipo juego. */
    private JComboBox<TipoJuegoMesa> comboTipoJuego;
    
    /** The current product. */
    private LineaProductoVenta currentProduct;
    
    /** The selected photo file. */
    private File selectedPhotoFile = null;

    /**
     * Instantiates a new modify A product window.
     *
     * @param parent the parent
     */
    public ModifyAProductWindow(JFrame parent) {
        super(parent, "Modify Product", true); // Ventana modal
        setSize(1050, 850);
        setLocationRelativeTo(parent);
        initLayout();
    }

    /**
     * Inits the layout.
     */
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

        JLabel lblTitle = new JLabel("Modify Product", SwingConstants.CENTER);
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
        
        lblPhoto = new JLabel("<html><center>X<br>NO PHOTO</center></html>", SwingConstants.CENTER);
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

        txtName = new JTextField();
        txtPrice = new JTextField();
        txtDescription = new JTextField();
        txtStock = new JTextField();
        
        btnCategories = new JButton("Select Categories");
        btnCategories.addActionListener(e -> abrirSelectorCategorias());

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

    /**
     * Creates the form row.
     *
     * @param labelText the label text
     * @param component the component
     * @param editable the editable
     * @return the j panel
     */
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

    /**
     * Cargar producto.
     *
     * @param p the p
     */
    public void cargarProducto(LineaProductoVenta p) {
        this.currentProduct = p;
        this.selectedPhotoFile = null;
        this.categoriasSeleccionadas = new HashSet<>(p.getCategorias());
        
        formPanel.removeAll();
        specificFieldsPanel.removeAll();

        // Campos comunes
        formPanel.add(createFormRow("NAME:", txtName, true));
        formPanel.add(createFormRow("PRICE:", txtPrice, true));
        formPanel.add(createFormRow("DESCRIPTION:", txtDescription, true));
        formPanel.add(createFormRow("NUMBER STOCK:", txtStock, true));
        formPanel.add(createFormRow("CATEGORIES:", btnCategories, true));
        
        txtName.setText(p.getNombre());
        txtPrice.setText(String.valueOf(p.getPrecio()));
        txtDescription.setText(p.getDescripcion() != null ? p.getDescripcion() : "");
        txtStock.setText(String.valueOf(p.getStock()));
        actualizarTextoBotonCategorias();

        // Campos específicos
        if (p instanceof Comic) {
            Comic c = (Comic) p;
            txtNumPaginas = new JTextField(String.valueOf(c.getNumeroPaginas()));
            txtAutor = new JTextField(c.getAutor());
            txtEditorial = new JTextField(c.getEditorial());
            txtAño = new JTextField(String.valueOf(c.getAñoPublicacion()));
            
            specificFieldsPanel.add(createFormRow("PAGES:", txtNumPaginas, true));
            specificFieldsPanel.add(createFormRow("AUTHOR:", txtAutor, true));
            specificFieldsPanel.add(createFormRow("EDITORIAL:", txtEditorial, true));
            specificFieldsPanel.add(createFormRow("YEAR:", txtAño, true));
            
        } else if (p instanceof Figura) {
            Figura f = (Figura) p;
            txtMarca = new JTextField(f.getMarca());
            txtMaterial = new JTextField(f.getMaterial());
            txtDimX = new JTextField(String.valueOf(f.getDimensionX()));
            txtDimY = new JTextField(String.valueOf(f.getDimensionY()));
            txtDimZ = new JTextField(String.valueOf(f.getDimensionZ()));
            
            specificFieldsPanel.add(createFormRow("BRAND:", txtMarca, true));
            specificFieldsPanel.add(createFormRow("MATERIAL:", txtMaterial, true));
            specificFieldsPanel.add(createFormRow("DIM X (cm):", txtDimX, true));
            specificFieldsPanel.add(createFormRow("DIM Y (cm):", txtDimY, true));
            specificFieldsPanel.add(createFormRow("DIM Z (cm):", txtDimZ, true));
            
        } else if (p instanceof JuegoDeMesa) {
            JuegoDeMesa j = (JuegoDeMesa) p;
            txtNumJugadores = new JTextField(String.valueOf(j.getNumeroJugadores()));
            txtEdadMin = new JTextField(String.valueOf(j.getEdadMinima()));
            txtEdadMax = new JTextField(String.valueOf(j.getEdadMaxima()));
            
            comboTipoJuego = new JComboBox<>(TipoJuegoMesa.values());
            comboTipoJuego.setSelectedItem(j.getTipoJuegoDeMesa());
            
            specificFieldsPanel.add(createFormRow("PLAYERS:", txtNumJugadores, true));
            specificFieldsPanel.add(createFormRow("MIN AGE:", txtEdadMin, true));
            specificFieldsPanel.add(createFormRow("MAX AGE:", txtEdadMax, true));
            specificFieldsPanel.add(createFormRow("GAME TYPE:", comboTipoJuego, true));
        }
        
        cargarImagen(p.getFoto() != null ? p.getFoto().getPath() : null);
        
        formPanel.revalidate();
        formPanel.repaint();
        specificFieldsPanel.revalidate();
        specificFieldsPanel.repaint();
    }
    
    /**
     * Actualizar texto boton categorias.
     */
    private void actualizarTextoBotonCategorias() {
        if (categoriasSeleccionadas.isEmpty()) {
            btnCategories.setText("Select Categories");
        } else {
            btnCategories.setText(categoriasSeleccionadas.size() + " Selected");
        }
    }

    /**
     * Abrir selector categorias.
     */
    private void abrirSelectorCategorias() {
        Set<Categoria> todas = Aplicacion.getInstancia().getCatalogo().getCategoriasTienda();
        
        if (todas == null || todas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no categories available to select.", "No Categories", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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

    /**
     * Cargar imagen.
     *
     * @param path the path
     */
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

    /**
     * Mostrar placeholder foto.
     */
    private void mostrarPlaceholderFoto() {
        lblPhoto.setIcon(null);
        lblPhoto.setText("<html><center>X<br>ADD PHOTO</center></html>");
    }
    
    /**
     * Seleccionar foto.
     */
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images (jpg, png, gif)", "jpg", "png", "gif", "jpeg"));
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            selectedPhotoFile = fileChooser.getSelectedFile();
            cargarImagen(selectedPhotoFile.getAbsolutePath());
        }
    }

    /**
     * Gets the btn back.
     *
     * @return the btn back
     */
    public JButton getBtnBack() { return btnBack; }
    
    /**
     * Gets the btn confirm changes.
     *
     * @return the btn confirm changes
     */
    public JButton getBtnConfirmChanges() { return btnConfirmChanges; }
    
    /**
     * Gets the current product.
     *
     * @return the current product
     */
    public LineaProductoVenta getCurrentProduct() { return currentProduct; }
    
    
    // Getters comunes
    /**
     * Gets the new name.
     *
     * @return the new name
     */
    public String getNewName() { return txtName.getText(); }
    
    /**
     * Gets the new price.
     *
     * @return the new price
     */
    public String getNewPrice() { return txtPrice.getText(); }
    
    /**
     * Gets the new description.
     *
     * @return the new description
     */
    public String getNewDescription() { return txtDescription.getText(); }
    
    /**
     * Gets the new stock.
     *
     * @return the new stock
     */
    public String getNewStock() { return txtStock.getText(); }
    
    /**
     * Gets the categorias seleccionadas.
     *
     * @return the categorias seleccionadas
     */
    public Set<Categoria> getCategoriasSeleccionadas() { return categoriasSeleccionadas; }
    
    /**
     * Gets the selected photo file.
     *
     * @return the selected photo file
     */
    public File getSelectedPhotoFile() { return selectedPhotoFile; }
    
    /**
     * Gets the new num paginas.
     *
     * @return the new num paginas
     */
    // Getters específicos
    public String getNewNumPaginas() { return txtNumPaginas != null ? txtNumPaginas.getText() : null; }
    
    /**
     * Gets the new autor.
     *
     * @return the new autor
     */
    public String getNewAutor() { return txtAutor != null ? txtAutor.getText() : null; }
    
    /**
     * Gets the new editorial.
     *
     * @return the new editorial
     */
    public String getNewEditorial() { return txtEditorial != null ? txtEditorial.getText() : null; }
    
    /**
     * Gets the new año.
     *
     * @return the new año
     */
    public String getNewAño() { return txtAño != null ? txtAño.getText() : null; }
    
    /**
     * Gets the new marca.
     *
     * @return the new marca
     */
    public String getNewMarca() { return txtMarca != null ? txtMarca.getText() : null; }
    
    /**
     * Gets the new material.
     *
     * @return the new material
     */
    public String getNewMaterial() { return txtMaterial != null ? txtMaterial.getText() : null; }
    
    /**
     * Gets the new dim X.
     *
     * @return the new dim X
     */
    public String getNewDimX() { return txtDimX != null ? txtDimX.getText() : null; }
    
    /**
     * Gets the new dim Y.
     *
     * @return the new dim Y
     */
    public String getNewDimY() { return txtDimY != null ? txtDimY.getText() : null; }
    
    /**
     * Gets the new dim Z.
     *
     * @return the new dim Z
     */
    public String getNewDimZ() { return txtDimZ != null ? txtDimZ.getText() : null; }
    
    /**
     * Gets the new num jugadores.
     *
     * @return the new num jugadores
     */
    public String getNewNumJugadores() { return txtNumJugadores != null ? txtNumJugadores.getText() : null; }
    
    /**
     * Gets the new edad min.
     *
     * @return the new edad min
     */
    public String getNewEdadMin() { return txtEdadMin != null ? txtEdadMin.getText() : null; }
    
    /**
     * Gets the new edad max.
     *
     * @return the new edad max
     */
    public String getNewEdadMax() { return txtEdadMax != null ? txtEdadMax.getText() : null; }
    
    /**
     * Gets the new tipo juego.
     *
     * @return the new tipo juego
     */
    public TipoJuegoMesa getNewTipoJuego() { return comboTipoJuego != null ? (TipoJuegoMesa) comboTipoJuego.getSelectedItem() : null; }
}