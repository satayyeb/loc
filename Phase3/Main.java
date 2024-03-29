import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws CsvValidationException, IOException {

        double rate = 1;
        CSVReader csvReader = new CSVReader(new FileReader("comments.csv"));
        String[][] dataSet = readFile(700, csvReader);
        HashMap<String, WordPossibility> wordsPossibility = calculateWordPossibility(dataSet, rate);

        ArrayList<CommentPossibility> commentPossibilities = calculateCommentPossibility(dataSet);
        //calculate Pz and Po

        double Pz = 0;
        for (String[] str : dataSet)
            if (str[1].equals("0"))
                Pz++;
        Pz = Pz / 700;
        double Po = 1 - Pz;

        //read tests
        String[][] testSet = readFile(191, csvReader);
        ArrayList<CommentPossibility> testCommentPossibilities = calculateCommentPossibility(testSet);

        //debug:
        //show the last test
//        System.out.println(testSet[190][0]);
//        System.exit(0);


        for (String s : wordsPossibility.keySet()) {
            System.out.println(s + " :" + wordsPossibility.get(s).P);
        }

        for (CommentPossibility commentPossibility : commentPossibilities) {
            commentPossibility.calculateProbability(wordsPossibility, Pz, Po, 0);
//            System.out.println(commentPossibility);
        }

        int rightGuesses = 0;
        for (CommentPossibility commentPossibility : commentPossibilities) {
            if (commentPossibility.isPolarity() == commentPossibility.isPredictedPolarity()){
                rightGuesses++;
            }
        }
        System.out.println("train right Guesses: " +  rightGuesses);
        System.out.println("train percentage: " + rightGuesses/ 700.0);


        double minP = 0.000025;
        for (CommentPossibility testCommentPossibility : testCommentPossibilities) {
            testCommentPossibility.calculateProbability(wordsPossibility, Pz, Po, minP);
        }

        rightGuesses = 0;
        for (CommentPossibility testCommentPossibility : testCommentPossibilities) {
            if (testCommentPossibility.isPolarity() == testCommentPossibility.isPredictedPolarity()){
                rightGuesses++;
            }
        }
        System.out.println("minP: " + minP);
        System.out.println("test right guesses: " + rightGuesses);
        System.out.println("test percentage: " + rightGuesses/191.0);
        minP*=0.9;


//        Set<Comment> comments = new HashSet<>();
//        Set<Comment> commentsTest = new HashSet<>();
//        HashMap<String, Word> words = new HashMap<>();
//        SortedMap<Double, Boolean> results = new TreeMap<>();
//
//        readComments(comments, commentsTest);
//
//        analyzeWords(comments, words);
//
//        printWords(words);
//

//
//        for (Comment comment : commentsTest) {
//            double prob = 0;
//            int knownWordsWeight = 0;
//            System.out.println("comment: " + comment.getComment());
//            for (String word : comment.getComment().split(" ")) {
//                if (words.containsKey(word)){
//
//                    Word thisWord = words.get(word);
//                    knownWordsWeight += thisWord.getWeight();
//
//                    if (thisWord.getCount() > 8 &&
//                            (thisWord.getGoodProb() > .8 || thisWord.getGoodProb() < 0.2)){
//                        prob += thisWord.getgoodProbWeighted();
//                        System.out.println(word + ":  wp:" + thisWord.getgoodProbWeighted() +
//                                " w:" + thisWord.getWeight() +
//                                " :p" + thisWord.getGoodProb());
//                    }
//                }
//            }
//
//            prob /= knownWordsWeight * 1.0;
//            System.out.println(prob + " --- " + comment.getPolarity());
//            System.out.println("--------------------------------------------------------------------");
//            comment.setProb(prob);
//            results.put(prob, comment.getPolarity());
//        }
//
//        for (Double aDouble : results.keySet()) {
//            System.out.println(aDouble + "->" + results.get(aDouble));
//        }

//        System.out.println("size" + results.size());
//        double threshold = 1;
//        for (int i = 0; i < 20; i++) {
//            int goodGuessesCount = 0;
//            int count =0;
//            for (Double aDouble : results.keySet()) {
//                    count++;
////                System.out.println("porb:" + aDouble + "  polarity:" + results.get(aDouble).toString());
////                System.out.println(aDouble + ">" + results.get(aDouble));
//                if (aDouble < threshold && results.get(aDouble).equals(Boolean.TRUE)){
//                    goodGuessesCount++;
//                }
//            }
//            System.out.println("count: " + count);
//            System.out.println(goodGuessesCount);
//            System.out.println("threshold: " + threshold + " : " + goodGuessesCount*1.0/190);
//            threshold *= 0.9;
//        }

    }


    private static ArrayList<CommentPossibility> calculateCommentPossibility(String[][] dataSet) {

        ArrayList<CommentPossibility> commentPossibilities = new ArrayList<>();
        for (String[] strings : dataSet) {
            commentPossibilities.add(new CommentPossibility(strings[0], strings[1]));
        }

        return commentPossibilities;
    }

    private static String[][] readFile(int numOfRead, CSVReader csvReader) throws IOException, CsvValidationException {
        String[][] dataSet = new String[numOfRead][2];
        for (int i = 0; i < numOfRead; i++) {
            String[] line = csvReader.readNext();
            String review = line[0];
            //remove extra characters
            review = review.replaceAll("[^A-Za-z0-9]+", " ");
            //keep important short words
            review = review.replaceAll("bad", "badddd");
            //remove words of length less than 4 from string
            review = review.replaceAll("\\b\\w{1,2}\\b\\s?", "");
            //remove first space
            review = review.replaceFirst("^ +", "");
            dataSet[i][0] = review;
            dataSet[i][1] = line[1];
        }
        return dataSet;
    }

    private static HashMap<String, WordPossibility> calculateWordPossibility(String[][] dataSet, double rate) {
        //serialize all words in a single string
        StringBuilder wordsSeriesBuilder = new StringBuilder();
        for (String[] review : dataSet)
            wordsSeriesBuilder.append(review[0]).append(" ");
        String wordsSeries = String.valueOf(wordsSeriesBuilder);

        double numberOfWords = wordsSeries.replaceAll("^ ", "").length();
        HashMap<String, WordPossibility> wordsP = new HashMap<>();
        for (String word : wordsSeries.split(" ")) {
            if (wordsP.get(word) == null) {
                Matcher matcher = Pattern.compile(word).matcher(wordsSeries);
                int count = 0;
                while (matcher.find())
                    count++;
                double P = count * rate / numberOfWords;

                double PandO = 0;
                double PandZ = 0;
                double numOfOnes = 0;
                for (String[] comment : dataSet) {
                    if (comment[1].equals("1")) {
                        numOfOnes++;
                        if (comment[0].contains(word))
                            PandO++;
                    } else {
                        if (comment[0].contains(word))
                            PandZ++;
                    }
                }
                double PconO = PandO / numOfOnes;
                double PconZ = PandZ / (700 - numOfOnes);
                PandO /= 700;
                PandZ /= 700;

                wordsP.put(word, new WordPossibility(P, PandZ, PandO, PconZ, PconO));
            }
        }
        return wordsP;
    }
