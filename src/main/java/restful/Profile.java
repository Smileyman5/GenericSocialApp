package restful;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Jonathan Baker
 */
public class Profile extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(Profile.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		Statement state = null;
		String username = request.getParameter("username");
		
		try {
			//create connection to database
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
			state = con.createStatement();
			
			//collect all confirmed friends
			ResultSet set = state.executeQuery("SELECT friend FROM Friends WHERE username='" + username + "' and status='Confirmed';");
			ArrayList<String> conFriends = buildList(set, "friend");

			//collect all pending friend requests made by user
			set = state.executeQuery("SELECT friend FROM Friends WHERE username='" + username + "' and status='Pending';");
			ArrayList<String> penFriends = buildList(set, "friend");
			
			//collect all friend requests from other users that are requesting user
			set = state.executeQuery("SELECT username FROM Friends WHERE friend='" + username + "' and status='Pending';");
			ArrayList<String> reqFriends = buildList(set, "username");
			
            // Display users with partial or full match
            String user = request.getParameter("user");
			
			//collect request for user search where the username is not the same as the
			//logged in user and starts with the requested user name
			ResultSet resultSet = state.executeQuery("SELECT username FROM Users WHERE username!='" + username + "' and username like '" + user + "%';");
			ArrayList<String> message = buildList(resultSet, "username");
            request.setAttribute("search", message);
            
			JsonObject obj = new JsonObject();
			JsonArray conF = new JsonArray();
			for (String name: conFriends) {
				conF.add(name);
			}
			obj.add("conFriends", conF);
			
			JsonArray penF = new JsonArray();
			for (String name: penFriends) {
				conF.add(name);
			}
			obj.add("penFriends", penF);
			
			JsonArray reqF = new JsonArray();
			for (String name: reqFriends) {
				conF.add(name);
			}
			obj.add("reqFriends", reqF);
			
			JsonArray mes = new JsonArray();
			for (String name: message) {
				conF.add(name);
			}
			obj.add("search", mes);
			
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(new Gson().toJson(obj));
            
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {	
			//close connection to the database
			try {
				if (state != null) { state.close(); }
				if (con != null) { con.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}	   
		}
	}

	/**
	 * Put accepts a payload and a URL path with a username appended to the end. Will add user is
	 * they do not exists or will update their data with the payload if they do. (JSONObject expected)
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
		if (paths.length != 1)
		{
			logger.error("Put must have profile/<username> at the end of it's URL.");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Put must have profile/<username> at the end of it's URL.");
			return;
		}

		// Simple and unavoidable try catch statement
		try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password"))
		{
			ArrayList<String> allUsers = DBManager.getUsers(conn);
			if (!allUsers.contains(paths[0]))
			{
				logger.info("Creating new user: "+ paths[0]);
                Statement stmt = conn.createStatement();
                stmt.execute("insert into users values ('" + paths[0] + "', 'password', NULL, NULL, NULL, NULL, 0);");
			    stmt.close();
			}
			else
				logger.info("Updating existing user: " + paths[0]);

            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
            reader.close();
            JsonObject data = (JsonObject) new JsonParser().parse(builder.toString());

            PreparedStatement pstmt = conn.prepareStatement("UPDATE users set birthday = ? where username = '" + paths[0] + "'");
            runPrep(data, pstmt, "birthday");
            pstmt = conn.prepareStatement("UPDATE users set first_name = ? where username = '" + paths[0] + "'");
            runPrep(data, pstmt, "firstName");
            pstmt = conn.prepareStatement("UPDATE users set last_name = ? where username = '" + paths[0] + "'");
            runPrep(data, pstmt, "lastName");
            pstmt = conn.prepareStatement("UPDATE users set gender = ? where username = '" + paths[0] + "'");
            runPrep(data, pstmt, "gender");
            pstmt.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

    /**
     * Delete removes the user given in the URL path
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
            logger.error("Delete does not accept vanilla url path.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Delete does not accept vanilla url path.");
            return;
        }

        String[] paths = PathSplitter.getPaths(request.getPathInfo());
        if (paths.length != 1)
        {
            logger.error("Delete must have profile/<username> at the end of it's URL.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Delete must have profile/<username> at the end of it's URL.");
            return;
        }

        // Simple and unavoidable try catch statement
        try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password"))
        {
            ArrayList<String> allUsers = DBManager.getUsers(conn);
            if (!allUsers.contains(paths[0]))
            {
                logger.info("User \"" + paths[0] + "\" already removed or never existed");
                return;
            }

            logger.info("Removing user: " + paths[0]);

            Statement stmt =  conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("Select * from users where username = '" + paths[0] + "'");
            JsonObject userData = new JsonObject();
            if (resultSet != null)
            {
                resultSet.next();
                userData.addProperty("username", resultSet.getString(1));
                userData.addProperty("password", "n/a");
                userData.addProperty("birthday", resultSet.getString(3));
                userData.addProperty("first_name", resultSet.getString(4));
                userData.addProperty("last_name", resultSet.getString(5));
                userData.addProperty("gender", resultSet.getString(6));
                userData.addProperty("login", resultSet.getInt(7));
            }

            stmt.execute("Delete from users where username = '" + paths[0] + "'");
            stmt.close();

            response.setContentType("application/json");
            response.getWriter().write(userData.toString());

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void runPrep(JsonObject data, PreparedStatement pstmt, String s)
    {
        try
        {
            if (data.get(s) != null)
            {
                pstmt.setString(1, data.get(s).getAsString());
                pstmt.execute();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<String> buildList(ResultSet set, String value) {
		ArrayList<String> list = null;
		try {
			list = new ArrayList<String>();
			while (set.next()) {
				list.add(set.getString(value));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}

}
