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

    /**
     * Get a list of all games in DB containing the name, genre, release year, publisher name and average rating of each game
     * @return list of all games in DB
     */
    public List<GameList> getGames()
    {
        List<GameList> games = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("""
                    SELECT game.name, genre, release_year, publisher.name, AVG(rating)
                    FROM game
                    JOIN publisher
                    \tON game.pub_id = publisher.pub_id
                    LEFT JOIN review
                    \tON game.game_id = review.game_id
                    GROUP BY game.name""");

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

    /**
     * Get a list of all publisher names in the DB
     * @return list of publisher names
     */
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

    /**
     * Get a list of all platform names in DB
     *
     * @return list of platform names
     */
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

    /**
     * Get a list of all reviewer names in DB
     *
     * @return list of reviewer names
     */
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

    /**
     * Get a list of all game names in DB
     *
     * @return list of game names
     */
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

    /**
     * Get a list of all genre names in DB
     *
     * @return list of genre names
     */
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

    /**
     * Get a list of all publishers in DB containing their name, city, state, country, and average rating for all games
     * @return list of all publishers in DB
     */
    public List<PublisherList> getPublishers()
    {
        List<PublisherList> publishers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("""
                    SELECT publisher.name, city, state, county, AVG(rating)
                    FROM publisher
                    LEFT JOIN game
                    \tON game.pub_id = publisher.pub_id
                    LEFT JOIN review
                    \tON game.game_id = review.game_id
                    GROUP BY publisher.name""");
            while(rs.next())
            {
                String pubName = rs.getString("name");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String country = rs.getString("county");
                Double avgRating = rs.getDouble("AVG(rating)");

                PublisherList pub = new PublisherList(pubName, city, state, country, avgRating);
                publishers.add(pub);
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return publishers;
    }

    /**
     * Get a list of all reviewers in DB conatining their name, avergae rating and number of ratings
     * @return list of all reviewers
     */
    public List<ReviewerList> getReviewers()
    {
        List<ReviewerList> reviewers = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("""
                    SELECT reviewer.name, AVG(rating), COUNT(rating)
                    FROM reviewer
                    LEFT JOIN review
                    \tON review.reviewer_id = reviewer.reviewer_id
                    GROUP BY reviewer.reviewer_id""");
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

    /**
     * Add a game to the DB
     * @param pubName publisher of game
     * @param genre genre of game
     * @param gameName name of game
     * @param release_year release year of game
     */
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

    /**
     * Add a publisher to DB
     *
     * @param name of publisher
     * @param city of publisher
     * @param state of publisher
     * @param country of publisher
     */
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

    /**
     * Add reviewer to DB
     * @param name of reviewer
     */
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

    /**
     * Add review to DB
     * @param reviewerName reviewer
     * @param gameName game reviewed
     * @param score score of game
     * @param comment about game
     */
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

    /**
     * Get a list of all reviews for a given game
     *
     * @param name of game
     * @return list of all reviews
     */
    public List<GameReviewsInfo> viewGameReviews(String name)
    {
        List<GameReviewsInfo> gameReviews = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("""
                    SELECT reviewer.name, rating, comment
                    FROM reviewer
                    JOIN review
                    ON reviewer.reviewer_id = review.reviewer_id
                    JOIN game
                    ON review.game_id = game.game_id
                    WHERE game.name = ?""");
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

    /**
     * Get a list of all platforms a game is played on
     * @param name of game
     * @return list of all platforms a game is played on
     */
    public List<GamePlatformInfo> getGamePlatforms(String name)
    {
        List<GamePlatformInfo> platforms = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("""
                    SELECT platform.name\s
                    FROM platform
                    JOIN plays_on
                    ON platform.platform_id = plays_on.pub_id
                    JOIN game
                    ON plays_on.game_id = game.game_id
                    WHERE game.name = ?;""");
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String platName = rs.getString("name");
                GamePlatformInfo plat = new GamePlatformInfo(platName);
                platforms.add(plat);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return platforms;
    }

    /**
     * Get a list of all games on a specified platform
     *
     * @param name of platform
     * @return list of games
     */
    public List<PlatformGamesInfo> getPlatformGames(String name)
    {
        List<PlatformGamesInfo> platforms = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("""
                    SELECT game.name FROM game\s
                    JOIN plays_on\s
                    ON game.game_id = plays_on.game_id
                    JOIN platform
                    ON platform.platform_id = plays_on.pub_id
                    where platform.name = ?;""");
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

    /**
     * Get a list of all games and their average rating by a specified publisher
     *
     * @param name of publisher
     * @return list of all games and their average rating
     */
    public List<PublisherInfo> getPublisherGames(String name)
    {
        List<PublisherInfo> games = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("""
                    SELECT game.name, AVG(rating)\s
                    FROM game
                    JOIN review
                    ON game.game_id = review.game_id
                    WHERE game.pub_id = (SELECT pub_id\s
                    FROM publisher
                    WHERE publisher.name = ?)
                    GROUP BY game.name;""");
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

    /**
     * Get a list of all reviews from a specified reviewer
     * @param reviewerName name of reviewer
     * @return list of all reviews
     */
    public List<ReviewerInfo> getReviewerInfo(String reviewerName)
    {
        List<ReviewerInfo> reviewer = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("""
                    SELECT game.name, ANY_VALUE(review.comment) as review, review.rating FROM reviewer\s
                    JOIN review ON reviewer.reviewer_id = review.reviewer_id
                    JOIN game on game.game_id = review.game_id
                    WHERE reviewer.name = ?;""");
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

    /**
     * Return a list of all games of a certain genre in the DB
     *
     * @param genreName Genre of games we wish to view
     * @return list of all games of a certain genre
     */
    public List<GenreInfo> getGenreInfo(String genreName)
    {
        List<GenreInfo> genre = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("""
                    SELECT game.name, AVG(rating)
                    FROM game
                    JOIN review
                    ON game.game_id = review.game_id
                    WHERE genre = ?
                    GROUP BY game.name;""");
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

    /**
     * Get the genre from a specified game
     * @param name of game
     * @return genre of game
     */
    public String getGameGenre(String name)
    {
        String genre = null;
        System.out.println(name);
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("SELECT genre FROM game WHERE game.name = ?;");
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();
            while(rs.next())
            {
                genre = rs.getString(1);
            }

        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return genre;

    }

    /**
     * Edit the name of a game in the DB
     * @param oldName old name of game
     * @param newName new name of game
     */
    public void editGameName(String oldName, String newName)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("UPDATE game SET game.name = ? WHERE (game.name =? AND game_id <> 0);");
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Edit the genre of a game
     * @param gameName name of game to be edited
     * @param oldGenre old genre of the game
     * @param newGenre new genre of the game
     */
    public void editGameGenre(String gameName, String oldGenre, String newGenre)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("UPDATE game SET game.genre = ? WHERE (game.genre =? AND game.name = ? AND game_id <> 0);");
            pstmt.setString(1, newGenre);
            pstmt.setString(2, oldGenre);
            pstmt.setString(3,gameName);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Remove a game from the DB
     * @param name name of game to be removed
     */
    public void removeGame(String name)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("DELETE FROM game WHERE (game.name = ? AND game_id <> 0);");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateGame(String oldName, String newName, String newGenre)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);
            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("UPDATE game SET name = ?, genre = ? WHERE (name = ? AND game_id <> 0);");
            pstmt.setString(1, newName);
            pstmt.setString(2, newGenre);
            pstmt.setString(3,oldName);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updatePublisher(String pubName, String newName, String newCity, String newState, String newCountry)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("UPDATE publisher " +
                    "SET name = ?," +
                    "city = ?," +
                    "state = ? ," +
                    "county = ? " +
                    "WHERE (name = ? AND pub_id <> 0);");
            pstmt.setString(1, newName);
            pstmt.setString(2, newCity);
            pstmt.setString(3,newState);
            pstmt.setString(4,newCountry);
            pstmt.setString(5,pubName);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateReviewer(String oldName, String newName)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("UPDATE reviewer SET name = ? WHERE (name = ? AND reviewer_id <> 0);");
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<String> gamesReviewed(String reviewerName)
    {
        List<String> games = new ArrayList<>();
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("SELECT game.name\n" +
                    "FROM review\n" +
                    "JOIN game\n" +
                    "ON game.game_id = review.game_id\n" +
                    "JOIN reviewer\n" +
                    "ON reviewer.reviewer_id = review.reviewer_id\n" +
                    "WHERE reviewer.name = ?;");
            pstmt.setString(1, reviewerName);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                String gameName = rs.getString("name");
                games.add(gameName);
            }

        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        return games;
    }

    public void updateReview(String gameName, String reviewerName, int newScore, String newComment)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("UPDATE review SET rating = ?, comment = ? " +
                    "WHERE (reviewer_id = " +
                    "(SELECT reviewer_id FROM reviewer WHERE name = ?) " +
                    "AND game_id = (SELECT game_id FROM game WHERE name = ?));");
            pstmt.setInt(1, newScore);
            pstmt.setString(2, newComment);
            pstmt.setString(3, reviewerName);
            pstmt.setString(4, gameName);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removePublisher(String name)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("DELETE FROM publisher WHERE (publisher.name = ? AND pub_id <> 0);");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removeReviewer(String name)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("DELETE FROM reviewer WHERE (reviewer.name = ? AND reviewer_id <> 0);");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removeReview(String reviewerName, String gameName)
    {
        try
        {
            //Do not commit to the database until specified
            conn.setAutoCommit(false);

            //prepared statement to update the actors of a specified film
            pstmt = conn.prepareStatement("DELETE from review WHERE reviewer_id = " +
                    "(SELECT reviewer_id FROM reviewer WHERE name = ?) " +
                    "AND game_id = (SELECT game_id FROM game WHERE name = ?);");
            pstmt.setString(1, reviewerName);
            pstmt.setString(2, gameName);
            pstmt.executeUpdate();
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
