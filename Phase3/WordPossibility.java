public class WordPossibility {
    protected double P;
    protected double PandZ;
    protected double PandO;
    protected double PconZ;
    protected double PconO;

    public WordPossibility(double P, double PandZ, double PandO, double PconZ, double PconO) {
        this.P = P;
        this.PandZ = PandZ;
        this.PandO = PandO;
        this.PconZ = PconZ;
        this.PconO = PconO;
    }

    @Override
    public String toString() {
        return "WordPossibility{" +
                "P=" + P +
                ", PandZ=" + PandZ +
                ", PandO=" + PandO +
                ", PconZ=" + PconZ +
                ", PconO=" + PconO +
                '}';
    }
}
