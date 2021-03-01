package src;

public class PublisherInfo
{
    String gameName;
    Double avgReview;

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public Double getAvgReview()
    {
        return avgReview;
    }

    public void setAvgReview(Double avgReview)
    {
        this.avgReview = avgReview;
    }

    public PublisherInfo(String gameName, Double avgReview)
    {
        this.gameName = gameName;
        this.avgReview = avgReview;
    }
}
