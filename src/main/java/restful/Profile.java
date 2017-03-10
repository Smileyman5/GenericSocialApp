package restful;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Jonathan Baker
 */
@WebServlet("/profile")
public class Profile extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		Statement state = null;
		String username = request.getParameter("username");
		
		try {
			//create connection to database
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "root", "");
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
