package LibrarySystem;
import java.util.ArrayList;
import java.util.List;

public class Student1 extends User {
    public static final int BORROW_LIMIT = 3; 
    private double finesOwed;
    private List<String> borrowedBooks;

    public Student1(String username, String password) {
        super(username, password);
        this.finesOwed = 0.0;
        this.borrowedBooks = new ArrayList<>();
    }

    public double getFinesOwed() {
        return finesOwed;
    }
    
    public List<String> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void addFine(double amount) {
        finesOwed += amount;
    }
    
    // Modified to return payment info instead of printing
    public String payFine(double amount) {
        double paidAmount = amount;
        if (amount > finesOwed) {
            paidAmount = finesOwed; 
        }
        finesOwed -= paidAmount;
        return String.format("Paid Ksh %.2f. Remaining fine: Ksh %.2f.", paidAmount, finesOwed);
    }

    // These methods are now handled by DatabaseConnection
    public String submitBorrowRequest(LibrarySystem system, String title) {
        // This method is now handled by DatabaseConnection
        return "Method not supported. Use LibrarySystem methods instead.";
    }

    public String submitReturnRequest(LibrarySystem system, String title) {
        // This method is now handled by DatabaseConnection
        return "Method not supported. Use LibrarySystem methods instead.";
    }

    @Override
    public void showMenu(LibrarySystem system) { 
        // Menu is now handled by GUI
        System.out.println("Menu handled by GUI interface.");
    }
}