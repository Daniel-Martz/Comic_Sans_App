package controladores;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sun.tools.javac.Main;

import controladores.MainController;
import modelo.aplicacion.Aplicacion;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Usuario;
import vista.main.MainFrame;
import vista.userPanels.HistorialPedidosPanel;
import vista.userPanels.LogInPanel;

public class ControladorHistorialPedidos implements ActionListener{
  HistorialPedidosPanel historialPedidosPanel;
  MainController mainController;
	
	public ControladorHistorialPedidos(HistorialPedidosPanel historialPedidosPanel, MainController m) {
		this.mainController = m;
    this.historialPedidosPanel = historialPedidosPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	  int productId = Integer.parseInt(e.getActionCommand());
    Window parentWindow = SwingUtilities.getWindowAncestor(historialPedidosPanel);
    vista.userWindows.VentanaDetallesProducto dialog = new vista.userWindows.VentanaDetallesProducto(parentWindow, Aplicacion.getInstancia().buscarProductoNuevo(productId));
    dialog.setVisible(true);
	}
}
