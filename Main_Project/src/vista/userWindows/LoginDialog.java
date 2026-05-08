package vista.userWindows;

import javax.swing.*;

import com.sun.tools.javac.Main;

import controladores.LoginController;
import controladores.LoginToCreateAccountController;
import controladores.MainController;
import vista.userPanels.LogInPanel;


/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class LoginDialog extends JDialog {
  LogInPanel logInPanel;

  public LoginDialog(JFrame parent) {
      super(parent, "Log in", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      this.logInPanel = new LogInPanel();
      this.setContentPane(logInPanel);
  }

  public void addListenerLogin(LoginController l){
      l.addListeningPanel(logInPanel);
      logInPanel.añadirListenerBotonLogIn(l);
  }

  public void addListenerCreateAccount(LoginToCreateAccountController l){
    l.addListeningPanel(logInPanel);
      logInPanel.añadirListenerBotonCrearUsuario(l); }
}


