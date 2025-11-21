package LibrarySystem;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibrarySystemGUI extends JFrame {
    private LibrarySystem librarySystem;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Student1 currentStudent;
    private Admin1 currentAdmin;

    private final Color BACKGROUND_DARK = new Color(18, 18, 24);
    private final Color CARD_DARK = new Color(30, 30, 40);
    private final Color ACCENT_PRIMARY = new Color(103, 58, 183);
    private final Color ACCENT_SECONDARY = new Color(0, 150, 136);
    private final Color ACCENT_SUCCESS = new Color(76, 175, 80);
    private final Color ACCENT_WARNING = new Color(255, 152, 0);
    private final Color ACCENT_ERROR = new Color(244, 67, 54);
    private final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color BORDER_COLOR = new Color(60, 60, 80);

    private class ButtonData {
        String text;
        Color color;
        ButtonData(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }

    public LibrarySystemGUI() {
        librarySystem = new LibrarySystem();
        setupFrame();
        applyDarkMode();
    }

    private void setupFrame() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BACKGROUND_DARK);

        cardPanel.add(createWelcomePanel(), "Welcome");
        cardPanel.add(createLoginPanel(), "Login");
        cardPanel.add(createSignupPanel(), "Signup");
        cardPanel.add(createStudentMenuPanel(), "StudentMenu");
        cardPanel.add(createAdminMenuPanel(), "AdminMenu");

        add(cardPanel);
        setVisible(true);
    }

    private void applyDarkMode() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            UIManager.put("Panel.background", BACKGROUND_DARK);
            UIManager.put("Button.background", ACCENT_PRIMARY);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.focus", new Color(0, 0, 0, 0));
            UIManager.put("TextField.background", CARD_DARK);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", ACCENT_PRIMARY);
            UIManager.put("TextField.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("PasswordField.background", CARD_DARK);
            UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
            UIManager.put("PasswordField.caretForeground", ACCENT_PRIMARY);
            UIManager.put("PasswordField.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("ComboBox.background", CARD_DARK);
            UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
            UIManager.put("ComboBox.border", BorderFactory.createLineBorder(BORDER_COLOR));
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            UIManager.put("List.background", CARD_DARK);
            UIManager.put("List.foreground", TEXT_PRIMARY);
            UIManager.put("ScrollPane.background", BACKGROUND_DARK);
            UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(BORDER_COLOR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel titleLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Your Gateway to Knowledge and Learning");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        gbc.gridy = 1;
        panel.add(subtitleLabel, gbc);

        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(40), gbc);

        JButton studentButton = createModernButton("Student Login", ACCENT_PRIMARY);
        studentButton.addActionListener(e -> {
            currentStudent = null;
            cardLayout.show(cardPanel, "Login");
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(studentButton, gbc);

        JButton adminButton = createModernButton("Admin Login", ACCENT_SECONDARY);
        adminButton.addActionListener(e -> {
            currentAdmin = null;
            cardLayout.show(cardPanel, "Login");
        });
        gbc.gridx = 1;
        panel.add(adminButton, gbc);

        JButton signupButton = createModernButton("Student Sign Up", ACCENT_SUCCESS);
        signupButton.addActionListener(e -> cardLayout.show(cardPanel, "Signup"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(signupButton, gbc);

        JButton addAdminButton = createModernButton("Add Admin", new Color(156, 39, 176));
        addAdminButton.addActionListener(e -> showAddAdminDialog());
        gbc.gridx = 1;
        panel.add(addAdminButton, gbc);

        JButton exitButton = createModernButton("Exit System", ACCENT_ERROR);
        exitButton.addActionListener(e -> {
            librarySystem.closeDatabaseConnection();
            System.exit(0);
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(exitButton, gbc);

        return panel;
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(0, 0, color, 0, getHeight(), color.darker());
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(color.brighter());
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(240, 240, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel userLabel = createStyledLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        JTextField usernameField = createStyledTextField();
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        JLabel passLabel = createStyledLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passwordField = createStyledPasswordField();
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JLabel typeLabel = createStyledLabel("Account Type:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(typeLabel, gbc);

        String[] types = {"Student", "Admin"};
        JComboBox<String> typeCombo = createStyledComboBox(types);
        gbc.gridx = 1;
        panel.add(typeCombo, gbc);

        JButton loginButton = createModernButton("Login", ACCENT_PRIMARY);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String type = (String) typeCombo.getSelectedItem();

            if (type.equals("Student")) {
                Student1 s = librarySystem.authenticateStudent(username, password);
                if (s != null) {
                    currentStudent = s;
                    cardLayout.show(cardPanel, "StudentMenu");
                    usernameField.setText("");
                    passwordField.setText("");
                } else {
                    showErrorDialog("Invalid username or password.", "Login Failed");
                }
            } else {
                Admin1 a = librarySystem.authenticateAdmin(username, password);
                if (a != null) {
                    currentAdmin = a;
                    cardLayout.show(cardPanel, "AdminMenu");
                    usernameField.setText("");
                    passwordField.setText("");
                } else {
                    showErrorDialog("Invalid username or password.", "Login Failed");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(loginButton, gbc);

        JButton backButton = createModernButton("Back", TEXT_SECONDARY);
        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(cardPanel, "Welcome");
        });
        gbc.gridx = 1;
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createSignupPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Student Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel userLabel = createStyledLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        JTextField usernameField = createStyledTextField();
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        JLabel passLabel = createStyledLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passwordField = createStyledPasswordField();
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton signupButton = createModernButton("Create Account", ACCENT_SUCCESS);
        signupButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showErrorDialog("Username and password cannot be empty.", "Invalid Input");
                return;
            }

            if (librarySystem.userExists(username)) {
                showErrorDialog("Username is already taken.", "Sign Up Failed");
                return;
            }

            if (librarySystem.registerStudent(username, password)) {
                showSuccessDialog("Account created successfully! You can now log in.", "Registration Complete");
                usernameField.setText("");
                passwordField.setText("");
                cardLayout.show(cardPanel, "Welcome");
            } else {
                showErrorDialog("Failed to create account. Please try again.", "Sign Up Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(signupButton, gbc);

        JButton backButton = createModernButton("Back", TEXT_SECONDARY);
        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(cardPanel, "Welcome");
        });
        gbc.gridx = 1;
        panel.add(backButton, gbc);

        return panel;
    }

    private JPanel createStudentMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Student Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        JLabel userLabel = new JLabel("Welcome, " + (currentStudent != null ? currentStudent.getUsername() : "Student"));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_SECONDARY);
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        ButtonData[] studentButtons = {
            new ButtonData("View Books", ACCENT_PRIMARY),
            new ButtonData("Request Book", ACCENT_SECONDARY),
            new ButtonData("My Books", ACCENT_WARNING),
            new ButtonData("Return Book", new Color(156, 39, 176)),
            new ButtonData("Check Fines", ACCENT_ERROR),
            new ButtonData("Pay Fine", new Color(0, 188, 212)),
            new ButtonData("Logout", TEXT_SECONDARY)
        };

        for (int i = 0; i < studentButtons.length; i++) {
            JButton button = createModernButton(studentButtons[i].text, studentButtons[i].color);
            final int index = i;
            button.addActionListener(e -> handleStudentMenuAction(index));
            gbc.gridx = 0;
            gbc.gridy = i + 2;
            gbc.gridwidth = 2;
            panel.add(button, gbc);
        }

        return panel;
    }

    private void handleStudentMenuAction(int index) {
        switch (index) {
            case 0: showAvailableBooksDialog(); break;
            case 1: showRequestBookDialog(); break;
            case 2: showBorrowedBooksDialog(); break;
            case 3: showReturnBookDialog(); break;
            case 4: showFinesDialog(); break;
            case 5: showPayFineDialog(); break;
            case 6: 
                currentStudent = null;
                cardLayout.show(cardPanel, "Welcome");
                break;
        }
    }

    private JPanel createAdminMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcomeLabel = new JLabel("Admin Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        JLabel userLabel = new JLabel("Welcome, " + (currentAdmin != null ? currentAdmin.getUsername() : "Admin"));
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_SECONDARY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(userLabel, gbc);

        ButtonData[] adminButtons = {
            new ButtonData("Manage Requests", ACCENT_PRIMARY),
            new ButtonData("Add Book", ACCENT_SUCCESS),
            new ButtonData("Delete Book", ACCENT_ERROR),
            new ButtonData("View Books", ACCENT_WARNING),
            new ButtonData("View Students", new Color(156, 39, 176)),
            new ButtonData("Fine Student", new Color(255, 87, 34)),
            new ButtonData("Change Password", new Color(0, 188, 212)),
            new ButtonData("Add New Admin", new Color(233, 30, 99)),
            new ButtonData("Logout", TEXT_SECONDARY)
        };

        for (int i = 0; i < adminButtons.length; i++) {
            JButton button = createModernButton(adminButtons[i].text, adminButtons[i].color);
            final int index = i;
            button.addActionListener(e -> handleAdminMenuAction(index));
            gbc.gridx = 0;
            gbc.gridy = i + 2;
            gbc.gridwidth = 2;
            panel.add(button, gbc);
        }

        return panel;
    }

    private void handleAdminMenuAction(int index) {
        switch (index) {
            case 0: showManageRequestsDialog(); break;
            case 1: showAddBookDialog(); break;
            case 2: showDeleteBookDialog(); break;
            case 3: showManageBooksDialog(); break;
            case 4: showStudentsDialog(); break;
            case 5: showFineStudentDialog(); break;
            case 6: showChangePasswordDialog(); break;
            case 7: showAddAdminDialog(); break;
            case 8: 
                currentAdmin = null;
                cardLayout.show(cardPanel, "Welcome");
                break;
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(CARD_DARK);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(CARD_DARK);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(CARD_DARK);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(ACCENT_PRIMARY);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(CARD_DARK);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });
        return combo;
    }

    private void showErrorDialog(String message, String title) {
        showStyledMessageDialog(message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message, String title) {
        showStyledMessageDialog(message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStyledMessageDialog(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", BACKGROUND_DARK);
        UIManager.put("Panel.background", BACKGROUND_DARK);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        
        JTextArea textArea = new JTextArea(message);
        textArea.setBackground(CARD_DARK);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.setBackground(BACKGROUND_DARK);
        
        JOptionPane.showMessageDialog(this, scrollPane, title, messageType);
        
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
    }

    private void showAvailableBooksDialog() {
        List<Book> books = librarySystem.getAllBooks();
        StringBuilder bookList = new StringBuilder("Available Books:\n\n");
        if (books.isEmpty()) {
            bookList.append("No books available in the library.");
        } else {
            for (Book book : books) {
                bookList.append("• ").append(book.getTitle()).append(" - Copies: ").append(book.getCopies()).append("\n");
            }
        }
        showStyledMessageDialog(bookList.toString(), "Available Books", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBorrowedBooksDialog() {
        if (currentStudent == null) return;
        
        List<String> borrowedBooks = librarySystem.getBorrowedBooks(currentStudent.getUsername());
        StringBuilder borrowedList = new StringBuilder("Your Borrowed Books:\n\n");
        if (borrowedBooks.isEmpty()) {
            borrowedList.append("You have no borrowed books.");
        } else {
            for (String book : borrowedBooks) {
                borrowedList.append("• ").append(book).append("\n");
            }
        }
        showStyledMessageDialog(borrowedList.toString(), "Borrowed Books", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showFinesDialog() {
        if (currentStudent == null) return;
        
        double fines = librarySystem.getStudentFines(currentStudent.getUsername());
        String message = String.format("Your Fines: Ksh %.2f", fines);
        showStyledMessageDialog(message, "Fines Overview", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRequestBookDialog() {
        if (currentStudent == null) return;

        if (currentStudent.getFinesOwed() > 0) {
            showErrorDialog("You have outstanding fines. Please pay them before borrowing.", "Cannot Borrow");
            return;
        }

        if (librarySystem.getBorrowedBooksCount(currentStudent.getUsername()) >= Student1.BORROW_LIMIT) {
            showErrorDialog("You have reached the borrow limit of " + Student1.BORROW_LIMIT + " books.", "Borrow Limit Reached");
            return;
        }

        JDialog dialog = createStyledDialog("Request Book", 400, 200);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = createStyledLabel("Enter book title:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(label, gbc);

        JTextField bookField = createStyledTextField();
        gbc.gridy = 1;
        contentPanel.add(bookField, gbc);

        JButton requestButton = createModernButton("Submit Request", ACCENT_PRIMARY);
        requestButton.addActionListener(e -> {
            String title = bookField.getText().trim();
            if (title.isEmpty()) {
                showErrorDialog("Book title cannot be empty.", "Invalid Input");
                return;
            }

            String result = librarySystem.submitBorrowRequest(currentStudent.getUsername(), title);
            if (result.contains("successfully") || result.contains("submitted")) {
                showSuccessDialog(result, "Request Submitted");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Request Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(requestButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showReturnBookDialog() {
        if (currentStudent == null) return;
        
        List<String> borrowedBooks = librarySystem.getBorrowedBooks(currentStudent.getUsername());
        if (borrowedBooks.isEmpty()) {
            showStyledMessageDialog("You have no books to return.", "No Books", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = createStyledDialog("Return Book", 400, 250);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = createStyledLabel("Select book to return:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(label, gbc);

        JComboBox<String> bookCombo = createStyledComboBox(borrowedBooks.toArray(new String[0]));
        gbc.gridy = 1;
        contentPanel.add(bookCombo, gbc);

        JLabel daysLabel = createStyledLabel("Days overdue:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(daysLabel, gbc);

        JSpinner daysSpinner = createStyledSpinner(0, 0, 365, 1);
        gbc.gridx = 1;
        contentPanel.add(daysSpinner, gbc);

        JButton returnButton = createModernButton("Return Book", ACCENT_SUCCESS);
        returnButton.addActionListener(e -> {
            String selectedBook = (String) bookCombo.getSelectedItem();
            int daysOverdue = (Integer) daysSpinner.getValue();

            String result = librarySystem.returnBook(currentStudent.getUsername(), selectedBook, daysOverdue);
            if (result.contains("successfully")) {
                if (daysOverdue > 0) {
                    double fineAmount = daysOverdue * 10.0;
                    showStyledMessageDialog(
                        String.format("Book returned successfully!\n\nOverdue by %d days.\nFine imposed: Ksh %.2f", 
                                    daysOverdue, fineAmount),
                        "Return Successful", 
                        JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    showSuccessDialog("Book returned successfully!", "Return Successful");
                }
                dialog.dispose();
            } else {
                showErrorDialog(result, "Return Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        contentPanel.add(returnButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showPayFineDialog() {
        if (currentStudent == null) return;
        
        double finesOwed = librarySystem.getStudentFines(currentStudent.getUsername());
        if (finesOwed <= 0) {
            showStyledMessageDialog("You have no outstanding fines.", "No Fines", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = createStyledDialog("Pay Fine", 400, 200);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel owedLabel = createStyledLabel(String.format("Fines owed: Ksh %.2f", finesOwed));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(owedLabel, gbc);

        JLabel payLabel = createStyledLabel("Amount to pay:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(payLabel, gbc);

        JSpinner amountSpinner = createStyledSpinner(finesOwed, 0, finesOwed, 0.5);
        gbc.gridx = 1;
        contentPanel.add(amountSpinner, gbc);

        JButton payButton = createModernButton("Pay Now", ACCENT_SUCCESS);
        payButton.addActionListener(e -> {
            double amount = (Double) amountSpinner.getValue();
            String result = librarySystem.payFine(currentStudent.getUsername(), amount);
            if (result.contains("Paid")) {
                showSuccessDialog(result, "Payment Successful");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Payment Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(payButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showManageRequestsDialog() {
        List<Request> pendingRequests = librarySystem.getPendingRequests();
        if (pendingRequests.isEmpty()) {
            showStyledMessageDialog("No pending requests.", "Pending Requests", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = createStyledDialog("Manage Requests", 600, 400);
        
        DefaultListModel<Request> listModel = new DefaultListModel<>();
        for (Request req : pendingRequests) {
            listModel.addElement(req);
        }

        JList<Request> requestList = new JList<>(listModel);
        requestList.setBackground(CARD_DARK);
        requestList.setForeground(TEXT_PRIMARY);
        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(requestList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_DARK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton approveButton = createModernButton("Approve", ACCENT_SUCCESS);
        approveButton.addActionListener(e -> {
            if (requestList.getSelectedIndex() < 0) {
                showErrorDialog("Please select a request.", "No Selection");
                return;
            }
            Request selectedRequest = requestList.getSelectedValue();
            String result = librarySystem.processRequest(selectedRequest, true);
            if (result.contains("approved")) {
                showSuccessDialog(result, "Request Approved");
                listModel.removeElement(selectedRequest);
            } else {
                showErrorDialog(result, "Approval Failed");
            }
        });
        buttonPanel.add(approveButton);

        JButton rejectButton = createModernButton("Reject", ACCENT_ERROR);
        rejectButton.addActionListener(e -> {
            if (requestList.getSelectedIndex() < 0) {
                showErrorDialog("Please select a request.", "No Selection");
                return;
            }
            Request selectedRequest = requestList.getSelectedValue();
            String result = librarySystem.processRequest(selectedRequest, false);
            if (result.contains("rejected")) {
                showSuccessDialog(result, "Request Rejected");
                listModel.removeElement(selectedRequest);
            } else {
                showErrorDialog(result, "Rejection Failed");
            }
        });
        buttonPanel.add(rejectButton);

        JButton closeButton = createModernButton("Close", TEXT_SECONDARY);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showManageBooksDialog() {
        List<Book> books = librarySystem.getAllBooks();
        StringBuilder bookList = new StringBuilder("Book Inventory:\n\n");
        if (books.isEmpty()) {
            bookList.append("No books in inventory.");
        } else {
            for (Book book : books) {
                bookList.append("• ").append(book.getTitle()).append(" - Copies: ").append(book.getCopies()).append("\n");
            }
        }
        showStyledMessageDialog(bookList.toString(), "Book Inventory", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStudentsDialog() {
        List<Student1> students = librarySystem.getAllStudents();
        StringBuilder studentList = new StringBuilder("Registered Students:\n\n");
        if (students.isEmpty()) {
            studentList.append("No students registered.");
        } else {
            for (Student1 student : students) {
                studentList.append("• ").append(student.getUsername())
                          .append(" - Fines: Ksh ")
                          .append(String.format("%.2f", student.getFinesOwed()))
                          .append(" - Books: ")
                          .append(librarySystem.getBorrowedBooksCount(student.getUsername()))
                          .append("\n");
            }
        }
        showStyledMessageDialog(studentList.toString(), "Students Overview", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddBookDialog() {
        JDialog dialog = createStyledDialog("Add Book", 500, 300);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel bookTitleLabel = createStyledLabel("Book Title:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        contentPanel.add(bookTitleLabel, gbc);

        JTextField titleField = createStyledTextField();
        titleField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        contentPanel.add(titleField, gbc);

        JLabel copiesLabel = createStyledLabel("Number of Copies:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(copiesLabel, gbc);

        JSpinner copiesSpinner = createStyledSpinner(1, 1, 100, 1);
        copiesSpinner.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        contentPanel.add(copiesSpinner, gbc);

        JButton addButton = createModernButton("Add Book", ACCENT_SUCCESS);
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            int copies = (Integer) copiesSpinner.getValue();

            if (title.isEmpty()) {
                showErrorDialog("Book title cannot be empty.", "Invalid Input");
                return;
            }

            String result = librarySystem.addBook(title, copies);
            if (result.contains("added") || result.contains("increased")) {
                showSuccessDialog(result, "Book Added");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Add Book Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(addButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showDeleteBookDialog() {
        List<Book> books = librarySystem.getAllBooks();
        if (books.isEmpty()) {
            showStyledMessageDialog("No books in inventory.", "No Books", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = createStyledDialog("Delete Book", 400, 200);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = createStyledLabel("Select book to delete:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(label, gbc);

        JComboBox<String> bookCombo = createStyledComboBox(
            books.stream().map(Book::getTitle).toArray(String[]::new)
        );
        gbc.gridy = 1;
        contentPanel.add(bookCombo, gbc);

        JButton deleteButton = createModernButton("Delete", ACCENT_ERROR);
        deleteButton.addActionListener(e -> {
            String selectedTitle = (String) bookCombo.getSelectedItem();
            String result = librarySystem.deleteBook(selectedTitle);
            if (result.contains("deleted")) {
                showSuccessDialog(result, "Book Deleted");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Delete Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(deleteButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showFineStudentDialog() {
        List<Student1> students = librarySystem.getAllStudents();
        if (students.isEmpty()) {
            showStyledMessageDialog("No students registered.", "No Students", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = createStyledDialog("Fine Student", 400, 220);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentLabel = createStyledLabel("Select Student:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(studentLabel, gbc);

        JComboBox<String> studentCombo = createStyledComboBox(
            students.stream().map(Student1::getUsername).toArray(String[]::new)
        );
        gbc.gridx = 1;
        contentPanel.add(studentCombo, gbc);

        JLabel amountLabel = createStyledLabel("Fine Amount (Ksh):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(amountLabel, gbc);

        JSpinner amountSpinner = createStyledSpinner(0.0, 0.0, 10000.0, 10.0);
        gbc.gridx = 1;
        contentPanel.add(amountSpinner, gbc);

        JButton fineButton = createModernButton("Apply Fine", ACCENT_WARNING);
        fineButton.addActionListener(e -> {
            String selectedUsername = (String) studentCombo.getSelectedItem();
            double fineAmount = (Double) amountSpinner.getValue();

            if (fineAmount <= 0) {
                showErrorDialog("Fine amount must be greater than 0.", "Invalid Amount");
                return;
            }

            String result = librarySystem.fineStudent(selectedUsername, fineAmount);
            if (result.contains("applied")) {
                showSuccessDialog(result, "Fine Applied");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Fine Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(fineButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showChangePasswordDialog() {
        JDialog dialog = createStyledDialog("Change Password", 400, 250);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel oldLabel = createStyledLabel("Current Password:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(oldLabel, gbc);

        JPasswordField oldField = createStyledPasswordField();
        gbc.gridx = 1;
        contentPanel.add(oldField, gbc);

        JLabel newLabel = createStyledLabel("New Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(newLabel, gbc);

        JPasswordField newField = createStyledPasswordField();
        gbc.gridx = 1;
        contentPanel.add(newField, gbc);

        JLabel confirmLabel = createStyledLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(confirmLabel, gbc);

        JPasswordField confirmField = createStyledPasswordField();
        gbc.gridx = 1;
        contentPanel.add(confirmField, gbc);

        JButton changeButton = createModernButton("Change Password", ACCENT_SUCCESS);
        changeButton.addActionListener(e -> {
            String oldPass = new String(oldField.getPassword());
            String newPass = new String(newField.getPassword());
            String confirmPass = new String(confirmField.getPassword());

            if (!currentAdmin.checkPassword(oldPass)) {
                showErrorDialog("Current password is incorrect.", "Invalid Password");
                return;
            }

            if (newPass.isEmpty()) {
                showErrorDialog("New password cannot be empty.", "Invalid Password");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                showErrorDialog("Passwords do not match.", "Password Mismatch");
                return;
            }

            String result = librarySystem.changeAdminPassword(currentAdmin.getUsername(), newPass);
            if (result.contains("changed")) {
                currentAdmin.setPassword(newPass);
                showSuccessDialog(result, "Password Changed");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Password Change Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        contentPanel.add(changeButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAddAdminDialog() {
        JDialog dialog = createStyledDialog("Add New Admin", 400, 250);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = createStyledLabel("Admin Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(userLabel, gbc);

        JTextField usernameField = createStyledTextField();
        gbc.gridx = 1;
        contentPanel.add(usernameField, gbc);

        JLabel passLabel = createStyledLabel("Admin Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(passLabel, gbc);

        JPasswordField passwordField = createStyledPasswordField();
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        JButton addButton = createModernButton("Add Admin", new Color(233, 30, 99));
        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showErrorDialog("Username and password cannot be empty.", "Invalid Input");
                return;
            }

            if (librarySystem.userExists(username)) {
                showErrorDialog("Username is already taken.", "Duplicate Username");
                return;
            }

            String result = librarySystem.registerAdmin(username, password);
            if (result.contains("created")) {
                showSuccessDialog(result, "Admin Added");
                usernameField.setText("");
                passwordField.setText("");
                dialog.dispose();
            } else {
                showErrorDialog(result, "Admin Creation Failed");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(addButton, gbc);

        JButton cancelButton = createModernButton("Cancel", TEXT_SECONDARY);
        cancelButton.addActionListener(e -> dialog.dispose());
        gbc.gridx = 1;
        contentPanel.add(cancelButton, gbc);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JDialog createStyledDialog(String title, int width, int height) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BACKGROUND_DARK);
        dialog.setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        
        return dialog;
    }

    private JSpinner createStyledSpinner(Number value, Comparable min, Comparable max, Number step) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        spinner.setBackground(CARD_DARK);
        spinner.setForeground(TEXT_PRIMARY);
        spinner.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setBackground(CARD_DARK);
            textField.setForeground(TEXT_PRIMARY);
            textField.setCaretColor(ACCENT_PRIMARY);
            textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        
        return spinner;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySystemGUI());
    }
}