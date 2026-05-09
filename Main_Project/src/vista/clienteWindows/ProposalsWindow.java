package vista.clienteWindows;

import javax.swing.*;

import controladores.ControladorProposals;
import modelo.aplicacion.Aplicacion;
import modelo.producto.EstadoConservacion;
import modelo.producto.ProductoSegundaMano;
import modelo.solicitud.Oferta;
import modelo.tiempo.DateTimeSimulado;
import modelo.usuario.ClienteRegistrado;
import vista.userPanels.ProposalsPanel;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Ventana que muestra el panel de propuestas de intercambio.
 * Es un JDialog para que esté a parte respecto al MainFrame.
 * Se abre desde el menú principal al pulsar el botón de intercambios y seleccionar el botón proposals.
 */
public class ProposalsWindow extends JDialog {

    private static final long serialVersionUID = 1L;

    private ProposalsPanel proposalsPanel;

    /**
     * @param parent el JFrame padre (MainFrame)
     */
    public ProposalsWindow(JFrame parent) {
        super(parent, "Interchange Proposals", true);
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
        VentanaExitoWindow exito = new VentanaExitoWindow(
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
        VentanaErrorWindow error = new VentanaErrorWindow(
            this, 
            "Error en la operación", 
            "HA OCURRIDO UN PROBLEMA", 
            motivo
        );
        error.mostrar();
    }
}