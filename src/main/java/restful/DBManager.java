package restful;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by alex on 3/12/2017.
 */
public class DBManager
{
    /**
     * Generic SQL execute query method.
     * @param sql
     *  Query command to execute.
     */
    public void connect(String sql) {
        // Simple but necessary try-catch statement
        try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
             Statement state = con.createStatement()) {
            state.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all current users in the database
     * @param conn
     *  Connection to the database
     * @return
     *  ArrayList of all users
     */
    public static ArrayList<String> getUsers(Connection conn)
    {
        ArrayList<String> users = new ArrayList<>();
        try (Statement stmt = conn.createStatement())
        {
            String sqlStr = "select * from users;";
            ResultSet resultSet = stmt.executeQuery(sqlStr);
            while (resultSet.next())
                users.add(resultSet.getString("username"));
            resultSet.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Gets access to PreparedStatement for pulling stats for any user
     * @param conn
     *  Connection to the database that this statement will be prepared for
     * @return
     *  PreparedStatement that requires username to pull stats for
     */
    public static PreparedStatement getStatsStatement(Connection conn)
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement("SELECT login FROM users WHERE username=?");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return stmt;
    }

    /**
     * Private constructor for all static method class
     */
    private DBManager()
    {
        throw new AssertionError("Private constructor cannot be called.");
    }
}
