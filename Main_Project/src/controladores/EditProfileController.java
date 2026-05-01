package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import modelo.aplicacion.Aplicacion;
import vista.userPanels.EditProfilePanel;

public class EditProfileController implements ActionListener{
  EditProfilePanel editProfilePanel;
  MainController mainController;
	
	public EditProfileController(MainController mainController) {
    this.mainController = mainController;
	}

  public void addListeningPanel(EditProfilePanel editProfilePanel){
    this.editProfilePanel = editProfilePanel;
    Aplicacion app = Aplicacion.getInstancia();
    editProfilePanel.inicializarCampos(app.getUsuarioActual().getNombreUsuario(), app.getUsuarioActual().getDNI());
  }
	
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
