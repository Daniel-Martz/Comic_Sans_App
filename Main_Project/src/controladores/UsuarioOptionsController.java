package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;
import vista.main.MainFrame;

/**
 * Controlador para las opciones del usuario (perfil, historial, cerrar sesión).
 */
public class UsuarioOptionsController implements ActionListener{
  MainController mainController;
  MainFrame mainFrame;
    
	/**
	 * Crea el controlador con las referencias a la ventana principal y al controlador
	 * @param mainFrame ventana principal de la aplicación
	 * @param m controlador principal usado para navegar entre paneles
	 */
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
    
	/**
	 * Cierra el diálogo de opciones y abre la ventana de edición de perfil.
	 */
	private void actionEditProfile() {
		mainController.cerrarVentanaOpcionesUsuario();
		mainController.abrirVentanaEditarUsuario();
	}

  /**
   * Muestra el historial de pedidos del cliente actual y navega al panel
   * correspondiente.
   */
  private void actionPurchaseHistory() {
	Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual(); 
	if(!(usuarioActual instanceof ClienteRegistrado clienteActual)){
	  throw new IllegalStateException("Only a registered customer can access their order history.");
	}
	mainFrame.getHistorialPedidosPanel().agregarPedido(clienteActual.getPedidos());

	mainController.cerrarVentanaOpcionesUsuario();
	mainController.navegarA(MainFrame.PANEL_HISTORIAL_PEDIDOS);
  }

  /**
   * Cierra la sesión del usuario actual y actualiza la interfaz.
   */
  private void actionCerrarSesion() {
	Object msg = "Log out completed";
	JOptionPane.showMessageDialog(mainFrame, msg ,"Log out", JOptionPane.INFORMATION_MESSAGE);
	mainController.cerrarVentanaOpcionesUsuario();
	mainController.refreshIconImage(false);
	Aplicacion.getInstancia().cerrarSesion();
	mainController.mostrarMenuPrincipal();
  }
    
}
