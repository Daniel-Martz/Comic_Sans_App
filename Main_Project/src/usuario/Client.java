package usuario;
import java.util.*;

import User;


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
	
	@Override
	public String toString() {
		return "Client [secondHandProducts=" + secondHandProducts + ", getSecondHandProducts()="
				+ getSecondHandProducts() + ", getUsername()=" + getUsername() + ", getDNI()=" + getDNI()
				+ ", getPassword()=" + getPassword() + ", toString()=" + super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	public Set<SecondHandProduct> getSecondHandProducts() {
		return secondHandProducts;
	}

	public void setSecondHandProducts(Set<SecondHandProduct> secondHandProducts) {
		this.secondHandProducts = secondHandProducts;
	}
 