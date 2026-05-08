package controladores;

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
		try{
      app.validarContraseñaLength(password);
			crearUsuarioPanel.setTenCharactersBox(true);
		}catch (IllegalArgumentException e) {
			crearUsuarioPanel.setTenCharactersBox(false);
		}

    try{
      app.validarContraseñaLower(password);
      app.validarContraseñaUpper(password);
			crearUsuarioPanel.setUpperAndLowerBox(true);
		}catch (IllegalArgumentException e) {
			crearUsuarioPanel.setUpperAndLowerBox(false);
		}

    try{
      app.validarContraseñaNumber(password);
			crearUsuarioPanel.setNumberBox(true);
		}catch (IllegalArgumentException e) {
			crearUsuarioPanel.setNumberBox(false);
		}
		
    try{
      app.validarContraseñaSymbol(password);
			crearUsuarioPanel.setSymbolBox(true);
		}catch (IllegalArgumentException e) {
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
