package itpu.warehouse.utils;

import itpu.warehouse.Constants;
import itpu.warehouse.entity.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static itpu.warehouse.Constants.*;

public class DataUtil {

    private static final String DELIMITER = ",";

    public static void loadDataFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip the header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(DELIMITER);
                Integer id = Integer.valueOf(row[0]);
                String name = row[1];
                Float price = Float.valueOf(row[3]);
                Integer quantity = Integer.valueOf(row[4]);
                String productType = "";
                switch (row[2]) {
                    case "Television": productType = Constants.PRODUCT_TELEVISION; break;
                    case "SmartPhone": productType = Constants.PRODUCT_SMARTPHONE; break;
                    case "Notebook": productType = PRODUCT_NOTEBOOK; break;
                    case "Monitor": productType = PRODUCT_MONITOR; break;
                    case "Keyboard": productType = PRODUCT_KEYBOARD; break;
                    case "Mouse": productType = PRODUCT_MOUSE; break;
                }
                Product product = new Product();
                product.setId(id);
                product.setCategory(productType);
                product.setName(name);
                product.setPrice(price);
                product.setQuantity(quantity);
                if (products.containsKey(productType)) {
                    Constants.products.get(productType).add(product);
                } else {
                    ArrayList<Product> list = new ArrayList<>();
                    list.add(product);
                    Constants.products.put(productType, list);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}
