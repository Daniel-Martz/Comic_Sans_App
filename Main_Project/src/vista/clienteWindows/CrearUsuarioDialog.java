package vista.clienteWindows;

import java.awt.event.ActionListener;

import javax.swing.*;

import controladores.CreateAccountController;
import controladores.MainController;
import vista.userPanels.CrearUsuarioPanel;


/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class CrearUsuarioDialog extends JDialog {
  CrearUsuarioPanel crearUsuarioPanel;
  public CrearUsuarioDialog(JFrame parent) {
      super(parent, "Crar usuario", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      this.crearUsuarioPanel = new CrearUsuarioPanel();
      this.setContentPane(crearUsuarioPanel);
  }

  public void addListener(CreateAccountController a){
    a.addListeningPanel(crearUsuarioPanel);
      this.crearUsuarioPanel.añadirListenerBotonCrear(a);
  }

  
}


