package sales.kiwamirembe.com.classes;

public class CartItem {

    public int cart_item_id;
    public String cart_item_sku;
    public String cart_item_name;
    public float cart_item_price;
    public int cart_item_quantity;
    public float cart_item_total;
    public float cart_item_discount;

    // Constructor without parameters
    public CartItem() {
    }

    // Constructor with parameters
    public CartItem(int cart_item_id, String cart_item_sku, String cart_item_name,
                    float cart_item_price, int cart_item_quantity, float cart_item_total,
                    float cart_item_discount) {
        this.cart_item_id = cart_item_id;
        this.cart_item_sku = cart_item_sku;
        this.cart_item_name = cart_item_name;
        this.cart_item_price = cart_item_price;
        this.cart_item_quantity = cart_item_quantity;
        this.cart_item_total = cart_item_total;
        this.cart_item_discount = cart_item_discount;
    }

    // Constructor with parameters
    public CartItem(String cart_item_sku, String cart_item_name,
                    float cart_item_price, int cart_item_quantity, float cart_item_total,
                    float cart_item_discount) {
        this.cart_item_id = cart_item_id;
        this.cart_item_sku = cart_item_sku;
        this.cart_item_name = cart_item_name;
        this.cart_item_price = cart_item_price;
        this.cart_item_quantity = cart_item_quantity;
        this.cart_item_total = cart_item_total;
        this.cart_item_discount = cart_item_discount;
    }

    // Getter and Setter for cart_item_id
    public int getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(int cart_item_id) {
        this.cart_item_id = cart_item_id;
    }

    // Getter and Setter for cart_item_sku
    public String getCart_item_sku() {
        return cart_item_sku;
    }

    public void setCart_item_sku(String cart_item_sku) {
        this.cart_item_sku = cart_item_sku;
    }

    // Getter and Setter for cart_item_name
    public String getCart_item_name() {
        return cart_item_name;
    }

    public void setCart_item_name(String cart_item_name) {
        this.cart_item_name = cart_item_name;
    }

    // Getter and Setter for cart_item_price
    public float getCart_item_price() {
        return cart_item_price;
    }

    public void setCart_item_price(float cart_item_price) {
        this.cart_item_price = cart_item_price;
    }

    // Getter and Setter for cart_item_quantity
    public int getCart_item_quantity() {
        return cart_item_quantity;
    }

    public void setCart_item_quantity(int cart_item_quantity) {
        this.cart_item_quantity = cart_item_quantity;
    }

    // Getter and Setter for cart_item_total
    public float getCart_item_total() {
        return cart_item_total;
    }

    public void setCart_item_total(float cart_item_total) {
        this.cart_item_total = cart_item_total;
    }

    // Getter and Setter for cart_item_discount
    public float getCart_item_discount() {
        return cart_item_discount;
    }

    public void setCart_item_discount(float cart_item_discount) {
        this.cart_item_discount = cart_item_discount;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cart_item_id=" + cart_item_id +
                ", cart_item_sku='" + cart_item_sku + '\'' +
                ", cart_item_name='" + cart_item_name + '\'' +
                ", cart_item_price=" + cart_item_price +
                ", cart_item_quantity=" + cart_item_quantity +
                ", cart_item_total=" + cart_item_total +
                ", cart_item_discount=" + cart_item_discount +
                '}';
    }
}
