
public class User {
	
	private String username;
	private String DNI;
	private String password;
	
	
	public User(String username, String DNI, String password) {
		this.username = username;
		this.DNI = DNI;
		this.password = password;
	}
	
	
	@Override
	public String toString() {
		return "User information: " + "\n" + "\t" + "- Username: " + this.username + "\n" + "\t" + "- DNI: " + this.DNI + "\n" + "\t" + "- Password:" + this.password;
	}
}
