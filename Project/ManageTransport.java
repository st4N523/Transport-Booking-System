import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class ManageTransport extends JPanel {
    private JTable transportTable;
    private DefaultTableModel tableModel;
    private JTextField txtType, txtFrom, txtTo, txtDate, txtTime, txtTransportationId, txtPrice;
    private JButton btnAdd, btnEdit, btnDelete, btnBack;
    public static final String FILE_NAME = TransportBookingSystem.TRANSPORT_DATA_FILE;
    private final TransportBookingSystem system;

    public ManageTransport(final TransportBookingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        // Create top panel for back button
        JPanel topPanel = new JPanel(new BorderLayout());
        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(150, 30));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                system.showAdminPage();  // Use system to navigate back
            }
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        // Table setup
        String[] columns = {"Booking ID", "Transportation ID", "Type", "From", "To", "Date", "Time", "Price"};
        tableModel = new DefaultTableModel(columns, 0);
        transportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transportTable);

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        txtTransportationId = new JTextField();  // Manually entered ID
        txtType = new JTextField();
        txtFrom = new JTextField();
        txtTo = new JTextField();
        txtDate = new JTextField();
        txtTime = new JTextField();
        txtPrice = new JTextField();

        inputPanel.add(new JLabel("Transportation ID:"));
        inputPanel.add(txtTransportationId);  // For manual input of Transportation ID
        inputPanel.add(new JLabel("Transportation Type:"));
        inputPanel.add(txtType);
        inputPanel.add(new JLabel("From:"));
        inputPanel.add(txtFrom);
        inputPanel.add(new JLabel("To:"));
        inputPanel.add(txtTo);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(txtDate);
        inputPanel.add(new JLabel("Time:"));
        inputPanel.add(txtTime);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(txtPrice);

        // Combine top panel and input panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel setup
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Transport");
        btnEdit = new JButton("Edit Transport");
        btnDelete = new JButton("Delete Transport");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTransport();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editTransport();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTransport();
            }
        });

        // Load data from the file
        loadTransportData();
    }

    private void addTransport() {
        String transportationId = txtTransportationId.getText();
        String type = txtType.getText();
        String from = txtFrom.getText();
        String to = txtTo.getText();
        String date = txtDate.getText();
        String time = txtTime.getText();
        String price = txtPrice.getText();

        if (transportationId.isEmpty() || type.isEmpty() || from.isEmpty() || to.isEmpty() || date.isEmpty() || time.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Auto-generate Booking ID
        String bookingId = generateBookingId();

        tableModel.addRow(new Object[]{bookingId, transportationId, type, from, to, date, time, price});
        clearFields();
        saveTransportData();
    }

    private String generateBookingId() {
        // Simple Booking ID generation: Using the current timestamp
        return "B" + System.currentTimeMillis();
    }

    private void editTransport() {
        int selectedRow = transportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setValueAt(txtTransportationId.getText(), selectedRow, 1);
        tableModel.setValueAt(txtType.getText(), selectedRow, 2);
        tableModel.setValueAt(txtFrom.getText(), selectedRow, 3);
        tableModel.setValueAt(txtTo.getText(), selectedRow, 4);
        tableModel.setValueAt(txtDate.getText(), selectedRow, 5);
        tableModel.setValueAt(txtTime.getText(), selectedRow, 6);
        tableModel.setValueAt(txtPrice.getText(), selectedRow, 7);
        clearFields();
        saveTransportData();
    }

    private void deleteTransport() {
        int selectedRow = transportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.removeRow(selectedRow);
        saveTransportData();
    }

    private void clearFields() {
        txtTransportationId.setText("");
        txtType.setText("");
        txtFrom.setText("");
        txtTo.setText("");
        txtDate.setText("");
        txtTime.setText("");
        txtPrice.setText("");
    }

    private void saveTransportData() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString() + (j == tableModel.getColumnCount() - 1 ? "" : ","));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data to file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTransportData() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            // File not found or empty - no data to load
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
