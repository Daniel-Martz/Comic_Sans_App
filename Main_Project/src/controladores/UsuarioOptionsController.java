package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.sun.tools.javac.Main;

import controladores.MainController;
import modelo.aplicacion.Aplicacion;
import vista.main.MainFrame;
import vista.userPanels.LogInPanel;

public class UsuarioOptionsController implements ActionListener{
  MainController mainController;
  MainFrame mainFrame;
	
	public UsuarioOptionsController(MainFrame mainFrame, MainController m) {
		this.mainController = m;
    this.mainFrame = mainFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "Edit profile":
      actionEditProfile();
			break;
		case "Purhcase History":
      actionPurchaseHistory();
			break;
		case "Notification Center":
      actionNotificationCenter();
			break;
		case "Cerrar Sesión":
      actionCerrarSesion();
			break;
		default:
			break;
		}
	}
	
	private void actionEditProfile() {
		// mainController.abrirVentanaCrearUsuario();
	}

  private void actionPurchaseHistory() {
    // mainController.navegarA(M);
  }

  private void actionNotificationCenter() {
    // mainController.navegarA(M);
  }

  private void actionCerrarSesion() {
    Object msg = "Log out completed";
    JOptionPane.showMessageDialog(mainFrame, msg ,"Log out", JOptionPane.INFORMATION_MESSAGE);
    mainController.cerrarVentanaOpcionesUsuario();
    Aplicacion.getInstancia().cerrarSesion();
  }
	
}
