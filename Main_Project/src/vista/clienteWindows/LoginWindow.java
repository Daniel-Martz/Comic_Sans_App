package vista.clienteWindows;

import javax.swing.*;

import com.sun.tools.javac.Main;

import controladores.LoginController;
import controladores.LoginToCreateAccountController;
import controladores.MainController;
import vista.userPanels.LogInPanel;


// TODO: Auto-generated Javadoc
/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class LoginWindow extends JDialog {
  
  /** The log in panel. */
  LogInPanel logInPanel;

  /**
   * Instantiates a new login window.
   *
   * @param parent the parent
   */
  public LoginWindow(JFrame parent) {
      super(parent, "Log in", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      this.logInPanel = new LogInPanel();
      this.setContentPane(logInPanel);
  }

  /**
   * Adds the listener login.
   *
   * @param l the l
   */
  public void addListenerLogin(LoginController l){
      l.addListeningPanel(logInPanel);
      logInPanel.añadirListenerBotonLogIn(l);
  }

  /**
   * Adds the listener create account.
   *
   * @param l the l
   */
  public void addListenerCreateAccount(LoginToCreateAccountController l){
    l.addListeningPanel(logInPanel);
      logInPanel.añadirListenerBotonCrearUsuario(l); }
}


