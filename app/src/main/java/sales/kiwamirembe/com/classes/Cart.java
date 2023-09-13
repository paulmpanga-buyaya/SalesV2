package sales.kiwamirembe.com.classes;

public class Cart {

    public int cart_id;
    public int cart_customer_id;
    public String cart_number; // to be used as receipt number
    public int cart_user_id;

    // Constructor without parameters
    public Cart() {
    }

    // Constructor with parameters
    public Cart(int cart_id, int cart_customer_id, String cart_number, int cart_user_id) {
        this.cart_id = cart_id;
        this.cart_customer_id = cart_customer_id;
        this.cart_number = cart_number;
        this.cart_user_id = cart_user_id;
    }

    // Constructor with parameters
    public Cart(int cart_customer_id, String cart_number, int cart_user_id) {
        this.cart_id = cart_id;
        this.cart_customer_id = cart_customer_id;
        this.cart_number = cart_number;
        this.cart_user_id = cart_user_id;
    }

    // Getter and Setter for cart_id
    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    // Getter and Setter for cart_customer_id
    public int getCart_customer_id() {
        return cart_customer_id;
    }

    public void setCart_customer_id(int cart_customer_id) {
        this.cart_customer_id = cart_customer_id;
    }

    // Getter and Setter for cart_number
    public String getCart_number() {
        return cart_number;
    }

    public void setCart_number(String cart_number) {
        this.cart_number = cart_number;
    }

    // Getter and Setter for cart_user_id
    public int getCart_user_id() {
        return cart_user_id;
    }

    public void setCart_user_id(int cart_user_id) {
        this.cart_user_id = cart_user_id;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cart_id=" + cart_id +
                ", cart_customer_id=" + cart_customer_id +
                ", cart_number='" + cart_number + '\'' +
                ", cart_user_id=" + cart_user_id +
                '}';
    }
}
