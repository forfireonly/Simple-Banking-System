import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    String[] firstBox = reader.readLine().split(" ");


    String[] secondBox = reader.readLine().split(" ");


    public Main() throws IOException {
        ////
    }

    public void fitBoxes() {
        int[] firstBoxNumbers = new int[3];
        int[] secondBoxNumbers = new int[3];

        for (int i = 0; i < 3; i++) {
            firstBoxNumbers[i] = Integer.parseInt(firstBox[i]);
            secondBoxNumbers[i] = Integer.parseInt(secondBox[i]);
        }
        Arrays.sort(firstBoxNumbers);
        Arrays.sort(secondBoxNumbers);
        if (firstBoxNumbers[0] > secondBoxNumbers[0] && firstBoxNumbers[1] > secondBoxNumbers[1] &&
            firstBoxNumbers[2] > secondBoxNumbers[2]) {
            System.out.println("Box 1 > Box 2");
        } else if (firstBoxNumbers[0] < secondBoxNumbers[0] && firstBoxNumbers[1] < secondBoxNumbers[1] &&
                firstBoxNumbers[2] < secondBoxNumbers[2]) {
            System.out.println("Box 1 < Box 2");
        } else {
            System.out.println("Incompatible");
        }
    }




    public static void main(String[] args) throws IOException {
        // write your code here
        Main main = new Main();
        main.fitBoxes();
    }
}