package vista.userPanels;

import java.awt.Panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class crearUsuarioPanel extends JPanel {
	JPanel panelTitulo;
	JPanel panelCamposRellenables;
	JPanel panelRequisitos;
	JPanel panelBotones;
	
	public crearUsuarioPanel() {
		panelTitulo = new JPanel();
		panelCamposRellenables = new JPanel(new BoxLayout(panelCamposRellenables, BoxLayout.Y_AXIS));	
    panelRequisitos = new JPanel(new BoxLayout(panelRequisitos, BoxLayout.Y_AXIS));
    panelBotones = new JPanel();
  }

}
