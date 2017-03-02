import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by alex on 2/14/2017.
 */
public class RegisterServlet extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(RegisterServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getSession().setAttribute("name", request.getParameter("username"));
        request.getSession().setAttribute("gender", "male");
        String message;
        Connection conn = null;
        Statement stmt = null;
        PrintWriter out = null;
        try
        {
            // Set response type
            response.setContentType("text/html");

            // Get PrintWriter from response
            out = response.getWriter();

            // Check for valid fields
            if (request.getParameter("username").equals("") || request.getParameter("password").equals("") || request.getParameter("re_password").equals(""))
            {
                message = "          <p style=color:#ff0033> Please fill out all fields. </p>";
                createMessage(message, out);
                logger.warn("User did not fill out all fields.");
                return;
            }
            else if (!request.getParameter("password").equals(request.getParameter("re_password")))
            {
                message = "          <p style=color:#ff0033> Password fields do not match. </p>";
                createMessage(message, out);
                logger.warn("User's password fields do not match.");
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
            String sqlStr = "select username from users;";
            ResultSet resultSet = stmt.executeQuery(sqlStr);

            // Compare username's to posted request
            while (resultSet.next())
            {
                if (resultSet.getString("username").equalsIgnoreCase(request.getParameter("username")))
                {
                    logger.warn(resultSet.getString("username") + " already exists in USERS table.");
                    // Print an HTML page as the output
                    message = "          <p style=color:#ff0033> Username is already taken. :/ </p>";
                    createMessage(message, out);
                    return;
                }
            }

            sqlStr = "insert into Users values ('" + request.getParameter("username") + "', '" + request.getParameter("password") + "', NULL, NULL, NULL, 'male', 0);";
            if (!stmt.execute(sqlStr))
            {
                logger.info("Added " + request.getParameter("username") + " to USERS table.");
                // Print an HTML page as the output
                message = "          <p style=color:#66CD00> User added. Welcome, " + request.getParameter("username") + "! :D</p>";
                createMessage(message, out);
            }
            else
                logger.error("Failed to add " + request.getParameter("username") + " to USERS table.");

        } catch (InstantiationException ex) {
            logger.error("Error: unable to instantiate driver class!");
        } catch (IllegalAccessException ex) {
            logger.error("Error: unable to access driver class!");
        } catch (ClassNotFoundException ex) {
            logger.error("Error: Class not found");
        }catch (SQLException ex) {
            logger.error(ex.getSQLState());
        } finally {
            System.out.close();  // Close the output writer
            try {
                // Close the resources
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                if (out != null) out.close();
            } catch (SQLException ex) {
                logger.error(ex.getSQLState());
            }
        }
    }

    private void createMessage(String msg, PrintWriter out)
    {
        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en-US\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Welcome to Generic Social App</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Welcome to Generic Social App!</h1>" +
                "    <form action=\"/register\" method=\"post\">\n" +
                msg +
                "          Username: <input type=\"text\" name=\"username\"><br>\n" +
                "          Password: <input type=\"password\" name=\"password\"><br>\n" +
                "          Re-type Password: <input type=\"password\" name=\"re_password\"><br>\n" +
                "          <input type=\"submit\" value=\"Register\">\n" +
                "    </form>\n" +
                "<p>Already have friends here? Search <a href=\"search.html\">here</a>.</p>\n" +
                "</body>\n" +
                "</html>");
    }
}
