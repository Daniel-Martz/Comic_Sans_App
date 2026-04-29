package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.tools.javac.Main;

import controladores.MainController;
import modelo.aplicacion.Aplicacion;
import vista.userPanels.LogInPanel;

public class VentanaUsuarioOptionsController implements ActionListener{
  MainController mainController;
	
	public VentanaUsuarioOptionsController(ActionEvent e, MainController m) {
		this.mainController = m;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "Edit profile":
			break;
		case "Purhcase History":
			break;
		case "Notification Center":
			break;
		default:
			break;
		}
	}
	
	private void actionEditProfile() {
		mainController.abrirVentanaCrearUsuario();
	}
	
}