import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class MakeBookingRequest extends JPanel {
    private JTable transportTable;
    private DefaultTableModel tableModel;
    private JTextField txtTransportationId, txtTicketAmount;
    private JButton btnMakeBooking, btnBack;
    private final TransportBookingSystem system;
    private static final String BOOKING_REQUEST_FILE = "booking_requests.txt";

    public MakeBookingRequest(final TransportBookingSystem system) {
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
        String[] columns = {"Transportation ID", "Type", "From", "To", "Date", "Time", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table cells non-editable
            }
        };
        transportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transportTable);

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        txtTransportationId = new JTextField();
        txtTicketAmount = new JTextField();
        inputPanel.add(new JLabel("Enter Transportation ID:"));
        inputPanel.add(txtTransportationId);
        inputPanel.add(new JLabel("Enter Ticket Amount:"));
        inputPanel.add(txtTicketAmount);

        // Combine top panel and input panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel setup
        JPanel buttonPanel = new JPanel();
        btnMakeBooking = new JButton("Make Booking Request");
        buttonPanel.add(btnMakeBooking);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnMakeBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeBookingRequest();
            }
        });

        // Load data from the transport data
        loadTransportData();
    }

    private void makeBookingRequest() {
        String transportationId = txtTransportationId.getText().trim();
        String ticketAmountStr = txtTicketAmount.getText().trim();
        if (transportationId.isEmpty() || ticketAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Transportation ID and Ticket Amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int ticketAmount;
        try {
            ticketAmount = Integer.parseInt(ticketAmountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Ticket Amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        boolean found = false;
        String[] bookingData = new String[10]; // Ensure this array has 10 elements
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(transportationId)) {
                found = true;
                bookingData[0] = generateBookingId(); // Booking ID
                bookingData[1] = (String) tableModel.getValueAt(i, 0); // Transportation ID
                bookingData[2] = (String) tableModel.getValueAt(i, 1); // Type
                bookingData[3] = (String) tableModel.getValueAt(i, 2); // From
                bookingData[4] = (String) tableModel.getValueAt(i, 3); // To
                bookingData[5] = (String) tableModel.getValueAt(i, 4); // Date
                bookingData[6] = (String) tableModel.getValueAt(i, 5); // Time
                bookingData[7] = (String) tableModel.getValueAt(i, 6); // Price
                bookingData[8] = String.valueOf(ticketAmount); // Ticket Amount
                bookingData[9] = "Pending"; // Status
                break;
            }
        }
    
        if (found) {
            saveBookingRequest(bookingData);
            JOptionPane.showMessageDialog(this, "Make Booking Request Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Transportation ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateBookingId() {
        // Simple Booking ID generation: Using the current timestamp
        return "B" + System.currentTimeMillis();
    }

    private void saveBookingRequest(String[] bookingData) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(BOOKING_REQUEST_FILE, true));
            writer.write(join(",", bookingData));
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving booking request.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String join(String delimiter, String[] elements) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            sb.append(elements[i]);
            if (i < elements.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    private void loadTransportData() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(TransportBookingSystem.TRANSPORT_DATA_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                // Skip the first element (Booking ID) and add the rest
                tableModel.addRow(new Object[]{rowData[1], rowData[2], rowData[3], rowData[4], rowData[5], rowData[6], rowData[7]});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data from file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}