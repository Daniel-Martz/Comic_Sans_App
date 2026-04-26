package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import modelo.aplicacion.Aplicacion;
import vista.userPanels.CrearUsuarioPanel;

public class NewPasswordController implements DocumentListener {
  CrearUsuarioPanel crearUsuarioPanel;
	public NewPasswordController(CrearUsuarioPanel crearUsuarioPanel) {
		this.crearUsuarioPanel = crearUsuarioPanel;
	}
	private void cambio() {
		Aplicacion app = Aplicacion.getInstancia();
		String password = crearUsuarioPanel.getPassword();
		if(app.contraseñaLength(password) == true) {
			crearUsuarioPanel.setTenCharactersBox(true);
		}else {
			crearUsuarioPanel.setTenCharactersBox(false);
		}
		if(app.contraseñaLower(password) == true && app.contraseñaUpper(password) == true) {
			crearUsuarioPanel.setUpperAndLowerBox(true);
		}else {
			crearUsuarioPanel.setUpperAndLowerBox(false);
		}

		if(app.contraseñaNumber(password) == true) {
			crearUsuarioPanel.setNumberBox(true);
		}else {
			crearUsuarioPanel.setNumberBox(false);
		}

		if(app.contraseñaSymbol(password) == true) {
			crearUsuarioPanel.setSymbolBox(true);
		}else {
			crearUsuarioPanel.setSymbolBox(false);
		}


	}
	@Override
	public void changedUpdate(DocumentEvent arg0) {
	}
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		cambio();
	}
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		cambio();
	}
		
}
