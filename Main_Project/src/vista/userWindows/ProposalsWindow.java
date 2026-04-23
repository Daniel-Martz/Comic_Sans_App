package vista.userWindows;

import javax.swing.*;

import vista.userPanels.InterchangeCardPanel;
import vista.userPanels.ProposalsPanel;

import java.awt.*;

/**
 * Ventana que muestra el panel de propuestas de intercambio.
 * Es un JDialog para que sea modal respecto al MainFrame.
 * Se abre desde el menú principal al pulsar el botón de intercambios.
 */
public class ProposalsWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private ProposalsPanel proposalsPanel;

    /**
     * @param parent el JFrame padre (MainFrame)
     */
    public ProposalsWindow(JFrame parent) {
        super(parent, "Interchange No Category Filters", true);
        initComponents();
        initLayout();
        pack();
        setSize(1100, 750);
        setLocationRelativeTo(parent);
        setResizable(true);
    }

    private void initComponents() {
        proposalsPanel = new ProposalsPanel();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(proposalsPanel, BorderLayout.CENTER);
    }

    /**
     * Devuelve el panel interno para que el controlador pueda
     * registrar listeners y cargar datos.
     */
    public ProposalsPanel getProposalsPanel() {
        return proposalsPanel;
    }

    /**
     * Abre la ventana. Llamado por el MainController.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Cierra la ventana.
     */
    public void cerrar() {
        dispose();
    }
    
    /**
     * Muestra la ventana de éxito personalizada.
     * @param mensaje Descripción del éxito.
     */
    public void mostrarVentanaExito(String mensaje) {
        VentanaExito exito = new VentanaExito(
            this, 
            "Operación Exitosa", 
            "¡ACCIÓN COMPLETADA!", 
            mensaje
        );
        exito.mostrar();
    }

    /**
     * Muestra la ventana de error personalizada.
     * @param motivo Descripción del error.
     */
    public void mostrarVentanaError(String motivo) {
        VentanaError error = new VentanaError(
            this, 
            "Error en la operación", 
            "HA OCURRIDO UN PROBLEMA", 
            motivo
        );
        error.mostrar();
    }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                // Frame padre necesario para que el JDialog sea modal
                JFrame parent = new JFrame("Parent Frame (test)");
                parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                parent.setSize(400, 200);
                parent.setLocationRelativeTo(null);
                parent.setVisible(true);

                ProposalsWindow window = new ProposalsWindow(parent);

                // Añadir muchas ofertas INCOME para forzar scroll
                for (int i = 1; i <= 14; i++) {
                    String header = "FROM: User" + i;
                    double balance = (i % 3 == 0) ? -5.5 * i : 10.0 * (i % 4);
                    String[][] given = { { "GivenProd " + i, "Comics", "Good", String.format("%.2f", 3.5 * i) } };
                    String[][] received = { { "ReceivedProd " + i, "Figures", "New", String.format("%.2f", 4.5 * i) } };

                    InterchangeCardPanel card = new InterchangeCardPanel(header, balance, given, received, InterchangeCardPanel.Modo.INCOME);
                    final int idx = i;
                    card.addAcceptListener(e -> {
                        window.mostrarVentanaExito("Aceptada la oferta #" + idx);
                        System.out.println("Accepted offer " + idx);
                    });
                    card.addRejectListener(e -> {
                        window.mostrarVentanaError("Rechazada la oferta #" + idx);
                        System.out.println("Rejected offer " + idx);
                    });

                    window.getProposalsPanel().añadirCardIncome(card);
                }

                // Añadir algunas ofertas SENT
                for (int i = 1; i <= 6; i++) {
                    String header = "TO: UserTo" + i;
                    double balance = 0.0;
                    String[][] given = { { "MyProd " + i, "Games", "Used", "15.00" } };
                    String[][] received = { { "TheirProd " + i, "Comics", "New", "20.00" } };

                    InterchangeCardPanel card = new InterchangeCardPanel(header, balance, given, received, InterchangeCardPanel.Modo.SENT);
                    final int idx = i;
                    card.addCancelListener(e -> {
                        window.mostrarVentanaError("Cancelada la oferta enviada #" + idx);
                        System.out.println("Cancelled sent offer " + idx);
                    });

                    window.getProposalsPanel().añadirCardSent(card);
                }

                // Mostrar la ventana modal con las propuestas
                window.mostrar();
            });
        
        }
    
}