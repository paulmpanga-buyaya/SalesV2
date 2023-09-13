package sales.kiwamirembe.com.classes;

public class SaleItem {

    public int sale_item_id; // this is not the ITEM ITEM_ID
    public String sale_item_sale_number; // this is the sale_number
    public String sale_item_name;
    public String sale_item_sku;
    public float sale_item_price;
    public int sale_item_quantity;
    public float sale_item_total;
    public int sale_item_web_synced;

    // Constructor without parameters
    public SaleItem() {
    }

    // Constructor with parameters
    public SaleItem(int sale_item_id, String sale_item_sale_number, String sale_item_name,
                    String sale_item_sku, float sale_item_price, int sale_item_quantity,
                    float sale_item_total, int sale_item_web_synced) {
        this.sale_item_id = sale_item_id;
        this.sale_item_sale_number = sale_item_sale_number;
        this.sale_item_name = sale_item_name;
        this.sale_item_sku = sale_item_sku;
        this.sale_item_price = sale_item_price;
        this.sale_item_quantity = sale_item_quantity;
        this.sale_item_total = sale_item_total;
        this.sale_item_web_synced = sale_item_web_synced;
    }

    public SaleItem(String sale_item_sale_number, String sale_item_name,
                    String sale_item_sku, float sale_item_price, int sale_item_quantity,
                    float sale_item_total, int sale_item_web_synced) {
        this.sale_item_id = sale_item_id;
        this.sale_item_sale_number = sale_item_sale_number;
        this.sale_item_name = sale_item_name;
        this.sale_item_sku = sale_item_sku;
        this.sale_item_price = sale_item_price;
        this.sale_item_quantity = sale_item_quantity;
        this.sale_item_total = sale_item_total;
        this.sale_item_web_synced = sale_item_web_synced;
    }

    // Getter and Setter for sale_item_id
    public int getSale_item_id() {
        return sale_item_id;
    }

    public void setSale_item_id(int sale_item_id) {
        this.sale_item_id = sale_item_id;
    }

    // Getter and Setter for sale_item_sale_number
    public String getSale_item_sale_number() {
        return sale_item_sale_number;
    }

    public void setSale_item_sale_number(String sale_item_sale_number) {
        this.sale_item_sale_number = sale_item_sale_number;
    }

    // Getter and Setter for sale_item_name
    public String getSale_item_name() {
        return sale_item_name;
    }

    public void setSale_item_name(String sale_item_name) {
        this.sale_item_name = sale_item_name;
    }

    // Getter and Setter for sale_item_sku
    public String getSale_item_sku() {
        return sale_item_sku;
    }

    public void setSale_item_sku(String sale_item_sku) {
        this.sale_item_sku = sale_item_sku;
    }

    // Getter and Setter for sale_item_price
    public float getSale_item_price() {
        return sale_item_price;
    }

    public void setSale_item_price(float sale_item_price) {
        this.sale_item_price = sale_item_price;
    }

    // Getter and Setter for sale_item_quantity
    public int getSale_item_quantity() {
        return sale_item_quantity;
    }

    public void setSale_item_quantity(int sale_item_quantity) {
        this.sale_item_quantity = sale_item_quantity;
    }

    // Getter and Setter for sale_item_total
    public float getSale_item_total() {
        return sale_item_total;
    }

    public void setSale_item_total(float sale_item_total) {
        this.sale_item_total = sale_item_total;
    }

    // Getter and Setter for sale_item_web_synced
    public int getSale_item_web_synced() {
        return sale_item_web_synced;
    }

    public void setSale_item_web_synced(int sale_item_web_synced) {
        this.sale_item_web_synced = sale_item_web_synced;
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "sale_item_id=" + sale_item_id +
                ", sale_item_sale_number='" + sale_item_sale_number + '\'' +
                ", sale_item_name='" + sale_item_name + '\'' +
                ", sale_item_sku='" + sale_item_sku + '\'' +
                ", sale_item_price=" + sale_item_price +
                ", sale_item_quantity=" + sale_item_quantity +
                ", sale_item_total=" + sale_item_total +
                ", sale_item_web_synced=" + sale_item_web_synced +
                '}';
    }
}
