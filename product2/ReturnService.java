import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class ReturnService {
    private ArrayList<ReturnRequest> returnRequests = new ArrayList<>();
    private int requestCounter = 1;
    private static final String RETURN_FILE = "returns.dat";

    public ReturnService() {
        loadReturns();
    }

    public ReturnRequest createReturnRequest(String invoiceNumber, String customerName,
                                           String customerPhone, String customerEmail,
                                           String productName, String returnReason,
                                           String damageDescription) {
        String requestId = "RET" + requestCounter++;
        
        ReturnRequest request = new ReturnRequest(requestId, invoiceNumber, customerName,
                customerPhone, customerEmail, productName, returnReason, damageDescription);
        
        returnRequests.add(request);
        saveReturns();
        return request;
    }

    public ArrayList<ReturnRequest> getAllReturnRequests() {
        return new ArrayList<>(returnRequests);
    }

    public ArrayList<ReturnRequest> getPendingReturns() {
        ArrayList<ReturnRequest> pending = new ArrayList<>();
        for (ReturnRequest request : returnRequests) {
            if ("PENDING".equals(request.status)) {
                pending.add(request);
            }
        }
        return pending;
    }

    public ReturnRequest getReturnRequestById(String requestId) {
        for (ReturnRequest request : returnRequests) {
            if (request.requestId.equals(requestId)) {
                return request;
            }
        }
        return null;
    }

    public boolean updateReturnStatus(String requestId, String status, LocalDate pickupDate, String instructions) {
        ReturnRequest request = getReturnRequestById(requestId);
        if (request != null) {
            request.status = status;
            request.scheduledPickupDate = pickupDate;
            request.pickupInstructions = instructions;
            saveReturns();
            return true;
        }
        return false;
    }

    // NEW: Edit any field of return request
    public boolean editReturnRequest(String requestId, ReturnRequest updatedRequest) {
        for (int i = 0; i < returnRequests.size(); i++) {
            if (returnRequests.get(i).requestId.equals(requestId)) {
                returnRequests.set(i, updatedRequest);
                saveReturns();
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void loadReturns() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RETURN_FILE))) {
            returnRequests = (ArrayList<ReturnRequest>) ois.readObject();
            // Set request counter based on existing requests
            if (!returnRequests.isEmpty()) {
                String lastId = returnRequests.get(returnRequests.size() - 1).requestId;
                requestCounter = Integer.parseInt(lastId.substring(3)) + 1;
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing return data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading returns: " + e.getMessage());
        }
    }

    private void saveReturns() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RETURN_FILE))) {
            oos.writeObject(returnRequests);
        } catch (IOException e) {
            System.out.println("Error saving returns: " + e.getMessage());
        }
    }
}