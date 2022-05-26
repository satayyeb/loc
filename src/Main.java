import java.util.*;

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

        //Run the algorithm and print results
        printSOP(getSimplifiedSOP(mintermNumbers));
    }

    //Returns a list of simplified minterms
    private static List<String> getSimplifiedSOP(List<Integer> mintermNumbers) {
        //convert the minterms to binary form
        List<String> mintermBinaries = new ArrayList<>();
        for (int number : mintermNumbers)
            mintermBinaries.add(getBinary(number));

        //loop over the main algorithm
        while (!algorithm(mintermBinaries).containsAll(mintermBinaries))
            mintermBinaries = algorithm(mintermBinaries);

        //convert from binary to letter form
        List<String> minterms = new ArrayList<>();
        for (String binary : mintermBinaries)
            minterms.add(getMinterm(binary));
        return minterms;
    }

    //The main algorithm
    private static List<String> algorithm(List<String> mintermBinaries) {
        List<String> newMinterms = new ArrayList<>();
        int size = mintermBinaries.size();
        boolean[] selected = new boolean[size];
        //find all Grey Codes
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (isGrey(mintermBinaries.get(i), mintermBinaries.get(j))) {
                    selected[i] = selected[j] = true;
                    String newBinary = replaceComplements(mintermBinaries.get(i), mintermBinaries.get(j));
                    if (!newMinterms.contains(newBinary))
                        newMinterms.add(newBinary);
                }
            }
        }
        //add all not selected minterms
        for (int i = 0; i < size; i++)
            if (!selected[i])
                newMinterms.add(mintermBinaries.get(i));
        return newMinterms;
    }

    //Print the results in the terminal
    private static void printSOP(List<String> minterms) {
        boolean firstFlag = true;
        for (String minterm : minterms) {
            if (!firstFlag)
                System.out.print(" + ");
            else
                firstFlag = false;
            System.out.print(minterm);
        }
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

    //returns the minterm. For example:
    //getMinterm("101")  returns  "AB'C"
    public static String getMinterm(String binary) {
        List<String> letters = getVars();
        StringBuilder minterm = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            if (binary.charAt(i) != '-') {
                minterm.append(letters.get(i));
                if (binary.charAt(i) == '0')
                    minterm.append("'");
            }
        }
        return String.valueOf(minterm);
    }
}