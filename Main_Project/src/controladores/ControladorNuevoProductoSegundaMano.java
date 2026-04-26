package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import vista.userWindows.VentanaNuevoProductoSegundaMano;
import vista.userPanels.NuevoProductoSegundaManoPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ControladorNuevoProductoSegundaMano implements ActionListener {

    private final VentanaNuevoProductoSegundaMano ventana;
    private final NuevoProductoSegundaManoPanel panel;
    private File imagenSeleccionada; // Guarda el archivo seleccionado temporalmente

    public ControladorNuevoProductoSegundaMano(VentanaNuevoProductoSegundaMano ventana) {
        this.ventana = ventana;
        this.panel = ventana.getPanel();
        
        // Conectar los botones del panel con este controlador
        this.panel.setControlador(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        switch (comando) {
            case "SUBIR_IMAGEN":
                seleccionarImagen();
                break;
            case "CONFIRMAR":
                confirmarCreacion();
                break;
            case "CANCELAR":
                ventana.cerrar();
                break;
        }
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Product Image");
        // Filtrar solo por imágenes
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));

        int seleccion = fileChooser.showOpenDialog(ventana);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            imagenSeleccionada = fileChooser.getSelectedFile();
            panel.setNombreImagen(imagenSeleccionada.getName());
        }
    }

    private void confirmarCreacion() {
        String nombre = panel.getNombre();
        String descripcion = panel.getDescripcion();

        // Validaciones básicas
        if (nombre.isEmpty()) {
            ventana.mostrarVentanaError("The product name cannot be empty.");
            return;
        }
        if (descripcion.isEmpty()) {
            ventana.mostrarVentanaError("Please add a description for the product.");
            return;
        }

        // Obtener el usuario actual
        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            ventana.mostrarVentanaError("You must be logged in as a registered client.");
            return;
        }
        
        ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();

        try {
            // Llamada al modelo para crear y añadir el producto a la cartera
            cliente.añadirProductoACarteraDeIntercambio(nombre, descripcion, imagenSeleccionada);
            
            ventana.mostrarVentanaExito("Your product has been added to your portfolio.\nIt is now pending validation.");
            ventana.cerrar();
            
        } catch (Exception ex) {
            ventana.mostrarVentanaError("Unexpected error: " + ex.getMessage());
        }
    }
}