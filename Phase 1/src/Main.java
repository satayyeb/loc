import java.math.BigInteger;
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

        mintermBinaries = simplifymore(mintermBinaries);

        List<String> minterms = new ArrayList<>();
        for (String binary : mintermBinaries)
            minterms.add(getMinterm(binary));
        return minterms;
    }

    private static Set<ArrayList<Integer>> getEssentials(HashSet<Integer> mins, Set<ArrayList<Integer>> binaries) {
        Set<ArrayList<Integer>> res = new HashSet<>();
        for (Integer min : mins) {
            int count = 0;
            for (ArrayList<Integer> binary : binaries) {
                if (binary.contains(min)) count++;
            }
            if (count == 1) {
                for (ArrayList<Integer> binary : binaries) {
                    if (binary.contains(min)) res.add(binary);
                }
            }
        }
        return res;
    }

    private static void removeEssentials(HashSet<Integer> mins, ArrayList<ArrayList<Integer>> binaries, ArrayList<ArrayList<Integer>> essentials) {
        for (ArrayList<Integer> essential : essentials) {
            for (Integer integer : essential) {
                if (mins.contains(integer)) mins.remove(integer);
            }
        }

        binaries.removeAll(essentials);
    }


    private static ArrayList<String> simplifymore(List<String> mintermBinaries) {
        ArrayList<ArrayList<Integer>> minterms = new ArrayList<>();
        Set<ArrayList<Integer>> mintersIntegerSet = new HashSet<>();

        for (String mintermBinary : mintermBinaries) {
            ArrayList<Integer> thisBinaryMinterms = getBinaryMinterms(mintermBinary);
            minterms.add(thisBinaryMinterms);
        }

        mintersIntegerSet.addAll(minterms);
        HashSet<Integer> neededMintermsUniqe = getNonRepetitiveMinterNumbers(minterms);
        Set<ArrayList<Integer>> essentialTerms = getEssentials(neededMintermsUniqe, mintersIntegerSet);
        // remove essentials
        mintersIntegerSet.removeAll(essentialTerms);
        Set<Set<ArrayList<Integer>>> all_possible_sets = powerSet(mintersIntegerSet);

        Set<ArrayList<Integer>> bestAns = new HashSet<>();
        for (Set<ArrayList<Integer>> all_possible_set : all_possible_sets) {
            HashSet<ArrayList<Integer>> a = new HashSet<>();
            a.addAll(all_possible_set);
            a.addAll(essentialTerms);
            if (isValid(a, neededMintermsUniqe)) {
                if (all_possible_set.size() < bestAns.size() || bestAns.size() == 0) {
                    bestAns = all_possible_set;
                }
            }
        }
        bestAns.addAll(essentialTerms);
        ArrayList<String> ansStr = new ArrayList<>();
        for (ArrayList<Integer> bestAn : bestAns) {
            for (String mintermBinary : mintermBinaries) {
                if (getBinaryMinterms(mintermBinary).containsAll(bestAn)) {
                    ansStr.add(mintermBinary);
                }
            }
        }
        return ansStr;

    }

    private static boolean isValid(Set<ArrayList<Integer>> terms, Set<Integer> neededMinters) {
        for (Integer neededMinter : neededMinters) {
            boolean has = false;
            for (ArrayList<Integer> term : terms) {
                if (term.contains(neededMinter)) {
                    has = true;
                    break;
                }
            }
            if (has == false) return false;
        }
        return true;
    }

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    private static HashSet<Integer> getNonRepetitiveMinterNumbers(ArrayList<ArrayList<Integer>> minterms) {
        HashSet a = new HashSet();
        for (ArrayList<Integer> minterm : minterms) {
            a.addAll(minterm);
        }
        return a;
    }

    private static ArrayList<Integer> getAllnumbers(ArrayList<ArrayList<Integer>> a, int not) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (i == not) continue;
            res.addAll(a.get(i));
        }
        return res;
    }


    private static ArrayList<Integer> getBinaryMinterms(String binary) {
        ArrayList<Integer> minterms = new ArrayList<>();
        minterms.add(0);
        for (int i = 0; i < binary.length(); i++) {
            char c = binary.charAt(binary.length() - 1 - i);
            if (c == '1') {
                for (int i1 = 0; i1 < minterms.size(); i1++) {
                    minterms.set(i1, minterms.get(i1) + (1 << i));
                }
            }
            if (c == '-') {
                ArrayList<Integer> p = new ArrayList<>(minterms);
                for (int i1 = 0; i1 < minterms.size(); i1++) {
                    minterms.set(i1, minterms.get(i1) + (1 << i));
                }
                minterms.addAll(p);
            }
        }
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
        if (minterms.size() == 1 && minterms.get(0).equals("")) System.out.print("1");
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