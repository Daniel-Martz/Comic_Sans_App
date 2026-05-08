package vista.empleadoPanel;

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

public class ManageTimePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color BG_COLOR = new Color(162, 187, 210);
    private final Color BANNER_MAIN_COLOR = new Color(54, 119, 189);
    private final Color SECTION_BG = new Color(225, 240, 255);
    private final Color DARK_BTN = new Color(15, 45, 85);

    private HeaderPanel headerPanel;
    
    private JSpinner spinDaysPerMonth;
    private JSpinner spinMonthsPerYear;
    private JButton btnUpdateCalendar;

    private JSlider sliderSpeed;
    private JLabel lblSpeedValue;
    private JButton btnUpdateSpeed;

    private JSpinner spinAdvanceDays;
    private JButton btnAdvanceDays;

    public ManageTimePanel() {
        initComponents();
        initLayout();
    }

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

    private JPanel createSpeedSection() {
        JPanel p = createSectionWrapper("TIME SPEED MULTIPLIER");
        JLabel l1 = new JLabel("Multiplier:"); l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        p.add(l1); p.add(sliderSpeed); p.add(lblSpeedValue);
        p.add(Box.createHorizontalStrut(15));
        p.add(btnUpdateSpeed);
        return p;
    }

    private JPanel createAdvanceSection() {
        JPanel p = createSectionWrapper("ADVANCE SIMULATION");
        JLabel l1 = new JLabel("Days to advance:"); l1.setFont(new Font("SansSerif", Font.BOLD, 16));
        p.add(l1); p.add(spinAdvanceDays);
        p.add(Box.createHorizontalStrut(15));
        p.add(btnAdvanceDays);
        return p;
    }

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

    public void setControlador(ActionListener actionCtrl, ChangeListener changeCtrl) {
        btnUpdateCalendar.addActionListener(actionCtrl);
        btnUpdateSpeed.addActionListener(actionCtrl);
        btnAdvanceDays.addActionListener(actionCtrl);
        sliderSpeed.addChangeListener(changeCtrl);
    }

    public HeaderPanel getHeaderPanel() { return headerPanel; }
    public JSpinner getSpinDaysPerMonth() { return spinDaysPerMonth; }
    public JSpinner getSpinMonthsPerYear() { return spinMonthsPerYear; }
    public JSlider getSliderSpeed() { return sliderSpeed; }
    public JLabel getLblSpeedValue() { return lblSpeedValue; }
    public JSpinner getSpinAdvanceDays() { return spinAdvanceDays; }
}