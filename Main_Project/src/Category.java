import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<String> subcategories; 

    public Category(String name) {
        this.name = name;
        this.subcategories = new ArrayList<>();
    }

    //se puede añadir categorias
    public void addSubcategory(String sub) {
        this.subcategories.add(sub);
    }

    public void removeSubcategory(String sub) {
        this.subcategories.remove(sub);
    }

    public String getNombre() { 
        return name; 
    }
    
}