import java.util.*;


public class Client extends User{

	private Set<Notification> notificaciones = new HashSet<>();
	private Set<SecondHandProduct> ecHandProdcuts = new HashSet<>();
	
	public Client(String username, String DNI, String password, Set<Notification> notification) {
		super(username, DNI, password);
		
	}
	

}
