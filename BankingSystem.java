package project;
import java.util.Scanner;

class Account {
    String userId;
    String password;
    double balance;

    Account(String userId, String password, double balance) {
        this.userId = userId;
        this.password = password;
        this.balance = balance;
    }
}

public class BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- Step 1: Create some user accounts (stored in array) ---
        Account[] accounts = new Account[3];
        accounts[0] = new Account("user1", "pass1", 5000.0);
        accounts[1] = new Account("user2", "pass2", 10000.0);
        accounts[2] = new Account("user3", "pass3", 7500.0);

        System.out.println("====== Welcome to Simple Banking System ======");

        // --- Step 2: Login process ---
        System.out.print("Enter Login ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Password: ");
        String pw = sc.nextLine();

        Account loggedIn = null;
        for (Account acc : accounts) {
            if (acc.userId.equals(id) && acc.password.equals(pw)) {
                loggedIn = acc;
                break;
            }
        }

        if (loggedIn == null) {
            System.out.println("Invalid Login ID or Password!");
            System.out.println("Exiting system...");
            return;
        }

        System.out.println("\nLogin Successful! Welcome, " + loggedIn.userId + "\n");

        // --- Step 3: Banking operations ---
        int choice;
        do {
            System.out.println("------ Main Menu ------");
            System.out.println("1. View Balance");
            System.out.println("2. Credit Money");
            System.out.println("3. Debit Money");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: ₹" + loggedIn.balance);
                    break;

                case 2:
                    System.out.print("Enter amount to credit: ₹");
                    double creditAmount = sc.nextDouble();
                    if (creditAmount > 0) {
                        loggedIn.balance += creditAmount;
                        System.out.println("Amount credited successfully!");
                    } else {
                        System.out.println("Invalid amount!");
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to debit: ₹");
                    double debitAmount = sc.nextDouble();
                    if (debitAmount > 0 && debitAmount <= loggedIn.balance) {
                        loggedIn.balance -= debitAmount;
                        System.out.println("Amount debited successfully!");
                    } else if (debitAmount > loggedIn.balance) {
                        System.out.println("Insufficient balance!");
                    } else {
                        System.out.println("Invalid amount!");
                    }
                    break;

                case 4:
                    System.out.println("Thank you for using the Banking System!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            System.out.println();

        } while (choice != 4);

        sc.close();
    }
}

