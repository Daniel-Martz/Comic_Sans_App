package usuario;
import java.util.*;

public class ClienteRegistrado extends Usuario {
	private Set<NotificacionDeseada> configuracionNotificaciones = new HashSet<NotificacionDeseada>();

	public ClienteRegistrado(String username, String DNI, String password) {
		super(username, DNI, password);
	}
	
}
