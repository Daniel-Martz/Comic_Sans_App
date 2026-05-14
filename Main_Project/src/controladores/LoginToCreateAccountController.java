package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vista.userPanels.LogInPanel;

// TODO: Auto-generated Javadoc
/**
 * Controlador que maneja la acción de pasar desde la ventana de login a la
 * ventana de creación de cuenta.
 */
public class LoginToCreateAccountController implements ActionListener{
  
  /** The login panel. */
  LogInPanel loginPanel;
  
  /** The main controller. */
  MainController mainController;
    
	/**
	 * Inicializa el controlador con una referencia al controlador principal para.
	 *
	 * @param mainController controlador principal usado para cerrar y abrir ventanas
	 */
	public LoginToCreateAccountController(MainController mainController) {
		this.mainController = mainController;
	}

  /**
   * Asocia el panel de login a este controlador para que pueda cerrarlo cuando se
   * abra el panel de creación de usuario.
   * @param l panel de login que este controlador manejará
   */
	public void addListeningPanel(LogInPanel l){
		this.loginPanel = l;
	}
    
	/**
	 * Cierra el diálogo de login y abre el de crear usuario.
	 *
	 * @param arg0 evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mainController.cerrarVentanaLogIn();
		mainController.abrirVentanaCrearUsuario();
	}
    
}



