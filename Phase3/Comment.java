public class Comment {
    private String comment;
    private Boolean polarity;
    private double prob;

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public Comment(String comment, Boolean polarity) {
        this.comment = comment;
        this.polarity = polarity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getPolarity() {
        return polarity;
    }

    public void setPolarity(Boolean polarity) {
        this.polarity = polarity;
    }
}
