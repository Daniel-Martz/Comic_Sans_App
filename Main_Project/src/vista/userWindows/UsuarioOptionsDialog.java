package vista.userWindows;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import controladores.MainController;
import controladores.UsuarioOptionsController;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Ventana de opciones de intercambio con bordes estilizados, 
 * diseño plano y soporte para imagen de fondo.
 */
public class UsuarioOptionsDialog extends JDialog {

    private JButton btnEditProfile;
    private JButton btnCerrarSesion;
    private JButton btnPurchaseHistory;
    private JPanel mainPanel;

    // --- PALETA DE COLORES ---
    private final Color COLOR_FONDO = new Color(245, 247, 250);     // Color de fondo (por si no hay imagen)
    private final Color COLOR_PRIMARIO = new Color(74, 144, 226);   // Azul de los botones
    private final Color COLOR_HOVER = new Color(53, 122, 189);      // Azul oscuro al pasar el ratón
    private final Color COLOR_TEXTO = new Color(44, 62, 80);        // Gris oscuro para bordes y textos

    public UsuarioOptionsDialog(JFrame parent) {
        super(parent, "Profile Menu", true);
        
        setSize(500, 450); 
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
        setupLayout();
    }

    private void initComponents() {
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        mainPanel.setBackground(COLOR_FONDO); // Color base por si no hay imagen

        // 2. Añadir un borde a toda la ventana (Borde exterior de color + padding interior)
        Border bordeVentana = BorderFactory.createLineBorder(COLOR_PRIMARIO, 4); // Borde azul grueso
        Border paddingVentana = new EmptyBorder(25, 40, 25, 40);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(bordeVentana, paddingVentana));

        Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 15);

        btnEditProfile = new JButton("EDIT MY PROFILE");
        btnPurchaseHistory = new JButton("PURCHASE HISTORY");
        btnCerrarSesion = new JButton("CERRAR SESION");
        

        btnEditProfile.setActionCommand("Edit Profile");
        btnCerrarSesion.setActionCommand("Cerrar Sesión");
        btnPurchaseHistory.setActionCommand("Purchase History");

        // 3. Aplicar estilos y BORDES a los botones
        JButton[] buttons = {btnEditProfile, btnPurchaseHistory, btnCerrarSesion};
        
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
        JLabel lblTitle = new JLabel("PROFILE OPTIONS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXTO); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 35, 0); 
        mainPanel.add(lblTitle, gbc);

        // Añadir botones
        gbc.insets = new Insets(12, 0, 12, 0); 
        
        gbc.gridy = 1;
        mainPanel.add(btnEditProfile, gbc);
        
        gbc.gridy = 2;
        mainPanel.add(btnPurchaseHistory, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(btnCerrarSesion, gbc);

        setContentPane(mainPanel);
    }

    public void cerrar() {
        this.dispose();
    }

    public void addListener(UsuarioOptionsController a){
        btnEditProfile.addActionListener(a);
        btnPurchaseHistory.addActionListener(a);
        btnCerrarSesion.addActionListener(a);
    }
}
