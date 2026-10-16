import java.util.Scanner;

public class PersonalBankLedger {
    static double balance = 0;
    static String[] transactions = new String[100];
    static int count = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter account holder name: ");
        String name = sc.nextLine();

        int choice;

        do {
            System.out.println("\n--- PERSONAL BANK LEDGER ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Statement");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: ");
                    double deposit = sc.nextDouble();

                    if (deposit > 0 && count < 100) {
                        balance = balance + deposit;
                        transactions[count] = "Deposited: " + deposit;
                        count++;
                        System.out.println("Deposit successful.");
                    } else {
                        System.out.println("Invalid amount.");
                    }
                    break;

                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    double withdraw = sc.nextDouble();

                    if (withdraw > 0 && withdraw <= balance
                            && count < 100) {
                        balance = balance - withdraw;
                        transactions[count] = "Withdrawn: " + withdraw;
                        count++;
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Invalid amount or insufficient balance.");
                    }
                    break;

                case 3:
                    System.out.println("\nAccount Holder: " + name);
                    System.out.println("Transaction Statement:");

                    for (int i = 0; i < count; i++) {
                        System.out.println(transactions[i]);
                    }

                    System.out.printf("Current Balance: %.2f%n", balance);
                    break;

                case 4:
                    System.out.println("Thank you for using Personal Bank Ledger.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }
}

