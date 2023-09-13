package sales.kiwamirembe.com.classes;

public class Item {

    private int item_id;
    private String item_name;
    private String item_sku;
    private float item_price;
    private int item_unit_id;
    private int item_category_id;
    private int item_web_synced;

    // Constructor without parameters
    public Item() {
    }

    // Constructor with parameters
    public Item(int item_id, String item_name, String item_sku, float item_price,
                int item_unit_id, int item_category_id, int item_web_synced) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_sku = item_sku;
        this.item_price = item_price;
        this.item_unit_id = item_unit_id;
        this.item_category_id = item_category_id;
        this.item_web_synced = item_web_synced;
    }

    // Getter and Setter for item_id
    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    // Getter and Setter for item_name
    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    // Getter and Setter for item_sku
    public String getItem_sku() {
        return item_sku;
    }

    public void setItem_sku(String item_sku) {
        this.item_sku = item_sku;
    }

    // Getter and Setter for item_price
    public float getItem_price() {
        return item_price;
    }

    public void setItem_price(float item_price) {
        this.item_price = item_price;
    }

    // Getter and Setter for item_unit_id
    public int getItem_unit_id() {
        return item_unit_id;
    }

    public void setItem_unit_id(int item_unit_id) {
        this.item_unit_id = item_unit_id;
    }

    // Getter and Setter for item_category_id
    public int getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(int item_category_id) {
        this.item_category_id = item_category_id;
    }

    // Getter and Setter for item_web_synced
    public int getItem_web_synced() {
        return item_web_synced;
    }

    public void setItem_web_synced(int item_web_synced) {
        this.item_web_synced = item_web_synced;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_id=" + item_id +
                ", item_name='" + item_name + '\'' +
                ", item_sku='" + item_sku + '\'' +
                ", item_price=" + item_price +
                ", item_unit_id=" + item_unit_id +
                ", item_category_id=" + item_category_id +
                ", item_web_synced=" + item_web_synced +
                '}';
    }
}
