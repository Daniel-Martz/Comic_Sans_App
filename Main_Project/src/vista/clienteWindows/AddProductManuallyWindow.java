package vista.clienteWindows;

import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import modelo.producto.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * The Class AddProductManuallyWindow.
 */
public class AddProductManuallyWindow extends JDialog {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The cmb tipo producto. */
    private JComboBox<String> cmbTipoProducto;
    
    /** The txt nombre. */
    private JTextField txtNombre;
    
    /** The txt descripcion. */
    private JTextArea txtDescripcion;
    
    /** The txt precio. */
    private JTextField txtPrecio;
    
    /** The txt stock. */
    private JTextField txtStock;
    
    /** The cmb categoria. */
    private JComboBox<String> cmbCategoria;
    
    /** The foto seleccionada. */
    private File fotoSeleccionada;
    
    /** The lbl foto path. */
    private JLabel lblFotoPath;
    
    /** The btn select foto. */
    private JButton btnSelectFoto;

    /** The panel specifics. */
    // Paneles específicos
    private JPanel panelSpecifics;
    
    /** The txt numero paginas. */
    // Comic fields
    private JTextField txtNumeroPaginas;
    
    /** The txt autor. */
    private JTextField txtAutor;
    
    /** The txt editorial. */
    private JTextField txtEditorial;
    
    /** The txt año publicacion. */
    private JTextField txtAñoPublicacion;
    
    /** The txt numero jugadores. */
    // JuegoDeMesa fields
    private JTextField txtNumeroJugadores;
    
    /** The txt edad minima. */
    private JTextField txtEdadMinima;
    
    /** The txt edad maxima. */
    private JTextField txtEdadMaxima;
    
    /** The cmb tipo juego. */
    private JComboBox<TipoJuegoMesa> cmbTipoJuego;
    
    /** The txt marca. */
    // Figura fields
    private JTextField txtMarca;
    
    /** The txt material. */
    private JTextField txtMaterial;
    
    /** The txt dx. */
    private JTextField txtDx;
    
    /** The txt dy. */
    private JTextField txtDy;
    
    /** The txt dz. */
    private JTextField txtDz;

    /**
     * Instantiates a new adds the product manually window.
     *
     * @param parent the parent
     */
    public AddProductManuallyWindow(JFrame parent) {
        super(parent, "Add product manually", true);
        setSize(550, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Panel: Tipo de Producto
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Tipo de Producto:"));
        cmbTipoProducto = new JComboBox<>(new String[]{"Comic", "BoardGame", "Figure"});
        cmbTipoProducto.addActionListener(e -> updateSpecificFields());
        topPanel.add(cmbTipoProducto);
        mainPanel.add(topPanel);

        // Common fields Panel
        JPanel commonPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        commonPanel.setBorder(BorderFactory.createTitledBorder("General Attributes"));
        
        commonPanel.add(new JLabel("Name:"));
        txtNombre = new JTextField();
        commonPanel.add(txtNombre);
        
        commonPanel.add(new JLabel("Description:"));
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        commonPanel.add(scrollDesc);
        
        commonPanel.add(new JLabel("Price (€):"));
        txtPrecio = new JTextField();
        commonPanel.add(txtPrecio);
        
        commonPanel.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        commonPanel.add(txtStock);
        
        commonPanel.add(new JLabel("Category:"));
        cmbCategoria = new JComboBox<>();
        cargarCategorias();
        commonPanel.add(cmbCategoria);
        
        commonPanel.add(new JLabel("Foto:"));
        JPanel fotoPanel = new JPanel(new BorderLayout(5, 5));
        btnSelectFoto = new JButton("Select");
        lblFotoPath = new JLabel("None selected");
        lblFotoPath.setFont(new Font("SansSerif", Font.ITALIC, 11));
        fotoPanel.add(btnSelectFoto, BorderLayout.WEST);
        fotoPanel.add(lblFotoPath, BorderLayout.CENTER);
        commonPanel.add(fotoPanel);

        btnSelectFoto.addActionListener(e -> seleccionarFoto());

        mainPanel.add(commonPanel);

        panelSpecifics = new JPanel(new CardLayout());
        panelSpecifics.setBorder(BorderFactory.createTitledBorder("Specific Attributes"));
        
        panelSpecifics.add(createComicPanel(), "Comic");
        panelSpecifics.add(createJuegoDeMesaPanel(), "GameBoard");
        panelSpecifics.add(createFiguraPanel(), "Figure");

        mainPanel.add(panelSpecifics);
        
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        // Bottom Panel: Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirm = new JButton("Confirm Changes");
        JButton btnCancel = new JButton("Cancel");
        
        btnConfirm.addActionListener(e -> añadirProducto());
        btnCancel.addActionListener(e -> dispose());
        
        bottomPanel.add(btnConfirm);
        bottomPanel.add(btnCancel);
        add(bottomPanel, BorderLayout.SOUTH);
        
        updateSpecificFields();
    }
    
    /**
     * Cargar categorias.
     */
    private void cargarCategorias() {
        Set<Categoria> categorias = Catalogo.getInstancia().getCategoriasTienda();
        for(Categoria c : categorias) {
            cmbCategoria.addItem(c.getNombre());
        }
    }
    
    /**
     * Update specific fields.
     */
    private void updateSpecificFields() {
        CardLayout cl = (CardLayout) panelSpecifics.getLayout();
        cl.show(panelSpecifics, (String) cmbTipoProducto.getSelectedItem());
    }
    
    /**
     * Creates the comic panel.
     *
     * @return the j panel
     */
    private JPanel createComicPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Number of pages:"));
        txtNumeroPaginas = new JTextField();
        p.add(txtNumeroPaginas);
        
        p.add(new JLabel("Author:"));
        txtAutor = new JTextField();
        p.add(txtAutor);
        
        p.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        p.add(txtEditorial);
        
        p.add(new JLabel("Publication year:"));
        txtAñoPublicacion = new JTextField();
        p.add(txtAñoPublicacion);
        return p;
    }
    
