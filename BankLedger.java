import java.util.Scanner;

public class BankLedger {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String name;
        double balance = 0;
        int choice;

        System.out.println("===== PERSONAL BANK LEDGER =====");

        System.out.print("Enter Account Holder Name: ");
        name = sc.nextLine();

        System.out.println("Welcome, " + name);

        do {
            System.out.println("\n----- MENU -----");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            if (choice == 1) {

                System.out.print("Enter deposit amount: ");
                double amount = sc.nextDouble();

                if (amount > 0) {
                    balance = balance + amount;
                    System.out.println("Amount deposited successfully.");
                } else {
                    System.out.println("Invalid amount.");
                }

            } else if (choice == 2) {

                System.out.print("Enter withdrawal amount: ");
                double amount = sc.nextDouble();

                if (amount > 0 && amount <= balance) {
                    balance = balance - amount;
                    System.out.println("Amount withdrawn successfully.");
                } else {
                    System.out.println("Insufficient balance or invalid amount.");
                }

            } else if (choice == 3) {

                System.out.println("Current Balance: ₹" + balance);

            } else if (choice == 4) {

                System.out.println("Thank you for using Bank Ledger.");

            } else {

                System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }
}