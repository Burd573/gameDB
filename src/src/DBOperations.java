package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBOperations
{
    Statement stmt;
    ResultSet rs;
    PreparedStatement pstmt;
    Connection conn;

    public DBOperations(Connection conn)
    {
        this.conn = conn;
        stmt = null;
        rs = null;
        pstmt = null;
    }

    public List<GameInfo> getGames()
    {
        List<GameInfo> games = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT game.name, genre, release_year, publisher.name, AVG(rating)\n" + "FROM game\n" + "JOIN publisher\n" + "\tON game.pub_id = publisher.pub_id\n" + "LEFT JOIN review\n" + "\tON game.game_id = review.game_id\n" + "GROUP BY game.name");

            while (rs.next())
            {
                String gameName = rs.getString("name");
                String genre = rs.getString("genre");
                Integer relYear = rs.getInt("release_year");
                String pubName = rs.getString("name");
                Double avgRating = rs.getDouble("AVG(rating)");

                GameInfo game = new GameInfo(gameName, genre, relYear, pubName, avgRating);
                games.add(game);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return games;
    }

    public List<String> getPublisherNames()
    {
        List<String> publishers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM publisher;");

            while(rs.next())
            {
                String pubName = rs.getString("name");
                publishers.add(pubName);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return publishers;
    }

    public List<PublisherInfo> getPublishers()
    {
        List<PublisherInfo> publishers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT publisher.name, city, state, county, AVG(rating)\n" +
                    "FROM game\n" +
                    "JOIN publisher\n" +
                    "\tON game.pub_id = publisher.pub_id\n" +
                    "JOIN review\n" +
                    "\tON game.game_id = review.game_id\n" +
                    "GROUP BY publisher.name");
            while(rs.next())
            {
                String pubName = rs.getString("name");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String country = rs.getString("county");
                Double avgRating = rs.getDouble("AVG(rating)");

                PublisherInfo pub = new PublisherInfo(pubName, city, state, country, avgRating);
                publishers.add(pub);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return publishers;
    }



    public List<ReviewerInfo> getReviewers()
    {
        List<ReviewerInfo> reviewers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT reviewer.name, AVG(rating), COUNT(rating)\n" +
                    "FROM reviewer\n" +
                    "JOIN review\n" +
                    "\tON review.reviewer_id = reviewer.reviewer_id\n" +
                    "GROUP BY reviewer.reviewer_id");
            while(rs.next())
            {
                String reviewerName = rs.getString("name");
                Double avgRating = rs.getDouble("AVG(rating)");
                Integer numReviews = rs.getInt("COUNT(rating)");

                ReviewerInfo rev = new ReviewerInfo(reviewerName,avgRating,numReviews);
                reviewers.add(rev);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return reviewers;
    }

    public void addGame(String pubName, String genre, String gameName, String release_year)
    {
        int year = Integer.parseInt(release_year);
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("INSERT INTO game(pub_id, genre, name, release_year) VALUES((SELECT pub_id FROM publisher WHERE publisher.name = ?),?,?,?);");
            pstmt.setString(1,pubName);
            pstmt.setString(2,genre);
            pstmt.setString(3,gameName);
            pstmt.setInt(4,year);

            if (pstmt.executeUpdate() > 0)
            {
                System.out.println("SUCCESS");
            }

            conn.commit();

        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void close(Statement stmt, ResultSet rs, PreparedStatement pstmt)
    {
        try
        {
            if(pstmt != null)
            {
                pstmt.close();
            }
            if (rs != null)
            {
                rs.close();
            }
            if (stmt != null)
            {
                stmt.close();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
