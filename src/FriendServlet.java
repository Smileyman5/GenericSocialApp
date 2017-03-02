import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by alex on 2/18/2017.
 */
public class FriendServlet extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(FriendServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        Connection conn = null;
        Statement stmt = null;
        PrintWriter out = null;
        try
        {
            // Set response type
            response.setContentType("text/html");

            // Get PrintWriter from response
            out = response.getWriter();

            if (session.isNew() || session.getAttribute("username") == null)
            {
                logger.info("No one is signed in. Suggesting redirection to register page.");
                request.setAttribute("message", "<p style=color:red>Please sign-in/register first. Go <a href=\"register.jsp\">here</a>.</p>\n");
                request.getRequestDispatcher("friends.jsp").include(request, response);
                return;
            }

            // Connect to JDBC Driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // Allocate a database Connection object
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");

            // Allocate a Statement object within the Connection
            stmt = conn.createStatement();

            String name = (String) session.getAttribute("username");
            // SQL query string -- grabbing user's friends
            String sqlStr = "select * from friends where username='" + name + "';";
            ResultSet resultSet = stmt.executeQuery(sqlStr);

            // User's friends
            ArrayList<String> friends = new ArrayList<>();
            while (resultSet.next())
                friends.add(resultSet.getString("friend"));
            resultSet.close();

            String result;
            int count = 0;
            // Display users with partial or full match
            StringBuilder message = new StringBuilder("<ul>");
            for (String username: friends)
            {
                // SQL query string -- grabbing friends of user's friends max 5
                sqlStr = "select * from friends where username='" + username + "';";
                ResultSet rs = stmt.executeQuery(sqlStr);
                while (rs.next())
                {
                    result = rs.getString("friend");
                    if (!result.equals(name))
                    {
                        message.append("<li>").append(result).append("<br/>").append(formRequest(result)).append("</li>");
                        if (count++ >= 5)
                        {
                            message.append("</ul>");
                            request.setAttribute("message", message.toString());
                            request.getRequestDispatcher("friends.jsp").include(request, response);
                            return;
                        }
                    }
                }
                rs.close();
            }
            message.append("</ul>");

            if (count == 0)
                message = new StringBuilder("No friends to recommend :/");
            request.setAttribute("message", message.toString());
            request.getRequestDispatcher("friends.jsp").include(request, response);

        } catch (InstantiationException ex)
        {
            logger.error("Error: unable to instantiate driver class!");
        } catch (IllegalAccessException ex)
        {
            logger.error("Error: unable to access driver class!");
        } catch (ClassNotFoundException ex)
        {
            logger.error("Error: Class not found");
        } catch (SQLException ex)
        {
            logger.error(ex.getSQLState());
        } finally
        {
            System.out.close();  // Close the output writer
            try
            {
                // Close the resources
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                if (out != null) out.close();
            } catch (SQLException ex)
            {
                logger.error(ex.getSQLState());
            }
        }
    }

    private String formRequest(String result)
    {
        StringBuilder request = new StringBuilder("<form action=\"/friends\" method=\"post\">");
        request.append("<input type=\"hidden\" name=\"friend\" value=\"true\" />")
                .append("<input type=\"hidden\" name=\"name\" id=\"name\" value=\"" + result + "\" />")
                .append("<input type=\"submit\" value=\"Send Friend Request\" />")
                .append("</form>");
        return request.toString();
    }
}