package vista.clienteWindows;

import modelo.producto.*;
import modelo.categoria.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.stream.Collectors;

/**
 * Ventana emergente (JDialog) que muestra la información detallada de un producto de venta.
 */
public class VentanaDetallesProducto extends JDialog {

    private static final long serialVersionUID = 1L;
    private final Color COLOR_FONDO = new Color(245, 245, 250);
    private final Color COLOR_TITULO = new Color(50, 60, 100);

    public VentanaDetallesProducto(Window parent, LineaProductoVenta producto) {
        super(parent, "Detalles del Producto", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(550, 720); // Ampliamos la ventana para hacer hueco a las reseñas
        setLocationRelativeTo(parent);
        initComponents(producto);
    }

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
        JLabel lblCats = new JLabel(cats.isEmpty() ? "Sin categoría" : "Categorías: " + cats);
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

        addDetailRow(detailsPanel, "Precio", String.format("%.2f €", p.getPrecio()));
        addDetailRow(detailsPanel, "Stock", String.valueOf(p.getStock()));
        addDetailRow(detailsPanel, "Valoración Media", String.format("%.1f / 5", p.obtenerPuntuacionMedia()));
        
        if (p.getDescuento() != null) {
            addDetailRow(detailsPanel, "Descuento", "Sí (Activo)");
        }
        
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        detailsPanel.add(Box.createVerticalStrut(10));

        // Detalles específicos por tipo
        if (p instanceof Comic) {
            Comic c = (Comic) p;
            addDetailRow(detailsPanel, "Tipo", "Cómic");
            addDetailRow(detailsPanel, "Autor", c.getAutor());
            addDetailRow(detailsPanel, "Editorial", c.getEditorial());
            addDetailRow(detailsPanel, "Páginas", String.valueOf(c.getNumeroPaginas()));
            addDetailRow(detailsPanel, "Año Publicación", String.valueOf(c.getAñoPublicacion()));
        } else if (p instanceof Figura) {
            Figura f = (Figura) p;
            addDetailRow(detailsPanel, "Tipo", "Figura");
            addDetailRow(detailsPanel, "Marca", f.getMarca());
            addDetailRow(detailsPanel, "Material", f.getMaterial());
            addDetailRow(detailsPanel, "Dimensiones", f.getDimensionX() + " x " + f.getDimensionY() + " x " + f.getDimensionZ() + " cm");
        } else if (p instanceof JuegoDeMesa) {
            JuegoDeMesa j = (JuegoDeMesa) p;
            addDetailRow(detailsPanel, "Tipo", "Juego de Mesa");
            addDetailRow(detailsPanel, "Tipo de Juego", j.getTipoJuegoDeMesa() != null ? j.getTipoJuegoDeMesa().toString() : "N/A");
            addDetailRow(detailsPanel, "Jugadores", j.getNumeroJugadores() + " máximo");
            addDetailRow(detailsPanel, "Edad", j.getEdadMinima() + " - " + j.getEdadMaxima() + " años");
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
        JLabel lblDescTitle = new JLabel("Descripción:");
        lblDescTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblDescTitle.setBorder(new EmptyBorder(10, 0, 5, 0));
        descPanel.add(lblDescTitle, BorderLayout.NORTH);
        
        JTextArea txtDesc = new JTextArea(p.getDescripcion() != null ? p.getDescripcion() : "Sin descripción.");
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
        JLabel lblRevTitle = new JLabel("Reseñas:");
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
            JLabel noReviews = new JLabel("No hay reseñas aún.");
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
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        bottomPanel.add(btnCerrar);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

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
