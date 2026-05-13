package controladores;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import modelo.aplicacion.Aplicacion;
import vista.userPanels.CrearUsuarioPanel;

/**
 * Controlador que actualiza en tiempo real los indicadores de fuerza de la
 * contraseña mientras el usuario escribe en el formulario de creación.
 */
public class NewPasswordController implements DocumentListener {
  CrearUsuarioPanel crearUsuarioPanel;
	/**
	 * Crea el controlador y asocia el panel de creación de usuario para actualizar
	 * los indicadores de contraseña.
	 * @param crearUsuarioPanel el panel de creación de usuario que maneja este controlador
	 */
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
