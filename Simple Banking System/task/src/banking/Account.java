package banking;

import java.io.*;
import java.util.Random;
import static banking.Main.dbWorker;

public class Account {

    private static class Card {

        String cardNumber = "400000";
        String pinNumber;
        File file = new File("account.txt");
        int id = 0;
        Integer balance = 0;

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
            for (int i = 0; i < accountInt.length; i+=2) {
                accountInt[i] = accountInt[i] * 2;
                if (accountInt[i] > 9) {
                    accountInt[i] = accountInt[i] - 9;
                }
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

                writeIntoFile(cardNumber, pinNumber, balance);
                dbWorker.insert(id, cardNumber, pinNumber, balance);

        }

        public void writeIntoFile(String cardNumber, String pinNumber, int balance) throws IOException {
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
