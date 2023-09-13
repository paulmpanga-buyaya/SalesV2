package sales.kiwamirembe.com.classes;

public class Customer {

    private int customer_id;
    private String customer_business_name;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private int customer_web_synced;

    // Constructor without parameters
    public Customer() {
    }

    // Constructor with parameters
    public Customer(int customer_id, String customer_business_name, String customer_name,
                    String customer_email, String customer_phone, int customer_web_synced) {
        this.customer_id = customer_id;
        this.customer_business_name = customer_business_name;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
        this.customer_web_synced = customer_web_synced;
    }

    // Getter and Setter for customer_id
    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    // Getter and Setter for customer_business_name
    public String getCustomer_business_name() {
        return customer_business_name;
    }

    public void setCustomer_business_name(String customer_business_name) {
        this.customer_business_name = customer_business_name;
    }

    // Getter and Setter for customer_name
    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    // Getter and Setter for customer_email
    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    // Getter and Setter for customer_phone
    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    // Getter and Setter for customer_web_synced
    public int getCustomer_web_synced() {
        return customer_web_synced;
    }

    public void setCustomer_web_synced(int customer_web_synced) {
        this.customer_web_synced = customer_web_synced;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id=" + customer_id +
                ", customer_business_name='" + customer_business_name + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", customer_email='" + customer_email + '\'' +
                ", customer_phone='" + customer_phone + '\'' +
                ", customer_web_synced=" + customer_web_synced +
                '}';
    }
}
