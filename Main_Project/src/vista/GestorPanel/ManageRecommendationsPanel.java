package vista.GestorPanel;

import vista.userPanels.HeaderPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageRecommendationsPanel.
 */
public class ManageRecommendationsPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The header panel. */
    private HeaderPanel headerPanel;
    
    /** The btn left arrow. */
    private JButton btnLeftArrow;
    
    /** The btn right arrow. */
    private JButton btnRightArrow;
    
    /** The selected number. */
    private int selectedNumber = 5;
    
    /** The number labels. */
    private List<JLabel> numberLabels;
    
    /** The row labels. */
    private List<JLabel> rowLabels;
    
    /** The reorder buttons. */
    private List<JButton> reorderButtons;

    /** The bg color. */
    private final Color BG_COLOR = new Color(162, 187, 210);
    
    /** The banner color. */
    private final Color BANNER_COLOR = new Color(20, 60, 100); 
    
    /** The section banner color. */
    private final Color SECTION_BANNER_COLOR = new Color(100, 120, 160); // purple/light blue

    /**
     * Instantiates a new manage recommendations panel.
     */
    public ManageRecommendationsPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        headerPanel = new HeaderPanel();
        headerPanel.configurarMenuGestor(); 

        numberLabels = new ArrayList<>();
        rowLabels = new ArrayList<>();
        reorderButtons = new ArrayList<>();
        
        btnLeftArrow = new JButton("<");
        btnLeftArrow.setContentAreaFilled(false);
        btnLeftArrow.setFocusPainted(false);
        btnLeftArrow.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnLeftArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLeftArrow.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        btnRightArrow = new JButton(">");
        btnRightArrow.setContentAreaFilled(false);
        btnRightArrow.setFocusPainted(false);
        btnRightArrow.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRightArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRightArrow.setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Header at the top
        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyContent = new JPanel(new BorderLayout());
        bodyContent.setOpaque(false);
        
        // Main Title Banner
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(BANNER_COLOR);
        JLabel lblTitle = new JLabel("MANAGE RECOMMENDATIONS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(15, 0, 15, 0));
        bannerPanel.add(lblTitle, BorderLayout.CENTER);
        
        bodyContent.add(bannerPanel, BorderLayout.NORTH);

        // Central Container aligned to center
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setOpaque(false);

        // SECTION A: Number of recommended products
        JPanel sectionA = new JPanel(new BorderLayout());
        sectionA.setOpaque(false);
        sectionA.setMaximumSize(new Dimension(400, 100));

        JLabel lblTitleA = new JLabel("Number of recommended products", SwingConstants.CENTER);
        lblTitleA.setOpaque(true);
        lblTitleA.setBackground(SECTION_BANNER_COLOR);
        lblTitleA.setForeground(Color.WHITE);
        lblTitleA.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitleA.setBorder(new EmptyBorder(10, 10, 10, 10));
        sectionA.add(lblTitleA, BorderLayout.NORTH);

        JPanel numberSelectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        numberSelectorPanel.setOpaque(false);
        numberSelectorPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(new LineBorder(Color.GRAY, 1));

        controlPanel.add(btnLeftArrow);

        for (int i = 1; i <= 5; i++) {
            JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
            sep.setPreferredSize(new Dimension(1, 30));
            sep.setForeground(Color.LIGHT_GRAY);
            controlPanel.add(sep);

            JLabel lblNum = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            lblNum.setPreferredSize(new Dimension(40, 30));
            lblNum.setOpaque(true);
            if (i == selectedNumber) {
                lblNum.setBackground(Color.LIGHT_GRAY);
                lblNum.setFont(new Font("SansSerif", Font.BOLD, 16));
            } else {
                lblNum.setBackground(Color.WHITE);
                lblNum.setFont(new Font("SansSerif", Font.PLAIN, 16));
            }
            numberLabels.add(lblNum);
            controlPanel.add(lblNum);
        }

        JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);
        sep2.setPreferredSize(new Dimension(1, 30));
        sep2.setForeground(Color.LIGHT_GRAY);
        controlPanel.add(sep2);

        controlPanel.add(btnRightArrow);

        numberSelectorPanel.add(controlPanel);
        sectionA.add(numberSelectorPanel, BorderLayout.CENTER);

        // SECTION B: Recommendation Order
        JPanel sectionB = new JPanel(new BorderLayout());
        sectionB.setOpaque(false);
        sectionB.setMaximumSize(new Dimension(400, 250));

        JPanel headerB = new JPanel(new BorderLayout());
        headerB.setOpaque(false);

        JLabel lblTitleB = new JLabel("Recommendation Order", SwingConstants.CENTER);
        lblTitleB.setOpaque(true);
        lblTitleB.setBackground(SECTION_BANNER_COLOR);
        lblTitleB.setForeground(Color.WHITE);
        lblTitleB.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitleB.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerB.add(lblTitleB, BorderLayout.CENTER);

        sectionB.add(headerB, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        String[] rows = {"1. Interests of User", "2. Puntuations", "3. New Products"};
        for (String rowText : rows) {
            JPanel rowPanel = new JPanel(new BorderLayout());
            rowPanel.setBackground(new Color(240, 248, 255)); // Very light blue background
            rowPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.LIGHT_GRAY, 1),
                    new EmptyBorder(10, 15, 10, 15)
            ));
            rowPanel.setMaximumSize(new Dimension(400, 45));
            
            JLabel lblText = new JLabel(rowText);
            lblText.setFont(new Font("SansSerif", Font.PLAIN, 14));
            rowLabels.add(lblText);
            rowPanel.add(lblText, BorderLayout.CENTER);
            
            JButton btnReorder = new JButton("↕");
            btnReorder.setContentAreaFilled(false);
            btnReorder.setBorderPainted(false);
            btnReorder.setFocusPainted(false);
            btnReorder.setFont(new Font("SansSerif", Font.BOLD, 16));
            btnReorder.setCursor(new Cursor(Cursor.HAND_CURSOR));
            reorderButtons.add(btnReorder);
            
            rowPanel.add(btnReorder, BorderLayout.EAST);
            
            listPanel.add(rowPanel);
            listPanel.add(Box.createVerticalStrut(5));
        }

        sectionB.add(listPanel, BorderLayout.CENTER);

        // Add sections to central container
        centerContainer.add(sectionA);
        centerContainer.add(Box.createVerticalStrut(40)); // Space between sections
        centerContainer.add(sectionB);

        centerWrapper.add(centerContainer);

        bodyContent.add(centerWrapper, BorderLayout.CENTER);

        add(bodyContent, BorderLayout.CENTER);
    }
    
    /**
     * Updates the UI to highlight the selected number.
     * @param num the number to select (1-5)
     */
    public void setSelectedNumber(int num) {
        if (num < 1 || num > 5) return;
        this.selectedNumber = num;
        for (int i = 0; i < numberLabels.size(); i++) {
            JLabel lbl = numberLabels.get(i);
            if (i + 1 == selectedNumber) {
                lbl.setBackground(Color.LIGHT_GRAY);
                lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
            } else {
                lbl.setBackground(Color.WHITE);
                lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            }
        }
        repaint();
    }
    
    /**
     * Update row texts.
     *
     * @param newTexts the new texts
     */
    public void updateRowTexts(List<String> newTexts) {
        if (newTexts.size() != rowLabels.size()) return;
        for (int i = 0; i < rowLabels.size(); i++) {
            rowLabels.get(i).setText((i + 1) + ". " + newTexts.get(i));
        }
        repaint();
    }
    
    /**
     * Gets the selected number.
     *
     * @return the currently selected number (1-7)
     */
    public int getSelectedNumber() {
        return selectedNumber;
    }

    // --- Listeners for the Header ---

    /**
     * Adds the home listener.
     *
     * @param listener the listener
     */
    public void addHomeListener(ActionListener listener) {
        headerPanel.addHomeListener(listener);
    }

    /**
     * Adds the notificaciones listener.
     *
     * @param listener the listener
     */
    public void addNotificacionesListener(ActionListener listener) {
        headerPanel.addNotificacionesListener(listener);
    }

    /**
     * Adds the perfil listener.
     *
     * @param listener the listener
     */
    public void addPerfilListener(ActionListener listener) {
        headerPanel.addPerfilListener(listener);
    }
    
    // --- Listeners for Section A ---

    /**
     * Adds the left arrow listener.
     *
     * @param listener the listener
     */
    public void addLeftArrowListener(ActionListener listener) {
        btnLeftArrow.addActionListener(listener);
    }
    
    /**
     * Adds the right arrow listener.
     *
     * @param listener the listener
     */
    public void addRightArrowListener(ActionListener listener) {
        btnRightArrow.addActionListener(listener);
    }
    
    // --- Listeners for Section B ---

    /**
     * Adds a listener for a specific reorder button.
     * @param index The index of the row (0, 1, or 2).
     * @param listener The action listener.
     */
    public void addReorderListener(int index, ActionListener listener) {
        if (index >= 0 && index < reorderButtons.size()) {
            reorderButtons.get(index).addActionListener(listener);
        }
    }
    
    /**
     * Helper to retrieve the actual reorder buttons for drag and drop setup.
     *
     * @return the reorder buttons
     */
    public List<JButton> getReorderButtons() {
        return reorderButtons;
    }

    /**
     * Gets the header panel.
     *
     * @return the header panel
     */
    public HeaderPanel getHeaderPanel() {
        return headerPanel;
    }
}