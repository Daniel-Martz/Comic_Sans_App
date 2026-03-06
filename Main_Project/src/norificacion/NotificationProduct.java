package norificacion;
import producto.Product;

public class NotificationProduct extends Notification{
	private Product p;
	
	NotificationProduct(String title, String content, User u, Product p){
		super.Notification(title, content, u);
		this.p = p;
	}

}