    /**
     * Creates the juego de mesa panel.
     *
     * @return the j panel
     */
    private JPanel createJuegoDeMesaPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Number of players:"));
        txtNumeroJugadores = new JTextField();
        p.add(txtNumeroJugadores);
        
        p.add(new JLabel("Min age:"));
        txtEdadMinima = new JTextField();
        p.add(txtEdadMinima);
        
        p.add(new JLabel("Max age:"));
        txtEdadMaxima = new JTextField();
        p.add(txtEdadMaxima);
        
        p.add(new JLabel("Type of game:"));
        cmbTipoJuego = new JComboBox<>(TipoJuegoMesa.values());
        p.add(cmbTipoJuego);
        return p;
    }
    
    /**
     * Creates the figura panel.
     *
     * @return the j panel
     */
    private JPanel createFiguraPanel() {
        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        p.add(txtMarca);
        
        p.add(new JLabel("Material:"));
        txtMaterial = new JTextField();
        p.add(txtMaterial);
        
        p.add(new JLabel("Dimension X:"));
        txtDx = new JTextField();
        p.add(txtDx);
        
        p.add(new JLabel("Dimension Y:"));
        txtDy = new JTextField();
        p.add(txtDy);
        
        p.add(new JLabel("Dimension Z:"));
        txtDz = new JTextField();
        p.add(txtDz);
        return p;
    }
    
    /**
     * Seleccionar foto.
     */
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            fotoSeleccionada = fileChooser.getSelectedFile();
            lblFotoPath.setText(fotoSeleccionada.getName());
        }
    }
    
    /**
     * Añadir producto.
     */
    private void añadirProducto() {
        try {
            String tipo = (String) cmbTipoProducto.getSelectedItem();
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            
            if (nombre.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "The name and description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            
            if (fotoSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "You must select a photo", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String categoriaNombre = (String) cmbCategoria.getSelectedItem();
            if (categoriaNombre == null) {
                JOptionPane.showMessageDialog(this, "You must select a category", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Categoria categoria = Catalogo.getInstancia().buscarCategoriaPorNombre(categoriaNombre);
            LineaProductoVenta nuevoProducto = null;
            
            if ("Comic".equals(tipo)) {
                int paginas = Integer.parseInt(txtNumeroPaginas.getText().trim());
                String autor = txtAutor.getText().trim();
                String editorial = txtEditorial.getText().trim();
                int año = Integer.parseInt(txtAñoPublicacion.getText().trim());
                
                nuevoProducto = new Comic(nombre, descripcion, fotoSeleccionada, stock, precio, 0, paginas, autor, editorial, año);
                
            } else if ("JuegoDeMesa".equals(tipo)) {
                int jugadores = Integer.parseInt(txtNumeroJugadores.getText().trim());
                int min = Integer.parseInt(txtEdadMinima.getText().trim());
                int max = Integer.parseInt(txtEdadMaxima.getText().trim());
                TipoJuegoMesa tipoJuego = (TipoJuegoMesa) cmbTipoJuego.getSelectedItem();
                
                nuevoProducto = new JuegoDeMesa(nombre, descripcion, fotoSeleccionada, stock, precio, jugadores, min, max, tipoJuego);
                
            } else if ("Figura".equals(tipo)) {
                String marca = txtMarca.getText().trim();
                String material = txtMaterial.getText().trim();
                double dx = Double.parseDouble(txtDx.getText().trim());
                double dy = Double.parseDouble(txtDy.getText().trim());
                double dz = Double.parseDouble(txtDz.getText().trim());
                
                nuevoProducto = new Figura(nombre, descripcion, fotoSeleccionada, stock, precio, 0, marca, material, dx, dy, dz);
            }
            
            if (nuevoProducto != null) {
                nuevoProducto.añadirCategoria(categoria);
                categoria.añadirProductoACategoria(nuevoProducto);
                Catalogo.getInstancia().añadirProducto(nuevoProducto);
                
                JOptionPane.showMessageDialog(this, "Product added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cerrar ventana
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please make sure to enter valid numeric values in the corresponding fields.", "Numeric Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error while adding product " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}