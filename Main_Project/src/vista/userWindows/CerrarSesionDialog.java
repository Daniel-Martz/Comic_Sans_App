package vista.userWindows;

import javax.swing.*;
import vista.userPanels.LogInPanel;


/**
 * Ventana emergente (JDialog) para los filtros avanzados.
 * Implementa un diseño de dos columnas donde la columna derecha (Filtros de Categoría)
 * cambia dinámicamente según lo seleccionado en la columna izquierda (Filtros Generales).
 */
public class CerrarSesionDialog extends JDialog {
  public CerrarSesionDialog(JFrame parent) {
      super(parent, "Log in", true); // true = Modal
      setSize(800, 750);
      setLocationRelativeTo(parent);
      this.setContentPane(new LogInPanel());
  }
}


