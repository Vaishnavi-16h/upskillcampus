package PACKskill;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Expense implements Serializable {
    int id;
    LocalDate date;
    double amount;
    String category;
    String description;

    public Expense(int id, LocalDate date, double amount, String category, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public String toString() {
        return id + " | " + date + " | ₹" + amount + " | " + category + " | " + description;
    }
}

public class ExpenseTracker {

    static List<Expense> expenses = new ArrayList<>();
    static Set<String> categories = new HashSet<>();
    static int idCounter = 1;

    static final String FILE_NAME = "expenses.dat";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n===== Expense Tracker =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Add Category");
            System.out.println("4. View Categories");
            System.out.println("5. Filter Expenses");
            System.out.println("6. Modify Expense");
            System.out.println("7. Delete Expense");
            System.out.println("8. Reports");
            System.out.println("9. Save & Exit");

            System.out.print("Choose option: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> addExpense();
                case 2 -> viewExpenses();
                case 3 -> addCategory();
                case 4 -> viewCategories();
                case 5 -> filterExpenses();
                case 6 -> modifyExpense();
                case 7 -> deleteExpense();
                case 8 -> generateReports();
                case 9 -> {
                    saveData();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ================= ADD EXPENSE =================
    static void addExpense() {
        try {
            System.out.print("Enter date (yyyy-mm-dd): ");
            LocalDate date = LocalDate.parse(sc.next());

            System.out.print("Enter amount: ");
            double amount = sc.nextDouble();
            sc.nextLine();

            System.out.print("Enter category: ");
            String category = sc.nextLine();
            categories.add(category);

            System.out.print("Enter description: ");
            String desc = sc.nextLine();

            expenses.add(new Expense(idCounter++, date, amount, category, desc));
            System.out.println("Expense added successfully!");

        } catch (Exception e) {
            System.out.println("Invalid input! Try again.");
            sc.nextLine();
        }
    }

    // ================= VIEW =================
    static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        expenses.forEach(System.out::println);
    }

    // ================= CATEGORY =================
    static void addCategory() {
        System.out.print("Enter new category: ");
        String cat = sc.next();
        categories.add(cat);
        System.out.println("Category added!");
    }

    static void viewCategories() {
        System.out.println("Categories:");
        categories.forEach(System.out::println);
    }

    // ================= FILTER =================
    static void filterExpenses() {
        System.out.println("1. By Category\n2. By Date Range");
        int ch = getIntInput();

        if (ch == 1) {
            System.out.print("Enter category: ");
            String cat = sc.next();

            expenses.stream()
                    .filter(e -> e.category.equalsIgnoreCase(cat))
                    .forEach(System.out::println);

        } else if (ch == 2) {
            try {
                System.out.print("Start date (yyyy-mm-dd): ");
                LocalDate start = LocalDate.parse(sc.next());

                System.out.print("End date: ");
                LocalDate end = LocalDate.parse(sc.next());

                expenses.stream()
                        .filter(e -> !e.date.isBefore(start) && !e.date.isAfter(end))
                        .forEach(System.out::println);

            } catch (Exception e) {
                System.out.println("Invalid date format!");
            }
        }
    }

    // ================= MODIFY =================
    static void modifyExpense() {
        System.out.print("Enter Expense ID to modify: ");
        int id = getIntInput();

        for (Expense e : expenses) {
            if (e.id == id) {
                try {
                    System.out.print("New amount: ");
                    e.amount = sc.nextDouble();
                    sc.nextLine();

                    System.out.print("New category: ");
                    e.category = sc.nextLine();

                    System.out.print("New description: ");
                    e.description = sc.nextLine();

                    System.out.println("Updated successfully!");
                    return;

                } catch (Exception ex) {
                    System.out.println("Invalid input!");
                }
            }
        }
        System.out.println("Expense not found.");
    }

    // ================= DELETE =================
    static void deleteExpense() {
        System.out.print("Enter Expense ID to delete: ");
        int id = getIntInput();

        expenses.removeIf(e -> e.id == id);
        System.out.println("Deleted (if ID existed).");
    }

    // ================= REPORTS =================
    static void generateReports() {
        System.out.println("1. Total Expense");
        System.out.println("2. Category-wise Report");

        int ch = getIntInput();

        if (ch == 1) {
            double total = expenses.stream().mapToDouble(e -> e.amount).sum();
            System.out.println("Total Spending: ₹" + total);

        } else if (ch == 2) {
            Map<String, Double> report = new HashMap<>();

            for (Expense e : expenses) {
                report.put(e.category,
                        report.getOrDefault(e.category, 0.0) + e.amount);
            }

            report.forEach((k, v) -> System.out.println(k + " : ₹" + v));
        }
    }

    // ================= FILE HANDLING =================
    static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(expenses);
            out.writeObject(categories);
        } catch (Exception e) {
            System.out.println("Error saving data!");
        }
    }

    static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            expenses = (List<Expense>) in.readObject();
            categories = (Set<String>) in.readObject();

            if (!expenses.isEmpty()) {
                idCounter = expenses.get(expenses.size() - 1).id + 1;
            }

        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
    }

    // ================= HELPER =================
    static int getIntInput() {
        try {
            return sc.nextInt();
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}
