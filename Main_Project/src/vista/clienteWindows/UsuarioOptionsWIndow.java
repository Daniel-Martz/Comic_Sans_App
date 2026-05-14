package vista.clienteWindows;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import controladores.MainController;
import controladores.UsuarioOptionsController;
import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * Ventana de opciones de intercambio con bordes estilizados, 
 * diseño plano y soporte para imagen de fondo.
 */
public class UsuarioOptionsWIndow extends JDialog {

    /** The btn edit profile. */
    private JButton btnEditProfile;
    
    /** The btn cerrar sesion. */
    private JButton btnCerrarSesion;
    
    /** The btn purchase history. */
    private JButton btnPurchaseHistory;
    
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
     * Instantiates a new usuario options W indow.
     *
     * @param parent the parent
     */
    public UsuarioOptionsWIndow(JFrame parent) {
        super(parent, "Profile Menu", true);
        
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

        btnEditProfile = new JButton("EDIT MY PROFILE");
        btnPurchaseHistory = new JButton("PURCHASE HISTORY");
        btnCerrarSesion = new JButton("CERRAR SESION");
        

        btnEditProfile.setActionCommand("Edit Profile");
        btnCerrarSesion.setActionCommand("Cerrar Sesión");
        btnPurchaseHistory.setActionCommand("Purchase History");
        JButton[] buttons = {btnEditProfile, btnPurchaseHistory, btnCerrarSesion};

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
        
        // Título visual dentro del panel
        JLabel lblTitle = new JLabel("PROFILE OPTIONS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXTO); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 35, 0); 
        mainPanel.add(lblTitle, gbc);

        // Añadir botones
        gbc.insets = new Insets(12, 0, 12, 0); 
        
        Usuario u = Aplicacion.getInstancia().getUsuarioActual();
        if (u instanceof ClienteRegistrado) {
            gbc.gridy = 1;
            mainPanel.add(btnEditProfile, gbc);
            
            gbc.gridy = 2;
            mainPanel.add(btnPurchaseHistory, gbc);
        }
        
        gbc.gridy = 3;
        mainPanel.add(btnCerrarSesion, gbc);

        setContentPane(mainPanel);
    }

    /**
     * Cerrar.
     */
    public void cerrar() {
        this.dispose();
    }

    /**
     * Adds the listener.
     *
     * @param a the a
     */
    public void addListener(UsuarioOptionsController a){
        btnEditProfile.addActionListener(a);
        btnPurchaseHistory.addActionListener(a);
        btnCerrarSesion.addActionListener(a);
    }
}
