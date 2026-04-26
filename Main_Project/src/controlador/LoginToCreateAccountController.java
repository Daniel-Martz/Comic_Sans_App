package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.tools.javac.Main;

import controladores.MainController;
import modelo.aplicacion.Aplicacion;
import vista.userPanels.CrearUsuarioPanel;
import vista.userPanels.LogInPanel;

public class LoginToCreateAccountController implements ActionListener{
  LogInPanel loginPanel;
  MainController mainController;
	
	public LoginToCreateAccountController(LogInPanel loginPanel, MainController mainController) {
		this.loginPanel = loginPanel;
		this.mainController = mainController;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mainController.cerrarVentanaLogIn();
		mainController.abrirVentanaCrearUsuario();
	}
	
}



