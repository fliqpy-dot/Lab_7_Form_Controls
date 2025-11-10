import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.*;

/**
 * MJ23 Playgrind Gym - Login Form
 * Software Engineering Project - Form Controls Assignment
 * 
 * Developer: Figuracion, Nick Jamesly M.
 * Module: 1.0 Security - Login Authentication
 * 
 * Form Controls: 8 total
 * 1. Username (Text Input)
 * 2. Password (Password Input)
 * 3. Role Selection (Dropdown)
 * 4. Remember Me (Checkbox)
 * 5. Show Password (Checkbox)
 * 6. Login Timestamp (Hidden Field)
 * 7. Session Token (Hidden Field)
 * 8. Button Group (Login/Reset)
 */
public class LoginForm extends JFrame {
    
    // Form Controls (8 total)
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JCheckBox rememberMeCheckBox;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JButton resetButton;
    
    // Hidden Fields
    private String loginTimestamp;
    private String sessionToken;
    
    // Error Labels
    private JLabel usernameErrorLabel;
    private JLabel passwordErrorLabel;
    private JLabel roleErrorLabel;
    private JLabel statusLabel;
    
    // Failed Login Tracking
    private int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 5;
    
    // Test Credentials (In real app, check against database)
    private Map<String, UserCredential> validUsers;
    
    public LoginForm() {
        initializeTestUsers();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initializeTestUsers() {
        validUsers = new HashMap<>();
        validUsers.put("admin", new UserCredential("admin", "Admin123", "Administrator"));
        validUsers.put("staff", new UserCredential("staff", "Staff123", "Staff Member"));
    }
    
    private void initComponents() {
        setTitle("MJ23 Playgrind Gym - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.decode("#F5F5DC"));

        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#332F64"));
        headerPanel.setPreferredSize(new Dimension(300, 80));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("MJ23 PLAYGRIND GYM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Management System Login");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Row 0: Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        formPanel.add(usernameLabel, gbc);
        usernameLabel.setForeground(Color.decode("#332F64"));
        
        // Row 1: Username Field (Control #1)
        gbc.gridy = 1;
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(300, 35));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#FDEE21")),
            BorderFactory.createEmptyBorder(80, 80, 80, 80)
        ));
        usernameField.setToolTipText("Enter username (4-20 characters, alphanumeric + underscore)");
        
