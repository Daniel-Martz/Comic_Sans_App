package vista.userPanels;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class mainMenuEmpleadoPanel extends JPanel {

    // 1. Declaración de componentes
    private JLabel lblTitle;
    private JPanel buttonPanel; // Cambiado a JPanel estándar
    
    private JButton btnManageInterchanges;
    private JButton btnManageProducts;
    private JButton btnManageOrders;
    private JButton btnValidationRequests;

    public mainMenuEmpleadoPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        // Título
        lblTitle = new JLabel("MAIN MENU");
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(74, 118, 201));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        // Botones e inicialización del sub-panel
        buttonPanel = new JPanel();
        btnManageInterchanges = new JButton("Manage Interchanges");
        btnManageProducts = new JButton("Manage Products");
        btnManageOrders = new JButton("Manage Orders");
        btnValidationRequests = new JButton("Validation Requests");
    }

    private void initLayout() {
        // Configuración panel principal
        setBackground(new Color(153, 180, 209));
        setBorder(new EmptyBorder(20, 50, 30, 50));
        setLayout(new BorderLayout(0, 25));

        // Configuración sub-panel de botones (WindowBuilder puede leer esto)
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 15));
        buttonPanel.add(btnManageInterchanges);
        buttonPanel.add(btnManageProducts);
        buttonPanel.add(btnManageOrders);
        buttonPanel.add(btnValidationRequests);

        // Añadir todo al panel principal
        add(lblTitle, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
}