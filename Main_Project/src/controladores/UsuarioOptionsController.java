package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.sun.tools.javac.Main;

import controladores.MainController;
import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;
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
		case "Edit Profile":
      actionEditProfile();
			break;
		case "Purchase History":
      actionPurchaseHistory();
			break;
		case "Cerrar Sesión":
      actionCerrarSesion();
			break;
		default:
			break;
		}
	}
	
	private void actionEditProfile() {
		mainController.cerrarVentanaOpcionesUsuario();
		mainController.abrirVentanaEditarUsuario();
	}

  private void actionPurchaseHistory() {
    Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual(); 
    if(!(usuarioActual instanceof ClienteRegistrado clienteActual)){
      throw new IllegalStateException("Solo un cliente registrado puede acceder a su historial de pedidos");
    }
    mainFrame.getHistorialPedidosPanel().agregarPedido(clienteActual.getPedidos());

	mainController.cerrarVentanaOpcionesUsuario();
    mainController.navegarA(MainFrame.PANEL_HISTORIAL_PEDIDOS);
  }

  private void actionCerrarSesion() {
    Object msg = "Log out completed";
    JOptionPane.showMessageDialog(mainFrame, msg ,"Log out", JOptionPane.INFORMATION_MESSAGE);
    mainController.cerrarVentanaOpcionesUsuario();
    mainController.refreshIconImage(false);
    Aplicacion.getInstancia().cerrarSesion();
    mainController.mostrarMenuPrincipal();
  }
	
}