        // Add focus listener for validation
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateUsername();
            }
        });
        
        formPanel.add(usernameField, gbc);
        
        // Row 2: Username Error
        gbc.gridy = 2;
        usernameErrorLabel = new JLabel(" ");
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        formPanel.add(usernameErrorLabel, gbc);
        
        // Row 3: Password Label
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password *");
        passwordLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        formPanel.add(passwordLabel, gbc);
        passwordLabel.setForeground(Color.decode("#332F64"));
        
        // Row 4: Password Field (Control #2)
        gbc.gridy = 4;
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#FDEE21")),
            BorderFactory.createEmptyBorder(80, 80, 80, 80)
        ));
        passwordField.setToolTipText("Min 8 chars, include uppercase, lowercase, and number");
        
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validatePassword();
            }
        });
        
        formPanel.add(passwordField, gbc);
        
        // Row 5: Password Error
        gbc.gridy = 5;
        passwordErrorLabel = new JLabel(" ");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        formPanel.add(passwordErrorLabel, gbc);
        
        // Row 6: Show Password Checkbox (Control #5)
        gbc.gridy = 6;
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(Color.WHITE);
        showPasswordCheckBox.setFont(new Font("Poppins", Font.PLAIN, 15));
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        formPanel.add(showPasswordCheckBox, gbc);
        
        // Row 7: Role Label
        gbc.gridy = 7;
        JLabel roleLabel = new JLabel("Login As *");
        roleLabel.setFont(new Font("Poppins", Font.BOLD, 16));
        formPanel.add(roleLabel, gbc);
        
        // Row 8: Role Dropdown (Control #3)
        gbc.gridy = 8;
        String[] roles = {"-- Select Role --", "Administrator", "Staff Member"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(new Dimension(300, 35));
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(Color.WHITE);
        roleComboBox.addActionListener(e -> validateRole());
        formPanel.add(roleComboBox, gbc);
        
        // Row 9: Role Error
        gbc.gridy = 9;
        roleErrorLabel = new JLabel(" ");
        roleErrorLabel.setForeground(Color.RED);
        roleErrorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        formPanel.add(roleErrorLabel, gbc);
        
        // Row 10: Remember Me Checkbox (Control #4)
        gbc.gridy = 10;
        rememberMeCheckBox = new JCheckBox("Remember me for 30 days");
        rememberMeCheckBox.setBackground(Color.WHITE);
        rememberMeCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(rememberMeCheckBox, gbc);
        
        // Row 11: Status Label
        gbc.gridy = 11;
        gbc.insets = new Insets(15, 5, 5, 5);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 40, 30, 40));
        
        // Login Button (Control #8a)
        final Color BUTTON_FILL_COLOR = Color.decode("#FDEE21");
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(140, 40));
        loginButton.setFont(new Font("Poppins", Font.BOLD, 18));
        loginButton.setBackground(BUTTON_FILL_COLOR);
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        
        // Add hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
        });
        
        // Reset Button (Control #8b)
        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(140, 40));
        resetButton.setFont(new Font("Poppins", Font.BOLD, 18));
        resetButton.setBackground(new Color(149, 165, 166));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(e -> handleReset());
        
        resetButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                resetButton.setBackground(new Color(127, 140, 141));
            }
            public void mouseExited(MouseEvent e) {
                resetButton.setBackground(new Color(149, 165, 166));
            }
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        
        // Test Credentials Info
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel infoTitle = new JLabel("Test Credentials:");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 11));
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel info1 = new JLabel("Admin: admin / Admin123");
        info1.setFont(new Font("Arial", Font.PLAIN, 10));
        info1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel info2 = new JLabel("Staff: staff / Staff123");
        info2.setFont(new Font("Arial", Font.PLAIN, 10));
        info2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(infoTitle);
        infoPanel.add(info1);
        infoPanel.add(info2);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        containerPanel.add(infoPanel, BorderLayout.CENTER);
        
        return containerPanel;
    }
    
    // Validation Methods
    private boolean validateUsername() {
        String username = usernameField.getText().trim();
        
        if (username.isEmpty()) {
            usernameErrorLabel.setText("Username is required");
            usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        if (username.length() < 4 || username.length() > 20) {
            usernameErrorLabel.setText("Username must be 4-20 characters");
            usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            usernameErrorLabel.setText("Only alphanumeric and underscore allowed");
            usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        usernameErrorLabel.setText(" ");
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return true;
    }
    
    private boolean validatePassword() {
        String password = new String(passwordField.getPassword());
        
        if (password.isEmpty()) {
            passwordErrorLabel.setText("Password is required");
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        if (password.length() < 8) {
            passwordErrorLabel.setText("Password must be at least 8 characters");
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        if (!password.matches(".*[A-Z].*")) {
            passwordErrorLabel.setText("Password must contain uppercase letter");
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        if (!password.matches(".*[a-z].*")) {
            passwordErrorLabel.setText("Password must contain lowercase letter");
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        if (!password.matches(".*\\d.*")) {
            passwordErrorLabel.setText("Password must contain a number");
            passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return false;
        }
        
        passwordErrorLabel.setText(" ");
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return true;
    }
    
    private boolean validateRole() {
        if (roleComboBox.getSelectedIndex() == 0) {
            roleErrorLabel.setText("Please select a role");
            return false;
        }
        roleErrorLabel.setText(" ");
        return true;
    }
    
    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('•');
        }
    }
    
    private void handleLogin() {
        // Check if account is locked
        if (failedAttempts >= MAX_ATTEMPTS) {
            statusLabel.setText("Account locked. Too many failed attempts.");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this,
                "Account locked due to too many failed login attempts.\n" +
                "Please contact administrator.",
                "Account Locked",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate all fields
        boolean usernameValid = validateUsername();
        boolean passwordValid = validatePassword();
        boolean roleValid = validateRole();
        
        if (!usernameValid || !passwordValid || !roleValid) {
            statusLabel.setText("Please correct the errors above");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        // Get form values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleComboBox.getSelectedItem();
        
        // Generate hidden fields (Control #6 & #7)
        loginTimestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        sessionToken = UUID.randomUUID().toString();
        
        // Authenticate
        if (authenticateUser(username, password, selectedRole)) {
            // Success
            statusLabel.setText("✓ Login successful!");
            statusLabel.setForeground(new Color(46, 204, 113));
            
            // Show success dialog
            String message = String.format(
                "Login Successful!\n\n" +
                "Username: %s\n" +
                "Role: %s\n" +
                "Login Time: %s\n" +
                "Session Token: %s\n" +
                "Remember Me: %s",
                username,
                selectedRole,
                loginTimestamp,
                sessionToken.substring(0, 8) + "...",
                rememberMeCheckBox.isSelected() ? "Yes (30 days)" : "No"
            );
            
            JOptionPane.showMessageDialog(this,
                message,
                "Login Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Reset failed attempts
            failedAttempts = 0;
            
            // In real application, navigate to dashboard
            // For demo, just show success
            
        } else {
            // Failed login
            failedAttempts++;
            int remaining = MAX_ATTEMPTS - failedAttempts;
            
            statusLabel.setText(String.format(
                "✗ Login failed! %d attempts remaining",
                remaining
            ));
            statusLabel.setForeground(Color.RED);
            
            JOptionPane.showMessageDialog(this,
                String.format(
                    "Invalid credentials!\n\n" +
                    "Failed attempts: %d of %d\n" +
                    "Remaining attempts: %d\n\n" +
                    "Test credentials:\n" +
                    "Admin: admin / Admin123\n" +
                    "Staff: staff / Staff123",
                    failedAttempts, MAX_ATTEMPTS, remaining
                ),
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean authenticateUser(String username, String password, String role) {
        UserCredential user = validUsers.get(username);
        if (user == null) {
            return false;
        }
        
        // In real app, compare hashed passwords
        // String hashedPassword = hashPassword(password);
        // return user.password.equals(hashedPassword) && user.role.equals(role);
        
        return user.password.equals(password) && user.role.equals(role);
    }
    
    // SHA-256 Password Hashing (ready for real implementation)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void handleReset() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        rememberMeCheckBox.setSelected(false);
        showPasswordCheckBox.setSelected(false);
        
        usernameErrorLabel.setText(" ");
        passwordErrorLabel.setText(" ");
        roleErrorLabel.setText(" ");
        statusLabel.setText(" ");
        
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        usernameField.requestFocus();
    }
    
    // Helper class for test credentials
    private static class UserCredential {
        String username;
        String password;
        String role;
        
        UserCredential(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
    
    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}    

