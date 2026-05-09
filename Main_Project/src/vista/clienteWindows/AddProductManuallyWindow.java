package vista.clienteWindows;

import modelo.aplicacion.Catalogo;
import modelo.categoria.Categoria;
import modelo.producto.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Set;

public class AddProductManuallyWindow extends JDialog {
    private static final long serialVersionUID = 1L;

    private JComboBox<String> cmbTipoProducto;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JComboBox<String> cmbCategoria;
    
    private File fotoSeleccionada;
    private JLabel lblFotoPath;
    private JButton btnSelectFoto;

    // Paneles específicos
    private JPanel panelSpecifics;
    
    // Comic fields
    private JTextField txtNumeroPaginas;
    private JTextField txtAutor;
    private JTextField txtEditorial;
    private JTextField txtAñoPublicacion;
    
    // JuegoDeMesa fields
    private JTextField txtNumeroJugadores;
    private JTextField txtEdadMinima;
    private JTextField txtEdadMaxima;
    private JComboBox<TipoJuegoMesa> cmbTipoJuego;
    
    // Figura fields
    private JTextField txtMarca;
    private JTextField txtMaterial;
    private JTextField txtDx;
    private JTextField txtDy;
    private JTextField txtDz;

    public AddProductManuallyWindow(JFrame parent) {
        super(parent, "Añadir Producto Manualmente", true);
        setSize(550, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Panel: Tipo de Producto
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Tipo de Producto:"));
        cmbTipoProducto = new JComboBox<>(new String[]{"Comic", "JuegoDeMesa", "Figura"});
        cmbTipoProducto.addActionListener(e -> updateSpecificFields());
        topPanel.add(cmbTipoProducto);
        mainPanel.add(topPanel);

        // Common fields Panel
        JPanel commonPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        commonPanel.setBorder(BorderFactory.createTitledBorder("Datos Generales"));
        
        commonPanel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        commonPanel.add(txtNombre);
        
        commonPanel.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        commonPanel.add(scrollDesc);
        
        commonPanel.add(new JLabel("Precio (€):"));
        txtPrecio = new JTextField();
        commonPanel.add(txtPrecio);
        
        commonPanel.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        commonPanel.add(txtStock);
        
        commonPanel.add(new JLabel("Categoría:"));
        cmbCategoria = new JComboBox<>();
        cargarCategorias();
        commonPanel.add(cmbCategoria);
        
        commonPanel.add(new JLabel("Foto:"));
        JPanel fotoPanel = new JPanel(new BorderLayout(5, 5));
        btnSelectFoto = new JButton("Seleccionar");
        lblFotoPath = new JLabel("Ninguna seleccionada");
        lblFotoPath.setFont(new Font("SansSerif", Font.ITALIC, 11));
        fotoPanel.add(btnSelectFoto, BorderLayout.WEST);
        fotoPanel.add(lblFotoPath, BorderLayout.CENTER);
        commonPanel.add(fotoPanel);

        btnSelectFoto.addActionListener(e -> seleccionarFoto());

        mainPanel.add(commonPanel);

        // Specific fields Panel (usamos CardLayout para cambiar entre atributos)
        panelSpecifics = new JPanel(new CardLayout());
        panelSpecifics.setBorder(BorderFactory.createTitledBorder("Atributos Específicos"));
        
        panelSpecifics.add(createComicPanel(), "Comic");
        panelSpecifics.add(createJuegoDeMesaPanel(), "JuegoDeMesa");
        panelSpecifics.add(createFiguraPanel(), "Figura");

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
    
    private void cargarCategorias() {
        Set<Categoria> categorias = Catalogo.getInstancia().getCategoriasTienda();
        for(Categoria c : categorias) {
            cmbCategoria.addItem(c.getNombre());
        }
    }
    
    private void updateSpecificFields() {
        CardLayout cl = (CardLayout) panelSpecifics.getLayout();
        cl.show(panelSpecifics, (String) cmbTipoProducto.getSelectedItem());
    }
    
    private JPanel createComicPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Número de Páginas:"));
        txtNumeroPaginas = new JTextField();
        p.add(txtNumeroPaginas);
        
        p.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        p.add(txtAutor);
        
        p.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField();
        p.add(txtEditorial);
        
        p.add(new JLabel("Año de Publicación:"));
        txtAñoPublicacion = new JTextField();
        p.add(txtAñoPublicacion);
        return p;
    }
    
    private JPanel createJuegoDeMesaPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Número de Jugadores:"));
        txtNumeroJugadores = new JTextField();
        p.add(txtNumeroJugadores);
        
        p.add(new JLabel("Edad Mínima:"));
        txtEdadMinima = new JTextField();
        p.add(txtEdadMinima);
        
        p.add(new JLabel("Edad Máxima:"));
        txtEdadMaxima = new JTextField();
        p.add(txtEdadMaxima);
        
        p.add(new JLabel("Tipo de Juego:"));
        cmbTipoJuego = new JComboBox<>(TipoJuegoMesa.values());
        p.add(cmbTipoJuego);
        return p;
    }
    
    private JPanel createFiguraPanel() {
        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        p.add(txtMarca);
        
        p.add(new JLabel("Material:"));
        txtMaterial = new JTextField();
        p.add(txtMaterial);
        
        p.add(new JLabel("Dimensión X:"));
        txtDx = new JTextField();
        p.add(txtDx);
        
        p.add(new JLabel("Dimensión Y:"));
        txtDy = new JTextField();
        p.add(txtDy);
        
        p.add(new JLabel("Dimensión Z:"));
        txtDz = new JTextField();
        p.add(txtDz);
        return p;
    }
    
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            fotoSeleccionada = fileChooser.getSelectedFile();
            lblFotoPath.setText(fotoSeleccionada.getName());
        }
    }
    
    private void añadirProducto() {
        try {
            String tipo = (String) cmbTipoProducto.getSelectedItem();
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            
            if (nombre.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre y la descripción no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            
            if (fotoSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una foto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String categoriaNombre = (String) cmbCategoria.getSelectedItem();
            if (categoriaNombre == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría.", "Error", JOptionPane.ERROR_MESSAGE);
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
                
                JOptionPane.showMessageDialog(this, "Producto añadido con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cerrar ventana
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Asegúrese de introducir valores numéricos válidos en los campos correspondientes.", "Error Numérico", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado al añadir producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}