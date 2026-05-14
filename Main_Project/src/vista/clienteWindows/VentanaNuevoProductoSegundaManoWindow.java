package vista.clienteWindows;

import javax.swing.*;
import vista.userPanels.NuevoProductoSegundaManoPanel;
import java.awt.*;

// TODO: Auto-generated Javadoc
/**
 * Ventana modal que contiene el formulario para añadir un nuevo
 * producto de segunda mano a la cartera del cliente.
 */
public class VentanaNuevoProductoSegundaManoWindow extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The panel nuevo producto. */
    private NuevoProductoSegundaManoPanel panelNuevoProducto;

    /**
     * Instantiates a new ventana nuevo producto segunda mano window.
     *
     * @param parent the parent
     */
    public VentanaNuevoProductoSegundaManoWindow(Window parent) {
        super(parent, "Add New Product", ModalityType.APPLICATION_MODAL);
        initComponents();
        initLayout();
        pack();
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    /**
     * Inits the components.
     */
    private void initComponents() {
        panelNuevoProducto = new NuevoProductoSegundaManoPanel();
    }

    /**
     * Inits the layout.
     */
    private void initLayout() {
        setLayout(new BorderLayout());
        add(panelNuevoProducto, BorderLayout.CENTER);
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public NuevoProductoSegundaManoPanel getPanel() {
        return panelNuevoProducto;
    }

    /**
     * Mostrar.
     */
    public void mostrar() {
        setVisible(true);
    }

    /**
     * Cerrar.
     */
    public void cerrar() {
        dispose();
    }

    /**
     * Mostrar ventana exito.
     *
     * @param mensaje the mensaje
     */
    // Usamos las ventanas genéricas creadas previamente
    public void mostrarVentanaExito(String mensaje) {
        VentanaExitoWindow exito = new VentanaExitoWindow(this, "Success", "PRODUCT ADDED!", mensaje);
        exito.mostrar();
    }

    /**
     * Mostrar ventana error.
     *
     * @param motivo the motivo
     */
    public void mostrarVentanaError(String motivo) {
        VentanaErrorWindow error = new VentanaErrorWindow(this, "Error", "COULD NOT ADD PRODUCT", motivo);
        error.mostrar();
    }
}
