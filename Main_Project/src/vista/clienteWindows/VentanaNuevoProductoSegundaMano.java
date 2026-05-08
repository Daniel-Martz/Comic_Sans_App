package vista.clienteWindows;

import javax.swing.*;
import vista.userPanels.NuevoProductoSegundaManoPanel;
import java.awt.*;

/**
 * Ventana modal que contiene el formulario para añadir un nuevo
 * producto de segunda mano a la cartera del cliente.
 */
public class VentanaNuevoProductoSegundaMano extends JDialog {

    private static final long serialVersionUID = 1L;
    private NuevoProductoSegundaManoPanel panelNuevoProducto;

    public VentanaNuevoProductoSegundaMano(Window parent) {
        super(parent, "Add New Product", ModalityType.APPLICATION_MODAL);
        initComponents();
        initLayout();
        pack();
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        panelNuevoProducto = new NuevoProductoSegundaManoPanel();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(panelNuevoProducto, BorderLayout.CENTER);
    }

    public NuevoProductoSegundaManoPanel getPanel() {
        return panelNuevoProducto;
    }

    public void mostrar() {
        setVisible(true);
    }

    public void cerrar() {
        dispose();
    }

    // Usamos las ventanas genéricas creadas previamente
    public void mostrarVentanaExito(String mensaje) {
        VentanaExito exito = new VentanaExito(this, "Success", "PRODUCT ADDED!", mensaje);
        exito.mostrar();
    }

    public void mostrarVentanaError(String motivo) {
        VentanaError error = new VentanaError(this, "Error", "COULD NOT ADD PRODUCT", motivo);
        error.mostrar();
    }
}
