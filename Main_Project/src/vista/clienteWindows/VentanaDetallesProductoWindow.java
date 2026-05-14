package vista.clienteWindows;

import modelo.producto.*;
import modelo.categoria.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.stream.Collectors;

// TODO: Auto-generated Javadoc
/**
 * Ventana emergente (JDialog) que muestra la información detallada de un producto de venta.
 */
public class VentanaDetallesProductoWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The color fondo. */
    private final Color COLOR_FONDO = new Color(245, 245, 250);
    
    /** The color titulo. */
    private final Color COLOR_TITULO = new Color(50, 60, 100);

    /**
     * Instantiates a new ventana detalles producto window.
     *
     * @param parent the parent
     * @param producto the producto
     */
    public VentanaDetallesProductoWindow(Window parent, LineaProductoVenta producto) {
        super(parent, "Detalles del Producto", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(550, 720); 
        setLocationRelativeTo(parent);
        initComponents(producto);
    }

    /**
     * Inits the components.
     *
     * @param p the p
     */
    private void initComponents(LineaProductoVenta p) {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header: Nombre y Categorías
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_FONDO);

        JLabel lblNombre = new JLabel(p.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblNombre.setForeground(COLOR_TITULO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblNombre);
        
        String cats = p.getCategorias().stream().map(Categoria::getNombre).collect(Collectors.joining(", "));
        JLabel lblCats = new JLabel(cats.isEmpty() ? "No category" : "Categories: " + cats);
        lblCats.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
        lblCats.setForeground(Color.GRAY);
        lblCats.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblCats);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center: Imagen y Detalles
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(COLOR_FONDO);

        // Imagen
        JLabel lblFoto = new JLabel("", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(200, 300));
        lblFoto.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        if (p.getFoto() != null && p.getFoto().exists()) {
            ImageIcon icon = new ImageIcon(p.getFoto().getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH);
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

        addDetailRow(detailsPanel, "Price", String.format("%.2f €", p.getPrecio()));
        addDetailRow(detailsPanel, "Stock", String.valueOf(p.getStock()));
        addDetailRow(detailsPanel, "Average Rating", String.format("%.1f / 5", p.obtenerPuntuacionMedia()));
        
        if (p.getDescuento() != null) {
            addDetailRow(detailsPanel, "Discount", "Yes (Active)");
        }
        
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        detailsPanel.add(Box.createVerticalStrut(10));

        // Detalles específicos por tipo
        if (p instanceof Comic) {
            Comic c = (Comic) p;
            addDetailRow(detailsPanel, "Type", "Comic");
            addDetailRow(detailsPanel, "Author", c.getAutor());
            addDetailRow(detailsPanel, "Editorial", c.getEditorial());
            addDetailRow(detailsPanel, "Pages", String.valueOf(c.getNumeroPaginas()));
            addDetailRow(detailsPanel, "Publication Year", String.valueOf(c.getAñoPublicacion()));
        } else if (p instanceof Figura) {
            Figura f = (Figura) p;
            addDetailRow(detailsPanel, "Type", "Figure");
            addDetailRow(detailsPanel, "Brand", f.getMarca());
            addDetailRow(detailsPanel, "Material", f.getMaterial());
            addDetailRow(detailsPanel, "Dimensions", f.getDimensionX() + " x " + f.getDimensionY() + " x " + f.getDimensionZ() + " cm");
        } else if (p instanceof JuegoDeMesa) {
            JuegoDeMesa j = (JuegoDeMesa) p;
            addDetailRow(detailsPanel, "Type", "Game board");
            addDetailRow(detailsPanel, "Game Type", j.getTipoJuegoDeMesa() != null ? j.getTipoJuegoDeMesa().toString() : "N/A");
            addDetailRow(detailsPanel, "Players", j.getNumeroJugadores() + " maximum");
            addDetailRow(detailsPanel, "Age", j.getEdadMinima() + " - " + j.getEdadMaxima() + " years");
        }

        // Si es un Pack, mostramos el contenido detallado del pack (productos incluídos,
        // cantidad, precio unitario y subtotal por cada elemento).
        if (p instanceof modelo.producto.Pack) {
            modelo.producto.Pack pack = (modelo.producto.Pack) p;
            addDetailRow(detailsPanel, "Tipo", "Pack");
            addDetailRow(detailsPanel, "Componentes", String.valueOf(pack.getProductosPack().size()));

            // Panel con listado de componentes
            JPanel comps = new JPanel();
            comps.setLayout(new BoxLayout(comps, BoxLayout.Y_AXIS));
            comps.setBackground(Color.WHITE);
            comps.setBorder(new EmptyBorder(8, 8, 8, 8));

            double sumaSubtotales = 0.0;
            if (pack.getProductosPack().isEmpty()) {
                JLabel empty = new JLabel("(Este pack no contiene productos)");
                empty.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
                empty.setForeground(Color.GRAY);
                comps.add(empty);
            } else {
                for (java.util.Map.Entry<LineaProductoVenta, Integer> entry : pack.getProductosPack().entrySet()) {
                    LineaProductoVenta child = entry.getKey();
                    int qty = entry.getValue();
                    double unit = child.getPrecio();
                    double subtotal = unit * qty;
                    sumaSubtotales += subtotal;

                    String nombre = child.getNombre();
                    String linea = String.format("%s (x%d) — %.2f € each — subtotal: %.2f €", nombre, qty, unit, subtotal);
                    JLabel lbl = new JLabel(linea);
                    lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
                    lbl.setBorder(new EmptyBorder(4, 0, 4, 0));
                    comps.add(lbl);
                }
            }

            // Mostrar suma de subtotales e indicar si difiere del precio del pack
            JLabel lblSum = new JLabel(String.format("Suma componentes: %.2f €", sumaSubtotales));
            lblSum.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
            lblSum.setBorder(new EmptyBorder(8, 0, 0, 0));
            comps.add(lblSum);

            if (Math.abs(sumaSubtotales - p.getPrecio()) > 0.01) {
                JLabel lblNote = new JLabel("Nota: el precio del pack difiere de la suma de componentes (puede incluir descuento/recargo). ");
                lblNote.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
                lblNote.setForeground(Color.DARK_GRAY);
                comps.add(lblNote);
            }

            // Añadimos el panel de componentes a detailsPanel como un bloque
            detailsPanel.add(Box.createVerticalStrut(8));
            detailsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            detailsPanel.add(Box.createVerticalStrut(6));
            JLabel compTitle = new JLabel("Contenido del pack:");
            compTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
            detailsPanel.add(compTitle);
            detailsPanel.add(Box.createVerticalStrut(6));
            detailsPanel.add(comps);
        }

        JScrollPane scrollDetails = new JScrollPane(detailsPanel);
        scrollDetails.setBorder(null);
        centerPanel.add(scrollDetails, BorderLayout.CENTER);

        // Panel inferior del centro (Descripción y Reseñas combinadas)
        JPanel southCenterPanel = new JPanel(new BorderLayout(0, 15));
        southCenterPanel.setBackground(COLOR_FONDO);

        // 1. Panel de Descripción
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(COLOR_FONDO);
        JLabel lblDescTitle = new JLabel("Description:");
        lblDescTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblDescTitle.setBorder(new EmptyBorder(10, 0, 5, 0));
        descPanel.add(lblDescTitle, BorderLayout.NORTH);
        
        JTextArea txtDesc = new JTextArea(p.getDescripcion() != null ? p.getDescripcion() : "No description.");
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setFocusable(false);
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        txtDesc.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        descPanel.add(txtDesc, BorderLayout.CENTER);
        
        southCenterPanel.add(descPanel, BorderLayout.NORTH);

        // 2. Panel de Reseñas
        JPanel reviewsPanel = new JPanel(new BorderLayout());
        reviewsPanel.setBackground(COLOR_FONDO);
        JLabel lblRevTitle = new JLabel("Reviews:");
        lblRevTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblRevTitle.setBorder(new EmptyBorder(0, 0, 5, 0));
        reviewsPanel.add(lblRevTitle, BorderLayout.NORTH);

        JPanel reviewsList = new JPanel();
        reviewsList.setLayout(new BoxLayout(reviewsList, BoxLayout.Y_AXIS));
        reviewsList.setBackground(Color.WHITE);

        if (p.getReseña() != null && !p.getReseña().isEmpty()) {
            for (Reseña r : p.getReseña()) {
                JPanel revCard = new JPanel(new BorderLayout());
                revCard.setBackground(Color.WHITE);
                revCard.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(Color.LIGHT_GRAY, 1),
                        new EmptyBorder(8, 8, 8, 8)
                ));
                
                JLabel lblScore = new JLabel("⭐ " + r.getPuntuacion() + " / 5.0");
                lblScore.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
                lblScore.setForeground(new Color(255, 140, 0));
                revCard.add(lblScore, BorderLayout.NORTH);
                
                JTextArea txtComentario = new JTextArea(r.getDescripcion() != null ? r.getDescripcion() : "");
                txtComentario.setLineWrap(true);
                txtComentario.setWrapStyleWord(true);
                txtComentario.setEditable(false);
                txtComentario.setFocusable(false);
                txtComentario.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
                revCard.add(txtComentario, BorderLayout.CENTER);
                
                reviewsList.add(revCard);
                reviewsList.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel noReviews = new JLabel("There are no reviews for this product yet.");
            noReviews.setFont(new Font("Comic Sans MS", Font.ITALIC, 13));
            noReviews.setForeground(Color.GRAY);
            noReviews.setBorder(new EmptyBorder(5, 5, 5, 5));
            reviewsList.add(noReviews);
        }

        JScrollPane scrollReviews = new JScrollPane(reviewsList);
        scrollReviews.setPreferredSize(new Dimension(500, 150));
        scrollReviews.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        reviewsPanel.add(scrollReviews, BorderLayout.CENTER);

        southCenterPanel.add(reviewsPanel, BorderLayout.CENTER);

        centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

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

    /**
     * Adds the detail row.
     *
     * @param panel the panel
     * @param label the label
     * @param value the value
     */
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label + ": ");
        lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        
        JLabel val = new JLabel(value != null ? value : "N/A");
        val.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }
}
