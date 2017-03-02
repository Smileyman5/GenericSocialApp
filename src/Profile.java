import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Jonathan Baker
 */
@WebServlet("/profile")
public class Profile extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		Statement state = null;
		String username = (String) request.getSession().getAttribute("username");
		
		try {
			//create connection to database
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "smileyman5", "password");
			state = con.createStatement();
			
			//collect all confirmed friends
			ResultSet set = state.executeQuery("SELECT friend FROM Friends WHERE username='" + username + "' and status='Confirmed';");
			ArrayList<String> conFriends = buildList(set, "friend");
			request.setAttribute("conFriends", conFriends);
			
			//collect all pending friend requests made by user
			set = state.executeQuery("SELECT friend FROM Friends WHERE username='" + username + "' and status='Pending';");
			ArrayList<String> penFriends = buildList(set, "friend");
			request.setAttribute("penFriends", penFriends);
			
			//collect all friend requests from other users that are requesting user
			set = state.executeQuery("SELECT username FROM Friends WHERE friend='" + username + "' and status='Pending';");
			ArrayList<String> reqFriends = buildList(set, "username");
			request.setAttribute("reqFriends", reqFriends);
			
			//collect request for user search
            ResultSet resultSet = state.executeQuery("SELECT username FROM Users WHERE username!='" + username + "';");
            // Display users with partial or full match
            String name = request.getParameter("user");
            ArrayList<String> message = null;
            if (name != null) {
            	message = new ArrayList<String>();
            	String result;
            	while (!name.equals("") && resultSet.next()) {
            		result = resultSet.getString("username");
            		if (result.startsWith(name))
            			message.add(result);
            	}
            }
            request.setAttribute("search", message);
            
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
		getServletContext().getRequestDispatcher("/profile.jsp").forward(request, response);
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
