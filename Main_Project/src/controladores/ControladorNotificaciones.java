package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.aplicacion.Aplicacion;
import modelo.notificacion.Notificacion;
import modelo.notificacion.NotificacionCliente;
import modelo.notificacion.NotificacionEmpleado;
import modelo.usuario.ClienteRegistrado;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;
import vista.userPanels.NotificacionesPanel;

public class ControladorNotificaciones implements ActionListener{
  NotificacionesPanel notificacionesPanel;
  MainController mainController;
	
	public ControladorNotificaciones(NotificacionesPanel notificacionesPanel, MainController m) {
		this.mainController = m;
    this.notificacionesPanel = notificacionesPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}

  public void leerNotificacion(Notificacion n){
    if (n == null) return;
    // Marcamos la notificación como leída
    n.read();
    mainController.abrirVentanaNotificacion(n);    

  }

 

  private void refrescarNotificaciones(){
    notificacionesPanel.clearNotificaciones(); 
    Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
    if((usuarioActual instanceof Empleado e)){
      e.getNotificaciones().forEach(n -> notificacionesPanel.agregarNotificacion(n)); 
    }
    if((usuarioActual instanceof ClienteRegistrado c)){
      c.getNotificaciones().forEach(n -> notificacionesPanel.agregarNotificacion(n)); 
    }
  }

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
