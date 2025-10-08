import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable {
    public String invoiceNumber;
    public String productName;
    public String productCategory;
    public LocalDate purchaseDate;
    public LocalDate warrantyEndDate;
    public double price;
    public String customerName;
    public String customerEmail;
    public String customerPhone;

    public Product(String invoiceNumber, String productName, String productCategory, 
                  LocalDate purchaseDate, LocalDate warrantyEndDate, double price,
                  String customerName, String customerEmail, String customerPhone) {
        this.invoiceNumber = invoiceNumber;
        this.productName = productName;
        this.productCategory = productCategory;
        this.purchaseDate = purchaseDate;
        this.warrantyEndDate = warrantyEndDate;
        this.price = price;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }

    public boolean isWarrantyValid() {
        return LocalDate.now().isBefore(warrantyEndDate) || LocalDate.now().equals(warrantyEndDate);
    }

    public long getWarrantyDaysLeft() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), warrantyEndDate);
    }

    @Override
    public String toString() {
        return String.format(
            "Invoice: %s | Product: %s | Category: %s | Price: ₹%.2f | Customer: %s | Warranty: %s (%d days left)",
            invoiceNumber, productName, productCategory, price, customerName,
            isWarrantyValid() ? "VALID" : "EXPIRED", getWarrantyDaysLeft()
        );
    }
}