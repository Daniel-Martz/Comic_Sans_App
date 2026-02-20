import java.util.*;


public class Client extends User{

	private Set<Notification> notifications = new HashSet<>();
	private Set<SecondHandProduct> secondHandProducts = new HashSet<>();
	
	public Client(String username, String DNI, String password, Set<Notification> notifications, Set<SecondHandProduct> secondHandProducts) {
		super(username, DNI, password);
		this.notifications = (notifications != null) ? notifications : new HashSet<>();
        this.secondHandProducts = (secondHandProducts != null) ? secondHandProducts : new HashSet<>();
	}
	
	public void addProductToSecondHandProducts(SecondHandProduct product){
		this.secondHandProducts.add(product);
	}
	
}
 