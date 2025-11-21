package LibrarySystem;
public class Book {
    private String title;
    private int copies;

    public Book(String title, int copies) {
        this.title = title;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public int getCopies() {
        return copies;
    }

    // These methods are now primarily for local object manipulation
    public void increaseCopies(int amount) {
        copies += amount;
    }

    public void decreaseCopies(int amount) {
        if (copies >= amount) copies -= amount;
    }

    @Override
    public String toString() {
        return title + " (" + copies + " copies)";
    }
}