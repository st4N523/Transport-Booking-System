import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class ManageBooking extends JPanel {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextField txtTicketAmount;
    private JButton btnEdit, btnDelete, btnBack;
    private final TransportBookingSystem system;
    private static final String BOOKING_REQUEST_FILE = "booking_requests.txt";

    public ManageBooking(final TransportBookingSystem system) {
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
        String[] columns = {"Booking ID", "Transportation ID", "Type", "From", "To", "Date", "Time", "Price", "Ticket Amount", "Status"}; // Added "Status"
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;  // Only "Ticket Amount" is editable
            }
        };
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
    
        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        txtTicketAmount = new JTextField();
        inputPanel.add(new JLabel("Edit Ticket Amount:"));
        inputPanel.add(txtTicketAmount);
    
        // Combine top panel and input panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
    
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    
        // Button panel setup
        JPanel buttonPanel = new JPanel();
        btnEdit = new JButton("Edit Booking Request");
        btnDelete = new JButton("Delete Booking Request");
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);
    
        // Event Listeners
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editBookingRequest();
            }
        });
    
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteBookingRequest();
            }
        });
    
        // Load initial data (if any)
        loadBookingData();
    }

    private void editBookingRequest() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String ticketAmountStr = txtTicketAmount.getText().trim();
        if (ticketAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a new Ticket Amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int ticketAmount;
        try {
            ticketAmount = Integer.parseInt(ticketAmountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Ticket Amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Preserve the existing status
        String status = (String) tableModel.getValueAt(selectedRow, 9); // Assuming status is in column 9
        tableModel.setValueAt(ticketAmount, selectedRow, 8);  // Update "Ticket Amount"
        tableModel.setValueAt(status, selectedRow, 9);  // Preserve the status
        saveBookingData();
        JOptionPane.showMessageDialog(this, "Booking request updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteBookingRequest() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.removeRow(selectedRow);
        saveBookingData();
        JOptionPane.showMessageDialog(this, "Booking request deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveBookingData() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(BOOKING_REQUEST_FILE));
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString() + (j == tableModel.getColumnCount() - 1 ? "" : ","));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving booking data to file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}