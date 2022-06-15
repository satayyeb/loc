import java.util.HashMap;

public class CommentPossibility {
    private String text;
    private boolean polarity;
    private double PZR;
    private double POR;
    private double PRZ;
    private double PRO;
    private double PR;
    private boolean predictedPolarity;

    public CommentPossibility(String text, String polarity) {
        this.text = text;
        this.polarity = polarity.equals("1");
    }

    public void calculateProbability(HashMap<String, WordPossibility> wordsPossibility, double PZ, double PO, double minP) {
        PRZ = 1;
        PRO = 1;
        PR = 1;
        for (String word : text.split(" ")) {
            if (wordsPossibility.containsKey(word) && wordsPossibility.get(word).P > minP) {
                PRZ *= wordsPossibility.get(word).PconZ;
                PRO *= wordsPossibility.get(word).PconO;
                PR *= wordsPossibility.get(word).P;
            }
        }
        PZR = PRZ * PZ / PR;
        POR = PRO * PO / PR;
        predictedPolarity = (POR > PZR);
    }


    @Override
    public String toString() {
        return "CommentPossibility{" +
                "text='" + text + '\'' +
                ", polarity='" + polarity + '\'' +
                ", predictedPolarity=" + predictedPolarity +
                ", PZR=" + PZR +
                ", POR=" + POR +
                ", PRZ=" + PRZ +
                ", PRO=" + PRO +
                ", PR=" + PR +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPolarity() {
        return polarity;
    }

    public void setPolarity(boolean polarity) {
        this.polarity = polarity;
    }

    public double getPR() {
        return PR;
    }

    public void setPR(double PR) {
        this.PR = PR;
    }

    public boolean isPredictedPolarity() {
        return predictedPolarity;
    }

    public void setPredictedPolarity(boolean predictedPolarity) {
        this.predictedPolarity = predictedPolarity;
    }

    public double getPZR() {
        return PZR;
    }

    public void setPZR(double PZR) {
        this.PZR = PZR;
    }

    public double getPOR() {
        return POR;
    }

    public void setPOR(double POR) {
        this.POR = POR;
    }

    public double getPRZ() {
        return PRZ;
    }

    public void setPRZ(double PRZ) {
        this.PRZ = PRZ;
    }

    public double getPRO() {
        return PRO;
    }

    public void setPRO(double PRO) {
        this.PRO = PRO;
    }
}