import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminUI {
    private Scanner scanner;
    private UserService userService;
    private ProductService productService;
    private ReturnService returnService;

    public AdminUI() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.productService = new ProductService();
        this.returnService = new ReturnService();
    }

    public void showAdminMainMenu() {
        while (true) {
            System.out.println("\n=== ADMIN PORTAL ===");
            System.out.println("1. Login");
            System.out.println("2. Register New Admin");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    adminRegister();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void adminLogin() {
        System.out.println("\n--- Admin Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userService.login(username, password) && userService.getCurrentUser().isAdmin) {
            System.out.println("Admin login successful! Welcome, " + username + "!");
            showAdminDashboard();
        } else {
            System.out.println("Invalid admin credentials or not an admin account.");
        }
    }

    private void adminRegister() {
        System.out.println("\n--- Admin Registration ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        if (userService.register(username, password, email, phone, true)) {
            System.out.println("Admin registration successful! You can now login.");
        } else {
            System.out.println("Username already exists. Please choose a different username.");
        }
    }

    private void showAdminDashboard() {
        while (true) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("Welcome, " + userService.getCurrentUser().username + "!");
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Return Requests");
            System.out.println("3. View All Users");
            System.out.println("4. View System Statistics");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    manageProducts();
                    break;
                case 2:
                    manageReturns();
                    break;
                case 3:
                    viewAllUsers();
                    break;
                case 4:
                    viewStatistics();
                    break;
                case 5:
                    userService.logout();
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageProducts() {
        while (true) {
            System.out.println("\n--- PRODUCT MANAGEMENT ---");
            System.out.println("1. View All Products");
            System.out.println("2. Add New Product");
            System.out.println("3. Edit Product Details");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Products");
            System.out.println("6. Back to Admin Dashboard");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewAllProducts();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    editProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    searchProducts();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewAllProducts() {
        ArrayList<Product> products = productService.getAllProducts();
        System.out.println("\n=== ALL PRODUCTS ===");
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i));
            }
        }
    }

    private void addProduct() {
        System.out.println("\n--- Add New Product ---");
        System.out.print("Invoice Number: ");
        String invoice = scanner.nextLine();
        System.out.print("Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Purchase Date (YYYY-MM-DD): ");
        LocalDate purchaseDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Warranty End Date (YYYY-MM-DD): ");
        LocalDate warrantyDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Price (INR): ");
        double price = getDoubleInput();
        System.out.print("Customer Name: ");
        String customerName = scanner.nextLine();
        System.out.print("Customer Email: ");
        String customerEmail = scanner.nextLine();
        System.out.print("Customer Phone: ");
        String customerPhone = scanner.nextLine();

        Product product = new Product(invoice, name, category, purchaseDate, warrantyDate, 
                                    price, customerName, customerEmail, customerPhone);
        
        if (productService.addProduct(product)) {
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Failed to add product. Invoice number might already exist.");
        }
    }

    private void editProduct() {
        System.out.println("\n--- Edit Product Details ---");
        System.out.print("Enter Invoice Number of product to edit: ");
        String invoiceNumber = scanner.nextLine();
        
        Product product = productService.getProductByInvoice(invoiceNumber);
        if (product == null) {
            System.out.println("Product not found with invoice number: " + invoiceNumber);
            return;
        }

        System.out.println("\nCurrent Product Details:");
        System.out.println("1. Product Name: " + product.productName);
        System.out.println("2. Category: " + product.productCategory);
        System.out.println("3. Purchase Date: " + product.purchaseDate);
        System.out.println("4. Warranty End Date: " + product.warrantyEndDate);
        System.out.println("5. Price: ₹" + product.price);
        System.out.println("6. Customer Name: " + product.customerName);
        System.out.println("7. Customer Email: " + product.customerEmail);
        System.out.println("8. Customer Phone: " + product.customerPhone);
        
        System.out.println("\nWhat would you like to edit? (Enter 1-8, or 0 to cancel): ");
        int fieldChoice = getIntInput();
        
        if (fieldChoice == 0) {
            System.out.println("Edit cancelled.");
            return;
        }

        switch (fieldChoice) {
            case 1:
                System.out.print("Enter new Product Name: ");
                product.productName = scanner.nextLine();
                break;
            case 2:
                System.out.print("Enter new Category: ");
                product.productCategory = scanner.nextLine();
                break;
            case 3:
                System.out.print("Enter new Purchase Date (YYYY-MM-DD): ");
                product.purchaseDate = LocalDate.parse(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new Warranty End Date (YYYY-MM-DD): ");
                product.warrantyEndDate = LocalDate.parse(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter new Price (INR): ");
                product.price = getDoubleInput();
                break;
            case 6:
                System.out.print("Enter new Customer Name: ");
                product.customerName = scanner.nextLine();
                break;
            case 7:
                System.out.print("Enter new Customer Email: ");
                product.customerEmail = scanner.nextLine();
                break;
            case 8:
                System.out.print("Enter new Customer Phone: ");
                product.customerPhone = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (productService.updateProduct(invoiceNumber, product)) {
            System.out.println(" Product details updated successfully!");
            System.out.println("\nUpdated Product Details:");
            System.out.println("Product Name: " + product.productName);
            System.out.println("Category: " + product.productCategory);
            System.out.println("Purchase Date: " + product.purchaseDate);
            System.out.println("Warranty End Date: " + product.warrantyEndDate);
            System.out.println("Price: ₹" + product.price);
            System.out.println("Customer: " + product.customerName);
        } else {
            System.out.println(" Failed to update product details.");
        }
    }

    private void deleteProduct() {
        System.out.print("Enter Invoice Number to delete: ");
        String invoice = scanner.nextLine();
        
        if (productService.deleteProduct(invoice)) {
            System.out.println(" Product deleted successfully.");
        } else {
            System.out.println(" Product not found.");
        }
    }

    private void searchProducts() {
        System.out.println("\n--- Search Products ---");
        System.out.print("Enter search term (product name, category, invoice, or customer): ");
        String searchTerm = scanner.nextLine();
        
        ArrayList<Product> results = productService.searchProducts(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No products found matching: " + searchTerm);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    private void manageReturns() {
        while (true) {
            System.out.println("\n--- RETURN REQUEST MANAGEMENT ---");
            System.out.println("1. View All Return Requests");
            System.out.println("2. View Pending Requests");
            System.out.println("3. Update Return Status");
            System.out.println("4. Edit Return Request Details");
            System.out.println("5. Back to Admin Dashboard");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewAllReturns();
                    break;
                case 2:
                    viewPendingReturns();
                    break;
                case 3:
                    updateReturnStatus();
                    break;
                case 4:
                    editReturnRequest();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void viewAllReturns() {
        ArrayList<ReturnRequest> returns = returnService.getAllReturnRequests();
        System.out.println("\n=== ALL RETURN REQUESTS ===");
        if (returns.isEmpty()) {
            System.out.println("No return requests found.");
        } else {
            for (ReturnRequest request : returns) {
                System.out.println(request);
            }
        }
    }

    private void viewPendingReturns() {
        ArrayList<ReturnRequest> pendingReturns = returnService.getPendingReturns();
        System.out.println("\n=== PENDING RETURN REQUESTS ===");
        if (pendingReturns.isEmpty()) {
            System.out.println("No pending return requests.");
        } else {
            for (ReturnRequest request : pendingReturns) {
                System.out.println(request);
            }
        }
    }

    private void updateReturnStatus() {
        System.out.print("Enter Return Request ID: ");
        String requestId = scanner.nextLine();
        
        ReturnRequest request = returnService.getReturnRequestById(requestId);
        if (request == null) {
            System.out.println("Return request not found.");
            return;
        }

        System.out.println("Current status: " + request.status);
        System.out.println("Product: " + request.productName);
        System.out.println("Customer: " + request.customerName);
        
        System.out.println("Select new status:");
        System.out.println("1. PENDING");
        System.out.println("2. APPROVED");
        System.out.println("3. REJECTED");
        System.out.println("4. PICKUP_SCHEDULED");
        System.out.println("5. COMPLETED");
        System.out.print("Choose status: ");
        
        int statusChoice = getIntInput();
        String status = getStatusFromChoice(statusChoice);
        
        if (status == null) {
            System.out.println("Invalid status choice.");
            return;
        }

        LocalDate pickupDate = null;
        String instructions = null;
        
        if ("PICKUP_SCHEDULED".equals(status)) {
            System.out.print("Enter pickup date (YYYY-MM-DD): ");
            pickupDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Enter pickup instructions: ");
            instructions = scanner.nextLine();
        }

        if (returnService.updateReturnStatus(requestId, status, pickupDate, instructions)) {
            System.out.println( Return status updated successfully!");
            
            System.out.println("\n=== CUSTOMER NOTIFICATION ===");
            System.out.println("Dear " + request.customerName + ",");
            System.out.println("Your return request " + requestId + " for " + request.productName + " has been " + status.toLowerCase() + ".");
            if (pickupDate != null) {
                System.out.println("Scheduled pickup: " + pickupDate);
            }
            if (instructions != null) {
                System.out.println("Instructions: " + instructions);
            }
            System.out.println("Thank you for choosing our service.");
        } else {
            System.out.println(" Failed to update return status.");
        }
    }

    // NEW: Edit return request details
    private void editReturnRequest() {
        System.out.println("\n--- Edit Return Request ---");
        System.out.print("Enter Return Request ID: ");
        String requestId = scanner.nextLine();
        
        ReturnRequest request = returnService.getReturnRequestById(requestId);
        if (request == null) {
            System.out.println("Return request not found.");
            return;
        }

        System.out.println("\nCurrent Return Request Details:");
        System.out.println("1. Customer Name: " + request.customerName);
        System.out.println("2. Customer Phone: " + request.customerPhone);
        System.out.println("3. Customer Email: " + request.customerEmail);
        System.out.println("4. Product Name: " + request.productName);
        System.out.println("5. Return Reason: " + request.returnReason);
        System.out.println("6. Damage Description: " + request.damageDescription);
        System.out.println("7. Status: " + request.status);
        
        System.out.println("\nWhat would you like to edit? (Enter 1-7, or 0 to cancel): ");
        int fieldChoice = getIntInput();
        
        if (fieldChoice == 0) {
            System.out.println("Edit cancelled.");
            return;
        }

        switch (fieldChoice) {
            case 1:
                System.out.print("Enter new Customer Name: ");
                request.customerName = scanner.nextLine();
                break;
            case 2:
                System.out.print("Enter new Customer Phone: ");
                request.customerPhone = scanner.nextLine();
                break;
            case 3:
                System.out.print("Enter new Customer Email: ");
                request.customerEmail = scanner.nextLine();
                break;
            case 4:
                System.out.print("Enter new Product Name: ");
                request.productName = scanner.nextLine();
                break;
            case 5:
                System.out.print("Enter new Return Reason: ");
                request.returnReason = scanner.nextLine();
                break;
            case 6:
                System.out.print("Enter new Damage Description: ");
                request.damageDescription = scanner.nextLine();
                break;
            case 7:
                System.out.print("Enter new Status: ");
                request.status = scanner.nextLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (returnService.editReturnRequest(requestId, request)) {
            System.out.println(" Return request updated successfully!");
        } else {
            System.out.println(" Failed to update return request.");
        }
    }

    private void viewAllUsers() {
        ArrayList<User> users = userService.getAllUsers();
        System.out.println("\n=== ALL USERS ===");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private void viewStatistics() {
        ArrayList<Product> products = productService.getAllProducts();
        ArrayList<ReturnRequest> returns = returnService.getAllReturnRequests();
        
        int validWarranties = 0;
        for (Product product : products) {
            if (product.isWarrantyValid()) {
                validWarranties++;
            }
        }
        
        int pendingReturns = 0;
        int completedReturns = 0;
        for (ReturnRequest request : returns) {
            if ("PENDING".equals(request.status)) {
                pendingReturns++;
            } else if ("COMPLETED".equals(request.status)) {
                completedReturns++;
            }
        }
        
        System.out.println("\n=== SYSTEM STATISTICS ===");
        System.out.println("Total Products: " + products.size());
        System.out.println("Products with Valid Warranty: " + validWarranties);
        System.out.println("Total Return Requests: " + returns.size());
        System.out.println("Pending Returns: " + pendingReturns);
        System.out.println("Completed Returns: " + completedReturns);
        System.out.println("Total Users: " + userService.getAllUsers().size());
    }

    private String getStatusFromChoice(int choice) {
        switch (choice) {
            case 1: return "PENDING";
            case 2: return "APPROVED";
            case 3: return "REJECTED";
            case 4: return "PICKUP_SCHEDULED";
            case 5: return "COMPLETED";
            default: return null;
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}