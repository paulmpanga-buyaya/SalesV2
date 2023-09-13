package sales.kiwamirembe.com.classes;

public class Inventory {

    public int inventory_id;
    public int inventory_item_id;
    public String inventory_item_name;
    public String inventory_item_sku;
    public int inventory_item_price;
    public int inventory_item_stock;
    public int inventory_item_web_synced;

    // Constructor without parameters
    public Inventory() {
    }

    // Constructor with parameters
    public Inventory(int inventory_id, int inventory_item_id, String inventory_item_name,
                     String inventory_item_sku, int inventory_item_price, int inventory_item_stock,
                     int inventory_item_web_synced) {
        this.inventory_id = inventory_id;
        this.inventory_item_id = inventory_item_id;
        this.inventory_item_name = inventory_item_name;
        this.inventory_item_sku = inventory_item_sku;
        this.inventory_item_price = inventory_item_price;
        this.inventory_item_stock = inventory_item_stock;
        this.inventory_item_web_synced = inventory_item_web_synced;
    }

    // Getter and Setter for inventory_id
    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    // Getter and Setter for inventory_item_id
    public int getInventory_item_id() {
        return inventory_item_id;
    }

    public void setInventory_item_id(int inventory_item_id) {
        this.inventory_item_id = inventory_item_id;
    }

    // Getter and Setter for inventory_item_name
    public String getInventory_item_name() {
        return inventory_item_name;
    }

    public void setInventory_item_name(String inventory_item_name) {
        this.inventory_item_name = inventory_item_name;
    }

    // Getter and Setter for inventory_item_sku
    public String getInventory_item_sku() {
        return inventory_item_sku;
    }

    public void setInventory_item_sku(String inventory_item_sku) {
        this.inventory_item_sku = inventory_item_sku;
    }

    // Getter and Setter for inventory_item_price
    public int getInventory_item_price() {
        return inventory_item_price;
    }

    public void setInventory_item_price(int inventory_item_price) {
        this.inventory_item_price = inventory_item_price;
    }

    // Getter and Setter for inventory_item_stock
    public int getInventory_item_stock() {
        return inventory_item_stock;
    }

    public void setInventory_item_stock(int inventory_item_stock) {
        this.inventory_item_stock = inventory_item_stock;
    }

    // Getter and Setter for inventory_item_web_synced
    public int getInventory_item_web_synced() {
        return inventory_item_web_synced;
    }

    public void setInventory_item_web_synced(int inventory_item_web_synced) {
        this.inventory_item_web_synced = inventory_item_web_synced;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventory_id=" + inventory_id +
                ", inventory_item_id=" + inventory_item_id +
                ", inventory_item_name='" + inventory_item_name + '\'' +
                ", inventory_item_sku='" + inventory_item_sku + '\'' +
                ", inventory_item_price=" + inventory_item_price +
                ", inventory_item_stock=" + inventory_item_stock +
                ", inventory_item_web_synced=" + inventory_item_web_synced +
                '}';
    }
}
