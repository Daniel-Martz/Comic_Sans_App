package vista.userWindows;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Ventana de opciones de intercambio con bordes estilizados, 
 * diseño plano y soporte para imagen de fondo.
 */
public class VentanaInterchangeOptions extends JDialog {

    private JButton btnMyProducts;
    private JButton btnSearch;
    private JButton btnProposals;
    private JPanel mainPanel;

    // --- PALETA DE COLORES ---
    private final Color COLOR_FONDO = new Color(245, 247, 250);     // Color de fondo (por si no hay imagen)
    private final Color COLOR_PRIMARIO = new Color(74, 144, 226);   // Azul de los botones
    private final Color COLOR_HOVER = new Color(53, 122, 189);      // Azul oscuro al pasar el ratón
    private final Color COLOR_TEXTO = new Color(44, 62, 80);        // Gris oscuro para bordes y textos

    public VentanaInterchangeOptions(JFrame parent) {
        super(parent, "Interchange Menu", true);
        
        // Configuración básica
        setSize(500, 450); 
        setLocationRelativeTo(parent);
        setResizable(false);
        // Opcional: Descomenta la siguiente línea si quieres quitar la barra nativa de Windows/Mac
        // setUndecorated(true); 

        // Inicializar componentes y layout
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        // 1. Crear un panel principal que soporte una IMAGEN DE FONDO
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // --- FONDO DE VENTANA ---
                // Si tienes una imagen de fondo en tus assets, pon su ruta aquí:
                // ImageIcon fondo = new ImageIcon("src/assets/tu_fondo.png");
                // g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setBackground(COLOR_FONDO); // Color base por si no hay imagen

        // 2. Añadir un borde a toda la ventana (Borde exterior de color + padding interior)
        Border bordeVentana = BorderFactory.createLineBorder(COLOR_PRIMARIO, 4); // Borde azul grueso
        Border paddingVentana = new EmptyBorder(25, 40, 25, 40);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(bordeVentana, paddingVentana));

        Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 15);

        btnMyProducts = new JButton("MY SECOND-HAND PRODUCTS");
        btnSearch = new JButton("SEARCH FOR INTERCHANGES");
        btnProposals = new JButton("INTERCHANGES PROPOSALS");

        // 3. Aplicar estilos y BORDES a los botones
        JButton[] buttons = {btnMyProducts, btnSearch, btnProposals};
        
        // Crear un borde redondeado para los botones
        Border bordeBoton = BorderFactory.createLineBorder(COLOR_TEXTO, 2, true); // Borde oscuro de 2px
        Border paddingBoton = new EmptyBorder(10, 15, 10, 15); // Espacio interno para que el texto respire
        Border bordeFinalBoton = BorderFactory.createCompoundBorder(bordeBoton, paddingBoton);

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(COLOR_PRIMARIO);
            btn.setForeground(Color.WHITE);
            
            // Aplicamos el borde que hemos creado
            btn.setBorder(bordeFinalBoton);
            btn.setFocusPainted(false); // Quita el recuadrito punteado feo de Java
            btn.setOpaque(true);
            
            btn.setPreferredSize(new Dimension(320, 55));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Efecto Hover (cambia el fondo pero mantiene el borde intacto)
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(COLOR_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(COLOR_PRIMARIO);
                }
            });
        }
    }

    private void setupLayout() {
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título visual dentro del panel
        JLabel lblTitle = new JLabel("INTERCHANGE OPTIONS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXTO); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Si usas un fondo oscuro, quizás quieras ponerle fondo al título para que se lea bien
        // lblTitle.setOpaque(true);
        // lblTitle.setBackground(new Color(255, 255, 255, 200)); // Fondo semitransparente
        
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 35, 0); 
        mainPanel.add(lblTitle, gbc);

        // Añadir botones
        gbc.insets = new Insets(12, 0, 12, 0); 
        
        gbc.gridy = 1;
        mainPanel.add(btnMyProducts, gbc);
        
        gbc.gridy = 2;
        mainPanel.add(btnSearch, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(btnProposals, gbc);

        setContentPane(mainPanel);
    }

    public void setControlador(ActionListener c) {
        btnMyProducts.addActionListener(c);
        btnSearch.addActionListener(c);
        btnProposals.addActionListener(c);
        
        btnMyProducts.setActionCommand("MY SECOND-HAND PRODUCTS");
        btnSearch.setActionCommand("SEARCH_INTERCHANGES");
        btnProposals.setActionCommand("PROPOSALS");
    }

    public void cerrar() {
        this.dispose();
    }
}