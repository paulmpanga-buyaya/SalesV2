package sales.kiwamirembe.com.classes;

public class Category {

    private int category_id;
    private String category_name;
    private String category_sku;
    private int category_web_synced;

    // Constructor without parameters
    public Category() {
    }

    // Constructor with parameters
    public Category(int category_id, String category_name, String category_sku, int category_web_synced) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_sku = category_sku;
        this.category_web_synced = category_web_synced;
    }

    // Getter and Setter for category_id
    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    // Getter and Setter for category_name
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    // Getter and Setter for category_sku
    public String getCategory_sku() {
        return category_sku;
    }

    public void setCategory_sku(String category_sku) {
        this.category_sku = category_sku;
    }

    // Getter and Setter for category_web_synced
    public int getCategory_web_synced() {
        return category_web_synced;
    }

    public void setCategory_web_synced(int category_web_synced) {
        this.category_web_synced = category_web_synced;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                ", category_sku='" + category_sku + '\'' +
                ", category_web_synced=" + category_web_synced +
                '}';
    }
}
