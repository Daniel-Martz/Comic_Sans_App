package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.aplicacion.Aplicacion;
import vista.userPanels.CrearUsuarioPanel;

/**
 * Controlador para la ventana de creación de cuenta de usuario.
 *
 * Lee los campos del panel de creación, llama al modelo para crear la
 * cuenta e intenta iniciar sesión automáticamente. Muestra errores en el
 * propio panel.
 */
public class CreateAccountController implements ActionListener{
  CrearUsuarioPanel crearUsuarioPanel;
  MainController mainController;
    
    /**
     * Inicializa el controlador con una referencia al controlador principal para
     * navegación y actualización de la UI.
     * @param mainController controlador principal de la aplicación
     */
    public CreateAccountController(MainController mainController) {
    this.mainController = mainController;
    }

  /**
   * Asocia el panel que este controlador debe escuchar.
   *
   * @param crearUsuarioPanel panel de creación de usuario
   */
  public void addListeningPanel(CrearUsuarioPanel crearUsuarioPanel){
    this.crearUsuarioPanel = crearUsuarioPanel;
  }
    
    /**
     * Maneja el envío del formulario de creación: valida mediante el modelo
     * y actualiza la UI según el resultado.
     *
     * @param arg0 evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        String usernameString = crearUsuarioPanel.getUsername();
        String dniString = crearUsuarioPanel.getDni();
        String passwordString = crearUsuarioPanel.getPassword(); 
        String confirmedPasswordString = crearUsuarioPanel.getConfirmedPassword();
    Aplicacion app = Aplicacion.getInstancia();
    try {
      app.crearCuenta(usernameString, dniString, passwordString, confirmedPasswordString);
      app.iniciarSesion(usernameString, passwordString);
      crearUsuarioPanel.setStatusLabelText("");
      mainController.cerrarVentanaCrearUsuario();
      mainController.refreshIconImage(true);
    }catch(IllegalArgumentException | IllegalStateException e) {
      crearUsuarioPanel.setStatusLabelText(e.getMessage());
      mainController.refreshIconImage(false);
    }
    }
    
}
