package vista.userPanels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel principal de propuestas de intercambio.
 *
 * Estructura visual:
 *
 *  ┌─────────────────────────────────────────────────────┐
 *  │                   PROPOSALS                         │  ← título
 *  ├──────────────────────┬──────────────────────────────┤
 *  │      INCOME          │           SENT               │  ← cabeceras
 *  │  ┌────────────────┐  │  ┌────────────────────────┐  │
 *  │  │ InterchangeCard│  │  │ InterchangeCard        │  │
 *  │  │ InterchangeCard│  │  │ InterchangeCard        │  │  ← scroll
 *  │  │ ...            │  │  │ ...                    │  │
 *  │  └────────────────┘  │  └────────────────────────┘  │
 *  └──────────────────────┴──────────────────────────────┘
 *
 * Sigue MVC: NO contiene lógica de negocio.
 * El controlador llama a cargarOfertas() para poblar las columnas.
 */
public class ProposalsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------
    // Contenedores internos de las cards
    // -------------------------------------------------------
    private JPanel contenedorIncome;   // columna izquierda (ofertas recibidas)
    private JPanel contenedorSent;     // columna derecha (ofertas enviadas)

    // Listas para que el controlador pueda acceder a cada card
    private final List<InterchangeCardPanel> cardsIncome = new ArrayList<>();
    private final List<InterchangeCardPanel> cardsSent   = new ArrayList<>();

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public ProposalsPanel() {
        initComponents();
        initLayout();
    }

    // -------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------
    private void initComponents() {
        contenedorIncome = crearContenedorCards();
        contenedorSent   = crearContenedorCards();
    }

    /**
     * Crea un panel vertical que irá dentro del JScrollPane de cada columna.
     * BoxLayout Y_AXIS apila las cards verticalmente.
     */
    private JPanel crearContenedorCards() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(153, 180, 209));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private void initLayout() {
        setBackground(new Color(153, 180, 209));
        setLayout(new BorderLayout());

        // ── Título superior ──────────────────────────────────
        JLabel titulo = new JLabel("PROPOSALS", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setOpaque(true);
        titulo.setBackground(new Color(74, 118, 201));
        titulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titulo, BorderLayout.NORTH);

        // ── Cuerpo: dos columnas ─────────────────────────────
        JPanel cuerpo = new JPanel(new GridLayout(1, 2, 10, 0));
        cuerpo.setBackground(new Color(153, 180, 209));
        cuerpo.setBorder(new EmptyBorder(10, 10, 10, 10));

        cuerpo.add(crearColumna("INCOME", contenedorIncome));
        cuerpo.add(crearColumna("SENT",   contenedorSent));

        add(cuerpo, BorderLayout.CENTER);
    }

    /**
     * Crea una columna completa con su cabecera y su scroll.
     */
    private JPanel crearColumna(String titulo, JPanel contenedor) {
        JPanel columna = new JPanel(new BorderLayout(0, 5));
        columna.setBackground(new Color(153, 180, 209));

        // Cabecera de columna
        JLabel cabecera = new JLabel(titulo, SwingConstants.CENTER);
        cabecera.setFont(new Font("SansSerif", Font.BOLD, 16));
        cabecera.setForeground(Color.WHITE);
        cabecera.setOpaque(true);
        cabecera.setBackground(new Color(74, 118, 201));
        cabecera.setBorder(new EmptyBorder(8, 0, 8, 0));

        // ScrollPane que envuelve el contenedor de cards
        JScrollPane scroll = new JScrollPane(contenedor,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        columna.add(cabecera, BorderLayout.NORTH);
        columna.add(scroll,   BorderLayout.CENTER);
        return columna;
    }

    // -------------------------------------------------------
    // Métodos públicos para el CONTROLADOR
    // -------------------------------------------------------

    /**
     * Añade una card a la columna INCOME (ofertas recibidas).
     * El controlador llama a este método por cada oferta recibida.
     */
    public void añadirCardIncome(InterchangeCardPanel card) {
        cardsIncome.add(card);
        contenedorIncome.add(card);
        contenedorIncome.add(Box.createVerticalStrut(10));
        contenedorIncome.revalidate();
        contenedorIncome.repaint();
    }

    /**
     * Añade una card a la columna SENT (ofertas enviadas).
     * El controlador llama a este método por cada oferta enviada.
     */
    public void añadirCardSent(InterchangeCardPanel card) {
        cardsSent.add(card);
        contenedorSent.add(card);
        contenedorSent.add(Box.createVerticalStrut(10));
        contenedorSent.revalidate();
        contenedorSent.repaint();
    }

    /**
     * Elimina todas las cards de ambas columnas.
     * Útil para recargar cuando cambian las ofertas.
     */
    public void limpiar() {
        cardsIncome.clear();
        cardsSent.clear();
        contenedorIncome.removeAll();
        contenedorSent.removeAll();
        contenedorIncome.revalidate();
        contenedorSent.revalidate();
        repaint();
    }

    /**
     * Devuelve las cards de INCOME (para que el controlador
     * pueda registrar listeners en cada una).
     */
    public List<InterchangeCardPanel> getCardsIncome() {
        return cardsIncome;
    }

    /**
     * Devuelve las cards de SENT.
     */
    public List<InterchangeCardPanel> getCardsSent() {
        return cardsSent;
    }
}