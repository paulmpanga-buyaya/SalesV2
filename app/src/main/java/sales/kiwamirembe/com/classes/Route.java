package sales.kiwamirembe.com.classes;

public class Route {

    private int route_id;
    private String route_name;
    private int route_web_synced;

    // Constructor without parameters
    public Route() {
    }

    // Constructor with parameters
    public Route(int route_id, String route_name, int route_web_synced) {
        this.route_id = route_id;
        this.route_name = route_name;
        this.route_web_synced = route_web_synced;
    }

    // Constructor with parameters
    public Route(String route_name, int route_web_synced) {
        this.route_name = route_name;
        this.route_web_synced = route_web_synced;
    }

    // Getter and Setter for route_id
    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    // Getter and Setter for route_name
    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    // Getter and Setter for route_web_synced
    public int getRoute_web_synced() {
        return route_web_synced;
    }

    public void setRoute_web_synced(int route_web_synced) {
        this.route_web_synced = route_web_synced;
    }

    @Override
    public String toString() {
        return "Route{" +
                "route_id=" + route_id +
                ", route_name='" + route_name + '\'' +
                ", route_web_synced=" + route_web_synced +
                '}';
    }
}
