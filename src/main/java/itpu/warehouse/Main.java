package itpu.warehouse;

import itpu.warehouse.auth.AuthService;
import itpu.warehouse.auth.User;
import itpu.warehouse.dao.ProductDAO;
import itpu.warehouse.dao.ProductDAOImpl;
import itpu.warehouse.UI.Console;
import itpu.warehouse.utils.DataUtil;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ProductDAO productDAO = new ProductDAOImpl();
    private static final Console console = new Console();

    public static void main(String[] args) {
        System.out.println("Loading data...");
        DataUtil.loadDataFromCSV();
        AuthService authService = new AuthService();
        boolean isAuthenticated = false;
        Scanner scanner = new Scanner(System.in);
         while (!isAuthenticated) {
            System.out.println("\nWelcome! Please choose an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        isAuthenticated = authService.login(username, password);
                        // get role by this username and check if the user is SUPER_ADMIN
                        User currentUser = authService.getCurrentUser();
                        if (currentUser != null && currentUser.getRole().equalsIgnoreCase("SUPER_ADMIN")) {
                            System.out.println("You are logged in as SUPER_ADMIN.");
                            // Ask if the SUPER_ADMIN wants to create a new admin or see the admin list
                            System.out.println("What would you like to do?");
                            System.out.println("1. Create new Admin");
                            System.out.println("2. See the admin list");
                            String adminChoice = scanner.nextLine();
                            switch (adminChoice) {
                                case "1":
                                    Boolean trueAdminName = false;
                                    String adminName = "";
                                    String newPassword = "";
                                    while (!trueAdminName) {
                                        System.out.println("Enter admin name: ");
                                         adminName = scanner.nextLine();
                                        Boolean isExistUsername = authService.checkAdminName(adminName);
                                        if (isExistUsername) {
                                            System.out.println("This admin is already exists");
                                        } else {
                                            trueAdminName = true;
                                        }
                                    }
                                    Boolean truePasswrod = false;
                                    while (!truePasswrod) {
                                        System.out.println("Enter password (at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character): ");
                                        newPassword = scanner.nextLine();
                                        if (authService.isStrongPassword(newPassword)) {
                                            truePasswrod = true;
                                        } else {
                                            System.out.println("Password is not strong enough. Please try again.");
                                        }
                                    }
                                    try {
                                        authService.registerAdmin(adminName, newPassword);
                                    } catch (Exception e) {
                                        System.out.println("Error during registration: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                    break;
                                case "2":
                                    List<User> adminUsers = authService.getAdmins();
                                    for (User user : adminUsers) {
                                        System.out.println("Username: " + user.getUsername() + ", Role: " + user.getRole());
                                    }
                                default:
                                    break;
                            }
                        }
                        break;
                    case "2":
                        
                        System.out.print("Enter username: ");
                        String newUsername = scanner.nextLine();
                        // Boolean isValidUsername = authService.isUsernameValid(newUsername);
                        System.out.print("Enter password: ");
                        String newPassword = scanner.nextLine();
                        try {
                            authService.register(newUsername, newPassword);
                        } catch (Exception e) {
                            System.out.println("Error during registration: " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "clear":
                        console.clearConsole();  // Call the clearConsole method to clear the screen
                        break;    
                    default:
                        System.out.println("Invalid option. Please choose 1 or 2.");
                }
            } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        console.printWelcomeMessage();

        boolean exit = false;
        while (!exit) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            switch (parts[0]) {
                case "search":
                    console.handleSearchCommand(parts, productDAO);
                    break;
                case "add":
                case "edit":
                    console.handleAddCommand(parts, productDAO);
                    break;
                case "listAll":
                case "listByType":
                    console.handleListCommand(parts, productDAO);
                    break;
                case "clear":
                    console.clearConsole();
                    break;
                case "help":
                    console.displayHelp();
                    break;
                case "exit":
                    exit = true;
                    console.printExitMessage();
                    break;
                default:
                    console.printInvalidCommandMessage();
            }
        }
        scanner.close();
    }
}
