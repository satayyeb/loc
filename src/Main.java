import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    private static int numberOfVariables;

    public static void main(String[] args) {
        //scan the input data
        Scanner scanner = new Scanner(System.in);
        numberOfVariables = Integer.parseInt(scanner.nextLine());
        String inputString = scanner.nextLine();

        //extract numbers form input data
        List<Integer> mintermNumbers = new ArrayList<>();
        for (String string : inputString.split(","))
            mintermNumbers.add(Integer.parseInt(string));

        //start the algorithm
        //TODO
    }

    public static boolean isGrey(String binary1, String binary2) {
        int count = 0;
        for (int i = 0; i < binary1.length(); i++)
            if (binary1.charAt(i) != binary2.charAt(i))
                count++;
        return (count == 1);
    }

    public static String getBinary(int mintermNumber) {
        StringBuilder bin = new StringBuilder();
        bin.append(Integer.toBinaryString(mintermNumber));
        int firstLength = bin.length();
        for (int i = 0; i < numberOfVariables - firstLength; i++)
            bin.insert(0, "0");
        return String.valueOf(bin);
    }
}