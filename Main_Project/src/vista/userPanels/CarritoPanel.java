package vista.userPanels;

import modelo.producto.LineaProductoVenta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Panel que muestra el carrito de la compra.
 * Vista MVC estricta adaptada a la maqueta visual del proyecto.
 */
public class CarritoPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color COLOR_FONDO = new Color(153, 180, 209);

    private HeaderPanel headerPanel;
    private JPanel panelScrollCarrito;
    private JPanel panelScrollRecomendaciones;
    private JLabel labelTotal;
    private JButton botonPagar;

    public CarritoPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BoxLayout(contenedorPrincipal, BoxLayout.Y_AXIS));
        contenedorPrincipal.setBackground(COLOR_FONDO);
        contenedorPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ── 1. Banner "MY SHOPPING CART" ─────────────────────────────────
        JLabel tituloCarrito = new JLabel("  MY SHOPPING CART");
        tituloCarrito.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloCarrito.setOpaque(true);
        tituloCarrito.setBackground(Color.DARK_GRAY);
        tituloCarrito.setForeground(Color.WHITE);
        tituloCarrito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        tituloCarrito.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(tituloCarrito);

        contenedorPrincipal.add(Box.createVerticalStrut(10));

        // ── 2. Scroll Horizontal de Productos del Carrito ─────────────────
        panelScrollCarrito = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelScrollCarrito.setBackground(COLOR_FONDO);

        JScrollPane scrollCarrito = new JScrollPane(panelScrollCarrito);
        scrollCarrito.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollCarrito.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollCarrito.setPreferredSize(new Dimension(800, 300));
        scrollCarrito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        scrollCarrito.setBorder(null);
        scrollCarrito.getViewport().setBackground(COLOR_FONDO);
        scrollCarrito.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(scrollCarrito);

        // ── 3. Zona de Total y Botón PAY NOW ─────────────────────────────
        JPanel panelPay = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelPay.setBackground(COLOR_FONDO);
        panelPay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panelPay.setAlignmentX(Component.LEFT_ALIGNMENT);

        labelTotal = new JLabel("Total to pay: 0.00 €");
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelPay.add(labelTotal);

        botonPagar = new JButton("PAY NOW");
        botonPagar.setBackground(new Color(50, 200, 80));
        botonPagar.setForeground(Color.WHITE);
        botonPagar.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelPay.add(botonPagar);

        contenedorPrincipal.add(panelPay);
        contenedorPrincipal.add(Box.createVerticalStrut(20));

        // ── 4. Banner Recomendaciones ─────────────────────────────────────
        JLabel tituloRecomendaciones = new JLabel("  YOU SHOULD ADD THESE PRODUCTS!!");
        tituloRecomendaciones.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloRecomendaciones.setOpaque(true);
        tituloRecomendaciones.setBackground(Color.DARK_GRAY);
        tituloRecomendaciones.setForeground(Color.WHITE);
        tituloRecomendaciones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        tituloRecomendaciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(tituloRecomendaciones);

        contenedorPrincipal.add(Box.createVerticalStrut(10));

        // ── 5. Scroll Horizontal de Recomendaciones ───────────────────────
        panelScrollRecomendaciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelScrollRecomendaciones.setBackground(COLOR_FONDO);

        JScrollPane scrollRecomendaciones = new JScrollPane(panelScrollRecomendaciones);
        scrollRecomendaciones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollRecomendaciones.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollRecomendaciones.setPreferredSize(new Dimension(800, 200));
        scrollRecomendaciones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scrollRecomendaciones.setBorder(null);
        scrollRecomendaciones.getViewport().setBackground(COLOR_FONDO);
        scrollRecomendaciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedorPrincipal.add(scrollRecomendaciones);

        add(contenedorPrincipal, BorderLayout.CENTER);
    }

    // ── Métodos MVC ────────────────────────────────────────────────────────
    public HeaderPanel getHeaderPanel() { return headerPanel; }

    /** Registra el controlador principal en los botones PAY NOW y VOLVER. */
    public void setControladorPrincipal(ActionListener c) {
        for (ActionListener al : botonPagar.getActionListeners())  botonPagar.removeActionListener(al);
        botonPagar.addActionListener(c);
        botonPagar.setActionCommand("PAY_NOW");
    }

    /** Actualiza el contenido del carrito. */
    public void actualizarCarrito(Map<LineaProductoVenta, Integer> productos,
                                   double total,
                                   ActionListener controlador) {
        panelScrollCarrito.removeAll();

        if (productos.isEmpty()) {
            JLabel vacio = new JLabel("Your shopping cart is empty.");
            vacio.setFont(new Font("SansSerif", Font.BOLD, 16));
            panelScrollCarrito.add(vacio);
        } else {
            for (Map.Entry<LineaProductoVenta, Integer> entry : productos.entrySet()) {
                panelScrollCarrito.add(crearTarjetaProducto(entry.getKey(), entry.getValue(), controlador));
            }
        }

        labelTotal.setText(String.format("Total to pay: %.2f €", total));
        panelScrollCarrito.revalidate();
        panelScrollCarrito.repaint();
    }

    private JPanel crearTarjetaProducto(LineaProductoVenta prod, int cantidad, ActionListener controlador) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 2),
                new EmptyBorder(10, 10, 10, 10)));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(200, 250));
        tarjeta.setMinimumSize(new Dimension(200, 250));
        tarjeta.setMaximumSize(new Dimension(200, 250));

        JLabel lblNombre = new JLabel(prod.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblNombre);

        JLabel lblCat = new JLabel(prod.getDescripcion() != null ? prod.getDescripcion() : "");
        lblCat.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCat.setForeground(Color.GRAY);
        lblCat.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(lblCat);

        tarjeta.add(Box.createVerticalStrut(10));

        JLabel imgLabel = new JLabel("IMAGE", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(220, 220, 220));
        imgLabel.setPreferredSize(new Dimension(140, 90));
        imgLabel.setMaximumSize(new Dimension(140, 90));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(imgLabel);

        tarjeta.add(Box.createVerticalStrut(10));

        JPanel panelUnidades = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panelUnidades.setBackground(Color.WHITE);

        JButton btnMenos = new JButton("-");
        btnMenos.setMargin(new Insets(2, 6, 2, 6));
        btnMenos.setActionCommand("DEC_" + prod.getNombre());
        btnMenos.addActionListener(controlador);

        JLabel lblCantidad = new JLabel(String.valueOf(cantidad));
        lblCantidad.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton btnMas = new JButton("+");
        btnMas.setMargin(new Insets(2, 6, 2, 6));
        btnMas.setActionCommand("INC_" + prod.getNombre());
        btnMas.addActionListener(controlador);

        panelUnidades.add(btnMenos);
        panelUnidades.add(lblCantidad);
        panelUnidades.add(btnMas);
        tarjeta.add(panelUnidades);

        tarjeta.add(Box.createVerticalStrut(5));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);

        JLabel lblPrecio = new JLabel(String.format("%.2f €", prod.getPrecio() * cantidad));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton btnBorrar = new JButton("🗑");
        btnBorrar.setForeground(Color.RED);
        btnBorrar.setMargin(new Insets(2, 5, 2, 5));
        btnBorrar.setActionCommand("DEL_" + prod.getNombre());
        btnBorrar.addActionListener(controlador);

        panelInferior.add(lblPrecio, BorderLayout.WEST);
        panelInferior.add(btnBorrar, BorderLayout.EAST);
        tarjeta.add(panelInferior);

        return tarjeta;
    }

    public void mostrarMensaje(String msg, String titulo) {
        JOptionPane.showMessageDialog(this, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}