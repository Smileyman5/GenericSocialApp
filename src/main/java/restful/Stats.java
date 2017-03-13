package restful;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Servlet responsible for controlling or reading statistics for individual users or all users
 */
public class Stats extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(Stats.class);

    /**
     * Broken into two parts.
     * 1. If the request is for the general stats gather all users login statistics and return them
     * as a JSONObject with Keys mapped to username and Values mapped to number of times that user has logged in.
     * 2. If the request's path includes a username it will return a JSONObject with that user's username and number of
     * times that user has logged in.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    if (request.getPathInfo() == null)
	    {
            // Simple and unavoidable try catch statement
            try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
                 Statement stmt = conn.createStatement())
            {
                ResultSet resultSet = stmt.executeQuery("Select username, login from users");
                JsonObject result = new JsonObject();
                while (resultSet.next())
                    result.addProperty(resultSet.getString("username"), resultSet.getInt("login"));

                response.setContentType("application/json");
                response.getWriter().write(result.toString());
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            String[] paths = PathSplitter.getPaths(request.getPathInfo());
            if (paths.length != 1)
                logger.warn("Path has more info than expected. (" + request.getPathInfo() + ") Ignoring all but first.");
            // Simple and unavoidable try catch statement
            try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password"))
            {
                ArrayList<String> allUsers = DBManager.getUsers(conn);
                if (!allUsers.contains(paths[0]))
                {
                    logger.error("User " + paths[0] + " was not found in the database!");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                PreparedStatement stmt = DBManager.getStatsStatement(conn);
                stmt.setString(1, paths[0]);
                StringBuilder builder = new StringBuilder();
                ResultSet set = stmt.executeQuery();
                while (set.next())
                    builder.append(set.getString(1));
                set.close();
                JsonObject result = new JsonObject();
                result.addProperty(paths[0], builder.toString());

                response.setContentType("application/json");
                response.getWriter().write(result.toString());
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
	}

    /**
     * Put will expect a username followed by a number indicating what the login stat will
     * be updated too in the path. Payload ignored for this method.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getPathInfo() == null)
        {
            logger.error("Put does not accept vanilla url path.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Put does not accept vanilla url path.");
            return;
        }

        String[] paths = PathSplitter.getPaths(request.getPathInfo());
        if (paths.length != 2)
        {
            logger.error("Put must have stats/<username>/<int> at the end of it's URL.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Put must have stats/<username>/<int> at the end of it's URL.");
            return;
        }

        // Simple and unavoidable try catch statement
        try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
             Statement stmt = conn.createStatement())
        {
            ArrayList<String> allUsers = DBManager.getUsers(conn);
            if (!allUsers.contains(paths[0]))
            {
                logger.error("User " + paths[0] + " was not found in the database!");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User " + paths[0] + " was not found in the database!");
                return;
            }

            String sql = "UPDATE users set login = " + Integer.parseInt(paths[1]) + " where username = '" + paths[0] + "'";
            if (stmt.execute(sql))
            {
                logger.error("Query to database failed.");
                response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Query to database failed.");
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Delete clears the login stats of the given user to zero.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getPathInfo() == null)
        {
            logger.error("Put does not accept vanilla url path.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Put does not accept vanilla url path.");
            return;
        }

        String[] paths = PathSplitter.getPaths(request.getPathInfo());
        if (paths.length != 1)
        {
            logger.error("Put must have stats/<username> at the end of it's URL.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Put must have stats/<username> at the end of it's URL.");
            return;
        }

        // Simple and unavoidable try catch statement
        try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
             Statement stmt = conn.createStatement())
        {
            ArrayList<String> allUsers = DBManager.getUsers(conn);
            if (!allUsers.contains(paths[0]))
            {
                logger.error("User " + paths[0] + " was not found in the database!");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User " + paths[0] + " was not found in the database!");
                return;
            }

            String sql = "UPDATE users set login = 0 where username = '" + paths[0] + "'";
            stmt.execute(sql);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
