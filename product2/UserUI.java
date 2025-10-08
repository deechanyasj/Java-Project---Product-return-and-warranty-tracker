import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class UserUI {
    private Scanner scanner;
    private UserService userService;
    private ProductService productService;
    private ReturnService returnService;

    public UserUI() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.productService = new ProductService();
        this.returnService = new ReturnService();
    }

    public void showUserMainMenu() {
        while (true) {
            System.out.println("\n=== USER PORTAL ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    userLogin();
                    break;
                case 2:
                    userRegister();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void userLogin() {
        System.out.println("\n--- User Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userService.login(username, password) && !userService.getCurrentUser().isAdmin) {
            System.out.println("✅ Login successful! Welcome, " + username + "!");
            showUserDashboard();
        } else {
            System.out.println("❌ Invalid credentials or admin account.");
        }
    }

    private void userRegister() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        if (userService.register(username, password, email, phone, false)) {
            System.out.println("✅ Registration successful! You can now login.");
        } else {
            System.out.println("❌ Username already exists. Please choose a different username.");
        }
    }

    private void showUserDashboard() {
        while (true) {
            System.out.println("\n=== USER DASHBOARD ===");
            System.out.println("Welcome, " + userService.getCurrentUser().username + "!");
            System.out.println("1. Check Warranty by Invoice Number");
            System.out.println("2. Request Product Return");
            System.out.println("3. View Return Status");
            System.out.println("4. View My Return Requests");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    checkWarranty();
                    break;
                case 2:
                    requestReturn();
                    break;
                case 3:
                    viewReturnStatus();
                    break;
                case 4:
                    viewMyReturnRequests();
                    break;
                case 5:
                    userService.logout();
                    System.out.println("✅ Logged out successfully.");
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    private void checkWarranty() {
        System.out.println("\n--- Check Warranty ---");
        System.out.print("Enter Invoice Number: ");
        String invoiceNumber = scanner.nextLine();

        Product product = productService.getProductByInvoice(invoiceNumber);
        if (product != null) {
            System.out.println("\n=== WARRANTY DETAILS ===");
            System.out.println("📦 Product: " + product.productName);
            System.out.println("📋 Category: " + product.productCategory);
            System.out.println("📅 Purchase Date: " + product.purchaseDate);
            System.out.println("🛡️ Warranty End Date: " + product.warrantyEndDate);
            System.out.println("💰 Price: ₹" + String.format("%.2f", product.price));
            System.out.println("👤 Customer: " + product.customerName);
            System.out.println("📧 Email: " + product.customerEmail);
            System.out.println("📞 Phone: " + product.customerPhone);
            System.out.println("✅ Warranty Status: " + (product.isWarrantyValid() ? "VALID" : "EXPIRED"));
            System.out.println("⏳ Days Left: " + product.getWarrantyDaysLeft());
            
            if (!product.isWarrantyValid()) {
                System.out.println("⚠️ Warning: Warranty has expired. Return requests cannot be processed.");
            }
        } else {
            System.out.println("❌ No product found with invoice number: " + invoiceNumber);
        }
    }

    private void requestReturn() {
        System.out.println("\n--- Request Product Return ---");
        System.out.print("Enter Invoice Number: ");
        String invoiceNumber = scanner.nextLine();

        Product product = productService.getProductByInvoice(invoiceNumber);
        if (product == null) {
            System.out.println("❌ No product found with this invoice number.");
            return;
        }

        // Check warranty validity
        if (!product.isWarrantyValid()) {
            System.out.println("❌ Warranty has expired for this product. Return request cannot be processed.");
            return;
        }

        System.out.println("\n📦 Product Details:");
        System.out.println("Name: " + product.productName);
        System.out.println("Purchase Date: " + product.purchaseDate);
        System.out.println("Warranty End: " + product.warrantyEndDate);
        System.out.println("Price: ₹" + String.format("%.2f", product.price));

        // Return reasons
        System.out.println("\n📝 Select Return Reason:");
        System.out.println("1. Manufacturing Defect");
        System.out.println("2. Physical Damage");
        System.out.println("3. Not Working");
        System.out.println("4. Wrong Item Delivered");
        System.out.println("5. Received Damaged Product");
        System.out.println("6. Performance Issues");
        System.out.println("7. Other");
        System.out.print("Choose reason (1-7): ");
        
        int reasonChoice = getIntInput();
        String returnReason = getReturnReason(reasonChoice);
        
        if (returnReason == null) {
            System.out.println("❌ Invalid reason selection.");
            return;
        }

        System.out.print("🔧 Describe the issue/damage in detail: ");
        String damageDescription = scanner.nextLine();

        // Auto-fill customer details from product or ask user
        System.out.println("\n👤 Customer Details:");
        System.out.print("Your Name [" + product.customerName + "]: ");
        String customerName = scanner.nextLine();
        if (customerName.isEmpty()) customerName = product.customerName;
        
        System.out.print("Your Phone [" + product.customerPhone + "]: ");
        String customerPhone = scanner.nextLine();
        if (customerPhone.isEmpty()) customerPhone = product.customerPhone;
        
        System.out.print("Your Email [" + product.customerEmail + "]: ");
        String customerEmail = scanner.nextLine();
        if (customerEmail.isEmpty()) customerEmail = product.customerEmail;

        // Create return request
        ReturnRequest request = returnService.createReturnRequest(
                invoiceNumber, customerName, customerPhone, customerEmail,
                product.productName, returnReason, damageDescription);

        System.out.println("\n🎉 RETURN REQUEST CREATED SUCCESSFULLY!");
        System.out.println("📋 Request ID: " + request.requestId);
        System.out.println("📦 Product: " + request.productName);
        System.out.println("📝 Reason: " + request.returnReason);
        System.out.println("📊 Status: " + request.status);
        
        System.out.println("\n📋 IMPORTANT INSTRUCTIONS:");
        System.out.println("1. ✅ Keep the product in its original packaging");
        System.out.println("2. ✅ Include all accessories, manuals, and freebies");
        System.out.println("3. ✅ Do not remove any labels or tags");
        System.out.println("4. ✅ Our representative will contact you within 24 hours");
        System.out.println("5. ✅ Pickup will be scheduled within 2-3 business days");
        System.out.println("6. ✅ Have your invoice ready for verification");
        System.out.println("7. ✅ Keep the product in a safe place until pickup");
        
        System.out.println("\n📞 Contact Support: 1800-123-4567");
        System.out.println("📧 Email: support@warrantytracker.in");
        System.out.println("\n📱 You will receive SMS and email updates about your return status.");
    }

    private void viewReturnStatus() {
        System.out.println("\n--- View Return Status ---");
        System.out.print("Enter Return Request ID: ");
        String requestId = scanner.nextLine();

        ReturnRequest request = returnService.getReturnRequestById(requestId);
        if (request != null) {
            System.out.println("\n=== RETURN REQUEST DETAILS ===");
            System.out.println("📋 Request ID: " + request.requestId);
            System.out.println("📦 Product: " + request.productName);
            System.out.println("🧾 Invoice: " + request.invoiceNumber);
            System.out.println("👤 Customer: " + request.customerName);
            System.out.println("📞 Phone: " + request.customerPhone);
            System.out.println("📧 Email: " + request.customerEmail);
            System.out.println("📝 Reason: " + request.returnReason);
            System.out.println("🔧 Issue: " + request.damageDescription);
            System.out.println("📊 Status: " + getStatusWithEmoji(request.status));
            System.out.println("📅 Request Date: " + request.requestDate.toLocalDate());
            System.out.println("⏰ Request Time: " + request.requestDate.toLocalTime());
            
            if (request.scheduledPickupDate != null) {
                System.out.println("🚚 Scheduled Pickup: " + request.scheduledPickupDate);
            }
            if (request.pickupInstructions != null && !request.pickupInstructions.isEmpty()) {
                System.out.println("📋 Instructions: " + request.pickupInstructions);
            }
            
            // Show next steps based on status
            showNextSteps(request.status);
        } else {
            System.out.println("❌ No return request found with ID: " + requestId);
        }
    }

    private void viewMyReturnRequests() {
        System.out.println("\n--- My Return Requests ---");
        System.out.print("Enter your registered email: ");
        String email = scanner.nextLine();
        
        ArrayList<ReturnRequest> allReturns = returnService.getAllReturnRequests();
        ArrayList<ReturnRequest> myReturns = new ArrayList<>();
        
        for (ReturnRequest request : allReturns) {
            if (request.customerEmail.equalsIgnoreCase(email)) {
                myReturns.add(request);
            }
        }
        
        if (myReturns.isEmpty()) {
            System.out.println("❌ No return requests found for email: " + email);
        } else {
            System.out.println("\n=== MY RETURN REQUESTS ===");
            for (int i = 0; i < myReturns.size(); i++) {
                ReturnRequest request = myReturns.get(i);
                System.out.println((i + 1) + ". " + request.requestId + " | " + 
                                 request.productName + " | " + 
                                 getStatusWithEmoji(request.status) + " | " + 
                                 request.requestDate.toLocalDate());
            }
        }
    }

    private String getReturnReason(int choice) {
        switch (choice) {
            case 1: return "Manufacturing Defect";
            case 2: return "Physical Damage";
            case 3: return "Not Working";
            case 4: return "Wrong Item Delivered";
            case 5: return "Received Damaged Product";
            case 6: return "Performance Issues";
            case 7: return "Other";
            default: return null;
        }
    }

    private String getStatusWithEmoji(String status) {
        switch (status) {
            case "PENDING": return "⏳ " + status;
            case "APPROVED": return "✅ " + status;
            case "REJECTED": return "❌ " + status;
            case "PICKUP_SCHEDULED": return "🚚 " + status;
            case "COMPLETED": return "🎉 " + status;
            default: return status;
        }
    }

    private void showNextSteps(String status) {
        System.out.println("\n📋 NEXT STEPS:");
        switch (status) {
            case "PENDING":
                System.out.println("• Our team is reviewing your request");
                System.out.println("• You'll receive an update within 24 hours");
                System.out.println("• Keep the product ready for inspection");
                break;
            case "APPROVED":
                System.out.println("• Your return has been approved");
                System.out.println("• We'll schedule pickup shortly");
                System.out.println("• Keep product packed with all accessories");
                break;
            case "PICKUP_SCHEDULED":
                System.out.println("• Pickup has been scheduled");
                System.out.println("• Be available at the scheduled time");
                System.out.println("• Keep invoice and product ready");
                break;
            case "COMPLETED":
                System.out.println("• Return process completed successfully");
                System.out.println("• Refund/Replacement processed");
                System.out.println("• Thank you for your patience");
                break;
            case "REJECTED":
                System.out.println("• Contact customer support for details");
                System.out.println("• Call: 1800-123-4567");
                System.out.println("• Email: support@warrantytracker.in");
                break;
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