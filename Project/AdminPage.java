import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminPage extends JPanel {
    private JPanel headerPanel, titlePanel, logoutPanel, panel;
    private JLabel headerLabel;
    private Font buttonFont;
    private JButton btnLogout, btnManageTransport, btnManagePassenger, btnManageBookingRequest;
    private TransportBookingSystem system;

    public AdminPage(TransportBookingSystem system) {
        this.system = system;
        
        // Create the layout
        setLayout(new BorderLayout());

        // Header panel with text label and logout button
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.blue);
        
        // Set the header label without an icon
        headerLabel = new JLabel("ADMIN MENU");
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

        btnManageTransport = new JButton("Manage Transportation");
        btnManagePassenger = new JButton("Manage Passenger");
        btnManageBookingRequest = new JButton("Manage Booking Request");

        btnManageTransport.setFont(buttonFont);
        btnManagePassenger.setFont(buttonFont);
        btnManageBookingRequest.setFont(buttonFont);

        panel.add(btnManageTransport);
        panel.add(btnManagePassenger);
        panel.add(btnManageBookingRequest);
        panel.add(btnLogout); // Add logout button here

        // Add action listeners
        ButtonHandler handler = new ButtonHandler();
        btnManageTransport.addActionListener(handler);
        btnManagePassenger.addActionListener(handler);
        btnManageBookingRequest.addActionListener(handler);
        btnLogout.addActionListener(handler);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnManageTransport) {
                system.showManageTransportPage();
            } else if (e.getSource() == btnManagePassenger) {
                system.showManagePassengerPage();
            } else if (e.getSource() == btnManageBookingRequest) {
                system.showManageBookingRequestPage();
            } else if (e.getSource() == btnLogout) {
                system.showLoginPage();
            }
        }
    }
}
