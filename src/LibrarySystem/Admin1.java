package LibrarySystem;
import java.util.List;

public class Admin1 extends User {

    public Admin1(String username, String password) {
        super(username, password);
    }

    // These methods are now handled by DatabaseConnection
    public String addBook(LibrarySystem system, String title, int copies) {
        // This method is now handled by DatabaseConnection
        return "Method not supported. Use LibrarySystem methods instead.";
    }

    public String deleteBook(LibrarySystem system, String title) {
        // This method is now handled by DatabaseConnection
        return "Method not supported. Use LibrarySystem methods instead.";
    }

    public String fineStudent(LibrarySystem system, String name, double fine) {
        // This method is now handled by DatabaseConnection
        return "Method not supported. Use LibrarySystem methods instead.";
    }

    @Override
    public void showMenu(LibrarySystem system) { 
        // Menu is now handled by GUI
        System.out.println("Menu handled by GUI interface.");
    }
}