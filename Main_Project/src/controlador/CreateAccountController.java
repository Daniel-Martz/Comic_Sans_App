package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import modelo.aplicacion.Aplicacion;
import vista.userPanels.CrearUsuarioPanel;

public class CreateAccountController implements ActionListener{
  CrearUsuarioPanel crearUsuarioPanel;
	
	public CreateAccountController(CrearUsuarioPanel crearUsuarioPanel) {
		this.crearUsuarioPanel = crearUsuarioPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String usernameString = crearUsuarioPanel.getUsername();
		String dniString = crearUsuarioPanel.getDni();
		String passwordString = crearUsuarioPanel.getPassword(); 
		String confirmedPasswordString = crearUsuarioPanel.getConfirmedPassword();
		if(!passwordString.equals(confirmedPasswordString)) {
			crearUsuarioPanel.setStatusLabelText("The two passwords are not equal.");
		}else {
			Aplicacion app = Aplicacion.getInstancia();
			try {
				app.crearCuenta(usernameString, dniString, passwordString);
				crearUsuarioPanel.setStatusLabelText("");
			}catch(IllegalArgumentException | IllegalStateException e) {
				crearUsuarioPanel.setStatusLabelText(e.getMessage());
			}
		}
	}
	
}
