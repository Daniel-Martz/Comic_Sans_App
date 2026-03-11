package usuario;
import notificacion.Notification;

public class User {
	
	private String username;
	private String DNI;
	private String password;
	private Notification n;
	
	
	public User(String username, String DNI, String password) {
		this.username = username;
		this.DNI = DNI;
		this.password = password;
		this-n = new Notification();

	}
	
	
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getDNI() {
		return DNI;
	}


	public void setDNI(String dNI) {
		DNI = dNI;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		return "User information: " + "\n" + "\t" + "- Username: " + this.username + "\n" + "\t" + "- DNI: " + this.DNI + "\n" + "\t" + "- Password:" + this.password;
	}
}
