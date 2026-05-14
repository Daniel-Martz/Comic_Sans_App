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
 *
 * Gestiona la selección de imagen, validación básica de campos y la llamada
 * al modelo para crear el producto en la cartera del usuario.
 */
public class ControladorNuevoProductoSegundaMano implements ActionListener {

    private final VentanaNuevoProductoSegundaManoWindow ventana;
    private final NuevoProductoSegundaManoPanel panel;
    private File imagenSeleccionada; // archivo seleccionado temporalmente

    /**
     * Crea el controlador y conecta los botones de la vista.
     *
     * @param ventana ventana modal para crear el producto
     */
    public ControladorNuevoProductoSegundaMano(VentanaNuevoProductoSegundaManoWindow ventana) {
        this.ventana = ventana;
        this.panel = ventana.getPanel();
        // Conectar los botones del panel con este controlador
        this.panel.setControlador(this);
    }

    /**
     * Maneja acciones de la ventana: subir imagen, confirmar o cancelar.
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

    /**
     * Abre un selector de ficheros para escoger la imagen del producto y
     * valida que sea JPG/PNG. Actualiza la vista con el nombre del fichero.
     * La imagen es obligatoria para poder confirmar la creación del producto.
     */
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Product Image (Required)");
        // Filtro visual
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        // Quitar "Todos los archivos"
        fileChooser.setAcceptAllFileFilterUsed(false);

        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoTmp = fileChooser.getSelectedFile();
            String nombreArchivo = archivoTmp.getName().toLowerCase();
            // Validación simple
            if (nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png")) {
                imagenSeleccionada = archivoTmp;
                panel.setNombreImagen(imagenSeleccionada.getName()); // Actualiza la vista
            } else {
                JOptionPane.showMessageDialog(null,
                        "Formato no válido. Por favor, selecciona un JPG o PNG.",
                        "Error de formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Valida los campos del formulario, copia la imagen a assets y llama al
     * modelo para añadir el producto. Muestra diálogos en caso de error.
     */
    private void confirmarCreacion() {
        String nombre = panel.getNombre();
        String descripcion = panel.getDescripcion();

        // Validaciones básicas
        if (imagenSeleccionada == null) {
            ventana.mostrarVentanaError("Debes añadir una foto del producto.");
            return;
        }

        if (nombre.isEmpty()) {
            ventana.mostrarVentanaError("El nombre del producto no puede estar vacío.");
            return;
        }
        if (descripcion.isEmpty()) {
            ventana.mostrarVentanaError("Por favor añade una descripción para el producto.");
            return;
        }

        // Obtener el usuario actual
        if (!(Aplicacion.getInstancia().getUsuarioActual() instanceof ClienteRegistrado)) {
            ventana.mostrarVentanaError("Debes estar conectado como cliente registrado.");
            return;
        }

        ClienteRegistrado cliente = (ClienteRegistrado) Aplicacion.getInstancia().getUsuarioActual();

        try {
            // Guardar la imagen en assets
            File directorioDestino = new File("src/assets/productos/segunda_mano");
            if (!directorioDestino.exists()) {
                directorioDestino.mkdirs();
            }
            File archivoFinal = new File(directorioDestino, imagenSeleccionada.getName());
            Files.copy(imagenSeleccionada.toPath(), archivoFinal.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Llamada al modelo para crear y añadir el producto a la cartera
            cliente.añadirProductoACarteraDeIntercambio(nombre, descripcion, archivoFinal);

            ventana.mostrarVentanaExito("Tu producto se ha añadido a la cartera y está pendiente de validación.");
            ventana.cerrar();

        } catch (IOException ioEx) {
            ventana.mostrarVentanaError("Error guardando la imagen: " + ioEx.getMessage());
        } catch (Exception ex) {
            ventana.mostrarVentanaError("Error inesperado: " + ex.getMessage());
        }
    }
}