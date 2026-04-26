package vista.userWindows;

import javax.swing.*;

import controlador.LoginController;
import controlador.LoginToCreateAccountController;
import controladores.MainController;
import vista.userPanels.LogInPanel;


/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class LoginDialog extends JDialog {
  public LoginDialog(JFrame parent, MainController m) {
      super(parent, "Log in", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      LogInPanel logInPanel = new LogInPanel();
      logInPanel.añadirListenerBotonLogIn(new LoginController(logInPanel, m));
      logInPanel.añadirListenerBotonCrearUsuario(new LoginToCreateAccountController(logInPanel, m));
      this.setContentPane(logInPanel);
  }
}


