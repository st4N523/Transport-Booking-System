import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PassengerPage extends JPanel {
    private JPanel headerPanel, titlePanel, logoutPanel, panel;
    private JLabel headerLabel;
    private Font buttonFont;
    private JButton btnLogout, btnMakeBooking, btnManageBooking, btnCheckBookingRequest;
    private TransportBookingSystem system;

    public PassengerPage(TransportBookingSystem system) {
        this.system = system;
        
        // Create the layout
        setLayout(new BorderLayout());

        // Header panel with just a text label and a logout button
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.green);
        
        // Set the header label without an icon
        headerLabel = new JLabel("PASSENGER MENU");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 60));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Center align the text

        titlePanel.add(headerLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Create logout panel
        logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.setPreferredSize(new Dimension(120, 40));
        btnLogout.setBackground(Color.RED);
        logoutPanel.add(btnLogout);

        headerPanel.add(logoutPanel, BorderLayout.NORTH);

        // Create button panel
        panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        buttonFont = new Font("Segoe UI", Font.BOLD, 18);

        btnMakeBooking = new JButton("Make Transport Booking Request");
        btnManageBooking = new JButton("Manage Booking");
        btnCheckBookingRequest = new JButton("Check Booking Request");

        btnMakeBooking.setFont(buttonFont);
        btnManageBooking.setFont(buttonFont);
        btnCheckBookingRequest.setFont(buttonFont);

        panel.add(btnMakeBooking);
        panel.add(btnManageBooking);
        panel.add(btnCheckBookingRequest);
        panel.add(btnLogout); // Add logout button here

        // Add action listeners
        ButtonHandler handler = new ButtonHandler();
        btnMakeBooking.addActionListener(handler);
        btnManageBooking.addActionListener(handler);
        btnCheckBookingRequest.addActionListener(handler);
        btnLogout.addActionListener(handler);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnMakeBooking) {
                system.showMakeBookingRequestPage();
            } else if (e.getSource() == btnManageBooking) {
                system.showManageBookingPage();
            } else if (e.getSource() == btnCheckBookingRequest) {
                system.showCheckBookingRequest();
            } else if (e.getSource() == btnLogout) {
                system.showLoginPage();
            }
        }
    }
}
