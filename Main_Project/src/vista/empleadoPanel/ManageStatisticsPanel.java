package vista.empleadoPanel;

import vista.userPanels.HeaderPanel;
import modelo.tiempo.DateTimeSimulado;
import modelo.tiempo.TiempoSimulado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageStatisticsPanel.
 */
public class ManageStatisticsPanel extends JPanel {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);
    
    /** The banner main color. */
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189);

    /** The header panel. */
    private HeaderPanel headerPanel;

    /** The cb start year. */
    // Selectores de fecha
    private JComboBox<Integer> cbStartDay, cbStartMonth, cbStartYear;
    
    /** The cb end year. */
    private JComboBox<Integer> cbEndDay, cbEndMonth, cbEndYear;

    /** The btn total earnings. */
    // Botones de acción
    private JButton btnTotalEarnings;
    
    /** The sub panel total. */
    private JPanel subPanelTotal;
    
    /** The btn monthly earnings. */
    private JButton btnMonthlyEarnings;
    
    /** The btn ambit earnings. */
    private JButton btnAmbitEarnings;

    /** The btn product earnings. */
    private JButton btnProductEarnings;
    
    /** The btn client earnings. */
    private JButton btnClientEarnings;

    /**
     * Instantiates a new manage statistics panel.
     */
    public ManageStatisticsPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuGestor();

        // El tiempo simulado empieza en 0, por lo que incluimos el 0 en las opciones
        int diasPorMes = TiempoSimulado.getInstance().getDiasPorMes();
        int mesesPorAño = TiempoSimulado.getInstance().getMesesPorAño();
        Integer[] days = new Integer[diasPorMes];   for (int i = 1; i <= diasPorMes; i++) days[i-1] = i;
        Integer[] months = new Integer[mesesPorAño]; for (int i = 1; i <= mesesPorAño; i++) months[i-1] = i;
        
        DateTimeSimulado ahora = new DateTimeSimulado();
        int currentYear = Math.max(1, ahora.getAño()); // El año mínimo es 1
        int currentMonth = Math.max(1, ahora.getMes());
        int currentDay = Math.max(1, ahora.getDia());
        
        // Mostrar desde el año 1 hasta un margen por encima del año actual simulado
        int maxYear = Math.max(currentYear + 5, 15);
        Integer[] years = new Integer[maxYear];  for (int i = 1; i <= maxYear; i++) years[i-1] = i;

        cbStartDay = new JComboBox<>(days); cbStartMonth = new JComboBox<>(months); cbStartYear = new JComboBox<>(years);
        cbEndDay = new JComboBox<>(days); cbEndMonth = new JComboBox<>(months); cbEndYear = new JComboBox<>(years);
        
        cbEndDay.setSelectedItem(currentDay);
        cbEndMonth.setSelectedItem(currentMonth);
        cbEndYear.setSelectedItem(currentYear);
        
        cbStartDay.setSelectedItem(1);
        cbStartMonth.setSelectedItem(1);
        cbStartYear.setSelectedItem(1);

        Color darkBlueBtn = new Color(64, 153, 230); // Azul aún más oscuro para contraste extremo
        Color midBlueBtn = new Color(65, 136, 198);

        btnTotalEarnings = createStyledButton("TOTAL EARNINGS ▼", darkBlueBtn);
        btnMonthlyEarnings = createStyledButton("Monthly Earnings", midBlueBtn);
        btnAmbitEarnings = createStyledButton("Ambit Earnings", midBlueBtn);
        btnProductEarnings = createStyledButton("PRODUCT EARNINGS", darkBlueBtn);
        btnClientEarnings = createStyledButton("CLIENT EARNINGS", darkBlueBtn);

        // Lógica de vista para desplegar el menú de Total Earnings
        btnTotalEarnings.addActionListener(e -> {
            boolean visible = subPanelTotal.isVisible();
            subPanelTotal.setVisible(!visible);
            btnTotalEarnings.setText(visible ? "TOTAL EARNINGS ▼" : "TOTAL EARNINGS ▲");
            revalidate();
            repaint();
        });
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentWrapper = new JPanel(new BorderLayout(0, 20));
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.setBorder(new EmptyBorder(20, 100, 40, 100));

        // 1. Título
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_MAIN_COLOR);
        bannerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        JLabel lblTitle = new JLabel("MANAGE STATISTICS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);
        contentWrapper.add(bannerPanel, BorderLayout.NORTH);

        // 2. Cuerpo Central
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);

        // 2A. Fechas
        JPanel datesPanel = new JPanel(new GridLayout(2, 1, 10, 15));
        datesPanel.setBackground(new Color(225, 240, 255)); // Fondo azul clarito
        datesPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(54, 119, 189), 2),
                BorderFactory.createTitledBorder(new EmptyBorder(10, 20, 20, 20), "EXTRACTION DATE RANGE", 
                        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.BOLD, 18), Color.DARK_GRAY)
        ));
        datesPanel.setMaximumSize(new Dimension(1000, 150));
        datesPanel.setPreferredSize(new Dimension(1000, 150));
        
        datesPanel.add(createDateRow("Start Date:", cbStartDay, cbStartMonth, cbStartYear));
        datesPanel.add(createDateRow("End Date:", cbEndDay, cbEndMonth, cbEndYear));
        datesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bodyPanel.add(datesPanel);
        bodyPanel.add(Box.createVerticalStrut(30));

        // 2B. Botones de Estadísticas
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

        JLabel lblSelectStats = new JLabel("SELECT THE TYPE OF STATISTICS", SwingConstants.CENTER);
        lblSelectStats.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSelectStats.setOpaque(true);
        lblSelectStats.setBackground(new Color(20, 55, 100)); // Fondo oscuro que hace contraste
        lblSelectStats.setForeground(Color.WHITE);
        lblSelectStats.setBorder(new EmptyBorder(10, 30, 10, 30));
        lblSelectStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSelectStats.setMaximumSize(new Dimension(1000, 50));
        lblSelectStats.setPreferredSize(new Dimension(1000, 50));
        buttonsPanel.add(lblSelectStats);
        buttonsPanel.add(Box.createVerticalStrut(20));

        subPanelTotal = new JPanel(new GridLayout(2, 1, 0, 10));
        subPanelTotal.setOpaque(false);
        subPanelTotal.setBorder(new EmptyBorder(10, 40, 10, 40)); // Indentado
        subPanelTotal.add(btnMonthlyEarnings);
        subPanelTotal.add(btnAmbitEarnings);
        subPanelTotal.setVisible(false); // Oculto por defecto
        subPanelTotal.setMaximumSize(new Dimension(850, 140));

        btnTotalEarnings.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanelTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProductEarnings.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClientEarnings.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(btnTotalEarnings);
        buttonsPanel.add(subPanelTotal);
        buttonsPanel.add(Box.createVerticalStrut(15));
        buttonsPanel.add(btnProductEarnings);
        buttonsPanel.add(Box.createVerticalStrut(15));
        buttonsPanel.add(btnClientEarnings);

        bodyPanel.add(buttonsPanel);
        contentWrapper.add(bodyPanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Creates the date row.
     *
     * @param label the label
     * @param day the day
     * @param month the month
     * @param year the year
     * @return the j panel
     */
    private JPanel createDateRow(String label, JComboBox<Integer> day, JComboBox<Integer> month, JComboBox<Integer> year) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        row.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setPreferredSize(new Dimension(100, 30));
        
        Font comboFont = new Font("SansSerif", Font.PLAIN, 14);
        day.setFont(comboFont); month.setFont(comboFont); year.setFont(comboFont);
        
        row.add(lbl);
        row.add(new JLabel("Day:")); row.add(day);
        row.add(new JLabel("Month:")); row.add(month);
        row.add(new JLabel("Year:")); row.add(year);
        return row;
    }

    /**
     * Creates the styled button.
     *
     * @param text the text
     * @param baseColor the base color
     * @return the j button
     */
    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 20));
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1, true),
                new EmptyBorder(12, 20, 12, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(850, 55));
        btn.setPreferredSize(new Dimension(850, 55));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        
        return btn;
    }

    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    // Getters
    public HeaderPanel getHeaderPanel() { return headerPanel; }
    
    /**
     * Gets the cb start day.
     *
     * @return the cb start day
     */
    public JComboBox<Integer> getCbStartDay() { return cbStartDay; }
    
    /**
     * Gets the cb start month.
     *
     * @return the cb start month
     */
    public JComboBox<Integer> getCbStartMonth() { return cbStartMonth; }
    
    /**
     * Gets the cb start year.
     *
     * @return the cb start year
     */
    public JComboBox<Integer> getCbStartYear() { return cbStartYear; }
    
    /**
     * Gets the cb end day.
     *
     * @return the cb end day
     */
    public JComboBox<Integer> getCbEndDay() { return cbEndDay; }
    
    /**
     * Gets the cb end month.
     *
     * @return the cb end month
     */
    public JComboBox<Integer> getCbEndMonth() { return cbEndMonth; }
    
    /**
     * Gets the cb end year.
     *
     * @return the cb end year
     */
    public JComboBox<Integer> getCbEndYear() { return cbEndYear; }

    /**
     * Sets the controlador.
     *
     * @param l the new controlador
     */
    public void setControlador(ActionListener l) {
        btnMonthlyEarnings.setActionCommand("MONTHLY");
        btnMonthlyEarnings.addActionListener(l);
        
        btnAmbitEarnings.setActionCommand("AMBIT");
        btnAmbitEarnings.addActionListener(l);
        
        btnProductEarnings.setActionCommand("PRODUCT");
        btnProductEarnings.addActionListener(l);
        
        btnClientEarnings.setActionCommand("CLIENT");
        btnClientEarnings.addActionListener(l);
    }
}