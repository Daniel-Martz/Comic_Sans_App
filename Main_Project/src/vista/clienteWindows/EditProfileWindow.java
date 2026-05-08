package vista.clienteWindows;

import javax.swing.*;

import controladores.EditProfileController;
import controladores.LoginController;
import controladores.LoginToCreateAccountController;
import vista.userPanels.EditProfilePanel;
import vista.userPanels.LogInPanel;


/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class EditProfileWindow extends JDialog {
  EditProfilePanel editProfilePanel;

  public EditProfileWindow(JFrame parent) {
      super(parent, "Log in", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      this.editProfilePanel = new EditProfilePanel();
      this.setContentPane(editProfilePanel);
  }

  public void addListenerChangeData(EditProfileController l){
    l.addListeningPanel(editProfilePanel);
      editProfilePanel.añadirListenerBotonChangeData(l); }
}


