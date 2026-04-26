package vista.userWindows;

import java.awt.event.ActionListener;

import javax.swing.*;

import controlador.CreateAccountController;
import controladores.MainController;
import vista.userPanels.CrearUsuarioPanel;
import vista.userPanels.LogInPanel;


/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class CrearUsuarioDialog extends JDialog {
  public CrearUsuarioDialog(JFrame parent, MainController m) {
      super(parent, "Crar usuario", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      CrearUsuarioPanel crearUsuarioPanel = new CrearUsuarioPanel();
      crearUsuarioPanel.añadirListenerBotonCrear(new CreateAccountController(crearUsuarioPanel, m));
      this.setContentPane(crearUsuarioPanel);
  }

  
}


