import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class ManagePassenger extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnAdd, btnEdit, btnDelete, btnBack;
    private static final String FILE_NAME = "user_data.txt";
    private TransportBookingSystem parentSystem;

    public ManagePassenger(TransportBookingSystem system) {
        this.parentSystem = system;
        setLayout(new BorderLayout());

        // Create header panel
        JPanel topPanel = new JPanel(new BorderLayout());
        btnBack = new JButton("Back");
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonContainer.add(btnBack);
        topPanel.add(buttonContainer, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Username", "Password"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtUsername);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(txtPassword);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("Add User");
        btnEdit = new JButton("Edit User");
        btnDelete = new JButton("Delete User");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentSystem.showAdminPage();
            }
        });

        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = userTable.getSelectedRow();
                if (row != -1) {
                    txtUsername.setText((String)tableModel.getValueAt(row, 0));
                    txtPassword.setText((String)tableModel.getValueAt(row, 1));
                }
            }
        });

        loadUserData();
    }

    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.length() == 0 || password.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] rowData = {username, password};
        tableModel.addRow(rowData);
        saveUserDataQuietly();
        clearFields();
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.length() == 0 || password.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.setValueAt(username, selectedRow, 0);
        tableModel.setValueAt(password, selectedRow, 1);
        saveUserDataQuietly();
        clearFields();
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this user?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            saveUserDataQuietly();
            clearFields();
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        userTable.clearSelection();
    }

    private boolean isUsernameExists(String username) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void saveUserDataQuietly() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(tableModel.getValueAt(i, 0).toString() + "," +
                           tableModel.getValueAt(i, 1).toString());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data to file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadUserData() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                return;
            }
            
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) continue;
                
                String[] rowData = line.split(",");
                if (rowData.length == 2) {
                    tableModel.addRow(new Object[]{rowData[0].trim(), rowData[1].trim()});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data from file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}