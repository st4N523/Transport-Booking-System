import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginPage extends JPanel implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton adminButton, passengerButton, signUpButton, exitButton;
    private TransportBookingSystem system;

    private JPanel headerPanel, titlePanel;
    private JLabel headerLabel;

    public LoginPage(TransportBookingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());  // Use BorderLayout for flexibility

        // Create header panel
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(70, 130, 180)); // Light blue color for header

        // Set the header label without an icon
        headerLabel = new JLabel("Welcome To Transport Booking System");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        headerLabel.setForeground(Color.WHITE);  // Set text color to white for contrast
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Center align the text

        titlePanel.add(headerLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Create login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5, 2)); // Grid layout for form inputs

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Passenger login button
        passengerButton = new JButton("Passenger Login");
        passengerButton.addActionListener(this);
        
        // Admin login button
        adminButton = new JButton("Admin Login");
        adminButton.addActionListener(this);

        // Sign Up button
        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(this);

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);

        // Add components to login panel
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(passengerButton);
        loginPanel.add(adminButton);
        loginPanel.add(signUpButton);
        loginPanel.add(exitButton);

        // Add header and login panel to the main panel
        add(headerPanel, BorderLayout.NORTH); // Header at the top
        add(loginPanel, BorderLayout.CENTER); // Login form in the center
    }

    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Handle Admin Login
        if (e.getSource() == adminButton) {
            system.handleAdminLogin(username, password);
        }
        // Handle Passenger Login
        else if (e.getSource() == passengerButton) {
            System.out.println("Passenger Login Attempt: " + username + ", " + password);  // Debugging

            // Check if username and password are entered
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            } else {
                system.handlePassengerLogin(username, password);
            }
        }
        // Handle Sign Up
        else if (e.getSource() == signUpButton) {
            // Check if username or password are empty
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.");
            } else if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists. Sign Up failed.");
            } else {
                try {
                    // Append the new username and password in "username,password" format
                    FileWriter writer = new FileWriter("user_data.txt", true);
                    writer.write(username + "," + password + "\n");
                    writer.close();
                    
                    JOptionPane.showMessageDialog(this, "Sign Up Successful!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving user data: " + ex.getMessage());
                }
            }
            // Clear the fields regardless of success or failure
            usernameField.setText("");
            passwordField.setText("");
        }
        // Handle Exit
        else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    // Method to check if username already exists
    private boolean isUsernameTaken(String username) {
        File userFile = new File("user_data.txt");

        // Check if the file exists, if not, create a new one
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();  // Create new file if it doesn't exist
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error creating user data file: " + e.getMessage());
            }
            return false;  // If file doesn't exist, return false, as no users are in the file
        }

        // If the file exists, proceed to check the username
        try {
            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] userData = line.split(",");
                // Ensure userData has at least one element before accessing it
                if (userData.length > 0 && userData[0].equals(username)) {
                    reader.close();
                    return true;  // Username found, return true
                }
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user data: " + e.getMessage());
        }
        return false;  // Username not found
    }
}
