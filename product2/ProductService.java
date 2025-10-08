import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class ProductService {
    private ArrayList<Product> products = new ArrayList<>();
    private static final String PRODUCT_FILE = "products.dat";

    public ProductService() {
        loadProducts();
        if (products.isEmpty()) {
            // Add sample products with Indian names and INR
            products.add(new Product("INV001", "Samsung TV 55\"", "Electronics", 
                    LocalDate.of(2024, 1, 15), LocalDate.of(2025, 1, 15), 45999.00,
                    "Rajesh Kumar", "rajesh.kumar@gmail.com", "9876543210"));
            
            products.add(new Product("INV002", "iPhone 15 Pro", "Mobile", 
                    LocalDate.of(2024, 3, 10), LocalDate.of(2025, 3, 10), 129999.00,
                    "Priya Sharma", "priya.sharma@gmail.com", "9123456789"));
            
            products.add(new Product("INV003", "Dell Laptop XPS", "Computers", 
                    LocalDate.of(2023, 12, 1), LocalDate.of(2024, 12, 1), 89999.00,
                    "Amit Patel", "amit.patel@gmail.com", "9898765432"));
            
            products.add(new Product("INV004", "LG Refrigerator", "Appliances", 
                    LocalDate.of(2024, 2, 20), LocalDate.of(2026, 2, 20), 34999.00,
                    "Sunita Singh", "sunita.singh@gmail.com", "9765432109"));
            
            products.add(new Product("INV005", "Sony Headphones", "Audio", 
                    LocalDate.of(2024, 4, 5), LocalDate.of(2025, 4, 5), 8999.00,
                    "Vikram Malhotra", "vikram.m@gmail.com", "9345678901"));
            saveProducts();
        }
    }

    public Product getProductByInvoice(String invoiceNumber) {
        for (Product product : products) {
            if (product.invoiceNumber.equalsIgnoreCase(invoiceNumber)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public boolean addProduct(Product product) {
        if (getProductByInvoice(product.invoiceNumber) != null) {
            return false;
        }
        products.add(product);
        saveProducts();
        return true;
    }

    public boolean deleteProduct(String invoiceNumber) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).invoiceNumber.equals(invoiceNumber)) {
                products.remove(i);
                saveProducts();
                return true;
            }
        }
        return false;
    }

    public boolean updateProduct(String invoiceNumber, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).invoiceNumber.equals(invoiceNumber)) {
                products.set(i, updatedProduct);
                saveProducts();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Product> searchProducts(String searchTerm) {
        ArrayList<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.productName.toLowerCase().contains(searchTerm.toLowerCase()) ||
                product.productCategory.toLowerCase().contains(searchTerm.toLowerCase()) ||
                product.invoiceNumber.toLowerCase().contains(searchTerm.toLowerCase()) ||
                product.customerName.toLowerCase().contains(searchTerm.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private void loadProducts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCT_FILE))) {
            products = (ArrayList<Product>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No existing product data found. Starting with sample data.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    private void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }
}