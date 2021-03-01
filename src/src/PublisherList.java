package src;

public class PublisherList
{
     String name;
     String city;
     String state;
     String country;
     Double avgRating;

    public PublisherList(String name, String city, String state, String country, Double avgRating)
    {
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.avgRating = avgRating;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
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
