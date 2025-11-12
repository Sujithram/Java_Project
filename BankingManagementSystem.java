package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BankingManagementSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

//-------------------------------------------------------
// Customer class: Holds user account and transaction data
//-------------------------------------------------------
class Customer {
    String id;
    String password;
    double balance;
    ArrayList<String> transactions = new ArrayList<>();

    Customer(String id, String password, double balance) {
        this.id = id;
        this.password = password;
        this.balance = balance;
        transactions.add("Account created with balance: " + balance);
    }

    void credit(double amount) {
        balance += amount;
        transactions.add("Credited: " + amount + " | New Balance: " + balance);
    }

    boolean debit(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        transactions.add("Debited: " + amount + " | New Balance: " + balance);
        return true;
    }
}

//-------------------------------------------------------
// Login Frame: Handles both admin and customer login
//-------------------------------------------------------
class LoginFrame extends JFrame {
    private JTextField idField;
    private JPasswordField passField;
    private JButton loginBtn, adminBtn;

    static Customer[] customers = new Customer[5];
    static int count = 0;

    final String ADMIN_ID = "admin";
    final String ADMIN_PASS = "1234";

    LoginFrame() {
        setTitle("Banking Management System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Banking Management System");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Login ID:"), gbc);
        gbc.gridx = 1; idField = new JTextField(15); add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; passField = new JPasswordField(15); add(passField, gbc);

        loginBtn = new JButton("Customer Login");
        adminBtn = new JButton("Admin Login");
        gbc.gridx = 0; gbc.gridy = 3; add(loginBtn, gbc);
        gbc.gridx = 1; add(adminBtn, gbc);

        preloadCustomers();

        loginBtn.addActionListener(e -> loginCustomer());
        adminBtn.addActionListener(e -> loginAdmin());

        setVisible(true);
    }

    private void preloadCustomers() {
        if (count == 0) {
            customers[0] = new Customer("C001", "1111", 5000);
            customers[1] = new Customer("C002", "2222", 7000);
            customers[2] = new Customer("C003", "3333", 10000);
            count = 3;
        }
    }

    private void loginCustomer() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        for (int i = 0; i < count; i++) {
            if (customers[i].id.equals(id) && customers[i].password.equals(pass)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new CustomerDashboard(customers[i]);
                dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid ID or Password!");
    }

    private void loginAdmin() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        if (id.equals(ADMIN_ID) && pass.equals(ADMIN_PASS)) {
            JOptionPane.showMessageDialog(this, "Admin Login Successful!");
            new AdminDashboard(customers, count);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!");
        }
    }
}

//-------------------------------------------------------
// Customer Dashboard: Deposit, Withdraw, View Transactions
//-------------------------------------------------------
class CustomerDashboard extends JFrame {
    Customer customer;
    JLabel balanceLabel;

    CustomerDashboard(Customer customer) {
        this.customer = customer;
        setTitle("Customer Dashboard - " + customer.id);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome, " + customer.id, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        balanceLabel = new JLabel("Balance: " + customer.balance, JLabel.CENTER);
        JButton creditBtn = new JButton("Credit (Deposit)");
        JButton debitBtn = new JButton("Debit (Withdraw)");
        JButton viewTransBtn = new JButton("View Transactions");

        panel.add(balanceLabel);
        panel.add(creditBtn);
        panel.add(debitBtn);
        panel.add(viewTransBtn);

        add(panel, BorderLayout.CENTER);

        creditBtn.addActionListener(e -> creditMoney());
        debitBtn.addActionListener(e -> debitMoney());
        viewTransBtn.addActionListener(e -> viewTransactions());

        setVisible(true);
    }

    private void creditMoney() {
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        try {
            double amt = Double.parseDouble(amtStr);
            if (amt <= 0) throw new NumberFormatException();
            customer.credit(amt);
            balanceLabel.setText("Balance: " + customer.balance);
            JOptionPane.showMessageDialog(this, "Amount Credited Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
    }

    private void debitMoney() {
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        try {
            double amt = Double.parseDouble(amtStr);
            if (amt <= 0) throw new NumberFormatException();
            if (customer.debit(amt)) {
                balanceLabel.setText("Balance: " + customer.balance);
                JOptionPane.showMessageDialog(this, "Amount Debited Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient Balance!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!");
        }
    }

    private void viewTransactions() {
        StringBuilder sb = new StringBuilder();
        for (String t : customer.transactions) sb.append(t).append("\n");
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }
}

//-------------------------------------------------------
// Admin Dashboard: View all customers and balances
//-------------------------------------------------------
class AdminDashboard extends JFrame {
    AdminDashboard(Customer[] customers, int count) {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        JLabel title = new JLabel("All Customer Details", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            sb.append("Customer ID: ").append(customers[i].id)
              .append("\nBalance: ").append(customers[i].balance)
              .append("\nTransactions: ").append(customers[i].transactions.size())
              .append(" entries\n-------------------------\n");
        }
        area.setText(sb.toString());

        add(new JScrollPane(area), BorderLayout.CENTER);
        setVisible(true);
    }
}
