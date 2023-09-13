package sales.kiwamirembe.com.classes;

public class Sale {

    public int sale_id;
    public String sale_number; // this is used as receipt number
    public int sale_customer_id;
    public float sale_total;
    public long sale_date;
    public int sale_payment_status;
    public int sale_web_synced;

    // Constructor without parameters
    public Sale() {
    }

    // Constructor with parameters
    public Sale(int sale_id, String sale_number, int sale_customer_id, float sale_total,
                long sale_date, int sale_payment_status, int sale_web_synced) {
        this.sale_id = sale_id;
        this.sale_number = sale_number;
        this.sale_customer_id = sale_customer_id;
        this.sale_total = sale_total;
        this.sale_date = sale_date;
        this.sale_payment_status = sale_payment_status;
        this.sale_web_synced = sale_web_synced;
    }

    // Constructor with parameters
    public Sale(String sale_number, int sale_customer_id, float sale_total,
                long sale_date, int sale_payment_status, int sale_web_synced) {
        this.sale_id = sale_id;
        this.sale_number = sale_number;
        this.sale_customer_id = sale_customer_id;
        this.sale_total = sale_total;
        this.sale_date = sale_date;
        this.sale_payment_status = sale_payment_status;
        this.sale_web_synced = sale_web_synced;
    }

    // Getter and Setter for sale_id
    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    // Getter and Setter for sale_number
    public String getSale_number() {
        return sale_number;
    }

    public void setSale_number(String sale_number) {
        this.sale_number = sale_number;
    }

    // Getter and Setter for sale_customer_id
    public int getSale_customer_id() {
        return sale_customer_id;
    }

    public void setSale_customer_id(int sale_customer_id) {
        this.sale_customer_id = sale_customer_id;
    }

    // Getter and Setter for sale_total
    public float getSale_total() {
        return sale_total;
    }

    public void setSale_total(float sale_total) {
        this.sale_total = sale_total;
    }

    // Getter and Setter for sale_date
    public long getSale_date() {
        return sale_date;
    }

    public void setSale_date(long sale_date) {
        this.sale_date = sale_date;
    }

    // Getter and Setter for sale_payment_status
    public int getSale_payment_status() {
        return sale_payment_status;
    }

    public void setSale_payment_status(int sale_payment_status) {
        this.sale_payment_status = sale_payment_status;
    }

    // Getter and Setter for sale_web_synced
    public int getSale_web_synced() {
        return sale_web_synced;
    }

    public void setSale_web_synced(int sale_web_synced) {
        this.sale_web_synced = sale_web_synced;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "sale_id=" + sale_id +
                ", sale_number='" + sale_number + '\'' +
                ", sale_customer_id=" + sale_customer_id +
                ", sale_total=" + sale_total +
                ", sale_date=" + sale_date +
                ", sale_payment_status=" + sale_payment_status +
                ", sale_web_synced=" + sale_web_synced +
                '}';
    }
}
