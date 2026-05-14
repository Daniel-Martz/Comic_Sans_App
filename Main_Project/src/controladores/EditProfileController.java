package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import modelo.aplicacion.Aplicacion;
import vista.userPanels.EditProfilePanel;

// TODO: Auto-generated Javadoc
/**
 * Controlador para el panel de edición de perfil de usuario.
 *
 * Rellena inicialmente los campos con los datos actuales y envía los cambios
 * al modelo cuando el usuario confirma la edición.
 */
public class EditProfileController implements ActionListener{
  
  /** The edit profile panel. */
  EditProfilePanel editProfilePanel;
  
  /** The main controller. */
  MainController mainController;
    
	/**
	 * Inicializa el controlador con una referencia al controlador principal para
	 * navegación y gestión de ventanas.
	 * @param mainController controlador principal para manejar navegación y ventanas relacionadas
	 */
	public EditProfileController(MainController mainController) {
	this.mainController = mainController;
	}

  /**
   * Asocia el panel y inicializa los campos con los datos del usuario actual.
   *
   * @param editProfilePanel panel de edición de perfil
   */
  public void addListeningPanel(EditProfilePanel editProfilePanel){
	this.editProfilePanel = editProfilePanel;
	Aplicacion app = Aplicacion.getInstancia();
	editProfilePanel.inicializarCampos(app.getUsuarioActual().getNombreUsuario(), app.getUsuarioActual().getDNI());
  }
    
	/**
	 * Envía la modificación de cuenta al modelo y muestra errores en el panel si los hay.
	 *
	 * @param arg0 evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String usernameString = editProfilePanel.getUsername();
		String dniString = editProfilePanel.getDni();
		String oldPasswordString = editProfilePanel.getOldPassword(); 
		String passwordString = editProfilePanel.getPassword(); 
		Aplicacion app = Aplicacion.getInstancia();
		try {
		  app.modificarCuenta(usernameString, dniString, oldPasswordString, passwordString);
		  editProfilePanel.setStatusLabelText("");
		  mainController.cerrarVentanaEditarUsuario();;
		}catch(IllegalArgumentException | IllegalStateException e) {
		  editProfilePanel.setStatusLabelText(e.getMessage());
		}
	}
    
}
