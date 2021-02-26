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

    public List<GameList> getGames()
    {
        List<GameList> games = new ArrayList<>();
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

                GameList game = new GameList(gameName, genre, relYear, pubName, avgRating);
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

    public List<String> getPlatformNames()
    {
        List<String> platforms = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM platform;");

            while(rs.next())
            {
                String platformName = rs.getString("name");
                platforms.add(platformName);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return platforms;
    }

    public List<String> getReviewerNames()
    {
        List<String> reviewers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM reviewer;");

            while(rs.next())
            {
                String pubName = rs.getString("name");
                reviewers.add(pubName);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return reviewers;
    }

    public List<String> getGameNames()
    {
        List<String> games = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name FROM game;");

            while(rs.next())
            {
                String pubName = rs.getString("name");
                games.add(pubName);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return games;
    }

    public List<String> getGenreNames()
    {
        List<String> genres = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT genre FROM game;");

            while(rs.next())
            {
                String genreName = rs.getString("genre");
                genres.add(genreName);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return genres;
    }

    public List<PublisherList> getPublishers()
    {
        List<PublisherList> publishers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT publisher.name, city, state, county, AVG(rating)\n" +
                    "FROM publisher\n" +
                    "LEFT JOIN game\n" +
                    "\tON game.pub_id = publisher.pub_id\n" +
                    "LEFT JOIN review\n" +
                    "\tON game.game_id = review.game_id\n" +
                    "GROUP BY publisher.name");
            while(rs.next())
            {
                String pubName = rs.getString("name");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String country = rs.getString("county");
                Double avgRating = rs.getDouble("AVG(rating)");

                PublisherList pub = new PublisherList(pubName, city,state, country, avgRating);
                publishers.add(pub);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return publishers;
    }



    public List<ReviewerList> getReviewers()
    {
        List<ReviewerList> reviewers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT reviewer.name, AVG(rating), COUNT(rating)\n" +
                    "FROM reviewer\n" +
                    "LEFT JOIN review\n" +
                    "\tON review.reviewer_id = reviewer.reviewer_id\n" +
                    "GROUP BY reviewer.reviewer_id");
            while(rs.next())
            {
                String reviewerName = rs.getString("name");
                Double avgRating = rs.getDouble("AVG(rating)");
                Integer numReviews = rs.getInt("COUNT(rating)");

                ReviewerList rev = new ReviewerList(reviewerName,avgRating,numReviews);
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

    public void addPublisher(String name, String city, String state, String country)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("INSERT INTO publisher(name, city, state, county) VALUES(?,?,?,?);");
            pstmt.setString(1,name);
            pstmt.setString(2,city);
            pstmt.setString(3,state);
            pstmt.setString(4,country);

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

    public void addReviewer(String name)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("INSERT INTO reviewer(name) VALUES(?);");
            pstmt.setString(1,name);

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

    public void addReview(String reviewerName, String gameName, Double score, String comment)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("INSERT INTO review (reviewer_id, game_id, rating, comment)\n" +
                    "VALUES(" +
                    "(SELECT reviewer_id FROM reviewer WHERE reviewer.name = ?), " +
                    "(SELECT game_id FROM game WHERE game.name = ?)," +
                    "?,?);");
            pstmt.setString(1, reviewerName);
            pstmt.setString(2, gameName);
            pstmt.setDouble(3, score);
            pstmt.setString(4, comment);

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

    public List<GameReviewsInfo> viewGameReviews(String name)
    {
        List<GameReviewsInfo> gameReviews = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("SELECT reviewer.name, rating, comment\n" +
                    "FROM reviewer\n" +
                    "JOIN review\n" +
                    "ON reviewer.reviewer_id = review.reviewer_id\n" +
                    "JOIN game\n" +
                    "ON review.game_id = game.game_id\n" +
                    "WHERE game.name = ?");
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String reviewerName = rs.getString("name");
                Double rating = rs.getDouble("rating");
                String comment = rs.getString("comment");

                GameReviewsInfo reviews = new GameReviewsInfo(reviewerName, rating, comment);
                gameReviews.add(reviews);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return gameReviews;
    }

    public List<PlatformGamesInfo> getPlatformGames(String name)
    {
        List<PlatformGamesInfo> platforms = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement
            pstmt = conn.prepareStatement("SELECT game.name FROM game \n" +
                    "JOIN plays_on \n" +
                    "ON game.game_id = plays_on.game_id\n" +
                    "JOIN platform\n" +
                    "ON platform.platform_id = plays_on.pub_id\n" +
                    "where platform.name = ?;");
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String platName = rs.getString("name");
                PlatformGamesInfo plat = new PlatformGamesInfo(platName);
                platforms.add(plat);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return platforms;
    }

    public List<PublisherInfo> getPublisherGames(String name)
    {
        List<PublisherInfo> games = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("SELECT game.name, AVG(rating) \n" +
                    "FROM game\n" + "JOIN review\n" +
                    "ON game.game_id = review.game_id\n" +
                    "WHERE game.pub_id = (SELECT pub_id \n" +
                    "FROM publisher\n" +
                    "WHERE publisher.name = ?)\n" +
                    "GROUP BY game.name;");
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String gameName = rs.getString("name");
                Double avgRating = rs.getDouble("AVG(rating)");

                PublisherInfo pub = new PublisherInfo(gameName,avgRating);
                games.add(pub);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return games;
    }


    public List<ReviewerInfo> getReviewerInfo(String reviewerName)
    {
        List<ReviewerInfo> reviewer = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("SELECT game.name, ANY_VALUE(review.comment) as review, review.rating FROM reviewer \n" +
                    "JOIN review ON reviewer.reviewer_id = review.reviewer_id\n" +
                    "JOIN game on game.game_id = review.game_id\n" +
                    "WHERE reviewer.name = ?;");
            pstmt.setString(1, reviewerName);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String gameName = rs.getString("name");
                String review = rs.getString("review");
                Double rating = rs.getDouble("rating");

                ReviewerInfo reviewerInfo = new ReviewerInfo(gameName,review,rating);
                reviewer.add(reviewerInfo);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return reviewer;
    }

    public List<GenreInfo> getGenreInfo(String genreName)
    {
        List<GenreInfo> genre = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("SELECT game.name, AVG(rating)\n" +
                    "FROM game\n" +
                    "JOIN review\n" +
                    "ON game.game_id = review.game_id\n" +
                    "WHERE genre = ?\n" +
                    "GROUP BY game.name;");
            pstmt.setString(1, genreName);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String gameName = rs.getString("name");
                Double avgRating = rs.getDouble("AVG(rating)");

                GenreInfo genreInfo = new GenreInfo(gameName,avgRating);
                genre.add(genreInfo);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return genre;
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
