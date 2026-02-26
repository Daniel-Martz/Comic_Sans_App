public class Comic extends Product {

    //ATRIBUTOS ESPECÍFICOS
    private String author;
    private int numPages;
    private String editorial;
    private String publishDate;

    public Comic(int id, String name, String description, double price, String photo, int stock, 
                 String author, int numPages, String editorial, String publishDate, Category category) {
        
        super(id, name, description, price, photo, stock, category);

        this.author = author;
        this.numPages = numPages;
        this.editorial = editorial;
        this.publishDate = publishDate;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getNumPages() { return numPages; }
    public void setNumPages(int numPages) { this.numPages = numPages; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public String getPublishDate() { return publishDate; }
    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }


}