package src;

import com.mysql.cj.protocol.Resultset;

import java.sql.*;

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

    public DBObject getGames()
    {
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT game.name, genre, release_year, publisher.name, AVG(rating)\n" +
                    "FROM game\n" +
                    "JOIN publisher\n" +
                    "\tON game.pub_id = publisher.pub_id\n" +
                    "JOIN review\n" +
                    "\tON game.game_id = review.game_id\n" +
                    "GROUP BY game.name");
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        DBObject res = new DBObject(rs,null);
        return res;
    }

    public DBObject getPublishers()
    {
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
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        DBObject res = new DBObject(rs,null);
        return res;
    }

    public DBObject getReviewers()
    {
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT reviewer.name, AVG(rating), COUNT(rating)\n" +
                    "FROM reviewer\n" +
                    "JOIN review\n" +
                    "\tON review.reviewer_id = reviewer.reviewer_id\n" +
                    "GROUP BY reviewer.reviewer_id");
        } catch(SQLException e)
        {
            e.printStackTrace();
        }

        DBObject res = new DBObject(rs,null);
        return res;
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
