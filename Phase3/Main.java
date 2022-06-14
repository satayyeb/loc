import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main
{
    public static void main(String[] args)
    {
        Set<Comment> comments = new HashSet<>();
        Set<Comment> commentsTest = new HashSet<>();
        HashMap<String, Word> words = new HashMap<>();
        SortedMap<Double, Boolean> results = new TreeMap<>();

        readComments(comments, commentsTest);

        analyzeWords(comments, words);

        printWords(words);



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

    private static void printWords(HashMap<String, Word> words) {
        for (Word value : words.values()) {
            System.out.println(value.getText() +
                    "\tBad: "+ value.getBadCount() +
                    "\tGood: " + value.getGoodCount() + " "+
                    "\tp: " + value.getGoodProb()+
                    "\tpw: " + value.getgoodProbWeighted() +
                    "\tw: " + value.getWeight());
        }
    }

    public static void readComments(Set<Comment> comments, Set<Comment> commentsTest){
        String line = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("comments.csv"));
            int count = 0;
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String text = line.substring(0,line.length()-1).
                        replaceAll("[\'\",.!?]", "").strip();
                if (count <= 700){
                    comments.add(new Comment(text, line.charAt(line.length() - 1) == '1'));
                } else{

                    commentsTest.add(new Comment(text, line.charAt(line.length() - 1) == '1'));
                }
                count++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public static void analyzeWords(Set<Comment> comments, HashMap<String, Word> words){
        for (Comment comment : comments) {
            for (String word : comment.getComment().split(" ")) {
                if (!words.containsKey(word)){
                    Word word1 = new Word(word);
                    word1.add(comment.getPolarity());
                    words.put(word, word1);

                } else{
                    words.get(word).add(comment.getPolarity());
                }
            }
        }
    }
}  