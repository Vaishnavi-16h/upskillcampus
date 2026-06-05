package PACKskill;

import java.util.*;

class Account 
{
    String name;
    String address;
    String phone;
    String password;
    int accountNumber;
    double balance;
    List<String> transactions = new ArrayList<>();

    public Account(String name, String address, String phone, String password, int accountNumber, double balance) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.accountNumber = accountNumber;
        this.balance = balance;

        transactions.add("Account created with balance: " + balance);
    }
}

public class BankingSystem 
{

    static Scanner sc = new Scanner(System.in);
    static List<Account> accounts = new ArrayList<>();
    static int accCounter = 1001;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Thank you!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // 🔹 1. Registration
    static void register() {
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();

        System.out.print("Set Password: ");
        String password = sc.nextLine();

        System.out.print("Initial Deposit: ");
        double balance = sc.nextDouble();

        int accNo = accCounter++;

        Account acc = new Account(name, address, phone, password, accNo, balance);
        accounts.add(acc);

        System.out.println("✅ Registration Successful!");
        System.out.println("Your Account Number: " + accNo);
    }

    // 🔹 2. Login
    static void login() {
        System.out.print("Enter Account Number: ");
        int accNo = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Account acc = findAccount(accNo);

        if (acc != null && acc.password.equals(password)) {
            System.out.println("✅ Login Successful!");
            userMenu(acc);
        } else {
            System.out.println("❌ Invalid Credentials!");
        }
    }

    // 🔹 User Menu
    static void userMenu(Account acc) {
        while (true) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. View Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Statement");
            System.out.println("6. Logout");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> viewAccount(acc);
                case 2 -> deposit(acc);
                case 3 -> withdraw(acc);
                case 4 -> transfer(acc);
                case 5 -> statement(acc);
                case 6 -> {
                    System.out.println("Logged out!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // 🔹 View Account
    static void viewAccount(Account acc) {
        System.out.println("\n--- Account Details ---");
        System.out.println("Name: " + acc.name);
        System.out.println("Address: " + acc.address);
        System.out.println("Phone: " + acc.phone);
        System.out.println("Balance: " + acc.balance);
    }

    // 🔹 Deposit
    static void deposit(Account acc) {
        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();

        if (amt <= 0) {
            System.out.println("❌ Invalid amount!");
            return;
        }

        acc.balance += amt;
        acc.transactions.add("Deposited: " + amt + " | Balance: " + acc.balance);

        System.out.println("✅ Deposit Successful!");
    }

    // 🔹 Withdraw
    static void withdraw(Account acc) {
        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();

        if (amt > acc.balance) {
            System.out.println("❌ Insufficient Balance!");
            return;
        }

        acc.balance -= amt;
        acc.transactions.add("Withdrawn: " + amt + " | Balance: " + acc.balance);

        System.out.println("✅ Withdrawal Successful!");
    }

    // 🔹 Transfer
    static void transfer(Account sender) {
        System.out.print("Enter Receiver Account No: ");
        int recAccNo = sc.nextInt();

        Account receiver = findAccount(recAccNo);

        if (receiver == null) {
            System.out.println("❌ Receiver not found!");
            return;
        }

        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();

        if (amt > sender.balance) {
            System.out.println("❌ Insufficient Balance!");
            return;
        }

        sender.balance -= amt;
        receiver.balance += amt;

        sender.transactions.add("Transferred: " + amt + " to Acc " + recAccNo);
        receiver.transactions.add("Received: " + amt + " from Acc " + sender.accountNumber);

        System.out.println("✅ Transfer Successful!");
    }

    // 🔹 Statement
    static void statement(Account acc) {
        System.out.println("\n--- Transaction History ---");
        for (String t : acc.transactions) {
            System.out.println(t);
        }
    }

    // 🔹 Find Account
    static Account findAccount(int accNo) {
        for (Account acc : accounts) {
            if (acc.accountNumber == accNo)
                return acc;
        }
        return null;
    }
}
