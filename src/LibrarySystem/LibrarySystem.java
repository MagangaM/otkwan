package LibrarySystem;

import java.util.*;
import java.sql.*;

public class LibrarySystem {
    private DatabaseConnection database;
    private List<Student1> students;
    private List<Admin1> admins;
    private List<Book> books;
    private List<Request> pendingRequests;

    public LibrarySystem() {
        this.database = new DatabaseConnection();
        initializeFromDatabase();
    }

    private void initializeFromDatabase() {
        // Initialize empty lists - data will be fetched from database as needed
        this.students = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.books = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
        
        // Load initial data from database
        refreshBooks();
        refreshAdmins();
    }

    // Authentication methods
    public Student1 authenticateStudent(String username, String password) {
        return database.authenticateStudent(username, password);
    }

    public Admin1 authenticateAdmin(String username, String password) {
        return database.authenticateAdmin(username, password);
    }

    // User management
    public boolean userExists(String username) {
        return database.userExists(username);
    }

    public boolean registerStudent(String username, String password) {
        return database.registerUser(username, password, "STUDENT");
    }

    public String registerAdmin(String username, String password) {
        if (database.registerUser(username, password, "ADMIN")) {
            return "Admin account '" + username + "' created successfully!";
        } else {
            return "Failed to create admin account.";
        }
    }

    public String changeAdminPassword(String username, String newPassword) {
        if (database.changePassword(username, newPassword)) {
            return "Password changed successfully!";
        } else {
            return "Failed to change password.";
        }
    }

    // Book management
    public String addBook(String title, int copies) {
        return database.addBook(title, copies);
    }

    public String deleteBook(String title) {
        return database.deleteBook(title);
    }

    public List<Book> getAllBooks() {
        return database.getAllBooks();
    }

    public Book findBook(String title) {
        return database.findBook(title);
    }

    private void refreshBooks() {
        this.books = database.getAllBooks();
    }

    // Student operations
    public String submitBorrowRequest(String username, String bookTitle) {
        return database.submitBorrowRequest(username, bookTitle);
    }

    public String returnBook(String username, String bookTitle, int daysOverdue) {
        return database.returnBook(username, bookTitle, daysOverdue);
    }

    public String payFine(String username, double amount) {
        return database.payFine(username, amount);
    }

    public List<String> getBorrowedBooks(String username) {
        return database.getBorrowedBooks(username);
    }

    public int getBorrowedBooksCount(String username) {
        return database.getBorrowedBooksCount(username);
    }

    public double getStudentFines(String username) {
        return database.getStudentFines(username);
    }

    // Admin operations
    public String fineStudent(String username, double amount) {
        return database.fineStudent(username, amount);
    }

    public List<Student1> getAllStudents() {
        return database.getAllStudents();
    }

    public List<Request> getPendingRequests() {
        return database.getPendingRequests();
    }

    public String processRequest(Request request, boolean approve) {
        return database.processRequest(request, approve);
    }

    private void refreshAdmins() {
        this.admins = database.getAllAdmins();
    }

    // Utility methods for backward compatibility
    public Student1 findStudent(String username) {
        return database.findStudent(username);
    }

    public Admin1 findAdmin(String username) {
        return database.findAdmin(username);
    }

    public void closeDatabaseConnection() {
        database.closeConnection();
    }

    // Main method for console version (optional)
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.runConsoleVersion();
    }

    private void runConsoleVersion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Welcome to the Library System ===");

        while (true) {
            System.out.print("\nAre you a 'student', 'admin', 'signup' to register, or 'exit'? ");
            String type = scanner.nextLine().trim().toLowerCase();

            if (type.equals("exit")) {
                System.out.println("Goodbye!");
                closeDatabaseConnection();
                break;
            } else if (type.equals("signup")) {
                studentSignupConsole(scanner);
            } else if (type.equals("student") || type.equals("admin")) {
                loginConsole(scanner, type);
            } else {
                System.out.println("Invalid option. Please choose 'student', 'admin', 'signup', or 'exit'.");
            }
        }
        scanner.close();
    }

    private void studentSignupConsole(Scanner scanner) {
        System.out.println("\n--- Student Signup ---");
        String username;
        String password;

        while (true) {
            System.out.print("Enter new username: ");
            username = scanner.nextLine().trim();
            if (!userExists(username) && !username.isEmpty()) {
                break;
            }
            System.out.println("Username is already taken or invalid. Please try another one.");
        }

        System.out.print("Enter new password: ");
        password = scanner.nextLine();

        if (password.trim().isEmpty()) {
            System.out.println("Password cannot be empty. Signup failed.");
            return;
        }

        if (registerStudent(username, password)) {
            System.out.println("Student account for '" + username + "' created successfully! You can now log in.");
        } else {
            System.out.println("Failed to create student account.");
        }
    }

    private void loginConsole(Scanner scanner, String type) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (type.equals("student")) {
            Student1 s = authenticateStudent(username, password);
            if (s != null) {
                System.out.println("Login successful!");
                // studentMenuConsole(scanner, s); // You can implement this if needed
            } else {
                System.out.println("Invalid username or password.");
            }
        } else {
            Admin1 a = authenticateAdmin(username, password);
            if (a != null) {
                System.out.println("Login successful!");
                // adminMenuConsole(scanner, a); // You can implement this if needed
            } else {
                System.out.println("Invalid username or password.");
            }
        }
    }
}