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

    //Returns the possible letters. For example:
    //If (numberOfVariables = 4) returns ["A", "B", "C", "D"]
    public static List<String> getVars() {
        List<String> vars = new ArrayList<>();
        for (int i = 0; i < numberOfVariables; i++)
            vars.add(Character.toString((char) (65 + i)));
        return vars;
    }

    //Replaces different bits with "-". For example:
    //replaceComplements("1010", "1000")  returns  "10-0"
    public static String replaceComplements(String binary1, String binary2) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary1.length(); i++)
            if (binary1.charAt(i) != binary2.charAt(i))
                result.append("-");
            else
                result.append(binary1.charAt(i));
        return String.valueOf(result);
    }

    //Returns true if two strings differ by only one bit
    public static boolean isGrey(String binary1, String binary2) {
        int count = 0;
        for (int i = 0; i < binary1.length(); i++)
            if (binary1.charAt(i) != binary2.charAt(i))
                count++;
        return (count == 1);
    }

    //Returns the binary value of a number containing left zeros.
    //For example, if numberOfVariables = 5
    //getBinary(3)  returns  "00011"
    public static String getBinary(int mintermNumber) {
        StringBuilder bin = new StringBuilder();
        bin.append(Integer.toBinaryString(mintermNumber));
        int firstLength = bin.length();
        for (int i = 0; i < numberOfVariables - firstLength; i++)
            bin.insert(0, "0");
        return String.valueOf(bin);
    }
}