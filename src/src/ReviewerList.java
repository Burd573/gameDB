package src;

public class ReviewerList
{
    String name;
    Double avgRating;
    Integer numReviews;

    public ReviewerList(String name, Double avgRating, Integer numReviews)
    {
        this.name = name;
        this.avgRating = avgRating;
        this.numReviews = numReviews;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Double getAvgRating()
    {
        return avgRating;
    }

    public void setAvgRating(Double avgRating)
    {
        this.avgRating = avgRating;
    }

    public Integer getNumReviews()
    {
        return numReviews;
    }

    public void setNumReviews(Integer numReviews)
    {
        this.numReviews = numReviews;
    }
}
