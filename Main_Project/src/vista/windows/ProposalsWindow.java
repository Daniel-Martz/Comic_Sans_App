package vista.windows;

import javax.swing.*;

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
}