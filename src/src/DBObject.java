package src;

import java.sql.*;

public class DBObject
{
    ResultSet rs;
    PreparedStatement pstmt;
    ResultSetMetaData rsmd;

    public DBObject(ResultSet rs, PreparedStatement pstmt)
    {
        try
        {
            this.rs = rs;
            this.pstmt = pstmt;
            this.rsmd = rs.getMetaData();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet getRs()
    {
        return rs;
    }

    public PreparedStatement getPstmt()
    {
        return pstmt;
    }

    public ResultSetMetaData getRsmd()
    {
        return rsmd;
    }
}
