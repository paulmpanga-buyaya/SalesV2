package sales.kiwamirembe.com.classes;

public class User {

    public int user_id;
    public String username;
    public int location_id;

    public User() {
    }

    public User(int user_id, String username, int location_id) {
        this.user_id = user_id;
        this.username = username;
        this.location_id = location_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }
}
