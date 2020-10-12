package banking;

import java.io.*;
import java.util.Scanner;

public class Main {

    Scanner scn = new Scanner(System.in);
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static  DataBaseWorker dbWorker = new DataBaseWorker();

    static String fileName = "";


    public void interactUserAccount() throws IOException {

        System.out.println();
        System.out.println("Enter your card number:");
        String cardNumberEntered = reader.readLine();
        System.out.println("Enter your PIN:");
        String pinNumberEntered = reader.readLine();

        String contentOfFile = readFromAccountFile();

        String[] accounts = contentOfFile.split("#");



        boolean isAccount = false;
        int userChoice = 0;

        //DataBaseWorker dbWorker = new DataBaseWorker();
        dbWorker.setCardNumberEntered(cardNumberEntered);
        dbWorker.setPinNumberEntered(pinNumberEntered);



        isAccount = dbWorker.selectAll();

        /*for (String each : accounts ) {
            String[] separateAccounts = each.split(" ");
            if (separateAccounts[0].equals(cardNumberEntered) && separateAccounts[1].equals(pinNumberEntered)) {
                System.out.println();
                System.out.println("You have successfully logged in!");
                System.out.println();
                balance = Double.parseDouble(separateAccounts[2]);
                isAccount = true;
                break;
            }
        }*/
        if (isAccount) {

            Menu.displayExtendedMenuAccount();
            boolean isKeepGoing = true;
            while (isKeepGoing){
                userChoice = Integer.parseInt(reader.readLine());

                switch (userChoice) {
                    case 1:
                        System.out.println();

                        System.out.println("Balance: " + dbWorker.getBalanceFromDataBase());
                        System.out.println();
                        Menu.displayExtendedMenuAccount();
                        break;
                    case 2:

                        System.out.println();
                        System.out.println("Enter income: ");
                        int income = Integer.parseInt(reader.readLine());
                        dbWorker.update(income + dbWorker.getBalanceFromDataBase());
                        System.out.println();
                        System.out.println("Income was added!");
                        System.out.println();
                        Menu.displayExtendedMenuAccount();

                        break;
                    case 3:
                        int balance = dbWorker.getBalanceFromDataBase();
                        System.out.println();
                        System.out.println("Transfer\n" +
                                    "Enter card number: ");
                        String card = reader.readLine();
                        if (card.equals(cardNumberEntered)) {
                            System.out.println("You can't transfer money to the same account!");
                            System.out.println();
                            Menu.displayExtendedMenuAccount();
                            break;

                        }
                        if (!checkLuhnAlg(card)) {
                            System.out.println();
                            Menu.displayExtendedMenuAccount();
                            break;
                        }
                        if (!dbWorker.checkExistance(card)) {
                            System.out.println("Such a card does not exist.");
                            System.out.println();
                            Menu.displayExtendedMenuAccount();
                            break;
                        } else {
                                System.out.println("Enter how much money you want to transfer:");
                                int amountToTransfer = Integer.parseInt(reader.readLine());
                                if (amountToTransfer > balance) {
                                    System.out.println("Not enough money!");
                                    System.out.println();
                                    Menu.displayExtendedMenuAccount();
                                    break;
                                } else {
                                    dbWorker.update(balance - amountToTransfer);
                                    amountToTransfer = amountToTransfer + dbWorker.getBalanceCardTransfer(card);
                                    dbWorker.transfer(card, amountToTransfer);
                                    System.out.println("Success!");
                                    System.out.println();
                                    Menu.displayExtendedMenuAccount();
                                    break;
                                }
                            }
                    case 4:
                        dbWorker.delete();
                        System.out.println("The account has been closed!");
                        isKeepGoing = false;
                        System.out.println();
                        break;
                    case 5:
                        System.out.println();
                        System.out.println("You have successfully logged out!");
                        System.out.println();
                        isKeepGoing = false;
                        break;
                    case 0:
                        System.out.println();
                        System.out.println("Bye!");
                        System.out.println();
                        System.exit(0);
                        isKeepGoing = false;
                        break;
                    default:
                }
            }
        } else {
            System.out.println();
            System.out.println("Wrong card number or PIN!");
        }
    }

    public boolean checkLuhnAlg(String card) {
        boolean isValid = false;
        String[] cardNumbers = card.split("");
        int[] cardNumbersInt = new int[cardNumbers.length];
        for (int i = 0; i< cardNumbers.length; i++) {
            cardNumbersInt[i] = Integer.parseInt(cardNumbers[i]);
        }
        for (int i = 0; i < cardNumbers.length; i += 2) {
            cardNumbersInt[i] = cardNumbersInt[i] * 2;

            if (cardNumbersInt[i] > 9) {
                cardNumbersInt[i] = cardNumbersInt[i] - 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < cardNumbers.length - 1; i++) {
            sum = sum + cardNumbersInt[i];
        }
        int lastDigit = 0;
        if (sum % 10 != 0) {
            do {
                lastDigit = lastDigit + 1;
            } while ((sum + lastDigit) % 10 != 0);
        }
        if (lastDigit == cardNumbersInt[cardNumbers.length - 1]) {
            isValid = true;
        } else {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        }
        return isValid;
    }

    public String readFromAccountFile() {
        File file = new File("account.txt");
        StringBuilder myStringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                myStringBuilder.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + "account.txt");
        }
        return String.valueOf(myStringBuilder);
    }



    public void interactWithUser() throws IOException {

        System.out.println();
        Menu.displayMenu();
        int userChoice;
        boolean isKeepGoing = true;

        while (isKeepGoing) {

            userChoice = Integer.parseInt(reader.readLine());

            switch (userChoice) {
                case 1:
                    Account newAccount = new Account();
                    System.out.println();
                    newAccount.displayCard();
                    Menu.displayMenu();
                    break;
                case 2:
                    interactUserAccount();
                    System.out.println();
                    Menu.displayMenu();
                    break;
                case 0:
                    System.out.println();
                    System.out.println("Bye!");
                    System.out.println();
                    isKeepGoing = false;
                    break;
                default:
            }
        }

    }

    public static void main(String[] args) throws IOException {

        if (args.length > 0 && args[0].equals("-fileName")) {
            fileName = args[1];
        }

        Main bankingSystem = new Main();
        dbWorker.createNewDatabase(fileName);
        bankingSystem.interactWithUser();
    }
}
