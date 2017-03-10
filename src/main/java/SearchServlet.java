import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by alex on 2/18/2017.
 */
public class SearchServlet extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(SearchServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Connection conn = null;
        Statement stmt = null;
        try
        {
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

            // Display users with partial or full match
            String name = request.getParameter("name");
            StringBuilder message = new StringBuilder("<ul>");
            String result;
            while (!name.equals("") && resultSet.next())
            {
                result = resultSet.getString("username");
                if (result.startsWith(name))
                    message.append("<li>").append(result).append("</li>");
            }
            message.append("</ul>");
            request.setAttribute("message", message.toString());
            request.getRequestDispatcher("search.jsp").forward(request, response);

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
            } catch (SQLException ex) {
                logger.error(ex.getSQLState());
            }
        }
    }
}
