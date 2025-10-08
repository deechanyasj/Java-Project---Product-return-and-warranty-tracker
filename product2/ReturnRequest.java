import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReturnRequest implements Serializable {
    public String requestId;
    public String invoiceNumber;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    public String productName;
    public String returnReason;
    public String damageDescription;
    public LocalDateTime requestDate;
    public String status;
    public LocalDate scheduledPickupDate;
    public String pickupInstructions;

    public ReturnRequest(String requestId, String invoiceNumber, String customerName, 
                        String customerPhone, String customerEmail, String productName,
                        String returnReason, String damageDescription) {
        this.requestId = requestId;
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.returnReason = returnReason;
        this.damageDescription = damageDescription;
        this.requestDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %s | Product: %s | Customer: %s | Status: %s | Reason: %s | Date: %s",
            requestId, productName, customerName, status, returnReason, requestDate.toLocalDate()
        );
    }
}