import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("       PRODUCT RETURN & WARRANTY TRACKER     ");
        System.out.println("               Made in India 🇮🇳                ");
        System.out.println("================================================");
        System.out.println("            All prices in INR                ");
        System.out.println("         Support: 1800-123-4567              ");
        System.out.println("================================================");
        
        showMainMenu();
    }

    public static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1.  Admin Portal");
            System.out.println("2.  User Portal");
            System.out.println("3.  About System");
            System.out.println("4.  Exit");
            System.out.print("Choose option: ");

            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    AdminUI adminUI = new AdminUI();
                    adminUI.showAdminMainMenu();
                    break;
                case 2:
                    UserUI userUI = new UserUI();
                    userUI.showUserMainMenu();
                    break;
                case 3:
                    showAbout();
                    break;
                case 4:
                    System.out.println("\n Thank you for using our system!");
                    System.out.println(" Customer Care: 1800-123-4567");
                    System.out.println(" Email: support@warrantytracker.in");
                    System.out.println(" Goodbye! Visit again!");
                    return;
                default:
                    System.out.println(" Invalid option. Please try again.");
            }
        }
    }

    private static void showAbout() {
        System.out.println("\n=== ABOUT WARRANTY TRACKER ===");
        System.out.println(" Comprehensive Product Return & Warranty Management System");
        System.out.println("\n FEATURES:");
        System.out.println("•  Warranty Status Checking");
        System.out.println("•  Product Return Requests");
        System.out.println("•  Real-time Status Tracking");
        System.out.println("•  Admin Management Panel");
        System.out.println("•  Data Persistence");
        System.out.println("•  Customer Support");
        
        System.out.println("\n SUPPORTED CURRENCY: Indian Rupee (INR)");
        
        System.out.println("\n DEFAULT LOGINS:");
        System.out.println(" Admin: username='admin', password='admin123'");
        System.out.println(" User: username='customer', password='customer123'");
        
        System.out.println("\n SAMPLE PRODUCTS:");
        System.out.println("• INV001 - Samsung TV 55\" - ₹45,999");
        System.out.println("• INV002 - iPhone 15 Pro - ₹1,29,999");
        System.out.println("• INV003 - Dell Laptop XPS - ₹89,999");
        System.out.println("• INV004 - LG Refrigerator - ₹34,999");
        System.out.println("• INV005 - Sony Headphones - ₹8,999");
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}