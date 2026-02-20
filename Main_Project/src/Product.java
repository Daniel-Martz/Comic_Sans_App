public abstract class Product {

	private int ID;
	private String name;
	private String description;
	private double price;
	private String photo;
	private int stock;
	private Category category;
	
	public Product(int ID, String name, String description, double price, String photo, int stock, Category category)
	{
		this.ID = ID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.photo = photo;
		this.stock = stock;
		this.category = category;
	}
	
	public boolean hasAvaliableStock(int desiredStock)
	{
		return this.stock >= desiredStock;
	}
	
	public void restock(int addStock) {
		if (addStock > 0) {
			this.stock += addStock;
		}
	}

	public boolean sellProduct(int desiredStock) {
		if (hasAvaliableStock(desiredStock)) {
			this.stock -= desiredStock;
			return true; 
		}
		System.out.println("Error: Not enough stock for " + this.name);
		return false; 
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
}