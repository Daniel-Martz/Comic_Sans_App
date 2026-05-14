package vista.clienteWindows;

import controladores.MainController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import modelo.notificacion.Notificacion;
import modelo.notificacion.NotificacionIntercambio;
import modelo.notificacion.NotificacionOferta;
import modelo.notificacion.NotificacionPedido;
import modelo.notificacion.NotificacionProducto;
import modelo.notificacion.NotificacionValidacion;
import modelo.producto.ProductoSegundaMano;
import modelo.solicitud.Oferta;
import modelo.solicitud.SolicitudPedido;
import vista.userPanels.InterchangeCardPanel;

// TODO: Auto-generated Javadoc
/**
 * Diálogo modal que muestra el contenido de una notificación de forma formateada.
 */
public class NotificacionWindow extends JDialog {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new notificacion window.
     *
     * @param owner the owner
     * @param n the n
     * @param mainController the main controller
     */
    public NotificacionWindow(Window owner, Notificacion n, MainController mainController) {
        super(owner, "Notificación", ModalityType.APPLICATION_MODAL);
        init(n);
    }

    /**
     * Inits the.
     *
     * @param n the n
     */
    private void init(Notificacion n) {
        setLayout(new BorderLayout(12, 12));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel principal con contenido
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Color.WHITE);

