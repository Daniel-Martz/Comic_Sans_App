package vista.userPanels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import modelo.producto.*;
import java.util.List;


public class InterchangeCardPanel extends JPanel {

    private static final long serialVersionUID = 1L;
	private HeaderPanel headerPanel;
    private PanelDesplegable panelGiven;
    private PanelDesplegable panelReceived;
    private ActionButtonPanel actionButtonPanel;
    
    private String usernameFrom;
    private List<ProductoSegundaMano> received;
    private List<ProductoSegundaMano> given;
    //Este boolean es para saber si también tendrá botón para aceptar
    private boolean alsoAccept;

    public InterchangeCardPanel(String usernameFrom, List<ProductoSegundaMano> received, List<ProductoSegundaMano> given, boolean alsoAccept) {
        this.usernameFrom = usernameFrom;
        this.received = received;
        this.given = given;
        this.alsoAccept = alsoAccept;
        initComponents();
        initLayout();
    }
    
    private double calcularBalance() {
        double totalReceived = 0.0;
        double totalGiven = 0.0;

        // Sumar precios de los productos que RECIBIMOS
        if (received != null) {
            for (ProductoSegundaMano p : received) {
                if (p.getDatosValidacion() != null) {
                    totalReceived += p.getDatosValidacion().getPrecioEstimadoProducto();
                }
            }
        }

        // Sumar precios de los productos que DAMOS
        if (given != null) {
            for (ProductoSegundaMano p : given) {
                if (p.getDatosValidacion() != null) {
                    totalGiven += p.getDatosValidacion().getPrecioEstimadoProducto();
                }
            }
        }

        // Balance = Lo que gano (Recibidos) - Lo que pierdo (Dados)
        return totalReceived - totalGiven;
    }

    private void initComponents() {
        headerPanel = new HeaderPanel(usernameFrom, calcularBalance());
        panelGiven = new PanelDesplegable("PRODUCTS GIVEN ▼", given);
        panelReceived = new PanelDesplegable("PRODUCTS RECEIVED ▼", received);
        actionButtonPanel = new ActionButtonPanel(alsoAccept);
    }

    private void initLayout() {
        // Configuración y layout
        setBackground(new Color(153, 180, 209));
        
        //Meto un borde que delimita el area del panel (exterior) y otro empty
        //que será el margen
        setBorder(new CompoundBorder(
            new LineBorder(Color.DARK_GRAY, 2), 
            new EmptyBorder(10, 10, 10, 10)
        ));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(headerPanel);
        //Esto se hace para dejar hueco entre paneles
        add(Box.createVerticalStrut(10));
        add(panelGiven);
        add(Box.createVerticalStrut(10));
        add(panelReceived);
        add(Box.createVerticalStrut(10));
        add(actionButtonPanel);
    }

    private class HeaderPanel extends JPanel {
        private static final long serialVersionUID = 1L;
		private JLabel lblFrom;
        private JLabel lblPrice;
        private String username;
        private double balance;

        public HeaderPanel(String username, double balance) {
        	this.username = username;
        	this.balance = balance;
            initComponents();
            initLayout();
        }

        void initComponents() {
            lblFrom = new JLabel("FROM: " + username);
            if (balance > 0) {
                lblPrice = new JLabel(String.format("BALANCE: +%.2f €", balance));
            } else if (balance < 0) {
                lblPrice = new JLabel(String.format("BALANCE: %.2f €", balance));
            } else {
                lblPrice = new JLabel("BALANCE: 0.00 €");
            }
        }

        void initLayout() {
            // Configuración visual
            lblFrom.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblPrice.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            //Quiero que si el balance es positivo aparezca en verde y sino en rojo
            if (balance > 0) {
                lblPrice.setForeground(new Color(0, 150, 0)); // Un verde oscuro que se lea bien
            } else if (balance < 0) {
                lblPrice.setForeground(Color.RED);
            } else {
                lblPrice.setForeground(Color.BLACK); // Negro si es 0 exacto
            }
            // Layout
            setLayout(new GridLayout(2, 1));
            setOpaque(false);
            add(lblFrom);
            add(lblPrice);
        }
    }

    private class ActionButtonPanel extends JPanel {
        private static final long serialVersionUID = 1L;
		private JButton btnAccept;
        private JButton btnReject;
        private boolean alsoAccept;

