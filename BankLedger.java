import java.io.*;
import java.util.*;
import java.util.stream.*;

class Transaction {
    private final String type;
    private final double amount;
    private final double balanceAfter;

    Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    String getType() { return type; }
    double getAmount() { return amount; }
    double getBalanceAfter() { return balanceAfter; }

    @Override
    public String toString() {
        return String.format("%-10s %10.2f %12.2f", type, amount, balanceAfter);
    }
}

// CO4: Custom exception for robust error handling
class InsufficientFundsException extends Exception {
    InsufficientFundsException(String message) { super(message); }
}

// CO4: Class modelling the account (objects, encapsulation)
class Account {
    private final String holder;
    private double balance;
    private final ArrayList<Transaction> history = new ArrayList<>(); // CO6

    Account(String holder) { this.holder = holder; }

    String getHolder() { return holder; }
    double getBalance() { return balance; }
    List<Transaction> getHistory() { return history; }

    void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
    }

    void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (amount > balance) throw new InsufficientFundsException("Insufficient balance.");
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, balance));
    }
}

public class BankLedger {

    // CO3: Method using a 1D array (average transaction amount)
    static double average(double[] amounts) {
        if (amounts.length == 0) return 0;
        double sum = 0;
        for (double a : amounts) sum += a;
        return sum / amounts.length;
    }

    // CO3: Recursion (compound interest year by year)
    static double compound(double principal, double rate, int years) {
        if (years == 0) return principal;
        return compound(principal * (1 + rate / 100), rate, years - 1);
    }

    // CO5: File I/O - write statement to a text file
    static void saveStatement(Account acc, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println(statementText(acc));
            System.out.println("Statement saved to " + fileName);
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    // CO5: StringBuilder for building the statement text
    static String statementText(Account acc) {
        StringBuilder sb = new StringBuilder();
        sb.append("Account Holder: ").append(acc.getHolder().toUpperCase()).append("\n");
        sb.append(String.format("%-10s %10s %12s%n", "TYPE", "AMOUNT", "BALANCE"));
        for (Transaction t : acc.getHistory())
            sb.append(t).append("\n");
        sb.append("Closing Balance: Rs. ").append(String.format("%.2f", acc.getBalance()));
        return sb.toString();
    }

    public static void main(String[] args) {
        // CO1: Variables, data types, Scanner input, operators
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        System.out.println("===== PERSONAL BANK LEDGER =====");
        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine().trim();           // CO5: String handling
        Account acc = new Account(name);              // CO4: object creation
        System.out.println("Welcome, " + name);

        // CO2: do-while loop with if-else / switch
        do {
            System.out.println("\n----- MENU -----");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. View Statement");
            System.out.println("5. Sorted Transactions (by amount)");
            System.out.println("6. Summary (Streams)");
            System.out.println("7. Save Statement to File");
            System.out.println("8. Interest Calculator");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            try {                                      // CO4: exception handling
                choice = Integer.parseInt(sc.nextLine().trim());

                if (choice == 1 || choice == 2) {      // CO2: compound condition
                    System.out.print("Enter amount: ");
                    double amount = Double.parseDouble(sc.nextLine().trim());
                    if (choice == 1) {
                        acc.deposit(amount);
                        System.out.println("Amount deposited successfully.");
                    } else {
                        acc.withdraw(amount);
                        System.out.println("Amount withdrawn successfully.");
                    }
                } else if (choice == 3) {
                    System.out.println("Current Balance: Rs. " + acc.getBalance());
                } else if (choice == 4) {
                    System.out.println(statementText(acc));
                } else if (choice == 5) {
                    // CO5: sorting with Comparator; CO6: lambda
                    List<Transaction> sorted = new ArrayList<>(acc.getHistory());
                    sorted.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
                    sorted.forEach(System.out::println);
                } else if (choice == 6) {
                    // CO6: Streams, lambda, Collectors, HashMap
                    double totalDep = acc.getHistory().stream()
                            .filter(t -> t.getType().equals("DEPOSIT"))
                            .mapToDouble(Transaction::getAmount).sum();
                    double totalWd = acc.getHistory().stream()
                            .filter(t -> t.getType().equals("WITHDRAW"))
                            .mapToDouble(Transaction::getAmount).sum();
                    Map<String, Long> count = acc.getHistory().stream()
                            .collect(Collectors.groupingBy(Transaction::getType, Collectors.counting()));
                    double[] amounts = acc.getHistory().stream()
                            .mapToDouble(Transaction::getAmount).toArray();   // CO3: array
                    System.out.println("Average amount : " + average(amounts));
                    System.out.println("Total Deposits : " + totalDep);
                    System.out.println("Total Withdrawals: " + totalWd);
                    System.out.println("Transaction count: " + count);
                    Optional<Transaction> biggest = acc.getHistory().stream()
                            .max(Comparator.comparingDouble(Transaction::getAmount));
                    System.out.println("Largest transaction: "
                            + biggest.map(Transaction::toString).orElse("None"));
                } else if (choice == 7) {
                    saveStatement(acc, "statement_" + name.replace(" ", "_") + ".txt");
                } else if (choice == 8) {
                    System.out.print("Enter principal, rate(%) and years: ");
                    double p = Double.parseDouble(sc.nextLine().trim());
                    double r = Double.parseDouble(sc.nextLine().trim());
                    int y = Integer.parseInt(sc.nextLine().trim());
                    System.out.printf("Amount after %d years: Rs. %.2f%n", y, compound(p, r, y));
                } else if (choice == 9) {
                    System.out.println("Thank you for using Bank Ledger.");
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (InsufficientFundsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a valid number.");
                choice = 0;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 9);

        sc.close();
    }
}
