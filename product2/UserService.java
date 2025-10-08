import java.util.ArrayList;
import java.io.*;

public class UserService {
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser = null;
    private static final String USER_FILE = "users.dat";

    public UserService() {
        loadUsers();
        if (users.isEmpty()) {
            // Add default admin user
            users.add(new User("admin", "admin123", "admin@company.com", "9876543210", true));
            // Add sample customer
            users.add(new User("customer", "customer123", "customer@gmail.com", "9123456780", false));
            saveUsers();
        }
    }

    public boolean register(String username, String password, String email, String phone, boolean isAdmin) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password, email, phone, isAdmin));
        saveUsers();
        return true;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing user data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}