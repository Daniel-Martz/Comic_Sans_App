package vista.userPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para gestionar los trueques, los que te llegan y los que mandas.
 */
public class ProposalsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel incomeContainer;
    private JPanel sentContainer;

    private final List<InterchangeCardPanel> incomeCards = new ArrayList<>();
    private final List<InterchangeCardPanel> sentCards = new ArrayList<>();

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    /**
     * Constructor para montar las dos columnas.
     */
    public ProposalsPanel() {
        initComponents();
        initLayout();
    }

    /**
     * Initializes the components.
     */
    private void initComponents() {
        incomeContainer = createCardsContainer();
        sentContainer = createCardsContainer();
    }

    /**
     * Creates a vertical panel that goes inside the JScrollPane of each column.
     *
     * @return the container panel
     */
    private JPanel createCardsContainer() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(153, 180, 209));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    /**
     * Initializes the layout.
     */
    private void initLayout() {
        setBackground(new Color(153, 180, 209));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("PROPOSALS", SwingConstants.CENTER);
        title.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(74, 118, 201));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 10, 0));
        body.setBackground(new Color(153, 180, 209));
        body.setBorder(new EmptyBorder(10, 10, 10, 10));

        body.add(createColumn("INCOME", incomeContainer));
        body.add(createColumn("SENT", sentContainer));

        add(body, BorderLayout.CENTER);
    }

    /**
     * Creates a complete column with its header and scroll pane.
     *
     * @param title the column title
     * @param container the container panel
     * @return the complete column panel
     */
    private JPanel createColumn(String title, JPanel container) {
        JPanel column = new JPanel(new BorderLayout(0, 5));
        column.setBackground(new Color(153, 180, 209));

        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(74, 118, 201));
        header.setBorder(new EmptyBorder(8, 0, 8, 0));

        JScrollPane scroll = new JScrollPane(container,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        column.add(header, BorderLayout.NORTH);
        column.add(scroll,   BorderLayout.CENTER);
        return column;
    }

    // -------------------------------------------------------
    // Métodos públicos para el CONTROLADOR
    // -------------------------------------------------------


    /**
     * Encaja una tarjeta nueva en las que has enviado.
     * @param card la tarjeta ya montada
     */
    public void addIncomeCard(InterchangeCardPanel card) {
        incomeCards.add(card);
        incomeContainer.add(card);
        incomeContainer.add(Box.createVerticalStrut(10));
        incomeContainer.revalidate();
        incomeContainer.repaint();
    }

    /**
     * Adds a card to the sent column.
     *
     * @param card the interchange card panel to add
     */
    public void addSentCard(InterchangeCardPanel card) {
        sentCards.add(card);
        sentContainer.add(card);
        sentContainer.add(Box.createVerticalStrut(10));
        sentContainer.revalidate();
        sentContainer.repaint();
    }

    /**
     * Clears all cards from both columns.
     */
    public void clear() {
        incomeCards.clear();
        sentCards.clear();
        incomeContainer.removeAll();
        sentContainer.removeAll();
        incomeContainer.revalidate();
        sentContainer.revalidate();
        repaint();
    }

    /**
     * @return devuelve la lista de tarjetas que nos han llegao
     */
    public List<InterchangeCardPanel> getIncomeCards() {
        return incomeCards;
    }

    /**
     * @return devuelve la lista de tarjetas que mandamos nosotros
     */
    public List<InterchangeCardPanel> getSentCards() {
        return sentCards;
    }
}
