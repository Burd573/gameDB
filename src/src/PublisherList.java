package src;

public class PublisherList {

    private String pubName;
    private String city;
    private String state;
    private String country;
    private Double avgRating;


    public PublisherList(String pubName, String city, String state, String country, Double avgRating)
    {
        this.pubName = pubName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.avgRating = avgRating;
    }

    public String getPubName()
    {
        return pubName;
    }

    public void setPubName(String pubName)
    {
        this.pubName = pubName;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Double getAvgRating()
    {
        return avgRating;
    }

    public void setAvgRating(Double avgRating)
    {
        this.avgRating = avgRating;
    }
}
