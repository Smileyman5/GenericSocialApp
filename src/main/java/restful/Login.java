package restful;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


/**
 * @author Jonathan Baker
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	 throws IOException, ServletException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String result = "";
	
		if (username == null && password == null) {
		}
		else if ((username = htmlFilter(username.trim())).length() == 0
				|| (password = htmlFilter(password.trim())).length() == 0) {
			result = "criteria";
		}
		else if (login(username, password)) {
			result = "loggedin";
		}
		else {
			result = "failed";
		}
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(result));
	}
	 
	   // Filter the string for special HTML characters to prevent
	   // command injection attack
	   private static String htmlFilter(String message) {
	      if (message == null) return null;
	      int len = message.length();
	      StringBuffer result = new StringBuffer(len + 20);
	      char aChar;
	 
	      for (int i = 0; i < len; ++i) {
	         aChar = message.charAt(i);
	         switch (aChar) {
	             case '<': result.append("&lt;"); break;
	             case '>': result.append("&gt;"); break;
	             case '&': result.append("&amp;"); break;
	             case '"': result.append("&quot;"); break;
	             default: result.append(aChar);
	         }
	      }
	      return (result.toString());
	   }
	   
	   private boolean login (String username, String password) {
		   Connection con = null;
		   Statement state = null;
		   try {
			   Class.forName("com.mysql.jdbc.Driver");
			   con = DriverManager.getConnection("jdbc:mysql://localhost:3306/social_data?useSSL=false", "root", "");
			   state = con.createStatement();
			   ResultSet set = state.executeQuery("SELECT * FROM Users WHERE BINARY username='" + username + "' AND BINARY password='" + password + "'");
			   return set.next();
		   } catch (SQLException | ClassNotFoundException e) {
			   e.printStackTrace();
		   } finally {	
			   try {
				if (state != null) { state.close(); }
				if (con != null) { con.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			   
		   }
		   return false;
	   }
}
