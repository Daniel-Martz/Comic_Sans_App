package vista.userWindows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Ventana de opciones de intercambio con el diseño estandarizado de la app.
 */
public class VentanaInterchangeOptions extends JDialog {

    private JButton btnMyProducts;
    private JButton btnSearch;
    private JButton btnProposals;
    private JPanel mainPanel;

    public VentanaInterchangeOptions(JFrame parent) {
        super(parent, "Interchange Menu", true);
        
        // Configuración básica
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Inicializar componentes y layout
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE); // Siguiendo el estilo limpio de la app
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Estilo de botones (puedes ajustar la fuente a Comic Sans si es el estilo de la app)
        Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 14);

        btnMyProducts = new JButton("MY SECOND-HAND PRODUCTS");
        btnSearch = new JButton("SEARCH FOR INTERCHANGES");
        btnProposals = new JButton("INTERCHANGES PROPOSALS");

        // Aplicar estilos comunes
        JButton[] buttons = {btnMyProducts, btnSearch, btnProposals};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(300, 50));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    private void setupLayout() {
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre botones

        // Añadir título visual dentro del panel
        JLabel lblTitle = new JLabel("INTERCHANGE OPTIONS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(lblTitle, gbc);

        // Añadir botones
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridy = 1;
        mainPanel.add(btnMyProducts, gbc);
        
        gbc.gridy = 2;
        mainPanel.add(btnSearch, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(btnProposals, gbc);

        setContentPane(mainPanel);
    }

    /**
     * Método para asignar el controlador y seguir el patrón de la app.
     */
    public void setControlador(ActionListener c) {
        btnMyProducts.addActionListener(c);
        btnSearch.addActionListener(c);
        btnProposals.addActionListener(c);
        
        // Asignamos ActionCommands para que el controlador sepa qué botón se pulsó
        btnMyProducts.setActionCommand("MY_PRODUCTS");
        btnSearch.setActionCommand("SEARCH_INTERCHANGES");
        btnProposals.setActionCommand("PROPOSALS");
    }

    // Getters por si el controlador necesita cerrar la ventana
    public void cerrar() {
        this.dispose();
    }
}