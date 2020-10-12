package banking;

public class Menu {

    public static void displayMenu() {
        System.out.println("1. Create an account \n2. Log into account \n0. Exit");
    }

    public static void displayMenuAccount() {
        System.out.println("1. Balance\n" +
                "2. Log out\n" +
                "0. Exit");
    }

    public static void displayExtendedMenuAccount() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit");
    }
}
