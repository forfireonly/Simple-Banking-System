package banking;

import java.io.*;
import java.util.Random;

public class Account {


    private static class Card {

        String cardNumber = "400000";
        String pinNumber;
        File file = new File("account.txt");
        //FileWriter writer = new FileWriter(file);

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

        String lastDigit = "0";

        public void generateCard() throws IOException {
            cardNumber = cardNumber + generateAccount() + lastDigit;
            pinNumber = generatePin();
            System.out.printf("Your card has been created \nYour card number: \n%s \nYour card PIN: \n%s\n", cardNumber, pinNumber);

            System.out.println();

            //check if file empty and write in an account information, otherwise append
            BufferedReader br = new BufferedReader(new FileReader("account.txt"));

            double balance = 0;
            writeIntoFile(cardNumber, pinNumber, balance);
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
