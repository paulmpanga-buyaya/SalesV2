package sales.kiwamirembe.com.classes;

public class Unit {

    private int unit_id;
    private String unit_name;
    private int unit_web_synced;

    // Constructor without parameters
    public Unit() {
    }

    // Constructor with parameters
    public Unit(int unit_id, String unit_name, int unit_web_synced) {
        this.unit_id = unit_id;
        this.unit_name = unit_name;
        this.unit_web_synced = unit_web_synced;
    }

    // Getter and Setter for unit_id
    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    // Getter and Setter for unit_name
    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    // Getter and Setter for unit_web_synced
    public int getUnit_web_synced() {
        return unit_web_synced;
    }

    public void setUnit_web_synced(int unit_web_synced) {
        this.unit_web_synced = unit_web_synced;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "unit_id=" + unit_id +
                ", unit_name='" + unit_name + '\'' +
                ", unit_web_synced=" + unit_web_synced +
                '}';
    }
}
