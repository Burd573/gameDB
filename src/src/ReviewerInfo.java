package src;

public class ReviewerInfo
{
    String gameName;
    String review;
    Double rating;

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public String getReview()
    {
        return review;
    }

    public void setReview(String review)
    {
        this.review = review;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public ReviewerInfo(String gameName, String review, Double rating)
    {
        this.gameName = gameName;
        this.review = review;
        this.rating = rating;
    }
}
