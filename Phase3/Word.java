import java.util.Objects;

public class Word {
    private String text;
    private int count;
    private int badCount;
    private int goodCount;

    public double getGoodProb() {
        if (goodCount == 0){
            return 1.0/(count+1);
        }
        return goodCount*1.0/count;
    }

    public double getBadProb() {
        return badCount*1.0/count;
    }

    public Word(String text) {
        this.text = text;
    }

    public void add(boolean isGood){
        if (isGood){
            goodCount++;
            count++;
        } else{
            badCount++;
            count++;
        }
    }

    public String getText() {
        return text;
    }

    public int getCount() {
        return count;
    }

    public int getBadCount() {
        return badCount;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public double getgoodProbWeighted(){
        return getGoodProb() * getWeight();
    }

    public int getWeight(){
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return text.equals(word.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
