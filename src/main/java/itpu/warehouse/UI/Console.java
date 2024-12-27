package itpu.warehouse.UI;

import itpu.warehouse.dao.ProductDAO;
import itpu.warehouse.entity.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Console {
    public void printWelcomeMessage() {
        System.out.println("Household Warehouse Management System");
        System.out.println("Version: 1.0.0");
        System.out.println("Creator: Xondamir Abrorov | xondamir_abrorov@student.itpu.uz");
        System.out.println("Email: xondamir_abrorov@student.itpu.uz");

        System.out.println("\nAvailable Commands:");
        System.out.println("* help");
        System.out.println("* search <product name>");
        System.out.println("* add <id|name|type|price|quantity>");
        System.out.println("* edit <id|name|type|price|quantity>");
        System.out.println("* listAll");
        System.out.println("* listByType <product type>");
        System.out.println("* clear");
        System.out.println("* exit");
    }

    public void handleSearchCommand(String[] parts, ProductDAO productDAO) {
        if (parts.length >= 2) {
            StringBuffer pName = new StringBuffer();
            for (int i=1; i<parts.length; i++) {
                pName.append(parts[i]).append(" ");
            }
            String productName = pName.toString().trim();
            List<Product> searchResults = productDAO.findByName(productName);
            if (searchResults.isEmpty()) {
                System.out.println("No products found for: " + productName);
            } else {
                System.out.println("Search Results:");
                if (!searchResults.isEmpty()) {
                    System.out.println("--------------------------------------------------------------------------------------------");
                    System.out.printf("| %-7s | %-25s | %-25s | %-11s | %-8s\n", "ID", "Name", "Category", "Price ($)", "Quantity |");
                    System.out.println("--------------------------------------------------------------------------------------------");
                }
                for (Product product : searchResults) {
                    System.out.println(product);
                }
                if (!searchResults.isEmpty()) {
                    System.out.println("--------------------------------------------------------------------------------------------");
                }
            }
        } else {
            System.out.println("Invalid command format. Usage: search <product name>");
        }
    }

    public void handleListCommand(String[] parts, ProductDAO productDAO) {
        if (parts[0].equals("listAll") || parts[0].equals("listByType")) {
            System.out.println(parts[0].equalsIgnoreCase("listAll") ? "All Products:" : "Products by type. Type: " + parts[1]);
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.printf("| %-7s | %-25s | %-25s | %-11s | %-8s\n", "ID", "Name", "Category", "Price ($)", "Quantity |");
            System.out.println("--------------------------------------------------------------------------------------------");
            List<Product> allProducts = parts[0].equalsIgnoreCase("listAll") ? productDAO.findAll() :
                    productDAO.findByCategory(parts[1]);
            for (Product product : allProducts) {
                System.out.println(product);
            }
            System.out.println("--------------------------------------------------------------------------------------------");
        } else {
            System.out.println("Invalid command format. Usage: list all");
        }
    }

    public void handleAddCommand(String[] parts, ProductDAO productDAO) {
        if (parts.length == 2) {
            parts = parts[1].split("\\|");
            String[] finalParts = parts;
            Optional<Product> product = productDAO.findById(Integer.parseInt(parts[0]));
            if (product.isPresent()) {
                productDAO.update(new Product(finalParts));
                System.out.println("Product edited successfully");
            } else {
                productDAO.save(new Product(parts));
                System.out.println("Product successfully added to the warehouse.");
            }
            handleListCommand(new String[] {"listAll"}, productDAO);
        } else {
            System.out.println("Invalid command format. Usage: list all");
        }
    }

    public void displayHelp() {
        System.out.println("\nAvailable Commands:");
        System.out.println("* search <product name>: Search for a product by name.");
        System.out.println("* listAll: List all products in the inventory.");
        System.out.println("* add <id|name|type|price|quantity>");
        System.out.println("* edit <id|name|type|price|quantity>");
        System.out.println("* listByType <product type>: show the products by their category type");
        System.out.println("* clear: Clear the console screen");
        System.out.println("* exit: exit from warehouse");
    }

    public void printExitMessage() {
        System.out.println("Exiting...");
    }

    public void printInvalidCommandMessage() {
        System.out.println("Invalid command.");
    }

    public void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
}
}
