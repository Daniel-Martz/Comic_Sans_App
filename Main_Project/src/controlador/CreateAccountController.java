package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.tools.javac.Main;

import controladores.MainController;
import modelo.aplicacion.Aplicacion;
import vista.userPanels.CrearUsuarioPanel;

public class CreateAccountController implements ActionListener{
  CrearUsuarioPanel crearUsuarioPanel;
  MainController mainController;
	
	public CreateAccountController(MainController mainController) {
    this.mainController = mainController;
	}

  public void addListeningPanel(CrearUsuarioPanel crearUsuarioPanel){
    this.crearUsuarioPanel = crearUsuarioPanel;
  }
	
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
