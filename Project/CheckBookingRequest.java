import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class CheckBookingRequest extends JPanel {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextField txtTransportationId;
    private JButton btnSearch, btnBack;
    private final TransportBookingSystem system;
    private static final String BOOKING_REQUEST_FILE = "booking_requests.txt";

    public CheckBookingRequest(final TransportBookingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        // Create top panel for back button
        JPanel topPanel = new JPanel(new BorderLayout());
        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(150, 30));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                system.showPassengerPage();  // Use system to navigate back
            }
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        // Table setup
        String[] columns = {"Booking ID", "Transportation ID", "Type", "From", "To", "Date", "Time", "Price", "Ticket Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        txtTransportationId = new JTextField();
        inputPanel.add(new JLabel("Search for Transportation ID:"));
        inputPanel.add(txtTransportationId);

        // Combine top panel and input panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel setup
        JPanel buttonPanel = new JPanel();
        btnSearch = new JButton("Search Booking");
        buttonPanel.add(btnSearch);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBooking();
            }
        });

        // Load initial data (if any)
        loadBookingData();
    }

    private void searchBooking() {
        String transportationId = txtTransportationId.getText().trim();
        if (transportationId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Transportation ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear the table before searching
        tableModel.setRowCount(0);

        // Load booking data and filter by Transportation ID
        loadBookingData(transportationId);
    }

    private void loadBookingData() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BOOKING_REQUEST_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading booking data from file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadBookingData(String transportationId) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BOOKING_REQUEST_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData[1].equals(transportationId)) {
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading booking data from file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}