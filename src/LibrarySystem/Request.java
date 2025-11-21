package LibrarySystem;

/**
 * Model class for handling student requests (Borrow or Return).
 */
public class Request {
    public enum Type {
        BORROW, RETURN
    }

    private final Type type;
    private final String studentUsername;
    private final String bookTitle;

    public Request(Type type, String studentUsername, String bookTitle) {
        this.type = type;
        this.studentUsername = studentUsername;
        this.bookTitle = bookTitle;
    }

    public Type getType() {
        return type;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    @Override
    public String toString() {
        String action = type == Type.BORROW ? "BORROW" : "RETURN";
        return String.format("[%s] Student '%s' for book '%s'",
                action, studentUsername, bookTitle);
    }
}