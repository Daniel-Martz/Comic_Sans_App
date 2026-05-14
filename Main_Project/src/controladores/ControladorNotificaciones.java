package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.aplicacion.Aplicacion;
import modelo.notificacion.Notificacion;
import modelo.notificacion.NotificacionCliente;
import modelo.notificacion.NotificacionEmpleado;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;
import vista.userPanels.NotificacionesPanel;

// TODO: Auto-generated Javadoc
/**
 * Controlador para las notificaciones del usuario.
 *
 * Permite leer, borrar y refrescar las notificaciones mostradas en la vista.
 */
public class ControladorNotificaciones implements ActionListener{
  
  /** The notificaciones panel. */
  NotificacionesPanel notificacionesPanel;
  
  /** The main controller. */
  MainController mainController;
    
    /**
     * Crea el controlador de notificaciones.
     *
     * @param notificacionesPanel panel que muestra las notificaciones
     * @param m controlador principal (para abrir ventanas relacionadas)
     */
    public ControladorNotificaciones(NotificacionesPanel notificacionesPanel, MainController m) {
        this.mainController = m;
    this.notificacionesPanel = notificacionesPanel;
    }
    
    /**
     * Método vacío por compatibilidad; aquí se podrían manejar acciones
     * emitidas por la vista si fuera necesario.
     *
     * @param e the e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }

  /**
   * Marca una notificación como leída y abre la ventana de detalle.
   *
   * @param n notificación a leer
   */
  public void leerNotificacion(Notificacion n){
    if (n == null) return;
    // Marcamos la notificación como leída
    n.read();
    mainController.abrirVentanaNotificacion(n);    
  
  }

 

  /**
   * Recarga las notificaciones del usuario actual y las muestra en la vista.
   */
  public void refrescarNotificaciones(){
    notificacionesPanel.clearNotificaciones(); 
    Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
    if((usuarioActual instanceof Empleado e)){
      List<Notificacion> notificaciones = new ArrayList<>(e.getNotificaciones());
      Collections.reverse(notificaciones);
      notificaciones.forEach(n -> notificacionesPanel.agregarNotificacion(n)); 
    }
    if((usuarioActual instanceof ClienteRegistrado c)){
      List<Notificacion> notificaciones = new ArrayList<>(c.getNotificaciones());
      Collections.reverse(notificaciones);
      notificaciones.forEach(n -> notificacionesPanel.agregarNotificacion(n)); 
    }
  }

  /**
   * Elimina la notificación indicada del usuario actual (si aplica) y
   * refresca la lista mostrada.
   *
   * @param notif notificación a borrar
   */
  public void borrarNotificacion(Notificacion notif){
    Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
    if((usuarioActual instanceof Empleado e)){
      if(notif instanceof NotificacionEmpleado notifE) 
      e.eliminarNotificacion(notifE);
    }
    if((usuarioActual instanceof ClienteRegistrado c)){
      if(notif instanceof NotificacionCliente notifC){
        c.eliminarNotificacion(notifC);
      }
    }
    refrescarNotificaciones();
  }
}