        // Título
        JLabel titulo = new JLabel("Notificación Details");
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(12));

        // ID
        contenido.add(crearFilaInfo("ID:", String.valueOf(n.getId()), n));
        contenido.add(Box.createVerticalStrut(8));

        // Mensaje
        contenido.add(crearFilaInfo("Message:", n.getMensaje(), n));
        contenido.add(Box.createVerticalStrut(8));

        // Fecha y hora
        contenido.add(crearFilaInfo("Date and Time:", n.getHoraEnvio().toStringFecha(), n));
        contenido.add(Box.createVerticalStrut(8));

        JPanel contenidoEspecifico = obtenerContenidoEspecifico(n);
        if (contenidoEspecifico != null) {
            contenido.add(contenidoEspecifico);
        }
        contenido.add(Box.createVerticalGlue());

        // Scroll si es necesario
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setPreferredSize(new Dimension(700, 500));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scroll, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel();
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dispose());
        botones.add(ok);
        add(botones, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(750, 580));
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Crea una fila con etiqueta y valor formateados.
     *
     * @param etiqueta the etiqueta
     * @param valor the valor
     * @param notif the notif
     * @return the j panel
     */
    private JPanel crearFilaInfo(String etiqueta, String valor, Notificacion notif) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelNombre = new JLabel(etiqueta);
        labelNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
        labelNombre.setPreferredSize(new Dimension(100, 20));

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
        labelValor.setForeground(new Color(60, 100, 200));

        fila.add(labelNombre);
        fila.add(Box.createHorizontalStrut(8));
        fila.add(labelValor);
        fila.add(Box.createHorizontalGlue());

        return fila;
    }

    /**
     * Método que resuelve el tipo de notificación y devuelve el contenido específico.
     * Centraliza la lógica de instanceof.
     *
     * @param notif the notif
     * @return the j panel
     */
    private JPanel obtenerContenidoEspecifico(Notificacion notif) {
        if (notif instanceof NotificacionOferta notificacionOferta) {
            return contenidoPropio(notificacionOferta);
        } else if (notif instanceof NotificacionPedido notificacionPedido) {
            return contenidoPropio(notificacionPedido);
        } else if (notif instanceof NotificacionProducto notificacionProducto) {
            return contenidoPropio(notificacionProducto);
        } else if (notif instanceof NotificacionIntercambio notificacionIntercambio) {
            return contenidoPropio(notificacionIntercambio);
        } else if (notif instanceof NotificacionValidacion notificacionValidacion) {
            return contenidoPropio(notificacionValidacion);
        }
        return null;
    }

    /**
     * Contenido propio.
     *
     * @param n the n
     * @return the j panel
     */
    private JPanel contenidoPropio(NotificacionOferta n) {
        if (n == null) return new JPanel();
        
        Oferta oferta = n.getOferta();
        if (oferta == null) return new JPanel();
        
        // Obtener usuarios de la oferta
        String userOfertante = oferta.getOfertante().getNombreUsuario();
        String userDestinatario = oferta.getDestinatario().getNombreUsuario();
        
        // Productos ofertados (dados por el ofertante)
        ProductoSegundaMano[] productosDados = oferta.productosOfertados() != null 
            ? oferta.productosOfertados().toArray(new ProductoSegundaMano[0])
            : new ProductoSegundaMano[0];
        
        // Productos solicitados (que espera recibir)
        ProductoSegundaMano[] productosRecibidos = oferta.productosSolicitados() != null
            ? oferta.productosSolicitados().toArray(new ProductoSegundaMano[0])
            : new ProductoSegundaMano[0];
        
        // Calcular balance
        double balance = calcularBalance(productosDados, productosRecibidos);
        
        // Crear el InterchangeCardPanel con los datos de la oferta
        String headerLabel = "FROM: " + userOfertante + " TO: " + userDestinatario;
        InterchangeCardPanel panel = new InterchangeCardPanel(
            headerLabel,
            balance,
            productosDados,
            productosRecibidos,
            InterchangeCardPanel.Modo.INCOME,
            "PRODUCTS OFFERED ▼",
            "PRODUCTS REQUESTED ▼",
            false
        );
        
        // Registrar listener para abrir detalles del producto
        panel.setInfoListener(event -> {
            String command = event.getActionCommand();
            if (command.startsWith("INFO_")) {
                int productID = Integer.parseInt(command.substring(5));
                ProductoSegundaMano product = findProductoById(productosDados, productosRecibidos, productID);
                if (product != null) {
                    // Abrir ventana de detalles del producto segundamano
                    // Convertir a LineaProductoVenta si es necesario o crear una ventana específica
                    new VentanaDetallesProductoSegundaManoWindow(NotificacionWindow.this, product).setVisible(true);
                }
            }
        });
        
        // Envolver en un panel para mantener formato consistente
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(panel);
        
        return wrapper;
    }
    
    /**
     * Busca un producto por ID en los arrays dados y recibidos.
     *
     * @param dados the dados
     * @param recibidos the recibidos
     * @param id the id
     * @return the producto segunda mano
     */
    private ProductoSegundaMano findProductoById(ProductoSegundaMano[] dados, ProductoSegundaMano[] recibidos, int id) {
        for (ProductoSegundaMano p : dados) {
            if (p != null && p.getID() == id) {
                return p;
            }
        }
        for (ProductoSegundaMano p : recibidos) {
            if (p != null && p.getID() == id) {
                return p;
            }
        }
        return null;
    }
    
    /**
     * Contenido propio.
     *
     * @param notif the notif
     * @return the j panel
     */
    private JPanel contenidoPropio(NotificacionIntercambio notif) {
        if (notif == null) return new JPanel();
        
        String codigoIntercambio = notif.getCodigoIntercambio();
        modelo.solicitud.DetallesIntercambio detalles = notif.getDetallesIntercambio();
        
        // Panel principal para los detalles del intercambio
        JPanel panelIntercambio = new JPanel();
        panelIntercambio.setLayout(new BoxLayout(panelIntercambio, BoxLayout.Y_AXIS));
        panelIntercambio.setBackground(new Color(245, 245, 250));
        panelIntercambio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Título: "Detalles del Intercambio"
        JLabel lblTituloIntercambio = new JLabel("Interchange Details");
        lblTituloIntercambio.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblTituloIntercambio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIntercambio.add(lblTituloIntercambio);
        panelIntercambio.add(Box.createVerticalStrut(10));
        
        // Código del intercambio
        addDetailRow(panelIntercambio, "Code", codigoIntercambio != null ? codigoIntercambio : "N/A");
        
        // Lugar del intercambio
        String lugar = detalles != null ? detalles.getLugarIntercambio() : "Not specified";
        addDetailRow(panelIntercambio, "Place", lugar != null ? lugar : "Not specified");
        
        // Fecha del intercambio
        String fecha = "Not specified";
        if (detalles != null && detalles.getFechaIntercambio() != null) {
            fecha = detalles.getFechaIntercambio().toStringFecha();
        }
        addDetailRow(panelIntercambio, "Fecha", fecha);
        
        // Envolver en panel para mantener formato
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(panelIntercambio);
        
        return wrapper;
    }
    
    /**
     * Contenido propio.
     *
     * @param notif the notif
     * @return the j panel
     */
    private JPanel contenidoPropio(NotificacionValidacion notif) {
        if (notif == null || notif.getSolicitudProductoSegundaMano() == null) return new JPanel();
        
        ProductoSegundaMano producto = notif.getSolicitudProductoSegundaMano().getProductoAValidar();
        if (producto == null) return new JPanel();
        
        // Crear panel con la estructura similar a VentanaDetallesProductoSegundaManoWindow
        JPanel cardPanel = new JPanel(new BorderLayout(15, 15));
        cardPanel.setBackground(new Color(245, 245, 250));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Izquierda: Imagen del producto
        JLabel lblFoto = new JLabel("", javax.swing.SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 150));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        if (producto.getFoto() != null && producto.getFoto().exists()) {
            try {
                ImageIcon icon = new ImageIcon(producto.getFoto().getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                lblFoto.setText("NO IMAGE");
            }
        } else {
            lblFoto.setText("NO IMAGE");
        }
        cardPanel.add(lblFoto, BorderLayout.WEST);
        
        // Centro: Detalles
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Nombre del producto
        JLabel lblNombre = new JLabel(producto.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblNombre);
        detailsPanel.add(Box.createVerticalStrut(8));
        
        // Condición
        String condicion = producto.getDatosValidacion() != null ? 
            producto.getDatosValidacion().getEstadoConservacion().toString() : "Pending";
        addDetailRow(detailsPanel, "Condition", condicion);
        
        // Valor estimado
        String valor = producto.getDatosValidacion() != null ? 
            String.format("%.2f €", producto.getDatosValidacion().getPrecioEstimadoProducto()) : "Pending";
        addDetailRow(detailsPanel, "Value", valor);
        
        // Propietario
        String propietario = producto.getClienteProducto() != null ? 
            producto.getClienteProducto().getNombreUsuario() : "Unknown";
        addDetailRow(detailsPanel, "Owner", propietario);
        
        cardPanel.add(detailsPanel, BorderLayout.CENTER);
        
        // Envolver en panel para mantener formato
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(cardPanel);
        
        return wrapper;
    }
    
    /**
     * Crea una fila con etiqueta y valor en un panel.
     *
     * @param panel the panel
     * @param label the label
     * @param value the value
     */
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel lbl = new JLabel(label + ": ");
        lbl.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        
        JLabel val = new JLabel(value != null ? value : "N/A");
        val.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }
    
    /**
     * Calcula el balance entre productos dados y recibidos.
     *
     * @param dados the dados
     * @param recibidos the recibidos
     * @return the double
     */
    private double calcularBalance(ProductoSegundaMano[] dados, ProductoSegundaMano[] recibidos) {
        double precioDados = 0;
        double precioRecibidos = 0;
        
        for (ProductoSegundaMano p : dados) {
            if (p != null && p.getDatosValidacion() != null) {
                precioDados += p.getDatosValidacion().getPrecioEstimadoProducto();
            }
        }
        
        for (ProductoSegundaMano p : recibidos) {
            if (p != null && p.getDatosValidacion() != null) {
                precioRecibidos += p.getDatosValidacion().getPrecioEstimadoProducto();
            }
        }
        
        return precioRecibidos - precioDados;
    }

    /**
     * Contenido propio.
     *
     * @param notif the notif
     * @return the j panel
     */
    private JPanel contenidoPropio(NotificacionPedido notif) {
        if (notif == null || notif.getPedido() == null) return new JPanel();
        
        SolicitudPedido pedido = (SolicitudPedido) notif.getPedido();
        Map<modelo.producto.LineaProductoVenta, Integer> productos = pedido.getProductosDiferentes();
        
        // Panel con scroll horizontal para las tarjetas
        JPanel panelProductos = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));
        panelProductos.setBackground(new Color(240, 240, 240));
        
        if (productos != null && !productos.isEmpty()) {
            for (Map.Entry<modelo.producto.LineaProductoVenta, Integer> entry : productos.entrySet()) {
                panelProductos.add(crearTarjetaProductoPedido(entry.getKey(), entry.getValue()));
            }
        } else {
            JLabel vacio = new JLabel("There aren't any products in this order.");
            vacio.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
            panelProductos.add(vacio);
        }
        
        // Crear scroll horizontal
        JScrollPane scrollPanel = new JScrollPane(panelProductos);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPanel.setPreferredSize(new Dimension(500, 220));
        scrollPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        scrollPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        scrollPanel.getViewport().setBackground(new Color(240, 240, 240));
        
        // Envolver en un panel para mantener formato
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(scrollPanel);
        
        return wrapper;
    }
    
    /**
     * Crea una tarjeta visual para un producto del pedido.
     *
     * @param prod the prod
     * @param cantidad the cantidad
     * @return the j panel
     */
    private JPanel crearTarjetaProductoPedido(modelo.producto.LineaProductoVenta prod, int cantidad) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(180, 220));
        tarjeta.setMinimumSize(new Dimension(180, 220));
        tarjeta.setMaximumSize(new Dimension(180, 220));
        
        // Nombre del producto
        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblNombre);
        
        tarjeta.add(Box.createVerticalStrut(8));
        
        // Imagen del producto
        JLabel imgLabel;
        if (prod.getFoto() != null && prod.getFoto().exists()) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(prod.getFoto().getPath());
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                ImageIcon iconoEscalado = new ImageIcon(imgEscalada);
                imgLabel = new JLabel(iconoEscalado, javax.swing.SwingConstants.CENTER);
            } catch (Exception ex) {
                imgLabel = new JLabel("IMAGE", javax.swing.SwingConstants.CENTER);
                imgLabel.setOpaque(true);
                imgLabel.setBackground(new Color(220, 220, 220));
            }
        } else {
            imgLabel = new JLabel("IMAGE", javax.swing.SwingConstants.CENTER);
            imgLabel.setOpaque(true);
            imgLabel.setBackground(new Color(220, 220, 220));
        }
        imgLabel.setPreferredSize(new Dimension(120, 80));
        imgLabel.setMaximumSize(new Dimension(120, 80));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(imgLabel);
        
        tarjeta.add(Box.createVerticalStrut(8));
        
        // Cantidad
        JLabel lblCantidad = new JLabel("Qty: " + cantidad);
        lblCantidad.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
        lblCantidad.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblCantidad);
        
        // Precio
        JLabel lblPrecio = new JLabel(String.format("%.2f €", prod.getPrecio() * cantidad));
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblPrecio);
        
        tarjeta.add(Box.createVerticalGlue());
        
        return tarjeta;
    }

    /**
     * Contenido propio.
     *
     * @param notif the notif
     * @return the j panel
     */
    private JPanel contenidoPropio(NotificacionProducto notif) {
        if (notif == null) return new JPanel();
        
        Set<modelo.producto.LineaProductoVenta> productos = notif.getProductos();
        
        // Panel con scroll horizontal para las tarjetas
        JPanel panelProductos = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 10));
        panelProductos.setBackground(new Color(240, 240, 240));
        
        if (productos != null && !productos.isEmpty()) {
            for (modelo.producto.LineaProductoVenta producto : productos) {
                panelProductos.add(crearTarjetaProductoRecomendado(producto));
            }
        } else {
            JLabel vacio = new JLabel("There aren't any products in this notification");
            vacio.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
            panelProductos.add(vacio);
        }
        
        // Crear scroll horizontal
        JScrollPane scrollPanel = new JScrollPane(panelProductos);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPanel.setPreferredSize(new Dimension(500, 220));
        scrollPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        scrollPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        scrollPanel.getViewport().setBackground(new Color(240, 240, 240));
        
        // Envolver en un panel para mantener formato
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(scrollPanel);
        
        return wrapper;
    }
    
    /**
     * Crea una tarjeta visual para un producto recomendado.
     *
     * @param prod the prod
     * @return the j panel
     */
    private JPanel crearTarjetaProductoRecomendado(modelo.producto.LineaProductoVenta prod) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(180, 220));
        tarjeta.setMinimumSize(new Dimension(180, 220));
        tarjeta.setMaximumSize(new Dimension(180, 220));
        
        // Nombre del producto
        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblNombre);
        
        tarjeta.add(Box.createVerticalStrut(8));
        
        // Imagen del producto
        JLabel imgLabel;
        if (prod.getFoto() != null && prod.getFoto().exists()) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(prod.getFoto().getPath());
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                ImageIcon iconoEscalado = new ImageIcon(imgEscalada);
                imgLabel = new JLabel(iconoEscalado, javax.swing.SwingConstants.CENTER);
            } catch (Exception ex) {
                imgLabel = new JLabel("IMAGE", javax.swing.SwingConstants.CENTER);
                imgLabel.setOpaque(true);
                imgLabel.setBackground(new Color(220, 220, 220));
            }
        } else {
            imgLabel = new JLabel("IMAGE", javax.swing.SwingConstants.CENTER);
            imgLabel.setOpaque(true);
            imgLabel.setBackground(new Color(220, 220, 220));
        }
        imgLabel.setPreferredSize(new Dimension(120, 80));
        imgLabel.setMaximumSize(new Dimension(120, 80));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(imgLabel);
        
        tarjeta.add(Box.createVerticalStrut(8));
        
        // Categoría
        JLabel lblCategoria = new JLabel(prod.getDescripcion() != null ? prod.getDescripcion() : "");
        lblCategoria.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
        lblCategoria.setForeground(Color.GRAY);
        lblCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblCategoria);
        
        tarjeta.add(Box.createVerticalStrut(4));
        
        // Precio
        JLabel lblPrecio = new JLabel(String.format("%.2f €", prod.getPrecio()));
        lblPrecio.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblPrecio);
        
        tarjeta.add(Box.createVerticalGlue());
        
        return tarjeta;
    }
}
