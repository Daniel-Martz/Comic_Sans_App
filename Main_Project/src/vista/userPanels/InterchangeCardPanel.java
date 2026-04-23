package vista.userPanels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

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
                                 String[][] givenData,
                                 String[][] receivedData,
                                 Modo modo) {
        initComponents(headerLabel, balance, givenData, receivedData, modo);
        initLayout();
    }

    /**
     * Constructor vacío: instancia la card sin datos.
     * Útil para crear la vista antes de tener el modelo listo.
     */
    public InterchangeCardPanel() {
        initComponents("", 0.0, new String[0][0], new String[0][0], Modo.INCOME);
        initLayout();
    }

    // -------------------------------------------------------
    // Inicialización interna
    // -------------------------------------------------------
    private void initComponents(String headerLabel,
                                  double balance,
                                  String[][] givenData,
                                  String[][] receivedData,
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
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

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

    /**
     * Recarga todos los datos de la card.
     * El controlador llama a este método cuando el modelo cambia.
     */
    public void update(String headerLabel,
                       double balance,
                       String[][] givenData,
                       String[][] receivedData,
                       Modo modo) {
        removeAll();
        initComponents(headerLabel, balance, givenData, receivedData, modo);
        initLayout();
        revalidate();
        repaint();
    }

    /** Muestra un diálogo de error. */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Muestra un diálogo informativo. */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Info", JOptionPane.INFORMATION_MESSAGE);
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
                lblPrice = new JLabel("PRICE: 0.00 €");
                lblPrice.setForeground(Color.BLACK);
            }

            lblHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblPrice.setFont(new Font("SansSerif", Font.BOLD, 13));

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
                btnAccept = new JButton("ACEPT");
                btnAccept.setBackground(new Color(50, 205, 50));
                btnAccept.setForeground(Color.WHITE);
                btnAccept.setFont(new Font("SansSerif", Font.BOLD, 12));

                btnReject = new JButton("REJECT");
                btnReject.setBackground(Color.RED);
                btnReject.setForeground(Color.WHITE);
                btnReject.setFont(new Font("SansSerif", Font.BOLD, 12));

                add(btnAccept);
                add(btnReject);

            } else {
                btnCancel = new JButton("CANCEL");
                btnCancel.setBackground(Color.RED);
                btnCancel.setForeground(Color.WHITE);
                btnCancel.setFont(new Font("SansSerif", Font.BOLD, 12));
                add(btnCancel);
            }
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

        public PanelDesplegable(String titulo, String[][] data) {
            this.titulo = titulo;

            toggleButton   = new JButton(titulo);
            contenidoPanel = new TablaProductosPanel(data);

            toggleButton.setBackground(new Color(100, 149, 237));
            toggleButton.setForeground(Color.BLACK);
            toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
            toggleButton.setFocusPainted(false);

            toggleButton.addActionListener(e -> {
                boolean visible = contenidoPanel.isVisible();
                contenidoPanel.setVisible(!visible);
                toggleButton.setText(visible
                        ? this.titulo.replace("▼", "▶")
                        : this.titulo.replace("▶", "▼"));
                revalidate();
                repaint();
            });

            setLayout(new BorderLayout());
            setOpaque(false);
            add(toggleButton,   BorderLayout.NORTH);
            add(contenidoPanel, BorderLayout.CENTER);
        }
    }

    // -------------------------------------------------------
    // Subpanel: tabla de productos
    // Columnas: Product | Category | Condition | Price | + Info
    // data[][]: {nombre, categoría, estado, precio}
    // -------------------------------------------------------
    private class TablaProductosPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public TablaProductosPanel(String[][] data) {
            String[] columnNames = {"Product", "Category", "Condition", "Price", "+ Info"};

            Object[][] tableData = new Object[data.length][5];
            for (int i = 0; i < data.length; i++) {
                tableData[i][0] = data[i].length > 0 ? data[i][0] : "";
                tableData[i][1] = data[i].length > 1 ? data[i][1] : "";
                tableData[i][2] = data[i].length > 2 ? data[i][2] : "";
                tableData[i][3] = data[i].length > 3 ? data[i][3] : "";
                tableData[i][4] = "+";
            }

            DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable table = new JTable(model);
            table.setRowHeight(22);
            table.getTableHeader().setBackground(new Color(240, 128, 128));
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
            table.getColumnModel().getColumn(4).setMaxWidth(50);
            table.getColumn("+ Info").setCellRenderer(new ButtonRenderer());

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int col = table.columnAtPoint(e.getPoint());
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && col == 4) {
                        JOptionPane.showMessageDialog(null,
                                "Producto: "   + table.getValueAt(row, 0) + "\n" +
                                "Categoría: "  + table.getValueAt(row, 1) + "\n" +
                                "Estado: "     + table.getValueAt(row, 2) + "\n" +
                                "Precio: "     + table.getValueAt(row, 3),
                                "Detalles del producto",
                                JOptionPane.INFORMATION_MESSAGE);
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
            setFont(new Font("SansSerif", Font.BOLD, 12));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "+" : value.toString());
            return this;
        }
    }
}