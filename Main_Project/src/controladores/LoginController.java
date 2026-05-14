package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.aplicacion.Aplicacion;
import vista.userPanels.LogInPanel;

// TODO: Auto-generated Javadoc
/**
 * Controlador del formulario de login.
 *
 * Lee usuario/contraseña del panel, intenta iniciar sesión y actualiza la
 * interfaz según el resultado.
 */
public class LoginController implements ActionListener{
  
  /** The login panel. */
  LogInPanel loginPanel;
  
  /** The main controller. */
  MainController mainController;
    
	/**
	 * Crea el controlador de login.
	 * @param m controlador principal para coordinar acciones como cerrar la ventana de login o actualizar el icono de usuario.
	 */
	public LoginController(MainController m) {
		this.mainController = m;
	}

	/**
	 * Asocia el panel de login a este controlador para poder leer los campos de usuario y contraseña.
	 * @param l panel de login que este controlador manejará
	 */
	public void addListeningPanel(LogInPanel l){
		this.loginPanel = l;
	}
    
	/**
	 * Maneja el envío del formulario de login.
	 *
	 * @param arg0 evento de acción
	 */
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