        public ActionButtonPanel(boolean alsoAccept) {
        	this.alsoAccept = alsoAccept;
            initComponents();
            initLayout();
        }

        void initComponents() {
            // Solo constructores
        	if(alsoAccept == true) {
        		btnAccept = new JButton("ACCEPT");
        	}
            btnReject = new JButton("REJECT");
        }

        void initLayout() {
            // Layout
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
            setOpaque(false);
            if(alsoAccept == true) {
                btnAccept.setBackground(new Color(50, 205, 50)); 
                btnAccept.setForeground(Color.WHITE);
            	add(btnAccept);
            }
            btnReject.setBackground(Color.RED);
            btnReject.setForeground(Color.WHITE);
            add(btnReject);
        }
    }

    private class PanelDesplegable extends JPanel {
        private static final long serialVersionUID = 1L;
		private String titulo;
        private JButton toggleButton;
        private TablaProductosPanel contenidoPanel;
        private List<ProductoSegundaMano> productos;

        public PanelDesplegable(String titulo, List<ProductoSegundaMano> productos) {
            this.titulo = titulo;
            this.productos = productos;
            initComponents();
            initLayout();
        }

        void initComponents() {
            toggleButton = new JButton(titulo);
            contenidoPanel = new TablaProductosPanel(productos);
        }

        void initLayout() {
            // Configuración visual
            toggleButton.setBackground(new Color(100, 149, 237));
            toggleButton.setForeground(Color.BLACK);
            toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
            toggleButton.setFocusPainted(false);

            // Listeners (Comportamiento)
            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isVisible = contenidoPanel.isVisible();
                    contenidoPanel.setVisible(!isVisible);
                    
                    if (isVisible) {
                        toggleButton.setText(titulo.replace("▼", "▶"));
                    } else {
                        toggleButton.setText(titulo.replace("▶", "▼"));
                    }
                    revalidate();
                    repaint();
                }
            });

            // Layout
            setLayout(new BorderLayout());
            setOpaque(false);
            add(toggleButton, BorderLayout.NORTH);
            add(contenidoPanel, BorderLayout.CENTER);
        }
    }

    private class TablaProductosPanel extends JPanel {
        private static final long serialVersionUID = 1L;
		private JTable table;
        private JScrollPane scrollPane;
        private List<ProductoSegundaMano> productos;

        public TablaProductosPanel(List<ProductoSegundaMano> productos) {
        	this.productos = productos;
            initComponents();
            initLayout();
        }

        void initComponents() {
            String[] columnNames = {"Product", "Condition", "Price", "+ Info"};
            
            //Llenamos la tabla dinámicamente
            int numRows = (productos != null) ? productos.size() : 0;

            Object[][] data = new Object[numRows][4];

            if (productos != null) {
                for (int i = 0; i < productos.size(); i++) {
                    ProductoSegundaMano p = productos.get(i);

                    data[i][0] = p.getNombre();

                    if (p.getDatosValidacion() != null) {
                        data[i][1] = p.getDatosValidacion().getEstadoConservacion();
                        data[i][2] = p.getDatosValidacion().getPrecioEstimadoProducto() + " €";
                    } else {
                        data[i][1] = "Pendiente";
                        data[i][2] = "N/A";
                    }
                    
                    data[i][3] = "[ + ]"; 
                }
            }

            //Esto se mete para que no deje editar las celdas de la tabla
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; 
                }
            };

            table = new JTable(model);
            scrollPane = new JScrollPane(table);
        }
        void initLayout() {
            // Configuración visual de la tabla
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(240, 128, 128)); 
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
            table.getColumnModel().getColumn(3).setMaxWidth(60);
            
            // Listeners de la tabla
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int column = table.getColumnModel().getColumnIndexAtX(e.getX());
                    int row = e.getY() / table.getRowHeight();

                    if (row < table.getRowCount() && row >= 0 && column == 3) {
                        String productName = (String) table.getValueAt(row, 0);
                        JOptionPane.showMessageDialog(null, "Abriendo info para: " + productName);
                    }
                }
            });

            // Configuración visual del ScrollPane
            scrollPane.setPreferredSize(new Dimension(400, 105)); 

            // Layout del panel
            setLayout(new BorderLayout());
            setBorder(new LineBorder(Color.BLACK, 1));
            add(scrollPane, BorderLayout.CENTER);
        }
    }
}