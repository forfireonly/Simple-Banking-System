package banking;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    Scanner scn = new Scanner(System.in);
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static String fileName = "";

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean selectAll(String cardNumberEntered, String pinNumberEntered){
        String sql = "SELECT number, pin FROM card";
        boolean isExist = false;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if (rs.getString("number").equals(cardNumberEntered) &&
                        rs.getString("pin").equals(pinNumberEntered)) {
                    System.out.println();
                    System.out.println("You have successfully logged in!");
                    System.out.println();
                    isExist = true;
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return isExist;
    }

    public void interactUserAccount() throws IOException {
        System.out.println();
        System.out.println("Enter your card number:");
        String cardNumberEntered = reader.readLine();
        System.out.println("Enter your PIN:");
        String pinNumberEntered = reader.readLine();

        String contentOfFile = readFromAccountFile();

        String[] accounts = contentOfFile.split("#");

        double balance = 0.0;

        boolean isAccount = false;
        int userChoice = 0;

        isAccount = selectAll(cardNumberEntered, pinNumberEntered);

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

            Menu.displayMenuAccount();

            while (true){
                userChoice = Integer.parseInt(reader.readLine());

                switch (userChoice) {
                    case 1:
                        System.out.println();
                        System.out.println("Balance: " + (int) balance);
                        System.out.println();
                        Menu.displayMenuAccount();
                        break;
                    case 2:
                        System.out.println();
                        System.out.println("You have successfully logged out!");
                        System.out.println();
                        Menu.displayMenu();
                        break;
                    case 0:
                        System.out.println();
                        System.out.println("Bye!");
                        System.exit(0);
                        break;
                    default:
                }
            }

        } else {
            System.out.println();
            System.out.println("Wrong card number or PIN!");
        }
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


    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:" + fileName;

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER,\n"
                + "	number TEXT,\n"
                + "	pin TEXT,\n"
                + "	balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       /* try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {

                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/
    }




    public static void main(String[] args) throws IOException {

        if (args.length > 0 && args[0].equals("-fileName")) {
            fileName = args[1];
        }
        createNewDatabase(fileName);
        Main bankingSystem = new Main();
        bankingSystem.interactWithUser();

    }
}
