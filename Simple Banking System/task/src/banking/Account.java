package banking;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Account {




    private static class Card {

        String cardNumber = "400000";
        String pinNumber;
        File file = new File("account.txt");
        int id = 0;

        private Card() throws IOException {
        }

        public String generateAccount() {
            Random random = new Random();
            StringBuilder stringBulder = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                stringBulder.append(random.nextInt(10));
            }
            return String.valueOf(stringBulder);
        }

        public String generatePin() {
            Random random = new Random();
            StringBuilder stringBulder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                stringBulder.append(random.nextInt(10));
            }
            return String.valueOf(stringBulder);
        }

        public String generateAccountLuhn(){
            id =id + 1;
            int sum = 0;
            String account = generateAccount();
            String[] accountString = account.split("");
            int[] accountInt = new int[accountString.length + 6];
            accountInt[0] = 4;
            accountInt[1] = 0;
            accountInt[2] = 0;
            accountInt[3] = 0;
            accountInt[4] = 0;
            accountInt[5] = 0;
            for (int i = 0; i < accountString.length; i++) {
                accountInt[i + 6] = Integer.parseInt(accountString[i]);
            }
            //System.out.println(Arrays.toString(accountInt));
            for (int i = 0; i < accountInt.length; i+=2) {
                accountInt[i] = accountInt[i] * 2;

                if (accountInt[i] > 9) {
                    accountInt[i] = accountInt[i] - 9;
                }

                //sum = sum + accountInt[i];
            }

            for (int each : accountInt) {
                sum = sum + each;
            }

            int lastDigit = 0;
            if (sum % 10 != 0) {
                do {
                    lastDigit = lastDigit + 1;
                } while ((sum + lastDigit) % 10 != 0);
            }
            return account + lastDigit;
        }

        public void generateCard() throws IOException {
            cardNumber = cardNumber + generateAccountLuhn();
            pinNumber = generatePin();
            System.out.printf("Your card has been created \nYour card number: \n%s \nYour card PIN: \n%s\n", cardNumber, pinNumber);
            System.out.println();
                double balance = 0;
                writeIntoFile(cardNumber, pinNumber, balance);
                insert(id, cardNumber, pinNumber, (int) balance);

        }

        public void insert(int id, String number, String pin, int balance) {
            String sql = "INSERT INTO card(id,number,pin, balance) VALUES(?,?,?,?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, number);
                pstmt.setString(3, pin);
                pstmt.setInt(4, balance);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        private Connection connect() {
            // SQLite connection string
            String url = "jdbc:sqlite:" + Main.fileName;
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

        public void writeIntoFile(String cardNumber, String pinNumber, double balance) throws IOException {
            FileWriter writer = new FileWriter(file, true);
            try (PrintWriter printWriter = new PrintWriter(writer)) {
                BufferedReader br = new BufferedReader(new FileReader("account.txt"));
                if (br.readLine() == null) {
                    printWriter.print(cardNumber + " ");
                    printWriter.print(pinNumber + " ");
                    printWriter.println(balance + "#");

                } else {
                    CharSequence account = cardNumber + " " + pinNumber + " " + balance + "#" + "\n";
                    printWriter.append(account);
                    printWriter.flush();
                }
            }
        }
    }
        void displayCard() throws IOException {
            Card newCard = new Card();
            newCard.generateCard();
        }
}
