package vista.clienteWindows;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * Ventana de opciones de intercambio con bordes estilizados, 
 * diseño plano y soporte para imagen de fondo.
 */
public class VentanaInterchangeOptionsWindow extends JDialog {

    /** The btn my products. */
    private JButton btnMyProducts;
    
    /** The btn search. */
    private JButton btnSearch;
    
    /** The btn proposals. */
    private JButton btnProposals;
    
    /** The main panel. */
    private JPanel mainPanel;

    /** The color fondo. */
    // --- PALETA DE COLORES ---
    private final Color COLOR_FONDO = new Color(245, 247, 250);    
    
    /** The color primario. */
    private final Color COLOR_PRIMARIO = new Color(74, 144, 226);  
    
    /** The color hover. */
    private final Color COLOR_HOVER = new Color(53, 122, 189);     
    
    /** The color texto. */
    private final Color COLOR_TEXTO = new Color(44, 62, 80);       

    /**
     * Instantiates a new ventana interchange options window.
     *
     * @param parent the parent
     */
    public VentanaInterchangeOptionsWindow(JFrame parent) {
        super(parent, "Interchange Menu", true);
        

        setSize(500, 450); 
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
        setupLayout();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        mainPanel.setBackground(COLOR_FONDO); 

        Border bordeVentana = BorderFactory.createLineBorder(COLOR_PRIMARIO, 4); 
        Border paddingVentana = new EmptyBorder(25, 40, 25, 40);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(bordeVentana, paddingVentana));

        Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 15);

        btnMyProducts = new JButton("MY SECOND-HAND PRODUCTS");
        btnSearch = new JButton("SEARCH FOR INTERCHANGES");
        btnProposals = new JButton("INTERCHANGES PROPOSALS");

        JButton[] buttons = {btnMyProducts, btnSearch, btnProposals};
        
        Border bordeBoton = BorderFactory.createLineBorder(COLOR_TEXTO, 2, true);
        Border paddingBoton = new EmptyBorder(10, 15, 10, 15); 
        Border bordeFinalBoton = BorderFactory.createCompoundBorder(bordeBoton, paddingBoton);

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(COLOR_PRIMARIO);
            btn.setForeground(Color.WHITE);
            
            btn.setBorder(bordeFinalBoton);
            btn.setFocusPainted(false); 
            btn.setOpaque(true);
            
            btn.setPreferredSize(new Dimension(320, 55));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    /**
     * Setup layout.
     */
    private void setupLayout() {
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitle = new JLabel("INTERCHANGE OPTIONS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXTO); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 35, 0); 
        mainPanel.add(lblTitle, gbc);
        gbc.insets = new Insets(12, 0, 12, 0); 
        
        gbc.gridy = 1;
        mainPanel.add(btnMyProducts, gbc);
        
        gbc.gridy = 2;
        mainPanel.add(btnSearch, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(btnProposals, gbc);

        setContentPane(mainPanel);
    }

    /**
     * Sets the controlador.
     *
     * @param c the new controlador
     */
    public void setControlador(ActionListener c) {
        btnMyProducts.addActionListener(c);
        btnSearch.addActionListener(c);
        btnProposals.addActionListener(c);
        
        btnMyProducts.setActionCommand("MY SECOND-HAND PRODUCTS");
        btnSearch.setActionCommand("SEARCH_INTERCHANGES");
        btnProposals.setActionCommand("PROPOSALS");
    }

    /**
     * Cerrar.
     */
    public void cerrar() {
        this.dispose();
    }
}