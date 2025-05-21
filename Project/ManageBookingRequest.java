import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class ManageBookingRequest extends JPanel {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton btnApprove, btnReject, btnBack, btnGenerateReport;
    private final TransportBookingSystem system;
    private static final String BOOKING_REQUEST_FILE = "booking_requests.txt";
    private static final String REPORT_FILE = "booking_report.txt";

    public ManageBookingRequest(final TransportBookingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        // Create top panel for back button
        JPanel topPanel = new JPanel(new BorderLayout());
        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(150, 30));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                system.showAdminPage();  // Use system to navigate back to admin page
            }
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        // Table setup
        String[] columns = {"Booking ID", "Transportation ID", "Type", "From", "To", "Date", "Time", "Price", "Ticket Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table cells non-editable
            }
        };
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        // Add top panel and table to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel setup
        JPanel buttonPanel = new JPanel();
        btnApprove = new JButton("Approve");
        btnReject = new JButton("Reject");
        btnGenerateReport = new JButton("Generate Report");
        buttonPanel.add(btnApprove);
        buttonPanel.add(btnReject);
        buttonPanel.add(btnGenerateReport);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnApprove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                approveBooking();
            }
        });

        btnReject.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rejectBooking();
            }
        });

        btnGenerateReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        // Load initial data (if any)
        loadBookingData();
    }

    private void approveBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to approve.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
        tableModel.setValueAt("APPROVED", selectedRow, 9);  // Update status to "APPROVED"
        updateBookingStatus(bookingId, "APPROVED");
        JOptionPane.showMessageDialog(this, "Booking ID " + bookingId + " has been approved.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void rejectBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to reject.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
        tableModel.setValueAt("REJECTED", selectedRow, 9);  // Update status to "REJECTED"
        updateBookingStatus(bookingId, "REJECTED");
        JOptionPane.showMessageDialog(this, "Booking ID " + bookingId + " has been rejected.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateBookingStatus(String bookingId, String status) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        File tempFile = new File("temp_booking_requests.txt");
        try {
            reader = new BufferedReader(new FileReader(BOOKING_REQUEST_FILE));
            writer = new BufferedWriter(new FileWriter(tempFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData.length >= 10 && rowData[0].equals(bookingId)) { // Check if rowData has at least 10 elements
                    rowData[9] = status;  // Update status
                }
                writer.write(join(",", rowData)); // Use the custom join method
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating booking status.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                // Replace the original file with the updated file
                File originalFile = new File(BOOKING_REQUEST_FILE);
                if (originalFile.delete()) {
                    tempFile.renameTo(originalFile);
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating booking status file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    private void loadBookingData() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(BOOKING_REQUEST_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData.length >= 10) { // Ensure the row has all required fields
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

    // Generate report method
    private void generateReport() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(REPORT_FILE));

            // Add header with fixed width formatting for alignment
            writer.write(String.format("%-15s %-20s %-10s %-10s %-10s %-10s %-10s %-10s %-15s %-10s", 
                                      "Booking ID", "Transportation ID", "Type", "From", "To", "Date", "Time", "Price", "Ticket Amount", "Status"));
            writer.newLine();
            
            // Write a separator line for table
            writer.write(String.format("%-15s %-20s %-10s %-10s %-10s %-10s %-10s %-10s %-15s %-10s", 
                                      "---------------", "--------------------", "----------", "----------", "----------", "----------", "----------", "----------", "---------------", "----------"));
            writer.newLine();

            // Write the data from the table to the report file with proper formatting
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(String.format("%-15s %-20s %-10s %-10s %-10s %-10s %-10s %-10s %-15s %-10s", 
                                          tableModel.getValueAt(i, 0),
                                          tableModel.getValueAt(i, 1),
                                          tableModel.getValueAt(i, 2),
                                          tableModel.getValueAt(i, 3),
                                          tableModel.getValueAt(i, 4),
                                          tableModel.getValueAt(i, 5),
                                          tableModel.getValueAt(i, 6),
                                          tableModel.getValueAt(i, 7),
                                          tableModel.getValueAt(i, 8),
                                          tableModel.getValueAt(i, 9)));
                writer.newLine();
            }

            JOptionPane.showMessageDialog(this, "Report has been generated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating the report.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
