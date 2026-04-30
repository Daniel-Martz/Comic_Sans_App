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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        
        // Filtro visual
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png"));
        
        // ESTO ES CLAVE: Quita la opción "Todos los archivos" del desplegable
        fileChooser.setAcceptAllFileFilterUsed(false);

        int seleccion = fileChooser.showOpenDialog(null); // O pasa tu ventana/panel
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoTmp = fileChooser.getSelectedFile();
            String nombreArchivo = archivoTmp.getName().toLowerCase();
            
            // Validación extra de seguridad (por si acaso)
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

    private void confirmarCreacion() {
        String nombre = panel.getNombre();
        String descripcion = panel.getDescripcion();

        // Validaciones básicas
        if (imagenSeleccionada == null) {
            JOptionPane.showMessageDialog(null, 
                "You must add a photo of the product.", 
                "Missing Image",
                JOptionPane.WARNING_MESSAGE);
            return; // CORTAMOS la ejecución aquí, no se crea el objeto
        }
        
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
        	
        	//Vamos a tener que hacer un guardado de la imagen en assets
            File directorioDestino = new File("src/assets/productos/segunda_mano");
            if (!directorioDestino.exists()) {
                directorioDestino.mkdirs(); // Crea la carpeta si no existe
            }
            
            // 2. Preparar el archivo final (la copia)
            File archivoFinal = new File(directorioDestino, imagenSeleccionada.getName());
            
            // 3. Copiar la imagen original a la carpeta de nuestro proyecto
            Files.copy(imagenSeleccionada.toPath(), archivoFinal.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // ---------------------------------------

            // Llamada al modelo para crear y añadir el producto a la cartera.
            // ¡OJO! Aquí pasamos 'archivoFinal' en lugar de 'imagenSeleccionada'
            cliente.añadirProductoACarteraDeIntercambio(nombre, descripcion, archivoFinal);
            
            ventana.mostrarVentanaExito("Your product has been added to your portfolio.\nIt is now pending validation.");
            ventana.cerrar();
            
        } catch (IOException ioEx) {
            // Capturamos específicamente errores al copiar la imagen
            ventana.mostrarVentanaError("Error saving the image: " + ioEx.getMessage());
        } catch (Exception ex) {
            ventana.mostrarVentanaError("Unexpected error: " + ex.getMessage());
        }
    }
}