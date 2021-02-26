package src;

public class PublisherInfo
{
    String gameName;
    Double avgRating;

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public Double getAvgRating()
    {
        return avgRating;
    }

    public void setAvgRating(Double avgRating)
    {
        this.avgRating = avgRating;
    }

    public PublisherInfo(String gameName, Double avgRating)
    {
        this.gameName = gameName;
        this.avgRating = avgRating;
    }
}
