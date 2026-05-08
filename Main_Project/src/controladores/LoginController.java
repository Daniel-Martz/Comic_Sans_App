package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.tools.javac.Main;

import modelo.aplicacion.Aplicacion;
import vista.userPanels.LogInPanel;

public class LoginController implements ActionListener{
  LogInPanel loginPanel;
  MainController mainController;
	
	public LoginController(MainController m) {
		this.mainController = m;
	}

  public void addListeningPanel(LogInPanel l){
    this.loginPanel = l;
  }
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String usernameString = loginPanel.getUsername();
		String passwordString = loginPanel.getPassword(); 
		Aplicacion app = Aplicacion.getInstancia();
		try {
		  app.iniciarSesion(usernameString, passwordString);
		  loginPanel.setStatusLabelText("");
		  mainController.cerrarVentanaLogIn();
		  mainController.refreshIconImage(true);
		}catch(IllegalArgumentException | IllegalStateException e) {
		  loginPanel.setStatusLabelText(e.getMessage());
		  mainController.refreshIconImage(false);
		}
	}
	
}
