package itpu.warehouse.auth;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private final String userFilePath = "src/main/resources/Users.csv";
    private final Map<String, User> users = new HashMap<>();
    private User currentUser = null;

    public AuthService() {
        loadUsers();
    }

    public boolean login(String username, String password) throws NoSuchAlgorithmException {
        User user = users.get(username);
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }

        String hashedPassword = hashPassword(password);
        if (!hashedPassword.equals(user.getPasswordHash())) {
            System.out.println("Invalid password!");
            return false;
        }

        currentUser = user;
        System.out.println("Welcome, " + username + " (" + user.getRole() + ")");
        return true;
    }

    public void register(String username, String password) throws IOException, NoSuchAlgorithmException {
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters long.");
        }
        if (password.length() < 8 || !password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one number.");
        }

        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User already exists!");
        }
        String role = "USER";

        String hashedPassword = hashPassword(password);
        User newUser = new User(username, hashedPassword, role);

        // Add to memory
        users.put(username, newUser);

        // Append to CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath, true))) {
            writer.write(username + "," + hashedPassword + "," + role);
            writer.newLine();
        }

        System.out.println("User registered successfully!");
    }
    public void registerAdmin(String username, String password) throws IOException, NoSuchAlgorithmException {
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters long.");
        }
        if (password.length() < 8 || !password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one number.");
        }

        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User already exists!");
        }
        String role = "ADMIN";
        String hashedPassword = hashPassword(password);
        User newUser = new User(username, hashedPassword, role);

        // Add to memory
        users.put(username, newUser);

        // Append to CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath, true))) {
            writer.write(username + "," + hashedPassword + "," + role);
            writer.newLine();
        }

        System.out.println("Admin registered successfully!");
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void loadUsers() {
        File file = new File(userFilePath);
        if (!file.exists()) {
            System.out.println("Users file not found. Starting with an empty user base.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String passwordHash = parts[1];
                    String role = parts[2];
                    users.put(username, new User(username, passwordHash, role));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    public boolean isStrongPassword(String password){
        if (password.length() < 8 || !password.matches(".*[0-9].*")) {
            return false;
        } 
        return true;
    }

    public boolean isUsernameValid(String username) {
        if (username.length() < 5) {
            return false;
        }
        return true;
    }

    public boolean checkAdminName(String username){
        User user = users.get(username);
        if (user == null) {
            return false;
        }
        return true;
    }

    public boolean checkUsername(String username){
        User user = users.get(username);
        if (user == null) {
            System.out.println("User not found!");
            return false;
        }
        return true;
    }

    public List<User> getAdmins() {
        List<User> adminUsers = new ArrayList<>();
        
        for (User user : users.values()) {
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                adminUsers.add(user);
            }
        }
        
        return adminUsers;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
