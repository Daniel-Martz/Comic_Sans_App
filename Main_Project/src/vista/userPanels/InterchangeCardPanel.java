package vista.userPanels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import modelo.producto.ProductoSegundaMano;

/**
 * Vista que muestra una tarjeta de intercambio entre dos usuarios.
 * Sigue el patrón MVC: NO contiene lógica de negocio.
 * Solo muestra datos y expone métodos para que el controlador
 * registre listeners y actualice la vista.
 *
 * Puede mostrarse en dos modos:
 *  - INCOME (oferta recibida): muestra FROM + botones ACCEPT y REJECT
 *  - SENT   (oferta enviada):  muestra TO   + botón CANCEL
 */
public class InterchangeCardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Modo de visualización de la card.
     */
    public enum Modo { INCOME, SENT }

    // -------------------------------------------------------
    // Subpaneles internos
    // -------------------------------------------------------
    private HeaderPanel       headerPanel;
    private PanelDesplegable  panelGiven;
    private PanelDesplegable  panelReceived;
    private ActionButtonPanel actionButtonPanel;

    private ActionListener infoListener;

    /**
    /* Constructor principal
    /*
    /* @param headerLabel   texto cabecera: "FROM: <nombre>" o "TO: <nombre>"
    /* @param balance       balance calculado por el controlador
    /* @param givenData     {nombre, categoría, estado, precio} de productos dados
    /* @param receivedData  {nombre, categoría, estado, precio} de productos recibidos
    /* @param modo          INCOME (accept+reject) o SENT (cancel)
    */
    public InterchangeCardPanel(String headerLabel,
                                 double balance,
                                 ProductoSegundaMano[] givenData,
                                 ProductoSegundaMano[] receivedData,
                                 Modo modo) {
        initComponents(headerLabel, balance, givenData, receivedData, modo);
        initLayout();
    }

    /**
     * Constructor vacío: instancia la card sin datos.
     * Útil para crear la vista antes de tener el modelo listo.
     */
    public InterchangeCardPanel() {
        initComponents("", 0.0, new ProductoSegundaMano[0], new ProductoSegundaMano[0], Modo.INCOME);
        initLayout();
    }

    // -------------------------------------------------------
    // Inicialización interna
    // -------------------------------------------------------
    private void initComponents(String headerLabel,
                                  double balance,
                                  ProductoSegundaMano[] givenData,
                                  ProductoSegundaMano[] receivedData,
                                  Modo modo) {
        headerPanel       = new HeaderPanel(headerLabel, balance);
        panelGiven        = new PanelDesplegable("PRODUCTS GIVEN ▼",    givenData);
        panelReceived     = new PanelDesplegable("PRODUCTS RECEIVED ▼", receivedData);
        actionButtonPanel = new ActionButtonPanel(modo);
    }

    private void initLayout() {
        setBackground(new Color(153, 180, 209));
        setBorder(new CompoundBorder(
                new LineBorder(Color.DARK_GRAY, 2),
                new EmptyBorder(10, 10, 10, 10)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // No fijar una altura máxima rígida: permitir que la card se encoja/expanda
        // cuando se oculta/mostrar el contenido desplegable.
        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(headerPanel);
        add(Box.createVerticalStrut(8));
        add(panelGiven);
        add(Box.createVerticalStrut(8));
        add(panelReceived);
        add(Box.createVerticalStrut(8));
        add(actionButtonPanel);
    }

    // -------------------------------------------------------
    // Métodos públicos para el CONTROLADOR

    /** Registra listener del botón ACCEPT (solo modo INCOME). */
    public void addAcceptListener(ActionListener listener) {
        actionButtonPanel.addAcceptListener(listener);
    }

    /** Registra listener del botón REJECT (solo modo INCOME). */
    public void addRejectListener(ActionListener listener) {
        actionButtonPanel.addRejectListener(listener);
    }

    /** Registra listener del botón CANCEL (solo modo SENT). */
    public void addCancelListener(ActionListener listener) {
        actionButtonPanel.addCancelListener(listener);
    }

    /** Registra listener genérico para el botón "+ Info" de los productos. */
    public void setInfoListener(ActionListener listener) {
        this.infoListener = listener;
    }

    /**
     * Recarga todos los datos de la card.
     * El controlador llama a este método cuando el modelo cambia.
     */
    public void update(String headerLabel,
                       double balance,
                       ProductoSegundaMano[] givenData,
                       ProductoSegundaMano[] receivedData,
                       Modo modo) {
        removeAll();
        initComponents(headerLabel, balance, givenData, receivedData, modo);
        initLayout();
        revalidate();
        repaint();
    }

    // -------------------------------------------------------
    // Subpanel: cabecera (FROM/TO + balance)
    // -------------------------------------------------------
    private class HeaderPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public HeaderPanel(String headerLabel, double balance) {
            JLabel lblHeader = new JLabel(headerLabel);
            JLabel lblPrice;

            if (balance > 0) {
                lblPrice = new JLabel(String.format("PRICE: +%.2f €", balance));
                lblPrice.setForeground(new Color(0, 150, 0));
            } else if (balance < 0) {
                lblPrice = new JLabel(String.format("PRICE: %.2f €", balance));
                lblPrice.setForeground(Color.RED);
            } else {
                lblPrice = new JLabel(String.format("PRICE: %.2f €", balance));
                lblPrice.setForeground(Color.BLACK);
            }

            lblHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 13));
            lblPrice.setFont(new Font("Comic Sans MS", Font.BOLD, 13));

            setLayout(new GridLayout(2, 1, 0, 2));
            setOpaque(false);
            add(lblHeader);
            add(lblPrice);
        }
    }

    // -------------------------------------------------------
    // Subpanel: botones de acción
    // -------------------------------------------------------
    private class ActionButtonPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JButton btnAccept;
        private JButton btnReject;
        private JButton btnCancel;

        public ActionButtonPanel(Modo modo) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
            setOpaque(false);

            if (modo == Modo.INCOME) {
                // ACCEPT button (simple rectangular style)
                btnAccept = new JButton("ACCEPT");
                btnAccept.setBackground(new Color(50, 205, 50));
                btnAccept.setForeground(Color.WHITE);
                btnAccept.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
                btnAccept.setOpaque(true);
                btnAccept.setContentAreaFilled(true);
                btnAccept.setFocusPainted(false);
                btnAccept.setPreferredSize(new Dimension(90, 28));
                btnAccept.setMargin(new Insets(4, 10, 4, 10));
                btnAccept.setBorder(new LineBorder(new Color(120, 120, 120), 1));

                // REJECT button (simple rectangular style)
                btnReject = new JButton("REJECT");
                btnReject.setBackground(new Color(178, 34, 34));
                btnReject.setForeground(Color.WHITE);
                btnReject.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
                btnReject.setOpaque(true);
                btnReject.setContentAreaFilled(true);
                btnReject.setFocusPainted(false);
                btnReject.setPreferredSize(new Dimension(90, 28));
                btnReject.setMargin(new Insets(4, 10, 4, 10));
                btnReject.setBorder(new LineBorder(new Color(120, 120, 120), 1));

                add(btnAccept);
                add(btnReject);

                // Hover effect: change border color and slightly darken background (no shadows)
                Border normalBorder = new LineBorder(new Color(120, 120, 120), 1);
                Border hoverBorder = new LineBorder(new Color(80, 80, 80), 1);

                applyHoverEffectToButton(btnAccept, btnAccept.getBackground(), normalBorder, hoverBorder);
                applyHoverEffectToButton(btnReject, btnReject.getBackground(), normalBorder, hoverBorder);

                // Si por alguna razón no existe btnReject, marcar ACCEPT en rojo
                if (btnReject == null && btnAccept != null) {
                    btnAccept.setBackground(new Color(178, 34, 34));
                }

            } else {
                btnCancel = new JButton("CANCEL");
                btnCancel.setBackground(new Color(178, 34, 34));
                btnCancel.setForeground(Color.WHITE);
                btnCancel.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
                btnCancel.setOpaque(true);
                btnCancel.setContentAreaFilled(true);
                btnCancel.setFocusPainted(false);
                btnCancel.setPreferredSize(new Dimension(90, 28));
                btnCancel.setMargin(new Insets(4, 10, 4, 10));
                btnCancel.setBorder(new LineBorder(new Color(120, 120, 120), 1));
                add(btnCancel);
                Border normalBorderC = new LineBorder(new Color(120, 120, 120), 1);
                Border hoverBorderC = new LineBorder(new Color(80, 80, 80), 1);
                applyHoverEffectToButton(btnCancel, btnCancel.getBackground(), normalBorderC, hoverBorderC);
            }
        }

        private void applyHoverEffectToButton(final AbstractButton btn, final Color baseColor, final Border normal, final Border hover) {
            if (btn == null) return;
            btn.setBorder(normal);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBorder(hover);
                    btn.setBackground(adjustColor(baseColor, 0.92f));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBorder(normal);
                    btn.setBackground(baseColor);
                }
            });
        }

        public void addAcceptListener(ActionListener l) {
            if (btnAccept != null) btnAccept.addActionListener(l);
        }

        public void addRejectListener(ActionListener l) {
            if (btnReject != null) btnReject.addActionListener(l);
        }

        public void addCancelListener(ActionListener l) {
            if (btnCancel != null) btnCancel.addActionListener(l);
        }
    }

    // -------------------------------------------------------
    // Subpanel: sección desplegable con tabla
    // -------------------------------------------------------
    private class PanelDesplegable extends JPanel {
        private static final long serialVersionUID = 1L;

        private final String            titulo;
        private JButton                 toggleButton;
        private TablaProductosPanel     contenidoPanel;

        public PanelDesplegable(String titulo, ProductoSegundaMano[] data) {
            this.titulo = titulo;

            toggleButton   = new JButton(titulo);
            contenidoPanel = new TablaProductosPanel(data);

            // Iniciar el panel desplegable cerrado por defecto para un aspecto
            // más compacto y predecible.
            contenidoPanel.setVisible(false);
            // Actualizar el texto del botón para mostrar el símbolo de cerrado
            toggleButton.setText(this.titulo.replace("▼", "▶"));

            toggleButton.setBackground(new Color(100, 149, 237));
            toggleButton.setForeground(Color.BLACK);
            toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
            toggleButton.setFocusPainted(false);

            toggleButton.addActionListener(e -> {
                boolean visible = contenidoPanel.isVisible();
                contenidoPanel.setVisible(!visible);
                // Mostrar la flecha correcta en función del nuevo estado
                toggleButton.setText(contenidoPanel.isVisible()
                        ? this.titulo.replace("▶", "▼")
                        : this.titulo.replace("▼", "▶"));
                // Revalidar / repintar la card contenedora para que el JScrollPane
                // en el padre actualice correctamente su tamaño.
                InterchangeCardPanel.this.revalidate();
                InterchangeCardPanel.this.repaint();
            });

            setLayout(new BorderLayout());
            setOpaque(false);
            add(toggleButton,   BorderLayout.NORTH);
            add(contenidoPanel, BorderLayout.CENTER);
        }
    }

    // -------------------------------------------------------
    // Subpanel: tabla de productos
    // Columnas: Product | Condition | Price | + Info
    // -------------------------------------------------------
    private class TablaProductosPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public TablaProductosPanel(ProductoSegundaMano[] data) {
            // 1. Nombres de columnas sin "Category"
            String[] columnNames = {"Product", "Condition", "Price", "+ Info"};

            // 2. Reducimos el array a 4 columnas
            Object[][] tableData = new Object[data.length][4];
            for (int i = 0; i < data.length; i++) {
                tableData[i][0] = data[i] != null ? data[i].getNombre() : "";
                
                tableData[i][1] = data[i] != null && data[i].getDatosValidacion() != null
                        ? data[i].getDatosValidacion().getEstadoConservacion().toString()
                        : "Pendiente";
                tableData[i][2] = data[i] != null && data[i].getDatosValidacion() != null
                        ? String.format("%.2f €", data[i].getDatosValidacion().getPrecioEstimadoProducto())
                        : "N/A";
                tableData[i][3] = "+"; // El botón
            }

            DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable table = new JTable(model);
            table.setRowHeight(22);
            table.getTableHeader().setBackground(new Color(240, 128, 128));
            table.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            
            // 3. La columna del botón ahora es la de índice 3 (0, 1, 2, 3)
            table.getColumnModel().getColumn(3).setMaxWidth(50);
            table.getColumn("+ Info").setCellRenderer(new ButtonRenderer());

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    
                    // 4. Escuchamos el clic en el nuevo índice 3
                    if (row >= 0 && col == 3) {
                        if (InterchangeCardPanel.this.infoListener != null && data[row] != null) {
                            InterchangeCardPanel.this.infoListener.actionPerformed(
                                new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "INFO_" + data[row].getID())
                            );
                        }
                    }
                }
            });

            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(420, 100));

            setLayout(new BorderLayout());
            setBorder(new LineBorder(Color.BLACK, 1));
            add(scroll, BorderLayout.CENTER);
        }
    }
    // -------------------------------------------------------
    // Renderer: botón verde "+" en la columna "+ Info"
    // -------------------------------------------------------
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(50, 205, 50));
            setForeground(Color.WHITE);
            setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "+" : value.toString());
            return this;
        }
    }

    private static Color adjustColor(Color c, float factor) {
        int r = Math.min(255, Math.max(0, Math.round(c.getRed() * factor)));
        int g = Math.min(255, Math.max(0, Math.round(c.getGreen() * factor)));
        int b = Math.min(255, Math.max(0, Math.round(c.getBlue() * factor)));
        return new Color(r, g, b, c.getAlpha());
    }

    // (removed custom rounded button helper — using plain JButtons with rectangular border)

    /**
     * Método estático de prueba para mostrar este panel aislado.
     * Puedes ejecutarlo desde Eclipse o desde la línea de comandos
     * llamando a InterchangeCardPanel.mostrarPanelDePrueba();
     */
    public static void mostrarPanelDePrueba() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prueba InterchangeCardPanel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            InterchangeCardPanel p1 = new InterchangeCardPanel(
                    "FROM: Alice", 12.50,
                    new ProductoSegundaMano[0],
                    new ProductoSegundaMano[0],
                    Modo.INCOME);

            InterchangeCardPanel p2 = new InterchangeCardPanel(
                    "TO: Bob", 0.0,
                    new ProductoSegundaMano[0],
                    new ProductoSegundaMano[0],
                    Modo.SENT);

            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(new Color(153, 180, 209));
            container.setBorder(new EmptyBorder(10, 10, 10, 10));
            container.add(p1);
            container.add(Box.createVerticalStrut(10));
            container.add(p2);

            JScrollPane scroll = new JScrollPane(container,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.getVerticalScrollBar().setUnitIncrement(16);

            frame.getContentPane().add(scroll);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /**
     * Pequeño main para facilitar ejecución rápida desde línea de comandos.
     */
    public static void main(String[] args) {
        mostrarPanelDePrueba();
    }
}
