package controladores;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import modelo.aplicacion.Aplicacion;
import modelo.aplicacion.Catalogo;
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
    StringTokenizer st = new StringTokenizer(e.getActionCommand());
    switch(st.nextToken(" ")){
      case "Refresh":
        refrescarNotificaciones();
        break;
      case "Read":
        leerNotificacion(Integer.parseInt(st.nextToken()));
        break;
      case "Delete":
        borrarNotificacion(Integer.parseInt(st.nextToken()));
       break; 
      default:
        
    }
	}

  private void leerNotificacion(int id){
    
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

  private void borrarNotificacion(int id){
    Usuario usuarioActual = Aplicacion.getInstancia().getUsuarioActual();
    if((usuarioActual instanceof Empleado e)){
      List<NotificacionEmpleado> notifs = e.getNotificaciones();
      for(NotificacionEmpleado n : notifs){
        if(n.getId() == id){
          e.eliminarNotificacion(n);
        }
      }
    }
    if((usuarioActual instanceof ClienteRegistrado c)){
      List<NotificacionCliente> notifs = c.getNotificaciones();
      for(NotificacionCliente n : notifs){
        if(n.getId() == id){
          c.eliminarNotificacion(n);
        }
      }
    }
    refrescarNotificaciones();
  }
}
