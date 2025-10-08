import java.io.Serializable;

public class User implements Serializable {
    public String username;
    public String password;
    public String email;
    public String phone;
    public boolean isAdmin;

    public User(String username, String password, String email, String phone, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return String.format(
            "User: %s | Email: %s | Phone: %s | Type: %s",
            username, email, phone, isAdmin ? "Admin" : "Customer"
        );
    }
}