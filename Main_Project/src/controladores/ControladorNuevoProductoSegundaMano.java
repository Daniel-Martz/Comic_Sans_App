package controladores;

import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import vista.clienteWindows.VentanaNuevoProductoSegundaManoWindow;
import vista.userPanels.NuevoProductoSegundaManoPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Controlador para la ventana de añadir un producto de segunda mano.
 */
public class ControladorNuevoProductoSegundaMano implements ActionListener {

    private final VentanaNuevoProductoSegundaManoWindow ventana;
    private final NuevoProductoSegundaManoPanel panel;
    private File imagenSeleccionada;

    /**
     * Crea el controlador y conecta los botones de la vista.
     *
     * @param ventana ventana modal para crear el producto
     */
    public ControladorNuevoProductoSegundaMano(VentanaNuevoProductoSegundaManoWindow ventana) {
        this.ventana = ventana;
        this.panel = ventana.getPanel();
        this.panel.setControlador(this);
    }

    /**
     * Maneja acciones de la ventana.
     *
     * @param e evento de acción
     */
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
        fileChooser.setDialogTitle("Select Product Image (Required)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoTmp = fileChooser.getSelectedFile();
            String nombreArchivo = archivoTmp.getName().toLowerCase();
            
            if (nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png")) {
                imagenSeleccionada = archivoTmp;
                panel.setNombreImagen(imagenSeleccionada.getName());
            } else {
                JOptionPane.showMessageDialog(null,
                        "Invalid format. Please select a JPG or PNG.",
                        "Format Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmarCreacion() {
        String nombre = panel.getNombre();
        String descripcion = panel.getDescripcion();

        if (imagenSeleccionada == null) {
            ventana.mostrarVentanaError("You must add a photo of the product.");
            return;
        }

        if (nombre.isEmpty()) {
            ventana.mostrarVentanaError("The product name cannot be empty.");
            return;
        }
        if (descripcion.isEmpty()) {
            ventana.mostrarVentanaError("Please add a description for the product.");
            return;
        }

        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            ventana.mostrarVentanaError("You must be logged in as a registered client.");
            return;
        }

        ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();

        try {
            File directorioDestino = new File("src/assets/productos/segunda_mano");
            if (!directorioDestino.exists()) {
                directorioDestino.mkdirs();
            }
            File archivoFinal = new File(directorioDestino, imagenSeleccionada.getName());
            Files.copy(imagenSeleccionada.toPath(), archivoFinal.toPath(), StandardCopyOption.REPLACE_EXISTING);

            cliente.añadirProductoACarteraDeIntercambio(nombre, descripcion, archivoFinal);

            ventana.mostrarVentanaExito("Your product has been added to the wallet and is pending validation.");
            ventana.cerrar();

        } catch (IOException ioEx) {
            ventana.mostrarVentanaError("Error saving image: " + ioEx.getMessage());
        } catch (Exception ex) {
            ventana.mostrarVentanaError("Unexpected error: " + ex.getMessage());
        }
    }
}