//
//    private static void printWords(HashMap<String, Word> words) {
//        for (Word value : words.values()) {
//            System.out.println(value.getText() +
//                    "\tBad: " + value.getBadCount() +
//                    "\tGood: " + value.getGoodCount() + " " +
//                    "\tp: " + value.getGoodProb() +
//                    "\tpw: " + value.getgoodProbWeighted() +
//                    "\tw: " + value.getWeight());
//        }
//    }
//
//    public static void readComments(Set<Comment> comments, Set<Comment> commentsTest) {
//        String line = "";
//        try {
//            BufferedReader br = new BufferedReader(new FileReader("comments.csv"));
//            int count = 0;
//            while ((line = br.readLine()) != null)   //returns a Boolean value
//            {
//                String text = line.substring(0, line.length() - 1).
//                        replaceAll("[\'\",.!?]", "").strip();
//                if (count <= 700) {
//                    comments.add(new Comment(text, line.charAt(line.length() - 1) == '1'));
//                } else {
//
//                    commentsTest.add(new Comment(text, line.charAt(line.length() - 1) == '1'));
//                }
//                count++;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    public static void analyzeWords(Set<Comment> comments, HashMap<String, Word> words) {
//        for (Comment comment : comments) {
//            for (String word : comment.getComment().split(" ")) {
//                if (!words.containsKey(word)) {
//                    Word word1 = new Word(word);
//                    word1.add(comment.getPolarity());
//                    words.put(word, word1);
//
//                } else {
//                    words.get(word).add(comment.getPolarity());
//                }
//            }
//        }
//    }

}  