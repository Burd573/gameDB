package src;

public class GameReviewsInfo
{
    private String reviewerName;
    private Double score;
    private String comment;

    public GameReviewsInfo(String reviewerName, Double score, String comment)
    {
        this.reviewerName = reviewerName;
        this.score = score;
        this.comment = comment;
    }

    public String getReviewerName()
    {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName)
    {
        this.reviewerName = reviewerName;
    }

    public Double getScore()
    {
        return score;
    }

    public void setScore(Double score)
    {
        this.score = score;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
