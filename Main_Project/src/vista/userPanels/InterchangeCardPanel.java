package vista.userPanels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import modelo.producto.ProductoSegundaMano;

// TODO: Auto-generated Javadoc
/**
 * Tarjeta super vitaminada para ver una solicitud de intercambio entera.
 */
public class InterchangeCardPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Modo de visualización de la card.
     */
    public enum Modo { 
 /** The income. */
 INCOME, 
 /** The sent. */
 SENT, 
 /** The employee. */
 EMPLOYEE }

    // -------------------------------------------------------
    // Subpaneles internos
    /** The header panel. */
    // -------------------------------------------------------
    private HeaderPanel       headerPanel;
    
    /** The panel given. */
    private PanelDesplegable  panelGiven;
    
    /** The panel received. */
    private PanelDesplegable  panelReceived;
    
    /** The action button panel. */
    private ActionButtonPanel actionButtonPanel;

    /** The info listener. */
    private ActionListener infoListener;
    
    /** The has buttons. */
    private boolean hasButtons = true;

    /**
     *     /* Constructor principal
     *     /*
     *     /* @param headerLabel   texto cabecera: "FROM: <nombre>" o "TO: <nombre>"
     *     /* @param balance       balance calculado por el controlador
     *     /* @param givenData     {nombre, categoría, estado, precio} de productos dados
     *     /* @param receivedData  {nombre, categoría, estado, precio} de productos recibidos
     *     /* @param modo          INCOME (accept+reject) o SENT (cancel).
     *
     * @param headerLabel the header label
     * @param balance the balance
     * @param givenData the given data
     * @param receivedData the received data
     * @param modo the modo
     * @param customGivenTitle the custom given title
     * @param customReceivedTitle the custom received title
     */
    public InterchangeCardPanel(String headerLabel,
                                 double balance,
                                 ProductoSegundaMano[] givenData,
                                 ProductoSegundaMano[] receivedData,
                                 Modo modo,
                                 String customGivenTitle,
                                 String customReceivedTitle) {
        initComponents(headerLabel, balance, givenData, receivedData, modo, customGivenTitle, customReceivedTitle);
        initLayout();
    }
    
    /**
     * Instantiates a new interchange card panel.
     *
     * @param headerLabel the header label
     * @param balance the balance
     * @param givenData the given data
     * @param receivedData the received data
     * @param modo the modo
     * @param customGivenTitle the custom given title
     * @param customReceivedTitle the custom received title
     * @param hasButtons the has buttons
     */
    public InterchangeCardPanel(String headerLabel,
                                 double balance,
                                 ProductoSegundaMano[] givenData,
                                 ProductoSegundaMano[] receivedData,
                                 Modo modo,
                                 String customGivenTitle,
                                 String customReceivedTitle,
                                boolean hasButtons) {
        this.hasButtons = hasButtons;
        initComponents(headerLabel, balance, givenData, receivedData, modo, customGivenTitle, customReceivedTitle);
        initLayout();
    }

    /**
     * Constructor vacío por si necesitas instanciarlo a capón.
     */
    public InterchangeCardPanel() {
        initComponents("", 0.0, new ProductoSegundaMano[0], new ProductoSegundaMano[0], Modo.INCOME, null, null);
        initLayout();
    }


    /**
     * Inits the components.
     *
     * @param headerLabel the header label
     * @param balance the balance
     * @param givenData the given data
     * @param receivedData the received data
     * @param modo the modo
     * @param customGivenTitle the custom given title
     * @param customReceivedTitle the custom received title
     */
    private void initComponents(String headerLabel,
                                  double balance,
                                  ProductoSegundaMano[] givenData,
                                  ProductoSegundaMano[] receivedData,
                                  Modo modo,
                                  String customGivenTitle,
                                  String customReceivedTitle) {
        headerPanel       = new HeaderPanel(headerLabel, balance);
        
        String givenTitle = customGivenTitle != null ? customGivenTitle : "PRODUCTS GIVEN ▼";
        String receivedTitle = customReceivedTitle != null ? customReceivedTitle : "PRODUCTS RECEIVED ▼";
        if (modo == Modo.EMPLOYEE) {
            if (customGivenTitle == null) givenTitle = "USER 1 PRODUCTS ▼";
            if (customReceivedTitle == null) receivedTitle = "USER 2 PRODUCTS ▼";
        }
        
        panelGiven        = new PanelDesplegable(givenTitle,    givenData);
        panelReceived     = new PanelDesplegable(receivedTitle, receivedData);
        actionButtonPanel = new ActionButtonPanel(modo);
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setBackground(new Color(153, 180, 209));
        setBorder(new CompoundBorder(
                new LineBorder(Color.DARK_GRAY, 2),
                new EmptyBorder(10, 10, 10, 10)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(headerPanel);
        add(Box.createVerticalStrut(8));
        add(panelGiven);
        add(Box.createVerticalStrut(8));
        add(panelReceived);
        add(Box.createVerticalStrut(8));
        if(hasButtons) add(actionButtonPanel);
    }

    // -------------------------------------------------------
    // Métodos públicos para el CONTROLADOR

    /**
     * Registra listener del botón ACCEPT (solo modo INCOME).
     *
     * @param listener the listener
     */
    public void addAcceptListener(ActionListener listener) {
        actionButtonPanel.addAcceptListener(listener);
    }

    /**
     * Registra listener del botón REJECT (solo modo INCOME).
     *
     * @param listener the listener
     */
    public void addRejectListener(ActionListener listener) {
        actionButtonPanel.addRejectListener(listener);
    }

    /**
     * Registra listener del botón CANCEL (solo modo SENT).
     *
     * @param listener the listener
     */
    public void addCancelListener(ActionListener listener) {
        actionButtonPanel.addCancelListener(listener);
    }

    /**
     * Registra listener genérico para el botón "+ Info" de los productos.
     *
     * @param listener the new info listener
     */
    public void setInfoListener(ActionListener listener) {
        this.infoListener = listener;
    }

    /**
     * Recarga todos los datos de la card.
     * El controlador llama a este método cuando el modelo cambia.
     *
     * @param headerLabel the header label
     * @param balance the balance
     * @param givenData the given data
     * @param receivedData the received data
     * @param modo the modo
     */
    public void update(String headerLabel,
                       double balance,
                       ProductoSegundaMano[] givenData,
                       ProductoSegundaMano[] receivedData,
                       Modo modo) {
        removeAll();
        initComponents(headerLabel, balance, givenData, receivedData, modo, null, null);
        initLayout();
        revalidate();
        repaint();
    }
    
    /**
     * The Class HeaderPanel.
     */
    private class HeaderPanel extends JPanel {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new header panel.
         *
         * @param headerLabel the header label
         * @param balance the balance
         */
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


    /**
     * The Class ActionButtonPanel.
     */
    private class ActionButtonPanel extends JPanel {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The btn accept. */
        private JButton btnAccept;
        
        /** The btn reject. */
        private JButton btnReject;
        
        /** The btn cancel. */
        private JButton btnCancel;

        /**
         * Instantiates a new action button panel.
         *
         * @param modo the modo
         */
        public ActionButtonPanel(Modo modo) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
            setOpaque(false);

            if (modo == Modo.INCOME) {
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
                Border normalBorder = new LineBorder(new Color(120, 120, 120), 1);
                Border hoverBorder = new LineBorder(new Color(80, 80, 80), 1);

                applyHoverEffectToButton(btnAccept, btnAccept.getBackground(), normalBorder, hoverBorder);
                applyHoverEffectToButton(btnReject, btnReject.getBackground(), normalBorder, hoverBorder);

                if (btnReject == null && btnAccept != null) {
                    btnAccept.setBackground(new Color(178, 34, 34));
                }

            } else if (modo == Modo.SENT) {
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
            } else if (modo == Modo.EMPLOYEE) {
                btnAccept = new JButton("APPROVE");
                btnAccept.setBackground(new Color(50, 205, 50));
                btnAccept.setForeground(Color.WHITE);
                btnAccept.setFont(new Font("Comic Sans MS", Font.BOLD, 11));
                btnAccept.setOpaque(true);
                btnAccept.setContentAreaFilled(true);
                btnAccept.setFocusPainted(false);
                btnAccept.setPreferredSize(new Dimension(90, 28));
                btnAccept.setMargin(new Insets(4, 10, 4, 10));
                btnAccept.setBorder(new LineBorder(new Color(120, 120, 120), 1));
                add(btnAccept);

                Border normalBorder = new LineBorder(new Color(120, 120, 120), 1);
                Border hoverBorder = new LineBorder(new Color(80, 80, 80), 1);

                applyHoverEffectToButton(btnAccept, btnAccept.getBackground(), normalBorder, hoverBorder);
            }
        }

        /**
         * Apply hover effect to button.
         *
         * @param btn the btn
         * @param baseColor the base color
         * @param normal the normal
         * @param hover the hover
         */
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

        /**
         * Adds the accept listener.
         *
         * @param l the l
         */
        public void addAcceptListener(ActionListener l) {
            if (btnAccept != null) btnAccept.addActionListener(l);
        }

        /**
         * Adds the reject listener.
         *
         * @param l the l
         */
        public void addRejectListener(ActionListener l) {
            if (btnReject != null) btnReject.addActionListener(l);
        }

        /**
         * Adds the cancel listener.
         *
         * @param l the l
         */
        public void addCancelListener(ActionListener l) {
            if (btnCancel != null) btnCancel.addActionListener(l);
        }
    }


    /**
     * The Class PanelDesplegable.
     */
    private class PanelDesplegable extends JPanel {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The titulo. */
        private final String            titulo;
        
        /** The toggle button. */
        private JButton                 toggleButton;
        
        /** The contenido panel. */
        private TablaProductosPanel     contenidoPanel;

        /**
         * Instantiates a new panel desplegable.
         *
         * @param titulo the titulo
         * @param data the data
         */
        public PanelDesplegable(String titulo, ProductoSegundaMano[] data) {
            this.titulo = titulo;

            toggleButton   = new JButton(titulo);
            contenidoPanel = new TablaProductosPanel(data);
            contenidoPanel.setVisible(false);

            toggleButton.setText(this.titulo.replace("▼", "▶"));

            toggleButton.setBackground(new Color(100, 149, 237));
            toggleButton.setForeground(Color.BLACK);
            toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
            toggleButton.setFocusPainted(false);

            toggleButton.addActionListener(e -> {
                boolean visible = contenidoPanel.isVisible();
                contenidoPanel.setVisible(!visible);
                toggleButton.setText(contenidoPanel.isVisible()
                        ? this.titulo.replace("▶", "▼")
                        : this.titulo.replace("▼", "▶"));
                InterchangeCardPanel.this.revalidate();
                InterchangeCardPanel.this.repaint();
            });

            setLayout(new BorderLayout());
            setOpaque(false);
            add(toggleButton,   BorderLayout.NORTH);
            add(contenidoPanel, BorderLayout.CENTER);
        }
    }


    /**
     * The Class TablaProductosPanel.
     */
    private class TablaProductosPanel extends JPanel {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new tabla productos panel.
         *
         * @param data the data
         */
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
                tableData[i][3] = "+"; 
            }

            DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable table = new JTable(model);
            table.setRowHeight(22);
            table.getTableHeader().setBackground(new Color(240, 128, 128));
            table.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 11));
            
            table.getColumnModel().getColumn(3).setMaxWidth(50);
            table.getColumn("+ Info").setCellRenderer(new ButtonRenderer());

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
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

    /**
     * The Class ButtonRenderer.
     */
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new button renderer.
         */
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(50, 205, 50));
            setForeground(Color.WHITE);
            setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        }

        /**
         * Gets the table cell renderer component.
         *
         * @param table the table
         * @param value the value
         * @param isSelected the is selected
         * @param hasFocus the has focus
         * @param row the row
         * @param column the column
         * @return the table cell renderer component
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "+" : value.toString());
            return this;
        }
    }

    /**
     * Adjust color.
     *
     * @param c the c
     * @param factor the factor
     * @return the color
     */
    private static Color adjustColor(Color c, float factor) {
        int r = Math.min(255, Math.max(0, Math.round(c.getRed() * factor)));
        int g = Math.min(255, Math.max(0, Math.round(c.getGreen() * factor)));
        int b = Math.min(255, Math.max(0, Math.round(c.getBlue() * factor)));
        return new Color(r, g, b, c.getAlpha());
    }
}
