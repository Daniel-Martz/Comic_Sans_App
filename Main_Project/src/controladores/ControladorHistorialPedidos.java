package controladores;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import vista.userPanels.HistorialPedidosPanel;

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
    vista.userWindows.VentanaDetallesProducto dialog = new vista.userWindows.VentanaDetallesProducto(parentWindow, Catalogo.getInstancia().buscarProductoNuevo(productId));
    dialog.setVisible(true);
	}
}
