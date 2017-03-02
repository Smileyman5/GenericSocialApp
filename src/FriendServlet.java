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

            if (session.isNew() || session.getAttribute("name") == null)
            {
                logger.info("No one is signed in. Suggesting redirection to register page.");
                createMessage("<p style=color:red>Please register first. Go <a href=\"index.jsp\">here</a>.</p>\n", out);
                return;
            }

            // Connect to JDBC Driver
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            // Allocate a database Connection object
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");

            // Allocate a Statement object within the Connection
            stmt = conn.createStatement();

            // Execute a SQL select query
            String sqlStr = "select username, gender from users;";
            ResultSet resultSet = stmt.executeQuery(sqlStr);

            // Display users with partial or full match
            String name = (String) session.getAttribute("name");
            StringBuilder message = new StringBuilder("<ul>");
            String result;
            String g2 = (String) session.getAttribute("gender");
            while (resultSet.next())
            {
                String g1 = resultSet.getString("gender");
                result = resultSet.getString("username");
                if ((result.contains(name) || name.contains(result) || g1.equals(g2)) && !result.equals(name))
                    message.append("<li>").append(result).append("</li>");
            }
            message.append("</ul>");
            createMessage(message.toString(), out);

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

    private void createMessage(String msg, PrintWriter out)
    {
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Friend Recommendations</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Friend Recommendations</h1>\n" +
                "<form action=\"/friends\" method=\"get\">\n" +
                "      <input type=\"submit\" value=\"Recommend\">\n" +
                "</form>\n" +
                "<p>Already have friends here? Search <a href=\"search.jsp\">here</a>.</p>" +
                 msg +
                "</body>\n" +
                "</html>");
    }
}