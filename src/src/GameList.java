package src;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameList
{
    private String name;
    private String genre;
    private Integer releaseYear;
    private String publisher;
    private Double avgReview;


    public GameList(String name, String genre, Integer releaseYear, String publisher, Double avgReview)
    {
        this.name = name;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.publisher = publisher;
        this.avgReview = avgReview;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public Integer getReleaseYear()
    {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear)
    {
        this.releaseYear = releaseYear;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    public Double getAvgReview()
    {
        return avgReview;
    }

    public void setAvgReview(Double avgReview)
    {
        this.avgReview = avgReview;
    }
}
