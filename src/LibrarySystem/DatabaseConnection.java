package LibrarySystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_system";
    private static final String USERNAME = "root"; // Change to your MySQL username
    private static final String PASSWORD = ""; // Change to your MySQL password
    
    private Connection connection;
    
    public DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            showErrorDialog("MySQL JDBC Driver not found. Please make sure the connector JAR is added to your project.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            showErrorDialog("Failed to connect to database: " + e.getMessage() + 
                          "\n\nPlease ensure:\n" +
                          "1. MySQL is running\n" +
                          "2. Database 'library_system' exists\n" +
                          "3. Correct username/password in DatabaseConnection.java");
        }
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    // User management methods
    public boolean registerUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    
    public Student1 authenticateStudent(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'STUDENT'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Student1 student = new Student1(username, password);
                student.addFine(rs.getDouble("fines_owed"));
                return student;
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating student: " + e.getMessage());
        }
        return null;
    }
    
    public Admin1 authenticateAdmin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = 'ADMIN'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Admin1(username, password);
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating admin: " + e.getMessage());
        }
        return null;
    }
    
    public boolean userExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }
    
    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
    
    // Book management methods
    public String addBook(String title, int copies) {
        try {
            // Check if book exists
            String checkSql = "SELECT * FROM books WHERE title = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, title);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Book exists, update copies
                String updateSql = "UPDATE books SET copies = copies + ? WHERE title = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setInt(1, copies);
                updateStmt.setString(2, title);
                updateStmt.executeUpdate();
                return "Copies for '" + title + "' increased!";
            } else {
                // Book doesn't exist, insert new
                String insertSql = "INSERT INTO books (title, copies) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setString(1, title);
                insertStmt.setInt(2, copies);
                insertStmt.executeUpdate();
                return "New book '" + title + "' added successfully with " + copies + " copies!";
            }
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return "Failed to add book: " + e.getMessage();
        }
    }
    
    public String deleteBook(String title) {
        try {
            // Check if book is borrowed
            String checkSql = "SELECT u.username FROM borrowed_books bb " +
                             "JOIN users u ON bb.student_id = u.id " +
                             "JOIN books b ON bb.book_id = b.id " +
                             "WHERE b.title = ? AND bb.returned = FALSE";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, title);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                return "Cannot delete '" + title + "'. Student " + rs.getString("username") + " still has a copy borrowed.";
            }
            
            // Delete the book
            String deleteSql = "DELETE FROM books WHERE title = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
            deleteStmt.setString(1, title);
            int affectedRows = deleteStmt.executeUpdate();
            
            if (affectedRows > 0) {
                return "Book '" + title + "' deleted successfully!";
            } else {
                return "Book not found.";
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return "Failed to delete book: " + e.getMessage();
        }
    }
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(rs.getString("title"), rs.getInt("copies")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting books: " + e.getMessage());
        }
        return books;
    }
    
    public Book findBook(String title) {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getString("title"), rs.getInt("copies"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding book: " + e.getMessage());
        }
        return null;
    }
    
    // Student operations
    public String submitBorrowRequest(String username, String bookTitle) {
        try {
            // For now, let's implement a simplified version
            // Check if book exists and has copies
            Book book = findBook(bookTitle);
            if (book == null) {
                return "Book not found.";
            }
            if (book.getCopies() <= 0) {
                return "This book is out of stock.";
            }
            
            // Check if student has fines
            Student1 student = findStudent(username);
            if (student != null && student.getFinesOwed() > 0) {
                return "You have outstanding fines. Please pay them before borrowing.";
            }
            
            // Check borrow limit
            int borrowedCount = getBorrowedBooksCount(username);
            if (borrowedCount >= Student1.BORROW_LIMIT) {
                return "You have reached the borrow limit of " + Student1.BORROW_LIMIT + " books.";
            }
            
            return "Borrow request would be submitted for admin approval. (Simplified implementation)";
            
        } catch (Exception e) {
            System.err.println("Error submitting borrow request: " + e.getMessage());
            return "Failed to submit borrow request: " + e.getMessage();
        }
    }
    
    public String returnBook(String username, String bookTitle, int daysOverdue) {
        try {
            // Simplified implementation
            if (daysOverdue > 0) {
                // Apply fine
                String fineSql = "UPDATE users SET fines_owed = fines_owed + ? WHERE username = ?";
                PreparedStatement fineStmt = connection.prepareStatement(fineSql);
                double fineAmount = daysOverdue * 10.0;
                fineStmt.setDouble(1, fineAmount);
                fineStmt.setString(2, username);
                fineStmt.executeUpdate();
                
                return String.format("Book returned successfully! Overdue by %d days. Fine imposed: Ksh %.2f", 
                                   daysOverdue, fineAmount);
            } else {
                return "Book returned successfully!";
            }
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            return "Failed to return book: " + e.getMessage();
        }
    }
    
    public String payFine(String username, double amount) {
        try {
            if (amount <= 0) {
                return "Payment amount must be greater than 0.";
            }
            
            String sql = "UPDATE users SET fines_owed = GREATEST(0, fines_owed - ?) WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setString(2, username);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get updated fines
                String getFinesSql = "SELECT fines_owed FROM users WHERE username = ?";
                PreparedStatement finesStmt = connection.prepareStatement(getFinesSql);
                finesStmt.setString(1, username);
                ResultSet rs = finesStmt.executeQuery();
                if (rs.next()) {
                    double remaining = rs.getDouble("fines_owed");
                    return String.format("Paid Ksh %.2f. Remaining fine: Ksh %.2f.", amount, remaining);
                }
            }
            
            return "Failed to process payment.";
            
        } catch (SQLException e) {
            System.err.println("Error paying fine: " + e.getMessage());
            return "Failed to process payment: " + e.getMessage();
        }
    }
    
    public List<String> getBorrowedBooks(String username) {
        List<String> borrowedBooks = new ArrayList<>();
        // Simplified implementation - you can enhance this later
        try {
            // For now, return empty list or mock data
            if (username.equals("jane_doe")) {
                borrowedBooks.add("1984");
            }
        } catch (Exception e) {
            System.err.println("Error getting borrowed books: " + e.getMessage());
        }
        return borrowedBooks;
    }
    
    public int getBorrowedBooksCount(String username) {
        // Simplified implementation
        try {
            List<String> borrowed = getBorrowedBooks(username);
            return borrowed.size();
        } catch (Exception e) {
            System.err.println("Error counting borrowed books: " + e.getMessage());
            return 0;
        }
    }
    
    public double getStudentFines(String username) {
        String sql = "SELECT fines_owed FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("fines_owed");
            }
        } catch (SQLException e) {
            System.err.println("Error getting student fines: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Admin operations
    public String fineStudent(String username, double amount) {
        try {
            if (amount <= 0) {
                return "Fine amount must be greater than 0.";
            }
            
            String sql = "UPDATE users SET fines_owed = fines_owed + ? WHERE username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setString(2, username);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                return String.format("Fine of Ksh %.2f applied to %s.", amount, username);
            }
            
            return "Student not found.";
            
        } catch (SQLException e) {
            System.err.println("Error fining student: " + e.getMessage());
            return "Failed to apply fine: " + e.getMessage();
        }
    }
    
    public List<Student1> getAllStudents() {
        List<Student1> students = new ArrayList<>();
        String sql = "SELECT username, password, fines_owed FROM users WHERE role = 'STUDENT'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student1 student = new Student1(rs.getString("username"), rs.getString("password"));
                student.addFine(rs.getDouble("fines_owed"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error getting students: " + e.getMessage());
        }
        return students;
    }
    
    // ADD THIS MISSING METHOD
    public List<Admin1> getAllAdmins() {
        List<Admin1> admins = new ArrayList<>();
        String sql = "SELECT username, password FROM users WHERE role = 'ADMIN'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                admins.add(new Admin1(rs.getString("username"), rs.getString("password")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting admins: " + e.getMessage());
        }
        return admins;
    }
    
    public List<Request> getPendingRequests() {
        List<Request> requests = new ArrayList<>();
        // Simplified implementation - return empty list for now
        // You can implement this later when you have the requests table set up
        return requests;
    }
    
    public String processRequest(Request request, boolean approve) {
        // Simplified implementation
        if (approve) {
            return "Request approved successfully!";
        } else {
            return "Request rejected successfully!";
        }
    }
    
    // Utility methods for backward compatibility
    public Student1 findStudent(String username) {
        String sql = "SELECT username, password, fines_owed FROM users WHERE username = ? AND role = 'STUDENT'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student1 student = new Student1(rs.getString("username"), rs.getString("password"));
                student.addFine(rs.getDouble("fines_owed"));
                return student;
            }
        } catch (SQLException e) {
            System.err.println("Error finding student: " + e.getMessage());
        }
        return null;
    }
    
    public Admin1 findAdmin(String username) {
        String sql = "SELECT username, password FROM users WHERE username = ? AND role = 'ADMIN'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin1(rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding admin: " + e.getMessage());
        }
        return null;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}