package controladores;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
import vista.userPanels.HistorialPedidosPanel;

// TODO: Auto-generated Javadoc
/**
 * Controlador para el historial de pedidos del usuario.
 *
 * Permite ver detalles de productos comprados y escribir reseñas cuando procede.
 */
public class ControladorHistorialPedidos implements ActionListener{
  
  /** The historial pedidos panel. */
  HistorialPedidosPanel historialPedidosPanel;
  
  /** The main controller. */
  MainController mainController;
	
  /**
   * Crea el controlador del historial de pedidos.
   *
   * @param historialPedidosPanel panel de historial
   * @param m controlador principal (para navegación)
   */
  public ControladorHistorialPedidos(HistorialPedidosPanel historialPedidosPanel, MainController m) {
    this.mainController = m;
    this.historialPedidosPanel = historialPedidosPanel;
  }
	
  /**
   * Maneja eventos desde el panel (ver info, reseñar producto, etc.).
   *
   * @param e evento de acción
   */
  @Override
  public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;
        
        if (cmd.startsWith("INFO_")) {
            int productId = Integer.parseInt(cmd.substring(5));
            Window parentWindow = SwingUtilities.getWindowAncestor(historialPedidosPanel);
            vista.clienteWindows.VentanaDetallesProductoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoWindow(parentWindow, Catalogo.getInstancia().buscarProductoNuevo(productId));
            dialog.setVisible(true);
        } else if (cmd.startsWith("REVIEW_")) {
            int productId = Integer.parseInt(cmd.substring(7));
            manejarReseña(productId);
        } else {
            // Backward compatibility
            try {
                int productId = Integer.parseInt(cmd);
                Window parentWindow = SwingUtilities.getWindowAncestor(historialPedidosPanel);
                vista.clienteWindows.VentanaDetallesProductoWindow dialog = new vista.clienteWindows.VentanaDetallesProductoWindow(parentWindow, Catalogo.getInstancia().buscarProductoNuevo(productId));
                dialog.setVisible(true);
            } catch (NumberFormatException ex) {
                // Ignore
            }
        }
	}
	
    /**
     * Abre la ventana para añadir una reseña si el usuario puede reseñar el
     * producto (no lo haya reseñado ya).
     *
     * @param productId id del producto a reseñar
     */
    private void manejarReseña(int productId) {
        modelo.usuario.Usuario u = Aplicacion.getInstancia().getUsuarioActual();
        if (!(u instanceof modelo.usuario.ClienteRegistrado)) return;
        modelo.usuario.ClienteRegistrado cliente = (modelo.usuario.ClienteRegistrado) u;
        
        // Check if already reviewed
        for (modelo.producto.Reseña r : cliente.getReseñas()) {
            if (r.getProducto().getID() == productId) {
                JOptionPane.showMessageDialog(historialPedidosPanel, "You have already reviewed this product.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Show dialog
        modelo.producto.LineaProductoVenta producto = Catalogo.getInstancia().buscarProductoNuevo(productId);
        if (producto == null) return;
        Window parentWindow = SwingUtilities.getWindowAncestor(historialPedidosPanel);
        if (parentWindow instanceof JFrame) {
            vista.clienteWindows.AddReviewWindow dialog = new vista.clienteWindows.AddReviewWindow((JFrame)parentWindow, producto, this);
            dialog.setVisible(true);
        }
    }
    
    /**
     * Confirma y guarda la reseña escrita por el cliente.
     *
     * @param producto producto reseñado
     * @param descripcion texto de la reseña
     * @param puntuacion nota asignada
     * @param window ventana de reseña que se cerrará al terminar
     */
    public void confirmarReseña(modelo.producto.LineaProductoVenta producto, String descripcion, double puntuacion, vista.clienteWindows.AddReviewWindow window) {
        modelo.usuario.Usuario u = Aplicacion.getInstancia().getUsuarioActual();
        if (u instanceof modelo.usuario.ClienteRegistrado) {
            modelo.usuario.ClienteRegistrado cliente = (modelo.usuario.ClienteRegistrado) u;
            cliente.escribirReseña(producto, descripcion, puntuacion, new modelo.tiempo.DateTimeSimulado());
            JOptionPane.showMessageDialog(window, "Review submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            window.dispose();
        }
    }
}
