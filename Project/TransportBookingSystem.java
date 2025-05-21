import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TransportBookingSystem extends JFrame {
    private LoginPage loginPage;
    private AdminPage adminPage;
    private PassengerPage passengerPage;
    private ManagePassenger managePassengerPage;

    public static final String TRANSPORT_DATA_FILE = "transport_data.txt";
    public static final String USER_DATA_FILE = "user_data.txt"; // Add path for the user data file

    public TransportBookingSystem() {
        setTitle("Transport Booking System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applyBlueTheme(); // Apply blue theme
        showLoginPage();
    }

    // Apply the blue theme to the system
    private void applyBlueTheme() {
        // Set background color of the main frame
        getContentPane().setBackground(new Color(70, 130, 180)); // Light blue background

        // Set the default button and label colors
        UIManager.put("Button.background", new Color(100, 149, 237)); // Light blue for buttons
        UIManager.put("Button.foreground", Color.BLACK); // Black text on buttons
        UIManager.put("Label.foreground", Color.BLACK); // Black text on labels
        UIManager.put("TextField.background", new Color(240, 248, 255)); // Lighter blue for text fields
        UIManager.put("TextField.foreground", Color.BLACK); // Black text on text fields
        UIManager.put("ComboBox.background", new Color(240, 248, 255)); // Lighter blue for combo boxes
        UIManager.put("ComboBox.foreground", Color.BLACK); // Black text on combo boxes
        UIManager.put("TextArea.background", new Color(240, 248, 255)); // Lighter blue for text areas
        UIManager.put("TextArea.foreground", Color.BLACK); // Black text on text areas
        
        // Set the SansSerif bold font for components
        Font sansSerifBoldFont = new Font("SansSerif", Font.BOLD, 16); // SansSerif font, bold, size 16

        // Set font globally for components
        UIManager.put("Button.font", sansSerifBoldFont);
        UIManager.put("Label.font", sansSerifBoldFont);
        UIManager.put("TextField.font", sansSerifBoldFont);
        UIManager.put("ComboBox.font", sansSerifBoldFont);
        UIManager.put("TextArea.font", sansSerifBoldFont);

        // Apply a different font for specific components if needed
        // Example: For a specific label or button
        JLabel label = new JLabel("Welcome");
        label.setFont(new Font("SansSerif", Font.BOLD, 18)); // Specific font for label
    }

    public void showLoginPage() {
        getContentPane().removeAll(); // Clear current content
        if (loginPage == null) {
            loginPage = new LoginPage(this);
        }
        getContentPane().add(loginPage);
        validate();
        repaint();
        setVisible(true);
    }

    public void showAdminPage() {
        getContentPane().removeAll(); // Clear current content
        if (adminPage == null) {
            adminPage = new AdminPage(this);
        }
        getContentPane().add(adminPage);
        validate();
        repaint();
    }

    public void showPassengerPage() {
        getContentPane().removeAll();
        if (passengerPage == null) {
            passengerPage = new PassengerPage(this);
        }
        getContentPane().add(passengerPage);
        validate();
        repaint();
    }

    public void handleAdminLogin(String username, String password) {
        if ("admin".equals(username) && "123".equals(password)) {
            showAdminPage();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid admin credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handlePassengerLogin(String username, String password) {
    try {
        File userFile = new File(USER_DATA_FILE);
        
        // If the file does not exist, create it
        if (!userFile.exists()) {
            userFile.createNewFile();
        }

        BufferedReader reader = new BufferedReader(new FileReader(userFile));
        String line;
        boolean validUser = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] credentials = line.split(",");
            if (credentials.length != 2) continue;

            String fileUsername = credentials[0].trim();
            String filePassword = credentials[1].trim();

            if (fileUsername.equals(username) && filePassword.equals(password)) {
                validUser = true;
                break;
            }
        }

        reader.close();

        if (validUser) {
            getContentPane().removeAll();
            passengerPage = new PassengerPage(this);
            getContentPane().add(passengerPage);
            validate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid passenger credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error reading user data", "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    // Function button for Passenger
    public void showCheckBookingRequest() {
        getContentPane().removeAll();
        getContentPane().add(new CheckBookingRequest(this));
        validate();
        repaint();
    }

    public void showMakeBookingRequestPage() {
        getContentPane().removeAll();
        getContentPane().add(new MakeBookingRequest(this));
        validate();
        repaint();
    }

    public void showManageBookingPage() {
        getContentPane().removeAll();
        getContentPane().add(new ManageBooking(this));
        validate();
        repaint();
    }

    // Function button for Admin
    public void showManageTransportPage() {
        getContentPane().removeAll();
        getContentPane().add(new ManageTransport(this));
        validate();
        repaint();
    }

    public void showManagePassengerPage() {
    getContentPane().removeAll();
    managePassengerPage = new ManagePassenger(this);
    getContentPane().add(managePassengerPage);
    validate();
    repaint();
	}

    public void showManageBookingRequestPage() {
        getContentPane().removeAll();
        getContentPane().add(new ManageBookingRequest(this));
        validate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TransportBookingSystem system = new TransportBookingSystem();
            }
        });
    }
}


