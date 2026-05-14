package vista.GestorPanel;

import vista.userPanels.HeaderPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageTimePanel.
 */
public class ManageTimePanel extends JPanel {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);
    
    /** The banner main color. */
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189);
    
    /** The section bg. */
    private final Color SECTION_BG = new Color(225, 240, 255);
    
    /** The dark btn. */
    private final Color DARK_BTN = new Color(15, 45, 85);

    /** The header panel. */
    private HeaderPanel headerPanel;
    
    /** The spin days per month. */
    private JSpinner spinDaysPerMonth;
    
    /** The spin months per year. */
    private JSpinner spinMonthsPerYear;
    
    /** The btn update calendar. */
    private JButton btnUpdateCalendar;

    /** The slider speed. */
    private JSlider sliderSpeed;
    
    /** The lbl speed value. */
    private JLabel lblSpeedValue;
    
    /** The btn update speed. */
    private JButton btnUpdateSpeed;

    /** The spin advance days. */
    private JSpinner spinAdvanceDays;
    
    /** The btn advance days. */
    private JButton btnAdvanceDays;

    /**
     * Instantiates a new manage time panel.
     */
    public ManageTimePanel() {
        initComponents();
        initLayout();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuGestor();

        // Controles Calendario
        spinDaysPerMonth = new JSpinner(new SpinnerNumberModel(30, 1, 365, 1));
        spinMonthsPerYear = new JSpinner(new SpinnerNumberModel(12, 1, 100, 1));
        btnUpdateCalendar = createStyledButton("APPLY", DARK_BTN, "UPDATE_CALENDAR");

        // Controles Velocidad
        sliderSpeed = new JSlider(0, 100, 10);
        sliderSpeed.setMajorTickSpacing(10);
        sliderSpeed.setPaintTicks(true);
        sliderSpeed.setBackground(SECTION_BG);
        lblSpeedValue = new JLabel("1.0x");
        lblSpeedValue.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnUpdateSpeed = createStyledButton("APPLY", DARK_BTN, "UPDATE_SPEED");

        // Controles Avanzar Tiempo
        spinAdvanceDays = new JSpinner(new SpinnerNumberModel(1, 1, 3650, 1));
        btnAdvanceDays = createStyledButton("ADVANCE", DARK_BTN, "ADVANCE_TIME");
        
        Font spinFont = new Font("SansSerif", Font.PLAIN, 18);
        spinDaysPerMonth.setFont(spinFont);
        spinMonthsPerYear.setFont(spinFont);
        spinAdvanceDays.setFont(spinFont);
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

        // Título Principal
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_MAIN_COLOR);
        bannerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 1));
        JLabel lblTitle = new JLabel("MANAGE TIME & CALENDAR", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);
        contentWrapper.add(bannerPanel, BorderLayout.NORTH);

        // Cuerpo Central con las 3 secciones
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);

        bodyPanel.add(createCalendarSection());
        bodyPanel.add(Box.createVerticalStrut(25));
        bodyPanel.add(createSpeedSection());
        bodyPanel.add(Box.createVerticalStrut(25));
        bodyPanel.add(createAdvanceSection());

        contentWrapper.add(bodyPanel, BorderLayout.CENTER);
        add(contentWrapper, BorderLayout.CENTER);
    }

    /**
     * Creates the section wrapper.
     *
     * @param title the title
     * @return the j panel
     */
    private JPanel createSectionWrapper(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        p.setBackground(SECTION_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BANNER_MAIN_COLOR, 2),
                BorderFactory.createTitledBorder(new EmptyBorder(10, 20, 10, 20), title,
                        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.BOLD, 18), Color.DARK_GRAY)
        ));
        p.setMaximumSize(new Dimension(1000, 130));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }

    /**
     * Creates the calendar section.
     *
     * @return the j panel
     */
    private JPanel createCalendarSection() {
        JPanel p = createSectionWrapper("CALENDAR CONFIGURATION");
        JLabel l1 = new JLabel("Days/Month:"); l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel l2 = new JLabel("Months/Year:"); l2.setFont(new Font("SansSerif", Font.BOLD, 16));
        p.add(l1); p.add(spinDaysPerMonth);
        p.add(Box.createHorizontalStrut(15));
        p.add(l2); p.add(spinMonthsPerYear);
        p.add(Box.createHorizontalStrut(15));
        p.add(btnUpdateCalendar);
        return p;
    }

    /**
     * Creates the speed section.
     *
     * @return the j panel
     */
    private JPanel createSpeedSection() {
        JPanel p = createSectionWrapper("TIME SPEED MULTIPLIER");
        JLabel l1 = new JLabel("Multiplier:"); l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        p.add(l1); p.add(sliderSpeed); p.add(lblSpeedValue);
        p.add(Box.createHorizontalStrut(15));
        p.add(btnUpdateSpeed);
        return p;
    }

    /**
     * Creates the advance section.
     *
     * @return the j panel
     */
    private JPanel createAdvanceSection() {
        JPanel p = createSectionWrapper("ADVANCE SIMULATION");
        JLabel l1 = new JLabel("Days to advance:"); l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        p.add(l1); p.add(spinAdvanceDays);
        p.add(Box.createHorizontalStrut(15));
        p.add(btnAdvanceDays);
        return p;
    }

    /**
     * Creates the styled button.
     *
     * @param text the text
     * @param baseColor the base color
     * @param actionCommand the action command
     * @return the j button
     */
    private JButton createStyledButton(String text, Color baseColor, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setActionCommand(actionCommand);
        btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.DARK_GRAY, 1, true), new EmptyBorder(8, 15, 8, 15)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) { btn.setBackground(baseColor.darker()); } @Override public void mouseExited(MouseEvent e) { btn.setBackground(baseColor); } });
        return btn;
    }

    /**
     * Sets the controlador.
     *
     * @param actionCtrl the action ctrl
     * @param changeCtrl the change ctrl
     */
    public void setControlador(ActionListener actionCtrl, ChangeListener changeCtrl) {
        btnUpdateCalendar.addActionListener(actionCtrl);
        btnUpdateSpeed.addActionListener(actionCtrl);
        btnAdvanceDays.addActionListener(actionCtrl);
        sliderSpeed.addChangeListener(changeCtrl);
    }

    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() { return headerPanel; }
    
    /**
     * Gets the spin days per month.
     *
     * @return the spin days per month
     */
    public JSpinner getSpinDaysPerMonth() { return spinDaysPerMonth; }
    
    /**
     * Gets the spin months per year.
     *
     * @return the spin months per year
     */
    public JSpinner getSpinMonthsPerYear() { return spinMonthsPerYear; }
    
    /**
     * Gets the slider speed.
     *
     * @return the slider speed
     */
    public JSlider getSliderSpeed() { return sliderSpeed; }
    
    /**
     * Gets the lbl speed value.
     *
     * @return the lbl speed value
     */
    public JLabel getLblSpeedValue() { return lblSpeedValue; }
    
    /**
     * Gets the spin advance days.
     *
     * @return the spin advance days
     */
    public JSpinner getSpinAdvanceDays() { return spinAdvanceDays; }
}